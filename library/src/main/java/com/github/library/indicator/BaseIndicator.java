package com.github.library.indicator;

import android.animation.Animator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

import java.util.List;

/**
 * Created by boby on 2016/12/11.
 */

public abstract class BaseIndicator {

    private View mTarget;

    private int mProgress = 0;

    private List<Animator> mAnimators;

    private boolean mDrawArrow = true;
    /**
     * Use with {@link #setAnimationStatus}
     */
    public static final int START_STATUS = 0x00000001;
    /**
     * Use with {@link #setAnimationStatus}
     */
    public static final int END_STATUS = 0x00000002;
    /**
     * Use with {@link #setAnimationStatus}
     */
    public static final int CANCEL_STATUS = 0x00000003;


    public View getTarget() {
        return mTarget;
    }

    public void setTarget(View target) {
        mTarget = target;
    }

    public void postInvalidate() {
        mTarget.postInvalidate();
    }

    public int getWidth() {
        return mTarget.getWidth();
    }

    public int getHeight() {
        return mTarget.getHeight();
    }

    /**
     * @param progress 0~100
     */
    public void setProgress(int progress) {
        mProgress = progress;
        postInvalidate();
    }

    public int getProgress() {
        return mProgress;
    }

    /**
     * draw indicator
     *
     * @param canvas
     * @param paint
     * @param path
     */
    public abstract void draw(Canvas canvas, Path path, Paint paint);

    /**
     * create animation or animations
     */
    public abstract List<Animator> createAnimation();

    /**
     * obtain data
     *
     * @param w
     * @param h
     */
    public abstract void initData(int w, int h);

    public void initAnimation() {
        mAnimators = mAnimators == null ? createAnimation() : mAnimators;
    }

    public boolean isDrawArrow() {
        return mDrawArrow;
    }

    public void setDrawArrow(boolean drawArrow) {
        mDrawArrow = drawArrow;
    }

    /**
     * make animation to start or end when target
     * view was be Visible or Gone or Invisible.
     * make animation to cancel when target view
     * be onDetachedFromWindow.
     *
     * @param indicatorType
     */
    public void setAnimationStatus(@IndicatorType int indicatorType) {
        if (mAnimators == null) {
            return;
        }
        int count = mAnimators.size();
        for (int i = 0; i < count; i++) {
            Animator animator = mAnimators.get(i);
            boolean isRunning = animator.isRunning();
            switch (indicatorType) {
                case START_STATUS:
                    if (!isRunning) {
                        animator.start();
                    }
                    break;
                case END_STATUS:
                    if (isRunning) {
                        animator.end();
                    }
                    break;
                case CANCEL_STATUS:
                    if (isRunning) {
                        animator.cancel();
                    }
                    break;
            }
        }
    }


}
