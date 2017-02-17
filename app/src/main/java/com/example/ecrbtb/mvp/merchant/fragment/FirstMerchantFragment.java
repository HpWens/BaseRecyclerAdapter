package com.example.ecrbtb.mvp.merchant.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ecrbtb.BasePageFragment;
import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.R;
import com.example.ecrbtb.event.LoginSuccessEvent;
import com.example.ecrbtb.mvp.login.bean.Manager;
import com.example.ecrbtb.mvp.login.bean.Store;
import com.example.ecrbtb.mvp.merchant.adpter.MerchantPageAdapter;
import com.example.ecrbtb.mvp.merchant.presenter.MerchantPresenter;
import com.example.ecrbtb.widget.pulltozoomview.PullToZoomScrollViewEx;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    TextView mTvMerchantAddress;
    TextView mTvMerchantCode;
    TextView mTvMerchantName;
    TextView mTvStoreUserName;

    MerchantPresenter mPresenter;

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
        return mPresenter = new MerchantPresenter(_mActivity);
    }

    @Override
    public int getResourceId() {
        return R.layout.fragment_merchant_first;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        registerEventBus();

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterEventBus();
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
        final ViewPager mViewPager = (ViewPager) contentView.findViewById(R.id.viewpager);
        FrameLayout mFrameLayout = (FrameLayout) contentView.findViewById(R.id.fl_root);
        mFrameLayout.getLayoutParams().height = mTab.getLayoutParams().height + mScreenWidth;

        mViewPager.setAdapter(new MerchantPageAdapter(getChildFragmentManager()));

        mTab.setupWithViewPager(mViewPager);

        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mViewPager.getParent().getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        setupTabIcons(mTab);

        initHeaderViews(headView);

        loadHeaderData();
    }

    private void loadHeaderData() {
        Store localStore = this.mPresenter.getStoreById();
        Manager localManager = this.mPresenter.getManagerById();
        if ((localStore != null) && (localManager != null)) {
            mTvMerchantName.setText(localStore.Name);
            mTvMerchantCode.setText("商户代码：" + localStore.Code + "\n所属行业：" + localStore.Industry);
            mTvMerchantAddress.setText("商户地址：" + localStore.Address);
            mTvStoreUserName.setText("操作账号：" + localManager.UserName);
        }

    }

    private void initHeaderViews(View headView) {
        mTvMerchantName = ((TextView) headView.findViewById(R.id.tv_name));
        mTvMerchantCode = ((TextView) headView.findViewById(R.id.tv_code));
        mTvMerchantAddress = ((TextView) headView.findViewById(R.id.tv_address));
        mTvStoreUserName = ((TextView) headView.findViewById(R.id.tv_username));
    }


    private void setupTabIcons(TabLayout tabLayout) {
        tabLayout.getTabAt(0).setCustomView(getTabView("销售管理", R.mipmap.ic_sale_manager));
        tabLayout.getTabAt(1).setCustomView(getTabView("采购管理", R.mipmap.ic_purchase_manager));
    }

    public View getTabView(String text, int icon) {
        View view = LayoutInflater.from(_mActivity).inflate(R.layout.item_tab, null);
        TextView txt_title = (TextView) view.findViewById(tv);
        txt_title.setText(text);
        txt_title.setGravity(Gravity.CENTER);
        Drawable drawable = _mActivity.getResources().getDrawable(icon);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        txt_title.setCompoundDrawables(drawable, null, null, null);
        txt_title.setCompoundDrawablePadding(8);
        return view;
    }

    @Subscribe(priority = 1, threadMode = ThreadMode.MAIN)
    public void onReceiveMessage(@NonNull LoginSuccessEvent event) {
        loadHeaderData();
    }

}
