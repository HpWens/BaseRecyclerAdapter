package com.github.library.indicator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.animation.LinearInterpolator;

import com.github.library.callback.MyAnimatorUpdateListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boby on 2016/12/11.
 */

public class ApayIndicator extends BaseIndicator {

    private int mRadius;

    private int mStrokeWidth;

    private float mStartAngle = -90;

    @Override
    public void draw(Canvas canvas, Path path, Paint paint) {
        paint.setStrokeWidth(mStrokeWidth);

        if (isDrawArrow()) {
            path.reset();

            path.moveTo(getWidth() / 2, getHeight() / 2 - mRadius / 3);
            path.lineTo(getWidth() / 2, getHeight() / 2 + mRadius / 3);

            path.moveTo(getWidth() / 2 - mRadius / 3, getHeight() / 2);
            path.lineTo(getWidth() / 2, getHeight() / 2 + mRadius / 3);
            path.lineTo(getWidth() / 2 + mRadius / 3, getHeight() / 2);

            canvas.drawPath(path, paint);
        }

        canvas.drawArc(new RectF(getWidth() / 2 - mRadius + mStrokeWidth, getHeight() / 2 - mRadius + mStrokeWidth, getWidth() / 2 + mRadius - mStrokeWidth, getHeight() / 2 + mRadius - mStrokeWidth),
                mStartAngle, -getProgress() * ((float) 330 / 100), false, paint);

    }


    @Override
    public List<Animator> createAnimation() {
        List<Animator> animators = new ArrayList<>();
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 360f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(2000);
        animator.addUpdateListener(new MyAnimatorUpdateListener<ApayIndicator>(ApayIndicator.this) {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mStartAngle = -90 + (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animators.add(animator);
        return animators;
    }

    @Override
    public void initData(int w, int h) {
        mRadius = Math.min(w, h) / 2;
        mStrokeWidth = mRadius / 16;
    }

}
