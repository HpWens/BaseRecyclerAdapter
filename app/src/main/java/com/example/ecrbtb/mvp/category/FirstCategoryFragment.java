package com.example.ecrbtb.mvp.category;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.ecrbtb.BaseActivity;
import com.example.ecrbtb.BasePageFragment;
import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.R;
import com.example.ecrbtb.config.Constants;
import com.example.ecrbtb.event.LoginSuccessEvent;
import com.example.ecrbtb.event.RequestPermissionEvent;
import com.example.ecrbtb.event.ResponsePermissionEvent;
import com.example.ecrbtb.event.SearchEvent;
import com.example.ecrbtb.event.StartBrotherEvent;
import com.example.ecrbtb.mvp.category.adapter.CategoryTypeAdapter;
import com.example.ecrbtb.mvp.category.adapter.IAddProductListener;
import com.example.ecrbtb.mvp.category.adapter.IOnClickColumnListener;
import com.example.ecrbtb.mvp.category.adapter.ProductAdapter;
import com.example.ecrbtb.mvp.category.bean.Product;
import com.example.ecrbtb.mvp.category.event.ShoppingCartEvent;
import com.example.ecrbtb.mvp.category.presenter.CategoryPresenter;
import com.example.ecrbtb.mvp.category.view.ICategoryView;
import com.example.ecrbtb.mvp.detail.DetailActivity;
import com.example.ecrbtb.mvp.goods.GoodsActivity;
import com.example.ecrbtb.mvp.goods.event.UpdateProductEvent;
import com.example.ecrbtb.mvp.login.LoginActivity;
import com.example.ecrbtb.mvp.search.SearchFragment;
import com.example.ecrbtb.mvp.shopping.event.RefreshProductEvent;
import com.example.ecrbtb.widget.PageStateLayout;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by boby on 2016/12/15.
 */

public class FirstCategoryFragment extends BasePageFragment implements ICategoryView {

    @InjectView(R.id.rv_type)
    RecyclerView mRvType;
    @InjectView(R.id.rv_content)
    RecyclerView mRvContent;
    @InjectView(R.id.swipe_refresh)
    SwipeRefreshLayout mRefreshLayout;
    @InjectView(R.id.linear_search)
    LinearLayout mLinearSearch;
    @InjectView(R.id.iv_layout)
    ImageView mIvChangeLayout;
    @InjectView(R.id.et_search)
    EditText mEtSearch;
    @InjectView(R.id.menu_item_linear)
    FloatingActionButton mMenuItemLinear;
    @InjectView(R.id.menu_item_grid)
    FloatingActionButton mMenuItemGrid;
    @InjectView(R.id.menu_item_flow)
    FloatingActionButton mMenuItemFlow;
    @InjectView(R.id.menu)
    FloatingActionMenu mMenu;

    private int storeId;
    private int storeFKId;

    private CategoryPresenter mPresenter;
    private CategoryTypeAdapter mColumnAdapter;

    private int mCurrentPage = 1;
    private int mCategoryId = 0;
    private String mSearchKey = "";
    private boolean mIsLinearLayout = true;
    private boolean mIsShowSearchData = false;

    private ProductAdapter mProductAdapter;

