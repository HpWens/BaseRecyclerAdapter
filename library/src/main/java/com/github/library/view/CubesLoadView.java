package com.github.library.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by Administrator on 7/6 0006.
 */
public class CubesLoadView extends View {

    private Context mContext;

    private Paint mPaint;

    private Paint mPaintRight;

    private Paint mPaintLeft;

    private Paint mPaintShadow;

    private Path p;

    private float centerX;

    private float mWidth;

    private float mItemWidth;

    private float mItemHeight;

    private boolean mShadow;

    private float mValueAnimator;

    private ValueAnimator valueAnimator;

    public CubesLoadView(Context context) {
        this(context, null);
    }

    public CubesLoadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CubesLoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mShadow = true;
        p = new Path();
        //大家注意哦，不要在onDraw里new Paint对象，
        // 上节中我为了省事就直接在onDraw（）函数中直接new 了Paint对象，
        // 由于onDraw函数在刷新时会连续调用多次，所以如果在其中不断的new对象，会造成程序不断的GC(内存回收)，
        // 是会严重影响性能的！在程序中，我有时会了为了方便理解，
        // 就直接在onDraw（）中创建对象了，大家在实际应用中一定要杜绝这种应用哦。

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(Color.rgb(247, 202, 42));
        mPaint.setStrokeWidth(1);

        mPaintRight = new Paint();
        mPaintRight.setAntiAlias(true);
        mPaintRight.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintRight.setColor(Color.rgb(188, 91, 26));
        mPaintRight.setStrokeWidth(1);

        mPaintLeft = new Paint();
        mPaintLeft.setAntiAlias(true);
        mPaintLeft.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintLeft.setColor(Color.rgb(227, 144, 11));
        mPaintLeft.setStrokeWidth(1);


        mPaintShadow = new Paint();
        mPaintShadow.setAntiAlias(true);
        mPaintShadow.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintShadow.setColor(Color.rgb(0, 0, 0));
        mPaintShadow.setStrokeWidth(1f);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        centerX = w / 2;

        mWidth = Math.min(w, h);

        //平行四边形的最小角度为30度
        mItemWidth = mWidth / 16 * (float) Math.sqrt(3);

