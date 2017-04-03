package com.github.baserecycleradapter.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.github.baserecycleradapter.R;
import com.github.library.BaseQuickAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.widget.MiGuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import static com.github.baserecycleradapter.activity.PullToRefreshActivity.addDatas;

/**
 * Created by Administrator on 7/27 0027.
 */
public class HeaderAndFooterActivity extends AppCompatActivity {

    private MiGuRecyclerView mRecyclerView;

    private BaseQuickAdapter<String, BaseViewHolder> mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header_and_footer);

        mRecyclerView = (MiGuRecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setAdapter(mAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.rv_item, getItemDatas()) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_item_text, item);
                helper.setOnClickListener(R.id.tv_item_text, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar.make(view, "your click item", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });

        mRecyclerView.setRefreshListener(new MiGuRecyclerView.RefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setNewData(getItemDatas());
                        mRecyclerView.refreshComplete();
                    }
                }, 3000);
            }
        });

        addHeaderView();

        addFooterView();


        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addData(addDatas());
                        mAdapter.loadMoreComplete();
                    }
                }, 3000);
            }
        });

    }

    private void addFooterView() {
        View footerView = getLayoutInflater().inflate(R.layout.rv_footer, null);
        footerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mAdapter.addFooterView(footerView);
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.addData(getItemDatas());
            }
        });
    }

    private void addHeaderView() {
        View headerView = getLayoutInflater().inflate(R.layout.rv_header, null);
        headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mAdapter.addHeaderView(headerView);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "your click headerView", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public static List<String> getItemDatas() {
        List<String> mList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mList.add("文淑的博客欢迎你");
        }
        return mList;
    }
}
