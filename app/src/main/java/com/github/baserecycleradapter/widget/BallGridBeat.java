package com.github.baserecycleradapter.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 8/19 0019.
 */
public class BallGridBeat extends View {

    private Context mContext;
    private Paint mPaint;
    private float mSpacing;

    private float mCenterX;
    private float mCenterY;

    private float mRadius;

    public static final int ALPHA = 255;

    int[] alphas = new int[]{ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA};

    public BallGridBeat(Context context) {
        this(context, null);
    }

    public BallGridBeat(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BallGridBeat(Context context, AttributeSet attrs, int defStyleAttr) {
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
        mRadius = (w - mSpacing * 4) / 6;
        mCenterX = w / 2 - (mRadius * 2 + mSpacing);
        mCenterY = h / 2 - (mRadius * 2 + mSpacing);
    }


    public int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                canvas.save();
                canvas.translate(mCenterX + (mRadius * 2) * j + mSpacing * j,
                        mCenterY + (mRadius * 2) * i + mSpacing * i);
                mPaint.setAlpha(alphas[3 * i + j]);
                canvas.drawCircle(0, 0, mRadius, mPaint);
                canvas.restore();
            }
        }
    }

    public void startAnimator() {
        post(new Runnable() {
            @Override
            public void run() {

                int[] durations = {960, 930, 1190, 1130, 1340, 940, 1200, 820, 1190};
                int[] delays = {360, 400, 680, 410, 710, -150, -120, 10, 320};

                for (int i = 0; i < 9; i++) {
                    final int index = i;
                    ValueAnimator alphaAnim = ValueAnimator.ofInt(255, 168, 255);
                    alphaAnim.setDuration(durations[i]);
                    alphaAnim.setRepeatMode(ValueAnimator.RESTART);
                    alphaAnim.setRepeatCount(ValueAnimator.INFINITE);
                    alphaAnim.setStartDelay(delays[i]);
                    alphaAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            alphas[index] = (int) animation.getAnimatedValue();
                            postInvalidate();
                        }
                    });
                    alphaAnim.start();
                }

            }
        });
    }

}
