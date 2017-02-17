package com.example.ecrbtb.mvp.goods.view;

import android.content.Context;

import com.example.ecrbtb.mvp.goods.bean.Goods;

import java.util.List;

/**
 * Created by boby on 2016/12/19.
 */

public interface IGoodsView {

    public void showLoadPage();

    public void showErrorPage();

    public void showEmptyPage();

    public void showNormalPage();

    public Context getContext();

    public void showServerError();

    public void getGoodsData(List<Goods> goodsList);

    public void showEmptyData();

    public void showEmptyNum();

    public void addShoppingCart(int num);

    //提交订单
    public void showNetErrorToast();

    public void showCommitDataLoad();

    public void startOrderActivity(String json);

    public void showResponseError(String error);

}
