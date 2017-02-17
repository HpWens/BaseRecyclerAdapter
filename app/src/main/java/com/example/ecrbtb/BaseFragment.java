package com.example.ecrbtb;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ecrbtb.event.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by boby on 2016/12/10.
 */

public abstract class BaseFragment<V extends ViewDataBinding, P extends BasePresenter> extends SupportFragment {

    protected String TAG;

    protected V mBinding;

    protected P mPresenter;

    protected SweetAlertDialog mDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.TAG = this.getClass().getSimpleName();
        if (getResourceId() != 0) {
            View view = inflater.inflate(getResourceId(), container, false);
            ButterKnife.inject(this, view);
            initPageStateLayout(view);
            //mBinding = DataBindingUtil.inflate(inflater, getResourceId(), container, false);
            initView(view);
            initInstanceState(savedInstanceState);
            initData();
            return view;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = initPresenter();
    }

    //初始化 presenter
    protected abstract P initPresenter();

    //获取布局资源ID
    public abstract int getResourceId();

    //初始化数据
    protected void initView(View view) {

    }

    //添加不同的页面布局
    protected void initPageStateLayout(View view) {

    }


    //初始化保存的状态
    protected void initInstanceState(Bundle savedInstanceState) {

    }

    //初始化数据
    protected void initData() {

    }

    @Override
    public void onStart() {
        super.onStart();
        //registerEventBus();
    }

    protected void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    protected void unregisterEventBus() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        //EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.MAIN, priority = 0)
//    public void onReceiveMessage(@NonNull MessageEvent event) {
//        //Toast.makeText(_mActivity, event.message, Toast.LENGTH_SHORT).show();
//    }


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
//    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN, priority = 1)
//    public void onReceiveStickyMessage(@NonNull MessageEvent event) {
//
//    }

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

    protected void showToast(int resId) {
        Toast.makeText(_mActivity, resId, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(CharSequence text) {
        Toast.makeText(_mActivity, text, Toast.LENGTH_SHORT).show();
    }


    /**
     * 显示加载load
     */
    public void showSweetAlertDialog(String titleText) {
        if (mDialog == null) {
            mDialog = new SweetAlertDialog(_mActivity, SweetAlertDialog.PROGRESS_TYPE);
        }
        mDialog.setTitleText(titleText);
        mDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        mDialog.show();
        mDialog.setCancelable(true);
    }

    /**
     * 隐藏 Dialog
     */
    public void dismissSweetAlertDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

}
