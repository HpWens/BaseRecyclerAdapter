package com.github.baserecycleradapter.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.github.baserecycleradapter.R;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.animation.AnimationType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 7/27 0027.
 */
public class AnimationActivity extends AppCompatActivity {

    private TextView tvAnimationType;
    private TextView tvIsFirstOnly;
    private RecyclerView mRecyclerView;
    private BaseRecyclerAdapter<String> mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        tvAnimationType = (TextView) findViewById(R.id.tv_animation_type);
        tvIsFirstOnly = (TextView) findViewById(R.id.tv_first_only);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new BaseRecyclerAdapter<String>(this, getItemDatas(), R.layout.rv_item) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_item_text, item);
            }
        });

        mAdapter.openLoadAnimation();

        init();
    }

    private void init() {

        tvAnimationType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAnimationTypeDialog();
            }
        });

        tvIsFirstOnly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showIsFirstOnlyDialog();
            }
        });

    }

    public static List<String> getItemDatas() {
        List<String> mList = new ArrayList<>();
        for (int i = 0; i < 99; i++) {
            mList.add("欢迎关注文淑的博客");
        }
        return mList;
    }


    private void showAnimationTypeDialog() {
        final String[] stringItems = {"CUSTOM", "ALPHA", "SCALE", "SLIDE_BOTTOM",
                "SLIDE_TOP", "SLIDE_LEFT", "SLIDE_RIGHT", "SLIDE_LEFT_RIGHT", "SLIDE_BOTTOM_TOP"};
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
                        mAdapter.openLoadAnimation(AnimationType.CUSTOM);
                        break;
                    case 1:
                        mAdapter.openLoadAnimation(AnimationType.ALPHA);
                        break;
                    case 2:
                        mAdapter.openLoadAnimation(AnimationType.SCALE);
                        break;
                    case 3:
                        mAdapter.openLoadAnimation(AnimationType.SLIDE_BOTTOM);
                        break;
                    case 4:
                        mAdapter.openLoadAnimation(AnimationType.SLIDE_TOP);
                        break;
                    case 5:
                        mAdapter.openLoadAnimation(AnimationType.SLIDE_LEFT);
                        break;
                    case 6:
                        mAdapter.openLoadAnimation(AnimationType.SLIDE_RIGHT);
                        break;
                    case 7:
                        mAdapter.openLoadAnimation(AnimationType.SLIDE_LEFT_RIGHT);
                        break;
                    case 8:
                        mAdapter.openLoadAnimation(AnimationType.SLIDE_BOTTOM_TOP);
                        break;
                }
                tvAnimationType.setText(stringItems[position]);
                mAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
    }

    private void showIsFirstOnlyDialog() {
        final String[] stringItems = {"isFirstOnly(true)", "isFirstOnly(false)"};
        final ActionSheetDialog dialog = new ActionSheetDialog(this, stringItems, null);
        dialog.isTitleShow(false)
                .show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    default:
                    case 0:
                        mAdapter.isFirstOnlyAnimation(true);
                        break;
                    case 1:
                        mAdapter.isFirstOnlyAnimation(false);
                        break;
                }
                tvIsFirstOnly.setText(stringItems[position]);
                mAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
    }
}
