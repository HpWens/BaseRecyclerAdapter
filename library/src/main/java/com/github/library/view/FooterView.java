package com.github.library.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.library.R;

/**
 * Created by Administrator on 7/25 0025.
 */
public class FooterView extends LinearLayout {

    private Context mContext;
    private int mPadding;

    private View mLoadView;
    private TextView mTextView;

    private LayoutParams mLayoutParams;

    private int mLoadType = -1;

    private static final int LOAD_TYPE_CUSTOM = 0x00000001;
    private static final int LOAD_TYPE_CUBES = 0x00000002;
    private static final int LOAD_TYPE_SWAP = 0x00000003;
    private static final int LOAD_TYPE_EAT_BEANS = 0x00000004;
    private static final int NO_MORE = 0x00000005;

    public FooterView(Context context) {
        this(context, null);
    }

    public FooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        this.mContext = context;
        this.mPadding = dip2px(8);
    }

    public void addViews() {
        this.removeAllViews();

        mTextView = new TextView(mContext);
        mLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = dip2px(8);
        mTextView.setLayoutParams(mLayoutParams);
        mTextView.setText(R.string.loading);

        switch (mLoadType) {
            case LOAD_TYPE_CUBES:
                mLoadView.setLayoutParams(new LayoutParams(dip2px(48), dip2px(48)));
                addView(mLoadView);
                addView(mTextView);
                break;
            default:
            case LOAD_TYPE_CUSTOM:
                setPadding(0, mPadding, 0, mPadding);
                mLoadView.setLayoutParams(new LayoutParams(dip2px(36), dip2px(36)));
                addView(mLoadView);
                addView(mTextView);
                break;
            case LOAD_TYPE_SWAP:
                mLoadView.setLayoutParams(new LayoutParams(dip2px(100), dip2px(100)));
                addView(mLoadView);
                //addView(mTextView);
                break;
            case LOAD_TYPE_EAT_BEANS:
                mLoadView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, dip2px(48)));
                addView(mLoadView);
                //addView(mTextView);
                break;
            case NO_MORE:
                mTextView.setText(R.string.no_more);
                addView(mTextView);
                break;
        }

    }

    /**
     * @param loadView
     */
    public void setLoadView(LoadType loadView) {
        switch (loadView) {
            case CUBES:
                mLoadType = LOAD_TYPE_CUBES;
                mLoadView = new CubesLoadView(mContext);
                break;
            default:
            case CUSTOM:
                mLoadType = LOAD_TYPE_CUSTOM;
                mLoadView = new ProgressBar(mContext);
                break;
            case SWAP:
                mLoadType = LOAD_TYPE_SWAP;
                mLoadView = new SwapLoadView(mContext);
                break;
            case EAT_BEANS:
                mLoadType = LOAD_TYPE_EAT_BEANS;
                mLoadView = new EatBeansLoadView(mContext);
                break;
        }
        // add views
        addViews();
        // start anim
        startAnimator();
    }

    /**
     * set no more view
     */
    public void setNoMoreView() {
        mLoadType = NO_MORE;
        addViews();
    }

    public void startAnimator() {
        switch (mLoadType) {
            case LOAD_TYPE_CUBES:
                if (mLoadView instanceof CubesLoadView) {
                    ((CubesLoadView) mLoadView).startAnimator();
                }
                break;
            default:
            case LOAD_TYPE_CUSTOM:
                break;
            case LOAD_TYPE_SWAP:
                if (mLoadView instanceof SwapLoadView) {
                    ((SwapLoadView) mLoadView).startAnimator();
                }
                break;
            case LOAD_TYPE_EAT_BEANS:
                if (mLoadView instanceof EatBeansLoadView) {
                    ((EatBeansLoadView) mLoadView).startAnimator();
                }
                break;
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
