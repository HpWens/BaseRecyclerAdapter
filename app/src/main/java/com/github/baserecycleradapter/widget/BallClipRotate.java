package com.github.baserecycleradapter.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by Administrator on 8/19 0019.
 */
public class BallClipRotate extends View {

    private Context mContext;
    private Paint mPaint;
    private float mSpacing;

    private float mCenterX;
    private float mCenterY;

    private float mScale;
    private float mScaleFloat;
    private float mDegrees;
    private float[] mStartAngles;

    public BallClipRotate(Context context) {
        this(context, null);
    }

    public BallClipRotate(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BallClipRotate(Context context, AttributeSet attrs, int defStyleAttr) {
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

        mSpacing = dip2px(4);
        mScale = 0f;
        mScaleFloat = 0f;
        mDegrees = 0f;

        mStartAngles = new float[]{255, 45};
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

        canvas.save();
        canvas.translate(mCenterX, mCenterY);
        canvas.scale(mScale, mScale);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(0, 0, mCenterX / 2.5f, mPaint);
        canvas.restore();

        canvas.translate(mCenterX, mCenterY);
        canvas.scale(mScaleFloat, mScaleFloat);
        canvas.rotate(mDegrees);
        mPaint.setStyle(Paint.Style.STROKE);

        for (int i = 0; i < mStartAngles.length; i++) {
            RectF rectF = new RectF(-mCenterX + mSpacing, -mCenterY + mSpacing,
                    mCenterX - mSpacing, mCenterY - mSpacing);
            canvas.drawArc(rectF, mStartAngles[i], 90, false, mPaint);
        }

    }

    public void startAnimator() {
        post(new Runnable() {
            @Override
            public void run() {
                ValueAnimator animator = ValueAnimator.ofFloat(1.0f, 0.3f, 1.0f);
                animator.setDuration(1000);
                animator.setInterpolator(new LinearInterpolator());
                animator.setRepeatMode(ValueAnimator.RESTART);
                animator.setRepeatCount(ValueAnimator.INFINITE);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        mScale = (float) valueAnimator.getAnimatedValue();
                        postInvalidate();
                    }
                });
                animator.start();

                ValueAnimator rotateAnim = ValueAnimator.ofFloat(0, 180, 360);
                rotateAnim.setDuration(750);
                rotateAnim.setRepeatCount(-1);
                rotateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mDegrees = (float) animation.getAnimatedValue();
                        postInvalidate();
                    }
                });
                rotateAnim.start();

                ValueAnimator scaleAnimator = ValueAnimator.ofFloat(1.0f, 0.6f, 1.0f);
                scaleAnimator.setDuration(1000);
                scaleAnimator.setRepeatCount(-1);
                scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mScaleFloat = (float) animation.getAnimatedValue();
                        postInvalidate();
                    }
                });
                scaleAnimator.start();
            }
        });
    }

}
