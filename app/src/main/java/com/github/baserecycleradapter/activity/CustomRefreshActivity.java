package com.github.baserecycleradapter.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.github.baserecycleradapter.R;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.listener.OnRecyclerItemClickListener;
import com.github.library.listener.RequestLoadMoreListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 8/24 0024.
 */
public class CustomRefreshActivity extends AppCompatActivity {

    private WSRecyclerView mRecyclerView;

    private BaseRecyclerAdapter<String> mAdapter;

    private MyHandler mMyHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMyHandler = new MyHandler(this);
        mRecyclerView = (WSRecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new BaseRecyclerAdapter<String>(this, getItemDatas(), R.layout.rv_item) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_item_text, item + helper.getAdapterPosition());
            }
        });
        mAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Snackbar.make(view, "当前的索引位置为" + position, Snackbar.LENGTH_SHORT).show();
            }
        });

        mAdapter.addHeaderView(mRecyclerView.getRefreshView());

        mRecyclerView.setOnRefreshCompleteListener(new WSRecyclerView.OnRefreshCompleteListener() {
            @Override
            public void onRefreshComplete() {
                mMyHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setData(getItemDatas());
                        mRecyclerView.refreshComplete();
                    }
                }, 5000);
            }
        });

        mAdapter.openLoadingMore(true);
        mAdapter.setOnLoadMoreListener(new RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mMyHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataChangeAfterLoadMore(getItemDatas(), true);
                    }
                }, 2000);
            }
        });
    }


    public static List<String> getItemDatas() {
        List<String> mList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            mList.add("文淑的博客欢迎你");
        }
        return mList;
    }

    private static class MyHandler extends Handler {
        private WeakReference<CustomRefreshActivity> activityWeakReference;

        public MyHandler(CustomRefreshActivity activity) {
            activityWeakReference = new WeakReference<CustomRefreshActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CustomRefreshActivity activity = activityWeakReference.get();
            if (activity == null) {
                return;
            }
        }
    }
}
