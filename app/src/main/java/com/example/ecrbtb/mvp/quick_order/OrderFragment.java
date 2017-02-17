package com.example.ecrbtb.mvp.quick_order;

import android.os.Bundle;

import com.example.ecrbtb.BasePageFragment;
import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.R;
import com.example.ecrbtb.widget.PageStateLayout;

/**
 * Created by boby on 2016/12/15.
 */

public class OrderFragment extends BasePageFragment {

    public static OrderFragment newInstance() {

        Bundle args = new Bundle();

        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getRootView() {
        return R.id.fl_root;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getResourceId() {
        return R.layout.fragment_quick_order_order;
    }


    @Override
    protected void initData() {
        super.initData();
        showPageState(PageStateLayout.ERROR_STATE);
    }
}
