package com.example.ecrbtb.mvp.quick_order.view;

import android.content.Context;

import com.example.ecrbtb.mvp.category.bean.Product;

import java.util.List;

/**
 * Created by boby on 2017/1/10.
 */

public interface ICommodityView {

    public Context getCommodityContext();

    public void getCommodityData(List<Product> productList);

    public void showLoadingPage();

    public void showNetErrorPage();
}
