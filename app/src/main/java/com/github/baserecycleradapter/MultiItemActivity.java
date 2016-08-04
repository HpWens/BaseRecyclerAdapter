package com.github.baserecycleradapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.github.baserecycleradapter.entity.MultiItem;
import com.github.library.BaseMultiItemAdapter;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jms on 2016/8/4.
 */
public class MultiItemActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private BaseRecyclerAdapter<String> mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header_and_footer);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(mAdapter = new BaseMultiItemAdapter<MultiItem>(this, getMultiItemDatas()) {
            @Override
            protected void convert(BaseViewHolder helper, MultiItem item) {
                switch (helper.getItemViewType()) {
                    case MultiItem.TEXT:
                        helper.setText(R.id.tv_item_text, item.content);
                        break;

                    case MultiItem.IMG:

                        break;

                    case MultiItem.IMGS:

                        break;
                }
            }

            @Override
            protected void addItemLayout() {
                addItemType(MultiItem.TEXT, R.layout.rv_item);
                addItemType(MultiItem.IMG, R.layout.item_image);
                addItemType(MultiItem.IMGS, R.layout.item_images);
            }
        });

        mAdapter.openLoadAnimation(false);

        addHeaderView();

        addFooterView();

    }

    private void addFooterView() {
        View headerView = getLayoutInflater().inflate(R.layout.rv_footer, null);
        headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mAdapter.addFooterView(headerView);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "your click footerView", Snackbar.LENGTH_SHORT).show();
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
            mList.add("欢迎关注文淑的博客");
        }
        return mList;
    }

    public static List<MultiItem> getMultiItemDatas() {
        List<MultiItem> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            MultiItem multiItem = new MultiItem();
            String str = null;
            multiItem.itemType = MultiItem.IMG;
            if (i % 2 == 0) {
                str = "hello world";
                multiItem.itemType = MultiItem.TEXT;
            } else if (i % 3 == 0) {
                multiItem.itemType = MultiItem.IMGS;
            }
            multiItem.content = str;
            list.add(multiItem);
        }
        return list;
    }
}
