package com.makatizen.makahanap.ui.report.person;

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
import android.util.Log;
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
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
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
import com.makatizen.makahanap.pojo.ItemImageDetails;
import com.makatizen.makahanap.pojo.LocationData;
import com.makatizen.makahanap.pojo.Person;
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
import com.makatizen.makahanap.utils.enums.AgeGroup;
import com.makatizen.makahanap.utils.enums.Type;
import com.yalantis.ucrop.UCrop;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;

public class ReportPersonActivity extends BaseActivity implements ReportPersonMvpView, OnDateSetListener,
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
    ReportPersonMvpPresenter<ReportPersonMvpView> mPresenter;

    @BindView(R.id.report_person_age_adult)
    CardView mReportPersonAgeAdult;

    @BindView(R.id.report_person_age_child)
    CardView mReportPersonAgeChild;

    @BindView(R.id.report_person_age_senior)
    CardView mReportPersonAgeSenior;

    @BindView(R.id.report_person_age_teen)
    CardView mReportPersonAgeTeen;

    @BindView(R.id.report_person_btn_add_image)
    Button mReportPersonBtnAddImage;

    @BindView(R.id.report_person_btn_cancel_delete)
    Button mReportPersonBtnCancelDelete;

    @BindView(R.id.report_person_btn_delete)
    Button mReportPersonBtnDelete;

    @BindView(R.id.report_person_btn_submit_report)
    Button mReportPersonBtnSubmitReport;

    @BindView(R.id.report_person_et_date)
    MaskedEditText mReportPersonEtDate;

    @BindView(R.id.report_person_et_description)
    EditText mReportPersonEtDescription;

    @BindView(R.id.report_person_et_location)
    EditText mReportPersonEtLocation;

    @BindView(R.id.report_person_et_more_location_info)
    EditText mReportPersonEtMoreLocationInfo;

    @BindView(R.id.report_person_et_name)
    EditText mReportPersonEtName;

    @BindView(R.id.report_person_et_reward_amount)
    EditText mReportPersonEtRewardAmount;

    @BindView(R.id.report_person_ibtn_date_picker)
    ImageButton mReportPersonIbtnDatePicker;

    @BindView(R.id.report_person_ibtn_place_picker)
    ImageButton mReportPersonIbtnPlacePicker;

    @BindView(R.id.report_person_linearl_delete)
    LinearLayout mReportPersonLinearlDelete;

    @BindView(R.id.report_person_rb_gender_female)
    RadioButton mReportPersonRbGenderFemale;

    @BindView(R.id.report_person_rb_gender_male)
    RadioButton mReportPersonRbGenderMale;

    @BindView(R.id.report_person_reward_layout)
    LinearLayout mReportPersonRewardLayout;

    @BindView(R.id.report_person_rv_images)
    RecyclerView mReportPersonRvImages;

    @BindView(R.id.report_person_selected_age)
    TextView mReportPersonSelectedAge;

    @BindView(R.id.report_person_switch_reward)
    Switch mReportPersonSwitchReward;

    @BindView(R.id.report_person_tv_add_location_info)
    TextView mReportPersonTvAddLocationInfo;

    @BindView(R.id.report_person_tv_date)
    TextView mReportPersonTvDate;

    @BindView(R.id.report_person_tv_description)
    TextView mReportPersonTvDescription;

    @BindView(R.id.report_person_tv_gender)
    TextView mReportPersonTvGender;

    @BindView(R.id.report_person_tv_location)
    TextView mReportPersonTvLocation;

    @BindView(R.id.report_person_tv_more_location_info)
    TextView mReportPersonTvMoreLocationInfo;

    @BindView(R.id.report_person_tv_name)
    TextView mReportPersonTvName;

    @BindView(R.id.report_pet_rg_gender)
    RadioGroup mReportPetRgGender;

    @BindView(R.id.textView102)
    TextView mTextView102;

    private List<String> mImagePaths = new ArrayList<>();

    private List<FrameLayout> mItemsMarkDeleteLayouts = new ArrayList<>();

    private LocationData mLocationData = new LocationData();

    private List<Integer> mMarkToDeleteItems = new ArrayList<>();

    private String mSelectedAgeGroup;

    private String mSelectedAgeRanged;

    private Type mType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityFragmentComponent().inject(this);
        mPresenter.attachView(this);
        setContentView(R.layout.activity_report_person);
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
        mReportPersonEtDate.setText(selectedDate);
    }

    @Override
    public void onFoundItemType() {
        mReportPersonTvName.setVisibility(View.GONE);
        mReportPersonEtName.setVisibility(View.GONE);

        //Change Titles
        mReportPersonTvLocation.setText(getResources().getString(R.string.hint_found_person_location));
        mReportPersonTvDate.setText(getResources().getString(R.string.hint_found_person_date));
        mReportPersonTvGender.setText(getResources().getString(R.string.hint_found_person_gender));
        mReportPersonTvDescription.setText(getResources().getString(R.string.hint_found_person_description));
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

        mReportPersonLinearlDelete.setVisibility(View.VISIBLE);
        if (markItemSize == 0) {
            mReportPersonLinearlDelete.setVisibility(View.GONE);
        } else if (markItemSize > 1) {
            mReportPersonBtnDelete.setText("Delete All");
        } else {
            mReportPersonBtnDelete.setText("Delete");
        }
    }

    @Override
    public void onLostItemType() {
        mReportPersonTvName.setVisibility(View.VISIBLE);
        mReportPersonEtName.setVisibility(View.VISIBLE);
        mReportPersonRewardLayout.setVisibility(View.VISIBLE);
        mReportPersonSwitchReward.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean isChecked) {
                if (isChecked) {
                    mReportPersonEtRewardAmount.setVisibility(View.VISIBLE);
                } else {
                    mReportPersonEtRewardAmount.setVisibility(View.GONE);
                }
            }
        });

        //Change Titles
        mReportPersonTvName.setText(getResources().getString(R.string.hint_lost_person_name));
        mReportPersonTvLocation.setText(getResources().getString(R.string.hint_lost_person_location));
        mReportPersonTvDate.setText(getResources().getString(R.string.hint_lost_person_date));
        mReportPersonTvDescription.setText(getResources().getString(R.string.hint_lost_person_description));
        mReportPersonTvGender.setText(getResources().getString(R.string.hint_lost_person_gender));

    }

    @Override
    public void onNoGiveReward() {
        mReportPersonEtRewardAmount.setVisibility(View.GONE);
    }

    @Override
    public void onSubmitReportCompleted() {
        final Dialog dialog = mAppAlertDialog.showCustomDialog(AlertType.SUCCESS, "Your report has been submitted",
                "We will let you know when person was matched with your person", "Okay", null);

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

    @OnClick({R.id.report_person_age_child, R.id.report_person_age_teen, R.id.report_person_age_adult,
            R.id.report_person_age_senior, R.id.report_person_ibtn_place_picker, R.id.report_person_ibtn_date_picker,
            R.id.report_person_btn_add_image, R.id.report_person_btn_delete, R.id.report_person_btn_cancel_delete,
            R.id.report_person_btn_submit_report})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.report_person_age_child:
                mPresenter.selectedAgeGroup(AgeGroup.CHILD);
                break;
            case R.id.report_person_age_teen:
                mPresenter.selectedAgeGroup(AgeGroup.TEEN);
                break;
            case R.id.report_person_age_adult:
                mPresenter.selectedAgeGroup(AgeGroup.ADULT);
                break;
            case R.id.report_person_age_senior:
                mPresenter.selectedAgeGroup(AgeGroup.SENIOR);
                break;
            case R.id.report_person_ibtn_place_picker:
                break;
            case R.id.report_person_ibtn_date_picker:
                DateDialogFragment datePicker = new DateDialogFragment();
                datePicker.show(getSupportFragmentManager(), "date_picker");
                break;
            case R.id.report_person_btn_add_image:
                View addImageDialog = getLayoutInflater().inflate(R.layout.dialog_add_image, null);
                mBottomSheetDialog.setContentView(addImageDialog);
                mBottomSheetDialog.show();
                addImageDialog.findViewById(R.id.dialog_image_open_camera).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(final View view) {
                                mBottomSheetDialog.hide();
                                Dexter.withActivity(ReportPersonActivity.this)
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
                                Dexter.withActivity(ReportPersonActivity.this)
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
            case R.id.report_person_btn_delete:
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
                mReportPersonLinearlDelete.setVisibility(View.GONE);

                break;
            case R.id.report_person_btn_cancel_delete:
                for (FrameLayout frame : mItemsMarkDeleteLayouts) {
                    frame.setVisibility(View.GONE);
                }
                mItemsMarkDeleteLayouts.clear();
                mMarkToDeleteItems.clear();
                mReportPersonLinearlDelete.setVisibility(View.GONE);
                break;
            case R.id.report_person_btn_submit_report:
                final Person person = new Person();
                person.setAgeGroup(mSelectedAgeGroup);
                person.setAgeRange(mSelectedAgeRanged);
                person.setDescription(mReportPersonEtDescription.getText().toString());
                person.setSex(mReportPersonRbGenderMale.isChecked() ? "Male" : "Female");
                person.setDate(mReportPersonEtDate.getRawText());
                person.setLocationData(mLocationData);
                person.setPersonImagesUrl(mImagePaths);
                person.setAdditionalLocationInfo(mReportPersonEtMoreLocationInfo.getText().toString());

                if (mType == Type.LOST) {
                    person.setName(mReportPersonEtName.getText().toString());

                    double reward = 0.0;
                    try {
                        reward = Double.parseDouble(mReportPersonEtRewardAmount.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    person.setReward(reward);

                    if (!mReportPersonSwitchReward.isChecked()) {
                        person.setReward((double) 0);
                    }
                }
                AlertDialog builder = new AlertDialog.Builder(this)
                        .setTitle("Submit")
                        .setMessage("Are you sure to report this item?")
                        .setCancelable(true)
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, final int i) {
                                mPresenter.submitReport(person);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, final int i) {
                                dialogInterface.cancel();
                            }
                        }).create();

                boolean isValidForm = mPresenter.validateReport(person, mReportPersonSwitchReward.isChecked());

                if (isValidForm) {
                    builder.show();
                }
                break;
        }
    }

    @Override
    public void onYesGiveReward() {
        mReportPersonEtRewardAmount.setVisibility(View.VISIBLE);
        mReportPersonEtRewardAmount.requestFocus();
    }

    @Override
    public void setDateError(final String error) {
        mReportPersonEtDate.setError(error);
        mReportPersonEtDate.requestFocus();
    }

    @Override
    public void setDescriptionError(final String error) {
        mReportPersonEtDescription.setError(error);
        mReportPersonEtDescription.requestFocus();
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
    public void setPersonAgeError(final String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPersonNameError(final String error) {
        mReportPersonEtName.setError(error);
        mReportPersonEtName.requestFocus();
    }

    @Override
    public void setRewardAmountError(final String error) {
        mReportPersonEtRewardAmount.setError(error);
        mReportPersonEtRewardAmount.requestFocus();
    }

    @Override
    public void showAgeRangeSeekDialog(final AgeGroup ageGroup) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(getApplicationContext());
        View promptView = layoutInflater
                .inflate(R.layout.dialog_age_seek_bar, null);
        final TextView rangeText = promptView.findViewById(R.id.seek_bar_range_tv);
        final TextView minValueText = promptView.findViewById(R.id.seek_bar_min);
        final TextView maxValueText = promptView.findViewById(R.id.seek_bar_max);
        final CrystalRangeSeekbar rangeSeekbar = (CrystalRangeSeekbar) promptView
                .findViewById(R.id.range_seek_bar);
        switch (ageGroup) {
            case CHILD: //Age Range 1-12
                rangeSeekbar.setMinValue(1);
                rangeSeekbar.setMaxValue(12);
                minValueText.setText("Min: 1");
                maxValueText.setText("Max: 12");
                mSelectedAgeGroup = "Child";
                mSelectedAgeRanged = "1 to 12";
                break;
            case TEEN: //Age Range 13-21
                rangeSeekbar.setMinValue(13);
                rangeSeekbar.setMaxValue(21);
                minValueText.setText("Min: 13");
                maxValueText.setText("Max: 21");
                mSelectedAgeGroup = "Teen";
                mSelectedAgeRanged = "13 to 21";
                break;
            case ADULT: //Age Range 22-59
                rangeSeekbar.setMinValue(22);
                rangeSeekbar.setMaxValue(59);
                minValueText.setText("Min: 22");
                maxValueText.setText("Max: 59");
                mSelectedAgeGroup = "Adult";
                mSelectedAgeRanged = "22 to 59";
                break;
            case SENIOR: //Age Range 60-Above
                rangeSeekbar.setMinValue(60);
                rangeSeekbar.setMaxValue(120);
                minValueText.setText("Min: 60");
                maxValueText.setText("Max: 120");
                mSelectedAgeGroup = "Senior";
                mSelectedAgeRanged = "60 to 120";
                break;
        }
        // set listener
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                rangeText.setText("Between: " + minValue + " to " + maxValue + " years old");
            }
        });

        // set final value listener
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
                mSelectedAgeRanged = String.valueOf(minValue) + " to " + String.valueOf(maxValue);
            }
        });
        AlertDialog alertDialog = new Builder(this)
                .setCancelable(false)
                .setView(promptView)
                .setTitle("Select Age Range")
                .setPositiveButton("OK", new OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, final int i) {
                        mReportPersonSelectedAge.setText(
                                "Selected Person Age: " + mSelectedAgeGroup + " at " + mSelectedAgeRanged
                                        + " years old");
                        mReportPersonSelectedAge.setTextColor(getResources().getColor(R.color.defaultGreen));
                    }
                }).setNegativeButton("Cancel", new OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, final int i) {
                        mSelectedAgeRanged = "";
                        mSelectedAgeGroup = "";
                        mReportPersonSelectedAge.setText("Selected Person Age: Nothing Selected");
                        mReportPersonSelectedAge.setTextColor(getResources().getColor(R.color.defaultRed));
                        clearSelectedAgeGroup();
                        dialogInterface.cancel();
                    }
                }).create();
        alertDialog.show();
    }

    @Override
    protected void init() {
        showBackButton(true);
        mType = (Type) getIntent().getSerializableExtra(IntentExtraKeys.TYPE);
        setTitle("I " + mType.name().toLowerCase() + " something");
        mPresenter.checkType(mType);

        mReportPersonRvImages.setAdapter(mItemImagesAdapter);
        mReportPersonRvImages.setLayoutManager(new GridLayoutManager(this, 3));
        RecyclerItemUtils.addTo(mReportPersonRvImages).setOnItemClickListener(this);
        RecyclerItemUtils.addTo(mReportPersonRvImages).setOnLongClickListener(this);

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.report_person_autocomplete_fragment);

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

    private void clearSelectedAgeGroup() {
        mReportPersonAgeChild.setCardBackgroundColor(Color.WHITE);
        mReportPersonAgeTeen.setCardBackgroundColor(Color.WHITE);
        mReportPersonAgeAdult.setCardBackgroundColor(Color.WHITE);
        mReportPersonAgeSenior.setCardBackgroundColor(Color.WHITE);
    }

    private void showAddMoreLocationInfo() {
        mReportPersonTvAddLocationInfo.setVisibility(View.VISIBLE);
        mReportPersonTvAddLocationInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                mReportPersonTvMoreLocationInfo.setVisibility(View.VISIBLE);
                mReportPersonEtMoreLocationInfo.setVisibility(View.VISIBLE);
            }
        });
    }
}
