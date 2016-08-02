package com.github.library.listener;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 8/2 0002.
 */
public interface OnDragAndSwipeListener {

    boolean onItemDrag(int fromPosition, int toPosition);

    void onItemSwipe(int position);

    void onItemSelected(RecyclerView.ViewHolder viewHolder);

    void onItemClear(RecyclerView.ViewHolder viewHolder);

    /**
     * @param viewHolder
     * @param dX         Side slip distance
     */
    void onItemSwipeAlpha(RecyclerView.ViewHolder viewHolder, float dX);
}
