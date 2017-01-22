package com.github.baserecycleradapter.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.github.baserecycleradapter.R;
import com.github.library.BaseQuickAdapter;
import com.github.library.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boby on 2017/1/22.
 */

public class TaoBaoActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView mRecyclerView;

    ImageButton mButton;

    boolean mIsLinearManager;

    BaseQuickAdapter<String, BaseViewHolder> mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taobao);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mButton = (ImageButton) findViewById(R.id.btn_change);

        mButton.setOnClickListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setAdapter(mAdapter = new BaseQuickAdapter<String, BaseViewHolder>(getDatas(), new int[]{R.layout.layout_linear, R.layout.layout_grid}) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_name, item);
            }
        });

        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.isFirstOnly(false);

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addData(addDatas());
                        mAdapter.loadMoreComplete();
                    }
                }, 1500);
            }
        });

    }

    @Override
    public void onClick(View v) {
        //线性
        if (mIsLinearManager) {
            mAdapter.setLayoutType(BaseQuickAdapter.TRANS_0_VIEW);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            //网格
            mAdapter.setLayoutType(BaseQuickAdapter.TRANS_1_VIEW);
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            //需要显示加载更多则加上下面这句   从新关联recycler
            mAdapter.onAttachedToRecyclerView(mRecyclerView);
        }
        mIsLinearManager = !mIsLinearManager;
    }

    public List<String> getDatas() {
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 66; i++) {
            datas.add("苹果超薄游戏本" + i);
        }
        return datas;
    }

    public List<String> addDatas() {
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            datas.add("苹果超薄游戏本" + i);
        }
        return datas;
    }

}
