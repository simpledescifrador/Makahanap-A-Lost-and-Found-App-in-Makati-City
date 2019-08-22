package com.makatizen.makahanap.ui.main.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.pojo.Notification;
import com.makatizen.makahanap.ui.base.BaseFragment;
import com.makatizen.makahanap.ui.item_details.ItemDetailsActivity;
import com.makatizen.makahanap.utils.IntentExtraKeys;
import com.makatizen.makahanap.utils.RecyclerItemUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationFragment extends BaseFragment implements NotificationMvpView, RecyclerItemUtils.OnItemClickListener, NotificationAdapter.OnNotificationOptionListener {

    NotificationFragment.NotificationUpdateListener mNotificationUpdateListener;
    @BindView(R.id.no_content_animation)
    LottieAnimationView mNoContentAnimation;
    @BindView(R.id.no_content_btn_report)
    Button mNoContentBtnReport;
    @BindView(R.id.no_content_iv_icon)
    ImageView mNoContentIvIcon;
    @BindView(R.id.no_content_layout)
    LinearLayout mNoContentLayout;
    @BindView(R.id.no_content_message)
    TextView mNoContentMessage;
    @BindView(R.id.no_content_title)
    TextView mNoContentTitle;
    @BindView(R.id.no_internet_animation)
    LottieAnimationView mNoInternetAnimation;
    @BindView(R.id.no_internet_btn_retry)
    Button mNoInternetBtnRetry;
    @BindView(R.id.no_internet_layout)
    LinearLayout mNoInternetLayout;
    @BindView(R.id.no_internet_message)
    TextView mNoInternetMessage;
    @BindView(R.id.no_internet_title)
    TextView mNoInternetTitle;
    @Inject
    NotificationAdapter mNotificationAdapter;
    @BindView(R.id.notification_appbar)
    AppBarLayout mNotificationAppbar;
    @BindView(R.id.notification_rv_notif)
    RecyclerView mNotificationRvNotif;
    @BindView(R.id.notification_srl_refresh)
    SwipeRefreshLayout mNotificationSrlRefresh;
    @Inject
    NotificationMvpPresenter<NotificationMvpView> mPresenter;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (intent.getExtras() != null) {
                int id = intent.getIntExtra("id", 0);
                String title = intent.getStringExtra("title");
                String content = intent.getStringExtra("content");
                String imageUrl = intent.getStringExtra("image_url");
                String createdAt = intent.getStringExtra("created_at");
                boolean isViewed = intent.getBooleanExtra("viewed", false);
                String type = intent.getStringExtra("type");
                int refId = intent.getIntExtra("ref_id", 0);
                mNotificationAdapter.insert(new Notification(id, content, createdAt, imageUrl, refId, title, type, isViewed), 0);
                mNotificationRvNotif.smoothScrollToPosition(0);
                mNoInternetLayout.setVisibility(View.GONE);
                mNoContentLayout.setVisibility(View.GONE);
                mNotificationRvNotif.setVisibility(View.VISIBLE);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        getActivityFragmentComponent().inject(this);
        mPresenter.attachView(this);
        setUnBinder(ButterKnife.bind(this, view));
        init();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }

    @Override
    public void noNetworkConnection() {
        if (mNotificationAdapter.getItemCount() == 0) {
            mNotificationRvNotif.setVisibility(View.GONE);
            mNotificationSrlRefresh.setRefreshing(false);
            mNoInternetLayout.setVisibility(View.VISIBLE);
            mNoContentLayout.setVisibility(View.GONE);

            mNoInternetTitle.setText(getResources().getString(R.string.title_no_network));
            mNoInternetMessage.setText(getResources().getString(R.string.message_no_network));
            mNoInternetBtnRetry.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View view) {
                    mNoInternetLayout.setVisibility(View.GONE);
                    mPresenter.loadNotification();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Network Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void noNotification() {
        //Set No Notification Layout
        mNoContentAnimation.setAnimation("animations/no_notifications.json");

        mNoContentTitle.setText(getResources().getString(R.string.title_no_notification));
        mNoContentMessage.setText(getResources().getString(R.string.message_no_notification));
        mNoContentBtnReport.setVisibility(View.GONE);

        mNotificationSrlRefresh.setRefreshing(false);

        mNoInternetLayout.setVisibility(View.GONE);
        mNotificationRvNotif.setVisibility(View.GONE);
        mNoContentLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onUpdatedNotifications(final List<Notification> notificationList) {
        mNotificationSrlRefresh.setRefreshing(false);
        mNotificationRvNotif.setVisibility(View.VISIBLE);

        mNotificationAdapter.updateNotifications(notificationList);
        mNotificationRvNotif.smoothScrollToPosition(0);
    }

    @OnClick(R.id.no_internet_btn_retry)
    public void onViewClicked() {
    }

    @Override
    public void setNotifications(final List<Notification> notificationList) {
        mNotificationSrlRefresh.setRefreshing(false);
        mNotificationRvNotif.setAdapter(mNotificationAdapter);
        mNotificationRvNotif.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerItemUtils.addTo(mNotificationRvNotif).setOnItemClickListener(this);
        mNotificationAdapter.setData(notificationList);
        mNotificationAdapter.setNotificationOptionListener(this);
    }

    @Override
    public void onDeleteSuccess(int position) {
        mNotificationAdapter.remove(position);
    }

    @Override
    public void setNotificationUnViewed(int position) {
        mNotificationAdapter.setNotificationUnViewed(position);
    }

    @Override
    public void setNotificationViewed(int position) {
        mNotificationAdapter.setNotificationViewed(position);
    }

    @Override
    public void onUpdateNotificationBadge(int number) {
        mNotificationUpdateListener.setUnViewedNotification(number);
    }

    @Override
    protected void init() {
        mPresenter.loadNotification();
        mNotificationSrlRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getContext(), "Updating", Toast.LENGTH_SHORT).show();
                mNotificationSrlRefresh.setRefreshing(true);
                mPresenter.updateNotification();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getContext())
                .registerReceiver(mBroadcastReceiver, new IntentFilter("com.makatizen.makahanap.notification"));
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        Notification notification = mNotificationAdapter.getItem(position);
        Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(IntentExtraKeys.ITEM_ID, notification.getReferenceId());
        mPresenter.setNotificationAsViewed(String.valueOf(notification.getId()), position);
        mPresenter.updateNotificationBadge();
        startActivity(intent);
    }

    @Override
    public void onOptionDelete(int id, int position) {
        mPresenter.deleteNotification(String.valueOf(id), position);
        mPresenter.updateNotificationBadge();
    }

    @Override
    public void onOptionMarkAsUnViewed(int id, int position) {
        mPresenter.setNotificationAsUnViewed(String.valueOf(id), position);
        mPresenter.updateNotificationBadge();
    }

    @Override
    public void onOptionMarkAsViewed(int id, int position) {
        mPresenter.setNotificationAsViewed(String.valueOf(id), position);
        mPresenter.updateNotificationBadge();
    }

    public void setOnNotificationUpdateListener(NotificationUpdateListener listener) {
        mNotificationUpdateListener = listener;
    }

    public interface NotificationUpdateListener {
        void setUnViewedNotification(int number);
    }

}
