package com.makatizen.makahanap.ui.search;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.makatizen.makahanap.R;
import com.makatizen.makahanap.pojo.FeedItem;
import com.makatizen.makahanap.ui.base.BaseActivity;
import com.makatizen.makahanap.ui.item_details.ItemDetailsActivity;
import com.makatizen.makahanap.utils.IntentExtraKeys;
import com.makatizen.makahanap.utils.RecyclerItemUtils;
import com.makatizen.makahanap.utils.RequestCodes;
import com.makatizen.makahanap.utils.SimpleDividerItemDecoration;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity implements SearchMvpView {

    @Inject
    SearchMvpPresenter<SearchMvpView> mPresenter;
    @BindView(R.id.search_back_btn)
    ImageButton mSearchBackBtn;
    @BindView(R.id.search_keyword_et)
    EditText mSearchKeywordEt;
    @BindView(R.id.search_filter_btn)
    ImageButton mSearchFilterBtn;
    @BindView(R.id.search_more_btn)
    ImageButton mSearchMoreBtn;
    @BindView(R.id.search_btn)
    TextView mSearchBtn;
    @BindView(R.id.search_progress_loading)
    ProgressBar mSearchProgressLoading;
    @BindView(R.id.search_again_btn)
    Button mSearchAgainBtn;
    @BindView(R.id.search_history_rv)
    RecyclerView mSearchHistoryRv;
    @BindView(R.id.search_clear_history)
    TextView mSearchClearHistory;
    @BindView(R.id.search_history_layout)
    FrameLayout mSearchHistoryLayout;
    @BindView(R.id.search_results_no_tv)
    TextView mSearchResultsNoTv;
    @BindView(R.id.search_list_rv)
    RecyclerView mSearchListRv;
    @BindView(R.id.search_no_results)
    TextView mSearchNoResults;

    @Inject
    SearchAdapter mSearchAdapter;
    private Dialog mFilterDialog;
    private Spinner mReportTypeSpinner;
    private Spinner mStatusSpinner;
    private Spinner mItemTypeSpinner;
    private boolean isFiltered = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getActivityFragmentComponent().inject(this);
        mPresenter.attachView(this);
        setUnBinder(ButterKnife.bind(this));

        init();
    }

    @Override
    protected void init() {
        mSearchListRv.setAdapter(mSearchAdapter);
        mSearchListRv.setLayoutManager(new LinearLayoutManager(this));
        mSearchListRv.addItemDecoration(new SimpleDividerItemDecoration(this));
        RecyclerItemUtils.addTo(mSearchListRv).setOnItemClickListener(new RecyclerItemUtils.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                int itemId = mSearchAdapter.getItem(position).getItemId();
                Intent intent = new Intent(SearchActivity.this, ItemDetailsActivity.class);
                intent.putExtra(IntentExtraKeys.ITEM_ID, itemId);
                startActivityForResult(intent, RequestCodes.ITEM_DETAILS);
            }
        });
    }

    @OnClick({R.id.search_back_btn, R.id.search_filter_btn, R.id.search_more_btn, R.id.search_btn, R.id.search_again_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_back_btn:
                onBackPressed();
                break;
            case R.id.search_filter_btn:
                mFilterDialog = new Dialog(this);
                mFilterDialog.setContentView(R.layout.search_filter_dialog);
                mFilterDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                mItemTypeSpinner = mFilterDialog.findViewById(R.id.filter_item_type_spinner);
                mReportTypeSpinner = mFilterDialog.findViewById(R.id.filter_report_type_spinner);
                mStatusSpinner = mFilterDialog.findViewById(R.id.filter_status_spinner);

                Button applyButton = mFilterDialog.findViewById(R.id.filter_apply_btn);
                Button resetButton = mFilterDialog.findViewById(R.id.filter_reset_btn);

//                if (this.isFiltered) {
//                    mPresenter.getSaveFilterValues();
//                }

                mFilterDialog.show();

                applyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        String itemType = mItemTypeSpinner.getSelectedItem().toString();
                        String reportType = mReportTypeSpinner.getSelectedItem().toString();
                        String status = mStatusSpinner.getSelectedItem().toString();

                        // Save filter values in cache
                        //Positions
                        int itemTypePosition = mItemTypeSpinner.getSelectedItemPosition();
                        int reportTypePosition = mReportTypeSpinner.getSelectedItemPosition();
                        int statusPosition = mStatusSpinner.getSelectedItemPosition();
                        HashMap<String, Integer> filterMapPositions = new HashMap<>();
                        filterMapPositions.put("item_type_position", itemTypePosition);
                        filterMapPositions.put("report_type_position", reportTypePosition);
                        filterMapPositions.put("status_position", statusPosition);
//                        mPresenter.saveFilterPosition(filterMapPositions);

                        isFiltered = true;
                        // TODO: 3/28/19 Filter search results
                        HashMap<String, String> filterMapValues = new HashMap<>();
                        filterMapValues.put("item_type", itemType);
                        filterMapValues.put("report_type", reportType);
                        filterMapValues.put("status", status);
                        String keyword = mSearchKeywordEt.getText().toString().trim();
                        mPresenter.searchFilter(keyword, filterMapValues);
                        mSearchAgainBtn.setVisibility(View.GONE);
                        mFilterDialog.dismiss();
                    }
                });

                resetButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        //Reset filter
                        mItemTypeSpinner.setSelection(0);
                        mReportTypeSpinner.setSelection(0);
                        mStatusSpinner.setSelection(0);
                    }
                });

                break;
            case R.id.search_more_btn:
                PopupMenu popupMenu = new PopupMenu(this, mSearchMoreBtn);
                popupMenu.getMenuInflater().inflate(R.menu.search_menu, popupMenu.getMenu());
                popupMenu.setGravity(Gravity.BOTTOM);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(final MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.search_sort:
                                break;
                            case R.id.search_advance:
                                break;
                        }
                        return true;
                    }
                });
                break;
            case R.id.search_btn:
                String input = mSearchKeywordEt.getText().toString().trim();
                mPresenter.getItems(input, "10");
                break;
            case R.id.search_again_btn:
                mSearchFilterBtn.setVisibility(View.GONE);
                mSearchMoreBtn.setVisibility(View.GONE);
                mSearchBtn.setVisibility(View.VISIBLE);
                mSearchAgainBtn.setVisibility(View.GONE);
                mSearchKeywordEt.requestFocus();
                break;
        }
    }

    @Override
    public void showSearchLoading() {
        mSearchProgressLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSearchLoading() {
        mSearchProgressLoading.setVisibility(View.GONE);
    }

    @Override
    public void noResultsFound(final String keyword) {
        mSearchNoResults.setVisibility(View.VISIBLE);
        mSearchNoResults.setText("No Results Found on \"" + keyword + "\"");
        mSearchListRv.setVisibility(View.GONE);
        mSearchResultsNoTv.setVisibility(View.GONE);
    }

    @Override
    public void setNumberOfResults(String numberOfResults) {
        mSearchResultsNoTv.setText("No of Results Found: " + numberOfResults);
        mSearchResultsNoTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void setSearchResults(List<FeedItem> feedItemList) {
        mSearchListRv.setVisibility(View.VISIBLE);
        mSearchNoResults.setVisibility(View.GONE);
        mSearchMoreBtn.setVisibility(View.VISIBLE);
        mSearchFilterBtn.setVisibility(View.VISIBLE);
        mSearchAgainBtn.setVisibility(View.VISIBLE);
        mSearchBtn.setVisibility(View.GONE);
        mSearchAdapter.setData(feedItemList);
    }
}
