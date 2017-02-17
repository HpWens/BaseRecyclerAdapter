package com.example.ecrbtb.mvp.shopping.presenter;

import android.os.Handler;
import android.os.Looper;

import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.MyApplication;
import com.example.ecrbtb.listener.MyResponseListener;
import com.example.ecrbtb.mvp.category.bean.Product;
import com.example.ecrbtb.mvp.goods.bean.Goods;
import com.example.ecrbtb.mvp.goods.biz.GoodsBiz;
import com.example.ecrbtb.mvp.shopping.biz.ShoppingBiz;
import com.example.ecrbtb.mvp.shopping.view.IShoppingView;
import com.example.ecrbtb.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by boby on 2016/12/22.
 */

public class ShoppingPresenter implements BasePresenter {

    private IShoppingView mIShoppingView;

    private ShoppingBiz mShoppingBiz;

    private GoodsBiz mGoodsBiz;

    private Handler mHandler;

    public ShoppingPresenter(IShoppingView IShoppingView) {
        mIShoppingView = IShoppingView;
        mShoppingBiz = ShoppingBiz.getInstance(mIShoppingView.getShoppingContext());
        mGoodsBiz = GoodsBiz.getInstance(mIShoppingView.getShoppingContext());
    }

    /**
     * 获取 shopping 数据
     */
    public void getShoppingData() {

        Schedulers.io().createWorker().schedule(new Action0() {
            @Override
            public void call() {
                final List<Product> resultList = new ArrayList<>();

                List<Product> totalProductList = mShoppingBiz.getShoppingCartData();

                if (totalProductList == null || totalProductList.isEmpty()) {
                    mHandler = new Handler(Looper.getMainLooper());
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mIShoppingView.showEmptyPage();
                        }
                    });
                    return;
                }

                for (Product totalProduct : totalProductList) {

                    //默认为货品
                    if (totalProduct.IsSingle == null) {
                        totalProduct.IsSingle = "0";
                    }

                    //有货品
                    if (totalProduct.IsSingle.equals("0")) {
                        List<Goods> goodsList = mShoppingBiz.getGoodsByNum(totalProduct.SupplierId, totalProduct.ProductId);
                        if (goodsList == null || goodsList.isEmpty()) {
                            continue;
                        }

                        for (Goods goods : goodsList) {
                            Product product = new Product();

                            product.DefaultPic = totalProduct.DefaultPic;
                            product.ProductName = goods.ProductName + "(" + goods.SpecValue + ")";
                            product.ProductNum = goods.GoodsNumber;
                            product.GoodsId = goods.Id;
                            product.AddCartTime = goods.AddCartTime;
                            product.ProductId = goods.ProductId;
                            product.SupplierId = goods.SupplierId;
                            product.SupplierName = totalProduct.SupplierName;
                            product.IsDeduction = totalProduct.IsDeduction;
                            product.SaleMode = goods.SaleMode;
                            product.SalesPrice = goods.SalesPrice;
                            product.SalesIntegral = goods.SalesIntegral;
                            product.IsSingle = totalProduct.IsSingle;
                            product.Deductrate = goods.Deductrate;

                            resultList.add(product);
                        }

                    } else { //单品
                        resultList.add(totalProduct);
                    }
                }

                Collections.sort(resultList, new Comparator<Product>() {
                    @Override
                    public int compare(Product o1, Product o2) {
                        return o1.AddCartTime < o2.AddCartTime ? 1 : -1;
                    }
                });

                AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                    @Override
                    public void call() {
                        mIShoppingView.showNormalPage();
                        mIShoppingView.getShoppingData(resultList);
                    }
                });
            }
        });

    }


    /**
     * 把商品从购物车删除
     *
     * @param supplierId
     * @param productId
     */
    public void deleteProduct(int supplierId, int productId) {
        mShoppingBiz.deleteProduct(supplierId, productId);
    }

    public void deleteProduct(int supplierId, int productId, int productNum) {
        mShoppingBiz.deleteProduct(supplierId, productId, productNum);
    }

    /**
     * 删除货品数据
     *
     * @param supplierId
     * @param goodsId
     */
    public void deleteGoods(int supplierId, int goodsId, int goodsNum) {
        mShoppingBiz.deleteGoods(supplierId, goodsId, goodsNum);
    }


    /**
     * @param productList
     * @param buyType
     */
    public void commitSettlement(List<Product> productList, String buyType) {

        if (!MyApplication.getInstance().isConnected()) {
            mIShoppingView.showNetErrorToast();
            return;
        } else {
            mIShoppingView.showSweetDialog();
        }

        HashMap<String, String> hm = new HashMap<>();
        hm.put("StoreId", mShoppingBiz.getStoreId() + "");
        hm.put("ManagerId", mShoppingBiz.getManagerId() + "");
        hm.put("productInfo", "" + StringUtils.getBase64String(StringUtils.getURLEncoderString(mShoppingBiz.getSettlementData(productList, buyType))).replaceAll("\n", ""));

        mShoppingBiz.commitSettlement(hm, new MyResponseListener() {
            @Override
            public void onResponse(final String json) {
                AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                    @Override
                    public void call() {
                        mIShoppingView.startOrderActivity(json);
                    }
                });
            }

            @Override
            public void onError(final String error) {
                AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                    @Override
                    public void call() {
                        mIShoppingView.showErrorToast(error);
                    }
                });
            }
        });

    }


}
