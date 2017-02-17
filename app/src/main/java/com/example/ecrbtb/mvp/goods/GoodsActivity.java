package com.example.ecrbtb.mvp.goods;

import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.ecrbtb.BaseActivity;
import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.R;
import com.example.ecrbtb.config.Constants;
import com.example.ecrbtb.mvp.category.bean.Product;
import com.example.ecrbtb.mvp.category.event.ShoppingCartEvent;
import com.example.ecrbtb.mvp.goods.adapter.GoodsAdapter;
import com.example.ecrbtb.mvp.goods.adapter.IGoodsAdapter;
import com.example.ecrbtb.mvp.goods.bean.Goods;
import com.example.ecrbtb.mvp.goods.event.UpdateProductEvent;
import com.example.ecrbtb.mvp.goods.presenter.GoodsPresenter;
import com.example.ecrbtb.mvp.goods.view.IGoodsView;
import com.example.ecrbtb.mvp.order.OrderActivity;
import com.example.ecrbtb.widget.PageStateLayout;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by boby on 2016/12/19.
 */

public class GoodsActivity extends BaseActivity implements IGoodsView {

    @InjectView(R.id.recycler)
    RecyclerView mRecycler;
    @InjectView(R.id.simple_view)
    SimpleDraweeView mSimpleView;
    @InjectView(R.id.tv_name)
    TextView mTvName;
    @InjectView(R.id.fl_page)
    FrameLayout mFlPage;

    private Product mProduct;
    private GoodsPresenter mPresenter;
    private GoodsAdapter mAdapter;
    private int mAdapterPosition = -1;
    private String mIsDeduction = "0";//是否抵扣    0不能抵扣用价格   1可以抵扣用积分加价格

    @Override
    protected void initView(ViewDataBinding bind) {
        mSimpleView.setImageURI(Constants.BASE_URL + mProduct.DefaultPic);
        mTvName.setText(mProduct.ProductName);

        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new GoodsAdapter(this, R.layout.item_goods, new ArrayList<Goods>(), mIsDeduction);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        super.initData();

        mPresenter.requestGoodsData(mProduct);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mAdapter.setOnGoodsAdapterListener(new IGoodsAdapter() {
            @Override
            public void showNumLessZero() {
                showToast("数量不能小于0");
            }
        });

    }

    @Override
    protected void initIntent(Intent intent) {
        super.initIntent(intent);
        mAdapterPosition = intent.getIntExtra(Constants.ADAPTER_POSITION, -1);
    }

    @Override
    protected void getParcelableExtras(Parcelable parcelable) {
        super.getParcelableExtras(parcelable);
        mProduct = (Product) parcelable;
        mIsDeduction = mProduct.IsDeduction;
    }

    @Override
    protected void initBarTint() {
        super.initBarTint();
    }

    @Override
    protected BasePresenter initPresenter() {
        return mPresenter = new GoodsPresenter(this);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_goods;
    }

    @Override
    protected int getRootView() {
        return R.id.fl_page;
    }

    @Override
    public void showLoadPage() {
        showPageState(PageStateLayout.LOADING_STATE);
    }

    @Override
    public void showErrorPage() {
        showPageState(PageStateLayout.ERROR_STATE);
    }

    @Override
    public void showEmptyPage() {
        showPageState(PageStateLayout.EMPTY_STATE);
    }

    @Override
    public void showNormalPage() {
        showPageState(PageStateLayout.NORMAL_STATE);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showServerError() {
        showToast(getString(R.string.server_error));
    }

    @Override
    public void showEmptyData() {
        showToast(getString(R.string.empty_data));
    }

    @Override
    public void showEmptyNum() {
        showToast(getString(R.string.empty_num));
    }

    @Override
    public void showNetErrorToast() {
        showToast("客官,你的网络不给力哟!");
    }

    @Override
    public void showCommitDataLoad() {
        showSweetAlertDialog("提交中...");
    }

    @Override
    public void addShoppingCart(int num) {
        int offsetNum = num - mProduct.ProductNum;
        EventBus.getDefault().post(new ShoppingCartEvent(offsetNum));
        //通知商品列表刷新数据
        if (mAdapterPosition != -1) {
            EventBus.getDefault().post(new UpdateProductEvent(num, mAdapterPosition));
        }
        finish();
    }

    @Override
    protected void retryLoading() {
        super.retryLoading();
        mPresenter.requestGoodsData(mProduct);
    }

    @Override
    public void getGoodsData(List<Goods> goodsList) {
        if (goodsList == null || goodsList.isEmpty()) {
            showEmptyPage();
        } else {

            mAdapter.setNewData(goodsList);
            mFlPage.setVisibility(View.GONE);
            mRecycler.setVisibility(View.VISIBLE);

            showNormalPage();
        }
    }

    @Override
    public void startOrderActivity(String json) {
        dismissSweetAlertDialog();
        Intent intent = new Intent(this, OrderActivity.class);
        intent.putExtra(OrderActivity.ORDER_DATA, json);
        startActivity(intent);
    }

    @Override
    public void showResponseError(String error) {
        dismissSweetAlertDialog();
        showToast(error);
    }

    @OnClick({R.id.tv_pay, R.id.tv_cart})
    public void onClick(View view) {
        int num = mAdapter.getGoodsNumber();
        switch (view.getId()) {
            case R.id.tv_pay:
                if (num == 0) {
                    showToast(R.string.empty_order);
                    return;
                }
                mPresenter.commitOrderData(mAdapter.getData(), Constants.BUY_TYPE_PRODUCT);
                break;
            case R.id.tv_cart:
                if (mAdapter.getGoodsNumber() == 0) {
                    showEmptyNum();
                    return;
                }
                mPresenter.joinShoppingCart(mProduct.ProductId, mProduct.SupplierId, mAdapter.getData());
                showToast("商品已加入进货车");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.clearHandler();
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, R.anim.bottom_exit);
    }

}
