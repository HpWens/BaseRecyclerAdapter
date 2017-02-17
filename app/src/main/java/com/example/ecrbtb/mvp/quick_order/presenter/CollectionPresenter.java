package com.example.ecrbtb.mvp.quick_order.presenter;

import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.MyApplication;
import com.example.ecrbtb.mvp.category.bean.Product;
import com.example.ecrbtb.mvp.order.biz.IComResponse;
import com.example.ecrbtb.mvp.quick_order.biz.CollectionBiz;
import com.example.ecrbtb.mvp.quick_order.view.ICollectionView;

import java.util.HashMap;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * Created by boby on 2017/1/10.
 */

public class CollectionPresenter implements BasePresenter {

    private ICollectionView mICollectionView;

    private CollectionBiz mCollectionBiz;

    public CollectionPresenter(ICollectionView ICollectionView) {
        mICollectionView = ICollectionView;
        mCollectionBiz = CollectionBiz.getInstance(mICollectionView.getQuickContext());
    }

    /**
     * 请求数据
     *
     * @param pageIndex
     */
    public void requestData(final int pageIndex, int pageState) {

        if (pageIndex == 1) {
            if (MyApplication.getInstance().isConnected()) {
                mICollectionView.showLoadingPage();
            } else {
                mICollectionView.showNetErrorPage();
                return;
            }
        }

        HashMap<String, String> hm = new HashMap<>();

        hm.put("FK_Flag", "1");
        hm.put("FK_Id", "" + mCollectionBiz.getStoreId());
        hm.put("PageIndex", pageIndex + "");
        hm.put("PageSize", "12");

        mCollectionBiz.requestCollectionData(pageState, hm, new IComResponse<Product>() {
            @Override
            public void getResponseData(final List<Product> datas) {
                AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                    @Override
                    public void call() {
                        mICollectionView.getCollectionData(datas, pageIndex);
                    }
                });
            }

            @Override
            public void onError(final String error) {
                AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                    @Override
                    public void call() {
                        mICollectionView.showError(error);
                    }
                });
            }

            @Override
            public void onComplete(String comp) {

            }
        });

    }
}
