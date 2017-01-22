package com.github.baserecycleradapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.baserecycleradapter.activity.AnimationActivity;
import com.github.baserecycleradapter.activity.EmptyActivity;
import com.github.baserecycleradapter.activity.FlowActivity;
import com.github.baserecycleradapter.activity.HeaderAndFooterActivity;
import com.github.baserecycleradapter.activity.MultiItemActivity;
import com.github.baserecycleradapter.activity.NavigationActivity;
import com.github.baserecycleradapter.activity.PullToRefreshActivity;
import com.github.baserecycleradapter.activity.SelectActivity;
import com.github.baserecycleradapter.activity.TanTanActivity;
import com.github.baserecycleradapter.activity.TaoBaoActivity;
import com.github.library.BaseQuickAdapter;
import com.github.library.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private BaseQuickAdapter<String, BaseViewHolder> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        mRecyclerView.setAdapter(mAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.rv_item, getItemDatas()) {
            @Override
            protected void convert(final BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_item_text, item);

                helper.setOnClickListener(R.id.cv_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = null;
                        switch (helper.getAdapterPosition()) {
                            case 0:
                                intent = new Intent(MainActivity.this, HeaderAndFooterActivity.class);
                                break;
                            case 1:
                                intent = new Intent(MainActivity.this, AnimationActivity.class);
                                break;
                            case 2:
                                intent = new Intent(MainActivity.this, PullToRefreshActivity.class);
                                break;
                            case 3:
                                intent = new Intent(MainActivity.this, EmptyActivity.class);
                                break;
                            case 4:
                                intent = new Intent(MainActivity.this, MultiItemActivity.class);
                                break;
                            case 5:
                                intent = new Intent(MainActivity.this, NavigationActivity.class);
                                break;
                            case 6:
                                intent = new Intent(MainActivity.this, FlowActivity.class);
                                break;
                            case 7:
                                intent = new Intent(MainActivity.this, TanTanActivity.class);
                                break;
                            case 8:
                                intent = new Intent(MainActivity.this, SelectActivity.class);
                                break;
                            case 9:
                                intent = new Intent(MainActivity.this, TaoBaoActivity.class);
                                break;
                        }
                        startActivity(intent);
                    }
                });

            }
        });

    }

    public static List<String> getItemDatas() {
        List<String> mList = new ArrayList<>();
        mList.add("头部+尾部");
        mList.add("item动画");
        mList.add("上拉下拉加载");
        mList.add("空数据");
        mList.add("多item");
        mList.add("字母导航");
        mList.add("流式布局");
        mList.add("探探");
        mList.add("单选");
        mList.add("类似淘宝列表切换");
        return mList;
    }
}
