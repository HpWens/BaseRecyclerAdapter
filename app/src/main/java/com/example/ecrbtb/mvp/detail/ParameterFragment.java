package com.example.ecrbtb.mvp.detail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.ecrbtb.BasePageFragment;
import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.R;
import com.example.ecrbtb.mvp.detail.bean.ProductParams;
import com.example.ecrbtb.mvp.detail.event.ProductParameterEvent;
import com.example.ecrbtb.mvp.detail.presenter.ParameterPresenter;
import com.example.ecrbtb.mvp.detail.view.IParameterView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by boby on 2016/12/28.
 */

public class ParameterFragment extends BasePageFragment implements IParameterView {

    @InjectView(R.id.recycler)
    RecyclerView mRecycler;

    private ParameterPresenter mPresenter;
    private BaseQuickAdapter<ProductParams, BaseViewHolder> mAdapter;
    private int mProductId;
    private int mSupplierId;

    private static final String PRODUCT_ID = "product_id";
    private static final String SUPPLIER_ID = "SupplierId_id";

    private String[] mType = new String[]{"品牌：", "类型：", "目录："};

    private List<ProductParams> mParamsList = new ArrayList<>();

    public static ParameterFragment newInstance(int productId, int supplierId) {

        Bundle args = new Bundle();

        ParameterFragment fragment = new ParameterFragment();
        args.putInt(PRODUCT_ID, productId);
        args.putInt(SUPPLIER_ID, supplierId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getRootView() {
        return R.id.fl_root;
    }

    @Override
    protected BasePresenter initPresenter() {
        return mPresenter = new ParameterPresenter(this);
    }

    @Override
    public int getResourceId() {
        return R.layout.fragment_detail_parameter;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        registerEventBus();
        Bundle bundle = getArguments();
        if (bundle != null) {

            int productId = mProductId = bundle.getInt(PRODUCT_ID);
            int supplierId = mSupplierId = bundle.getInt(SUPPLIER_ID);

            mPresenter.requestParasData(productId, supplierId);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setAutoMeasureEnabled(true);

        mRecycler.setLayoutManager(linearLayoutManager);
        mRecycler.setHasFixedSize(true);
        mRecycler.setNestedScrollingEnabled(false);

        for (int i = 0; i < mType.length; i++) {
            ProductParams params = new ProductParams();
            params.Name = mType[i];
            params.Value = "";
            mParamsList.add(params);
        }

        mRecycler.setAdapter(mAdapter = new BaseQuickAdapter<ProductParams, BaseViewHolder>(R.layout.item_parameter, mParamsList) {
            @Override
            protected void convert(BaseViewHolder helper, ProductParams item) {
                helper.setText(R.id.tv_type, item.Name);
                helper.setText(R.id.tv_value, item.Value);
            }
        });
    }

    @Override
    public Context getParameterContext() {
        return _mActivity;
    }

    @Override
    public void getParamsData(List<ProductParams> paramsList) {
        mAdapter.addData(paramsList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterEventBus();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 0)
    public void onReceiveMessage(@NonNull ProductParameterEvent event) {
        List<String> params = mPresenter.getParameterData(mProductId, mSupplierId);
        for (int i = 0; i < params.size(); i++) {
            ProductParams ps = mAdapter.getData().get(i);
            ps.Value = params.get(i);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
