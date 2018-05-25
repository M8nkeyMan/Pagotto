package com.pag.socialz.Managers;

import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AlphaAnimation;

public class AnimationManager {

    public final static int DEFAULT_DELAY = 0;
    public final static int SHORT_DURATION = 200;
    public final static int ALPHA_SHORT_DURATION = 400;

    public static ViewPropertyAnimator hideViewByScale (View v) {

        ViewPropertyAnimator propertyAnimator = v.animate().setStartDelay(DEFAULT_DELAY).setDuration(SHORT_DURATION)
                .scaleX(0).scaleY(0);

        return propertyAnimator;
    }

    public static ViewPropertyAnimator showViewByScale (View v) {

        ViewPropertyAnimator propertyAnimator = v.animate().setStartDelay(DEFAULT_DELAY)
                .scaleX(1).scaleY(1);

        return propertyAnimator;
    }

    public static ViewPropertyAnimator hideViewByScaleAndVisibility (final View v) {

        ViewPropertyAnimator propertyAnimator = v.animate().setStartDelay(DEFAULT_DELAY).setDuration(SHORT_DURATION)
                .scaleX(0).scaleY(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        v.setVisibility(View.GONE);
                    }
                });

        return propertyAnimator;
    }

    public static AlphaAnimation hideViewByAlpha(final View v) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(ALPHA_SHORT_DURATION);
        v.setAnimation(alphaAnimation);
        return alphaAnimation;
    }

    public static ViewPropertyAnimator showViewByScaleAndVisibility (View v) {
        v.setVisibility(View.VISIBLE);
        ViewPropertyAnimator propertyAnimator = v.animate().setStartDelay(DEFAULT_DELAY).scaleX(1).scaleY(1);
        return propertyAnimator;
    }
}