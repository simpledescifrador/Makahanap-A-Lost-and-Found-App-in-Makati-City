package com.makatizen.makahanap.ui.report.pet;

import android.Manifest.permission;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import br.com.sapereaude.maskedEditText.MaskedEditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.Place.Field;
import com.google.android.libraries.places.api.model.RectangularBounds;
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
import com.makatizen.makahanap.pojo.ItemImageDetails;
import com.makatizen.makahanap.pojo.LocationData;
import com.makatizen.makahanap.pojo.Pet;
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
import com.makatizen.makahanap.utils.enums.PetType;
import com.makatizen.makahanap.utils.enums.Type;
import com.yalantis.ucrop.UCrop;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;

public class ReportPetActivity extends BaseActivity implements ReportPetMvpView, OnDateSetListener,
        OnItemClickListener, OnLongClickListener {

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
    ReportPetMvpPresenter<ReportPetMvpView> mPresenter;

    @BindView(R.id.report_pet_btn_add_image)
    Button mReportPetBtnAddImage;

    @BindView(R.id.report_pet_btn_cancel_delete)
    Button mReportPetBtnCancelDelete;

    @BindView(R.id.report_pet_btn_delete)
    Button mReportPetBtnDelete;

    @BindView(R.id.report_pet_btn_submit_report)
    Button mReportPetBtnSubmitReport;

    @BindView(R.id.report_pet_condition_rg)
    RadioGroup mReportPetConditionRg;

    @BindView(R.id.report_pet_et_date)
    MaskedEditText mReportPetEtDate;

    @BindView(R.id.report_pet_et_description)
    EditText mReportPetEtDescription;

    @BindView(R.id.report_pet_et_location)
    EditText mReportPetEtLocation;

    @BindView(R.id.report_pet_et_more_location_info)
    EditText mReportPetEtMoreLocationInfo;

    @BindView(R.id.report_pet_et_name)
    EditText mReportPetEtName;

    @BindView(R.id.report_pet_et_reward_amount)
    EditText mReportPetEtRewardAmount;

    @BindView(R.id.report_pet_good_rb)
    RadioButton mReportPetGoodRb;

    @BindView(R.id.report_pet_ibtn_date_picker)
    ImageButton mReportPetIbtnDatePicker;

    @BindView(R.id.report_pet_ibtn_place_picker)
    ImageButton mReportPetIbtnPlacePicker;

    @BindView(R.id.report_pet_injured_rb)
    RadioButton mReportPetInjuredRb;

    @BindView(R.id.report_pet_linearl_delete)
    LinearLayout mReportPetLinearlDelete;

    @BindView(R.id.report_pet_reward_layout)
    LinearLayout mReportPetRewardLayout;

    @BindView(R.id.report_pet_rv_images)
    RecyclerView mReportPetRvImages;

    @BindView(R.id.report_pet_select_type)
    TextView mReportPetSelectType;

    @BindView(R.id.report_pet_switch_reward)
    Switch mReportPetSwitchReward;

    @BindView(R.id.report_pet_tv_add_location_info)
    TextView mReportPetTvAddLocationInfo;

    @BindView(R.id.report_pet_tv_condition)
    TextView mReportPetTvCondition;

    @BindView(R.id.report_pet_tv_date)
    TextView mReportPetTvDate;

    @BindView(R.id.report_pet_tv_description)
    TextView mReportPetTvDescription;

    @BindView(R.id.report_pet_tv_location)
    TextView mReportPetTvLocation;

    @BindView(R.id.report_pet_tv_more_location_info)
    TextView mReportPetTvMoreLocationInfo;

    @BindView(R.id.report_pet_tv_name)
    TextView mReportPetTvName;

    @BindView(R.id.report_pet_type_bird)
    CardView mReportPetTypeBird;

    @BindView(R.id.report_pet_type_cat)
    CardView mReportPetTypeCat;

    @BindView(R.id.report_pet_type_dog)
    CardView mReportPetTypeDog;

    @BindView(R.id.report_pet_type_others)
    CardView mReportPetTypeOthers;

    private List<String> mImagePaths = new ArrayList<>();

    private List<FrameLayout> mItemsMarkDeleteLayouts = new ArrayList<>();

    private LocationData mLocationData = new LocationData();

    private List<Integer> mMarkToDeleteItems = new ArrayList<>();

    private String mSelectedSubType;

    private String mSelectedType;

    private Type mType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityFragmentComponent().inject(this);
        mPresenter.attachView(this);

        setContentView(R.layout.activity_report_pet);
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
    public void onDateSet(final DatePicker datePicker, final int year, final int month, final int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String selectedDate = String.format("%d", year) + String.format("%02d", month + 1) + String
                .format("%02d", dayOfMonth);
        mReportPetEtDate.setText(selectedDate);
    }

    @Override
    public void onFoundItemType() {
        mReportPetTvName.setVisibility(View.GONE);
        mReportPetEtName.setVisibility(View.GONE);
        //Change Titles
        mReportPetTvLocation.setText(getResources().getString(R.string.hint_found_pet_location));
        mReportPetTvDate.setText(getResources().getString(R.string.hint_found_pet_date));
        mReportPetTvDescription.setText(getResources().getString(R.string.hint_found_pet_description));
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

        mReportPetLinearlDelete.setVisibility(View.VISIBLE);
        if (markItemSize == 0) {
            mReportPetLinearlDelete.setVisibility(View.GONE);
        } else if (markItemSize > 1) {
            mReportPetBtnDelete.setText("Delete All");
        } else {
            mReportPetBtnDelete.setText("Delete");
        }
    }

    @Override
    public void onLostItemType() {
        mReportPetTvName.setVisibility(View.VISIBLE);
        mReportPetEtName.setVisibility(View.VISIBLE);
        mReportPetRewardLayout.setVisibility(View.VISIBLE);
        mReportPetSwitchReward.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean isChecked) {
                if (isChecked) {
                    mReportPetEtRewardAmount.setVisibility(View.VISIBLE);
                } else {
                    mReportPetEtRewardAmount.setVisibility(View.GONE);
                }
            }
        });

        //Change Titles
        mReportPetTvName.setText(getResources().getString(R.string.hint_lost_pet_name));
        mReportPetTvLocation.setText(getResources().getString(R.string.hint_lost_pet_location));
        mReportPetTvDate.setText(getResources().getString(R.string.hint_lost_pet_date));
        mReportPetTvDescription.setText(getResources().getString(R.string.hint_lost_pet_description));
    }

    @Override
    public void onNoGiveReward() {
        mReportPetEtRewardAmount.setVisibility(View.GONE);
    }

    @Override
    public void onSubmitReportCompleted() {
        final Dialog dialog = mAppAlertDialog.showCustomDialog(AlertType.SUCCESS, "Your report has been submitted",
                "We will let you know when other pet was matched with your pet", "Okay", null);

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

    @OnClick({R.id.report_pet_type_dog, R.id.report_pet_type_cat, R.id.report_pet_type_bird,
            R.id.report_pet_type_others, R.id.report_pet_ibtn_place_picker,
            R.id.report_pet_ibtn_date_picker, R.id.report_pet_btn_add_image, R.id.report_pet_btn_delete,
            R.id.report_pet_btn_cancel_delete, R.id.report_pet_btn_submit_report})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.report_pet_type_dog:
                mPresenter.selectedPetType(PetType.DOG);
                break;
            case R.id.report_pet_type_cat:
                mPresenter.selectedPetType(PetType.CAT);
                break;
            case R.id.report_pet_type_bird:
                mPresenter.selectedPetType(PetType.BIRD);
                break;
            case R.id.report_pet_type_others:
                mPresenter.selectedPetType(PetType.OTHERS);
                break;
            case R.id.report_pet_ibtn_place_picker:

                break;
            case R.id.report_pet_ibtn_date_picker:
                DateDialogFragment datePicker = new DateDialogFragment();
                datePicker.show(getSupportFragmentManager(), "date_picker");
                break;
            case R.id.report_pet_btn_add_image:
                View addImageDialog = getLayoutInflater().inflate(R.layout.dialog_add_image, null);
                mBottomSheetDialog.setContentView(addImageDialog);
                mBottomSheetDialog.show();
                addImageDialog.findViewById(R.id.dialog_image_open_camera).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(final View view) {
                                mBottomSheetDialog.hide();
                                Dexter.withActivity(ReportPetActivity.this)
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
                                Dexter.withActivity(ReportPetActivity.this)
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
            case R.id.report_pet_btn_delete:
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
                mReportPetLinearlDelete.setVisibility(View.GONE);

                break;
            case R.id.report_pet_btn_cancel_delete:
                for (FrameLayout frame : mItemsMarkDeleteLayouts) {
                    frame.setVisibility(View.GONE);
                }
                mItemsMarkDeleteLayouts.clear();
                mMarkToDeleteItems.clear();
                mReportPetLinearlDelete.setVisibility(View.GONE);
                break;
            case R.id.report_pet_btn_submit_report:
                final Pet pet = new Pet();
                pet.setItemType(mType);
                pet.setBreed(mSelectedSubType);
                pet.setPetType(mSelectedType);
                pet.setDescription(mReportPetEtDescription.getText().toString());
                String condition = mReportPetGoodRb.isChecked() ? "Good" : "Injured";
                pet.setCondition(condition);
                pet.setDate(mReportPetEtDate.getRawText());
                pet.setLocationData(mLocationData);
                pet.setPetImagesUrl(mImagePaths);
                pet.setAdditionalLocationInfo(mReportPetEtMoreLocationInfo.getText().toString());
                if (mType == Type.LOST) {
                    pet.setName(mReportPetEtName.getText().toString());

                    double reward = 0.0;
                    try {
                        reward = Double.parseDouble(mReportPetEtRewardAmount.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    pet.setReward(reward);

                    if (!mReportPetSwitchReward.isChecked()) {
                        pet.setReward((double) 0);
                    }
                }

                AlertDialog builder = new AlertDialog.Builder(this)
                        .setTitle("Post")
                        .setMessage("Are you sure to post this")
                        .setCancelable(true)
                        .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, final int i) {
                                mPresenter.submitReport(pet);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, final int i) {
                                dialogInterface.cancel();
                            }
                        }).create();
                boolean isValidForm = mPresenter.validateReport(pet, mReportPetSwitchReward.isChecked());
                if (isValidForm) {
                    builder.show();
                }
                break;
        }
    }


    @Override
    public void onYesGiveReward() {
        mReportPetEtRewardAmount.setVisibility(View.VISIBLE);
        mReportPetEtRewardAmount.requestFocus();
    }

    @Override
    public void setDateError(final String error) {
        mReportPetEtDate.setError(error);
        mReportPetEtDate.requestFocus();
    }

    @Override
    public void setDescriptionError(final String error) {
        mReportPetEtDescription.setError(error);
        mReportPetEtDescription.requestFocus();
    }

    @Override
    public void setImageError(final String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setLocationError(final String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPetNameError(final String error) {
        mReportPetEtName.setError(error);
        mReportPetEtName.requestFocus();
    }

    @Override
    public void setPetTypeError(final String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setRewardAmountError(final String error) {
        mReportPetEtRewardAmount.setError(error);
        mReportPetEtRewardAmount.requestFocus();
    }

    @Override
    public void setSelectedType(final PetType petType) {
        mSelectedType = petType.name().replace("_", " ");
        mReportPetSelectType.setText("Selected Pet Type: " + mSelectedType);
        mReportPetSelectType.setTextColor(getResources().getColor(R.color.defaultGreen));
        clearCategorySelected();
        switch (petType) {
            case DOG:
                mReportPetTypeDog.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                break;
            case CAT:
                mReportPetTypeCat.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                break;
            case BIRD:
                mReportPetTypeBird.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                break;
            case OTHERS:
                mReportPetTypeOthers.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                break;
        }
    }

    @Override
    public void showPetSubType(final PetType petType) {
        switch (petType) {
            case DOG:
                final String[] dogTypes = getResources().getStringArray(R.array.dog_type);
                new Builder(this)
                        .setTitle("Select a Dog Type")
                        .setCancelable(false)
                        .setSingleChoiceItems(dogTypes, -1,
                                new OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialogInterface, final int i) {
                                        mSelectedSubType = dogTypes[i];
                                        if (dogTypes[i].equals("Others")) {
                                            // TODO: 3/22/19 Show Input Dialog
                                            showInputDialog(PetType.DOG);
                                            dialogInterface.dismiss();
                                        }
                                    }
                                }).setPositiveButton("Ok", new OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, final int i) {
                        mReportPetSelectType.setText(
                                "Selected Pet Type: " + mSelectedType + " > " + mSelectedSubType);
                        mReportPetSelectType
                                .setTextColor(getResources().getColor(R.color.defaultGreen));
                    }
                }).setNegativeButton("Cancel", new OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, final int i) {
                        dialogInterface.cancel();
                        mSelectedSubType = "";
                        mSelectedType = "";
                        mReportPetSelectType.setText("Selected Pet Type: Please select a type");
                        mReportPetSelectType.setTextColor(getResources().getColor(R.color.defaultRed));
                        clearCategorySelected();
                    }
                }).create().show();
                break;
            case CAT:
                final String[] catTypes = getResources().getStringArray(R.array.cat_type);
                new Builder(this)
                        .setTitle("Select a Cat Type")
                        .setCancelable(false)
                        .setSingleChoiceItems(catTypes, -1,
                                new OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialogInterface, final int i) {
                                        mSelectedSubType = catTypes[i];
                                        if (catTypes[i].equals("Others")) {
                                            showInputDialog(PetType.CAT);
                                            dialogInterface.dismiss();
                                        }
                                    }
                                }).setPositiveButton("Ok", new OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, final int i) {
                        mReportPetSelectType.setText(
                                "Selected Pet Type: " + mSelectedType + " > " + mSelectedSubType);
                        mReportPetSelectType
                                .setTextColor(getResources().getColor(R.color.defaultGreen));
                    }
                }).setNegativeButton("Cancel", new OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, final int i) {
                        dialogInterface.cancel();
                        mSelectedSubType = "";
                        mSelectedType = "";
                        mReportPetSelectType.setText("Selected Pet Type: Please select a type");
                        mReportPetSelectType.setTextColor(getResources().getColor(R.color.defaultRed));
                        clearCategorySelected();
                    }
                }).create().show();
                break;
            case BIRD:
                final String[] birdTypes = getResources().getStringArray(R.array.bird_type);
                new Builder(this)
                        .setTitle("Select a Bird Type")
                        .setCancelable(false)
                        .setSingleChoiceItems(birdTypes, -1,
                                new OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialogInterface, final int i) {
                                        mSelectedSubType = birdTypes[i];
                                        if (birdTypes[i].equals("Others")) {
                                            showInputDialog(PetType.BIRD);
                                            dialogInterface.dismiss();
                                        }
                                    }
                                }).setPositiveButton("Ok", new OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, final int i) {
                        mReportPetSelectType.setText(
                                "Selected Pet Type: " + mSelectedType + " > " + mSelectedSubType);
                        mReportPetSelectType
                                .setTextColor(getResources().getColor(R.color.defaultGreen));
                    }
                }).setNegativeButton("Cancel", new OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, final int i) {
                        dialogInterface.cancel();
                        mSelectedSubType = "";
                        mSelectedType = "";
                        mReportPetSelectType.setText("Selected Pet Type: Please select a type");
                        mReportPetSelectType.setTextColor(getResources().getColor(R.color.defaultRed));
                        clearCategorySelected();
                    }
                }).create().show();
                break;
            case OTHERS:
                // TODO: 3/20/19 Show input dialog
                LayoutInflater layoutInflater = LayoutInflater
                        .from(getApplicationContext());
                View promptView = layoutInflater
                        .inflate(R.layout.dialog_input, null);
                final EditText input = promptView
                        .findViewById(R.id.input_et);
                new Builder(this)
                        .setTitle("Other Pet Type")
                        .setCancelable(true)
                        .setView(promptView)
                        .setPositiveButton("OK", new OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, final int i) {
                                mSelectedType = input.getText().toString();
                                mReportPetSelectType.setText(
                                        "Selected Pet Type: " + mSelectedType);
                                mReportPetSelectType
                                        .setTextColor(getResources().getColor(R.color.defaultGreen));
                            }
                        }).setNegativeButton("Cancel", new OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, final int i) {
                        mSelectedType = "";
                        mSelectedSubType = "";
                        mReportPetSelectType.setText("Selected Category: Please select a type");
                        mReportPetSelectType.setTextColor(getResources().getColor(R.color.defaultRed));
                        dialogInterface.cancel();
                    }
                }).setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel(final DialogInterface dialogInterface) {
                        mSelectedType = "";
                        mSelectedSubType = "";
                        mReportPetSelectType.setText("Selected Category: Please select a type");
                        mReportPetSelectType.setTextColor(getResources().getColor(R.color.defaultRed));
                        dialogInterface.cancel();
                    }
                }).create().show();

                break;
        }
    }

    @Override
    protected void init() {
        showBackButton(true);
        mType = (Type) getIntent().getSerializableExtra(IntentExtraKeys.TYPE);
        setTitle("I " + mType.name().toLowerCase() + " something");
        mPresenter.checkType(mType);
        mReportPetRvImages.setAdapter(mItemImagesAdapter);
        mReportPetRvImages.setLayoutManager(new GridLayoutManager(this, 3));
        RecyclerItemUtils.addTo(mReportPetRvImages).setOnItemClickListener(this);
        RecyclerItemUtils.addTo(mReportPetRvImages).setOnLongClickListener(this);

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.report_pet_autocomplete_fragment);
        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(14.530872, 121.022232),
                new LatLng(14.568527, 121.045865));
        autocompleteFragment.setLocationBias(bounds);
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
    }

    private void clearCategorySelected() {
        mReportPetTypeDog.setCardBackgroundColor(Color.WHITE);
        mReportPetTypeCat.setCardBackgroundColor(Color.WHITE);
        mReportPetTypeBird.setCardBackgroundColor(Color.WHITE);
        mReportPetTypeOthers.setCardBackgroundColor(Color.WHITE);
    }

    private void showAddMoreLocationInfo() {
        mReportPetTvAddLocationInfo.setVisibility(View.VISIBLE);
        mReportPetTvAddLocationInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                mReportPetTvMoreLocationInfo.setVisibility(View.VISIBLE);
                mReportPetEtMoreLocationInfo.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showInputDialog(PetType type) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(getApplicationContext());
        View promptView = layoutInflater
                .inflate(R.layout.dialog_input, null);
        final EditText input = promptView
                .findViewById(R.id.input_et);
        AlertDialog alertDialog = new Builder(this)
                .setCancelable(true)
                .setView(promptView)
                .setPositiveButton("OK", new OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, final int i) {
                        mSelectedSubType = input.getText().toString();
                        mReportPetSelectType.setText(
                                "Selected Pet Type: " + mSelectedType + " > " + mSelectedSubType);
                        mReportPetSelectType
                                .setTextColor(getResources().getColor(R.color.defaultGreen));
                    }
                }).setNegativeButton("Cancel", new OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, final int i) {
                        mSelectedType = "";
                        mSelectedSubType = "";
                        mReportPetSelectType.setText("Selected Pet Type: Please select a type");
                        mReportPetSelectType.setTextColor(getResources().getColor(R.color.defaultRed));
                        dialogInterface.cancel();
                    }
                }).setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel(final DialogInterface dialogInterface) {
                        mSelectedType = "";
                        mSelectedSubType = "";
                        mReportPetSelectType.setText("Selected Pet Type: Please select a type");
                        mReportPetSelectType.setTextColor(getResources().getColor(R.color.defaultRed));
                        dialogInterface.cancel();
                        clearCategorySelected();
                    }
                }).create();

        switch (type) {
            case DOG:
                alertDialog.setTitle("Select Other Dog Type");
                alertDialog.show();
                break;
            case CAT:
                alertDialog.setTitle("Select Other Cat Type");
                alertDialog.show();
                break;
            case BIRD:
                alertDialog.setTitle("Select Other Bird Type");
                alertDialog.show();
                break;
        }
    }
}
