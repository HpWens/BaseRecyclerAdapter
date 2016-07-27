package com.github.library.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;


/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class SlideInLeftAnimation implements BaseAnimation {

    @Override
    public Animator[] getAnimators(View view) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", -view.getRootView().getWidth(), 0);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1f);
        return new Animator[]{
                translationX
        };
    }
}
