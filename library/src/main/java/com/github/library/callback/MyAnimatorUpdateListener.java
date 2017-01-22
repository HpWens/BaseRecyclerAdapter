package com.github.library.callback;

import android.animation.ValueAnimator;

import java.lang.ref.WeakReference;

/**
 * Created by boby on 2016/12/13.
 */

public class MyAnimatorUpdateListener<T> implements ValueAnimator.AnimatorUpdateListener {

    private WeakReference<T> mWeakReference;

    public MyAnimatorUpdateListener(T t) {
        mWeakReference = new WeakReference<T>(t);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        T o = mWeakReference.get();
        if (o == null) {
            return;
        }
    }
}
