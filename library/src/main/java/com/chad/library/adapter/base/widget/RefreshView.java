package com.chad.library.adapter.base.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.chad.library.adapter.base.indicator.ApayIndicator;
import com.chad.library.adapter.base.indicator.BaseIndicator;
import com.chad.library.adapter.base.util.ScreenUtils;

/**
 * Created by boby on 2016/12/11.
 */

public class RefreshView extends View {

    private Context mContext;

    private Paint mPaint;

    private Path mPath;

    private BaseIndicator mBaseIndicator;

    private int mIndicatorId;

    private int mIndicatorColor;

    /**
     * 仿支付宝刷新
     */
    public static final int APAY_TYPE = 0x00000001;

    public RefreshView(Context context) {
        this(context, null);
    }

    public RefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#999999"));
        mPaint.setStyle(Paint.Style.STROKE);

        mPath = new Path();

        applyIndicator();

    }

    public void setIndicatorId(int indicatorId) {
        mIndicatorId = indicatorId;
        applyIndicator();
    }

    public void setIndicatorColor(int color) {
        mIndicatorColor = color;
        mPaint.setColor(mIndicatorColor);
        this.invalidate();
    }

    private void applyIndicator() {
        switch (mIndicatorId) {
            default:
            case APAY_TYPE:
                mBaseIndicator = new ApayIndicator();
                break;
        }
        mBaseIndicator.setTarget(this);
        mBaseIndicator.initAnimation();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBaseIndicator.initData(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mBaseIndicator.draw(canvas, mPath, mPaint);
    }

    /**
     * @param progress
     */
    public void setProgress(int progress) {
        mBaseIndicator.setProgress(progress);
    }

    /**
     * 是否绘制箭头
     *
     * @param isDraw
     */
    public void setIsDrawArrow(boolean isDraw) {
        mBaseIndicator.setDrawArrow(isDraw);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        //处理 wrap_content问题
        int defaultDimension = ScreenUtils.dip2px(mContext, 32);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultDimension, defaultDimension);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultDimension, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, defaultDimension);
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //Log.e("RefreshView", "onDetachedFromWindow--------00000");
        //mBaseIndicator.setAnimationStatus(BaseIndicator.CANCEL_STATUS);
    }

    /**
     * 开始动画
     */
    public void startAnimator() {
        mBaseIndicator.setAnimationStatus(BaseIndicator.START_STATUS);
    }

    public void endAnimator() {
        mBaseIndicator.setAnimationStatus(BaseIndicator.END_STATUS);
    }


}
