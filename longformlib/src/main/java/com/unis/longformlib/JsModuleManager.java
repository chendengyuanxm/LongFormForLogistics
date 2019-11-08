package com.unis.longformlib;

import android.content.Context;
import android.util.Log;


import com.unis.longformlib.webview.SelfWebView;

import java.util.LinkedHashMap;

/**
 * Author:Arnold
 * Date:2019/10/10 14:36
 */
public class JsModuleManager {
    private final LinkedHashMap<String, JsMethod> methodMap = new LinkedHashMap<String, JsMethod>();

    private Context context;
    private SelfWebView webView;
    private JsBridge jsBridge;

    public JsModuleManager(Context context, SelfWebView webView, JsBridge jsBridge) {
        this.context = context;
        this.webView = webView;
        this.jsBridge = jsBridge;
    }

    public void addModule(String key, AbstractJsModule module) {
        module.initModule(context, webView, jsBridge);
        methodMap.putAll(module.getAllJsMethods());
    }

    private JsMethod getMethod(String key) {
        return methodMap.get(key);
    }

    public boolean exec(String path, String args, String callback, String serviceId) {
        Log.d("Devin", "Javascript[" + path + ", " + serviceId + ", " + callback + ", " + args);
        JsMethod jsMethod = getMethod(path);
        if (jsMethod == null) {
            CallbackContext callbackContext = new CallbackContext(webView, callback, serviceId);
            callbackContext.error("can't find method via path [" + path + "]");
            return false;
        } else {
            return jsMethod.exec(args, callback, serviceId);
        }
    }
}
