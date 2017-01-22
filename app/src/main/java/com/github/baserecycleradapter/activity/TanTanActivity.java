package com.github.baserecycleradapter.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.github.baserecycleradapter.R;
import com.github.baserecycleradapter.callback.TanTanCallback;
import com.github.baserecycleradapter.entity.SwipeCardBean;
import com.github.library.BaseQuickAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.layoutManager.CardConfig;
import com.github.library.layoutManager.OverLayCardLayoutManager;

/**
 * Created by boby on 2017/1/22.
 */

public class TanTanActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;

    BaseQuickAdapter<SwipeCardBean, BaseViewHolder> mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);

        mRecyclerView.setLayoutManager(new OverLayCardLayoutManager());

        mRecyclerView.setAdapter(mAdapter = new BaseQuickAdapter<SwipeCardBean, BaseViewHolder>(R.layout.item_swipe_card, SwipeCardBean.initDatas()) {

            @Override
            protected void convert(BaseViewHolder helper, SwipeCardBean item) {
                helper.setText(R.id.tvName, item.getName());
                helper.setText(R.id.tvPrecent, item.getPostition() + " /" + mData.size());
                helper.setBackgroundRes(R.id.iv, item.getResId());
            }
        });

        CardConfig.initConfig(this);

        final TanTanCallback callback = new TanTanCallback(mRecyclerView, mAdapter, mAdapter.getData());

        //测试竖直滑动是否已经不会被移除屏幕
        //callback.setHorizontalDeviation(Integer.MAX_VALUE);

        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);


    }
}
