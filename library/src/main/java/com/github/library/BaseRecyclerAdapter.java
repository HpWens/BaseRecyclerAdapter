package com.github.library;

import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.github.library.animation.AlphaInAnimation;
import com.github.library.animation.AnimationType;
import com.github.library.animation.BaseAnimation;
import com.github.library.animation.CustomAnimation;
import com.github.library.animation.ScaleInAnimation;
import com.github.library.animation.SlideInBottomAnimation;
import com.github.library.animation.SlideInLeftAnimation;
import com.github.library.animation.SlideInRightAnimation;
import com.github.library.animation.SlideInTopAnimation;
import com.github.library.listener.OnRecyclerItemClickListener;
import com.github.library.listener.OnRecyclerItemLongClickListener;
import com.github.library.listener.RequestLoadMoreListener;
import com.github.library.view.FooterView;
import com.github.library.view.LoadType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jms on 2016/7/19.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected Context mContext;
    protected int mLayoutResId;
    protected LayoutInflater mLayoutInflater;
    protected List<T> mData;

    private int mLastPosition = -1;
    private int mViewType = -1;
    private int pageSize = -1;

    private int mDuration = DEFAULT_DURATION;
    private boolean mOpenAnimationEnable = true;
    private boolean mFirstOnlyAnimationEnable = false;//animation show first
    private BaseAnimation[] mBaseAnimation;
    private Interpolator mInterpolator = new LinearInterpolator();
    private LoadType mLoadType;

    private boolean mLoadingMoreEnable = false;
    private boolean mNextLoadingEnable = false;

    private boolean mHeadAndEmptyEnable; // headerView and emptyView
    private boolean mFootAndEmptyEnable;// footerView and emptyView
    private boolean mEmptyEnable;

    private View mHeaderView;
    private View mFooterView;
    private View mEmptyView;
    private View mLoadView;

    private static final int VIEW_TYPE_HEADER = 0x00000001;//header
    private static final int VIEW_TYPE_CONTENT = 0x00000002;//content
    private static final int VIEW_TYPE_FOOTER = 0x00000003;//footer
    private static final int VIEW_TYPE_EMPTY = 0x00000004;//empty
    private static final int VIEW_TYPE_LOADING = 0x00000005;//loading

    private static final int DEFAULT_DURATION = 300;

    private RequestLoadMoreListener mRequestLoadMoreListener;
    private OnRecyclerItemClickListener onRecyclerItemClickListener;
    private OnRecyclerItemLongClickListener onRecyclerItemLongClickListener;

    public BaseRecyclerAdapter(Context context, List<T> data, int layoutResId) {
        this.mContext = context;
        this.mData = data == null ? new ArrayList<T>() : data;
        if (layoutResId != 0) {
            this.mLayoutResId = layoutResId;
        }
        this.mLayoutInflater = LayoutInflater.from(mContext);
        //default animation
        this.mBaseAnimation = new BaseAnimation[]{new CustomAnimation()};

        this.mLoadType = LoadType.CUSTOM;
    }

    /**
     * @param position
     * @return
     */
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getItemCount() {
        int loadMoreCount = isLoadMore() ? 1 : 0;
        int count = mData.size() + getHeaderViewCount() + getFooterViewCount() + loadMoreCount;
        if (mData.size() == 0 && mEmptyView != null) {

            if (count == 0 && (!mHeadAndEmptyEnable || !mFootAndEmptyEnable)) {
                count += getEmptyViewCount();
            } else if (mHeadAndEmptyEnable || mFootAndEmptyEnable) {
                count += getEmptyViewCount();
            }

            if ((mHeadAndEmptyEnable && getHeaderViewCount() == 1 && count == 1) || count == 0) {
                mEmptyEnable = true;
                count += getEmptyViewCount();
            }
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {

        if (mHeaderView != null && position == 0) {
            return VIEW_TYPE_HEADER;
        }

        // emptyView position <=2 (header +  empty + footer)
        // four situation   {@link #setEmptyView(header+empty+footer , empty  , empty+footer,header+empty)}  position <= 2
        if (mData.size() == 0 && mEmptyView != null && mEmptyEnable && position <= 2) {

            // three situation   {@link #setEmptyView(header + empty + footer , header + empty , empty+footer)}  position = 1
            if ((mHeadAndEmptyEnable || mFootAndEmptyEnable) && position == 1) {

                if (mHeaderView == null && mFooterView != null) { //empty+footer
                    return VIEW_TYPE_FOOTER;
                } else if (mHeaderView != null && mEmptyView != null) {  //header + empty + footer , header + empty
                    return VIEW_TYPE_EMPTY;
                }
            }
            //two situation   position = 0
            else if (position == 0) {
                if (mHeaderView == null) {
                    return VIEW_TYPE_EMPTY;
                } else {
                    return VIEW_TYPE_HEADER;
                }
            } else if (position == 2 && mHeaderView != null && mFooterView != null) {
                return VIEW_TYPE_FOOTER;
            } else if (position == 1) {
                if (mHeaderView != null) {
                    return VIEW_TYPE_EMPTY;
                }
                return VIEW_TYPE_FOOTER;
            }
        }
        //position == mData.size() + getHeaderViewsCount()
        else if (position == mData.size() + getHeaderViewCount()) {
            if (mNextLoadingEnable) {
                return VIEW_TYPE_LOADING;
            }
            return VIEW_TYPE_FOOTER;
        }
        //type content
        else if (position - getHeaderViewCount() >= 0) {
            return VIEW_TYPE_CONTENT;
        }
        return super.getItemViewType(position - getHeaderViewCount());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                baseViewHolder = new BaseViewHolder(mHeaderView);
                break;
            default:
            case VIEW_TYPE_CONTENT:
                baseViewHolder = new BaseViewHolder(mLayoutInflater.inflate(mLayoutResId, parent, false));
                initItemClickListener(baseViewHolder);
                break;
            case VIEW_TYPE_FOOTER:
                baseViewHolder = new BaseViewHolder(mFooterView);
                break;
            case VIEW_TYPE_EMPTY:
                baseViewHolder = new BaseViewHolder(mEmptyView);
                break;
            case VIEW_TYPE_LOADING:
                baseViewHolder = addLoadingView(mLoadType);
                break;
        }
        return baseViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mViewType = holder.getItemViewType();
        switch (mViewType) {
            case VIEW_TYPE_HEADER:
                break;
            case VIEW_TYPE_EMPTY:
                break;
            case VIEW_TYPE_FOOTER:
                break;
            default:
            case VIEW_TYPE_CONTENT:
                convert((BaseViewHolder) holder, mData.get(holder.getLayoutPosition() - getHeaderViewCount()));
                // addAnimation(holder, holder.getLayoutPosition());
                break;
            case VIEW_TYPE_LOADING:
                addLoadMore();
                break;
        }
    }

    /**
     * @param baseViewHolder
     */
    private void initItemClickListener(final BaseViewHolder baseViewHolder) {
        if (onRecyclerItemClickListener != null) {
            baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecyclerItemClickListener.onItemClick(v, baseViewHolder.getLayoutPosition() - getHeaderViewCount());
                }
            });
        }
        if (onRecyclerItemLongClickListener != null) {
            baseViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return onRecyclerItemLongClickListener.onItemLongClick(v, baseViewHolder.getLayoutPosition() - getHeaderViewCount());
                }
            });
        }
    }

    /**
     * @param helper
     * @param item
     */
    protected abstract void convert(BaseViewHolder helper, T item);

    /**
     * add animation when you want to show time
     *
     * @param holder
     */
    private void addAnimation(RecyclerView.ViewHolder holder, int position) {
        if (mOpenAnimationEnable) {
            if (!mFirstOnlyAnimationEnable || holder.getLayoutPosition() > mLastPosition) {
                if (mBaseAnimation.length == 1) {// one kind animation
                    for (Animator anim : mBaseAnimation[0].getAnimators(holder.itemView)) {
                        startAnim(anim);
                    }
                } else {   // more kind animation
                    if (position % 2 == 0) {
                        for (Animator anim : mBaseAnimation[0].getAnimators(holder.itemView)) {
                            startAnim(anim);
                        }
                    } else {
                        for (Animator anim : mBaseAnimation[1].getAnimators(holder.itemView)) {
                            startAnim(anim);
                        }
                    }
                }
                mLastPosition = holder.getLayoutPosition();
            }
        }
    }

    /**
     * set anim to start when loading
     *
     * @param anim
     */
    protected void startAnim(Animator anim) {
        anim.setInterpolator(mInterpolator);
        anim.setDuration(mDuration).start();
    }

    /**
     * set the itemView animation
     *
     * @param animationType One of {ALPHA}, {SCALE}, {SLIDE_LEFT}, {SLIDE_RIGHT}, {SLIDE_BOTTOM},{SLIDE_TOP}.
     */
    public void openLoadAnimation(AnimationType animationType) {
        this.mOpenAnimationEnable = true;
        mBaseAnimation = null;
        switch (animationType) {
            case CUSTOM:
                mBaseAnimation = new BaseAnimation[]{new CustomAnimation()};
                break;
            case ALPHA:
                mBaseAnimation = new BaseAnimation[]{new AlphaInAnimation()};
                break;
            case SCALE:
                mBaseAnimation = new BaseAnimation[]{new ScaleInAnimation()};
                break;
            case SLIDE_LEFT:
                mBaseAnimation = new BaseAnimation[]{new SlideInLeftAnimation()};
                break;
            case SLIDE_RIGHT:
                mBaseAnimation = new BaseAnimation[]{new SlideInRightAnimation()};
                break;
            case SLIDE_BOTTOM:
                mBaseAnimation = new BaseAnimation[]{new SlideInBottomAnimation()};
                break;
            case SLIDE_TOP:
                mBaseAnimation = new BaseAnimation[]{new SlideInTopAnimation()};
                break;
            case SLIDE_LEFT_RIGHT:
                mBaseAnimation = new BaseAnimation[]{new SlideInLeftAnimation(), new SlideInRightAnimation()};
                break;
            case SLIDE_BOTTOM_TOP:
                mBaseAnimation = new BaseAnimation[]{new SlideInBottomAnimation(), new SlideInTopAnimation()};
                break;
            default:
                break;
        }
    }


    /**
     * @param animation
     */
    public void openLoadAnimation(BaseAnimation[] animation) {
        this.mBaseAnimation = animation;
        this.mOpenAnimationEnable = true;
    }

    /**
     * open the animation
     */
    public void openLoadAnimation() {
        openLoadAnimation(true);
    }

    /**
     * @param openAnimationEnable
     */
    public void openLoadAnimation(boolean openAnimationEnable) {
        this.mOpenAnimationEnable = openAnimationEnable;
    }

    /**
     * @param enable
     */
    public void openLoadingMore(boolean enable) {
        this.mNextLoadingEnable = enable;
    }

    /**
     * @param pageSize
     * @param enable
     */
    public void openLoadingMore(int pageSize, boolean enable) {
        this.pageSize = pageSize;
        this.mNextLoadingEnable = enable;
    }

    /**
     * add loadMore interface
     */
    public void addLoadMore() {
        if (isLoadMore() && !mLoadingMoreEnable) {
            mLoadingMoreEnable = true;
            mRequestLoadMoreListener.onLoadMoreRequested();
        }
    }

    /**
     * is loaded more
     */
    private boolean isLoadMore() {
        return mNextLoadingEnable && mRequestLoadMoreListener != null && mData.size() > 0;
    }

    /**
     * @param firstOnlyAnimation
     */
    public void isFirstOnlyAnimation(boolean firstOnlyAnimation) {
        this.mFirstOnlyAnimationEnable = firstOnlyAnimation;
    }

    /**
     * @param position
     */
    public void remove(int position) {
        mData.remove(position);
        notifyItemRemoved(position + getHeaderViewCount());
    }

    /**
     * @param position
     * @param item
     */
    public void add(int position, T item) {
        mData.add(position, item);
        notifyItemInserted(position);
    }

    /**
     * @param datas
     */
    public void addAll(List<T> datas) {
        if (datas != null) {
            mData.addAll(datas);
            notifyDataSetChanged();
        }
    }

    /**
     * @param item
     */
    public void add(T item) {
        add(mData.size(), item);
    }

    /**
     * @param data
     */
    public void setData(List<T> data) {
        this.mData = data;
        if (mRequestLoadMoreListener != null) {
            mNextLoadingEnable = true;
            mFooterView = null;
        }
        mLastPosition = -1;
        notifyDataSetChanged();
    }

    /**
     * @param data
     */
    public void addData(List<T> data) {
        this.mData.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * @return
     */
    public List getData() {
        return mData;
    }

    /**
     * @param duration
     */
    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    /**
     * @return
     */
    public int getHeaderViewCount() {
        return mHeaderView == null ? 0 : 1;
    }

    /**
     * @return
     */
    public int getFooterViewCount() {
        return mFooterView == null ? 0 : 1;
    }

    /**
     * @return
     */
    public int getEmptyViewCount() {
        return mEmptyView == null ? 0 : 1;
    }


    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int type = holder.getItemViewType();
        if (type == VIEW_TYPE_EMPTY || type == VIEW_TYPE_HEADER || type == VIEW_TYPE_FOOTER
                || type == VIEW_TYPE_LOADING) {
            setFullSpan(holder);
        } else {
            addAnimation(holder, holder.getLayoutPosition());
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    return (type == VIEW_TYPE_EMPTY || type == VIEW_TYPE_HEADER || type == VIEW_TYPE_FOOTER
                            || type == VIEW_TYPE_LOADING) ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }


    /**
     * @param holder
     */
    protected void setFullSpan(RecyclerView.ViewHolder holder) {
        if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            params.setFullSpan(true);
        }
    }

    /**
     * @param header
     */
    public void addHeaderView(View header) {
        this.mHeaderView = header;
        this.notifyDataSetChanged();
    }


    /**
     * @param footer
     */
    public void addFooterView(View footer) {
        mNextLoadingEnable = false;
        this.mFooterView = footer;
        this.notifyDataSetChanged();
    }

    /**
     * add no more data view
     */
    public void addNoMoreView() {
        mNextLoadingEnable = false;
        mFooterView = new FooterView(mContext);
        ((FooterView) mFooterView).setNoMoreView();
        this.notifyDataSetChanged();
    }


    /**
     * @param emptyView
     */
    public void addEmptyView(View emptyView) {
        addEmptyView(false, false, emptyView);
    }

    /**
     * @param isHeadAndEmpty
     * @param emptyView
     */
    public void addEmpty(boolean isHeadAndEmpty, View emptyView) {
        addEmptyView(isHeadAndEmpty, false, emptyView);
    }

    /**
     * @param isHeadAndEmpty
     * @param isFootAndEmpty
     * @param emptyView
     */
    public void addEmptyView(boolean isHeadAndEmpty, boolean isFootAndEmpty, View emptyView) {
        mHeadAndEmptyEnable = isHeadAndEmpty;
        mFootAndEmptyEnable = isFootAndEmpty;
        mEmptyView = emptyView;
        mEmptyEnable = true;
    }

    /**
     * add a loadingView
     *
     * @param loadingView
     */
    public void addLoadingView(View loadingView) {
        this.mLoadView = loadingView;
    }

    /**
     * @param loadType
     * @return
     */
    public BaseViewHolder addLoadingView(LoadType loadType) {
        mLoadView = new FooterView(mContext);
        if (mLoadView instanceof FooterView) {
            ((FooterView) mLoadView).setLoadView(loadType);
        }
        return new BaseViewHolder(mLoadView);
    }

    /**
     * @param loadType
     */
    public void setLoadType(LoadType loadType) {
        this.mLoadType = loadType;
    }

    /**
     * @param isNextLoad
     */
    public void notifyDataChangeAfterLoadMore(boolean isNextLoad) {
        mNextLoadingEnable = isNextLoad;
        mLoadingMoreEnable = false;
        notifyDataSetChanged();
    }

    /**
     * @param data
     * @param isNextLoad
     */
    public void notifyDataChangeAfterLoadMore(List<T> data, boolean isNextLoad) {
        mData.addAll(data);
        notifyDataChangeAfterLoadMore(isNextLoad);
    }


    /**
     * @return
     */
    public View getHeaderView() {
        return mHeaderView;
    }

    /**
     * @return
     */
    public View getFooterView() {
        return mFooterView;
    }

    /**
     * @return
     */
    public View getEmptyView() {
        return mEmptyView;
    }

    /**
     * @param onRecyclerViewItemClickListener
     */
    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerItemClickListener = onRecyclerViewItemClickListener;
    }

    /**
     * @param onRecyclerViewItemLongClickListener
     */
    public void setOnRecyclerItemLongClickListener(OnRecyclerItemLongClickListener onRecyclerViewItemLongClickListener) {
        this.onRecyclerItemLongClickListener = onRecyclerViewItemLongClickListener;
    }

    /**
     * @param requestLoadMoreListener
     */
    public void setOnLoadMoreListener(RequestLoadMoreListener requestLoadMoreListener) {
        this.mRequestLoadMoreListener = requestLoadMoreListener;
    }
}
