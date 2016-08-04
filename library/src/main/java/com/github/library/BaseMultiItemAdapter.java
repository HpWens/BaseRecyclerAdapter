package com.github.library;

import android.content.Context;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.github.library.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by Administrator on 8/3 0003.
 */
public abstract class BaseMultiItemAdapter<T extends MultiItemEntity> extends BaseRecyclerAdapter {

    private SparseArray<Integer> layouts;

    public BaseMultiItemAdapter(Context context, List data) {
        super(context, data);
        //add item layout
        addItemLayout();
        //open multi item type
        openMultiItemType(true);
    }

    /**
     * @param type
     * @param layoutResId
     */
    protected void addItemType(int type, int layoutResId) {
        if (layouts == null) {
            layouts = new SparseArray<>();
        }
        layouts.put(type, layoutResId);
    }

    /**
     * @param viewType
     */
    private int getItemLayoutId(int viewType) {
        return layouts == null ? 0 : layouts.get(viewType);
    }

    @Override
    protected int getMultiItemViewType(int position) {
        return ((MultiItemEntity) mData.get(position)).itemType;
    }

    @Override
    protected BaseViewHolder onBaseViewHolder(ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, getItemLayoutId(viewType));
    }

    @Override
    protected void convert(BaseViewHolder helper, Object item) {
        convert(helper, (T) item);
    }

    protected abstract void convert(BaseViewHolder helper, T item);

    protected abstract void addItemLayout();

}
