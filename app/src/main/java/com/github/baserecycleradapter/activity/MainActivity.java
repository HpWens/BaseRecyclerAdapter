package com.github.baserecycleradapter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.baserecycleradapter.R;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.listener.OnRecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private BaseRecyclerAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new BaseRecyclerAdapter<String>(this, getItemDatas(), R.layout.rv_item) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_item_text, item);
            }
        });

        mAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = null;
                switch (position) {
                    default:
                    case 0:
                        intent = new Intent(MainActivity.this, HeaderAndFooterActivity.class);
                        break;
                    case 1:
                        intent = new Intent(MainActivity.this, AnimationActivity.class);
                        break;
                    case 2:
                        intent = new Intent(MainActivity.this, ItemAnimationActivity.class);
                        break;
                    case 3:
                        intent = new Intent(MainActivity.this, PullToRefreshActivity.class);
                        break;
                    case 4:
                        intent = new Intent(MainActivity.this, EmptyActivity.class);
                        break;
                    case 5:
                        intent = new Intent(MainActivity.this, DragAndSwipeActivity.class);
                        break;
                    case 6:
                        intent = new Intent(MainActivity.this, MultiItemActivity.class);
                        break;
                    case 7:
                        intent = new Intent(MainActivity.this, NavigationActivity.class);
                        break;
                    case 8:
                        intent = new Intent(MainActivity.this, CustomRefreshActivity.class);
                        break;
                }
                startActivity(intent);
            }
        });
    }

    public static List<String> getItemDatas() {
        List<String> mList = new ArrayList<>();
        mList.add("HeaderAndFooter Adapter");
        mList.add("Animation Adapter");
        mList.add("ItemAnimation Adapter");
        mList.add("PullToRefresh Adapter");
        mList.add("Empty Adapter");
        mList.add("Drag And Swipe Adapter");
        mList.add("Multi Item Adapter");
        mList.add("Navigation Adapter");
        mList.add("Custom Refresh Adapter");
        return mList;
    }
}
