package com.example.ecrbtb.mvp.login.biz;


import com.example.ecrbtb.mvp.login.bean.LoginState;

/**
 * Created by boby on 2016/12/6.
 */

public interface OnLoginListener {

    public void loginSuccess(LoginState loginState);

    public void loginFailed();

}
