package com.github.baserecycleradapter.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.github.baserecycleradapter.R;
import com.github.library.BaseQuickAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.indicator.LoadMoreType;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 7/27 0027.
 */
public class PullToRefreshActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;

    private BaseQuickAdapter<String, BaseViewHolder> mAdapter;


    private Button btnLoadType;

    private MyHandler mHandler = new MyHandler(this);

    //delayMillis
    private static final int DELAY_MILLIS = 1500;

    private int mShowType = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_refresh);

        btnLoadType = (Button) findViewById(R.id.btn_load_type);
        btnLoadType.setOnClickListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);

        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setAdapter(mAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.rv_item, getItemDatas()) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_item_text, item);
            }
        });

        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

                mShowType++;

                if (mShowType == 2) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.loadMoreFail();
                        }
                    }, DELAY_MILLIS);

                } else if (mShowType >= 4) {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.loadMoreEnd();
                        }
                    });

                } else {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.addData(addDatas());
                            mAdapter.loadMoreComplete();
                        }
                    }, DELAY_MILLIS);
                }

            }
        });

        addHeaderView();
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
        for (int i = 0; i < 10; i++) {
            mList.add("欢迎关注文淑的博客" + new Random().nextInt(100));
        }
        return mList;
    }

    public static List<String> addDatas() {
        List<String> mList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            mList.add("我是新增条目" + (i + 1));
        }
        return mList;
    }

    @Override
    public void onRefresh() {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mShowType = 0;
                mAdapter.setNewData(getItemDatas());
                mRefreshLayout.setRefreshing(false);
            }
        }, DELAY_MILLIS);

    }

    @Override
    public void onClick(View v) {
        showAnimationTypeDialog();
    }


    private void showAnimationTypeDialog() {
        final String[] stringItems = {"APAY", "BEAT", "CLIP_ROTATE", "SCALE"};
        final ActionSheetDialog dialog = new ActionSheetDialog(this, stringItems, null);
//        title("choose animation type")//
//                .titleTextSize_SP(14.5f)//
        dialog.isTitleShow(false)
                .show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    default:
                    case 0:
                        mAdapter.setLoadMoreType(LoadMoreType.APAY);
                        break;
                    case 1:
                        mAdapter.setLoadMoreType(LoadMoreType.BALL_BEAT);
                        break;
                    case 2:
                        mAdapter.setLoadMoreType(LoadMoreType.BALL_CLIP_ROTATE);
                        break;
                    case 3:
                        mAdapter.setLoadMoreType(LoadMoreType.BALL_SCALE);
                        break;
                }
                btnLoadType.setText(stringItems[position]);
                mAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
    }


    private static class MyHandler extends Handler {
        private WeakReference<PullToRefreshActivity> activityWeakReference;

        public MyHandler(PullToRefreshActivity activity) {
            activityWeakReference = new WeakReference<PullToRefreshActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            PullToRefreshActivity activity = activityWeakReference.get();
            if (activity == null) {
                return;
            }
        }
    }

}
