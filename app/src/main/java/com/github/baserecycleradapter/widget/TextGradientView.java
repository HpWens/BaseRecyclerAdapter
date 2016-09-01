package com.github.baserecycleradapter.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 8/31 0031.
 */
public class TextGradientView extends View {

    private Context mContext;
    private Paint mPaint;
    private int[] mColors;
    private float[] mPositions;

    private float mAnimatorValue;
    private ValueAnimator mAnimator;
    private LinearGradient mLinearGradient;
    private boolean mGradientEnable;

    private String mTextStr;

    private final static String DEFAULT_TEXT = "下拉刷新";

    public TextGradientView(Context context) {
        this(context, null);
    }

    public TextGradientView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextGradientView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mTextStr = DEFAULT_TEXT;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(dip2px(16));
        mColors = new int[]{0xff000000, 0xffff0000, 0xff00ff00, 0xff0000ff, 0xffffff00, 0xff000000};
        mPositions = new float[]{0f, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f};
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mGradientEnable) {
            mPaint.setShader(mLinearGradient = new LinearGradient(0, 0, mAnimatorValue, mAnimatorValue, mColors,
                    mPositions, Shader.TileMode.CLAMP));
        }
        canvas.drawText(mTextStr, 0, -mPaint.getFontMetrics().top, mPaint);
    }

    public void startAnimator() {
        float textWidth = getWidth();
        mGradientEnable = true;
        mAnimator = ValueAnimator.ofFloat(0f, textWidth);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.setDuration(3000);
        mAnimator.addUpdateListener(new MyUpdateListener(this) {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatorValue = (float) valueAnimator.getAnimatedValue();
                postInvalidate();
            }
        });
        mAnimator.start();
    }

    public void stopAnimator() {
        if (mAnimator != null) {
            mGradientEnable = false;
            mPaint.setShader(new LinearGradient(0, 0, 0, 0, 0xff000000, 0xff000000, Shader.TileMode.CLAMP));
            mAnimatorValue = 0;
            clearAnimation();
            mAnimator.setRepeatCount(1);
            mAnimator.cancel();
            mAnimator.end();
            postInvalidate();
        }

    }

    /**
     * @param text
     */
    public void setText(String text) {
        mTextStr = text;
        postInvalidate();
    }

    /**
     * @param dpValue
     * @return
     */
    public int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    private static class MyUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        private WeakReference<TextGradientView> mWeakReference;

        public MyUpdateListener(TextGradientView gradientView) {
            mWeakReference = new WeakReference<TextGradientView>(gradientView);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            TextGradientView grad = mWeakReference.get();
            if (grad == null) {
                return;
            }
        }
    }
}

