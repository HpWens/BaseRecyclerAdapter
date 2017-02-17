package com.example.ecrbtb.mvp.order.biz;

import com.example.ecrbtb.mvp.goods.bean.Goods;

import java.util.List;

/**
 * Created by boby on 2017/1/9.
 */

public interface IOrderResponse {

    public void getResponseData(List<Goods> datas);

    public void onError(String error);

    public void getSuppliers(List<Integer> suppliers);

    public void getPriceAndIntegral(double price, int integral);

    public void getCommitData(String productIds, String productInfo);
}
