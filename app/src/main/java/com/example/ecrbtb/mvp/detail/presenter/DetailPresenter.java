package com.example.ecrbtb.mvp.detail.presenter;

import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.MyApplication;
import com.example.ecrbtb.config.Constants;
import com.example.ecrbtb.listener.MyResponseListener;
import com.example.ecrbtb.mvp.category.bean.Product;
import com.example.ecrbtb.mvp.category.biz.CategoryBiz;
import com.example.ecrbtb.mvp.detail.biz.DetailBiz;
import com.example.ecrbtb.mvp.detail.biz.ILikeResponse;
import com.example.ecrbtb.mvp.detail.biz.IResponse;
import com.example.ecrbtb.mvp.detail.view.IDetailView;
import com.example.ecrbtb.mvp.goods.bean.Goods;
import com.example.ecrbtb.mvp.goods.biz.GoodsBiz;
import com.example.ecrbtb.mvp.goods.biz.IGoodsBiz;
import com.example.ecrbtb.mvp.order.biz.IComResponse;
import com.example.ecrbtb.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * Created by boby on 2016/12/27.
 */

public class DetailPresenter implements BasePresenter {


    private DetailBiz mDetailBiz;
    private GoodsBiz mGoodsBiz;
    private CategoryBiz mCategoryBiz;

    private IDetailView mIDetailView;

    public DetailPresenter(IDetailView IDetailView) {
        mIDetailView = IDetailView;
        mDetailBiz = DetailBiz.getInstance(mIDetailView.getDetailContext());
        mGoodsBiz = GoodsBiz.getInstance(mIDetailView.getDetailContext());
        mCategoryBiz = CategoryBiz.getInstance(mIDetailView.getDetailContext());
    }

