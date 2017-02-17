package com.example.ecrbtb.mvp.merchant.listener;

import android.webkit.JavascriptInterface;

public class JsInterfaceListener {
    JsCallBackListener mListener;

    public JsInterfaceListener(JsCallBackListener listener) {
        this.mListener = listener;
    }

    @JavascriptInterface
    public String JsCallDeviceFunction() {
        return "android";
    }

    @JavascriptInterface
    public void JsOpenScanFunction(String paramString) {
        this.mListener.openScan(paramString);
    }
}