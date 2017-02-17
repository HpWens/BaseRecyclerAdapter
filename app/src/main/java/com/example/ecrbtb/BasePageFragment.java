package com.example.ecrbtb;

import android.view.View;
import android.view.ViewGroup;

import com.example.ecrbtb.annotation.PageState;
import com.example.ecrbtb.widget.PageStateLayout;

/**
 * Created by boby on 2016/12/15.
 */

public abstract class BasePageFragment extends BaseFragment {

    private PageStateLayout mPageStateLayout;

    @Override
    protected void initPageStateLayout(View view) {
        super.initPageStateLayout(view);
        if (getRootView() != 0) {
            if (mPageStateLayout == null) {
                mPageStateLayout = new PageStateLayout(_mActivity);
            }
            ViewGroup rootView = (ViewGroup) view.findViewById(getRootView());
            mPageStateLayout = new PageStateLayout(_mActivity);
            rootView.addView(mPageStateLayout, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            mPageStateLayout.setOnRetryListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    retryLoading();
                }
            });
        }
    }


    //获取布局父控件ID
    protected abstract int getRootView();


    //从新加载界面
    protected void retryLoading() {

    }

    /**
     * @param state 显示不同的界面
     */
    protected void showPageState(@PageState int state) {
        if (mPageStateLayout != null) {
            mPageStateLayout.show(state);
        }
    }

    protected void showPageState(@PageState int state, String emptyText) {
        if (mPageStateLayout != null) {
            mPageStateLayout.setEmptyPageText(emptyText);
            mPageStateLayout.show(state);
        }
    }

    /**
     * @return
     */
    protected int getPageState() {
        if (mPageStateLayout != null) {
            return mPageStateLayout.getState();
        }
        return 0;
    }
}
