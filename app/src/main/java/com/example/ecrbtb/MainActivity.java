package com.example.ecrbtb;

import android.Manifest;
import android.databinding.ViewDataBinding;
import android.os.Bundle;

import com.example.ecrbtb.config.Constants;
import com.example.ecrbtb.event.BackToFirstFragmentEvent;
import com.example.ecrbtb.event.RequestPermissionEvent;
import com.example.ecrbtb.event.ResponsePermissionEvent;
import com.example.ecrbtb.mvp.home.MainFragment;
import com.grasp.tint.SystemBarTintManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import me.yokeyword.fragmentation.anim.DefaultVerticalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class MainActivity extends PermissionsActivity implements BaseMainFragment.OnBackToFirstListener {

    protected String[] filePermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private int mPermissionType = -1;

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected int getRootView() {
        return 0;
    }

    @Override
    protected void initView(ViewDataBinding bind) {

    }

    @Override
    protected void initBarTint() {
        super.initBarTint();
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.colorPrimary);
    }

    @Override
    public void requestPermissionResult(boolean allowPermission) {
        if (allowPermission) {
            switch (mPermissionType){
                case Constants.FILE_PERMISSION:
                    EventBus.getDefault().post(new ResponsePermissionEvent(Constants.FILE_PERMISSION));
                    break;
            }
        }
    }

    @Override
    protected void initInstanceState(Bundle savedInstanceState) {
        super.initInstanceState(savedInstanceState);
        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_container, MainFragment.newInstance());
        }
    }


    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultVerticalAnimator();
    }


    @Override
    public void onBackToFirstFragment() {
        EventBus.getDefault().post(new BackToFirstFragmentEvent());
    }

    @Subscribe
    public void requestPermission(RequestPermissionEvent event) {
        switch (event.type) {
            case Constants.FILE_PERMISSION:
                mPermissionType = Constants.FILE_PERMISSION;
                if (!mayRequestPermission(filePermissions)) {
                    return;
                }
                EventBus.getDefault().post(new ResponsePermissionEvent(Constants.FILE_PERMISSION));
                break;

        }
    }
}
