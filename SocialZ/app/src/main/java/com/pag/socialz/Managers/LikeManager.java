package com.pag.socialz.Managers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.pag.socialz.Activities.BaseActivity;
import com.pag.socialz.Activities.MainActivity;
import com.pag.socialz.ApplicationHelper;
import com.pag.socialz.Enums.ProfileStatus;
import com.pag.socialz.Listeners.OnObjectExistListener;
import com.pag.socialz.Models.Post;
import com.pag.socialz.R;

public class LikeManager {

    private static final int ANIMATION_DURATION = 300;

    public enum AnimationType{
        COLOR_ANIM, BOUNCE_ANIM;
    }

    private Context context;
    private String postId;
    private String postAuthorId;

    private AnimationType likeAnimationType = AnimationType.BOUNCE_ANIM;

    private TextView likesCounterTextViev;
    private ImageView likesImageView;

    private boolean isListView = false;
    private boolean isLiked = false;
    private boolean updatingLikeCounter = true;

    public LikeManager(Context context, Post post, TextView likesCounterTextViev, ImageView likesImageView, boolean isListView){
        this.context = context;
        this.postId = post.getId();
        this.postAuthorId = post.getAuthorId();
        this.likesCounterTextViev = likesCounterTextViev;
        this.likesImageView = likesImageView;
        this.isListView = isListView;
    }

    private void startAnimateLikeButton(AnimationType animationType){
        switch (animationType){
            case COLOR_ANIM:
                colorAnimateImageView();
                break;
            case BOUNCE_ANIM:
                bounceAnimateImageView();
                break;
        }
    }

    private void bounceAnimateImageView(){
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(likesImageView, "scaleX",0.2f,1f);
        bounceAnimX.setDuration(ANIMATION_DURATION);
        bounceAnimX.setInterpolator(new BounceInterpolator());
        ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(likesImageView, "scaleY",0.2f,1f);
        bounceAnimY.setDuration(ANIMATION_DURATION);
        bounceAnimY.setInterpolator(new BounceInterpolator());
        bounceAnimY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                likesImageView.setImageResource(!isLiked?R.drawable.ic_like_activated : R.drawable.ic_like);
            }
        });
        animatorSet.play(bounceAnimX).with(bounceAnimY);
        animatorSet.start();
    }

    private void colorAnimateImageView(){
        final int activatedColor = context.getResources().getColor(R.color.like_icon_activated);
        final ValueAnimator colorAnimator = !isLiked?ObjectAnimator.ofFloat(0f,1f):ObjectAnimator.ofFloat(1f,0f);
        colorAnimator.setDuration(ANIMATION_DURATION);
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float mul = (Float) valueAnimator.getAnimatedValue();
                int alpha = adjustAlpha(activatedColor, mul);
                likesImageView.setColorFilter(alpha, PorterDuff.Mode.SRC_ATOP);
                if(mul == 0.0){
                    likesImageView.setColorFilter(null);
                }
            }
        });
        colorAnimator.start();
    }

    private int adjustAlpha(int color, float factor){
        int alpha = Math.round(Color.alpha(color)* factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    private void addLike(long previous) {
        updatingLikeCounter = true;
        isLiked = true;
        likesCounterTextViev.setText(String.valueOf(previous + 1));
        ApplicationHelper.getDatabaseManager().createOrUpdateLike(postId, postAuthorId);
    }

    private void removeLike(long previous) {
        updatingLikeCounter = true;
        isLiked = false;
        likesCounterTextViev.setText(String.valueOf(previous - 1));
        ApplicationHelper.getDatabaseManager().removeLike(postId, postAuthorId);
    }

    private void updateLocalPostLikeCounter(Post post){
        if(isLiked){
            post.setLikesCount(post.getLikesCount() + 1);
        }else{
            post.setLikesCount(post.getLikesCount() - 1);
        }
    }

    private void showWarningMessage(BaseActivity baseActivity, int messageId){
        if(baseActivity instanceof MainActivity){
            ((MainActivity)baseActivity).showFloatButtonRelatedSnackBar(messageId);
        }else{
            baseActivity.showSnackBar(messageId);
        }
    }

    public void likeClickActionLocal(Post post){
        setUpdatingLikeCounter(false);
        likeClickAction(post.getLikesCount());
        updateLocalPostLikeCounter(post);
    }

    public void likeClickAction(long previous){
        if(!updatingLikeCounter){
            startAnimateLikeButton(likeAnimationType);
            if(!isLiked) addLike(previous);
            else removeLike(previous);
        }
    }

    public AnimationType getLikeAnimationType() {
        return likeAnimationType;
    }

    public void setLikeAnimationType(AnimationType likeAnimationType) {
        this.likeAnimationType = likeAnimationType;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isUpdatingLikeCounter() {
        return updatingLikeCounter;
    }

    public void setUpdatingLikeCounter(boolean updatingLikeCounter) {
        this.updatingLikeCounter = updatingLikeCounter;
    }

    public void handleLikeClickAction(final BaseActivity baseActivity, final Post post){
        PostManager.getInstance(baseActivity.getApplicationContext()).isPostExistSingleValue(post.getId(), new OnObjectExistListener<Post>() {
            @Override
            public void onDataChanged(boolean exist) {
                if(exist)
                    if(baseActivity.hasInternetConnection()) doHandleLikeClickAction(baseActivity,post);
                    else showWarningMessage(baseActivity,R.string.internet_connection_failed);
                else showWarningMessage(baseActivity,R.string.message_post_was_removed);
            }
        });
    }

    public void initLike(boolean isLiked){
        likesImageView.setImageResource(isLiked? R.drawable.ic_like_activated : R.drawable.ic_like);
        this.isLiked = isLiked;
    }

    private void doHandleLikeClickAction(BaseActivity baseActivity, Post post){
        ProfileStatus profileStatus = ProfileManager.getInstance(baseActivity).checkProfile();
        if(profileStatus.equals(ProfileStatus.PROFILE_CREATED)){
            if(isListView) likeClickActionLocal(post);
            else likeClickAction(post.getLikesCount());
        }else{
            baseActivity.doAuthorization(profileStatus);
        }
    }
}
