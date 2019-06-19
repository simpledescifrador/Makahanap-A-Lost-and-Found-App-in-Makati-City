package com.makatizen.makahanap.ui.report.personal_thing;


import android.Manifest.permission;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import br.com.sapereaude.maskedEditText.MaskedEditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.Place.Field;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.pojo.BarangayData;
import com.makatizen.makahanap.pojo.ItemImageDetails;
import com.makatizen.makahanap.pojo.LocationData;
import com.makatizen.makahanap.pojo.PersonalThing;
import com.makatizen.makahanap.ui.base.BaseActivity;
import com.makatizen.makahanap.ui.image_viewer.ImageViewerActivity;
import com.makatizen.makahanap.ui.report.adapter.ItemImagesAdapter;
import com.makatizen.makahanap.utils.AppAlertDialog;
import com.makatizen.makahanap.utils.AppAlertDialog.AlertType;
import com.makatizen.makahanap.utils.DateDialogFragment;
import com.makatizen.makahanap.utils.ImageUtils;
import com.makatizen.makahanap.utils.IntentExtraKeys;
import com.makatizen.makahanap.utils.PermissionUtils;
import com.makatizen.makahanap.utils.RecyclerItemUtils;
import com.makatizen.makahanap.utils.RecyclerItemUtils.OnItemClickListener;
import com.makatizen.makahanap.utils.RecyclerItemUtils.OnLongClickListener;
import com.makatizen.makahanap.utils.RequestCodes;
import com.makatizen.makahanap.utils.enums.Categories;
import com.makatizen.makahanap.utils.enums.Type;
import com.yalantis.ucrop.UCrop;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;

