package com.example.ecrbtb.mvp.order.view;

import android.content.Context;

import com.example.ecrbtb.mvp.goods.bean.Goods;
import com.example.ecrbtb.mvp.order.bean.Address;

import java.util.List;

/**
 * Created by boby on 2017/1/6.
 */

public interface IOrderView {

    public Context getOrderContext();

    public void getAddressListData(List<Address> datas, boolean isShow);

    public void showNetError();

    public void showSweetDialog();

    public void showResponseError(String error);

    public void onSuccessAddAddress(String success);

    public void showOrderListData(List<Goods> goodsList);

    public void getSuppliers(List<Integer> suppliers);

    public void getFreightData(int supplierId, String freight);

    public void getPriceAndIntegral(double price, int integral);

    public void getProductIdsAndInfo(String productIds, String productInfo);


    //提交订单返回结果
    public void showCommitError(String error);

    public void showCommitSuccess(String success);

}
