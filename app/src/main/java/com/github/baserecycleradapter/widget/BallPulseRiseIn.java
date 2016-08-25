package com.github.baserecycleradapter.widget;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by Administrator on 8/19 0019.
 */
public class BallPulseRiseIn extends View {

    private Context mContext;
    private Paint mPaint;

    private float mWidth;
    private float mHeight;
    private float mRadius;

    public BallPulseRiseIn(Context context) {
        this(context, null);
    }

    public BallPulseRiseIn(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BallPulseRiseIn(Context context, AttributeSet attrs, int defStyleAttr) {
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
        mWidth = w;
        mHeight = h;
    }


    public int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(mWidth / 4, mRadius * 2, mRadius, mPaint);
        canvas.drawCircle(mWidth * 3 / 4, mRadius * 2, mRadius, mPaint);

        canvas.drawCircle(mRadius, mHeight - 2 * mRadius, mRadius, mPaint);
        canvas.drawCircle(mWidth / 2, mHeight - 2 * mRadius, mRadius, mPaint);
        canvas.drawCircle(mWidth - mRadius, mHeight - 2 * mRadius, mRadius, mPaint);
    }

    public void startAnimator() {
        post(new Runnable() {
            @Override
            public void run() {

                PropertyValuesHolder rotation = PropertyValuesHolder.ofFloat("rotationX", 0, 360);
                ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(BallPulseRiseIn.this, rotation);
                animator.setInterpolator(new LinearInterpolator());
                animator.setRepeatMode(ValueAnimator.RESTART);
                animator.setRepeatCount(ValueAnimator.INFINITE);
                animator.setDuration(1500);
                animator.start();

            }
        });
    }

}
