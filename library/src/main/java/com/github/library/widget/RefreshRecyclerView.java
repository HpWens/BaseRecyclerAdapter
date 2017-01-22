package com.github.library.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by boby on 2016/12/12.
 */

public class RefreshRecyclerView extends RecyclerView {

    private PullRefreshLayout mPullRefreshLayout;

    private float mSlidingY = -1;

    private boolean mIsRefresh = true;

    private OnRefreshCompleteListener mListener;

    private static final float DRAG_RATE = 2;

    public RefreshRecyclerView(Context context) {
        this(context, null);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    //初始化数据
    private void init(Context context) {
        mPullRefreshLayout = new PullRefreshLayout(context);
    }

    public PullRefreshLayout getPullRefreshLayout() {
        return mPullRefreshLayout;
    }

    /**
     * 刷新完成
     */
    public void refreshComplete() {
        mPullRefreshLayout.refreshComplete();
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (mSlidingY == -1) {
            mSlidingY = e.getRawY();
        }
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mSlidingY = e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaY = e.getRawY() - mSlidingY;
                mSlidingY = e.getRawY();
                if (mIsRefresh && isSlidingTop()) {
                    mPullRefreshLayout.onMove(deltaY / DRAG_RATE);
                    if (mPullRefreshLayout.getVisibleHeight() > 0 && mPullRefreshLayout.getRefreshState() < mPullRefreshLayout.REFRESHING_STATE) {
                        return false;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
            default:
                mSlidingY = -1;
                if (mPullRefreshLayout.releaseAction()) {
                    if (mListener != null) {
                        mListener.onRefreshCompleteListener();
                    }
                }
                break;

        }
        return super.onTouchEvent(e);
    }

    public boolean isRefresh() {
        return mIsRefresh;
    }

    //是否滑动到顶部
    private boolean isSlidingTop() {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            if (((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition() == 0) {
                return true;
            }
            return false;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int positions[] = ((StaggeredGridLayoutManager) getLayoutManager()).findFirstVisibleItemPositions(null);
            if (getChildAt(0) != null && getChildAt(0).getY() == 0 && positions[0] == 0) {
                return true;
            }
            return false;
        }
        return false;
    }

    public void setRefresh(boolean refresh) {
        mIsRefresh = refresh;
    }

    public interface OnRefreshCompleteListener {
        void onRefreshCompleteListener();
    }

    public void setOnRefreshCompleteListener(OnRefreshCompleteListener listener) {
        this.mListener = listener;
    }
}
