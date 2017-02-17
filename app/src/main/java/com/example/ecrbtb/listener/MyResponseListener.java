package com.example.ecrbtb.listener;

/**
 * Created by boby on 2017/1/6.
 */

public interface MyResponseListener {
    void onResponse(String json);

    void onError(String error);
}
