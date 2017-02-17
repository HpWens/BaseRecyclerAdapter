package com.example.ecrbtb.mvp.login.biz;

/**
 * Created by boby on 2016/12/6.
 */

public interface IUserBiz {
    public void login(String storeCode, String username, String password, OnLoginListener loginListener);
}
