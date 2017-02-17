package com.example.ecrbtb;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.ecrbtb.annotation.PageState;
import com.example.ecrbtb.event.MessageEvent;
import com.example.ecrbtb.widget.PageStateLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by boby on 2016/12/9.
 */

public abstract class BaseActivity<T extends ViewDataBinding, E extends BasePresenter> extends SupportActivity {

    protected String TAG;

    private PageStateLayout mPageStateLayout;

    protected Context mContext;

    protected T mBind;

    protected E mPresenter;

    private Parcelable mParcelableExtra;

    protected SweetAlertDialog mDialog;

    public static final String DEFAULT_PARCELABLE_NAME = "DEFAULT_PARCELABLE_NAME";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Explode());
        }

        init();

        initInstanceState(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            addTransitionListener();
        }
    }


    protected void initBarTint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        //改变状态栏的颜色
//        SystemBarTintManager tintManager = new SystemBarTintManager(this);
//        tintManager.setStatusBarTintEnabled(true);
//        tintManager.setStatusBarTintResource(R.color.colorPrimary);
        //initPageStateLayout();
    }

    private void initPageStateLayout() {
        if (mPageStateLayout == null) {
            mPageStateLayout = new PageStateLayout(this);
        }
        if (getRootView() != 0) {
            ViewGroup rootView = (ViewGroup) findViewById(getRootView());
            mPageStateLayout = new PageStateLayout(this);
            rootView.addView(mPageStateLayout, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            mPageStateLayout.setOnRetryListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    retryLoading();
                }
            });
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void addTransitionListener() {
        final Transition transition = getWindow().getSharedElementEnterTransition();

        if (transition != null) {
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionStart(Transition transition) {
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionPause(Transition transition) {
                }

                @Override
                public void onTransitionResume(Transition transition) {
                }
            });
        }
    }

    /**
     * 添加状态布局
     */
    protected void addPageStateLayout(ViewGroup rootView) {
        //只允许添加一次
        if (mPageStateLayout.getParent() == null) {
            rootView.addView(mPageStateLayout, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void init() {
        this.mContext = this;
        this.TAG = this.getClass().getSimpleName();

        Intent intent = getIntent();
        if (intent != null) {
            initIntent(intent);
        }

        mParcelableExtra = intent.getParcelableExtra(DEFAULT_PARCELABLE_NAME);
        if (mParcelableExtra != null) {
            getParcelableExtras(mParcelableExtra);
        }

        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID());
            mPresenter = initPresenter();
            mBind = DataBindingUtil.setContentView(this, getContentViewLayoutID());
            //绑定EventBus
            registerEventBus();
            //初始化ButterKnife
            ButterKnife.inject(this);
            //初始化不同状态显示无网络、加载、空、正常界面
            initPageStateLayout();
            initView(mBind);

            initData();

            initListener();
        } else {
            Log.e(TAG, "onCreate: contentView un exists");
        }

        initBarTint();

    }

    //初始化保存的状态
    protected void initInstanceState(Bundle savedInstanceState) {

    }

    //从新加载界面
    protected void retryLoading() {

    }

    //初始化数据
    protected void initData() {

    }

    //初始化接口
    protected void initListener() {

    }

    //初始化控件
    protected abstract void initView(T bind);

    //初始化 presenter
    protected abstract E initPresenter();

    //获取布局ID
    protected abstract int getContentViewLayoutID();

    //获取布局父控件ID
    protected abstract int getRootView();

    protected void initIntent(Intent intent) {

    }

    protected void getParcelableExtras(Parcelable parcelable) {

    }

    protected void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(CharSequence text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.MAIN, priority = 0)
    public void onReceiveMessage(@NonNull MessageEvent event) {
        Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
    }


    /**
     * 发送消息，用于各个组件之间通信
     *
     * @param event 消息事件对象
     */
    protected void sendMessage(@NonNull MessageEvent event) {
        // 发布EventBus消息事件
        EventBus.getDefault().post(event);
    }

    /**
     * 接收有粘性的消息
     *
     * @param event
     */
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN, priority = 1)
    public void onReceiveStickyMessage(@NonNull MessageEvent event) {

    }

    /**
     * 发送有粘性的消息
     *
     * @param event
     */
    protected void sendStickyMessage(@NonNull MessageEvent event) {
        EventBus.getDefault().postSticky(event);
    }

    /**
     * 移除（消费）sticky事件
     */
    protected void removeStickyEvent() {
        MessageEvent stickyEvent = EventBus.getDefault().removeStickyEvent(MessageEvent.class);
        if (stickyEvent != null) {
        }
    }

    /**
     * 取消事件的传递
     *
     * @param event
     */
    protected void cancelEvent(MessageEvent event) {
        EventBus.getDefault().cancelEventDelivery(event);
    }

    /**
     * @param state 显示不同的界面
     */
    protected void showPageState(@PageState int state) {
        if (mPageStateLayout != null) {
            mPageStateLayout.show(state);
        }
    }

    /**
     * 显示加载load
     */
    public void showSweetAlertDialog(String titleText) {
        mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mDialog.setTitleText(titleText);
        mDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        mDialog.show();
        mDialog.setCancelable(false);
    }

    /**
     * 隐藏 Dialog
     */
    public void dismissSweetAlertDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

}
