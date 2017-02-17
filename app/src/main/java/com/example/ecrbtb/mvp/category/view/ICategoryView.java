package com.example.ecrbtb.mvp.category.view;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.ecrbtb.mvp.category.bean.Product;

import java.util.List;

/**
 * Created by boby on 2016/12/15.
 */

public interface ICategoryView {


    public void showNetErrorPage();

    public void showEmptyPage();

    public void showLoadingPage();

    public void requestProductData(List<Product> lists, boolean isLoadMore);

    public void showServerException();

    public void showNormalPage();

    public void requestCategoryData(List<MultiItemEntity> lists);

    public void getStoreId(int id);

}
