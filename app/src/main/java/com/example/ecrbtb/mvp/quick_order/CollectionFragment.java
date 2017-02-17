package com.example.ecrbtb.mvp.quick_order;

import android.os.Bundle;

import com.example.ecrbtb.BasePageFragment;
import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.R;
import com.example.ecrbtb.widget.PageStateLayout;

/**
 * Created by boby on 2016/12/15.
 */

public class CollectionFragment extends BasePageFragment {

    private static final int COLLECTION = 0;
    private static final int COMMODITY = 1;

    private int mState = COLLECTION;

    private static final String ARGS_STATE = "args_state";

    public static CollectionFragment newInstance(int state) {

        Bundle args = new Bundle();
        args.getInt(ARGS_STATE, state);
        CollectionFragment fragment = new CollectionFragment();
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
        return R.layout.fragment_quick_order_collection;
    }


    @Override
    protected void initData() {
        super.initData();
        showPageState(PageStateLayout.EMPTY_STATE);
    }
}
