package com.example.ecrbtb.mvp.login.view;

import com.example.ecrbtb.mvp.login.bean.LoginState;

/**
 * Created by boby on 2016/12/13
 */

public interface IUserLoginView {

    public String getUserName();

    public String getPassword();

    public String getStoreCode();

    public void showPageLoad();

    public void showPageNormal();

    public void loginSuccess(LoginState loginState);

    public void loginFailed();

    public void showPageError();

    public void showPasswordFormatError();

}