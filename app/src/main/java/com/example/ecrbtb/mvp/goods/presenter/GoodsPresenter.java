package com.example.ecrbtb.mvp.goods.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.MyApplication;
import com.example.ecrbtb.config.Constants;
import com.example.ecrbtb.listener.MyResponseListener;
import com.example.ecrbtb.mvp.category.bean.Product;
import com.example.ecrbtb.mvp.detail.biz.DetailBiz;
import com.example.ecrbtb.mvp.goods.bean.Goods;
import com.example.ecrbtb.mvp.goods.biz.GoodsBiz;
import com.example.ecrbtb.mvp.goods.biz.IGoodsBiz;
import com.example.ecrbtb.mvp.goods.view.IGoodsView;
import com.example.ecrbtb.utils.StringUtils;

import java.util.HashMap;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * Created by boby on 2016/12/19.
 */

public class GoodsPresenter implements BasePresenter {


    private Context mContext;

    private IGoodsView mIGoodsView;

    private GoodsBiz mGoodsBiz;

    private Handler mHandler;

    private DetailBiz mDetailBiz;

    public GoodsPresenter(IGoodsView IGoodsView) {
        mIGoodsView = IGoodsView;
        mContext = mIGoodsView.getContext();
        mGoodsBiz = GoodsBiz.getInstance(mContext);
        mDetailBiz=DetailBiz.getInstance(mContext);
    }

    public void requestGoodsData(final Product product) {

        if (MyApplication.getInstance().isConnected()) {
            mIGoodsView.showLoadPage();
        } else {
            mIGoodsView.showErrorPage();
        }

        HashMap<String, String> hm = new HashMap<>();

        hm.put("pid", product.ProductId + "");
        hm.put("gid", product.ProductGoodsId + "");
        hm.put("isTransfer", "true");
        hm.put("isShelved", "true");
        hm.put("FK_Id", product.SupplierId + "");
        hm.put("FK_Flag", 2 + "");
        hm.put("Token", mGoodsBiz.getToken());

        mGoodsBiz.questGoodsData(hm, new IGoodsBiz() {
            @Override
            public void responseCode(int code) {
                switch (code) {
                    case Constants.REQUEST_FAILED_CODE:
                        mHandler = new Handler(Looper.getMainLooper());
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mIGoodsView.showServerError();
                            }
                        });
                        break;
                    case Constants.REQUEST_SUCCESS_CODE:
                        mHandler = new Handler(Looper.getMainLooper());
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mIGoodsView.getGoodsData(mGoodsBiz.getGoodsData(product.ProductId, product.SupplierId));
                            }
                        });
                        break;
                }
            }
        });
    }


    /**
     * 加入购物车
     *
     * @param goodsList
     */
    public void joinShoppingCart(int productId, int supplierId, List<Goods> goodsList) {
        if (goodsList == null || goodsList.isEmpty()) {
            mIGoodsView.showEmptyData();
            return;
        }
        int num = 0;
        for (Goods goods : goodsList) {
            num += goods.GoodsNumber;
            mGoodsBiz.updateGoodsNumById(goods.Id, goods.SupplierId, goods.GoodsNumber);
        }
        //保存商品数量
        mGoodsBiz.saveProductNum(productId, supplierId, num);

        mIGoodsView.addShoppingCart(num);
    }


    /**
     * 清理 handler
     */
    public void clearHandler() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }



    /**
     * 提交订数据
     */
    public void commitOrderData(List<Goods> goodsList, String buyType) {

        if (!MyApplication.getInstance().isConnected()) {
            mIGoodsView.showNetErrorToast();
            return;
        } else {
            mIGoodsView.showCommitDataLoad();
        }

        HashMap<String, String> hm = new HashMap<>();
        hm.put("StoreId", mDetailBiz.getStoreId() + "");
        hm.put("ManagerId", mDetailBiz.getManagerId() + "");
        hm.put("productInfo", "" + StringUtils.getBase64String(StringUtils.getURLEncoderString(mDetailBiz.getCommitOrderData(goodsList, buyType))).replaceAll("\n", ""));

        mDetailBiz.commitOrderData(hm, new MyResponseListener() {
            @Override
            public void onResponse(final String json) {
                AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                    @Override
                    public void call() {
                        mIGoodsView.startOrderActivity(json);
                    }
                });
            }

            @Override
            public void onError(final String error) {
                AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                    @Override
                    public void call() {
                        mIGoodsView.showResponseError(error);
                    }
                });
            }
        });
    }


}
