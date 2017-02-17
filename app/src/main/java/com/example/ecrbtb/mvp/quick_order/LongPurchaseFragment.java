package com.example.ecrbtb.mvp.quick_order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.ecrbtb.BasePageFragment;
import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.MyApplication;
import com.example.ecrbtb.R;
import com.example.ecrbtb.config.Constants;
import com.example.ecrbtb.mvp.merchant.MerchantWebActivity;
import com.example.ecrbtb.mvp.quick_order.adapter.PurchaseAdapter;
import com.example.ecrbtb.mvp.quick_order.bean.Purchase;
import com.example.ecrbtb.mvp.quick_order.presenter.PurchasePresenter;
import com.example.ecrbtb.mvp.quick_order.view.IPurchaseView;
import com.example.ecrbtb.widget.PageStateLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boby on 2016/12/15.
 */

public class LongPurchaseFragment extends BasePageFragment implements IPurchaseView,
        PurchaseAdapter.OnPurchaseListener, SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener {

    RecyclerView mRecycler;

    int mCurrentPage = 1;

    PurchaseAdapter mAdapter;

    PurchasePresenter mPresenter;

    SwipeRefreshLayout mRefreshLayout;

    static final int DEFAULT_PAGE = 1;

    public static LongPurchaseFragment newInstance() {

        Bundle args = new Bundle();

        LongPurchaseFragment fragment = new LongPurchaseFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initView(View view) {
        super.initView(view);

        mRecycler = (RecyclerView) view.findViewById(R.id.recycler);

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);

        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        mRecycler.setLayoutManager(new LinearLayoutManager(_mActivity));

        mRecycler.setAdapter(mAdapter = new PurchaseAdapter(_mActivity, new ArrayList<Purchase>()));

        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);

        mAdapter.isFirstOnly(false);

        mAdapter.setOnPurchaseListener(this);

        mRefreshLayout.setOnRefreshListener(this);

        mAdapter.setOnLoadMoreListener(this);

        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                mScrollTotal += dy;
//                if (dy > 5) {
//                    mFab.hide();
//                } else if (dy < -5) {
//                    mFab.show();
//                }
//                if (mScrollTotal <= 0) {
//                    mFab.hide();
//                }
            }
        });

    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mPresenter.requestData(mCurrentPage);
    }

    @Override
    protected int getRootView() {
        return R.id.fl_root;
    }

    @Override
    protected BasePresenter initPresenter() {
        return mPresenter = new PurchasePresenter(this);
    }

    @Override
    public int getResourceId() {
        return R.layout.fragment_quick_order_order;
    }


    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public Context getPurchaseContext() {
        return _mActivity;
    }

    @Override
    public void getPurchaseData(List<Purchase> purchases, int currentPage) {
        //判断当前是否为第一页数据
        if (currentPage == 1) {
            //显示空数据
            if (purchases == null || purchases.isEmpty()) {
                showPageState(PageStateLayout.EMPTY_STATE);
                return;
            }
            mAdapter.setNewData(purchases);
            showPageState(PageStateLayout.NORMAL_STATE);
        } else {
            if (purchases == null || purchases.isEmpty()) {
                mAdapter.loadMoreEnd();
                return;
            }
            mAdapter.addData(purchases);
            mAdapter.loadMoreComplete();
        }

        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showLoadingPage() {
        showPageState(PageStateLayout.LOADING_STATE);
    }

    @Override
    public void showNetErrorPage() {
        showPageState(PageStateLayout.ERROR_STATE);
    }

    @Override
    public void showError(String error) {
        if (MyApplication.getInstance().isConnected()) {
            showToast(error);
        } else {
            showToast("客官,请检查你的网络连接");
        }

        if (mAdapter.isLoadMoreEnable()) {
            mCurrentPage = mCurrentPage > 1 ? mCurrentPage - 1 : mCurrentPage;
            mAdapter.loadMoreFail();
        }

        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    protected void retryLoading() {
        super.retryLoading();
        mPresenter.requestData(mCurrentPage);
    }

    @Override
    public void onStartDetail(String url) {
        Intent intent = new Intent(_mActivity, MerchantWebActivity.class);
        intent.putExtra(Constants.MERCHANT_URL, url);
        startActivity(intent);
    }

    @Override
    public void onAddShoppingCart(List<Purchase> purchaseList) {
        if (purchaseList == null || purchaseList.isEmpty()) {
            showToast("获取订单数据异常,加入进货车失败");
            return;
        }

        showToast("客官请见谅,功能正在研发当中!");
        
    }

    @Override
    public void onRefresh() {
        mCurrentPage = DEFAULT_PAGE;
        mPresenter.requestData(mCurrentPage);
    }

    @Override
    public void onLoadMoreRequested() {
        mCurrentPage++;
        mPresenter.requestData(mCurrentPage);
    }
}
