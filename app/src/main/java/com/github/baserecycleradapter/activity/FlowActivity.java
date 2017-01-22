package com.github.baserecycleradapter.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.github.baserecycleradapter.R;
import com.github.library.BaseQuickAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.layoutManager.FlowLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boby on 2017/1/22.
 */

public class FlowActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);

        mRecyclerView.setLayoutManager(new FlowLayoutManager());

        mRecyclerView.setAdapter(new BaseQuickAdapter<String, BaseViewHolder>(R.layout.rv_item_btn, getDatas()) {

            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.btn_name, item);
            }
        });

    }


    public List<String> getDatas() {
        List<String> datas = new ArrayList<>();

        datas.add("女装");
        datas.add("数码电器");
        datas.add("车票旅行");
        datas.add("运动户外");
        datas.add("浴巾 成人 冬季");
        datas.add("牛肉干");
        datas.add("橘子");
        datas.add("海泰首饰专用");
        datas.add("女装");
        datas.add("女装");
        datas.add("女装");
        datas.add("女装");
        datas.add("小孩 大海 太空");
        datas.add("女装");
        datas.add("女装");
        datas.add("女装");
        datas.add("梦想");

        return datas;
    }
}
