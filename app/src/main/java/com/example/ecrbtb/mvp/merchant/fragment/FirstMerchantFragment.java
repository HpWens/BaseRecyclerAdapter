package com.example.ecrbtb.mvp.merchant.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ecrbtb.BasePageFragment;
import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.R;
import com.example.ecrbtb.mvp.merchant.adpter.MerchantPageAdapter;
import com.example.ecrbtb.widget.pulltozoomview.PullToZoomScrollViewEx;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.example.ecrbtb.R.id.tv;

/**
 * Created by boby on 2016/12/15.
 */

public class FirstMerchantFragment extends BasePageFragment {

    @InjectView(R.id.scroll_view)
    PullToZoomScrollViewEx mPullEx;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    public static FirstMerchantFragment newInstance() {

        Bundle args = new Bundle();

        FirstMerchantFragment fragment = new FirstMerchantFragment();
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
        return R.layout.fragment_merchant_first;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        loadViewForCode();

        mToolbar.setTitle("商户中心");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.onBackPressed();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    private void loadViewForCode() {

        View headView = LayoutInflater.from(_mActivity).inflate(R.layout.profile_zoom_head, null, false);
        View zoomView = LayoutInflater.from(_mActivity).inflate(R.layout.profile_zoom_view, null, false);
        View contentView = LayoutInflater.from(_mActivity).inflate(R.layout.profile_content_view, null, false);
        mPullEx.setHeaderView(headView);
        mPullEx.setZoomView(zoomView);
        mPullEx.setScrollContentView(contentView);

        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        _mActivity.getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
        mPullEx.setHeaderLayoutParams(localObject);

        TabLayout mTab = (TabLayout) contentView.findViewById(R.id.tab);
        ViewPager mViewPager = (ViewPager) contentView.findViewById(R.id.viewpager);
        FrameLayout mFrameLayout = (FrameLayout) contentView.findViewById(R.id.fl_root);
        mFrameLayout.getLayoutParams().height = mTab.getLayoutParams().height + mScreenWidth;

        mViewPager.setAdapter(new MerchantPageAdapter(getChildFragmentManager()));

        mTab.setupWithViewPager(mViewPager);

        setupTabIcons(mTab);
    }


    private void setupTabIcons(TabLayout tabLayout) {
        tabLayout.getTabAt(0).setCustomView(getTabView("销售管理"));
        tabLayout.getTabAt(1).setCustomView(getTabView("采购管理"));
    }

    public View getTabView(String text) {
        View view = LayoutInflater.from(_mActivity).inflate(R.layout.item_tab, null);
        TextView txt_title = (TextView) view.findViewById(tv);
        txt_title.setText(text);
        Drawable drawable = _mActivity.getResources().getDrawable(R.drawable.ic_discover_white_24dp);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        txt_title.setCompoundDrawables(drawable, null, null, null);
        txt_title.setCompoundDrawablePadding(4);
        return view;
    }

}
