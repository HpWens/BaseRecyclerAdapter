package com.example.ecrbtb.mvp.detail.biz;

/**
 * Created by boby on 2016/12/27.
 */

public interface IResponse {

    void onSuccess();

    void onFailed();

    void getPhoneUrls(String urls);
}
