package com.example.ecrbtb.mvp.detail.view;

import android.content.Context;

import com.example.ecrbtb.mvp.category.bean.Product;
import com.example.ecrbtb.mvp.goods.bean.Goods;

import java.util.List;

/**
 * Created by boby on 2016/12/27.
 */

public interface IDetailView {

    public Context getDetailContext();

    public void showServerError();

    public void getProductData(Product product);

    public void getBannerData(List<String> bannerList);

    public void showLoadPage();

    public void showNormalPage();

    public void showNetError();

    public void getGoodsData(List<Goods> list, boolean moreRequest);

    public void getLikeData(List<Product> productList);

    public void showEmptyData();

    public void updateShoppingCartNum(int num);

    public void getPhotoUrls(String urls);

    public void showNetErrorToast();

    public void showCommitDataLoad();

    public void dismissDataLoad();

    public void startOrderActivity(String json);

    public void showResponseError(String error);

    //显示收藏返回结果
    public void showCollectionResult(String result, boolean isCollection);

}
