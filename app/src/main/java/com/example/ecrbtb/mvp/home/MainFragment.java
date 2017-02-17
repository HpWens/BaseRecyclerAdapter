package com.example.ecrbtb.mvp.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.ecrbtb.BaseFragment;
import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.MyApplication;
import com.example.ecrbtb.R;
import com.example.ecrbtb.event.BackToFirstFragmentEvent;
import com.example.ecrbtb.event.StartBrotherEvent;
import com.example.ecrbtb.event.TabSelectedEvent;
import com.example.ecrbtb.mvp.category.CategoryFragment;
import com.example.ecrbtb.mvp.category.event.ShoppingCartEvent;
import com.example.ecrbtb.mvp.home.fragment.HomeFragment;
import com.example.ecrbtb.mvp.home.presenter.HomePresenter;
import com.example.ecrbtb.mvp.home.view.IHomeView;
import com.example.ecrbtb.mvp.login.LoginActivity;
import com.example.ecrbtb.mvp.merchant.fragment.MerchantFragment;
import com.example.ecrbtb.mvp.quick_order.QuickOrderFragment;
import com.example.ecrbtb.mvp.shopping.ShoppingFragment;
import com.example.ecrbtb.widget.BottomBar;
import com.example.ecrbtb.widget.BottomBarTab;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.InjectView;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by boby on 2016/12/18.
 */

public class MainFragment extends BaseFragment implements IHomeView {

    @InjectView(R.id.bottom_bar)
    BottomBar mBottomBar;

    public static final int HOME = 0;
    public static final int CATEGORY = 1;
    public static final int QUICK_ORDER = 2;
    public static final int SHOPPING_CART = 3;
    public static final int MERCHANT_CENTER = 4;

    private HomePresenter mHomePresenter;

    private SupportFragment[] mFragments = new SupportFragment[5];

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initInstanceState(Bundle savedInstanceState) {
        super.initInstanceState(savedInstanceState);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (savedInstanceState == null) {
            mFragments[HOME] = HomeFragment.newInstance();
            mFragments[CATEGORY] = CategoryFragment.newInstance();
            mFragments[QUICK_ORDER] = QuickOrderFragment.newInstance();
            mFragments[SHOPPING_CART] = ShoppingFragment.newInstance();
            mFragments[MERCHANT_CENTER] = MerchantFragment.newInstance();

            loadMultipleRootFragment(R.id.fl_container, HOME,
                    mFragments[HOME],
                    mFragments[CATEGORY],
                    mFragments[QUICK_ORDER],
                    mFragments[SHOPPING_CART],
                    mFragments[MERCHANT_CENTER]);

        } else {
            mFragments[HOME] = findChildFragment(HomeFragment.class);
            mFragments[CATEGORY] = findChildFragment(CategoryFragment.class);
            mFragments[QUICK_ORDER] = findChildFragment(QuickOrderFragment.class);
            mFragments[SHOPPING_CART] = findChildFragment(ShoppingFragment.class);
            mFragments[MERCHANT_CENTER] = findChildFragment(MerchantFragment.class);
        }
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        registerEventBus();

        mHomePresenter = new HomePresenter(this);

        mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_home_24dp, getString(R.string.home)))
                .addItem(new BottomBarTab(_mActivity, R.drawable.ic_category_24dp, getString(R.string.category)))
                .addItem(new BottomBarTab(_mActivity, R.drawable.ic_quick_order_24dp, getString(R.string.quick_order)))
                .addItem(new BottomBarTab(_mActivity, R.drawable.ic_cart_24dp, getString(R.string.shopping_cart)))
                .addItem(new BottomBarTab(_mActivity, R.drawable.ic_merchant_24dp, getString(R.string.merchant_center)));

        mBottomBar.getItem(3).setUnreadCount(mHomePresenter.getShoppingCartNum());

        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                if(position==4){
                    if (!mHomePresenter.isLogin()) {
                        startActivity(new Intent(_mActivity, LoginActivity.class));
                        mBottomBar.setCurrentItem(prePosition);
                        return;
                    }
                }
                showHideFragment(mFragments[position], mFragments[prePosition]);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                SupportFragment currentFragment = mFragments[position];
                int count = currentFragment.getChildFragmentManager().getBackStackEntryCount();
//                if (count == 1) {
//                    EventBus.getDefault().post(new TabSelectedEvent(position));
//                }
            }
        });

        MyApplication.getInstance().setIsLogin(mHomePresenter.isLogin());

    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getResourceId() {
        return R.layout.activity_home;
    }


    @Subscribe
    public void onReceiveMessage(@NonNull ShoppingCartEvent event) {
        if (event.isReset) {
            mBottomBar.getItem(3).setUnreadCount(event.num);
            mHomePresenter.saveShoppingCartNum(event.num);
        } else {
            int num = mBottomBar.getItem(3).getUnreadCount();
            mBottomBar.getItem(3).setUnreadCount(num + event.num);
            mHomePresenter.saveShoppingCartNum(num + event.num);
        }
    }


    @Subscribe
    public void onTabSelectedEvent(TabSelectedEvent event) {
        mBottomBar.setCurrentItem(event.position);
    }

    /**
     * start other BrotherFragment
     */
    @Subscribe
    public void startBrother(StartBrotherEvent event) {
        start(event.targetFragment);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 0)
    public void onReceiveMessage(@NonNull BackToFirstFragmentEvent event) {
        mBottomBar.setCurrentItem(0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterEventBus();
    }

    @Override
    public Context getMainContext() {
        return _mActivity;
    }
}
