package com.github.baserecycleradapter.activity;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.github.baserecycleradapter.widget.PullRefreshLoadView;

/**
 * Created by Administrator on 8/25 0025.
 */
public class WSRecyclerView extends RecyclerView {

    private PullRefreshLoadView mPullRefreshLoadView;

    private float mLastY = -1;

    private boolean mScrollTop;

    private OnRefreshCompleteListener mCompleteListener;

    public WSRecyclerView(Context context) {
        this(context, null);
    }

    public WSRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WSRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPullRefreshLoadView = new PullRefreshLoadView(context);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        if (mLastY == -1) {
            mLastY = e.getRawY();
        }
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isScrollTop()) {
                    float deltaY = e.getRawY() - mLastY;
                    mPullRefreshLoadView.onMove(deltaY / 3);
                    mLastY = e.getRawY();
                    if (mPullRefreshLoadView.getVisibleHeight() > 0 &&
                            mPullRefreshLoadView.getState() < mPullRefreshLoadView.STATE_REFRESHING) {
                        return false;
                    }
                }
                break;
            default:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                mLastY = -1; // reset
                if (mPullRefreshLoadView.releaseAction()) {
                    if (mCompleteListener != null) {
                        mCompleteListener.onRefreshComplete();
                    }
                }
                break;
        }

        return super.onTouchEvent(e);
    }

    public PullRefreshLoadView getRefreshView() {
        return mPullRefreshLoadView == null ? null : mPullRefreshLoadView;
    }

    public void refreshComplete() {
        mPullRefreshLoadView.refreshComplete();
    }

    public boolean isScrollTop() {
        if (getLayoutManager() instanceof LinearLayoutManager &&
                ((LinearLayoutManager) getLayoutManager()).findFirstCompletelyVisibleItemPosition() <= 1) {
            mScrollTop = true;
        } else {
            mScrollTop = false;
        }
        return mScrollTop;
    }

    public interface OnRefreshCompleteListener {
        void onRefreshComplete();
    }

    public void setOnRefreshCompleteListener(OnRefreshCompleteListener listener) {
        mCompleteListener = listener;
    }
}
