package com.example.ecrbtb.mvp.shopping.view;

import android.content.Context;

import com.example.ecrbtb.mvp.category.bean.Product;

import java.util.List;

/**
 * Created by boby on 2016/12/22.
 */

public interface IShoppingView {

    public void showLoadPage();

    public void showErrorPage();

    public void showEmptyPage();

    public void showNormalPage();

    public Context getShoppingContext();

    public void getShoppingData(List<Product> lists);

    public void showNetErrorToast();

    public void showSweetDialog();

    public void showErrorToast(String error);

    public void startOrderActivity(String json);

}
