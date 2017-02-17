package com.example.ecrbtb.mvp.home.presenter;


import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.mvp.home.biz.MainBiz;
import com.example.ecrbtb.mvp.home.view.IHomeView;

/**
 * Created by boby on 2016/12/14
 */

public class HomePresenter implements BasePresenter {

    private IHomeView mIHomeView;
    private MainBiz mMainBiz;

    public HomePresenter(IHomeView IHomeView) {
        mIHomeView = IHomeView;
        mMainBiz = MainBiz.getInstance(mIHomeView.getMainContext());
    }

    /**
     * @param num
     */
    public void saveShoppingCartNum(int num) {
        if (num >= 0) {
            mMainBiz.saveShoppingCartNum(num);
        }
    }

    public int getShoppingCartNum() {
        return mMainBiz.getShoppingCartNum();
    }

    public boolean isLogin() {
        return mMainBiz.isLogin();
    }

    /**
     * @return
     */
    public String getToken() {
        return mMainBiz.getToken();
    }

}