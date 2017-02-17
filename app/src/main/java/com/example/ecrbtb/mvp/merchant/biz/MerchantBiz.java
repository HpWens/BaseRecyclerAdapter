package com.example.ecrbtb.mvp.merchant.biz;

import android.content.Context;

import com.example.ecrbtb.BaseBiz;
import com.example.ecrbtb.mvp.login.bean.Manager;
import com.example.ecrbtb.mvp.login.bean.Store;

import org.xutils.ex.DbException;

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

    public Manager getManagerById(int managerId) {
        try {
            Manager manager = db.selector(Manager.class).where("ManagerId", "=", managerId).findFirst();
            return manager;
        } catch (DbException localDbException) {
            localDbException.printStackTrace();
        }
        return null;
    }

    public Store getStoreById(int storeId) {
        try {
            Store store = db.selector(Store.class).where("StoreId", "=", storeId).findFirst();
            return store;
        } catch (DbException localDbException) {
            localDbException.printStackTrace();
        }
        return null;
    }


}
