package com.example.ecrbtb.mvp.home.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.ecrbtb.BasePageFragment;
import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.R;
import com.example.ecrbtb.config.Constants;
import com.example.ecrbtb.mvp.category.bean.Product;
import com.example.ecrbtb.mvp.detail.DetailActivity;
import com.example.ecrbtb.mvp.home.presenter.HomePresenter;
import com.example.ecrbtb.mvp.home.view.IHomeView;
import com.example.ecrbtb.widget.PageStateLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.InjectView;

import static com.example.ecrbtb.R.id.fab;

/**
 * Created by boby on 2016/12/14.
 */

public class FirstHomeFragment extends BasePageFragment implements IHomeView {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.web_view)
    WebView mWebView;
    @InjectView(fab)
    FloatingActionButton mFab;

    private boolean mAddJsCodeEnable = true;

    private HomePresenter mHomePresenter;

    public static FirstHomeFragment newInstance() {

        Bundle args = new Bundle();

        FirstHomeFragment fragment = new FirstHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        //registerEventBus();
        showPageState(PageStateLayout.LOADING_STATE);

        mToolbar.setTitle("首页");

        List<String> cookies = new ArrayList<>();
        cookies.add("StoreManagerTicket=" + mHomePresenter.getToken());

        syncCookieToWebView(Constants.PAY_BASE_URL + Constants.HOME_URL, cookies);

        HashMap<String, String> header = new HashMap<>();
        header.put("ECR-APP", "android");

        mWebView.loadUrl(Constants.PAY_BASE_URL + Constants.HOME_URL, header);

        WebSettings settings = mWebView.getSettings();

        // 设置是够支持js脚本
        settings.setJavaScriptEnabled(true);
        // 设置是否支持画面缩放
        settings.setBuiltInZoomControls(true);

        settings.setSupportZoom(true);
        // 设置是否显示缩放器
        settings.setDisplayZoomControls(false);
        //  设置字体的大小
        settings.setTextZoom(120);

        //优先使用缓存:
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (mAddJsCodeEnable) {
                    mAddJsCodeEnable = false;
                    showPageState(PageStateLayout.NORMAL_STATE);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("Supplier/Product/Product.aspx")) {
                    if (url.contains("pid=") && url.contains("sid=")) {
                        Product product = new Product();
                        product.ProductId = Integer.parseInt(url.substring(url.indexOf("pid=") + 4, url.indexOf("&")));
                        product.SupplierId = Integer.parseInt(url.substring(url.indexOf("sid=") + 4, url.length()));

                        Intent intent = new Intent(_mActivity, DetailActivity.class);
                        intent.putExtra(DetailActivity.PRODUCT_DATA, product);
                        _mActivity.startActivity(intent);
                        return true;
                    }
                }
                return false;
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebView.canGoBack()) {
                    showToast("返回上一页");
                    mWebView.goBack();
                }
            }
        });

    }

    @Override
    protected BasePresenter initPresenter() {
        return mHomePresenter = new HomePresenter(this);
    }

    @Override
    public int getResourceId() {
        return R.layout.fragment_home_first;
    }

    @Override
    protected int getRootView() {
        return R.id.fl_container;
    }

//    @Subscribe(threadMode = ThreadMode.MAIN, priority = 0)
//    public void onReceiveMessage(@NonNull StopLoadEvent event) {
//        showPageState(PageStateLayout.NORMAL_STATE);
//    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        unregisterEventBus();
//    }

    /**
     * cookie同步
     */
    @SuppressWarnings("deprecation")
    private void syncCookieToWebView(String url, List<String> cookies) {
        CookieSyncManager.createInstance(_mActivity);
        CookieManager cm = CookieManager.getInstance();
        cm.setAcceptCookie(true);
        if (cookies != null) {
            for (String cookie : cookies) {
                cm.setCookie(url, cookie);//注意端口号和域名，这种方式可以同步所有cookie，包括sessionid
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager.getInstance().sync();
        }
    }

    @Override
    public Context getMainContext() {
        return null;
    }
}
