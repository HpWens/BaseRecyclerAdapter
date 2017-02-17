package com.example.ecrbtb.mvp.home.biz;

import android.content.Context;

import com.example.ecrbtb.BaseBiz;
import com.example.ecrbtb.config.Constants;

/**
 * Created by boby on 2016/12/20.
 */

public class MainBiz extends BaseBiz {

    public MainBiz(Context context) {
        super(context);
    }

    private static class SingletonHolder {
        private static MainBiz INSTANCE = new MainBiz(mContext);
    }

    public static MainBiz getInstance(Context ctx) {
        if (ctx != null) {
            mContext = ctx;
        }
        return MainBiz.SingletonHolder.INSTANCE;
    }

    /**
     * 保存购物车数量
     */
    public void saveShoppingCartNum(int num) {
        prefer.edit().putInt(Constants.SHOPPING_CART_NUM, num).commit();
    }

    public int getShoppingCartNum() {
        return prefer.getInt(Constants.SHOPPING_CART_NUM, 0);
    }

}
