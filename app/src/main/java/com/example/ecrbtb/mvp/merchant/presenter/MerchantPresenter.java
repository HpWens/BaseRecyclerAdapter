package com.example.ecrbtb.mvp.merchant.presenter;

import android.content.Context;
import android.util.TypedValue;

import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.mvp.login.bean.Manager;
import com.example.ecrbtb.mvp.login.bean.Store;
import com.example.ecrbtb.mvp.merchant.biz.MerchantBiz;

/**
 * Created by boby on 2016/12/30.
 */

public class MerchantPresenter implements BasePresenter {

    private MerchantBiz mMerchantBiz;

    public MerchantPresenter(Context context) {
        mMerchantBiz = MerchantBiz.getInstance(context);
    }

    public int getStoreId() {
        return mMerchantBiz.getStoreId();
    }

    public String getToken() {
        return mMerchantBiz.getToken();
    }

    public int dip2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public Manager getManagerById() {
        return this.mMerchantBiz.getManagerById(mMerchantBiz.getManagerId());
    }

    public Store getStoreById() {
        return this.mMerchantBiz.getStoreById(mMerchantBiz.getStoreId());
    }

    public boolean isLogin() {
        return this.mMerchantBiz.isLogin();
    }

}
