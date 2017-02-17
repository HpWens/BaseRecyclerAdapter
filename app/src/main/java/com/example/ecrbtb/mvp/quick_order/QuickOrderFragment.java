package com.example.ecrbtb.mvp.quick_order;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.R;
import com.example.ecrbtb.BaseMainFragment;

/**
 * Created by boby on 2016/12/14.
 */

public class QuickOrderFragment extends BaseMainFragment {

    public static QuickOrderFragment newInstance() {

        Bundle args = new Bundle();

        QuickOrderFragment fragment = new QuickOrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getResourceId() {
        return R.layout.fragment_shopping;
    }


    @Override
    protected void initInstanceState(Bundle savedInstanceState) {
        super.initInstanceState(savedInstanceState);
        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_container, FirstQuickOrderFragment.newInstance());
        }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

    }

}
