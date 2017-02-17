package com.example.ecrbtb.mvp.login.presenter;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.MyApplication;
import com.example.ecrbtb.R;
import com.example.ecrbtb.mvp.login.bean.LoginState;
import com.example.ecrbtb.mvp.login.biz.OnLoginListener;
import com.example.ecrbtb.mvp.login.biz.UserBiz;
import com.example.ecrbtb.mvp.login.view.IUserLoginView;
import com.example.ecrbtb.utils.KeyBoardUtil;
import com.example.ecrbtb.utils.StringUtils;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.animation.ZoomEnter.ZoomInBottomEnter;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by boby on 2016/12/13.
 */

public class UserLoginPresenter implements BasePresenter {

    private IUserLoginView mIUserLoginView;

    private UserBiz mUserBiz;

    private String mUserName;
    private String mPassword;
    private String mStoreCode;

    public UserLoginPresenter(Context context, IUserLoginView IUserLoginView) {
        mIUserLoginView = IUserLoginView;
        mUserBiz = UserBiz.getInstance(context);
    }

    /**
     * 隐藏软键盘
     *
     * @param activity
     * @param editText
     */
    public void hideKeyBoard(final Activity activity, EditText editText) {
        editText.requestFocus();
        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        KeyBoardUtil.getInstance(activity).hide();
                    }
                });
    }

    /**
     * 登录
     */
    public void login(RelativeLayout rootView) {
        mUserName = mIUserLoginView.getUserName();
        mPassword = mIUserLoginView.getPassword();
        mStoreCode = mIUserLoginView.getStoreCode();

        if (!StringUtils.isEmpty(mUserName) && !StringUtils.isEmpty(mPassword) && !StringUtils.isEmpty(mStoreCode)) {
            if (mPassword.length() < 6) {
                mIUserLoginView.showPasswordFormatError();
                return;
            }
            if (!MyApplication.getInstance().isConnected()) {
                mIUserLoginView.showPageError();
                return;
            }

            mUserBiz.login(mStoreCode, mUserName, mPassword, new OnLoginListener() {
                @Override
                public void loginSuccess(LoginState loginState) {
                    mIUserLoginView.loginSuccess(loginState);
                }

                @Override
                public void loginFailed() {
                    mIUserLoginView.loginFailed();
                }
            });

        } else {
            Snackbar.make(rootView, rootView.getContext().getString(R.string.empty), Snackbar.LENGTH_SHORT).show();
        }

    }

    /**
     * 显示 什么是代码  提示框
     */
    public void showWhatCodeDialog(Context context) {
        final MaterialDialog dialog = new MaterialDialog(context);
        dialog//
                .btnNum(1)
                .content(context.getString(R.string.code_alert))//
                .btnText("取消")//
                .showAnim(new ZoomInBottomEnter())//
                .dismissAnim(new SlideBottomExit())//
                .show();

        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.dismiss();
            }
        });

    }

    /**
     * 重新加载界面
     */
    public void retryLoading() {
        if (!MyApplication.getInstance().isConnected()) {
            mIUserLoginView.showPageError();
        } else {
            mIUserLoginView.showPageNormal();
        }
    }
}
