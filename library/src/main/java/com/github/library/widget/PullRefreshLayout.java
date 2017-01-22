package com.github.library.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.library.R;
import com.github.library.util.ScreenUtils;


/**
 * Created by boby on 2016/12/11.
 */

public class PullRefreshLayout extends LinearLayout {

    private Context mContext;

    private LinearLayout mRefreshLayout;

    private RefreshView mRefreshView;

    private TextView mRefreshText;

    private final static String PULL_REFRESH_STR = "下拉刷新";

    private final static String RELEASE_REFRESH_STR = "松开刷新";

    private final static String REFRESHING_STR = "正在刷新...";

    private final static String REFRESHED_STR = "数据加载完毕";

    private String mPullRefreshStr;

    private String mReleaseRefreshStr;

    private String mRefreshingStr;

    private String mRefreshedStr;
    //下拉刷新状态
    public final static int PULL_REFRESH_STATE = 0;
    //松开刷新状态
    public final static int RELEASE_REFRESH_STATE = 1;
    //正在刷新状态
    public final static int REFRESHING_STATE = 2;
    //数据加载完毕状态
    public final static int REFRESHED_STATE = 3;
    //放大进度
    private final static int MAX_PROGRESS = 100;

    private int mRefreshState;
    //下拉多高 显示松开刷新
    private int mRefreshHeight;

    public PullRefreshLayout(Context context) {
        this(context, null);
    }

    public PullRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        removeAllViews();
        setOrientation(HORIZONTAL);
        setGravity(Gravity.BOTTOM);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;

        mPullRefreshStr = PULL_REFRESH_STR;
        mReleaseRefreshStr = RELEASE_REFRESH_STR;
        mRefreshingStr = REFRESHING_STR;
        mRefreshedStr = REFRESHED_STR;

        mRefreshState = PULL_REFRESH_STATE;
        mRefreshHeight = ScreenUtils.dip2px(mContext, 54);

        addRefreshLayout();
    }

    private void addRefreshLayout() {
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);

        mRefreshLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.refresh_header, null);
        addView(mRefreshLayout, new LayoutParams(LayoutParams.MATCH_PARENT, 1));

        mRefreshView = (RefreshView) findViewById(R.id.refresh_view);
        mRefreshText = (TextView) findViewById(R.id.refresh_status_tv);

        this.setLayoutParams(lp);
        this.setPadding(0, 0, 0, 0);
    }

    /***
     * @return
     */
    private LayoutParams getRefreshLayoutParams() {
        return (LayoutParams) mRefreshLayout.getLayoutParams();
    }

    /**
     * 获取可见的高度
     *
     * @return
     */
    public int getVisibleHeight() {
        LayoutParams lp = getRefreshLayoutParams();
        return lp.height;
    }

    /**
     * 移动过程中不断改变高度和状态
     *
     * @param delta
     */
    public void onMove(float delta) {
        if (getVisibleHeight() > 0 || delta > 0) {
            setVisibleHeight((int) (delta + getVisibleHeight()));
            if (mRefreshState <= RELEASE_REFRESH_STATE) {
                changeRefreshState(getVisibleHeight() >= mRefreshHeight ? RELEASE_REFRESH_STATE : PULL_REFRESH_STATE, getVisibleHeight() * MAX_PROGRESS / mRefreshHeight);
            }
        }
    }

    public boolean releaseAction() {
        boolean isRefresh = false;
        int visibleHeight = getVisibleHeight();
        int destHeight = 1;
        if (visibleHeight == 1) {
            isRefresh = false;
        }

        if (visibleHeight > mRefreshHeight && mRefreshState < REFRESHING_STATE) {
            changeRefreshState(REFRESHING_STATE, MAX_PROGRESS);
            isRefresh = true;
        }

        if (mRefreshState == REFRESHING_STATE) {
            destHeight = mRefreshHeight;
        } else if (mRefreshState == PULL_REFRESH_STATE) {
            mRefreshView.setIsDrawArrow(true);
            mRefreshView.setProgress(0);
        }

        releaseMoveToRefreshHeight(destHeight);

        return isRefresh;
    }

    /**
     * 重置刷新
     */
    public void resetRefresh() {
        releaseMoveToRefreshHeight(0);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                changeRefreshState(PULL_REFRESH_STATE, 0);
            }
        }, 500);
    }

    /**
     * 刷新完成
     */
    public void refreshComplete() {
        changeRefreshState(REFRESHED_STATE, MAX_PROGRESS);
        resetRefresh();
    }

    /**
     * @param destHeight 释放点移动到最低点
     */
    private void releaseMoveToRefreshHeight(int destHeight) {
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

    /**
     * 状态发生改变
     *
     * @param state
     * @param progress
     */
    public void changeRefreshState(int state, int progress) {
        if (mRefreshState != 0 && state == mRefreshState) {
            return;
        }
        switch (state) {
            default:
            case PULL_REFRESH_STATE:
                mRefreshView.setVisibility(View.VISIBLE);
                mRefreshText.setText(mPullRefreshStr);
                mRefreshView.setProgress(progress);
                mRefreshView.setIsDrawArrow(true);
                //mRefreshView.startAnimator();
                break;
            case RELEASE_REFRESH_STATE:
                mRefreshView.setVisibility(View.VISIBLE);
                mRefreshText.setText(mReleaseRefreshStr);
                mRefreshView.setProgress(MAX_PROGRESS);
                mRefreshView.setIsDrawArrow(false);
                mRefreshView.endAnimator();
                break;
            case REFRESHING_STATE:
                mRefreshView.setVisibility(View.VISIBLE);
                mRefreshText.setText(mRefreshingStr);
                mRefreshView.setProgress(MAX_PROGRESS);
                mRefreshView.setIsDrawArrow(false);
                mRefreshView.startAnimator();
                break;
            case REFRESHED_STATE:
                mRefreshView.setVisibility(View.GONE);
                mRefreshText.setText(mRefreshedStr);
                break;
        }
        mRefreshState = state;
    }

    /**
     * 设置可见的高度
     *
     * @param height
     */
    public void setVisibleHeight(int height) {
        if (height <= 0)
            height = 0;
        LayoutParams lp = getRefreshLayoutParams();
        lp.height = height;
        mRefreshLayout.setLayoutParams(lp);
    }


    /**
     * args数组显示的文字
     *
     * @param args
     */
    public void setRefreshText(String... args) {
        if (args == null) {
            return;
        }
        mPullRefreshStr = args[0] == null ? PULL_REFRESH_STR : args[0];
        mReleaseRefreshStr = args[1] == null ? RELEASE_REFRESH_STR : args[1];
        mRefreshingStr = args[2] == null ? REFRESHING_STR : args[2];
        mRefreshedStr = args[3] == null ? REFRESHED_STR : args[3];
    }

    public int getRefreshState() {
        return mRefreshState;
    }

    public void setRefreshState(int refreshState) {
        mRefreshState = refreshState;
    }

}
