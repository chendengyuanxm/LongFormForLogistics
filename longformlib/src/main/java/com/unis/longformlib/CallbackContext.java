package com.unis.longformlib;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.unis.longformlib.webview.SelfWebView;


/**
 * Author:Arnold
 * Date:2019/10/12 10:20
 */
public class CallbackContext {

    private String callbackName;
    private String serviceId;

    private SelfWebView mWebView;
    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    public CallbackContext(SelfWebView webView, String callbackName, String serviceId) {
        this.mWebView = webView;
        this.callbackName = callbackName;
        this.serviceId = serviceId;
    }

    public void sendResult(final boolean sucess, final String resultJson) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                String js = String.format("javascript:%s('%s', '%s', %b', '%s')",
                        callbackName,
                        serviceId,
                        sucess,
                        resultJson
                );
                Log.d("devin", js);
                mWebView.evaluateJavascript(js);
            }
        });
    }

    public void success(String result) {
        sendResult(true, result);
    }

    public void error(String error) {
        sendResult(false, error);
    }
}
