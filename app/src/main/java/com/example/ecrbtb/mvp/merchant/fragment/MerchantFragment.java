package com.example.ecrbtb.mvp.merchant.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.ecrbtb.BaseMainFragment;
import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.R;

/**
 * Created by boby on 2016/12/14.
 */

public class MerchantFragment extends BaseMainFragment {

    public static MerchantFragment newInstance() {

        Bundle args = new Bundle();

        MerchantFragment fragment = new MerchantFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getResourceId() {
        return R.layout.fragment_merchant;
    }


    @Override
    protected void initInstanceState(Bundle savedInstanceState) {
        super.initInstanceState(savedInstanceState);
        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_container, FirstMerchantFragment.newInstance());
        }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
    }

}
