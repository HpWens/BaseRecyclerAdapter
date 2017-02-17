package com.example.ecrbtb.mvp.detail.presenter;

import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.mvp.detail.bean.ProductParams;
import com.example.ecrbtb.mvp.detail.biz.ParameterBiz;
import com.example.ecrbtb.mvp.detail.view.IParameterView;
import com.example.ecrbtb.mvp.order.biz.IComResponse;

import java.util.HashMap;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * Created by boby on 2016/12/28.
 */

public class ParameterPresenter implements BasePresenter {

    private IParameterView mIParameterView;

    private ParameterBiz mParameterBiz;

    public ParameterPresenter(IParameterView IParameterView) {
        mIParameterView = IParameterView;
        mParameterBiz = ParameterBiz.getInstance(mIParameterView.getParameterContext());
    }

    /**
     * 获取商品参数数据
     */
    public void requestParasData(int productId, int supplierId) {
        HashMap<String, String> hm = new HashMap<>();

        hm.put("pid", productId + "");
        hm.put("FK_Id", supplierId + "");
        hm.put("FK_Flag", 2 + "");

        mParameterBiz.requestParamData(hm, new IComResponse<ProductParams>() {
            @Override
            public void getResponseData(final List<ProductParams> datas) {
                AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                    @Override
                    public void call() {
                        mIParameterView.getParamsData(datas);
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

    /**
     * @return
     */
    public List<String> getParameterData(int productId, int supplierId) {
        return mParameterBiz.getParameterData(productId, supplierId);
    }
}
