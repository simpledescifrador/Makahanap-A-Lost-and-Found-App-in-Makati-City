package com.makatizen.makahanap.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.v4.content.FileProvider;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.utils.enums.Type;
import com.yalantis.ucrop.UCrop;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.inject.Inject;

public class ImageUtils {

    private Activity mActivity;

    private String mImagePath = "";

    @Inject
    public ImageUtils(final Activity activity) {
        mActivity = activity;
    }

    public Bitmap getBitmapFromUri(Uri uri) throws IOException {
        return MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), uri);
    }

    public int getFileSizeInKb(File file) {
        return Integer.parseInt(String.valueOf(file.length() / 1024));
    }

    public void getImageFromGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        mActivity.startActivityForResult(pickPhoto, RequestCodes.OPEN_GALLERY);
    }

    public String getImagePath() {
        return mImagePath;
    }

    public long getImageSizeInKb(Uri uri) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = getBitmapFromUri(uri);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        return imageInByte.length;
    }

    public void openCamera() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider
                        .getUriForFile(mActivity, "com.makatizen.makahanap.provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI);
                mActivity.startActivityForResult(pictureIntent, RequestCodes.OPEN_CAMERA);
            }
        }
    }

    public void startImageCrop(Uri sourceUri) {
        Type type = (Type) mActivity.getIntent().getSerializableExtra(IntentExtraKeys.TYPE);
        UCrop uCrop = UCrop.of(sourceUri, Uri.fromFile(
                new File(mActivity.getCacheDir(), "LF" + "_" + System.currentTimeMillis() + ".jpg")));
        uCrop.withOptions(getCropOptions());
        uCrop.start(mActivity);
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mImagePath = image.getAbsolutePath();
        return image;
    }

    private UCrop.Options getCropOptions() {
        UCrop.Options options = new UCrop.Options();
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);
        options.setStatusBarColor(mActivity.getResources().getColor(R.color.colorPrimaryDark));
        options.setToolbarColor(mActivity.getResources().getColor(R.color.colorPrimary));

        options.setToolbarTitle("Crop Image");
        return options;
    }
}