    public void requestDetailData(final Product product) {

        if (MyApplication.getInstance().isConnected()) {
            mIDetailView.showLoadPage();
        } else {
            mIDetailView.showNetError();
        }


        product.IsSingle = product.IsSingle == null ? "1" : product.IsSingle;

        if (product.IsSingle.equals("1")) {
            //更新单个商品的货品数量
            mDetailBiz.updateSingleProductGoodsNum(product.ProductId, product.SupplierId, product.ProductNum);
        }

        HashMap<String, String> hm = new HashMap<>();

        hm.put("pid", product.ProductId + "");
        hm.put("gid", "");
        hm.put("fields", "");
        hm.put("isup", "true");
        hm.put("FK_Id", product.SupplierId + "");
        hm.put("FK_Flag", 2 + "");
        hm.put("Token", mDetailBiz.getToken());


        mDetailBiz.requestDetailData(hm, product.ProductId, product.SupplierId, new IResponse() {
            @Override
            public void onSuccess() {
                AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                    @Override
                    public void call() {
                        Product p = mDetailBiz.findProductById(product.ProductId, product.SupplierId);
                        if (p == null)
                            return;
                        mIDetailView.getProductData(p);
                        mIDetailView.getBannerData(getBannerData(p));
                    }
                });
            }

            @Override
            public void onFailed() {
                AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                    @Override
                    public void call() {
                        mIDetailView.showServerError();
                    }
                });
            }

            @Override
            public void getPhoneUrls(final String urls) {
                AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                    @Override
                    public void call() {
                        mIDetailView.getPhotoUrls(urls);
                    }
                });
            }

        });


        mIDetailView.getGoodsData(mGoodsBiz.getGoodsData(product.ProductId, product.SupplierId), true);

    }


    /**
     * @param product
     */
    public void requestGoodsData(final Product product) {

        if (MyApplication.getInstance().isConnected()) {
            mIDetailView.showLoadPage();
        } else {
            mIDetailView.showNetError();
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
                        AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                            @Override
                            public void call() {
                                mIDetailView.showServerError();
                            }
                        });
                        break;
                    case Constants.REQUEST_SUCCESS_CODE:
                        AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                            @Override
                            public void call() {
                                mIDetailView.getGoodsData(mGoodsBiz.getGoodsData(product.ProductId, product.SupplierId), false);
                            }
                        });
                        break;
                }
            }
        });
    }

    /**
     * 获取图片地址
     *
     * @param product
     * @return
     */
    public List<String> getBannerData(Product product) {
        HashMap<String, String> hm = new HashMap<>();
        List<String> lists = new ArrayList<>();
        if (product != null) {
            String pics = product.Pics;
            if (!StringUtils.isEmpty(pics)) {
                String[] p = pics.split(",");
                for (int i = 0; i < p.length; i++) {
                    lists.add(Constants.BASE_URL + p[i]);
                }
            }
        }
        return lists;
    }


    /**
     * @param product
     */
    public void requestLikeData(Product product) {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("topNum", 2 + "");
        hm.put("pid", product.ProductId + "");
        hm.put("storeId", mDetailBiz.getStoreId() + "");
        hm.put("fields", "");
        hm.put("FK_Id", product.SupplierId + "");
        hm.put("FK_Flag", 2 + "");

        mDetailBiz.requestLikeData(hm, product.SupplierId, new ILikeResponse() {
            @Override
            public void getLikeData(final List<Product> productList) {
                AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                    @Override
                    public void call() {
                        mIDetailView.getLikeData(productList);
                    }
                });
            }
        });
    }


    /**
     * @param productId
     * @param supplierId
     * @param productNum
     */
    public void updateProductNumById(int productId, int supplierId, int productNum) {
        mCategoryBiz.updateProductNumById(productId, supplierId, productNum);
    }


    /**
     * @param productId
     * @param supplierId
     * @param goodsList
     */
    public void updateGoodsNumById(int productId, int supplierId, List<Goods> goodsList) {
        if (goodsList == null || goodsList.isEmpty()) {
            mIDetailView.showEmptyData();
            return;
        }
        int num = 0;
        for (Goods goods : goodsList) {
            num += goods.GoodsNumber;
            mGoodsBiz.updateGoodsNumById(goods.Id, goods.SupplierId, goods.GoodsNumber);
        }
        //保存商品数量
        mGoodsBiz.saveProductNum(productId, supplierId, num);

        mIDetailView.updateShoppingCartNum(num);
    }


    /**
     * 提交订数据
     */
    public void commitOrderData(List<Goods> goodsList, String buyType) {

        if (!MyApplication.getInstance().isConnected()) {
            mIDetailView.showNetErrorToast();
            return;
        } else {
            mIDetailView.showCommitDataLoad();
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
                        mIDetailView.startOrderActivity(json);
                    }
                });
            }

            @Override
            public void onError(final String error) {
                AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                    @Override
                    public void call() {
                        mIDetailView.showResponseError(error);
                    }
                });
            }
        });
    }


    //请求收藏取消接口
    public void requestCollection(final boolean isCollection, int supplierId, int productId) {


        if (!MyApplication.getInstance().isConnected()) {
            mIDetailView.showNetErrorToast();
            return;
        }

        String url = "";

        HashMap<String, String> hm = new HashMap<>();
        hm.put("FK_Id", supplierId + "");
        hm.put("FK_Flag", 2 + "");
        hm.put("token", mDetailBiz.getToken());
        if (isCollection) {
            hm.put("ProductId", productId + "");
            url = Constants.COLLECTION_URL;
        } else {
            hm.put("id", productId + "");
            url = Constants.CANCEL_COLLECTION_URL;
        }

        mDetailBiz.requestCollection(hm, url, new IComResponse() {
            @Override
            public void getResponseData(List datas) {

            }

            @Override
            public void onError(String error) {
                AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                    @Override
                    public void call() {

                    }
                });
            }

            @Override
            public void onComplete(final String comp) {
                AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                    @Override
                    public void call() {
                        mIDetailView.showCollectionResult(comp,isCollection);
                    }
                });
            }
        });

    }


}
