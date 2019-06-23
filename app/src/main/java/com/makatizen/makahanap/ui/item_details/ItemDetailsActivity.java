package com.makatizen.makahanap.ui.item_details;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.remote.ApiConstants;
import com.makatizen.makahanap.pojo.api_response.GetItemDetailsResponse;
import com.makatizen.makahanap.ui.base.BaseActivity;
import com.makatizen.makahanap.ui.image_viewer.ImageViewerActivity;
import com.makatizen.makahanap.ui.item_details.ImageSliderAdapter.OnItemClickListener;
import com.makatizen.makahanap.utils.DateUtils;
import com.makatizen.makahanap.utils.IntentExtraKeys;
import de.hdodenhof.circleimageview.CircleImageView;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class ItemDetailsActivity extends BaseActivity implements ItemDetailsMvpView, OnItemClickListener {

    @Inject
    ImageSliderAdapter mImageSliderAdapter;

    @BindView(R.id.item_details_btn_account_message)
    Button mItemDetailsBtnAccountMessage;

    @BindView(R.id.item_details_ibtn_map)
    ImageButton mItemDetailsIbtnMap;

    @BindView(R.id.item_details_image_progress_loading)
    ProgressBar mItemDetailsImageProgressLoading;

    @BindView(R.id.item_details_image_slide_dots)
    LinearLayout mItemDetailsImageSlideDots;

    @BindView(R.id.item_details_iv_account_image)
    CircleImageView mItemDetailsIvAccountImage;

    @BindView(R.id.item_details_iv_report_type)
    ImageView mItemDetailsIvReportType;

    @BindView(R.id.item_details_person_layout)
    ConstraintLayout mItemDetailsPersonLayout;

    @BindView(R.id.item_details_person_tv_date)
    TextView mItemDetailsPersonTvDate;

    @BindView(R.id.item_details_person_tv_date_lbl)
    TextView mItemDetailsPersonTvDateLbl;

    @BindView(R.id.item_details_person_tv_name)
    TextView mItemDetailsPersonTvName;

    @BindView(R.id.item_details_person_tv_name_lbl)
    TextView mItemDetailsPersonTvNameLbl;

    @BindView(R.id.item_details_pet_layout)
    ConstraintLayout mItemDetailsPetLayout;

    @BindView(R.id.item_details_pet_tv_breed)
    TextView mItemDetailsPetTvBreed;

    @BindView(R.id.item_details_pet_tv_breed_lbl)
    TextView mItemDetailsPetTvBreedLbl;

    @BindView(R.id.item_details_pet_tv_condition)
    TextView mItemDetailsPetTvCondition;

    @BindView(R.id.item_details_pet_tv_condition_lbl)
    TextView mItemDetailsPetTvConditionLbl;

    @BindView(R.id.item_details_pet_tv_date)
    TextView mItemDetailsPetTvDate;

    @BindView(R.id.item_details_pet_tv_date_lbl)
    TextView mItemDetailsPetTvDateLbl;

    @BindView(R.id.item_details_pet_tv_type)
    TextView mItemDetailsPetTvType;

    @BindView(R.id.item_details_pet_tv_type_lbl)
    TextView mItemDetailsPetTvTypeLbl;

    @BindView(R.id.item_details_progress_layout)
    FrameLayout mItemDetailsProgressLayout;

    @BindView(R.id.item_details_pt_layout)
    ConstraintLayout mItemDetailsPtLayout;

    @BindView(R.id.item_details_pt_tv_brand)
    TextView mItemDetailsPtTvBrand;

    @BindView(R.id.item_details_pt_tv_brand_lbl)
    TextView mItemDetailsPtTvBrandLbl;

    @BindView(R.id.item_details_pt_tv_category)
    TextView mItemDetailsPtTvCategory;

    @BindView(R.id.item_details_pt_tv_category_lbl)
    TextView mItemDetailsPtTvCategoryLbl;

    @BindView(R.id.item_details_pt_tv_color)
    TextView mItemDetailsPtTvColor;

    @BindView(R.id.item_details_pt_tv_color_lbl)
    TextView mItemDetailsPtTvColorLbl;

    @BindView(R.id.item_details_pt_tv_date)
    TextView mItemDetailsPtTvDate;

    @BindView(R.id.item_details_pt_tv_date_lbl)
    TextView mItemDetailsPtTvDateLbl;

    @BindView(R.id.item_details_pt_tv_turnover)
    TextView mItemDetailsPtTvTurnover;

    @BindView(R.id.item_details_pt_tv_turnover_lbl)
    TextView mItemDetailsPtTvTurnoverLbl;

    @BindView(R.id.item_details_reward_layout)
    LinearLayout mItemDetailsRewardLayout;

    @BindView(R.id.item_details_toolbar)
    Toolbar mItemDetailsToolbar;

    @BindView(R.id.item_details_tv_account_name)
    TextView mItemDetailsTvAccountName;

    @BindView(R.id.item_details_tv_created_at)
    TextView mItemDetailsTvCreatedAt;

    @BindView(R.id.item_details_tv_description)
    TextView mItemDetailsTvDescription;

    @BindView(R.id.item_details_tv_description_lbl)
    TextView mItemDetailsTvDescriptionLbl;

    @BindView(R.id.item_details_tv_location)
    TextView mItemDetailsTvLocation;

    @BindView(R.id.item_details_tv_reward_amount)
    TextView mItemDetailsTvRewardAmount;

    @BindView(R.id.item_details_tv_title)
    TextView mItemDetailsTvTitle;

    @BindView(R.id.item_details_tv_type)
    TextView mItemDetailsTvType;

    @BindView(R.id.item_details_view_pager)
    ViewPager mItemDetailsViewPager;

    @Inject
    ItemDetailsMvpPresenter<ItemDetailsMvpView> mPresenter;

    private int autoScrollDelay = 5000;

    private DecimalFormat df = new DecimalFormat("#,###,##0.00");

    private Handler handler = new Handler();

    private int mAccountId;

    private int mCurrentPage = 0;

    Runnable runnable = new Runnable() {
        public void run() {
            if (mImageSliderAdapter.getCount() == mCurrentPage) {
                mCurrentPage = 0;
            } else {
                mCurrentPage++;
            }
            mItemDetailsViewPager.setCurrentItem(mCurrentPage, true);
            handler.postDelayed(this, autoScrollDelay);
        }
    };

    private TextView[] mDots;

    private int mItemId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityFragmentComponent().inject(this);
        mPresenter.attachView(this);
        setContentView(R.layout.activity_item_details);
        setUnBinder(ButterKnife.bind(this));
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, autoScrollDelay);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void hideItemDetailsLoading() {
        mItemDetailsProgressLayout.setVisibility(View.GONE);
        mItemDetailsBtnAccountMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideItemImageLoading() {
        mItemDetailsImageProgressLoading.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(final List<String> imageList) {
        //Show Selected Image
        Intent intent = new Intent(this, ImageViewerActivity.class);
        intent.putExtra(IntentExtraKeys.SELECTED_POSITION, mCurrentPage);
        intent.putStringArrayListExtra(IntentExtraKeys.ITEM_IMAGES, (ArrayList<String>) imageList);
        startActivity(intent);
    }

    @Override
    public void onPersonData(final GetItemDetailsResponse response) {
        switch (response.getType()) {
            case LOST:
                mItemDetailsTvType.setText("Lost");
                mItemDetailsTvType.setTextColor(getResources().getColor(R.color.defaultRed));
                mItemDetailsPersonTvDateLbl.setText("Date Lost:");
                mItemDetailsPersonTvDate.setText(DateUtils.DateFormat(response.getPersonData().getDate()));
                mItemDetailsPersonTvName.setText(response.getPersonData().getName());
                break;
            case FOUND:
                mItemDetailsTvType.setText("Found");
                mItemDetailsTvType.setTextColor(getResources().getColor(R.color.defaultGreen));
                mItemDetailsPersonTvDateLbl.setText("Date Found:");
                mItemDetailsPersonTvDate.setText(DateUtils.DateFormat(response.getPersonData().getDate()));

                mItemDetailsPersonTvNameLbl.setVisibility(View.GONE); //REMOVE PERSON NAME
                mItemDetailsPersonTvName.setVisibility(View.GONE);
                break;
        }

        mItemDetailsIvReportType.setImageDrawable(getResources().getDrawable(R.drawable.ic_boy));
        mItemDetailsTvTitle.setText(response.getTitle());

        String location = "@ " + response.getPersonData().getAdditionalLocationInfo() + " " + response
                .getLocationData().getName();
        mItemDetailsTvLocation.setText(location);

        String description = response.getPersonData().getDescription();
        if (description.isEmpty()) {
            mItemDetailsTvDescriptionLbl.setVisibility(View.GONE);
            mItemDetailsTvDescription.setVisibility(View.GONE);
        } else {
            mItemDetailsTvDescription.setText(description);
        }

        mAccountId = response.getAccount().getId(); //Pass ID to local variable for messaging
        Glide.with(this)
                .load(ApiConstants.MAKATIZEN_API_BASE_URL + response.getAccount().getProfileImageUrl())
                .into(mItemDetailsIvAccountImage);

        String accountName = response.getAccount().getFirstName() + " " + response.getAccount().getLastName();
        mItemDetailsTvAccountName.setText(accountName);

        mItemDetailsTvCreatedAt.setText(DateUtils.DateToTimeFormat(response.getCreatedAt()));

        mItemDetailsPersonLayout.setVisibility(View.VISIBLE);

        Double reward = response.getPersonData().getReward() == null ? 0
                : response.getPersonData().getReward();

        if (reward != 0) {
            mItemDetailsTvRewardAmount.setText(String.valueOf(df.format(reward)));
            mItemDetailsRewardLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onPersonalThingData(final GetItemDetailsResponse response) {
        switch (response.getType()) {
            case LOST:
                mItemDetailsTvType.setText("Lost");
                mItemDetailsTvType.setTextColor(getResources().getColor(R.color.defaultRed));
                mItemDetailsPtTvDateLbl.setText("Date Lost:");
                mItemDetailsPtTvDate.setText(DateUtils.DateFormat(response.getPersonalThingData().getDate()));
                break;
            case FOUND:
                mItemDetailsTvType.setText("Found");
                mItemDetailsTvType.setTextColor(getResources().getColor(R.color.defaultGreen));
                mItemDetailsPtTvDateLbl.setText("Date Found:");
                mItemDetailsPtTvDate.setText(DateUtils.DateFormat(response.getPersonalThingData().getDate()));

                boolean isTurnOvered = response.getPersonalThingData().isItemSurrendered();

                if (isTurnOvered) {
                    mItemDetailsPtTvTurnoverLbl.setVisibility(View.VISIBLE);
                    mItemDetailsPtTvTurnover.setVisibility(View.VISIBLE);
                    mItemDetailsPtTvTurnover
                            .setText("Barangay " + response.getPersonalThingData().getBarangayData().getName());
                }
                break;
        }

        mItemDetailsIvReportType.setImageDrawable(getResources().getDrawable(R.drawable.ic_smartphone));

        mItemDetailsTvTitle.setText(response.getTitle());

        String location = "@ " + response.getPersonalThingData().getAdditionalLocationInfo() + " " + response
                .getLocationData().getName();
        mItemDetailsTvLocation.setText(location);

        String description = response.getPersonalThingData().getDescription();
        if (description.isEmpty()) {
            mItemDetailsTvDescriptionLbl.setVisibility(View.GONE);
            mItemDetailsTvDescription.setVisibility(View.GONE);
        } else {
            mItemDetailsTvDescription.setText(description);
        }

        mAccountId = response.getAccount().getId(); //Pass ID to local variable for messaging
        Glide.with(this)
                .load(ApiConstants.MAKATIZEN_API_BASE_URL + response.getAccount().getProfileImageUrl())
                .into(mItemDetailsIvAccountImage);

        String accountName = response.getAccount().getFirstName() + " " + response.getAccount().getLastName();
        mItemDetailsTvAccountName.setText(accountName);

        mItemDetailsTvCreatedAt.setText(DateUtils.DateToTimeFormat(response.getCreatedAt()));

        mItemDetailsPtLayout.setVisibility(View.VISIBLE); //Show Personal Thing Details Layout
        mItemDetailsPtTvCategory.setText(response.getPersonalThingData().getCategory());

        String brand = response.getPersonalThingData().getBrand();
        if (brand.isEmpty()) {
            mItemDetailsPtTvBrandLbl.setVisibility(View.GONE);
            mItemDetailsPtTvBrand.setVisibility(View.GONE);
        } else {
            mItemDetailsPtTvBrand.setText(brand);
        }

        mItemDetailsPtTvColor.setText(response.getPersonalThingData().getColor());

        Double reward = response.getPersonalThingData().getReward() == null ? 0
                : response.getPersonalThingData().getReward();

        if (reward != 0) {
            mItemDetailsTvRewardAmount.setText(String.valueOf(df.format(reward)));
            mItemDetailsRewardLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onPetData(final GetItemDetailsResponse response) {
        switch (response.getType()) {
            case LOST:
                mItemDetailsTvType.setText("Lost");
                mItemDetailsTvType.setTextColor(getResources().getColor(R.color.defaultRed));
                mItemDetailsPtTvDateLbl.setText("Date Lost:");
                mItemDetailsPetTvDate.setText(DateUtils.DateFormat(response.getPetData().getDate()));
                break;
            case FOUND:
                mItemDetailsTvType.setText("Found");
                mItemDetailsTvType.setTextColor(getResources().getColor(R.color.defaultGreen));
                mItemDetailsPetTvDateLbl.setText("Date Found:");
                mItemDetailsPetTvDate.setText(DateUtils.DateFormat(response.getPetData().getDate()));
                break;
        }

        mItemDetailsIvReportType.setImageDrawable(getResources().getDrawable(R.drawable.ic_pet));
        mItemDetailsTvTitle.setText(response.getTitle());

        String location = "@ " + response.getPetData().getAdditionalLocationInfo() + " " + response
                .getLocationData().getName();
        mItemDetailsTvLocation.setText(location);

        String description = response.getPetData().getDescription();
        if (description.isEmpty()) {
            mItemDetailsTvDescriptionLbl.setVisibility(View.GONE);
            mItemDetailsTvDescription.setVisibility(View.GONE);
        } else {
            mItemDetailsTvDescription.setText(description);
        }

        mAccountId = response.getAccount().getId(); //Pass ID to local variable for messaging
        Glide.with(this)
                .load(ApiConstants.MAKATIZEN_API_BASE_URL + response.getAccount().getProfileImageUrl())
                .into(mItemDetailsIvAccountImage);

        String accountName = response.getAccount().getFirstName() + " " + response.getAccount().getLastName();
        mItemDetailsTvAccountName.setText(accountName);

        mItemDetailsTvCreatedAt.setText(DateUtils.DateToTimeFormat(response.getCreatedAt()));

        mItemDetailsPetLayout.setVisibility(View.VISIBLE);

        mItemDetailsPetTvType.setText(response.getPetData().getPetType());
        mItemDetailsPetTvBreed.setText(response.getPetData().getBreed());
        mItemDetailsPetTvCondition.setText(response.getPetData().getCondition());

        Double reward = response.getPetData().getReward() == null ? 0
                : response.getPetData().getReward();

        if (reward != 0) {
            mItemDetailsTvRewardAmount.setText(String.valueOf(df.format(reward)));
            mItemDetailsRewardLayout.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.item_details_ibtn_map, R.id.item_details_btn_account_message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.item_details_ibtn_map:
                break;
            case R.id.item_details_btn_account_message:
                break;
        }
    }

    @Override
    public void removeMessageButton() {
        mItemDetailsBtnAccountMessage.setVisibility(View.GONE);
    }

    @Override
    public void setItemImages(final List<String> itemImages) {
        mImageSliderAdapter.setImages(itemImages);
        addDotsIndicator(0);
    }

    @Override
    protected void init() {
        mItemId = getIntent().getIntExtra(IntentExtraKeys.ITEM_ID, 0);
        setSupportActionBar(mItemDetailsToolbar);
        setTitle(null);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }
        mItemDetailsViewPager.setAdapter(mImageSliderAdapter);

        //Load Item Images
        mPresenter.loadItemImages(mItemId);
        //Load Item Details
        mPresenter.loadItemDetails(mItemId);

        mImageSliderAdapter.setOnItemClickListener(this);
        mItemDetailsViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(final int state) {
            }

            @Override
            public void onPageScrolled(final int i, final float v, final int i1) {

            }

            @Override
            public void onPageSelected(final int i) {
                addDotsIndicator(i);
                mCurrentPage = i;
            }
        });
        mItemDetailsViewPager.setCurrentItem(mCurrentPage);
    }

    private void addDotsIndicator(int position) {
        mItemDetailsImageSlideDots.removeAllViews();
        mDots = new TextView[mImageSliderAdapter.getCount()];
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.quantum_grey));

            mItemDetailsImageSlideDots.addView(mDots[i]);
        }
        if (mDots.length > 0) {
            mDots[position].setTextColor(Color.WHITE);
        }
    }
}