        mItemHeight = mWidth / 16;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if (mValueAnimator >= 0 && mValueAnimator < (1.0f / 3)) {
            //绘制第一阶段
            drawStage1(canvas, mValueAnimator);
            if (mShadow) {
                drawShadow1(canvas, mValueAnimator);
            }
        } else if (mValueAnimator >= (1.0f / 3) && mValueAnimator < (1.0f / 3 * 2)) {
            //绘制第二阶段
            drawStage2(canvas, mValueAnimator);
            if (mShadow) {
                drawShadow2(canvas, mValueAnimator);
            }
        } else if (mValueAnimator >= (1.0f / 3 * 2) && mValueAnimator <= 1.0f) {
            //绘制第三阶段
            drawStage3(canvas, mValueAnimator);
            if (mShadow) {
                drawShadow3(canvas, mValueAnimator);
            }
        }


    }


    //绘制第三阶段
    private void drawStage3(Canvas canvas, float valueAnimator) {

        //这里2个立方体都在移动，每个立方体在X轴方向移动的距离为mItemWidth/2
        float moveX = mItemWidth / 2 * (valueAnimator - (1.0f / 3) * 2) / (1.0f / 3);
        float moveY = mItemHeight / 2 * (valueAnimator - (1.0f / 3) * 2) / (1.0f / 3);

        //第二阶段移动到第三阶段  X增大了mItemWidth   Y减少了mItemHeight
        // X增大 Y增大
        p.reset();
        p.moveTo(centerX - 2 * mItemWidth - mItemWidth / 2 + mItemWidth + moveX, 4 * mItemHeight - mItemHeight / 2 - mItemHeight + moveY);
        p.lineTo(centerX - mItemWidth - mItemWidth / 2 + mItemWidth + moveX, 3 * mItemHeight - mItemHeight / 2 - mItemHeight + moveY);
        p.lineTo(centerX - mItemWidth / 2 + mItemWidth + moveX, 4 * mItemHeight - mItemHeight / 2 - mItemHeight + moveY);
        p.lineTo(centerX - mItemWidth / 2 - mItemWidth + mItemWidth + moveX, 5 * mItemHeight - mItemHeight / 2 - mItemHeight + moveY);
        p.close();
        canvas.drawPath(p, mPaint);

        //第二阶段移动到第三阶段  X增大了mItemWidth   Y减少了mItemHeight
        // X增大 Y增大
        p.reset();
        p.moveTo(centerX - 2 * mItemWidth - mItemWidth / 2 + mItemWidth + moveX, 4 * mItemHeight - mItemHeight / 2 - mItemHeight + moveY);
        p.lineTo(centerX - mItemWidth - mItemWidth / 2 + mItemWidth + moveX, 5 * mItemHeight - mItemHeight / 2 - mItemHeight + moveY);
        p.lineTo(centerX - mItemWidth - mItemWidth / 2 + mItemWidth + moveX, 7 * mItemHeight - mItemHeight / 2 - mItemHeight + moveY);
        p.lineTo(centerX - 2 * mItemWidth - mItemWidth / 2 + mItemWidth + moveX, 6 * mItemHeight - mItemHeight / 2 - mItemHeight + moveY);
        p.close();
        canvas.drawPath(p, mPaintLeft);

        //第二阶段移动到第三阶段  X,Y不变
        // X增大 Y增大
        p.reset();
        p.moveTo(centerX + mItemWidth / 2 + moveX, 4 * mItemHeight + mItemHeight / 2 + moveY);
        p.lineTo(centerX - mItemWidth + mItemWidth / 2 + moveX, 3 * mItemHeight + mItemHeight / 2 + moveY);
        p.lineTo(centerX + mItemWidth / 2 + moveX, 2 * mItemHeight + mItemHeight / 2 + moveY);
        p.lineTo(centerX + mItemWidth + mItemWidth / 2 + moveX, 3 * mItemHeight + mItemHeight / 2 + moveY);
        p.close();
        canvas.drawPath(p, mPaint);


        //第二阶段移动到第三阶段  X,Y不变
        // X增大 Y增大
        p.reset();
        p.moveTo(centerX + mItemWidth / 2 + moveX, 4 * mItemHeight + mItemHeight / 2 + moveY);
        p.lineTo(centerX + mItemWidth + mItemWidth / 2 + moveX, 3 * mItemHeight + mItemHeight / 2 + moveY);
        p.lineTo(centerX + 1 * mItemWidth + mItemWidth / 2 + moveX, 5 * mItemHeight + mItemHeight / 2 + moveY);
        p.lineTo(centerX + mItemWidth / 2 + moveX, 6 * mItemHeight + mItemHeight / 2 + moveY);
        p.close();
        canvas.drawPath(p, mPaintRight);


        //第二阶段移动到第三阶段  X,Y不变
        // X减小 Y减小
        p.reset();
        p.moveTo(centerX - mItemWidth - mItemWidth / 2 - moveX, 5 * mItemHeight - mItemHeight / 2 - moveY);
        p.lineTo(centerX - mItemWidth / 2 - moveX, 4 * mItemHeight - mItemHeight / 2 - moveY);
        p.lineTo(centerX + mItemWidth - mItemWidth / 2 - moveX, 5 * mItemHeight - mItemHeight / 2 - moveY);
        p.lineTo(centerX - mItemWidth / 2 - moveX, 6 * mItemHeight - mItemHeight / 2 - moveY);
        p.close();
        canvas.drawPath(p, mPaint);


        //第二阶段移动到第三阶段  X,Y不变
        // X减小 Y减小
        p.reset();
        p.moveTo(centerX - mItemWidth - mItemWidth / 2 - moveX, 5 * mItemHeight - mItemHeight / 2 - moveY);
        p.lineTo(centerX - mItemWidth / 2 - moveX, 6 * mItemHeight - mItemHeight / 2 - moveY);
        p.lineTo(centerX - mItemWidth / 2 - moveX, 8 * mItemHeight - mItemHeight / 2 - moveY);
        p.lineTo(centerX - mItemWidth - mItemWidth / 2 - moveX, 7 * mItemHeight - mItemHeight / 2 - moveY);
        p.close();
        canvas.drawPath(p, mPaintLeft);


        //第二阶段移动到第三阶段  X减少mItemWidth,Y增加mItemHeight
        // X减小 Y减小
        p.reset();
        p.moveTo(centerX + mItemWidth / 2 - mItemWidth - moveX, 4 * mItemHeight + mItemHeight / 2 + mItemHeight - moveY);
        p.lineTo(centerX + mItemWidth + mItemWidth / 2 - mItemWidth - moveX, 3 * mItemHeight + mItemHeight / 2 + mItemHeight - moveY);
        p.lineTo(centerX + 2 * mItemWidth + mItemWidth / 2 - mItemWidth - moveX, 4 * mItemHeight + mItemHeight / 2 + mItemHeight - moveY);
        p.lineTo(centerX + mItemWidth + mItemWidth / 2 - mItemWidth - moveX, 5 * mItemHeight + mItemHeight / 2 + mItemHeight - moveY);
        p.close();
        canvas.drawPath(p, mPaint);

        //第二阶段移动到第三阶段  X减少mItemWidth,Y增加mItemHeight
        // X减小 Y减小
        p.reset();
        p.moveTo(centerX + mItemWidth / 2 - mItemWidth - moveX, 4 * mItemHeight + mItemHeight / 2 + mItemHeight - moveY);
        p.lineTo(centerX + mItemWidth + mItemWidth / 2 - mItemWidth - moveX, 5 * mItemHeight + mItemHeight / 2 + mItemHeight - moveY);
        p.lineTo(centerX + mItemWidth + mItemWidth / 2 - mItemWidth - moveX, 7 * mItemHeight + mItemHeight / 2 + mItemHeight - moveY);
        p.lineTo(centerX + mItemWidth / 2 - mItemWidth - moveX, 6 * mItemHeight + mItemHeight / 2 + mItemHeight - moveY);
        p.close();
        canvas.drawPath(p, mPaintLeft);


        //第二阶段移动到第三阶段  X减少mItemWidth,Y增加mItemHeight
        // X减小 Y减小
        p.reset();
        p.moveTo(centerX + mItemWidth + mItemWidth / 2 - mItemWidth - moveX, 5 * mItemHeight + mItemHeight / 2 + mItemHeight - moveY);
        p.lineTo(centerX + 2 * mItemWidth + mItemWidth / 2 - mItemWidth - moveX, 4 * mItemHeight + mItemHeight / 2 + mItemHeight - moveY);
        p.lineTo(centerX + 2 * mItemWidth + mItemWidth / 2 - mItemWidth - moveX, 6 * mItemHeight + mItemHeight / 2 + mItemHeight - moveY);
        p.lineTo(centerX + mItemWidth + mItemWidth / 2 - mItemWidth - moveX, 7 * mItemHeight + mItemHeight / 2 + mItemHeight - moveY);
        p.close();
        canvas.drawPath(p, mPaintRight);


    }

    //绘制第二阶段
    private void drawStage2(Canvas canvas, float valueAnimator) {

        //这里2个立方体都在移动，每个立方体在X轴方向移动的距离为mItemWidth
        float moveX = mItemWidth * (valueAnimator - 1.0f / 3) / (1.0f / 3);
        float moveY = mItemHeight * (valueAnimator - 1.0f / 3) / (1.0f / 3);

        //第一阶段移动到第二阶段  X,Y各自减少了mItemWidth/2,mItemHeight/2
        //X 增大   Y减少
        p.reset();
        p.moveTo(centerX - 2 * mItemWidth - mItemWidth / 2 + moveX, 4 * mItemHeight - mItemHeight / 2 - moveY);
        p.lineTo(centerX - mItemWidth - mItemWidth / 2 + moveX, 3 * mItemHeight - mItemHeight / 2 - moveY);
        p.lineTo(centerX - mItemWidth / 2 + moveX, 4 * mItemHeight - mItemHeight / 2 - moveY);
        p.lineTo(centerX - mItemWidth / 2 - mItemWidth + moveX, 5 * mItemHeight - mItemHeight / 2 - moveY);
        p.close();
        canvas.drawPath(p, mPaint);

        //第一阶段移动到第二阶段  X,Y各自减少了mItemWidth/2,mItemHeight/2
        //X 增大   Y减少
        p.reset();
        p.moveTo(centerX - 2 * mItemWidth - mItemWidth / 2 + moveX, 4 * mItemHeight - mItemHeight / 2 - moveY);
        p.lineTo(centerX - mItemWidth - mItemWidth / 2 + moveX, 5 * mItemHeight - mItemHeight / 2 - moveY);
        p.lineTo(centerX - mItemWidth - mItemWidth / 2 + moveX, 7 * mItemHeight - mItemHeight / 2 - moveY);
        p.lineTo(centerX - 2 * mItemWidth - mItemWidth / 2 + moveX, 6 * mItemHeight - mItemHeight / 2 - moveY);
        p.close();
        canvas.drawPath(p, mPaintLeft);

        //第一阶段移动到第二阶段  X,Y各自增加了mItemWidth/2,mItemHeight/2
        //X,y 不变化
        p.reset();
        p.moveTo(centerX + mItemWidth / 2, 4 * mItemHeight + mItemHeight / 2);
        p.lineTo(centerX - mItemWidth + mItemWidth / 2, 3 * mItemHeight + mItemHeight / 2);
        p.lineTo(centerX + mItemWidth / 2, 2 * mItemHeight + mItemHeight / 2);
        p.lineTo(centerX + mItemWidth + mItemWidth / 2, 3 * mItemHeight + mItemHeight / 2);
        p.close();
        canvas.drawPath(p, mPaint);


        //***这块是多出来的 第一阶段移动到第二阶段在X方向增加   Y方向增加
        p.reset();
        p.moveTo(centerX + mItemWidth / 2, 4 * mItemHeight + mItemHeight / 2);
        p.lineTo(centerX + mItemWidth + mItemWidth / 2, 3 * mItemHeight + mItemHeight / 2);
        p.lineTo(centerX + 1 * mItemWidth + mItemWidth / 2, 5 * mItemHeight + mItemHeight / 2);
        p.lineTo(centerX + mItemWidth / 2, 6 * mItemHeight + mItemHeight / 2);
        p.close();
        canvas.drawPath(p, mPaintRight);


        //第一阶段移动到第二阶段  X,Y各自减少了mItemWidth/2,mItemHeight/2
        //X,Y不变化
        p.reset();
        p.moveTo(centerX - mItemWidth - mItemWidth / 2, 5 * mItemHeight - mItemHeight / 2);
        p.lineTo(centerX - mItemWidth / 2, 4 * mItemHeight - mItemHeight / 2);
        p.lineTo(centerX + mItemWidth - mItemWidth / 2, 5 * mItemHeight - mItemHeight / 2);
        p.lineTo(centerX - mItemWidth / 2, 6 * mItemHeight - mItemHeight / 2);
        p.close();
        canvas.drawPath(p, mPaint);

        //第一阶段移动到第二阶段  X,Y各自减少了mItemWidth/2,mItemHeight/2
        //X,Y不变化
        p.reset();
        p.moveTo(centerX - mItemWidth - mItemWidth / 2, 5 * mItemHeight - mItemHeight / 2);
        p.lineTo(centerX - mItemWidth / 2, 6 * mItemHeight - mItemHeight / 2);
        p.lineTo(centerX - mItemWidth / 2, 8 * mItemHeight - mItemHeight / 2);
        p.lineTo(centerX - mItemWidth - mItemWidth / 2, 7 * mItemHeight - mItemHeight / 2);
        p.close();
        canvas.drawPath(p, mPaintLeft);

        //第一阶段移动到第二阶段  X,Y各自减少了mItemWidth/2,mItemHeight/2
        //X,Y不变化
        p.reset();
        p.moveTo(centerX - mItemWidth / 2, 6 * mItemHeight - mItemHeight / 2);
        p.lineTo(centerX + mItemWidth - mItemWidth / 2, 5 * mItemHeight - mItemHeight / 2);
        p.lineTo(centerX + mItemWidth - mItemWidth / 2, 7 * mItemHeight - mItemHeight / 2);
        p.lineTo(centerX - mItemWidth / 2, 8 * mItemHeight - mItemHeight / 2);
        p.close();
        canvas.drawPath(p, mPaintRight);


        //第一阶段移动到第二阶段  X,Y各自增加了mItemWidth/2,mItemHeight/2
        // X 减少  Y增大
        p.reset();
        p.moveTo(centerX + mItemWidth / 2 - moveX, 4 * mItemHeight + mItemHeight / 2 + moveY);
        p.lineTo(centerX + mItemWidth + mItemWidth / 2 - moveX, 3 * mItemHeight + mItemHeight / 2 + moveY);
        p.lineTo(centerX + 2 * mItemWidth + mItemWidth / 2 - moveX, 4 * mItemHeight + mItemHeight / 2 + moveY);
        p.lineTo(centerX + mItemWidth + mItemWidth / 2 - moveX, 5 * mItemHeight + mItemHeight / 2 + moveY);
        p.close();
        canvas.drawPath(p, mPaint);

        //第一阶段移动到第二阶段  X,Y各自增加了mItemWidth/2,mItemHeight/2
        // X 减少  Y增大
        p.reset();
        p.moveTo(centerX + mItemWidth / 2 - moveX, 4 * mItemHeight + mItemHeight / 2 + moveY);
        p.lineTo(centerX + mItemWidth + mItemWidth / 2 - moveX, 5 * mItemHeight + mItemHeight / 2 + moveY);
        p.lineTo(centerX + mItemWidth + mItemWidth / 2 - moveX, 7 * mItemHeight + mItemHeight / 2 + moveY);
        p.lineTo(centerX + mItemWidth / 2 - moveX, 6 * mItemHeight + mItemHeight / 2 + moveY);
        p.close();
        canvas.drawPath(p, mPaintLeft);


        //第一阶段移动到第二阶段  X,Y各自增加了mItemWidth/2,mItemHeight/2
        // X 减少  Y增大
        p.reset();
        p.moveTo(centerX + mItemWidth + mItemWidth / 2 - moveX, 5 * mItemHeight + mItemHeight / 2 + moveY);
        p.lineTo(centerX + 2 * mItemWidth + mItemWidth / 2 - moveX, 4 * mItemHeight + mItemHeight / 2 + moveY);
        p.lineTo(centerX + 2 * mItemWidth + mItemWidth / 2 - moveX, 6 * mItemHeight + mItemHeight / 2 + moveY);
        p.lineTo(centerX + mItemWidth + mItemWidth / 2 - moveX, 7 * mItemHeight + mItemHeight / 2 + moveY);
        p.close();
        canvas.drawPath(p, mPaintRight);

    }

    //绘制第一阶段
    private void drawStage1(Canvas canvas, float valueAnimator) {

        //这里2个立方体都在移动，每个立方体在X轴方向移动的距离为mItemWidth/2
        float moveX = mItemWidth / 2.0f * valueAnimator / (1.0f / 3);
        float moveY = mItemHeight / 2.0f * valueAnimator / (1.0f / 3);

        //顺时针绘制平行四边形
        //向后移动在X 方向 减少   Y方向减少
        p.reset();
        p.moveTo(centerX - 2 * mItemWidth - moveX, 4 * mItemHeight - moveY);
        p.lineTo(centerX - mItemWidth - moveX, 3 * mItemHeight - moveY);
        p.lineTo(centerX - moveX, 4 * mItemHeight - moveY);
        p.lineTo(centerX - mItemWidth - moveX, 5 * mItemHeight - moveY);
        p.close();
        canvas.drawPath(p, mPaint);

        //向后移动在X 方向 减少   Y方向减少
        p.reset();
        p.moveTo(centerX - 2 * mItemWidth - moveX, 4 * mItemHeight - moveY);
        p.lineTo(centerX - mItemWidth - moveX, 5 * mItemHeight - moveY);
        p.lineTo(centerX - mItemWidth - moveX, 7 * mItemHeight - moveY);
        p.lineTo(centerX - 2 * mItemWidth - moveX, 6 * mItemHeight - moveY);
        p.close();
        canvas.drawPath(p, mPaintLeft);

        //向前移动在X 方向 X方向增加   Y方向增加
        p.reset();
        p.moveTo(centerX + moveX, 4 * mItemHeight + moveY);
        p.lineTo(centerX - mItemWidth + moveX, 3 * mItemHeight + moveY);
        p.lineTo(centerX + moveX, 2 * mItemHeight + moveY);
        p.lineTo(centerX + mItemWidth + moveX, 3 * mItemHeight + moveY);
        p.close();
        canvas.drawPath(p, mPaint);

        //向前移动在X 方向 X方向增加   Y方向增加
        p.reset();
        p.moveTo(centerX + moveX, 4 * mItemHeight + moveY);
        p.lineTo(centerX + mItemWidth + moveX, 3 * mItemHeight + moveY);
        p.lineTo(centerX + 2 * mItemWidth + moveX, 4 * mItemHeight + moveY);
        p.lineTo(centerX + mItemWidth + moveX, 5 * mItemHeight + moveY);
        p.close();
        canvas.drawPath(p, mPaint);

        //向前移动在X 方向 X方向增加   Y方向增加
        p.reset();
        p.moveTo(centerX + moveX, 4 * mItemHeight + moveY);
        p.lineTo(centerX + mItemWidth + moveX, 5 * mItemHeight + moveY);
        p.lineTo(centerX + mItemWidth + moveX, 7 * mItemHeight + moveY);
        p.lineTo(centerX + moveX, 6 * mItemHeight + moveY);
        p.close();
        canvas.drawPath(p, mPaintLeft);


        //向前移动在X 方向 X方向增加   Y方向增加
        p.reset();
        p.moveTo(centerX + mItemWidth + moveX, 5 * mItemHeight + moveY);
        p.lineTo(centerX + 2 * mItemWidth + moveX, 4 * mItemHeight + moveY);
        p.lineTo(centerX + 2 * mItemWidth + moveX, 6 * mItemHeight + moveY);
        p.lineTo(centerX + mItemWidth + moveX, 7 * mItemHeight + moveY);
        p.close();
        canvas.drawPath(p, mPaintRight);

        //向后移动在X 方向 减少   Y方向减少
        p.reset();
        p.moveTo(centerX - mItemWidth - moveX, 5 * mItemHeight - moveY);
        p.lineTo(centerX - moveX, 4 * mItemHeight - moveY);
        p.lineTo(centerX + mItemWidth - moveX, 5 * mItemHeight - moveY);
        p.lineTo(centerX - moveX, 6 * mItemHeight - moveY);
        p.close();
        canvas.drawPath(p, mPaint);

        //向后移动在X 方向 减少   Y方向减少
        p.reset();
        p.moveTo(centerX - mItemWidth - moveX, 5 * mItemHeight - moveY);
        p.lineTo(centerX - moveX, 6 * mItemHeight - moveY);
        p.lineTo(centerX - moveX, 8 * mItemHeight - moveY);
        p.lineTo(centerX - mItemWidth - moveX, 7 * mItemHeight - moveY);
        p.close();
        canvas.drawPath(p, mPaintLeft);

        //向后移动在X 方向 减少   Y方向减少
        p.reset();
        p.moveTo(centerX - moveX, 6 * mItemHeight - moveY);
        p.lineTo(centerX + mItemWidth - moveX, 5 * mItemHeight - moveY);
        p.lineTo(centerX + mItemWidth - moveX, 7 * mItemHeight - moveY);
        p.lineTo(centerX - moveX, 8 * mItemHeight - moveY);
        p.close();
        canvas.drawPath(p, mPaintRight);
    }

    //绘制第一阶段的阴影
    private void drawShadow1(Canvas canvas, float valueAnimator) {

        float moveX = mItemWidth / 2.0f * valueAnimator / (1.0f / 3);
        float moveY = mItemHeight / 2.0f * valueAnimator / (1.0f / 3);

        //向左上运动  X,Y减少
        p.reset();
        p.moveTo(centerX - 2 * mItemWidth - moveX, 12 * mItemHeight - moveY);
        p.lineTo(centerX - mItemWidth - moveX, 11 * mItemHeight - moveY);
        p.lineTo(centerX - moveX, 12 * mItemHeight - moveY);
        p.lineTo(centerX - mItemWidth - moveX, 13 * mItemHeight - moveY);
        p.close();
        canvas.drawPath(p, mPaintShadow);

        //向右下运动  X,Y增加
        p.reset();
        p.moveTo(centerX - mItemWidth + moveX, 11 * mItemHeight + moveY);
        p.lineTo(centerX + moveX, 10 * mItemHeight + moveY);
        p.lineTo(centerX + mItemWidth + moveX, 11 * mItemHeight + moveY);
        p.lineTo(centerX + moveX, 12 * mItemHeight + moveY);
        p.close();
        canvas.drawPath(p, mPaintShadow);

        //向右下运动  X,Y增加
        p.reset();
        p.moveTo(centerX + moveX, 12 * mItemHeight + moveY);
        p.lineTo(centerX + mItemWidth + moveX, 11 * mItemHeight + moveY);
        p.lineTo(centerX + 2 * mItemWidth + moveX, 12 * mItemHeight + moveY);
        p.lineTo(centerX + mItemWidth + moveX, 13 * mItemHeight + moveY);
        p.close();
        canvas.drawPath(p, mPaintShadow);

        //向左上运动  X,Y减少
        p.reset();
        p.moveTo(centerX - mItemWidth - moveX, 13 * mItemHeight - moveY);
        p.lineTo(centerX - moveX, 12 * mItemHeight - moveY);
        p.lineTo(centerX + mItemWidth - moveX, 13 * mItemHeight - moveY);
        p.lineTo(centerX - moveX, 14 * mItemHeight - moveY);
        p.close();
        canvas.drawPath(p, mPaintShadow);

    }

    //绘制第二阶段的阴影
    private void drawShadow2(Canvas canvas, float valueAnimator) {

        float moveX = mItemWidth * (valueAnimator - 1.0f / 3) / (1.0f / 3);
        float moveY = mItemHeight * (valueAnimator - 1.0f / 3) / (1.0f / 3);

        //X增大 Y减少
        p.reset();
        p.moveTo(centerX - 2 * mItemWidth - mItemWidth / 2 + moveX, 12 * mItemHeight - mItemHeight / 2 - moveY);
        p.lineTo(centerX - mItemWidth - mItemWidth / 2 + moveX, 11 * mItemHeight - mItemHeight / 2 - moveY);
        p.lineTo(centerX - mItemWidth / 2 + moveX, 12 * mItemHeight - mItemHeight / 2 - moveY);
        p.lineTo(centerX - mItemWidth - mItemWidth / 2 + moveX, 13 * mItemHeight - mItemHeight / 2 - moveY);
        p.close();
        canvas.drawPath(p, mPaintShadow);


        p.reset();
        p.moveTo(centerX - mItemWidth + mItemWidth / 2, 11 * mItemHeight + mItemHeight / 2);
        p.lineTo(centerX + mItemWidth / 2, 10 * mItemHeight + mItemHeight / 2);
        p.lineTo(centerX + mItemWidth + mItemWidth / 2, 11 * mItemHeight + mItemHeight / 2);
        p.lineTo(centerX + mItemWidth / 2, 12 * mItemHeight + mItemHeight / 2);
        p.close();
        canvas.drawPath(p, mPaintShadow);

        //X 减少  Y增大
        p.reset();
        p.moveTo(centerX + mItemWidth / 2 - moveX, 12 * mItemHeight + mItemHeight / 2 + moveY);
        p.lineTo(centerX + mItemWidth + mItemWidth / 2 - moveX, 11 * mItemHeight + mItemHeight / 2 + moveY);
        p.lineTo(centerX + 2 * mItemWidth + mItemWidth / 2 - moveX, 12 * mItemHeight + mItemHeight / 2 + moveY);
        p.lineTo(centerX + mItemWidth + mItemWidth / 2 - moveX, 13 * mItemHeight + mItemHeight / 2 + moveY);
        p.close();
        canvas.drawPath(p, mPaintShadow);


        p.reset();
        p.moveTo(centerX - mItemWidth - mItemWidth / 2, 13 * mItemHeight - mItemHeight / 2);
        p.lineTo(centerX - mItemWidth / 2, 12 * mItemHeight - mItemHeight / 2);
        p.lineTo(centerX + mItemWidth - mItemWidth / 2, 13 * mItemHeight - mItemHeight / 2);
        p.lineTo(centerX - mItemWidth / 2, 14 * mItemHeight - mItemHeight / 2);
        p.close();
        canvas.drawPath(p, mPaintShadow);

    }

    //绘制第三阶段的阴影
    private void drawShadow3(Canvas canvas, float valueAnimator) {

        float moveX = mItemWidth / 2 * (valueAnimator - (1.0f / 3) * 2) / (1.0f / 3);
        float moveY = mItemHeight / 2 * (valueAnimator - (1.0f / 3) * 2) / (1.0f / 3);

        //X增大 Y增大
        p.reset();
        p.moveTo(centerX - 2 * mItemWidth - mItemWidth / 2 + mItemWidth + moveX, 12 * mItemHeight - mItemHeight / 2 - mItemHeight + moveY);
        p.lineTo(centerX - mItemWidth - mItemWidth / 2 + mItemWidth + moveX, 11 * mItemHeight - mItemHeight / 2 - mItemHeight + moveY);
        p.lineTo(centerX - mItemWidth / 2 + mItemWidth + moveX, 12 * mItemHeight - mItemHeight / 2 - mItemHeight + moveY);
        p.lineTo(centerX - mItemWidth - mItemWidth / 2 + mItemWidth + moveX, 13 * mItemHeight - mItemHeight / 2 - mItemHeight + moveY);
        p.close();
        canvas.drawPath(p, mPaintShadow);

        //X增大 Y增大
        p.reset();
        p.moveTo(centerX - mItemWidth + mItemWidth / 2 + moveX, 11 * mItemHeight + mItemHeight / 2 + moveY);
        p.lineTo(centerX + mItemWidth / 2 + moveX, 10 * mItemHeight + mItemHeight / 2 + moveY);
        p.lineTo(centerX + mItemWidth + mItemWidth / 2 + moveX, 11 * mItemHeight + mItemHeight / 2 + moveY);
        p.lineTo(centerX + mItemWidth / 2 + moveX, 12 * mItemHeight + mItemHeight / 2 + moveY);
        p.close();
        canvas.drawPath(p, mPaintShadow);

        //X 减少  Y减少
        p.reset();
        p.moveTo(centerX + mItemWidth / 2 - mItemWidth - moveX, 12 * mItemHeight + mItemHeight / 2 + mItemHeight - moveY);
        p.lineTo(centerX + mItemWidth + mItemWidth / 2 - mItemWidth - moveX, 11 * mItemHeight + mItemHeight / 2 + mItemHeight - moveY);
        p.lineTo(centerX + 2 * mItemWidth + mItemWidth / 2 - mItemWidth - moveX, 12 * mItemHeight + mItemHeight / 2 + mItemHeight - moveY);
        p.lineTo(centerX + mItemWidth + mItemWidth / 2 - mItemWidth - moveX, 13 * mItemHeight + mItemHeight / 2 + mItemHeight - moveY);
        p.close();
        canvas.drawPath(p, mPaintShadow);

        //X 减少  Y减少
        p.reset();
        p.moveTo(centerX - mItemWidth - mItemWidth / 2 - moveX, 13 * mItemHeight - mItemHeight / 2 - moveY);
        p.lineTo(centerX - mItemWidth / 2 - moveX, 12 * mItemHeight - mItemHeight / 2 - moveY);
        p.lineTo(centerX + mItemWidth - mItemWidth / 2 - moveX, 13 * mItemHeight - mItemHeight / 2 - moveY);
        p.lineTo(centerX - mItemWidth / 2 - moveX, 14 * mItemHeight - mItemHeight / 2 - moveY);
        p.close();
        canvas.drawPath(p, mPaintShadow);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        //处理 wrap_content问题
        int defaultDimension = dip2px(100);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultDimension, defaultDimension);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultDimension, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, defaultDimension);
        }

    }


    public void startAnimator() {
        post(new Runnable() {
            @Override
            public void run() {
                stopAnimator();
                startAnimator(0f, 1f, 800);
            }
        });
    }


    public void stopAnimator() {
        if (valueAnimator != null) {
            clearAnimation();
            valueAnimator.setRepeatCount(0);
            valueAnimator.cancel();
            valueAnimator.end();
            mValueAnimator = 0f;
            postInvalidate();
        }
    }

    private ValueAnimator startAnimator(float startValue, float endValue, long duration) {
        valueAnimator = ValueAnimator.ofFloat(startValue, endValue);
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);//无限循环
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mValueAnimator = (float) valueAnimator.getAnimatedValue();
                postInvalidate();
            }
        });
        if (!valueAnimator.isRunning()) {
            valueAnimator.start();
        }
        return valueAnimator;
    }

    public void isShadow(boolean show) {
        this.mShadow = show;
        postInvalidate();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}
