package com.example.ecrbtb.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.ecrbtb.listener.MyAnimatorUpdateListener;

import java.util.ArrayList;
import java.util.List;

import static org.xutils.common.util.DensityUtil.dip2px;

/**
 * Created by Administrator on 8/19 0019.
 */
public class PageLoadingView extends View {
    //画笔
    private Paint mPaint;
    //中心X 坐标
    private float mCenterX;
    //中心Y 坐标
    private float mCenterY;
    //半径长度
    private float mRadius;
    //动画是否运行中
    private boolean mAnimatorEnable;

    public static final float SCALE = 1.0f;

    public static final int ALPHA = 255;
    //缩放动画集合
    private List<Animator> scaleAnimList;
    //透明度动画集合
    private List<Animator> alphaAnimList;

    //缩放集合
    float[] scaleFloats = new float[]{SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE};
    //透明度集合
    int[] alphas = new int[]{ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA};

    public PageLoadingView(Context context) {
        this(context, null);
    }

    public PageLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    /**
     * 初始化数据
     */
    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(dip2px(1));
        mPaint.setColor(Color.parseColor("#666666"));

        scaleAnimList = new ArrayList<>();
        alphaAnimList = new ArrayList<>();

        initAnimator();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        //处理 wrap_content问题
        int defaultDimension = dip2px(32);

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

    /**
     * 初始化动画
     */
    private void initAnimator() {
        int[] delays = {0, 120, 240, 360, 480, 600, 720, 780, 840};
        for (int i = 0; i < 8; i++) {
            final int index = i;
            ValueAnimator scaleAnim = ValueAnimator.ofFloat(1, 0.4f, 1);
            scaleAnim.setDuration(1000);
            scaleAnim.setRepeatMode(ValueAnimator.RESTART);
            scaleAnim.setRepeatCount(ValueAnimator.INFINITE);
            scaleAnim.setStartDelay(delays[i]);
            scaleAnim.addUpdateListener(new MyAnimatorUpdateListener<PageLoadingView>(PageLoadingView.this) {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    scaleFloats[index] = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            // scaleAnim.start();
            scaleAnimList.add(scaleAnim);

            ValueAnimator alphaAnim = ValueAnimator.ofInt(255, 77, 255);
            alphaAnim.setDuration(1000);
            alphaAnim.setRepeatMode(ValueAnimator.RESTART);
            alphaAnim.setRepeatCount(ValueAnimator.INFINITE);
            alphaAnim.setStartDelay(delays[i]);
            alphaAnim.addUpdateListener(new MyAnimatorUpdateListener<PageLoadingView>(PageLoadingView.this) {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    alphas[index] = (int) valueAnimator.getAnimatedValue();
                    postInvalidate();
                }
            });
            alphaAnimList.add(alphaAnim);
            //alphaAnim.start();
        }

    }

    public void endAnimator() {
        for (Animator animator : scaleAnimList) {
            animator.end();
        }
        for (Animator animator : alphaAnimList) {
            animator.end();
        }
        mAnimatorEnable = false;
    }

    public void startAnimator() {
        for (Animator animator : scaleAnimList) {
            animator.start();
        }
        for (Animator animator : alphaAnimList) {
            animator.start();
        }
        mAnimatorEnable = true;
    }

    public boolean isLoading() {
        return mAnimatorEnable;
    }

}
