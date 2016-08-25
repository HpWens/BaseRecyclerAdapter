package com.github.baserecycleradapter.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 8/19 0019.
 */
public class BallSpinFadeLoader extends View {

    private Context mContext;
    private Paint mPaint;

    private float mCenterX;
    private float mCenterY;
    private float mRadius;

    private boolean mAnimatorEnable;

    float[] scaleFloats = new float[]{SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE};

    int[] alphas = new int[]{ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA};


    public static final float SCALE = 1.0f;
    public static final int ALPHA = 255;

    private ValueAnimator scaleAnim;
    private ValueAnimator alphaAnim;

    public BallSpinFadeLoader(Context context) {
        this(context, null);
    }

    public BallSpinFadeLoader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BallSpinFadeLoader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(dip2px(1));
        mPaint.setColor(Color.parseColor("#999999"));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        //处理 wrap_content问题
        int defaultDimension = dip2px(30);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultDimension, defaultDimension);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultDimension, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, defaultDimension);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = w / 10;
        mCenterX = w / 2;
        mCenterY = h / 2;
    }


    public int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < alphas.length; i++) {
            canvas.save();
            canvas.translate(mCenterX + (getWidth() / 2 - mRadius) * (float) Math.cos(Math.toRadians(i * 45)),
                    mCenterY + (getWidth() / 2 - mRadius) * (float) Math.sin(Math.toRadians(i * 45)));
            canvas.scale(scaleFloats[i], scaleFloats[i]);
            mPaint.setAlpha(alphas[i]);
            canvas.drawCircle(0, 0, mRadius, mPaint);
            canvas.restore();
        }
    }

    public void startAnimator() {
        post(new Runnable() {
            @Override
            public void run() {

                int[] delays = {0, 120, 240, 360, 480, 600, 720, 780, 840};
                for (int i = 0; i < 8; i++) {
                    final int index = i;
                    scaleAnim = ValueAnimator.ofFloat(1, 0.4f, 1);
                    scaleAnim.setDuration(1000);
                    scaleAnim.setRepeatMode(ValueAnimator.RESTART);
                    scaleAnim.setRepeatCount(ValueAnimator.INFINITE);
                    scaleAnim.setStartDelay(delays[i]);
                    scaleAnim.addUpdateListener(new MyUpdateListener(BallSpinFadeLoader.this) {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            scaleFloats[index] = (float) animation.getAnimatedValue();
                            postInvalidate();
                        }
                    });
                    scaleAnim.start();

                    alphaAnim = ValueAnimator.ofInt(255, 77, 255);
                    alphaAnim.setDuration(1000);
                    alphaAnim.setRepeatMode(ValueAnimator.RESTART);
                    alphaAnim.setRepeatCount(ValueAnimator.INFINITE);
                    alphaAnim.setStartDelay(delays[i]);
                    alphaAnim.addUpdateListener(new MyUpdateListener(BallSpinFadeLoader.this) {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            alphas[index] = (int) valueAnimator.getAnimatedValue();
                            postInvalidate();
                        }
                    });
                    alphaAnim.start();
                }

                mAnimatorEnable = true;
            }
        });
    }

    public boolean isLoading() {
        return mAnimatorEnable;
    }

    private static class MyUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        private WeakReference<BallSpinFadeLoader> mWeakReference;

        public MyUpdateListener(BallSpinFadeLoader ballSpinFadeLoader) {
            mWeakReference = new WeakReference<BallSpinFadeLoader>(ballSpinFadeLoader);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            BallSpinFadeLoader ball = mWeakReference.get();
            if (ball == null) {
                return;
            }
        }
    }

}
