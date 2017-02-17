package com.example.ecrbtb.mvp.login;

import android.databinding.ViewDataBinding;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.PermissionsActivity;
import com.example.ecrbtb.R;
import com.example.ecrbtb.event.LoginSuccessEvent;
import com.example.ecrbtb.mvp.login.bean.LoginState;
import com.example.ecrbtb.mvp.login.presenter.UserLoginPresenter;
import com.example.ecrbtb.mvp.login.view.IUserLoginView;
import com.example.ecrbtb.widget.PageStateLayout;

import org.greenrobot.eventbus.EventBus;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by boby on 2016/12/13.
 */

public class LoginActivity extends PermissionsActivity implements IUserLoginView {

    @InjectView(R.id.et_code)
    EditText mEtCode;
    @InjectView(R.id.et_account)
    EditText mEtAccount;
    @InjectView(R.id.et_password)
    EditText mEtPassword;
    @InjectView(R.id.btn_login)
    Button mBtnLogin;
    @InjectView(R.id.tv_what_code)
    TextView mTvWhatCode;
    @InjectView(R.id.rl_root)
    RelativeLayout mRlRoot;

    private UserLoginPresenter mPresenter;

    @Override
    protected void initData() {
        super.initData();
        //隐藏软键盘
        hideKeyBoard();
    }

    @Override
    protected void initView(ViewDataBinding bind) {

    }

    @Override
    protected BasePresenter initPresenter() {
        return mPresenter = new UserLoginPresenter(mContext, this);
    }

    @Override
    protected void retryLoading() {
        super.retryLoading();
        mPresenter.retryLoading();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected int getRootView() {
        return R.id.rl_root;
    }

    private void hideKeyBoard() {
        mPresenter.hideKeyBoard(this, mEtCode);
    }


    @Override
    public void requestPermissionResult(boolean allowPermission) {

    }

    @OnClick({R.id.btn_login, R.id.tv_what_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                mPresenter.login(mRlRoot);
                break;
            case R.id.tv_what_code:
                mPresenter.showWhatCodeDialog(mContext);
                break;
        }
    }

    @Override
    public String getUserName() {
        return mEtAccount.getText().toString();
    }

    @Override
    public String getPassword() {
        return mEtPassword.getText().toString();
    }

    @Override
    public String getStoreCode() {
        return mEtCode.getText().toString();
    }

    @Override
    public void showPageLoad() {
        showPageState(PageStateLayout.LOADING_STATE);
    }

    @Override
    public void showPageNormal() {
        showPageState(PageStateLayout.NORMAL_STATE);
    }


    @Override
    public void loginSuccess(LoginState loginState) {
        if (loginState.state == 0) {
            showToast(loginState.msg);
            return;
        }
        EventBus.getDefault().post(new LoginSuccessEvent());
        finish();
    }

    @Override
    public void loginFailed() {
        showToast(this.getString(R.string.login_failed));
    }

    @Override
    public void showPageError() {
        showPageState(PageStateLayout.ERROR_STATE);
    }

    @Override
    public void showPasswordFormatError() {
        showToast(this.getString(R.string.password_alert));
    }
}
