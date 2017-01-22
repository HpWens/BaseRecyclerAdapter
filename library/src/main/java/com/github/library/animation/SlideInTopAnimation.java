package com.github.library.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by jms on 2016/7/19.
 */
public class SlideInTopAnimation implements BaseAnimation {


    @Override
    public Animator[] getAnimators(View view) {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", -view.getRootView().getHeight(), 0);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1f);
        return new Animator[]{
                translationY, alpha
        };
    }
}