public class ReportPersonalThingActivity extends BaseActivity implements ReportPersonalThingMvpView,
        OnDateSetListener, OnItemClickListener, OnLongClickListener {

    @Inject
    AppAlertDialog mAppAlertDialog;

    @Inject
    BottomSheetDialog mBottomSheetDialog;

    @Inject
    ImageUtils mImageUtils;

    @Inject
    ItemImagesAdapter mItemImagesAdapter;

    @Inject
    PermissionUtils mPermissionUtils;

    @Inject
    ReportPersonalThingMvpPresenter<ReportPersonalThingMvpView> mPresenter;

    @BindView(R.id.report_pt_btn_add_image)
    Button mReportPtBtnAddImage;

    @BindView(R.id.report_pt_btn_cancel_delete)
    Button mReportPtBtnCancelDelete;

    @BindView(R.id.report_pt_btn_delete)
    Button mReportPtBtnDelete;

    @BindView(R.id.report_pt_btn_submit_report)
    Button mReportPtBtnSubmitReport;

    @BindView(R.id.report_pt_cat_bag)
    CardView mReportPtCatBag;

    @BindView(R.id.report_pt_cat_id)
    CardView mReportPtCatId;

    @BindView(R.id.report_pt_cat_keys)
    CardView mReportPtCatKeys;

    @BindView(R.id.report_pt_cat_laptop)
    CardView mReportPtCatLaptop;

    @BindView(R.id.report_pt_cat_mobile)
    CardView mReportPtCatMobile;

    @BindView(R.id.report_pt_cat_more)
    LinearLayout mReportPtCatMore;

    @BindView(R.id.report_pt_cat_school_supplies)
    CardView mReportPtCatSchoolSupplies;

    @BindView(R.id.report_pt_cat_umbrella)
    CardView mReportPtCatUmbrella;

    @BindView(R.id.report_pt_cat_wallet)
    CardView mReportPtCatWallet;

    @BindView(R.id.report_pt_et_brand)
    EditText mReportPtEtBrand;

    @BindView(R.id.report_pt_et_color)
    EditText mReportPtEtColor;

    @BindView(R.id.report_pt_et_date)
    MaskedEditText mReportPtEtDate;

    @BindView(R.id.report_pt_et_description)
    EditText mReportPtEtDescription;

    @BindView(R.id.report_pt_et_location)
    EditText mReportPtEtLocation;

    @BindView(R.id.report_pt_et_more_location_info)
    EditText mReportPtEtMoreLocationInfo;

    @BindView(R.id.report_pt_et_name)
    EditText mReportPtEtName;

    @BindView(R.id.report_pt_et_reward_amount)
    EditText mReportPtEtRewardAmount;

    @BindView(R.id.report_pt_ibtn_date_picker)
    ImageButton mReportPtIbtnDatePicker;

    @BindView(R.id.report_pt_ibtn_place_picker)
    ImageButton mReportPtIbtnPlacePicker;

    @BindView(R.id.report_pt_linearl_delete)
    LinearLayout mReportPtLinearlDelete;

    @BindView(R.id.report_pt_reward_layout)
    LinearLayout mReportPtRewardLayout;

    @BindView(R.id.report_pt_rv_images)
    RecyclerView mReportPtRvImages;

    @BindView(R.id.report_pt_select_category)
    TextView mReportPtSelectCategory;

    @BindView(R.id.report_pt_spinner_barangay_names)
    Spinner mReportPtSpinnerBarangayNames;

    @BindView(R.id.report_pt_switch_reward)
    Switch mReportPtSwitchReward;

    @BindView(R.id.report_pt_switch_turnover)
    Switch mReportPtSwitchTurnover;

    @BindView(R.id.report_pt_turnover_layout)
    LinearLayout mReportPtTurnoverLayout;

    @BindView(R.id.report_pt_tv_add_location_info)
    TextView mReportPtTvAddLocationInfo;

    @BindView(R.id.report_pt_tv_brand)
    TextView mReportPtTvBrand;

    @BindView(R.id.report_pt_tv_color)
    TextView mReportPtTvColor;

    @BindView(R.id.report_pt_tv_date)
    TextView mReportPtTvDate;

    @BindView(R.id.report_pt_tv_description)
    TextView mReportPtTvDescription;

    @BindView(R.id.report_pt_tv_location)
    TextView mReportPtTvLocation;

    @BindView(R.id.report_pt_tv_more_location_info)
    TextView mReportPtTvMoreLocationInfo;

    @BindView(R.id.report_pt_tv_name)
    TextView mReportPtTvName;

    private List<String> mImagePaths = new ArrayList<>();

    private List<FrameLayout> mItemsMarkDeleteLayouts = new ArrayList<>();

    private LocationData mLocationData = new LocationData();

    private List<Integer> mMarkToDeleteItems = new ArrayList<>();

    private Type mType;

    private String selectedCategoryString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityFragmentComponent().inject(this);
        mPresenter.attachView(this);
        setContentView(R.layout.activity_report_personal_thing);
        setUnBinder(ButterKnife.bind(this));

        init();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCodes.OPEN_CAMERA:
                    Uri imageUri = Uri.fromFile(new File(mImageUtils.getImagePath()));

                    if (imageUri != null) {
                        mImageUtils.startImageCrop(imageUri);
                    }
                    break;
                case RequestCodes.OPEN_GALLERY:
                    if (data.getData() != null) {
                        Uri selectedImage = data.getData();
                        if (selectedImage != null) {
                            mImageUtils.startImageCrop(selectedImage);
                        }
                    }
                    break;
                case UCrop.REQUEST_CROP:
                    Uri imageUriResultCrop = UCrop.getOutput(data);
                    mImagePaths.add(imageUriResultCrop.getPath());
                    int imageCount = mImagePaths.size();
                    ItemImageDetails itemImageDetails = new ItemImageDetails();
                    itemImageDetails.setPath(mImagePaths.get(imageCount - 1));
                    itemImageDetails.setSize(
                            String.valueOf(mImageUtils.getFileSizeInKb(new File(mImagePaths.get(imageCount - 1))))
                                    + " KB");
                    mItemImagesAdapter.insert(itemImageDetails, mItemImagesAdapter.getItemCount());
                    break;

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void hideBrandOption() {
        mReportPtTvBrand.setVisibility(View.GONE);
        mReportPtEtBrand.setVisibility(View.GONE);
    }

    @Override
    public void onDateSet(final DatePicker datePicker, final int year, final int month, final int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String selectedDate = String.format("%d", year) + String.format("%02d", month + 1) + String
                .format("%02d", dayOfMonth);
        mReportPtEtDate.setText(selectedDate);
    }

    @Override
    public void onFoundItemType() {
        mPresenter.loadBarangayList();
        mReportPtTurnoverLayout.setVisibility(View.VISIBLE);
        mReportPtSwitchTurnover.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean isChecked) {
                if (isChecked) {
                    mReportPtSpinnerBarangayNames.setVisibility(View.VISIBLE);
                } else {
                    mReportPtSpinnerBarangayNames.setVisibility(View.GONE);
                }
            }
        });
        //Change Titles
        mReportPtTvName.setText(getResources().getString(R.string.hint_found_pt_item_name));
        mReportPtTvLocation.setText(getResources().getString(R.string.hint_found_pt_location));
        mReportPtTvDate.setText(getResources().getString(R.string.hint_found_pt_date));
        mReportPtTvDescription.setText(getResources().getString(R.string.hint_found_pt_description));
        mReportPtTvColor.setText(getResources().getString(R.string.hint_found_pt_color));
        mReportPtTvBrand.setText(getResources().getString(R.string.hint_found_pt_brand));
    }

    @Override
    public void onItemClicked(final RecyclerView recyclerView, final int position, final View v) {
        //Show Selected Image
        Intent intent = new Intent(this, ImageViewerActivity.class);
        intent.putExtra(IntentExtraKeys.SELECTED_POSITION, position);
        intent.putStringArrayListExtra(IntentExtraKeys.ITEM_IMAGES, (ArrayList<String>) mImagePaths);
        startActivity(intent);
    }

    @Override
    public void onLongClicked(final RecyclerView recyclerView, final int position, final View v) {
        FrameLayout mItemsMarkDeleteLayout = v.findViewById(R.id.add_image_fl_delete);
        if (mItemsMarkDeleteLayout.getVisibility() == View.GONE) {
            //Mark to Delete
            mItemsMarkDeleteLayout.setVisibility(View.VISIBLE);
            mMarkToDeleteItems.add(position);
            mItemsMarkDeleteLayouts.add(mItemsMarkDeleteLayout);
        } else {
            //Remove Mark to Delete
            mItemsMarkDeleteLayout.setVisibility(View.GONE);
            mMarkToDeleteItems.remove(position);
            mItemsMarkDeleteLayouts.remove(mItemsMarkDeleteLayout);
        }

        int markItemSize = mMarkToDeleteItems.size();

        mReportPtLinearlDelete.setVisibility(View.VISIBLE);
        if (markItemSize == 0) {
            mReportPtLinearlDelete.setVisibility(View.GONE);
        } else if (markItemSize > 1) {
            mReportPtBtnDelete.setText("Delete All");
        } else {
            mReportPtBtnDelete.setText("Delete");
        }
    }

    @Override
    public void onLostItemType() {
        mReportPtRewardLayout.setVisibility(View.VISIBLE);
        mReportPtSwitchReward.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean isChecked) {
                if (isChecked) {
                    mReportPtEtRewardAmount.setVisibility(View.VISIBLE);
                } else {
                    mReportPtEtRewardAmount.setVisibility(View.GONE);
                }
            }
        });

        //Change Titles
        mReportPtTvName.setText(getResources().getString(R.string.hint_lost_pt_item_name));
        mReportPtTvLocation.setText(getResources().getString(R.string.hint_lost_pt_location));
        mReportPtTvDate.setText(getResources().getString(R.string.hint_lost_pt_date));
        mReportPtTvDescription.setText(getResources().getString(R.string.hint_lost_pt_description));
        mReportPtTvColor.setText(getResources().getString(R.string.hint_lost_pt_color));
        mReportPtTvBrand.setText(getResources().getString(R.string.hint_lost_pt_brand));
    }

    @Override
    public void onNoGiveReward() {
        mReportPtEtRewardAmount.setVisibility(View.GONE);
    }

    @Override
    public void onNoTurnoverItem() {
        mReportPtSpinnerBarangayNames.setVisibility(View.GONE);
    }

    @Override
    public void onSubmitReportCompleted() {
//        FancyAlertDialog.Builder builder = null;
//        switch (mType) {
//            case FOUND:
//                builder = mAppAlertDialog.showSuccessAlertDialog("Your report has been submitted",
//                        "We will let you know when lost items was matched with your item", "OK", "See on Map");
//                break;
//            case LOST:
//                builder = mAppAlertDialog.showSuccessAlertDialog("Your report has been submitted",
//                        "We will let you know when found item was matched with your item", "OK", "See on Map");
//                break;
//        }
//
//        if (builder == null) {
//            return;
//        }
//
//        builder.OnPositiveClicked(new FancyAlertDialogListener() {
//            @Override
//            public void OnClick() {
//                setResult(RESULT_OK);
//                finish();
//            }
//        })
//                .OnNegativeClicked(new FancyAlertDialogListener() {
//                    @Override
//                    public void OnClick() {
//                        // TODO: 6/19/19
//                    }
//                })
//                .build();
        final Dialog dialog = mAppAlertDialog.showCustomDialog(AlertType.SUCCESS, "Your report has been submitted", "We will let you know when other item was matched with your item", "Okay");

        if (dialog == null) {
            return;
        }
        dialog.findViewById(R.id.custom_alert_btn_positive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                dialog.dismiss();
                setResult(RESULT_OK);
                finish();
            }
        });

        dialog.findViewById(R.id.custom_alert_btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                dialog.dismiss();
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @OnClick({R.id.report_pt_cat_mobile, R.id.report_pt_cat_laptop, R.id.report_pt_cat_school_supplies,
            R.id.report_pt_cat_id, R.id.report_pt_cat_wallet, R.id.report_pt_cat_umbrella, R.id.report_pt_cat_bag,
            R.id.report_pt_cat_keys, R.id.report_pt_cat_more, R.id.report_pt_ibtn_place_picker,
            R.id.report_pt_ibtn_date_picker, R.id.report_pt_btn_add_image, R.id.report_pt_btn_submit_report,
            R.id.report_pt_btn_delete, R.id.report_pt_btn_cancel_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.report_pt_cat_mobile:
                mPresenter.selectCategory(Categories.MOBILE_PHONE);
                break;
            case R.id.report_pt_cat_laptop:
                mPresenter.selectCategory(Categories.LAPTOP);
                break;
            case R.id.report_pt_cat_school_supplies:
                mPresenter.selectCategory(Categories.SCHOOL_SUPPLIES);
                break;
            case R.id.report_pt_cat_id:
                mPresenter.selectCategory(Categories.ID);
                break;
            case R.id.report_pt_cat_wallet:
                mPresenter.selectCategory(Categories.WALLET);
                break;
            case R.id.report_pt_cat_umbrella:
                mPresenter.selectCategory(Categories.UMBRELLA);
                break;
            case R.id.report_pt_cat_bag:
                mPresenter.selectCategory(Categories.BAG);
                break;
            case R.id.report_pt_cat_keys:
                mPresenter.selectCategory(Categories.KEYS);
                break;
            case R.id.report_pt_cat_more:
                final String[] moreCategories = getResources().getStringArray(R.array.more_categories);
                new Builder(this)
                        .setTitle("Select Categories")
                        .setPositiveButton("OK", new OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, final int i) {
                                clearCategorySelected();
                                mReportPtSelectCategory
                                        .setText("Selected Category: " + selectedCategoryString);
                                mReportPtSelectCategory
                                        .setTextColor(getResources().getColor(R.color.defaultGreen));

                            }
                        }).setNegativeButton("Cancel", new OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, final int i) {
                        dialogInterface.cancel();
                        selectedCategoryString = "";
                        mReportPtSelectCategory
                                .setText("Select Category: Please select category");
                        mReportPtSelectCategory
                                .setTextColor(getResources()
                                        .getColor(R.color.defaultRed));
                    }
                }).setSingleChoiceItems(moreCategories, -1,
                        new OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, final int i) {
                                selectedCategoryString = moreCategories[i];
                                switch (moreCategories[i]) {
                                    case "Arts":
                                        mPresenter.selectCategory(Categories.ART);
                                        break;
                                    case "Beauty Accessories":
                                        mPresenter.selectCategory(Categories.BEAUTY_ACCESSORIES);
                                        break;
                                    case "Books":
                                        mPresenter.selectCategory(Categories.BOOKS);
                                        break;
                                    case "Clothes":
                                        mPresenter.selectCategory(Categories.CLOTHES);
                                        break;
                                    case "Paperworks":
                                        mPresenter.selectCategory(Categories.PAPER_WORKS);
                                        break;
                                    case "Bank Cards":
                                        mPresenter.selectCategory(Categories.BANK_CARDS);
                                        break;
                                    case "Jewelry":
                                        mPresenter.selectCategory(Categories.JEWELRY);
                                        break;
                                    case "eReaders":
                                        mPresenter.selectCategory(Categories.eREADERS);
                                        break;
                                    case "PC Accessories":
                                        mPresenter.selectCategory(Categories.PC_ACCESSORIES);
                                        break;
                                    case "Photography Equipment":
                                        mPresenter.selectCategory(Categories.PHOTOGRAPHY_EQUIPMENT);
                                        break;
                                    case "Mobile Phone Accessories":
                                        mPresenter.selectCategory(Categories.MOBILE_PHONE_ACCESSORIES);
                                        break;
                                    case "Sports Equipment":
                                        mPresenter.selectCategory(Categories.SPORTS_EQUIPMENT);
                                        break;
                                    case "Others":
                                        dialogInterface.dismiss();
                                        mPresenter.selectCategory(Categories.OTHERS);
                                        //Show Input Dialog for new category
                                        LayoutInflater layoutInflater = LayoutInflater
                                                .from(getApplicationContext());
                                        View promptView = layoutInflater
                                                .inflate(R.layout.dialog_other_category, null);
                                        final EditText input = promptView
                                                .findViewById(R.id.other_category_et);
                                        new Builder(ReportPersonalThingActivity.this)
                                                .setTitle("Other Category")
                                                .setView(promptView)
                                                .setPositiveButton("OK", new OnClickListener() {
                                                    @Override
                                                    public void onClick(final DialogInterface dialogInterface,
                                                            final int i) {
                                                        selectedCategoryString = input.getText().toString();
                                                        mReportPtSelectCategory
                                                                .setText("Selected Category: "
                                                                        + selectedCategoryString);
                                                        mReportPtSelectCategory
                                                                .setTextColor(getResources()
                                                                        .getColor(R.color.defaultGreen));
                                                    }
                                                }).setNegativeButton("Cancel", new OnClickListener() {
                                            @Override
                                            public void onClick(final DialogInterface dialogInterface,
                                                    final int i) {
                                                selectedCategoryString = "";
                                                mReportPtSelectCategory
                                                        .setText("Select Category: Please select category");
                                                mReportPtSelectCategory
                                                        .setTextColor(getResources()
                                                                .getColor(R.color.defaultRed));
                                                dialogInterface.cancel();
                                            }
                                        }).create().show();
                                        break;
                                }
                            }
                        }).create().show();
                break;
            case R.id.report_pt_ibtn_place_picker:
                break;
            case R.id.report_pt_ibtn_date_picker:
                DateDialogFragment datePicker = new DateDialogFragment();
                datePicker.show(getSupportFragmentManager(), "date_picker");
                break;
            case R.id.report_pt_btn_add_image:
                View addImageDialog = getLayoutInflater().inflate(R.layout.dialog_add_image, null);
                mBottomSheetDialog.setContentView(addImageDialog);
                mBottomSheetDialog.show();
                addImageDialog.findViewById(R.id.dialog_image_open_camera).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(final View view) {
                                mBottomSheetDialog.hide();
                                Dexter.withActivity(ReportPersonalThingActivity.this)
                                        .withPermission(permission.CAMERA)
                                        .withListener(new PermissionListener() {
                                            @Override
                                            public void onPermissionDenied(final PermissionDeniedResponse response) {
                                                // check for permanent denial of permission
                                                if (response.isPermanentlyDenied()) {
                                                    // navigate user to app settings
                                                    mPermissionUtils.showSettingsDialog();
                                                }
                                            }

                                            @Override
                                            public void onPermissionGranted(
                                                    final PermissionGrantedResponse response) {
                                                mImageUtils.openCamera();
                                            }

                                            @Override
                                            public void onPermissionRationaleShouldBeShown(
                                                    final PermissionRequest permission, final PermissionToken token) {
                                                token.continuePermissionRequest();
                                            }
                                        }).check();
                            }
                        });
                addImageDialog.findViewById(R.id.dialog_image_browse_gallery).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(final View view) {
                                mBottomSheetDialog.hide();
                                Dexter.withActivity(ReportPersonalThingActivity.this)
                                        .withPermission(permission.WRITE_EXTERNAL_STORAGE)
                                        .withListener(new PermissionListener() {
                                            @Override
                                            public void onPermissionDenied(final PermissionDeniedResponse response) {
                                                // check for permanent denial of permission
                                                if (response.isPermanentlyDenied()) {
                                                    // navigate user to app settings
                                                    mPermissionUtils.showSettingsDialog();
                                                }
                                            }

                                            @Override
                                            public void onPermissionGranted(
                                                    final PermissionGrantedResponse response) {
                                                mImageUtils.getImageFromGallery();
                                            }

                                            @Override
                                            public void onPermissionRationaleShouldBeShown(
                                                    final PermissionRequest permission, final PermissionToken token) {
                                                token.continuePermissionRequest();
                                            }
                                        }).check();
                            }
                        });
                break;
            case R.id.report_pt_btn_submit_report:
                final PersonalThing personalThing = new PersonalThing();
                personalThing.setType(mType);
                personalThing.setItemName(mReportPtEtName.getText().toString());
                personalThing.setItemImagesUrl(mImagePaths);
                personalThing.setLocationData(mLocationData);
                personalThing.setDate(mReportPtEtDate.getRawText());
                personalThing.setColor(mReportPtEtColor.getText().toString());
                personalThing.setCategory(selectedCategoryString);
                personalThing.setBrand(mReportPtEtBrand.getText().toString());
                personalThing.setDescription(mReportPtEtDescription.getText().toString());
                personalThing.setAdditionalLocationInfo(mReportPtEtMoreLocationInfo.getText().toString());

                if (mType == Type.LOST) {
                    double reward = 0.0;
                    try {
                        reward = Double.parseDouble(mReportPtEtRewardAmount.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    personalThing.setReward(reward);

                    if (!mReportPtSwitchReward.isChecked()) {
                        personalThing.setReward((double) 0);
                    }
                }
                if (mType == Type.FOUND) {
                    //Found Type
                    BarangayData barangayData = new BarangayData();
                    barangayData.setName(mReportPtSpinnerBarangayNames.getSelectedItem().toString());
                    personalThing.setBarangayData(barangayData);
                    personalThing.setItemSurrendered(mReportPtSwitchTurnover.isChecked());
                }

                AlertDialog builder = new Builder(this)
                        .setTitle("Submit")
                        .setMessage("Are you sure to report this item?")
                        .setCancelable(true)
                        .setPositiveButton("Submit", new OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, final int i) {
                                mPresenter.submitReport(personalThing);
                            }
                        })
                        .setNegativeButton("Cancel", new OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, final int i) {
                                dialogInterface.cancel();
                            }
                        }).create();

                boolean isValidForm = mPresenter.validateReport(personalThing, mReportPtSwitchReward.isChecked());

                if (isValidForm) {
                    builder.show();
                }
                break;
            case R.id.report_pt_btn_delete:
                for (FrameLayout frame : mItemsMarkDeleteLayouts) {
                    frame.setVisibility(View.GONE);
                }
                mItemsMarkDeleteLayouts.clear();

                Collections.sort(mMarkToDeleteItems, Collections.<Integer>reverseOrder());

                for (final Integer position : mMarkToDeleteItems) {
                    mImagePaths.remove(mItemImagesAdapter.getItem(position).getPath());
                    mItemImagesAdapter.remove(position);
                }
                mMarkToDeleteItems.clear();
                mReportPtLinearlDelete.setVisibility(View.GONE);

                break;
            case R.id.report_pt_btn_cancel_delete:
                for (FrameLayout frame : mItemsMarkDeleteLayouts) {
                    frame.setVisibility(View.GONE);
                }
                mItemsMarkDeleteLayouts.clear();
                mMarkToDeleteItems.clear();
                mReportPtLinearlDelete.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onYesGiveReward() {
        mReportPtEtRewardAmount.setVisibility(View.VISIBLE);
        mReportPtEtRewardAmount.requestFocus();
    }

    @Override
    public void onYesTurnoverItem() {
        mReportPtSpinnerBarangayNames.setVisibility(View.VISIBLE);
    }

    @Override
    public void setBarangayNameList(final List<String> barangayList) {
        ArrayAdapter<String> brgyNamesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                barangayList);
        mReportPtSpinnerBarangayNames.setPrompt("Select Barangay");
        mReportPtSpinnerBarangayNames.setAdapter(brgyNamesAdapter);

    }

    @Override
    public void setBrandError(final String error) {
        mReportPtEtBrand.setError(error);
        mReportPtEtBrand.requestFocus();
    }

    @Override
    public void setCategoryError(final String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setColorError(final String error) {
        mReportPtEtColor.setError(error);
        mReportPtEtColor.requestFocus();
    }

    @Override
    public void setDateError(final String error) {
        mReportPtEtDate.setError(error);
        mReportPtEtDate.requestFocus();
    }

    @Override
    public void setDescriptionError(final String error) {
        mReportPtEtDescription.setError(error);
        mReportPtEtDescription.requestFocus();
    }

    @Override
    public void setImageError(final String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setItemNameError(final String error) {
        mReportPtEtName.setError(error);
        mReportPtEtName.requestFocus();
    }

    @Override
    public void setLocationError(final String error) {
        mReportPtEtLocation.setError(error);
        mReportPtEtLocation.requestFocus();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setRewardAmountError(final String error) {
        mReportPtEtRewardAmount.setError(error);
        mReportPtEtRewardAmount.requestFocus();
    }

    @Override
    public void setSelectedCategory(final Categories selectedCategory) {
        selectedCategoryString = selectedCategory.name().replace("_", " ");
        mReportPtSelectCategory.setText("Selected Category: " + selectedCategoryString);
        mReportPtSelectCategory.setTextColor(getResources().getColor(R.color.defaultGreen));
        clearCategorySelected();
        switch (selectedCategory) {
            case ID:
                mReportPtCatId.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                break;
            case BAG:
                mReportPtCatBag.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                break;
            case KEYS:
                mReportPtCatKeys.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                break;
            case LAPTOP:
                mReportPtCatLaptop.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                break;
            case WALLET:
                mReportPtCatWallet.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                break;
            case UMBRELLA:
                mReportPtCatUmbrella.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                break;
            case MOBILE_PHONE:
                mReportPtCatMobile.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                break;
            case SCHOOL_SUPPLIES:
                mReportPtCatSchoolSupplies.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                break;
        }
    }

    @Override
    public void showBrandOption() {
        mReportPtTvBrand.setVisibility(View.VISIBLE);
        mReportPtEtBrand.setVisibility(View.VISIBLE);
    }

    @Override
    protected void init() {
        showBackButton(true);
        mType = (Type) getIntent().getSerializableExtra(IntentExtraKeys.TYPE);
        setTitle("I " + mType.name().toLowerCase() + " something");
        mPresenter.checkType(mType);

        mReportPtRvImages.setAdapter(mItemImagesAdapter);
        mReportPtRvImages.setLayoutManager(new GridLayoutManager(this, 3));
        RecyclerItemUtils.addTo(mReportPtRvImages).setOnItemClickListener(this);
        RecyclerItemUtils.addTo(mReportPtRvImages).setOnLongClickListener(this);

        Places.initialize(this, getResources().getString(R.string.google_api_key));
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Field.ID, Field.NAME, Field.ADDRESS, Field.LAT_LNG));
        autocompleteFragment.a.setTextSize(14);
        autocompleteFragment.a.setTextColor(getResources().getColor(R.color.colorPrimaryText));
        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
            }

            @Override
            public void onPlaceSelected(Place place) {
                mLocationData.setId(place.getId());
                mLocationData.setName(place.getName());
                mLocationData.setAddress(place.getAddress());
                mLocationData.setLatlng(place.getLatLng());
                showAddMoreLocationInfo();
            }
        });
        // TODO: 6/15/19 Add Location Bias
    }

    private void clearCategorySelected() {
        mReportPtCatId.setCardBackgroundColor(Color.WHITE);
        mReportPtCatBag.setCardBackgroundColor(Color.WHITE);
        mReportPtCatKeys.setCardBackgroundColor(Color.WHITE);
        mReportPtCatLaptop.setCardBackgroundColor(Color.WHITE);
        mReportPtCatWallet.setCardBackgroundColor(Color.WHITE);
        mReportPtCatUmbrella.setCardBackgroundColor(Color.WHITE);
        mReportPtCatMobile.setCardBackgroundColor(Color.WHITE);
        mReportPtCatSchoolSupplies.setCardBackgroundColor(Color.WHITE);
    }

    private void showAddMoreLocationInfo() {
        mReportPtTvAddLocationInfo.setVisibility(View.VISIBLE);
        mReportPtTvAddLocationInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                mReportPtTvMoreLocationInfo.setVisibility(View.VISIBLE);
                mReportPtEtMoreLocationInfo.setVisibility(View.VISIBLE);
            }
        });
    }


}
