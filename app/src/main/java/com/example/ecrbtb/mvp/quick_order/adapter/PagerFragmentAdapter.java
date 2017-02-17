package com.example.ecrbtb.mvp.quick_order.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.ecrbtb.mvp.quick_order.CollectionFragment;
import com.example.ecrbtb.mvp.quick_order.LongPurchaseFragment;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by boby on 2016/12/15.
 */

public class PagerFragmentAdapter extends FragmentPagerAdapter {

    private String[] mTab = new String[]{"收藏商品", "常购商品", "常购订单"};

    private SupportFragment[] mFragments = new SupportFragment[3];

    private static final int COLLECTION = 0;
    private static final int COMMODITY = 1;
    private static final int ORDER = 2;


    public PagerFragmentAdapter(FragmentManager fm) {
        super(fm);

        mFragments[0] = CollectionFragment.newInstance(COLLECTION);
        mFragments[1] = CollectionFragment.newInstance(COMMODITY);
        mFragments[2] = LongPurchaseFragment.newInstance();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return mTab.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTab[position];
    }


}
