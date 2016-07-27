package com.github.library.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by jms on 2016/7/19.
 */
public class ScaleInAnimation implements BaseAnimation {

    private static final float DEFAULT_SCALE_FROM = 0.5f;

    private final float mFrom;

    public ScaleInAnimation(float from) {
        mFrom = from;
    }

    public ScaleInAnimation() {
        this(DEFAULT_SCALE_FROM);
    }

    @Override
    public Animator[] getAnimators(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", mFrom, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", mFrom, 1f);
        return new Animator[]{scaleX, scaleY};
    }
}
