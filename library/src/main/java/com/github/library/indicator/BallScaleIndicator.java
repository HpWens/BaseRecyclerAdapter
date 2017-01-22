package com.github.library.indicator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boby on 2017/1/22.
 */

public class BallScaleIndicator extends BaseIndicator {

    float scale = 1;
    int alpha = 255;

    float mCircleSpacing;

    float mCenterX, mCenterY;

    @Override
    public void draw(Canvas canvas, Path path, Paint paint) {
        paint.setAlpha(alpha);
        canvas.scale(scale, scale, mCenterX, mCenterY);
        paint.setAlpha(alpha);
        canvas.drawCircle(mCenterX, mCenterY, mCenterX - mCircleSpacing, paint);
    }

    @Override
    public List<Animator> createAnimation() {
        List<Animator> animators = new ArrayList<>();
        ValueAnimator scaleAnim = ValueAnimator.ofFloat(0, 1);
        scaleAnim.setInterpolator(new LinearInterpolator());
        scaleAnim.setDuration(1000);
        scaleAnim.setRepeatCount(-1);
        scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scale = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        scaleAnim.start();

        ValueAnimator alphaAnim = ValueAnimator.ofInt(255, 0);
        alphaAnim.setInterpolator(new LinearInterpolator());
        alphaAnim.setDuration(1000);
        alphaAnim.setRepeatCount(-1);
        alphaAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                alpha = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        alphaAnim.start();
        animators.add(scaleAnim);
        animators.add(alphaAnim);
        return animators;
    }

    @Override
    public void initData(int w, int h) {
        mCircleSpacing = 4;
        mCenterX = w / 2;
        mCenterY = h / 2;
    }
}
