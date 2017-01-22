package com.github.library.indicator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boby on 2017/1/22.
 */

public class BallClipRotateIndicator extends BaseIndicator {

    float scaleFloat = 1, degrees;

    float mCircleSpacing;

    float mCenterX, mCenterY;

    @Override
    public void draw(Canvas canvas, Path path, Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);

        canvas.translate(mCenterX, mCenterY);
        canvas.scale(scaleFloat, scaleFloat);
        canvas.rotate(degrees);
        RectF rectF = new RectF(-mCenterX + mCircleSpacing, -mCenterY + mCircleSpacing,
                0 + mCenterX - mCircleSpacing, 0 + mCenterY - mCircleSpacing);
        canvas.drawArc(rectF, -45, 270, false, paint);
    }

    @Override
    public List<Animator> createAnimation() {
        List<Animator> animators = new ArrayList<>();
        ValueAnimator scaleAnim = ValueAnimator.ofFloat(1, 0.6f, 0.5f, 1);
        scaleAnim.setDuration(750);
        scaleAnim.setRepeatCount(-1);
        scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scaleFloat = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        scaleAnim.start();

        ValueAnimator rotateAnim = ValueAnimator.ofFloat(0, 180, 360);
        rotateAnim.setDuration(750);
        rotateAnim.setRepeatCount(-1);
        rotateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                degrees = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        rotateAnim.start();
        animators.add(scaleAnim);
        animators.add(rotateAnim);
        return animators;
    }

    @Override
    public void initData(int w, int h) {
        mCircleSpacing = 12;
        mCenterX = w / 2;
        mCenterY = h / 2;
    }
}
