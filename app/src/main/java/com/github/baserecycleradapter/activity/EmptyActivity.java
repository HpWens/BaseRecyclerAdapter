package com.github.baserecycleradapter.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.github.baserecycleradapter.R;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;

import java.util.ArrayList;

/**
 * Created by Administrator on 7/27 0027.
 */
public class EmptyActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private BaseRecyclerAdapter<String> mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setAdapter(mAdapter=new BaseRecyclerAdapter<String>(this, new ArrayList<String>(), R.layout.rv_item) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {

            }
        });

        addEmptyView();

    }

    private void addEmptyView() {

        View emptyView=getLayoutInflater().inflate(R.layout.rv_empty, null);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        mAdapter.addEmptyView(emptyView);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "your click empty", Snackbar.LENGTH_SHORT).show();
            }
        });

    }
}
