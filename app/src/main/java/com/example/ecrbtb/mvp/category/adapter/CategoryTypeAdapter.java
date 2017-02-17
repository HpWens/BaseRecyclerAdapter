package com.example.ecrbtb.mvp.category.adapter;

import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.ecrbtb.R;
import com.example.ecrbtb.mvp.category.bean.Column0;
import com.example.ecrbtb.mvp.category.bean.Column1;
import com.example.ecrbtb.mvp.category.bean.Column2;

import java.util.List;

/**
 * Created by boby on 2016/12/15.
 */

public class CategoryTypeAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int TYPE_COLUMN_0 = 0;
    public static final int TYPE_COLUMN_1 = 1;
    public static final int TYPE_COLUMN_2 = 2;

    private int mC0LastCheckedPosition = -1;

    private int mC0CurrentPosition = 0;

    private SparseBooleanArray mC0BooleanArray;

    private int mC1LastCheckedPosition = -1;

    private int mC1CurrentPosition = -1;

    private SparseBooleanArray mC1BooleanArray;

    private IOnClickColumnListener mListener;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public CategoryTypeAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_COLUMN_0, R.layout.item_category_column0);
        addItemType(TYPE_COLUMN_1, R.layout.item_category_column0);
        addItemType(TYPE_COLUMN_2, R.layout.item_category_column0);

        mC0BooleanArray = new SparseBooleanArray(data.size());

        mC1BooleanArray = new SparseBooleanArray(data.size());
    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_COLUMN_0:
                final Column0 c0 = (Column0) item;
                helper.setText(R.id.tv_name, c0.name);
                helper.setTextSize(R.id.tv_name, 14);
                if (mC0BooleanArray.get(helper.getAdapterPosition())) {
                    helper.setVisible(R.id.view_line, View.VISIBLE);
                    helper.setBackgroundColor(R.id.rl_root, Color.WHITE);
                    helper.setTextColor(R.id.tv_name, Color.parseColor("#FF3657"));
                } else {
                    helper.setVisible(R.id.view_line, View.INVISIBLE);
                    helper.setBackgroundColor(R.id.rl_root, Color.parseColor("#F1F1F1"));
                    helper.setTextColor(R.id.tv_name, Color.parseColor("#333333"));
                }
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onClickColumnListener(c0.id);
                        }

                        int pos = helper.getAdapterPosition();
                        if (c0.isExpanded()) {
                            collapse(pos, false);
                        } else {
                            expand(pos, false);
                        }

                        clearC1Selected(); //清空2级菜单选中

                        Log.e("CategoryTypeAdapter", "onClick--------"+pos+"**"+mC0CurrentPosition);

                        if (pos == mC0CurrentPosition) {
                            return;
                        }

                        mC0CurrentPosition = pos;

                        setC0ItemChecked(pos);

                    }
                });
                break;
            case TYPE_COLUMN_1:
                final Column1 c1 = (Column1) item;
                helper.setText(R.id.tv_name, c1.name);
                helper.setTextSize(R.id.tv_name, 12);
                if (mC1BooleanArray.get(helper.getAdapterPosition())) {
                    helper.setBackgroundColor(R.id.rl_root, Color.WHITE);
                    helper.setTextColor(R.id.tv_name, Color.parseColor("#FF8196"));
                } else {
                    helper.setBackgroundColor(R.id.rl_root, Color.parseColor("#F1F1F1"));
                    helper.setTextColor(R.id.tv_name, Color.parseColor("#666666"));
                }
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onClickColumnListener(c1.id);
                        }
                        int pos = helper.getAdapterPosition();
                        if (c1.isExpanded()) {
                            collapse(pos, false);
                        } else {
                            expand(pos, false);
                        }

                        if (pos == mC1CurrentPosition) {
                            return;
                        }

                        mC1CurrentPosition = pos;

                        setC1ItemChecked(pos);

                    }
                });
                break;
            case TYPE_COLUMN_2:
                final Column2 c2 = (Column2) item;
                helper.setText(R.id.tv_name, c2.name);
                helper.setTextSize(R.id.tv_name, 10);
                helper.setTextColor(R.id.tv_name, Color.parseColor("#999999"));
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onClickColumnListener(c2.id);
                        }
                    }
                });
                break;
        }
    }

    private void clearC1Selected() {
        if (mC1LastCheckedPosition > -1) {
            mC1BooleanArray.put(mC1LastCheckedPosition, false);
            notifyItemChanged(mC1LastCheckedPosition);
            notifyDataSetChanged();
        }
        mC1LastCheckedPosition = -1;
        mC1CurrentPosition = -1;
    }


    /**
     * @param position
     */
    public void setC0ItemChecked(int position) {
        mC0BooleanArray.put(position, true);

        if (mC0LastCheckedPosition > -1) {
            mC0BooleanArray.put(mC0LastCheckedPosition, false);
            notifyItemChanged(mC0LastCheckedPosition);
        }

        notifyDataSetChanged();

        mC0LastCheckedPosition = position;
    }

    public int getLastCheckedPosition() {
        return mC0LastCheckedPosition;
    }

    public void setColumn0Position(int position) {
        this.mC0CurrentPosition = position;
    }


    private void setC1ItemChecked(int position) {
        mC1BooleanArray.put(position, true);

        if (mC1LastCheckedPosition > -1) {
            mC1BooleanArray.put(mC1LastCheckedPosition, false);
            notifyItemChanged(mC1LastCheckedPosition);
        }

        notifyDataSetChanged();

        mC1LastCheckedPosition = position;
    }

    /**
     * @param listener
     */
    public void setOnClickColumnListener(IOnClickColumnListener listener) {
        this.mListener = listener;
    }

}
