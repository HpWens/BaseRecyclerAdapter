package com.github.library.indicator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.radius;
import static android.R.attr.x;
import static android.R.attr.y;

/**
 * Created by boby on 2017/1/22.
 */

public class BallBeatIndicator extends BaseIndicator {

    public static final float SCALE = 1.0f;

    public static final int ALPHA = 255;

    private float[] scaleFloats = new float[]{SCALE,
            SCALE,
            SCALE};

    int[] alphas = new int[]{ALPHA,
            ALPHA,
            ALPHA,};

    float mCircleSpacing;

    float mRadius;

    float mCenterX, mCenterY;

    @Override
    public void draw(Canvas canvas, Path path, Paint paint) {

        for (int i = 0; i < 3; i++) {
            canvas.save();
            float translateX = mCenterX + (mRadius * 2) * i + mCircleSpacing * i;
            canvas.translate(translateX, mCenterY);
            canvas.scale(scaleFloats[i], scaleFloats[i]);
            paint.setAlpha(alphas[i]);
            canvas.drawCircle(0, 0, mRadius, paint);
            canvas.restore();
        }
    }

    @Override
    public List<Animator> createAnimation() {
        List<Animator> animators = new ArrayList<>();
        int[] delays = new int[]{350, 0, 350};
        for (int i = 0; i < 3; i++) {
            final int index = i;
            ValueAnimator scaleAnim = ValueAnimator.ofFloat(1, 0.75f, 1);
            scaleAnim.setDuration(700);
            scaleAnim.setRepeatCount(-1);
            scaleAnim.setStartDelay(delays[i]);
            scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    scaleFloats[index] = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            scaleAnim.start();

            ValueAnimator alphaAnim = ValueAnimator.ofInt(255, 51, 255);
            alphaAnim.setDuration(700);
            alphaAnim.setRepeatCount(-1);
            alphaAnim.setStartDelay(delays[i]);
            alphaAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    alphas[index] = (int) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            alphaAnim.start();
            animators.add(scaleAnim);
            animators.add(alphaAnim);
        }
        return animators;
    }

    @Override
    public void initData(int w, int h) {
        mCircleSpacing = 4;
        mRadius = (w - mCircleSpacing * 2) / 6;
        mCenterX = w / 2 - (mRadius * 2 + mCircleSpacing);
        mCenterY = h / 2;
    }
}
