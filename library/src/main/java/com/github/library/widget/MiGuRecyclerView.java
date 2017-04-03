package com.github.library.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import com.github.library.BaseQuickAdapter;
import com.github.library.listener.AppBarStateChangeListener;

import static com.github.library.listener.BaseRefreshListener.STATE_REFRESHING;
import static com.github.library.listener.BaseRefreshListener.STATE_RELEASE_TO_REFRESH;

/**
 * Created by boby on 2017/3/31.
 */

public class MiGuRecyclerView extends RecyclerView {

    private BaseQuickAdapter mRefreshAdapter;

    private boolean pullRefreshEnabled = true;

    private MiGuRefreshHeader mMiGuRefreshHeader;

    private RefreshListener mRefreshListener;

    private float mLastY = -1;

    private boolean isScrollTop = true;

    private static final float DRAG_RATE = 2;

    private AppBarStateChangeListener.State appbarState = AppBarStateChangeListener.State.EXPANDED;

    public MiGuRecyclerView(Context context) {
        this(context, null);
    }

    public MiGuRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MiGuRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if (pullRefreshEnabled) {
            mMiGuRefreshHeader = new MiGuRefreshHeader(getContext());
        }
    }


    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof BaseQuickAdapter) {
            mRefreshAdapter = (BaseQuickAdapter) adapter;
            mRefreshAdapter.addHeaderView(mMiGuRefreshHeader, 0);
        }
        super.setAdapter(adapter);
    }


    public void refresh() {
        if (pullRefreshEnabled && mRefreshAdapter != null && mRefreshListener != null) {
            mMiGuRefreshHeader.setState(STATE_REFRESHING);
            mRefreshListener.onRefresh();
        }
    }

    public void refreshComplete() {
        if (mRefreshAdapter != null) {
            mMiGuRefreshHeader.refreshComplete();
        }
    }

    public void setPullRefreshEnabled(boolean enabled) {
        pullRefreshEnabled = enabled;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mRefreshAdapter != null) {
                    final float deltaY = ev.getRawY() - mLastY;
                    mLastY = ev.getRawY();
                    if (isScrollTop && isOnTop() && pullRefreshEnabled && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                        mMiGuRefreshHeader.onMove(deltaY / DRAG_RATE);
                        if (mMiGuRefreshHeader.getVisibleHeight() > 0 && mMiGuRefreshHeader.getState() < MiGuRefreshHeader.STATE_REFRESHING) {
                            return false;
                        }
                    }
                }
                break;
            default:
                if (mRefreshAdapter != null) {
                    mLastY = -1; // reset
                    if (isScrollTop && isOnTop() && pullRefreshEnabled && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                        if (mMiGuRefreshHeader.releaseAction()) {
                            if (mRefreshListener != null) {
                                mRefreshListener.onRefresh();
                            }
                        }
                    }
                }
                break;
        }

        return super.onTouchEvent(ev);
    }

    private boolean isOnTop() {
        if (mMiGuRefreshHeader.getParent() != null) {
            return true;
        }
        return false;
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        if (mRefreshAdapter != null && pullRefreshEnabled) {
            if (mMiGuRefreshHeader.getState() == STATE_RELEASE_TO_REFRESH || mMiGuRefreshHeader.getState() == STATE_REFRESHING) {
                isScrollTop = true;
            } else {
                //处理线性
                LayoutManager lm = getLayoutManager();
                if (lm instanceof LinearLayoutManager) {
                    LinearLayoutManager llm = (LinearLayoutManager) lm;

                    int position = llm.findFirstCompletelyVisibleItemPosition();

                    if (mRefreshAdapter.getHeaderLayout().getChildCount() == 1) {
                        if (position == 1 || position == 0) {
                            isScrollTop = true;
                        } else {
                            isScrollTop = false;
                        }
                    } else {
                        if (position == 0) {
                            isScrollTop = true;
                        } else {
                            isScrollTop = false;
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //解决和CollapsingToolbarLayout冲突的问题
        AppBarLayout appBarLayout = null;
        ViewParent p = getParent();
        while (p != null) {
            if (p instanceof CoordinatorLayout) {
                break;
            }
            p = p.getParent();
        }
        if (p instanceof CoordinatorLayout) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) p;
            final int childCount = coordinatorLayout.getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                final View child = coordinatorLayout.getChildAt(i);
                if (child instanceof AppBarLayout) {
                    appBarLayout = (AppBarLayout) child;
                    break;
                }
            }
            if (appBarLayout != null) {
                appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
                    @Override
                    public void onStateChanged(AppBarLayout appBarLayout, State state) {
                        appbarState = state;
                    }
                });
            }
        }
    }

    public interface RefreshListener {
        void onRefresh();
    }

    public void setRefreshListener(RefreshListener listener) {
        this.mRefreshListener = listener;
    }
}
