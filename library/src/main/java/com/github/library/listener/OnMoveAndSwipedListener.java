package com.github.library.listener;

/**
 * Created by jms on 2016/7/28.
 */
public interface OnMoveAndSwipedListener {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
