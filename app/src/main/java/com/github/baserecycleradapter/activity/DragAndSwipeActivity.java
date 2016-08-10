package com.github.baserecycleradapter.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.github.baserecycleradapter.R;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.callback.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jms on 2016/8/2.
 */
public class DragAndSwipeActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private BaseRecyclerAdapter<String> mAdapter;
    private Button btnManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_swipe);

        btnManager = (Button) findViewById(R.id.btn_manager);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setAdapter(mAdapter = new BaseRecyclerAdapter<String>(this, getItemDatas(), R.layout.rv_item) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_item_text, item);
                helper.setVisible(R.id.iv_drag, true);
            }
        });

        btnManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showManagerDialog();
            }
        });

        mAdapter.openLoadAnimation(false);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);

        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);

        mAdapter.setItemTouchHelper(mItemTouchHelper);

        mAdapter.setDragViewId(R.id.iv_drag);

        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        addHeaderView();

        addFooterView();

    }


    private void showManagerDialog() {
        final String[] stringItems = {"LinearLayoutManager", "GridLayoutManager"};
        final ActionSheetDialog dialog = new ActionSheetDialog(this, stringItems, null);
        dialog.isTitleShow(false)
                .show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    default:
                    case 0:
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(DragAndSwipeActivity.this));
                        break;
                    case 1:
                        mRecyclerView.setLayoutManager(new GridLayoutManager(DragAndSwipeActivity.this, 2));
                        break;
                }
                mRecyclerView.setAdapter(mAdapter);
                dialog.dismiss();
            }
        });
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
        for (int i = 0; i < 20; i++) {
            mList.add("文淑" + i);
        }
        return mList;
    }
}
