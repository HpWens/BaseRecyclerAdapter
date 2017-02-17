package com.example.ecrbtb.mvp.quick_order;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;

import com.example.ecrbtb.BasePageFragment;
import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.R;
import com.example.ecrbtb.mvp.quick_order.adapter.PagerFragmentAdapter;

import butterknife.InjectView;

/**
 * Created by boby on 2016/12/15.
 */

public class FirstQuickOrderFragment extends BasePageFragment {

    @InjectView(R.id.tab)
    TabLayout mTab;
    @InjectView(R.id.viewpager)
    ViewPager mViewPager;
    @InjectView(R.id.bar_layout)
    AppBarLayout mBarLayout;
    @InjectView(R.id.fl_root)
    FrameLayout mFlRoot;

    public static FirstQuickOrderFragment newInstance() {

        Bundle args = new Bundle();

        FirstQuickOrderFragment fragment = new FirstQuickOrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getRootView() {
        return 0;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getResourceId() {
        return R.layout.fragment_quick_order_first;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);

        mTab.addTab(mTab.newTab());
        mTab.addTab(mTab.newTab());
        mTab.addTab(mTab.newTab());

        mViewPager.setAdapter(new PagerFragmentAdapter(getChildFragmentManager()));
        mViewPager.setOffscreenPageLimit(3);

        mTab.setupWithViewPager(mViewPager);
    }

}
