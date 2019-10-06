package com.makatizen.makahanap.ui.item_details;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.remote.ApiConstants;
import com.makatizen.makahanap.pojo.MakahanapAccount;
import com.makatizen.makahanap.pojo.api_response.GetItemDetailsResponse;
import com.makatizen.makahanap.ui.base.BaseActivity;
import com.makatizen.makahanap.ui.chat_convo.ChatConvoActivity;
import com.makatizen.makahanap.ui.image_viewer.ImageViewerActivity;
import com.makatizen.makahanap.ui.item_details.ImageSliderAdapter.OnItemClickListener;
import com.makatizen.makahanap.ui.main.account.AccountFragment;
import com.makatizen.makahanap.ui.main.feed.FeedFragment;
import com.makatizen.makahanap.ui.return_item.ReturnItemActivity;
import com.makatizen.makahanap.utils.DateUtils;
import com.makatizen.makahanap.utils.IntentExtraKeys;
import com.makatizen.makahanap.utils.RequestCodes;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

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
    @BindView(R.id.return_item_okay)
    TextView mReturnItemOkay;
    @BindView(R.id.item_return_layout)
    CardView mItemReturnLayout;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.divider3)
    View divider3;
    @BindView(R.id.textView19)
    TextView textView19;
    @BindView(R.id.divider4)
    View divider4;
    @BindView(R.id.textView20)
    TextView textView20;
    @BindView(R.id.progressBar3)
    ProgressBar progressBar3;
    @BindView(R.id.return_agreement_agree)
    TextView mReturnAgreementAgree;
    @BindView(R.id.return_agreement_disagree)
    TextView mReturnAgreementDisagree;
    @BindView(R.id.item_return_agreement_layout)
    CardView mItemReturnAgreementLayout;
    @BindView(R.id.item_details_map_view)
    ImageView mItemDetailsMapView;

    private int autoScrollDelay = 5000;

    private DecimalFormat df = new DecimalFormat("#,###,##0.00");

    private Handler handler = new Handler();

    private MakahanapAccount itemOwnerAccount = new MakahanapAccount();

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
    private Menu mMenu;
    private String mSelectedReportReason = "";

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

        mPresenter.checkItemReported(String.valueOf(mItemId));
        String lat = response.getLocationData().getLatitude();
        String lng = response.getLocationData().getLongitude();
        final String url = "https://maps.googleapis.com/maps/api/staticmap?center=" + lat + "," + lng + "&zoom=17&scale=1&size=600x300&maptype=roadmap&key=" + getResources().getString(R.string.google_map_api_key) + "&format=png&visual_refresh=true&markers=size:mid%7Ccolor:0xff5050%7Clabel:%7C" + lat + "," + lng;
        Glide.with(this)
                .load(url)
                .into(mItemDetailsMapView);
        itemOwnerAccount = response.getAccount();
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

        Glide.with(this)
                .load(response.getAccount().getProfileImageUrl())
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
        mPresenter.checkItemReported(String.valueOf(mItemId));
        String lat = response.getLocationData().getLatitude();
        String lng = response.getLocationData().getLongitude();
        final String url = "https://maps.googleapis.com/maps/api/staticmap?center=" + lat + "," + lng + "&zoom=15&scale=1&size=600x300&maptype=roadmap&key=" + getResources().getString(R.string.google_map_api_key) + "&format=png&visual_refresh=true&markers=size:mid%7Ccolor:0xff5050%7Clabel:%7C" + lat + "," + lng;
        Glide.with(this)
                .load(url)
                .into(mItemDetailsMapView);
        itemOwnerAccount = response.getAccount();
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

        Glide.with(this)
                .load(response.getAccount().getProfileImageUrl())
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
        mPresenter.checkItemReported(String.valueOf(mItemId));
        String lat = response.getLocationData().getLatitude();
        String lng = response.getLocationData().getLongitude();
        final String url = "https://maps.googleapis.com/maps/api/staticmap?center=" + lat + "," + lng + "&zoom=15&scale=1&size=600x300&maptype=roadmap&key=" + getResources().getString(R.string.google_map_api_key) + "&format=png&visual_refresh=true&markers=size:mid%7Ccolor:0xff5050%7Clabel:%7C" + lat + "," + lng;
        Glide.with(this)
                .load(url)
                .into(mItemDetailsMapView);
        itemOwnerAccount = response.getAccount();
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

        Glide.with(this)
                .load(response.getAccount().getProfileImageUrl())
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

    @Override
    public void onSuccessGetChatId(final int chatId) {
        Intent intent = new Intent(this, ChatConvoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(IntentExtraKeys.ACCOUNT, itemOwnerAccount);
        intent.putExtra(IntentExtraKeys.ITEM_ID, mItemId);
        intent.putExtra(IntentExtraKeys.CHAT_ID, String.valueOf(chatId));
        startActivity(intent);
    }

    @OnClick({R.id.return_agreement_agree, R.id.return_agreement_disagree, R.id.item_details_ibtn_map, R.id.item_details_btn_account_message, R.id.return_item_okay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.item_details_ibtn_map:
                break;
            case R.id.item_details_btn_account_message:
//                mPresenter.openChat(itemOwnerAccount.getId(), "Single");
                mPresenter.openItemChat(mItemId);
                break;
            case R.id.return_item_okay:
                mPresenter.startReturnActivity();
                break;
            case R.id.return_agreement_agree:
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_return_agreement);
                dialog.setTitle("Confirmation");
                dialog.getWindow().setLayout(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                dialog.show();

                final Button confirmed = dialog.findViewById(R.id.return_agreement_ok_btn);
                Button cancel = dialog.findViewById(R.id.return_agreement_cancel_btn);
                final CheckBox agree = dialog.findViewById(R.id.return_agreement_yes_cb);

                agree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(final CompoundButton compoundButton, final boolean isChecked) {
                        if (isChecked) {
                            confirmed.setEnabled(true);
                        } else {
                            confirmed.setEnabled(false);
                        }
                    }
                });

                confirmed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        // TODO: 3/11/19 Set the transaction as accepted
                        mPresenter.agreeAgreement();
                        dialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.return_agreement_disagree:
                new AlertDialog.Builder(this)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure to disagree?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.disgreeAgreement();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RequestCodes.RETURN_ITEM) {
                mItemReturnLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void showReturnOption() {
        mItemReturnLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void openReturnItemActivity(int meetupId) {
        Intent intent = new Intent(this, ReturnItemActivity.class);
        intent.putExtra(IntentExtraKeys.ITEM_ID, mItemId);
        intent.putExtra(IntentExtraKeys.MEET_ID, meetupId);
        startActivityForResult(intent, RequestCodes.RETURN_ITEM);
    }

    @Override
    public void showReturnAgreement() {
        mItemReturnAgreementLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRatingDialog(String profileUrl, String name) {
        mItemReturnAgreementLayout.setVisibility(View.GONE);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_rating);
        dialog.getWindow().setLayout(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        ImageView accountImage = dialog.findViewById(R.id.rating_account_image);
        TextView accountName = dialog.findViewById(R.id.rating_account_name);
        final RatingBar accountRating = dialog.findViewById(R.id.rating_account_bar);
        Button goodJob = dialog.findViewById(R.id.rating_feedback_goodjob_btn);
        Button helpFul = dialog.findViewById(R.id.rating_feedback_sohelpful_btn);
        Button badAttitude = dialog.findViewById(R.id.rating_feedback_badattitude_btn);

        Glide.with(this)
                .load(ApiConstants.MAKATIZEN_API_BASE_URL + profileUrl)
                .apply(RequestOptions.overrideOf(300, 300).circleCrop())
                .into(accountImage);

        accountName.setText(name);
        dialog.setCancelable(false);
        dialog.show();

        goodJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                dialog.dismiss();
                mPresenter.rateAccount(String.valueOf(accountRating.getRating()), "Good Job");
            }
        });
        helpFul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                dialog.dismiss();
                mPresenter.rateAccount(String.valueOf(accountRating.getRating()), "Helpful");

            }
        });
        badAttitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                dialog.dismiss();
                mPresenter.rateAccount(String.valueOf(accountRating.getRating()), "Bad Attitude");
            }
        });
    }

    @Override
    public void hideReturnAgreement() {
        mItemReturnAgreementLayout.setVisibility(View.GONE);
    }

    @Override
    public void onSuccessReturn() {
        FeedFragment feedFragment = FeedFragment.getInstance();
        AccountFragment accountFragment = AccountFragment.getInstance();
        feedFragment.refreshData();
        accountFragment.refreshData();
        finish();
    }

    @Override
    public void showReportOption() {
        MenuItem menuItem = mMenu.findItem(R.id.option_item_details_report);
        menuItem.setVisible(true);
    }

    @Override
    public void showRemoveOption() {
        MenuItem menuItem = mMenu.findItem(R.id.option_item_details_remove);
        menuItem.setVisible(true);
    }

    @Override
    public void onSuccessReportItem() {
        MenuItem menuItem = mMenu.findItem(R.id.option_item_details_report);
        menuItem.setVisible(false);
        Toast.makeText(this, "This item was successfully reported", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideReportOption() {
        MenuItem menuItem = mMenu.findItem(R.id.option_item_details_report);
        menuItem.setVisible(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_item_detail, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_item_details_remove:
                break;
            case R.id.option_item_details_report:
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_report_item);
                dialog.getWindow().setLayout(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                dialog.show();
                final RadioButton r1 = dialog.findViewById(R.id.report_item_reason_1);
                final RadioButton r2 = dialog.findViewById(R.id.report_item_reason_2);
                final RadioButton r3 = dialog.findViewById(R.id.report_item_reason_3);
                final RadioButton r4 = dialog.findViewById(R.id.report_item_reason_4);
                final RadioButton r5 = dialog.findViewById(R.id.report_item_reason_5);
                final RadioButton r6 = dialog.findViewById(R.id.report_item_reason_6);
                final RadioButton r7 = dialog.findViewById(R.id.report_item_reason_7);
                final Button submit = dialog.findViewById(R.id.report_item_submit_btn);
                r1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSelectedReportReason = "Offensive";
                        submit.setEnabled(true);
                        r1.setChecked(true);
                        r2.setChecked(false);
                        r3.setChecked(false);
                        r4.setChecked(false);
                        r5.setChecked(false);
                        r6.setChecked(false);
                        r7.setChecked(false);

                    }
                });
                r2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSelectedReportReason = "Sexually Inappropriate";
                        submit.setEnabled(true);
                        r1.setChecked(false);
                        r2.setChecked(true);
                        r3.setChecked(false);
                        r4.setChecked(false);
                        r5.setChecked(false);
                        r6.setChecked(false);
                        r7.setChecked(false);

                    }
                });
                r3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSelectedReportReason = "Violence";
                        submit.setEnabled(true);

                        r1.setChecked(false);
                        r2.setChecked(false);
                        r3.setChecked(true);
                        r4.setChecked(false);
                        r5.setChecked(false);
                        r6.setChecked(false);
                        r7.setChecked(false);

                    }
                });
                r4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSelectedReportReason = "Prohibited Content";

                        submit.setEnabled(true);

                        r1.setChecked(false);
                        r2.setChecked(false);
                        r3.setChecked(false);
                        r4.setChecked(true);
                        r5.setChecked(false);
                        r6.setChecked(false);
                        r7.setChecked(false);

                    }
                });
                r5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSelectedReportReason = "Spam";

                        submit.setEnabled(true);

                        r1.setChecked(false);
                        r2.setChecked(false);
                        r3.setChecked(false);
                        r4.setChecked(false);
                        r5.setChecked(true);
                        r6.setChecked(false);
                        r7.setChecked(false);

                    }
                });
                r6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSelectedReportReason = "False News";

                        submit.setEnabled(true);

                        r1.setChecked(false);
                        r2.setChecked(false);
                        r3.setChecked(false);
                        r4.setChecked(false);
                        r5.setChecked(false);
                        r6.setChecked(true);
                        r7.setChecked(false);

                    }
                });
                r7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSelectedReportReason = "Other";
                        submit.setEnabled(true);

                        r1.setChecked(false);
                        r2.setChecked(false);
                        r3.setChecked(false);
                        r4.setChecked(false);
                        r5.setChecked(false);
                        r6.setChecked(false);
                        r7.setChecked(true);

                    }
                });

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        mPresenter.reportItem(String.valueOf(mItemId), mSelectedReportReason);
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
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

        mPresenter.checkReturnStatus(mItemId);
        mPresenter.checkItemPendingReturnAgreement(mItemId);

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
