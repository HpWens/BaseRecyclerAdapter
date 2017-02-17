package com.example.ecrbtb.mvp.category.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.MyApplication;
import com.example.ecrbtb.config.Constants;
import com.example.ecrbtb.mvp.category.biz.CategoryBiz;
import com.example.ecrbtb.mvp.category.biz.IResponseListener;
import com.example.ecrbtb.mvp.category.view.ICategoryView;

import java.util.HashMap;

/**
 * Created by boby on 2016/12/15.
 */

public class CategoryPresenter implements BasePresenter {

    private ICategoryView mICategoryView;

    private Context mContext;

    private CategoryBiz mCategoryBiz;

    private Handler mHandler;


    public CategoryPresenter(Context context, ICategoryView ICategoryView) {
        mContext = context;
        mICategoryView = ICategoryView;
        mCategoryBiz = CategoryBiz.getInstance(context);
    }

    /**
     * 初始化 栏目 数据
     */
    public void requestCategoryData() {
        //显示不同的界面
        showPageLayout();

        //判定是否已经加载过栏目数据
        if (mCategoryBiz.isExistCategoryData()) {
            mICategoryView.requestCategoryData(mCategoryBiz.getCategoryData());
            return;
        }

        HashMap<String, String> hm = new HashMap<>();

        hm.put("parentId", "0");
        hm.put("layer", "0");
        hm.put("FK_Id", "0");
        hm.put("FK_Flag", "0");

        mCategoryBiz.requestCategoryDate(hm, new IResponseListener() {
            @Override
            public void responseCode(int code) {
                if (code == Constants.REQUEST_FAILED_CODE) {
                    mHandler = new Handler(Looper.getMainLooper());
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mICategoryView.showServerException();
                        }
                    });
                } else if (code == Constants.REQUEST_SUCCESS_CODE) {
                    mHandler = new Handler(Looper.getMainLooper());
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mICategoryView.requestCategoryData(mCategoryBiz.getCategoryData());
                        }
                    });
                }
            }
        });

    }

    /**
     * 初始化商品数据
     */
    public void requestProductData(int storeId, int storeFKId, final int cId, final int pageIndex, final boolean isLoadMore) {

        requestProductData(storeId, storeFKId, cId, pageIndex, isLoadMore, "", false);

    }


    /**
     * 初始化商品数据
     */
    public void requestProductData(int storeId, int storeFKId, final int cId, final int pageIndex,
                                   final boolean isLoadMore, String searchKey, final boolean isSearch) {

        if (!isLoadMore) {
            showPageLayout();
        }

        HashMap<String, String> hm = new HashMap<>();

        hm.put("fields", "");
        hm.put("PageIndex", "" + pageIndex);
        hm.put("PageSize", "12");
        hm.put("SortField", "");
        hm.put("SortDirect", "");
        hm.put("Condition", "");
        hm.put("StoreId", "" + storeId);
        hm.put("UserId", "");
        hm.put("Status", "1");
        hm.put("Keywords", "" + searchKey);
        hm.put("IsProduct", "1");
        hm.put("SupplierId", "0");
        hm.put("FK_Flag", "2");
        hm.put("StoreFKId", "" + storeFKId);
        hm.put("cid", "" + cId);
        hm.put("Shelevd", "" + 1);
        hm.put("SupplierStatus", "" + 1);
        hm.put("Stock", "" + 1);
        hm.put("supplierdroit", "" + 1);
        hm.put("categorydroit", "" + 1);
        hm.put("productdroit", "" + 1);

        mCategoryBiz.requestProductData(hm, isSearch, new IResponseListener() {
            @Override
            public void responseCode(int code) {
                if (code == Constants.REQUEST_FAILED_CODE) {
                    mHandler = new Handler(Looper.getMainLooper());
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mICategoryView.showServerException();
                        }
                    });
                } else if (code == Constants.REQUEST_SUCCESS_CODE) {
                    mHandler = new Handler(Looper.getMainLooper());
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (isSearch) {
                                mICategoryView.requestProductData(mCategoryBiz.getSearchData(), isLoadMore);
                            } else {
                                mICategoryView.requestProductData(mCategoryBiz.getProductDataByPage(cId, pageIndex), isLoadMore);
                            }
                        }
                    });
                }
            }
        });

    }


    /**
     * 不同状态显示不同界面
     */
    private void showPageLayout() {
        if (MyApplication.getInstance().isConnected()) {
            mICategoryView.showLoadingPage();
        } else {
            mICategoryView.showNetErrorPage();
            return;
        }
    }

    //获取门店ID
    public void getStoreId() {
        int storeId = mCategoryBiz.getStoreId();
        MyApplication.getInstance().setStoreId(storeId);
        mICategoryView.getStoreId(storeId);
    }
}
