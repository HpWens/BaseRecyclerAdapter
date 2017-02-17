package com.example.ecrbtb.mvp.order.presenter;

import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.MyApplication;
import com.example.ecrbtb.listener.MyResponseListener;
import com.example.ecrbtb.mvp.goods.bean.Goods;
import com.example.ecrbtb.mvp.order.bean.Address;
import com.example.ecrbtb.mvp.order.bean.OrderData;
import com.example.ecrbtb.mvp.order.biz.IComResponse;
import com.example.ecrbtb.mvp.order.biz.IOrderResponse;
import com.example.ecrbtb.mvp.order.biz.OrderBiz;
import com.example.ecrbtb.mvp.order.view.IOrderView;

import java.util.HashMap;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by boby on 2017/1/6.
 */

public class OrderPresenter implements BasePresenter {

    private IOrderView mIOrderView;

    private OrderBiz mOrderBiz;

    public OrderPresenter(IOrderView IOrderView) {
        mIOrderView = IOrderView;
        mOrderBiz = OrderBiz.getInstance(mIOrderView.getOrderContext());
    }

    /**
     * @param json
     */
    public void handlerOrderData(final String json) {
        Schedulers.io().createWorker().schedule(new Action0() {
            @Override
            public void call() {
                mOrderBiz.handlerOrderData(json, new IOrderResponse() {
                    @Override
                    public void getResponseData(final List<Goods> datas) {
                        AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                            @Override
                            public void call() {
                                mIOrderView.showOrderListData(datas);
                            }
                        });
                    }

                    @Override
                    public void onError(String error) {

                    }

                    @Override
                    public void getSuppliers(final List<Integer> suppliers) {
                        AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                            @Override
                            public void call() {
                                mIOrderView.getSuppliers(suppliers);
                            }
                        });
                    }

                    @Override
                    public void getPriceAndIntegral(final double price, final int integral) {
                        AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                            @Override
                            public void call() {
                                mIOrderView.getPriceAndIntegral(price, integral);
                            }
                        });
                    }

                    @Override
                    public void getCommitData(final String productIds, final String productInfo) {
                        AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                            @Override
                            public void call() {
                                mIOrderView.getProductIdsAndInfo(productIds, productInfo);
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * 获取地址列表数据
     */
    public void getAddressListData(final boolean isShow) {
        mOrderBiz.getAddressListData(new IComResponse<Address>() {
            @Override
            public void getResponseData(final List<Address> datas) {
                AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                    @Override
                    public void call() {
                        mIOrderView.getAddressListData(datas, isShow);
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
     * 提交新增地址
     *
     * @param address
     */
    public void commitAddAddress(Address address) {

        if (!MyApplication.getInstance().isConnected()) {
            mIOrderView.showNetError();
            return;
        }

        mOrderBiz.commitAddAddressData(address, new MyResponseListener() {
            @Override
            public void onResponse(String json) {
                mIOrderView.onSuccessAddAddress(json);
            }

            @Override
            public void onError(String error) {
                mIOrderView.showResponseError(error);
            }
        });

    }


    /**
     * 获取运费
     *
     * @param supplierId
     * @param addressId
     * @param weight
     */
    public void getFreightData(final int supplierId, int addressId, double weight) {

        if (!MyApplication.getInstance().isConnected()) {
            mIOrderView.showNetError();
            return;
        }

        HashMap<String, String> hm = new HashMap<>();

        hm.put("SupplierId", supplierId + "");
        hm.put("Weight", weight + "");
        hm.put("AddressId", addressId + "");

        mOrderBiz.requestFreightData(hm, new IComResponse() {
            @Override
            public void getResponseData(List datas) {

            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onComplete(final String comp) {
                AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                    @Override
                    public void call() {
                        mIOrderView.getFreightData(supplierId, comp);
                    }
                });
            }
        });
    }


    /**
     * 提交订单数据
     */
    public void commitOrderData(OrderData orderData) {

        if (MyApplication.getInstance().isConnected()) {
            mIOrderView.showSweetDialog();
        } else {
            mIOrderView.showNetError();
            return;
        }


        HashMap<String, String> hm = new HashMap<>();

        hm.put("token", mOrderBiz.getToken());
        hm.put("ManagerId", mOrderBiz.getManagerId() + "");
        hm.put("StoreId", mOrderBiz.getStoreId() + "");
        hm.put("IsClerk", "0");
        hm.put("order", mOrderBiz.composeOrderData(orderData));

        mOrderBiz.commitOrderData(hm, orderData.PayType, new IComResponse() {
            @Override
            public void getResponseData(List datas) {

            }

            @Override
            public void onError(final String error) {
                AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                    @Override
                    public void call() {
                        mIOrderView.showCommitError(error);
                    }
                });
            }

            @Override
            public void onComplete(final String comp) {
                AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                    @Override
                    public void call() {
                        mIOrderView.showCommitSuccess(comp);
                    }
                });
            }
        });

    }


}
