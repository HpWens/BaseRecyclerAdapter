package com.example.ecrbtb.mvp.quick_order.presenter;

import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.MyApplication;
import com.example.ecrbtb.mvp.category.bean.Product;
import com.example.ecrbtb.mvp.order.biz.IComResponse;
import com.example.ecrbtb.mvp.quick_order.biz.CollectionBiz;
import com.example.ecrbtb.mvp.quick_order.view.ICommodityView;

import java.util.HashMap;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * Created by boby on 2017/1/10.
 */

public class CommodityPresenter implements BasePresenter {

    private ICommodityView mICommodityView;

    private CollectionBiz mCollectionBiz;

    public CommodityPresenter(ICommodityView iCommodityView) {
        mICommodityView = iCommodityView;
        mCollectionBiz = CollectionBiz.getInstance(mICommodityView.getCommodityContext());
    }

    /**
     * 请求数据
     *
     * @param pageIndex
     */
    public void requestData(int pageIndex) {

        if (MyApplication.getInstance().isConnected()) {
            mICommodityView.showLoadingPage();
        } else {
            mICommodityView.showNetErrorPage();
        }

        HashMap<String, String> hm = new HashMap<>();

        hm.put("FK_Flag", "1");
        hm.put("FK_Id", "" + mCollectionBiz.getStoreId());
        hm.put("PageIndex", pageIndex + "");
        hm.put("PageSize", "12");

        mCollectionBiz.requestCommodityData(hm, new IComResponse<Product>() {
            @Override
            public void getResponseData(final List<Product> datas) {
                AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                    @Override
                    public void call() {
                        mICommodityView.getCommodityData(datas);
                    }
                });
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onComplete(String comp) {

            }
        });

    }
}