    public static FirstCategoryFragment newInstance() {
        Bundle args = new Bundle();

        FirstCategoryFragment fragment = new FirstCategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected BasePresenter initPresenter() {
        return mPresenter = new CategoryPresenter(_mActivity, this);
    }

    @Override
    public int getResourceId() {
        return R.layout.fragment_catecory_first;
    }

    @Override
    protected int getRootView() {
        return R.id.fl_root;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        registerEventBus();

        mRvType.setLayoutManager(new LinearLayoutManager(_mActivity));
        mRvType.setAdapter(mColumnAdapter = new CategoryTypeAdapter(new ArrayList<MultiItemEntity>()));
        mColumnAdapter.setOnClickColumnListener(new IOnClickColumnListener() {
            @Override
            public void onClickColumnListener(int categoryId) {
                if (mEtSearch.getText().toString().length() > 0) {
                    mEtSearch.setText("");
                    mSearchKey = "";
                    mIsShowSearchData = false;
                }
                mCurrentPage = 1;//点击获取第一页数据
                mCategoryId = categoryId;
                mPresenter.requestProductData(storeId, storeFKId, mCategoryId, mCurrentPage, false);
            }
        });

        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mProductAdapter = new ProductAdapter(_mActivity, R.layout.item_linear_product, R.layout.item_grid_product, new ArrayList<Product>());
        mRvContent.setLayoutManager(new LinearLayoutManager(_mActivity));
        mProductAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mProductAdapter.isFirstOnly(false);
        mRvContent.setAdapter(mProductAdapter);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mProductAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
                mCurrentPage = 1;
                requestProductData(false);
            }
        });

        mProductAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mProductAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
                mCurrentPage++;
                requestProductData(true);
            }
        });

        mProductAdapter.setIAddProductListener(new IAddProductListener() {
            @Override
            public void startLogin() {
                startActivity(new Intent(_mActivity, LoginActivity.class));
            }

            @Override
            public void addShoppingCart() {
                EventBus.getDefault().post(new ShoppingCartEvent(1));
            }

            @Override
            public void subShoppingCart() {
                EventBus.getDefault().post(new ShoppingCartEvent(-1));
            }

            @Override
            public void startGoodsPage(int position, Product product) {
                Intent intent = new Intent(_mActivity, GoodsActivity.class);
                intent.putExtra(BaseActivity.DEFAULT_PARCELABLE_NAME, product);
                intent.putExtra(Constants.ADAPTER_POSITION, position);
//                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(_mActivity,
//                        R.anim.bottom_enter, 0);
//                ActivityCompat.startActivity(_mActivity, intent, compat.toBundle());
                _mActivity.startActivity(intent);
                _mActivity.overridePendingTransition(R.anim.bottom_enter,0);
            }

            @Override
            public void startDetailActivity(View view0, View view1, Product product) {
                Intent intent = new Intent(_mActivity, DetailActivity.class);
                intent.putExtra(DetailActivity.PRODUCT_DATA, product);

                ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        _mActivity, new Pair<>(view0, DetailActivity.VIEW_NAME_IMAGE), new Pair<>(view1, DetailActivity.VIEW_NAME_IMAGE));
                ActivityCompat.startActivity(_mActivity, intent, activityOptions.toBundle());
            }
        });

        mRvContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 5) {
                    mMenu.hideMenu(true);
                } else if (dy < -5) {
                    mMenu.showMenu(true);
                }
            }
        });


    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.getStoreId();
        mPresenter.requestCategoryData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        unregisterEventBus();
    }

    @Override
    public void showNetErrorPage() {
        showPageState(PageStateLayout.ERROR_STATE);
    }

    @Override
    public void showEmptyPage() {
        showPageState(PageStateLayout.EMPTY_STATE);
    }

    @Override
    public void showLoadingPage() {
        showPageState(PageStateLayout.LOADING_STATE);
    }

    @Override
    public void showNormalPage() {
        showPageState(PageStateLayout.NORMAL_STATE);
    }

    @Override
    public void showServerException() {
        showToast(getString(R.string.server_exception));
        showNormalPage();
    }

    @Override
    public void getStoreId(int id) {
        storeFKId = storeId = id;
    }

    @Override
    protected void retryLoading() {
        super.retryLoading();
        mPresenter.requestCategoryData();
    }

    @Override
    public void requestCategoryData(List<MultiItemEntity> lists) {
        mColumnAdapter.setNewData(lists);
        if (lists == null || lists.isEmpty()) {
            showEmptyPage();
            return;
        } else {
            showNormalPage();
        }
        mColumnAdapter.setC0ItemChecked(0);
        //获取全部的第一页数据
        mCurrentPage = 1;
        mPresenter.requestProductData(storeId, storeFKId, 0, mCurrentPage, false);

//        //发送消息给首页
//        EventBus.getDefault().post(new StopLoadEvent());
    }


    @Override
    public void requestProductData(List<Product> lists, boolean isLoadMore) {
        if (isLoadMore) {
            if (lists == null || lists.isEmpty()) {
                mProductAdapter.loadMoreEnd();
            } else {
                mProductAdapter.addData(lists);
                mProductAdapter.loadMoreComplete();
            }
        } else {
            if (lists == null || lists.isEmpty()) {
                showEmptyPage();
                return;
            } else {
                showNormalPage();
            }
            mProductAdapter.setNewData(lists);

            mRefreshLayout.setRefreshing(false);
        }
    }

    @OnClick({R.id.iv_layout, R.id.linear_search, R.id.et_search, R.id.menu_item_linear,
            R.id.menu_item_grid, R.id.menu_item_flow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_layout:
                mIsLinearLayout = !mIsLinearLayout;
                if (mIsLinearLayout) {
                    mIvChangeLayout.setImageResource(R.mipmap.ic_grid);
                    mProductAdapter.setLayoutType(BaseQuickAdapter.TRANS_LINEAR_TYPE);
                    mRvContent.setLayoutManager(new LinearLayoutManager(_mActivity));
                } else {
                    mIvChangeLayout.setImageResource(R.mipmap.ic_linear);
                    mProductAdapter.setLayoutType(BaseQuickAdapter.TRANS_GRID_TYPE);
                    mRvContent.setLayoutManager(new GridLayoutManager(_mActivity, 2));
                    mProductAdapter.onAttachedToRecyclerView(mRvContent);
                }
                break;
            case R.id.et_search:
            case R.id.linear_search:
                EventBus.getDefault().post(new RequestPermissionEvent(Constants.FILE_PERMISSION));
                break;
            case R.id.menu_item_linear:
                mIvChangeLayout.setImageResource(R.mipmap.ic_grid);
                mProductAdapter.setLayoutType(BaseQuickAdapter.TRANS_LINEAR_TYPE);
                mRvContent.setLayoutManager(new LinearLayoutManager(_mActivity));
                closeMenu();
                break;
            case R.id.menu_item_grid:
                mIvChangeLayout.setImageResource(R.mipmap.ic_linear);
                mProductAdapter.setLayoutType(BaseQuickAdapter.TRANS_GRID_TYPE);
                mRvContent.setLayoutManager(new GridLayoutManager(_mActivity, 2));
                mProductAdapter.onAttachedToRecyclerView(mRvContent);
                closeMenu();
                break;
            case R.id.menu_item_flow:
                break;
        }
    }

    @Subscribe
    public void responsePermission(ResponsePermissionEvent event) {
        //申请权限  读写文件的权限
        switch (event.type) {
            case Constants.FILE_PERMISSION:
                EventBus.getDefault().post(new StartBrotherEvent(SearchFragment.newInstance()));
                break;
        }
    }

    @Subscribe
    public void onReceiveMessage(@NonNull UpdateProductEvent event) {
        if (event.position == -1) {
            for (Product product : mProductAdapter.getData()) {
                if (product.SupplierId == event.supplierId && product.ProductId == event.productId) {
                    product.ProductNum = event.num;
                    break;
                }
            }
        } else {
            mProductAdapter.getData().get(event.position).ProductNum = event.num;
        }
        mProductAdapter.closeLoadAnimation();
        mProductAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 0)
    public void onReceiveMessage(@NonNull LoginSuccessEvent event) {
        mPresenter.getStoreId();
        if (mColumnAdapter.getLastCheckedPosition() != 0) {
            mColumnAdapter.setColumn0Position(-1);
            mColumnAdapter.setC0ItemChecked(0);
        }
        mCurrentPage = 1;
        mPresenter.requestProductData(storeId, storeFKId, 0, mCurrentPage, false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 0)
    public void onReceiveMessage(@NonNull SearchEvent event) {
        mEtSearch.setText(event.msg);
        mCurrentPage = 1;
        mSearchKey = event.msg;
        mIsShowSearchData = true;
        //获取搜索商品
        mPresenter.requestProductData(storeId, storeFKId, 0, mCurrentPage, false, mSearchKey, true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 0)
    public void onReceiveMessage(@NonNull RefreshProductEvent event) {
        List<Product> productList = mProductAdapter.getData();
        for (Product product : productList) {
            if (product.ProductId == event.productId) {
                product.ProductNum += event.productNum;
                mProductAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    /**
     * 获取数据
     *
     * @param isLoadMore
     */
    public void requestProductData(boolean isLoadMore) {
        if (mIsShowSearchData) {
            mPresenter.requestProductData(storeId, storeFKId, 0, mCurrentPage, isLoadMore, mSearchKey, true);
        } else {
            mPresenter.requestProductData(storeId, storeFKId, mCategoryId, mCurrentPage, isLoadMore);
        }
    }

    public void closeMenu() {
        if (mMenu.isOpened()) {
            mMenu.close(true);
        }
    }

}
