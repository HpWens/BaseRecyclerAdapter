package com.example.ecrbtb.mvp.merchant;

import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.ecrbtb.BaseActivity;
import com.example.ecrbtb.BasePresenter;
import com.example.ecrbtb.MyApplication;
import com.example.ecrbtb.R;
import com.example.ecrbtb.config.Constants;
import com.example.ecrbtb.mvp.merchant.presenter.MerchantPresenter;
import com.example.ecrbtb.utils.StringUtils;
import com.grasp.tint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.InjectView;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * Created by boby on 2016/12/30.
 */

public class MerchantWebActivity extends BaseActivity {


    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @InjectView(R.id.web_view)
    WebView mWebView;

    private String mLoadUrl;

    private MerchantPresenter mPresenter;

    @Override
    protected void initView(ViewDataBinding bind) {

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    finish();
                }
            }
        });

        List<String> cookies = new ArrayList<>();
        cookies.add("StoreManagerTicket=" + mPresenter.getToken());

        syncCookieToWebView(mLoadUrl, cookies);

        HashMap<String, String> header = new HashMap<>();
        header.put("ECR-APP", "android");

        mWebView.loadUrl(mLoadUrl, header);
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
        //不使用缓存:
        //settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                        String mimetype, long contentLength) {

                //第一种下载方式是 自定义的http工具类
//                new HttpDownloadThread(url,contentDisposition,mimetype,contentLength).start();
                //第二种下载方式是调用系统的webView,具有默认的进度条
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                WebResourceResponse response = null;
//                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mWebView.getLayoutParams();
//                if (url.contains("head-back")) {
//                    try {
//                        lp.topMargin = 0 - mPresenter.dip2px(MerchantWebActivity.this
//                                , 40);
//                        InputStream logo = getAssets().open("logo.png");
//                        response = new WebResourceResponse("image/png", "UTF-8", logo);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
                return response;
            }

            //重写这个方法 返回true，在当前 webView 打开，否则在浏览器中打开
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(mLoadUrl);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);


            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request,
                                            WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    int statusCode = errorResponse.getStatusCode();
                }
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String
                    failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                //  没有网络连接
                if (false == MyApplication.getInstance().isConnected()) {
                    MyApplication.getInstance().showWifiDlg(MerchantWebActivity.this);
                } else {
                    if (errorCode == 404) {
                        //用javascript隐藏系统定义的404页面信息
                        String data = "Page NO FOUND！";
                        view.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");
                        mWebView.setVisibility(View.INVISIBLE);
                    } else {//其他状态码错误的处理，这里就不罗列出来了

                    }
                }

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {
                super.onReceivedError(view, request, error);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    int errorCode = error.getErrorCode();
                    CharSequence description = error.getDescription();
                }


            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                //                LUtils.i("newProgress=" + newProgress);
                if (newProgress != 100) {
                    mProgressBar.setProgress(newProgress);
                } else {
                    mProgressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mToolbar.setTitle(title);
            }
        });
    }

    @Override
    protected void initBarTint() {
        super.initBarTint();
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
    }

    @Override
    protected void initIntent(Intent intent) {
        super.initIntent(intent);
        if (intent != null) {
            mLoadUrl = intent.getStringExtra(Constants.MERCHANT_URL);
            if (StringUtils.isEmpty(mLoadUrl))
                mLoadUrl = Constants.BASE_URL;
        }
    }

    /**
     * 监听按下返回键
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected BasePresenter initPresenter() {
        return mPresenter = new MerchantPresenter(this);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_merchant_web;
    }

    @Override
    protected int getRootView() {
        return R.id.fl_root;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.destroy();

    }

    /**
     * cookie同步
     */
    @SuppressWarnings("deprecation")
    private void syncCookieToWebView(String url, List<String> cookies) {
        CookieSyncManager.createInstance(this);
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

    @SuppressWarnings("deprecation")
    public static void loadCookies() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager.getInstance().sync();
        }
    }

    @SuppressWarnings("deprecation")
    public void clearCookies(Context context) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeAllCookies(null);
        } else {
            cookieManager.removeAllCookie();
        }
    }

}
