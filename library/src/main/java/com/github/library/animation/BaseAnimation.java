package com.github.library.animation;

import android.animation.Animator;
import android.view.View;

/**
 * Created by jms on 2016/7/19.
 */
public interface BaseAnimation {

    Animator[] getAnimators(View view);
}
