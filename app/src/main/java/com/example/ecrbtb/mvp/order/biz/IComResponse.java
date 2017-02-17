package com.example.ecrbtb.mvp.order.biz;

import java.util.List;

/**
 * Created by boby on 2017/1/6.
 */

public interface IComResponse<T> {

    public void getResponseData(List<T> datas);

    public void onError(String error);

    public void onComplete(String comp);
}
