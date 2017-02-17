package com.example.ecrbtb.mvp.order;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;

import com.example.ecrbtb.BaseActivity;
import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.R;
import com.grasp.tint.SystemBarTintManager;

/**
 * Created by boby on 2017/1/4.
 */

public class OrderActivity extends BaseActivity {

    public static final String ORDER_DATA = "order_data";

    private String mOrderData = "";

    @Override
    protected void initIntent(Intent intent) {
        super.initIntent(intent);
        if (intent != null) {
            mOrderData = intent.getStringExtra(ORDER_DATA);
        }
    }

    @Override
    protected void initView(ViewDataBinding bind) {

    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_order;
    }

    @Override
    protected void initBarTint() {
        super.initBarTint();
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
    }

    @Override
    protected int getRootView() {
        return 0;
    }

    @Override
    protected void initInstanceState(Bundle savedInstanceState) {
        super.initInstanceState(savedInstanceState);
        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_container, OrderFragment.newInstance(mOrderData));
        }
    }
}
