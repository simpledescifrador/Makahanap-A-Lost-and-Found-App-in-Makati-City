package com.makatizen.makahanap.ui.return_item;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.ui.base.BaseActivity;
import com.makatizen.makahanap.utils.AppAlertDialog;
import com.makatizen.makahanap.utils.ImageUtils;
import com.makatizen.makahanap.utils.IntentExtraKeys;
import com.makatizen.makahanap.utils.PermissionUtils;
import com.makatizen.makahanap.utils.RequestCodes;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReturnItemActivity extends BaseActivity implements ReturnItemMvpView {
    @Inject
    ImageUtils mImageUtils;
    @Inject
    AppAlertDialog mAppAlertDialog;
    @Inject
    PermissionUtils mPermissionUtils;
    @Inject
    ReturnItemMvpPresenter<ReturnItemMvpView> mPresenter;
    @BindView(R.id.return_location)
    TextView mReturnLocation;
    @BindView(R.id.return_date)
    TextView mReturnDate;
    @BindView(R.id.return_upload_image_iv)
    ImageView mReturnUploadImageIv;
    @BindView(R.id.return_upload_image_label)
    TextView mReturnUploadImageLabel;
    @BindView(R.id.return_upload_image_option)
    FrameLayout mReturnUploadImageOption;
    @BindView(R.id.return_submit_btn)
    Button mReturnSubmitBtn;
    @Inject
    BottomSheetDialog mBottomSheetDialog;
    private String mImagePath = "";
    private int mItemId;
    private int mMeetupId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityFragmentComponent().inject(this);
        mPresenter.attachView(this);

        setContentView(R.layout.activity_return_item);
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
                    mImagePath = imageUriResultCrop.getPath();
                    mReturnUploadImageLabel.setVisibility(View.INVISIBLE);
                    Glide.with(this)
                            .load(mImagePath)
                            .into(mReturnUploadImageIv);
                    break;

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBottomSheetDialog.dismiss();
        mPresenter.detachView();
    }

    @Override
    protected void init() {
        showBackButton(true);
        setTitle("Set item as return");
        mItemId = getIntent().getIntExtra(IntentExtraKeys.ITEM_ID, 0);
        mMeetupId = getIntent().getIntExtra(IntentExtraKeys.MEET_ID, 0);
        mPresenter.loadMeetingDetails(mMeetupId);
    }

    @OnClick({R.id.return_upload_image_option, R.id.return_submit_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.return_upload_image_option:
                View addImageDialog = getLayoutInflater().inflate(R.layout.dialog_add_image, null);
                mBottomSheetDialog.setContentView(addImageDialog);
                mBottomSheetDialog.show();
                addImageDialog.findViewById(R.id.dialog_image_open_camera).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(final View view) {
                                mBottomSheetDialog.hide();
                                Dexter.withActivity(ReturnItemActivity.this)
                                        .withPermission(Manifest.permission.CAMERA)
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
                                Dexter.withActivity(ReturnItemActivity.this)
                                        .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
            case R.id.return_submit_btn:
                boolean isValid = mPresenter.validate(mImagePath);
                if (isValid) {
                    new AlertDialog.Builder(this)
                            .setTitle("Submit")
                            .setMessage("Are yor sure to set return item")
                            .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mPresenter.submit(mMeetupId, mItemId, mImagePath);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).create().show();
                }
                break;
        }
    }

    @Override
    public void setMeetUpDetails(String location, String date) {
        mReturnLocation.setText(location);
        mReturnDate.setText(date.toUpperCase());
    }

    @Override
    public void onSubmitCompleted() {
        setResult(RESULT_OK);
        finish();
    }
}
