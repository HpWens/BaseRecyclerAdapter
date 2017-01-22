package com.github.baserecycleradapter.adapter;

import com.github.baserecycleradapter.R;
import com.github.baserecycleradapter.entity.MultipleItem;
import com.github.library.BaseMultiItemQuickAdapter;
import com.github.library.BaseViewHolder;

import java.util.List;

/**
 * Created by boby on 2017/1/22.
 */

public class OrderMultipleAdapter extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public OrderMultipleAdapter(List<MultipleItem> data) {
        super(data);

        addItemType(MultipleItem.HEADER, R.layout.multiple_header);
        addItemType(MultipleItem.CONTENT, R.layout.multiple_content);

    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItem item) {
        switch (item.itemType) {
            case MultipleItem.HEADER:

                break;
            case MultipleItem.CONTENT:
                helper.setText(R.id.tv_name, "小小淑" + item.name);
                break;

        }
    }
}
