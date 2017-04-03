package com.github.library.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.library.R;
import com.github.library.listener.BaseRefreshListener;

/**
 * Created by boby on 2017/3/31.
 */

public class MiGuRefreshHeader extends LinearLayout implements BaseRefreshListener {

    private LinearLayout mContainer;

    private ImageView mMiGuImageView;

    private TextView mStatusTextView;

    private int mState = STATE_NORMAL;

    private int mMeasureHeight;

    private AnimationDrawable mMiGuDrawable;

    public MiGuRefreshHeader(Context context) {
        this(context, null);
    }

    public MiGuRefreshHeader(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MiGuRefreshHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 初始化 View
     */
    private void initView() {

        // 初始情况，设置下拉刷新view高度为0
        mContainer = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.refresh_header, null);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        this.setLayoutParams(lp);
        this.setPadding(0, 0, 0, 0);

        addView(mContainer, new LayoutParams(LayoutParams.MATCH_PARENT, 0));
        setGravity(Gravity.BOTTOM);

        //图片控件
        mMiGuImageView = (ImageView) findViewById(R.id.iv_refresh);
        //文本控件
        mStatusTextView = (TextView) findViewById(R.id.tv_status);

        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mMeasureHeight = getMeasuredHeight();//获取控件高度 默认 40dp 由于测试机密度为 3 所以像素为 120px
    }

    public void setMiGuImageView(int resId) {
        mMiGuImageView.setImageResource(resId);
    }

    public void setState(int state) {
        if (state == mState) return;

        if (state == STATE_NORMAL) {
            //下拉
            mMiGuImageView.setImageResource(R.drawable.pull_down);
            mStatusTextView.setText(R.string.pull_refresh);
        } else if (state == STATE_RELEASE_TO_REFRESH) {
            //释放
            mMiGuImageView.setImageResource(R.drawable.pull_end);
            mStatusTextView.setText(R.string.release_refresh);
        } else if (state == STATE_REFRESHING) {
            //刷新
            mStatusTextView.setText(R.string.refreshing);
            mMiGuImageView.setImageResource(R.drawable.refreshing);
            mMiGuDrawable = (AnimationDrawable) mMiGuImageView.getDrawable();
            //播放动画
            mMiGuDrawable.start();

            smoothScrollTo(mMeasureHeight);
        } else if (state == STATE_DONE) {
            //完成
            if (mMiGuDrawable != null)
                //停止动画
                mMiGuDrawable.stop();
        }

        mState = state;
    }

    public int getState() {
        return mState;
    }


    public void setVisibleHeight(int height) {
        if (height < 0) height = 0;

        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    public int getVisibleHeight() {
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        return lp.height;
    }

    public void reset() {
        smoothScrollTo(0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setState(STATE_NORMAL);
            }
        }, 500);
    }

    private void smoothScrollTo(int destHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(getVisibleHeight(), destHeight);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setVisibleHeight((int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    @Override
    public void onMove(float delta) {

        if (getVisibleHeight() > 0 || delta > 0) {
            setVisibleHeight((int) delta + getVisibleHeight());
            if (mState <= STATE_RELEASE_TO_REFRESH) {
                if (getVisibleHeight() < mMeasureHeight) {
                    setState(STATE_NORMAL);
                } else {
                    setState(STATE_RELEASE_TO_REFRESH);
                }
            }
        }
    }

    @Override
    public boolean releaseAction() {

        boolean isOnRefresh = false;

        int height = getVisibleHeight();

        if (height == 0) {
            isOnRefresh = false;
        }

        if (getVisibleHeight() > mMeasureHeight && mState < STATE_REFRESHING) {
            //刷新状态
            setState(STATE_REFRESHING);
            isOnRefresh = true;
        }

        if (mState == STATE_REFRESHING && height <= mMeasureHeight) {
            //处于刷新状态，手指还在向上滑动
        }

        if (mState != STATE_REFRESHING) {
            smoothScrollTo(0);
        }

        if (mState == STATE_REFRESHING) {
            int destHeight = mMeasureHeight;
            smoothScrollTo(destHeight);
        }

        return isOnRefresh;
    }

    @Override
    public void refreshComplete() {
        setState(STATE_DONE);

        reset();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//             reset();
//            }
//        }, 200);
    }
}
