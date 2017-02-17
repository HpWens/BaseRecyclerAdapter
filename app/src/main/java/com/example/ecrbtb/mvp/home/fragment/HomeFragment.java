package com.example.ecrbtb.mvp.home.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.ecrbtb.BaseMainFragment;
import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.R;

/**
 * Created by boby on 2016/12/14.
 */

public class HomeFragment extends BaseMainFragment {


    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getResourceId() {
        return R.layout.fragment_home;
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_container, FirstHomeFragment.newInstance());
        }
    }
}
