package com.github.library.layoutManager;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by boby on 2016/12/7.
 */

public class FlowLayoutManager extends RecyclerView.LayoutManager {

    private int mVerticalOffset; //竖直偏移量 每次换行时，要根据这个offset判断
    private int mFirstVisPos;  //屏幕可见的第一个View的Position
    private int mLastVisPos;  //屏幕可见的最后一个View的Position
    private boolean mCanScrollVertically = false;

    private SparseArray<Rect> mItemRects = new SparseArray<>();//key 是View的position，保存View的bounds ，

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
                .LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        if (getItemCount() == 0) {//没有Item，界面空着吧
            detachAndScrapAttachedViews(recycler);
            return;
        }
        if (getItemCount() == 0 && state.isPreLayout()) {//state.isPreLayout()是支持动画的
            return;
        }
        //onLayoutChildren方法在RecyclerView 初始化时 会执行两遍
        detachAndScrapAttachedViews(recycler);

        //初始化区域
        mVerticalOffset = 0;
        mFirstVisPos = 0;
        mLastVisPos = getItemCount();

        //初始化时调用 填充childView
        fill(recycler, state);

        if (getItemCount() > getChildCount()) {
            mCanScrollVertically = true;
        }

    }

    /**
     * @param recycler
     * @param state
     */
    private void fill(RecyclerView.Recycler recycler, RecyclerView.State state) {
        fill(recycler, state, 0);
    }


    /**
     * 填充childView的核心方法,应该先填充，再移动。
     * 在填充时，预先计算dy的在内，如果View越界，回收掉。
     * 一般情况是返回dy，如果出现View数量不足，则返回修正后的dy.
     *
     * @param recycler
     * @param state
     * @param dy       RecyclerView给我们的位移量,+,显示底端， -，显示头部
     * @return 修正以后真正的dy（可能剩余空间不够移动那么多了 所以return <|dy|）
     */
    private int fill(RecyclerView.Recycler recycler, RecyclerView.State state, int dy) {
        int topOffset = getPaddingTop();//布局的上偏移量

        //回收越界子View
        if (getChildCount() > 0) {//屏幕可见的view数量
            for (int i = getChildCount() - 1; i >= 0; i--) {
                View child = getChildAt(i);
                if (dy > 0) { //上拉   需要回收当前屏幕，上越界的View
                    if (getDecoratedBottom(child) - dy < topOffset) {
                        removeAndRecycleView(child, recycler);
                        mFirstVisPos++;
                        continue;
                    }
                } else if (dy < 0) {  //下拉  //回收当前屏幕，下越界的View
                    if (getDecoratedTop(child) - dy > getHeight() - getPaddingBottom()) {
                        removeAndRecycleView(child, recycler);
                        mFirstVisPos--;
                        continue;
                    }
                }
            }

        }

        int leftOffset = getPaddingLeft();//布局的左偏移量
        int lineMaxHeight = 0; //每一行最大的高度

        if (dy >= 0) {
            int minPos = mFirstVisPos;//初始化时，我们不清楚究竟要layout多少个子View，所以就假设从0~itemcount-1
            mLastVisPos = getItemCount() - 1;
            if (getChildCount() > 0) {
                View lastView = getChildAt(getChildCount() - 1);
                minPos = getPosition(lastView) + 1;//从最后一个View+1开始吧
                topOffset = getDecoratedTop(lastView);
                leftOffset = getDecoratedRight(lastView);
                lineMaxHeight = Math.max(lineMaxHeight, getDecoratedMeasureVertical(lastView));
            }
            // 顺序addChildView
            for (int i = minPos; i <= mLastVisPos; i++) {
                //找recycler要一个childItemView,
                // 我们不管它是从scrap里取，还是从RecyclerViewPool里取，亦或是onCreateViewHolder里拿。
                View child = recycler.getViewForPosition(i);
                addView(child);
                measureChildWithMargins(child, 0, 0);
                //计算宽度 包括margin
                if (leftOffset + getDecoratedMeasureHorizontal(child) <= getHorizontalSpace()) {//当前行还排列的下
                    layoutDecoratedWithMargins(child, leftOffset, topOffset,
                            leftOffset + getDecoratedMeasureHorizontal(child), topOffset + getDecoratedMeasureVertical(child));

                    //保存Rect供逆序layout用
                    Rect rect = new Rect(leftOffset, topOffset + mVerticalOffset,
                            leftOffset + getDecoratedMeasureHorizontal(child), topOffset + getDecoratedMeasureVertical(child) + mVerticalOffset);
                    mItemRects.put(i, rect);

                    //改变 left  lineHeight
                    leftOffset += getDecoratedMeasureHorizontal(child);
                    lineMaxHeight = Math.max(lineMaxHeight, getDecoratedMeasureVertical(child));
                } else {//当前行排列不下
                    //改变top  left  lineHeight
                    leftOffset = getPaddingLeft();
                    topOffset += lineMaxHeight;
                    lineMaxHeight = 0;

                    //新起一行的时候要判断一下边界
                    if (topOffset - dy > getHeight() - getPaddingBottom()) {
                        //越界了 就回收
                        removeAndRecycleView(child, recycler);
                        mLastVisPos = i - 1;
                    } else {
                        layoutDecoratedWithMargins(child, leftOffset, topOffset,
                                leftOffset + getDecoratedMeasureHorizontal(child), topOffset + getDecoratedMeasureVertical(child));

                        //保存Rect供逆序layout用
                        Rect rect = new Rect(leftOffset, topOffset + mVerticalOffset,
                                leftOffset + getDecoratedMeasureHorizontal(child), topOffset + getDecoratedMeasureVertical(child) + mVerticalOffset);
                        mItemRects.put(i, rect);

                        //改变 left  lineHeight
                        leftOffset += getDecoratedMeasureHorizontal(child);
                        lineMaxHeight = Math.max(lineMaxHeight, getDecoratedMeasureVertical(child));
                    }
                }
            }

            //添加完后，判断是否已经没有更多的ItemView，并且此时屏幕仍有空白，则需要修正dy
            View lastChild = getChildAt(getChildCount() - 1);
            if (getPosition(lastChild) == getItemCount() - 1) {
                int gap = getHeight() - getPaddingBottom() - getDecoratedBottom(lastChild);
                if (gap > 0) {   //向上拉动
                    dy -= gap;
                }
            }
        } else { //向下拉动

            /**
             *   利用Rect保存子View边界正序排列时，保存每个子View的Rect，逆序时，直接拿出来layout。
             */
            int maxPos = getItemCount() - 1;
            mFirstVisPos = 0;
            if (getChildCount() > 0) {
                View firstView = getChildAt(0);
                maxPos = getPosition(firstView) - 1;
            }

            for (int i = maxPos; i >= mFirstVisPos; i--) {
                Rect rect = mItemRects.get(i);

                if (rect.bottom - mVerticalOffset - dy < getPaddingTop()) {//拉动到屏幕之上了
                    mFirstVisPos = i + 1;
                    break;
                } else {
                    View child = recycler.getViewForPosition(i);
                    addView(child, 0);//将View添加至RecyclerView中，childIndex为1，但是View的位置还是由layout的位置决定
                    measureChildWithMargins(child, 0, 0);

                    layoutDecoratedWithMargins(child, rect.left, rect.top - mVerticalOffset, rect.right, rect.bottom - mVerticalOffset);
                }
            }
        }

        return dy;
    }

    @Override
    public boolean canScrollVertically() {
        return mCanScrollVertically;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State
            state) {
        if (dy == 0 || getItemCount() == 0) {
            return 0;
        }
        int realOffset = dy;//实际滑动的距离， 可能会在边界处被修复
        //边界修复代码
        if (mVerticalOffset + realOffset < 0) {//上边界   手指向下滑动  realOffset为负数
            realOffset = -mVerticalOffset;
        } else if (realOffset > 0) {//下边界     手指向上滑动   realOffset 为正数
            //利用最后一个子View比较修正
            View lastChild = getChildAt(getChildCount() - 1);//获取到可见屏幕最后一个View
            if (getPosition(lastChild) == getItemCount() - 1) {//是否滑动到底部
                int gap = getHeight() - getPaddingBottom() - getDecoratedBottom(lastChild);
                if (gap > 0) {
                    realOffset = -gap;
                } else if (gap == 0) {
                    realOffset = 0;
                } else {
                    realOffset = Math.min(realOffset, -gap);
                }
            }
        }

        realOffset = fill(recycler, state, realOffset);//先填充，再位移。

        mVerticalOffset += realOffset;  //累加实际滑动距离

        offsetChildrenVertical(-realOffset); //滑动

        return realOffset;
    }

    /***
     * 获取某个childView在水平方向所占的空间
     *
     * @param view
     * @return
     */
    public int getDecoratedMeasureHorizontal(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        return getDecoratedMeasuredWidth(view) + params.leftMargin + params.rightMargin;
    }

    /**
     * 获取某个childView在竖直方向所占的空间
     *
     * @param view
     * @return
     */
    public int getDecoratedMeasureVertical(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        return getDecoratedMeasuredHeight(view) + params.topMargin + params.bottomMargin;
    }

    /**
     * 获取垂直方向上的空间
     *
     * @return
     */
    public int getVerticalSpace() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }


    /**
     * @return
     */
    public int getHorizontalSpace() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

}
