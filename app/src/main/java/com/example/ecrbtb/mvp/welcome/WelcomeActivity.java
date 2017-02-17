package com.example.ecrbtb.mvp.welcome;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecrbtb.BaseActivity;
import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.MainActivity;
import com.example.ecrbtb.R;

/**
 * Created by boby on 2017/1/10.
 */

public class WelcomeActivity extends BaseActivity {

    private ImageView mIvWelcome;

    private TextView mTvTime;

    private boolean mStart = true;

    private static String SAVE_START_STATE = "state";

    @Override
    protected void initView(ViewDataBinding bind) {

        mIvWelcome = (ImageView) findViewById(R.id.iv_welcome);
        mTvTime = (TextView) findViewById(R.id.tv_time);

        delay(3);
    }

    @Override
    protected void initInstanceState(Bundle savedInstanceState) {
        super.initInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mStart = savedInstanceState.getBoolean(SAVE_START_STATE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVE_START_STATE, false);
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_welcome;
    }

    @Override
    protected int getRootView() {
        return 0;
    }


    public void delay(final int timer) {
        if (timer == 0) {
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
            return;
        } else {
            mTvTime.setText(timer + "秒  跳过");
            mIvWelcome.postDelayed(new Runnable() {
                @Override
                public void run() {
                    delay(timer - 1);
                }
            }, 1000);
        }
    }
}
