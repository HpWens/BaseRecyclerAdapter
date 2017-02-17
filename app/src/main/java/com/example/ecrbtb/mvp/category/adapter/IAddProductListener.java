package com.example.ecrbtb.mvp.category.adapter;

import android.view.View;

import com.example.ecrbtb.mvp.category.bean.Product;

/**
 * Created by boby on 2016/12/17.
 */

public interface IAddProductListener {

    void startLogin();

    void addShoppingCart();

    void subShoppingCart();

    void startGoodsPage(int position, Product product);

    void startDetailActivity(View view0, View view1, Product product);

}
