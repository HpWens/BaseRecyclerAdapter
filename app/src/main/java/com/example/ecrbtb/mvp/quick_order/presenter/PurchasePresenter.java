package com.example.ecrbtb.mvp.quick_order.presenter;

import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.MyApplication;
import com.example.ecrbtb.mvp.order.biz.IComResponse;
import com.example.ecrbtb.mvp.quick_order.bean.Purchase;
import com.example.ecrbtb.mvp.quick_order.biz.CollectionBiz;
import com.example.ecrbtb.mvp.quick_order.view.IPurchaseView;

import java.util.HashMap;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * Created by boby on 2017/1/11.
 */

public class PurchasePresenter implements BasePresenter {

    private IPurchaseView mIPurchaseView;
    private CollectionBiz mCollectionBiz;

    public PurchasePresenter(IPurchaseView IPurchaseView) {
        mIPurchaseView = IPurchaseView;
        mCollectionBiz = CollectionBiz.getInstance(mIPurchaseView.getPurchaseContext());
    }

    /**
     * 请求数据
     *
     * @param pageIndex
     */
    public void requestData(final int pageIndex) {

        if (pageIndex == 1) {
            if (MyApplication.getInstance().isConnected()) {
                mIPurchaseView.showLoadingPage();
            } else {
                mIPurchaseView.showNetErrorPage();
                return;
            }
        }

        HashMap<String, String> hm = new HashMap<>();

        hm.put("FK_Flag", "1");
        hm.put("FK_Id", "" + mCollectionBiz.getStoreId());
        hm.put("PageIndex", pageIndex + "");
        hm.put("PageSize", "12");
        hm.put("Condition", "o.Status=7");

        mCollectionBiz.requestPurchaseData(hm, new IComResponse<Purchase>() {
            @Override
            public void getResponseData(final List<Purchase> datas) {
                AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                    @Override
                    public void call() {
                        mIPurchaseView.getPurchaseData(datas, pageIndex);
                    }
                });
            }

            @Override
            public void onError(final String error) {
                AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                    @Override
                    public void call() {
                        mIPurchaseView.showError(error);
                    }
                });
            }

            @Override
            public void onComplete(String comp) {

            }
        });

    }


    /**
     * 再一次购买
     *
     * @param purchaseList
     */
    public void payAgain(List<Purchase> purchaseList) {

    }

}
