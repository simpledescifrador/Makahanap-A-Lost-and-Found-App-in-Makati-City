package com.makatizen.makahanap.ui.main.feed;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.airbnb.lottie.LottieAnimationView;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.pojo.FeedItem;
import com.makatizen.makahanap.ui.base.BaseFragment;
import com.makatizen.makahanap.ui.item_details.ItemDetailsActivity;
import com.makatizen.makahanap.utils.AppAlertDialog;
import com.makatizen.makahanap.utils.IntentExtraKeys;
import com.makatizen.makahanap.utils.RecyclerItemUtils;
import com.makatizen.makahanap.utils.RecyclerItemUtils.OnItemClickListener;
import com.makatizen.makahanap.utils.RequestCodes;
import java.util.List;
import javax.inject.Inject;

public class FeedFragment extends BaseFragment implements FeedMvpView, OnItemClickListener {

    @Inject
    AppAlertDialog mAppAlertDialog;

    @Inject
    FeedAdapter mFeedAdapter;

    @BindView(R.id.feed_rb_item_all)
    RadioButton mFeedRbItemAll;

    @BindView(R.id.feed_rb_item_found)
    RadioButton mFeedRbItemFound;

    @BindView(R.id.feed_rb_item_lost)
    RadioButton mFeedRbItemLost;

    @BindView(R.id.feed_rel_layout_content)
    RelativeLayout mFeedRelLayoutContent;

    @BindView(R.id.feed_rg_filter)
    RadioGroup mFeedRgFilter;

    @BindView(R.id.feed_rv_latest_reports)
    RecyclerView mFeedRvLatestReports;

    @BindView(R.id.feed_srl_refresh_feed)
    SwipeRefreshLayout mFeedSrlRefreshFeed;

    @BindView(R.id.no_content_animation)
    LottieAnimationView mNoContentAnimation;

    @BindView(R.id.no_content_btn_report)
    Button mNoContentBtnReport;

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
    FeedMvpPresenter<FeedMvpView> mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

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
    public void hideFeedLoading() {
        mFeedSrlRefreshFeed.setRefreshing(false);
    }

    @Override
    public void noFeedContent() {
        mFeedRelLayoutContent.setVisibility(View.VISIBLE);
        mFeedRgFilter.setVisibility(View.GONE);
        mFeedRvLatestReports.setVisibility(View.GONE);
        mNoContentLayout.setVisibility(View.VISIBLE);

        mNoContentTitle.setText(getResources().getString(R.string.title_no_feed_content));
        mNoContentMessage.setText(getResources().getString(R.string.message_no_feed_content));
        mNoContentBtnReport.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {

            }
        });
    }

    @Override
    public void noNetworkConnection() {
//        final Dialog dialog = mAppAlertDialog.showCustomDialog(AlertType.DANGER, "No Network Connection",
//                "Please make to turn on your internet connection.", "Retry", "animations/no_internet.json");
//        dialog.setCancelable(true);
//        dialog.findViewById(R.id.custom_alert_btn_positive).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(final View view) {
//                mPresenter.loadLatestFeed();
//                dialog.dismiss();
//            }
//        });
//
//        dialog.findViewById(R.id.custom_alert_btn_close).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(final View view) {
//                dialog.dismiss();
//            }
//        });
        mFeedRelLayoutContent.setVisibility(View.VISIBLE);
        mFeedRgFilter.setVisibility(View.GONE);
        mNoInternetLayout.setVisibility(View.VISIBLE);

        mNoInternetTitle.setText(getResources().getString(R.string.title_no_network));
        mNoInternetMessage.setText(getResources().getString(R.string.message_no_network));
        mNoInternetBtnRetry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                mNoInternetLayout.setVisibility(View.GONE);
                mPresenter.loadLatestFeed();
            }
        });
    }

    @Override
    public void onFeedItemsUpdate(final List<FeedItem> feedItems) {
        mFeedRgFilter.setVisibility(View.VISIBLE);
        mFeedRelLayoutContent.setVisibility(View.VISIBLE);
        mFeedRvLatestReports.setVisibility(View.VISIBLE);
        mNoContentLayout.setVisibility(View.GONE);
        mNoInternetLayout.setVisibility(View.GONE);

        mFeedAdapter.updateFeedItemList(feedItems);
    }

    @Override
    public void onItemClicked(final RecyclerView recyclerView, final int position, final View v) {
        int itemId = mFeedAdapter.getItem(position).getItemId();
        Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
        intent.putExtra(IntentExtraKeys.ITEM_ID, itemId);
        startActivityForResult(intent, RequestCodes.ITEM_DETAILS);
    }


    @Override
    public void setFeedItem(final FeedItem feedItem) {
        mFeedRgFilter.setVisibility(View.VISIBLE);
        mFeedRelLayoutContent.setVisibility(View.VISIBLE);
        mFeedAdapter.insert(feedItem, mFeedAdapter.getItemCount());
        mFeedAdapter.feedItemsListTempHolder.add(feedItem);

    }

    @Override
    public void showFeedLoading() {
        mFeedSrlRefreshFeed.setRefreshing(true);
        mFeedRelLayoutContent.setVisibility(View.GONE);
    }

    @Override
    protected void init() {
        mFeedRvLatestReports.setAdapter(mFeedAdapter);
        mFeedRvLatestReports.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL));
        RecyclerItemUtils.addTo(mFeedRvLatestReports).setOnItemClickListener(this);
        mPresenter.loadLatestFeed();

        mFeedSrlRefreshFeed.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.refreshFeed();
                Toast.makeText(getActivity(), "Updating", Toast.LENGTH_SHORT).show();
            }
        });

        mFeedRgFilter.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final RadioGroup radioGroup, final int id) {
                switch (id) {
                    case R.id.feed_rb_item_all:
                        mFeedAdapter.getFilter().filter("All");
                        break;
                    case R.id.feed_rb_item_lost:
                        mFeedAdapter.getFilter().filter("Lost");
                        break;
                    case R.id.feed_rb_item_found:
                        mFeedAdapter.getFilter().filter("Found");
                        break;
                }
            }
        });

    }
}
