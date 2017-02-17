package com.example.ecrbtb.mvp.search;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ecrbtb.BaseFragment;
import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.R;
import com.example.ecrbtb.event.SearchEvent;
import com.example.ecrbtb.utils.KeyBoardUtil;
import com.example.ecrbtb.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by boby on 2016/12/18.
 */

public class SearchFragment extends BaseFragment {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.et_search)
    EditText mEtSearch;

    public static SearchFragment newInstance() {
        Bundle args = new Bundle();

        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        registerEventBus();

        mToolbar.setTitle("搜索商品");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtil.getInstance(_mActivity).hide();
                pop();
            }
        });

        mEtSearch.postDelayed(new Runnable() {
            @Override
            public void run() {
                KeyBoardUtil.getInstance(_mActivity).openKeyboard(mEtSearch, false);
            }
        }, 500);

        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    searchKey();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getResourceId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initInstanceState(Bundle savedInstanceState) {
        super.initInstanceState(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        unregisterEventBus();
    }

    @OnClick(R.id.btn_search)
    public void onClick() {
        searchKey();
    }

    private void searchKey() {
        String search = mEtSearch.getText().toString();
        if (!StringUtils.isEmpty(search)) {
            KeyBoardUtil.getInstance(_mActivity).hide();
            EventBus.getDefault().post(new SearchEvent(search));
            pop();
        } else {
            showToast("输入的内容不能为空!");
        }
    }
}
