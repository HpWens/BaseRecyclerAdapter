package com.example.ecrbtb.mvp.merchant.adpter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.ecrbtb.mvp.merchant.fragment.MerchantContentFragment;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by boby on 2016/12/29.
 */

public class MerchantPageAdapter extends FragmentPagerAdapter {

    private String[] mTab = new String[]{"销售管理", "采购管理"};

    private SupportFragment[] mFragments = new SupportFragment[2];

    public MerchantPageAdapter(FragmentManager fm) {
        super(fm);

        mFragments[0] = MerchantContentFragment.newInstance(MerchantContentFragment.SALE_TYPE);
        mFragments[1] = MerchantContentFragment.newInstance(MerchantContentFragment.MANAGER_TYPE);
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
