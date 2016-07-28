package com.github.library.callback;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.github.library.listener.OnMoveAndSwipedListener;

/**
 * Created by jms on 2016/7/28.
 */
public class SimpleItemTouchHelper extends ItemTouchHelper.Callback {

    private OnMoveAndSwipedListener mListener;

    public SimpleItemTouchHelper(OnMoveAndSwipedListener listener) {
        mListener = listener;
    }

    /**
     * This method is used to set we drag the direction and the direction of the ski
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        final int swipeFlags = ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    /**
     * The callback when we drag the item this method;
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //如果两个item不是一个类型的，我们让他不可以拖拽
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        //回调adapter中的onItemMove方法
        mListener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    /**
     * Callback when we lateral spreads item this method;
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //回调adapter中的onItemDismiss方法
        mListener.onItemDismiss(viewHolder.getAdapterPosition());
    }

}
