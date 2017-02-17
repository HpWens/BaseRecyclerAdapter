package com.example.ecrbtb.mvp.quick_order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.ecrbtb.BaseActivity;
import com.example.ecrbtb.BasePageFragment;
import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.MyApplication;
import com.example.ecrbtb.R;
import com.example.ecrbtb.config.Constants;
import com.example.ecrbtb.mvp.category.bean.Product;
import com.example.ecrbtb.mvp.category.event.ShoppingCartEvent;
import com.example.ecrbtb.mvp.detail.DetailActivity;
import com.example.ecrbtb.mvp.goods.GoodsActivity;
import com.example.ecrbtb.mvp.goods.event.UpdateProductEvent;
import com.example.ecrbtb.mvp.login.LoginActivity;
import com.example.ecrbtb.mvp.quick_order.adapter.CollectionAdapter;
import com.example.ecrbtb.mvp.quick_order.presenter.CollectionPresenter;
import com.example.ecrbtb.mvp.quick_order.view.ICollectionView;
import com.example.ecrbtb.widget.PageStateLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by boby on 2016/12/15.
 */

public class CollectionFragment extends BasePageFragment implements ICollectionView {

    @InjectView(R.id.recycler)
    RecyclerView mRecycler;
    @InjectView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @InjectView(R.id.fl_root)
    FrameLayout mFlRoot;

    static final int DEFAULT_PAGE = 1;
    static final String PAGE_STAT = "page_state";
    static final int PRODUCT_ADD_NUM = 1;
    static final int PRODUCT_SUB_NUM = -1;

    CollectionAdapter mAdapter;
    int mCurrentPage = 1;
    int mPageState = 0;
    CollectionPresenter mPresenter;


    public static CollectionFragment newInstance(int state) {

        Bundle args = new Bundle();
        args.putInt(PAGE_STAT, state);
        CollectionFragment fragment = new CollectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getRootView() {
        return R.id.fl_root;
    }

    @Override
    protected BasePresenter initPresenter() {
        return mPresenter = new CollectionPresenter(this);
    }

    @Override
    public int getResourceId() {
        return R.layout.fragment_quick_order_collection;
    }


    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        registerEventBus();

        Bundle bundle = getArguments();
        if (bundle != null) {
            mPageState = bundle.getInt(PAGE_STAT);
        }

        mSwipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        mRecycler.setLayoutManager(new LinearLayoutManager(_mActivity));
        mRecycler.setAdapter(mAdapter = new CollectionAdapter(_mActivity, R.layout.item_collection_product, new ArrayList<Product>()));

        mAdapter.openLoadAnimation(BaseQuickAdapter.CUSTOM);
        mAdapter.isFirstOnly(false);

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentPage = 1;
                mPresenter.requestData(mCurrentPage, mPageState);
            }
        });

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mCurrentPage++;
                mPresenter.requestData(mCurrentPage, mPageState);
            }
        });

        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!mAdapter.getOpenAnimationEnable()) {
                    mAdapter.openLoadAnimation();
                }
            }
        });

        mAdapter.setOnCollectionListener(new CollectionAdapter.OnCollectionListener() {
            @Override
            public void onStartLogin() {
                startActivity(new Intent(_mActivity, LoginActivity.class));
            }

            @Override
            public void onAddProduct() {
                EventBus.getDefault().post(new ShoppingCartEvent(PRODUCT_ADD_NUM));
            }

            @Override
            public void onSubProduct() {
                EventBus.getDefault().post(new ShoppingCartEvent(PRODUCT_SUB_NUM));
            }

            @Override
            public void onStartDetail(Product product) {
                Intent intent = new Intent(_mActivity, DetailActivity.class);
                intent.putExtra(Constants.PRODUCT_DATA, product);
                startActivity(intent);
            }

            @Override
            public void onStartGoods(int position, Product product) {
                Intent intent = new Intent(_mActivity, GoodsActivity.class);
                intent.putExtra(BaseActivity.DEFAULT_PARCELABLE_NAME, product);
                intent.putExtra(Constants.ADAPTER_POSITION, position);
                _mActivity.startActivity(intent);
                _mActivity.overridePendingTransition(R.anim.bottom_enter, 0);
            }
        });
    }

    @Override
    public Context getQuickContext() {
        return _mActivity;
    }

    @Override
    public void getCollectionData(List<Product> productList, int currentPage) {
        if (currentPage == DEFAULT_PAGE) {
            if (productList == null || productList.isEmpty()) {
                showPageState(PageStateLayout.EMPTY_STATE);
                return;
            }
            showPageState(PageStateLayout.NORMAL_STATE);
            mAdapter.setNewData(productList);
        } else {
            if (productList == null || productList.isEmpty()) {
                mAdapter.loadMoreEnd();
                return;
            }
            mAdapter.addData(productList);
            mAdapter.loadMoreComplete();
        }

        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
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
            if (mAdapter.isLoadMoreEnable()) {
                if (mCurrentPage <= 1) {
                    mCurrentPage = DEFAULT_PAGE;
                } else {
                    mCurrentPage--;
                }
                mAdapter.loadMoreFail();
            }
        } else {
            showToast("客官,请检查你的网络连接");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterEventBus();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mPresenter.requestData(mCurrentPage, mPageState);
    }

    @Override
    protected void retryLoading() {
        super.retryLoading();
        mPresenter.requestData(mCurrentPage, mPageState);
    }

    @Subscribe
    public void onReceiveMessage(@NonNull UpdateProductEvent event) {
        if (mAdapter.getData() == null || mAdapter.getData().isEmpty()) {
            return;
        }
        Product product = mAdapter.getData().get(event.position);
        if (product != null) {
            product.ProductNum = event.num;
            mAdapter.closeLoadAnimation();
            mAdapter.notifyDataSetChanged();
        }
    }

}
