package com.example.ecrbtb.mvp.merchant.biz;

import android.content.Context;

import com.example.ecrbtb.BaseBiz;

/**
 * Created by boby on 2016/12/30.
 */

public class MerchantBiz extends BaseBiz {

    public MerchantBiz(Context context) {
        super(context);
    }

    private static class SingletonHolder {
        private static MerchantBiz INSTANCE = new MerchantBiz(mContext);
    }

    public static MerchantBiz getInstance(Context ctx) {
        if (ctx != null) {
            mContext = ctx.getApplicationContext();
        }
        return MerchantBiz.SingletonHolder.INSTANCE;
    }

}
