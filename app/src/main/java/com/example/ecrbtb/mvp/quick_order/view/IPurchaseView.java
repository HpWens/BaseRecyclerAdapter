package com.example.ecrbtb.mvp.quick_order.view;

import android.content.Context;

import com.example.ecrbtb.mvp.quick_order.bean.Purchase;

import java.util.List;

/**
 * Created by boby on 2017/1/11.
 */

public interface IPurchaseView {

    public Context getPurchaseContext();

    public void getPurchaseData(List<Purchase> purchaseList, int currentPage);

    public void showLoadingPage();

    public void showNetErrorPage();

    public void showError(String error);

}
