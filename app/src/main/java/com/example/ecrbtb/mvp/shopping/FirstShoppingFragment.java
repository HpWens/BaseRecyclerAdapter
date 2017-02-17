package com.example.ecrbtb.mvp.shopping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.ecrbtb.BasePageFragment;
import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.R;
import com.example.ecrbtb.config.Constants;
import com.example.ecrbtb.event.BackToFirstFragmentEvent;
import com.example.ecrbtb.mvp.category.bean.Product;
import com.example.ecrbtb.mvp.category.event.ShoppingCartEvent;
import com.example.ecrbtb.mvp.order.OrderActivity;
import com.example.ecrbtb.mvp.shopping.adapter.IShopListener;
import com.example.ecrbtb.mvp.shopping.adapter.ShoppingAdapter;
import com.example.ecrbtb.mvp.shopping.event.RefreshProductEvent;
import com.example.ecrbtb.mvp.shopping.presenter.ShoppingPresenter;
import com.example.ecrbtb.mvp.shopping.view.IShoppingView;
import com.example.ecrbtb.widget.PageStateLayout;
import com.flyco.animation.FlipEnter.FlipVerticalSwingEnter;
import com.flyco.animation.FlipExit.FlipVerticalExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by boby on 2016/12/15.
 */

public class FirstShoppingFragment extends BasePageFragment implements IShoppingView, IShopListener {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.recycler)
    RecyclerView mRecycler;
    @InjectView(R.id.cb_all)
    CheckBox mCbAll;
    @InjectView(R.id.tv_price)
    TextView mTvPrice;
    @InjectView(R.id.tv_settlement)
    TextView mTvSettlement;

    private ShoppingPresenter mPresenter;
    private ShoppingAdapter mAdapter;

    private boolean mIsAllSelected;

    public static FirstShoppingFragment newInstance() {

        Bundle args = new Bundle();

        FirstShoppingFragment fragment = new FirstShoppingFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getRootView() {
        return R.id.fl_root;
    }

    @Override
    protected BasePresenter initPresenter() {
        return mPresenter = new ShoppingPresenter(this);
    }

    @Override
    public int getResourceId() {
        return R.layout.fragment_shopping_first;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        registerEventBus();


        mToolbar.setTitle("进货车");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new BackToFirstFragmentEvent());
            }
        });

        mRecycler.setLayoutManager(new LinearLayoutManager(_mActivity));
        mRecycler.setAdapter(mAdapter = new ShoppingAdapter(_mActivity, R.layout.item_shopping_cart, new ArrayList<Product>()));

        mAdapter.setOnIShopListener(this);

        mCbAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsAllSelected = !mIsAllSelected;
                if (mIsAllSelected) {
                    mAdapter.setAllSelected(true);
                    setSettlementNum(mAdapter.getSelectedNum());
                    //显示金额
                    mAdapter.getCartUnitPrice();
                } else {
                    mAdapter.setAllSelected(false);
                    setSettlementNum(0);
                    mTvPrice.setText(getString(R.string.amount));
                }
            }
        });
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
        showPageState(PageStateLayout.EMPTY_STATE, getString(R.string.empty_shopping_cart));
    }

    @Override
    public void showNormalPage() {
        showPageState(PageStateLayout.NORMAL_STATE);
    }

    @Override
    public Context getShoppingContext() {
        return _mActivity;
    }

    @Override
    public void getShoppingData(List<Product> lists) {
        mAdapter.setNewData(lists);
        mIsAllSelected = false;
        mCbAll.setChecked(false);
        setSettlementNum(0);
        mTvPrice.setText(getString(R.string.amount));
        EventBus.getDefault().post(new ShoppingCartEvent(mAdapter.getAllNum(), true));
    }

    @Override
    public void showNetErrorToast() {
        showToast("亲,你的网络不给力哟!");
    }

    @Override
    public void showSweetDialog() {
        showSweetAlertDialog("载入中...");
    }

    @Override
    public void showErrorToast(String error) {
        dismissSweetAlertDialog();
        showToast(error);
    }

    @Override
    public void startOrderActivity(String json) {
        dismissSweetAlertDialog();

        Intent intent = new Intent(_mActivity, OrderActivity.class);
        intent.putExtra(OrderActivity.ORDER_DATA, json);
        startActivity(intent);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterEventBus();
        ButterKnife.reset(this);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        mPresenter.getShoppingData();
    }

    @OnClick({R.id.cb_all, R.id.tv_settlement})
    public void onClick(View view) {
        int num = mAdapter.getSelectedNum();
        switch (view.getId()) {
            case R.id.cb_all:
                break;
            case R.id.tv_settlement:
                if (num == 0) {
                    showToast(R.string.empty_order);
                    return;
                }
                mPresenter.commitSettlement(mAdapter.getSelectedData(), Constants.BUY_TYPE_CART);
                break;
        }
    }

    @Override
    public void addShoppingCart(int productId) {
        EventBus.getDefault().post(new ShoppingCartEvent(1));
        EventBus.getDefault().post(new RefreshProductEvent(productId, 1));
    }

    @Override
    public void subShoppingCart(int productId) {
        subShoppingCart(productId, -1);
    }


    public void subShoppingCart(int productId, int num) {
        EventBus.getDefault().post(new ShoppingCartEvent(num));
        EventBus.getDefault().post(new RefreshProductEvent(productId, num));
    }

    @Override
    public void showNumLessOne() {
        showToast(R.string.num_less_one);
    }

    @Override
    public void showDeleteDialog(final int position, final int supplierId, final int productId, final int goodsId, final int productNum, final boolean isSingle) {
        final MaterialDialog dialog = new MaterialDialog(_mActivity);
        dialog.btnNum(2)
                .title("提示")
                .btnText("取消", "确定")
                .content("你确定将此产品删除吗?")
                .showAnim(new FlipVerticalSwingEnter())
                .dismissAnim(new FlipVerticalExit())
                .show();
        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.dismiss();
            }
        }, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                if (isSingle) {
                    mPresenter.deleteProduct(supplierId, productId, productNum);
                } else {
                    //记住同时删除商品与货品
                    mPresenter.deleteProduct(supplierId, productId, productNum);
                    mPresenter.deleteGoods(supplierId, goodsId, productNum);
                }
                subShoppingCart(productId, -productNum);
                mAdapter.getData().remove(position);
                mAdapter.notifyItemRemoved(position);
                //设置选中数量
                setSettlementNum(mAdapter.getSelectedNum());
                mAdapter.getCartUnitPrice();
                //设置选中的状态
                dialog.dismiss();
            }
        });

    }

    @Override
    public void cancelAllSelected(boolean isCancel) {
        if (isCancel) {
            if (mCbAll.isChecked()) {
                mCbAll.setChecked(false);
                mIsAllSelected = false;
            }
        } else {
            if (mAdapter.isAllSelected()) {
                mCbAll.setChecked(true);
                mIsAllSelected = true;
            }
        }
        setSettlementNum(mAdapter.getSelectedNum());
        //显示金额
        mAdapter.getCartUnitPrice();
    }

    @Override
    public void showEmptyData() {
        showToast(R.string.server_error);
    }

    @Override
    public void getCartPrice(String cartPrice) {
        mTvPrice.setText("金额：" + cartPrice);
    }


    /**
     * @return
     */
    public void setSettlementNum(int num) {
        mTvSettlement.setText("结算(" + num + ")");
    }


}
