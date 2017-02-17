package com.example.ecrbtb.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ecrbtb.R;
import com.example.ecrbtb.annotation.PageState;

import static com.example.ecrbtb.R.id.page_bt;

/**
 * Created by boby on 2016/12/12.
 */

public class PageStateLayout extends FrameLayout {

    private Context mContext;

    public static final int NORMAL_STATE = 0;
    public static final int LOADING_STATE = 1;
    public static final int ERROR_STATE = 2;
    public static final int EMPTY_STATE = 3;

    private ImageView mIvEmpty;
    private TextView mTvEmpty;

    private Button mBtnError;
    private TextView mTvError;
    private ImageView mIvError;

    private ProgressBar mIvLoad;
    private TextView mTvLoad;

    private int mEmptyIconId;
    private String mEmptyText = "数据为空";

    private int mErrorIconId;
    private String mErrorText = "点击重试";

    private View mLoadingView;//正在加载中..的界面
    private View mErrorView; //错误界面
    private View mEmptyView;//空界面

    private int mState;

    private OnClickListener mRetryListener;

    public PageStateLayout(Context context) {
        this(context, null);
    }

    public PageStateLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageStateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        initAttr(attrs);
        initView();
    }

    private void initView() {
        removeAllViews();

        mErrorView = createErrorView();
        mLoadingView = createLoadingView();
        mEmptyView = createEmptyView();

        add(mErrorView);
        add(mLoadingView);
        add(mEmptyView);

        show(NORMAL_STATE);
    }

    private void add(View view) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, LayoutParams
                .MATCH_PARENT);
        addView(view, layoutParams);
    }

    private void init(Context context) {
        mContext = context;
        mState = NORMAL_STATE;
    }

    /**
     * hide view
     */
    private void hide() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            view.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * remove views
     */
    public void removeViews() {
        ViewGroup parent = (ViewGroup) getChildAt(0).getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
    }

    // 根据不同的状态显示不同的界面
    public void show(@PageState int pageState) {
        mState = pageState;
        if (mState == NORMAL_STATE) {
            hide();
        } else {
            mLoadingView.setVisibility(mState == LOADING_STATE ? View.VISIBLE : View.INVISIBLE);

//            if (mLoadingView.getVisibility() == View.VISIBLE) {
//                mIvLoad.startAnimator();
//            } else {
//                mIvLoad.endAnimator();
//            }

            mErrorView.setVisibility(mState == ERROR_STATE ? View.VISIBLE
                    : View.INVISIBLE);

            mEmptyView.setVisibility(mState == EMPTY_STATE ? View.VISIBLE
                    : View.INVISIBLE);
        }

    }

    /**
     * @param attrs
     */
    private void initAttr(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.page);
        //mEmptyText = typedArray.getString(R.styleable.page_emptyText);
        mEmptyIconId = typedArray.getResourceId(R.styleable.page_emptyIcon, -1);
        // mErrorText = typedArray.getString(R.styleable.page_errorIcon);
        mErrorIconId = typedArray.getResourceId(R.styleable.page_errorIcon, -1);
        typedArray.recycle();
    }

    /* 创建了空的界面 */
    private View createEmptyView() {
        View view = View.inflate(mContext, R.layout.loadpage_empty,
                null);
        mTvEmpty = (TextView) view.findViewById(R.id.tv_empty);
        mIvEmpty = (ImageView) view.findViewById(R.id.iv_empty);
        if (TextUtils.isEmpty(mEmptyText)) {
            mTvEmpty.setText(mEmptyText);
        }

        if (mEmptyIconId != -1) {
            mIvEmpty.setImageResource(mEmptyIconId);
        }
        return view;
    }

    /* 创建加载中的界面 */
    private View createLoadingView() {
        View view = View.inflate(mContext,
                R.layout.loadpage_loading, null);
        mIvLoad = (ProgressBar) view.findViewById(R.id.load_view);
        mTvLoad = (TextView) view.findViewById(R.id.load_tv);
        return view;
    }

    /* 创建了错误界面 */
    private View createErrorView() {
        View view = View.inflate(mContext, R.layout.loadpage_error,
                null);
        mBtnError = (Button) view.findViewById(page_bt);
        mIvError = (ImageView) view.findViewById(R.id.page_iv);
        if (!TextUtils.isEmpty(mErrorText)) {
            mBtnError.setText(mErrorText);
        }

        if (mErrorIconId != -1) {
            mIvError.setImageResource(mErrorIconId);
        }

        mBtnError.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mRetryListener != null) {
                    mRetryListener.onClick(v);
                }
            }
        });
        return view;
    }


    /**
     * @param text
     */
    public void setEmptyPageText(String text) {
        mTvEmpty.setText(text);
    }

    /**
     * @param onClickListener
     */
    public void setOnRetryListener(OnClickListener onClickListener) {
        mRetryListener = onClickListener;
    }


    public int getState() {
        return mState;
    }
}
