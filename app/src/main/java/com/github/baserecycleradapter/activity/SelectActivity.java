package com.github.baserecycleradapter.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;

import com.github.baserecycleradapter.R;
import com.github.library.BaseQuickAdapter;
import com.github.library.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boby on 2017/1/22.
 */

public class SelectActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;

    BaseQuickAdapter<String, BaseViewHolder> mAdapter;

    private SparseBooleanArray mBooleanArray;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setAdapter(mAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.rv_item_cb, getDatas()) {
            @Override
            protected void convert(final BaseViewHolder helper, String item) {

                if (mBooleanArray.get(helper.getAdapterPosition())) {
                    helper.setBackgroundColor(R.id.tv, Color.parseColor("#FF4081"));
                } else {
                    helper.setBackgroundColor(R.id.tv, Color.parseColor("#FFFFFF"));
                }

                helper.setOnClickListener(R.id.tv, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setItemChecked(helper.getAdapterPosition());
                    }
                });

            }
        });

    }

    public List<String> getDatas() {
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            datas.add("" + i);
        }

        mBooleanArray = new SparseBooleanArray(datas.size());

        return datas;
    }


    private int mLastCheckedPosition = -1;

    /**
     * @param position
     */
    public void setItemChecked(int position) {
        if (mLastCheckedPosition == position)
            return;

        mBooleanArray.put(position, true);

        if (mLastCheckedPosition > -1) {
            mBooleanArray.put(mLastCheckedPosition, false);
            mAdapter.notifyItemChanged(mLastCheckedPosition);
        }

        mAdapter.notifyDataSetChanged();

        mLastCheckedPosition = position;
    }


}
