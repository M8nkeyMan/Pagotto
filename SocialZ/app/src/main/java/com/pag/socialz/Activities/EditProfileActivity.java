package com.pag.socialz.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pag.socialz.Listeners.OnObjectChangedListener;
import com.pag.socialz.Listeners.OnProfileCreatedListener;
import com.pag.socialz.Managers.ProfileManager;
import com.pag.socialz.Managers.ValidationUtils;
import com.pag.socialz.Models.Profile;
import com.pag.socialz.R;

public class EditProfileActivity extends ImagePickerActivity implements OnProfileCreatedListener {

    private static final String TAG = EditProfileActivity.class.getSimpleName();

    private EditText nameEditText;
    private ImageView imageView;
    private ProgressBar progressBar;

    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        progressBar = findViewById(R.id.progressBar);
        imageView = findViewById(R.id.imageView);
        nameEditText = findViewById(R.id.nameEditText);
        showProgress();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        ProfileManager.getInstance(this).getProfileSingleValue(firebaseUser.getUid(), createOnProfileChangedListener());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectImageClick(view);
            }
        });

    }

    @Override
    protected ProgressBar getProgressView() {
        return progressBar;
    }

    @Override
    protected ImageView getImageView() {
        return imageView;
    }

    @Override
    protected void onImagePickedAction() {
        startCropImageActivity();
    }

    @Override
    public void onProfileCreated(boolean success) {
        hideProgress();
        if (success) finish();
        else showSnackBar(R.string.error_fail_update_profile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        handleCropImageResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if (hasInternetConnection()) attemptCreateProfile();
                else showSnackBar(R.string.internet_connection_failed);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private OnObjectChangedListener<Profile> createOnProfileChangedListener() {
        return new OnObjectChangedListener<Profile>() {
            @Override
            public void onObjectChanged(Profile obj) {
                profile = obj;
                fillUIFields();
            }
        };
    }

    private void fillUIFields() {
        if (profile != null) {
            nameEditText.setText(profile.getUsername());

            if (profile.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(profile.getPhotoUrl())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .crossFade()
                        .error(R.drawable.ic_stub)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(imageView);
            }
        }
        hideProgress();
        nameEditText.requestFocus();
    }

    private void attemptCreateProfile() {
        nameEditText.setError(null);
        String name = nameEditText.getText().toString().trim();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(name)) {
            nameEditText.setError(getString(R.string.error_field_required));
            focusView = nameEditText;
            cancel = true;
        } else if (!ValidationUtils.isNameValid(name)) {
            nameEditText.setError(getString(R.string.error_profile_name_length));
            focusView = nameEditText;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress();
            profile.setUsername(name);
            ProfileManager.getInstance(this).createOrUpdateProfile(profile, imageUri, this);
        }
    }
}
