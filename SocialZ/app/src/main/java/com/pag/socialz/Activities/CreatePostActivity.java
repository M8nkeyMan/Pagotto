package com.pag.socialz.Activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.pag.socialz.Listeners.OnPostCreatedListener;
import com.pag.socialz.Util.LogUtil;
import com.pag.socialz.Managers.PostManager;
import com.pag.socialz.Managers.ValidationUtils;
import com.pag.socialz.Models.Post;
import com.pag.socialz.R;

public class CreatePostActivity extends ImagePickerActivity implements OnPostCreatedListener {

    private static final String TAG = CreatePostActivity.class.getSimpleName();
    public static final int CREATE_NEW_POST_REQUEST = 11;

    protected ImageView imageView;
    protected ProgressBar progressBar;
    protected EditText titleEditText;
    protected EditText descriptionEditText;

    protected PostManager postManager;
    protected boolean creatingPost = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        if(actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        postManager = PostManager.getInstance(CreatePostActivity.this);
        imageView = findViewById(R.id.imageView);
        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        progressBar = findViewById(R.id.progressBar);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectImageClick(view);
            }
        });
        titleEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(titleEditText.hasFocus() && titleEditText.getError() != null){
                    titleEditText.setError(null);
                    return true;
                }
                return false;
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
        loadImageToImageView();
    }

    @Override
    public void onPostSaved(boolean success) {
        hideProgress();
        if(success){
            setResult(RESULT_OK);
            CreatePostActivity.this.finish();
            LogUtil.logDebug(TAG,"Post was created");
        }else{
            creatingPost = false;
            showSnackBar(R.string.error_fail_create_post);
            LogUtil.logDebug(TAG, "Failed to create a post");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_post_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.post:
                if (!creatingPost) {
                    if (hasInternetConnection()) {
                        attemptCreatePost();
                    } else {
                        showSnackBar(R.string.internet_connection_failed);
                    }
                }

                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    protected void attemptCreatePost() {
        titleEditText.setError(null);
        descriptionEditText.setError(null);
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        View focusView = null;
        boolean cancel = false;
        if (TextUtils.isEmpty(description)) {
            descriptionEditText.setError(getString(R.string.warning_empty_description));
            focusView = descriptionEditText;
            cancel = true;
        }
        if (TextUtils.isEmpty(title)) {
            titleEditText.setError(getString(R.string.warning_empty_title));
            focusView = titleEditText;
            cancel = true;
        } else if (!ValidationUtils.isPostTitleValid(title)) {
            titleEditText.setError(getString(R.string.error_post_title_length));
            focusView = titleEditText;
            cancel = true;
        }
        if (!(this instanceof EditPostActivity) && imageUri == null) {
            showWarningDialog(R.string.warning_empty_image);
            focusView = imageView;
            cancel = true;
        }

        if (!cancel) {
            creatingPost = true;
            hideSoftInput();
            savePost(title, description);
        } else if (focusView != null) {
            focusView.requestFocus();
        }
    }

    protected void savePost(String title, String description){
        showProgress(R.string.message_creating_post);
        Post post = new Post();
        post.setTitle(title);
        post.setDescription(description);
        post.setAuthorId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        postManager.createOrUpdatePostWithImage(imageUri,CreatePostActivity.this,post);
    }
}
