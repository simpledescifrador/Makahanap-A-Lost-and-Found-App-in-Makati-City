package com.makatizen.makahanap.ui.main.feed;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedFragment extends BaseFragment implements FeedMvpView, OnItemClickListener {
    private static FeedFragment instance;
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
    LinearLayout mFeedRelLayoutContent;
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
    @Inject
    BottomSheetDialog mBottomSheetDialog1;
    @BindView(R.id.feed_post_sort)
    TextView mFeedPostSort;
    @BindView(R.id.feed_post_view)
    ImageView mFeedPostView;
    private boolean isGridView = false;

    public static FeedFragment getInstance() {
        if (instance == null) {
            instance = new FeedFragment();
        }
        return instance;

    }

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
        mFeedRelLayoutContent.setVisibility(View.VISIBLE);
        mFeedRvLatestReports.setVisibility(View.VISIBLE);
        mNoContentLayout.setVisibility(View.GONE);
        mNoInternetLayout.setVisibility(View.GONE);

        mFeedAdapter.updateFeedItemList(feedItems);
        mFeedRgFilter.check(R.id.feed_rb_item_all);
        mFeedRvLatestReports.smoothScrollToPosition(0);
        mPresenter.loadFeedState();
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
        mFeedRelLayoutContent.setVisibility(View.VISIBLE);
        mFeedAdapter.insert(feedItem, mFeedAdapter.getItemCount());
        mFeedAdapter.feedItemsListTempHolder.add(feedItem);
        mPresenter.loadFeedState();

    }

    @Override
    public void showFeedLoading() {
        mFeedSrlRefreshFeed.setRefreshing(true);
        mFeedRelLayoutContent.setVisibility(View.GONE);
    }

    @Override
    public void sortFeedByAll() {
        mFeedAdapter.getFilter().filter("All");
        mFeedPostSort.setText("All Posts");
    }

    @Override
    public void sortFeedByLost() {
        mFeedAdapter.getFilter().filter("Lost");
        mFeedPostSort.setText("Lost Posts");
    }

    @Override
    public void sortFeedByFound() {
        mFeedAdapter.getFilter().filter("Found");
        mFeedPostSort.setText("Found Posts");
    }

    public void refreshData() {
        Toast.makeText(getContext(), "Updating", Toast.LENGTH_SHORT).show();
        mFeedRvLatestReports.smoothScrollToPosition(0);
        mFeedSrlRefreshFeed.setVisibility(View.VISIBLE);
        mFeedSrlRefreshFeed.setRefreshing(true);
        mPresenter.refreshFeed();
    }

    @Override
    public void gridView() {
        isGridView = true;
        mFeedRvLatestReports.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL));
        mFeedRvLatestReports.setAdapter(mFeedAdapter);
        mFeedPostView.setImageDrawable(getResources().getDrawable(R.drawable.ic_view_agenda_grey_800_24dp));
    }

    @Override
    public void listView() {
        isGridView = false;
        mFeedRvLatestReports.setLayoutManager(new LinearLayoutManager(getContext()));
        mFeedRvLatestReports.setAdapter(mFeedAdapter);
        mFeedPostView.setImageDrawable(getResources().getDrawable(R.drawable.ic_view_module_grey_800_24dp));
    }

    @Override
    protected void init() {
        RecyclerItemUtils.addTo(mFeedRvLatestReports).setOnItemClickListener(this);
        mFeedRvLatestReports.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL));
        mFeedRvLatestReports.setAdapter(mFeedAdapter);
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

    @OnClick({R.id.feed_post_sort, R.id.feed_post_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.feed_post_sort:
                View sortDialog = getLayoutInflater().inflate(R.layout.dialog_feed_sort, null);
                mBottomSheetDialog1.setContentView(sortDialog);
                mBottomSheetDialog1.show();
                sortDialog.findViewById(R.id.sort_all_posts).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.setSortFeed("All");
                        mBottomSheetDialog1.dismiss();
                    }
                });
                sortDialog.findViewById(R.id.sort_lost_posts).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.setSortFeed("Lost");
                        mBottomSheetDialog1.dismiss();
                    }
                });
                sortDialog.findViewById(R.id.sort_found_posts).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.setSortFeed("Found");
                        mBottomSheetDialog1.dismiss();
                    }
                });
                break;
            case R.id.feed_post_view:
                View viewDialog = getLayoutInflater().inflate(R.layout.dialog_post_view, null);
                mBottomSheetDialog1.setContentView(viewDialog);
                mBottomSheetDialog1.show();
                viewDialog.findViewById(R.id.view_grid).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.setPostViewState("Grid");
                        mBottomSheetDialog1.dismiss();
                    }
                });
                viewDialog.findViewById(R.id.view_list).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.setPostViewState("List");
                        mBottomSheetDialog1.dismiss();
                    }
                });
                break;
        }
    }
}
