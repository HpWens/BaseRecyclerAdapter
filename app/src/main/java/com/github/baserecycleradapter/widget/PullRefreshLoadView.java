package com.github.baserecycleradapter.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.baserecycleradapter.R;

/**
 * Created by Administrator on 8/24 0024.
 */
public class PullRefreshLoadView extends LinearLayout {

    private Context mContext; //上下文

    private LinearLayout mContainer;

    private ImageView mArrowIv;  //箭头

    private TextGradientView mStatusTv;  //下拉刷新  释放刷新  刷新完成

    private BallSpinFadeLoader mLoader;  //loading 图标

    private int mState;     //状态

    private int mHeight;  //高度

    private Animation mArrowDownAnim;//向下动画
    private Animation mArrowUpAnim;  //向上动画

    public final static int STATE_PULL_TO_REFRESH = 0;
    public final static int STATE_RELEASE_TO_REFRESH = 1;
    public final static int STATE_REFRESHING = 2;
    public final static int STATE_REFRESHED = 3;

    public final static String PULL_TO_REFRESH = "下拉刷新";
    public final static String RELEASE_TO_REFRESH = "释放立即刷新";
    public final static String REFRESHING = "正在刷新...";
    public final static String REFRESHED = "刷新完成";

    public PullRefreshLoadView(Context context) {
        this(context, null);
    }

    public PullRefreshLoadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRefreshLoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        mState = STATE_PULL_TO_REFRESH;
        mContainer = (LinearLayout) LayoutInflater.from(getContext()).inflate(
                R.layout.rv_refresh, null);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        setLayoutParams(lp);
        setPadding(0, 0, 0, 0);

        addView(mContainer, new LayoutParams(LayoutParams.MATCH_PARENT, 0));
        setGravity(Gravity.BOTTOM);

        mArrowIv = (ImageView) mContainer.findViewById(R.id.iv_arrow);
        mStatusTv = (TextGradientView) mContainer.findViewById(R.id.tv_refresh_status);
        mLoader = (BallSpinFadeLoader) mContainer.findViewById(R.id.ball_loader);

        mArrowDownAnim = new RotateAnimation(180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mArrowDownAnim.setDuration(180);
        mArrowDownAnim.setFillAfter(true);

        mArrowUpAnim = new RotateAnimation(0.0f, 180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mArrowUpAnim.setDuration(180);
        mArrowUpAnim.setFillAfter(true);

        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mHeight = getMeasuredHeight();
    }


    /**
     * @param resId
     */
    public void setArrowImageView(int resId) {
        if (mArrowIv != null) {
            mArrowIv.setImageResource(resId);
        }
    }

    public int getState() {
        return mState;
    }

    public int getVisibleHeight() {
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        return lp.height;
    }


    public void setVisibleHeight(int height) {
        if (height <= 0)
            height = 0;
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    public void refreshComplete() {
        setState(STATE_REFRESHED);
        reset();
    }

    /**
     * @param delta
     */
    public void onMove(float delta) {
        if (getVisibleHeight() > 0 || delta > 0) {
            setVisibleHeight((int) (delta + getVisibleHeight()));
            if (mState <= STATE_RELEASE_TO_REFRESH) {
                if (getVisibleHeight() > mHeight) {
                    setState(STATE_RELEASE_TO_REFRESH);
                } else {
                    setState(STATE_PULL_TO_REFRESH);
                }
            }
        }
    }

    public boolean releaseAction() {
        boolean isOnRefresh = false;
        int height = getVisibleHeight();
        int destHeight = 0;
        if (height == 0) isOnRefresh = false;

        if (mState == STATE_REFRESHING) {
            destHeight = mHeight;
        }

        if (height > mHeight && mState < STATE_REFRESHING) {
            setState(STATE_REFRESHING);
            destHeight = mHeight;
            isOnRefresh = true;
        }

        smoothScrollTo(destHeight);

        return isOnRefresh;
    }

    public void reset() {
        smoothScrollTo(0);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                setState(STATE_PULL_TO_REFRESH);
            }
        }, 200);
    }

    /**
     * @param state
     */
    public void setState(int state) {
        if (state == mState)
            return;
        if (state == STATE_REFRESHING) {
            if (!mLoader.isLoading()) {
                mLoader.startAnimator();
            }
            mArrowIv.clearAnimation();
            mStatusTv.setText(REFRESHING);
            mStatusTv.startAnimator();
            mLoader.setVisibility(View.VISIBLE);
            mArrowIv.setVisibility(View.INVISIBLE);
        } else if (state == STATE_PULL_TO_REFRESH) {
            mArrowIv.setVisibility(View.VISIBLE);
            mLoader.setVisibility(View.INVISIBLE);
            mStatusTv.setText(PULL_TO_REFRESH);
            if (mState == STATE_RELEASE_TO_REFRESH) {
                mArrowIv.startAnimation(mArrowDownAnim);
            } else if (mState == STATE_REFRESHING) {
                mArrowIv.clearAnimation();
            }
        } else if (state == STATE_RELEASE_TO_REFRESH) {
            mArrowIv.setVisibility(View.VISIBLE);
            mLoader.setVisibility(View.INVISIBLE);
            mStatusTv.setText(RELEASE_TO_REFRESH);
            if (mState != STATE_RELEASE_TO_REFRESH) {
                mArrowIv.clearAnimation();
                mArrowIv.startAnimation(mArrowUpAnim);
            }
        } else if (state == STATE_REFRESHED) {
            mArrowIv.clearAnimation();
            mStatusTv.stopAnimator();
            mStatusTv.setText(REFRESHED);
            mArrowIv.setVisibility(View.INVISIBLE);
            mLoader.setVisibility(View.INVISIBLE);
        }
        mState = state;
    }

    /**
     * @param destHeight
     */
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

}
