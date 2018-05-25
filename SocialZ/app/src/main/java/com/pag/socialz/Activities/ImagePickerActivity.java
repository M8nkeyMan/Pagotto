package com.pag.socialz.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pag.socialz.Managers.LogUtil;
import com.pag.socialz.Managers.ValidationUtils;
import com.pag.socialz.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;

public abstract class ImagePickerActivity extends BaseActivity{

    private static final String TAG = ImagePickerActivity.class.getSimpleName();
    protected static final int MAX_FILE_SIZE_BYTES = 10485760; //10mb
    private static final String SAVED_IMAGE_URI = "";

    protected Uri imageUri;

    protected abstract ProgressBar getProgressView();

    protected abstract ImageView getImageView();

    protected abstract void onImagePickedAction();

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(SAVED_IMAGE_URI, imageUri);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVED_IMAGE_URI)) {
                imageUri = savedInstanceState.getParcelable(SAVED_IMAGE_URI);
                loadImageToImageView();
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void onSelectImageClick(View view){

    }

    protected void loadImageToImageView(){
        if(imageUri == null) return;
        Glide.with(this)
                .load(imageUri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .fitCenter()
                .listener(new RequestListener<Uri, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        getProgressView().setVisibility(View.GONE);
                        LogUtil.logDebug(TAG, "Glide success loading from: "+imageUri.getPath());
                        return false;
                    }
                })
                .into(getImageView());
    }
    protected boolean isImageFileValid(Uri imageUri) {
        int message = R.string.error_general;
        boolean result = false;

        if (imageUri != null) {
            if (ValidationUtils.isImage(imageUri, this)) {
                File imageFile = new File(imageUri.getPath());
                if (imageFile.length() > MAX_FILE_SIZE_BYTES) {
                    message = R.string.error_bigger_file;
                } else {
                    result = true;
                }
            } else {
                message = R.string.error_incorrect_file_type;
            }
        }

        if (!result) {
            showSnackBar(message);
            getProgressView().setVisibility(View.GONE);
        }

        return result;
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            if (isImageFileValid(imageUri)) {
                this.imageUri = imageUri;
            }

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            } else {
                // no permissions required or already grunted
                onImagePickedAction();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LogUtil.logDebug(TAG, "CAMERA_CAPTURE_PERMISSIONS granted");
                CropImage.startPickImageActivity(this);
            } else {
                showSnackBar(R.string.permissions_not_granted);
                LogUtil.logDebug(TAG, "CAMERA_CAPTURE_PERMISSIONS not granted");
            }
        }
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (imageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LogUtil.logDebug(TAG, "PICK_IMAGE_PERMISSIONS granted");
                onImagePickedAction();
            } else {
                showSnackBar(R.string.permissions_not_granted);
                LogUtil.logDebug(TAG, "PICK_IMAGE_PERMISSIONS not granted");
            }
        }
    }

}
