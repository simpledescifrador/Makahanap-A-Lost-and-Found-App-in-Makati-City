package com.makatizen.makahanap.ui.main.account.reports;

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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.airbnb.lottie.LottieAnimationView;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.pojo.FeedItem;
import com.makatizen.makahanap.ui.base.BaseFragment;
import com.makatizen.makahanap.ui.item_details.ItemDetailsActivity;
import com.makatizen.makahanap.utils.IntentExtraKeys;
import com.makatizen.makahanap.utils.RecyclerItemUtils;
import com.makatizen.makahanap.utils.RecyclerItemUtils.OnItemClickListener;
import com.makatizen.makahanap.utils.RequestCodes;
import java.util.List;
import javax.inject.Inject;

public class AccountReportsFragment extends BaseFragment implements AccountReportsMvpView, OnItemClickListener {

    @Inject
    AccountReportsAdapter mAccountReportsAdapter;

    @BindView(R.id.account_reports_loading_layout)
    FrameLayout mAccountReportsLoadingLayout;

    @BindView(R.id.account_reports_rv_items)
    RecyclerView mAccountReportsRvItems;

    @BindView(R.id.account_reports_srl_refresh)
    SwipeRefreshLayout mAccountReportsSrlRefresh;

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
    AccountReportsMvpPresenter<AccountReportsMvpView> mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_reports, container, false);
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
    public void noFeedContent() {
        mAccountReportsSrlRefresh.setRefreshing(false);
        mAccountReportsLoadingLayout.setVisibility(View.GONE);
        mNoContentLayout.setVisibility(View.VISIBLE);

        //Change Animation Height
        mNoContentAnimation.getLayoutParams().height = 150;
        mNoContentTitle.setTextSize(18);
        mAccountReportsRvItems.setVisibility(View.GONE);

        mNoContentTitle.setText(getResources().getString(R.string.title_account_no_feed_content));
        mNoContentMessage.setText(getResources().getString(R.string.message_no_feed_content));
        mNoContentBtnReport.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {

            }
        });
    }

    @Override
    public void noNetworkConnection() {
        mAccountReportsSrlRefresh.setRefreshing(false);
        mAccountReportsLoadingLayout.setVisibility(View.GONE);
        mAccountReportsRvItems.setVisibility(View.GONE);

        mNoInternetTitle.setTextSize(18);

        mNoInternetLayout.setVisibility(View.VISIBLE);

        mNoInternetTitle.setText(getResources().getString(R.string.title_no_network));
        mNoInternetMessage.setText(getResources().getString(R.string.message_no_network));
        mNoInternetBtnRetry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                mNoInternetLayout.setVisibility(View.GONE);
                mAccountReportsLoadingLayout.setVisibility(View.VISIBLE);
                mPresenter.loadAccountReports();
            }
        });
    }

    @Override
    public void onAccountReportUpdates(final List<FeedItem> feedItems) {
        mAccountReportsSrlRefresh.setRefreshing(false);
        mAccountReportsLoadingLayout.setVisibility(View.GONE);
        mNoContentLayout.setVisibility(View.GONE);
        mNoInternetLayout.setVisibility(View.GONE);
        mAccountReportsRvItems.setVisibility(View.VISIBLE);

        mAccountReportsAdapter.updateFeedItemList(feedItems);
        mAccountReportsRvItems.smoothScrollToPosition(0);
    }

    @Override
    public void onItemClicked(final RecyclerView recyclerView, final int position, final View v) {
        int itemId = mAccountReportsAdapter.getItem(position).getItemId();
        Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
        intent.putExtra(IntentExtraKeys.ITEM_ID, itemId);
        startActivityForResult(intent, RequestCodes.ITEM_DETAILS);
    }

    @Override
    public void setAccountReports(final List<FeedItem> feedItemList) {
        mAccountReportsAdapter.setData(feedItemList);
        mAccountReportsLoadingLayout.setVisibility(View.GONE);
        mAccountReportsRvItems.setVisibility(View.VISIBLE);
    }

    @Override
    protected void init() {
        mAccountReportsRvItems.setAdapter(mAccountReportsAdapter);
        mAccountReportsRvItems.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL));
        RecyclerItemUtils.addTo(mAccountReportsRvItems).setOnItemClickListener(this);
        mPresenter.loadAccountReports();
        mAccountReportsSrlRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getContext(), "Updating", Toast.LENGTH_SHORT).show();
                mPresenter.checkUpdateAccountReports();
            }
        });
    }
}
