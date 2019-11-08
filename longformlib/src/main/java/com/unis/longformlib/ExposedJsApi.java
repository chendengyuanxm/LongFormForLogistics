package com.unis.longformlib;

import android.webkit.JavascriptInterface;

/**
 * Author:Arnold
 * Date:2019/10/9 17:18
 */
public class ExposedJsApi {
    private JsModuleManager jsModuleManager;

    public ExposedJsApi(JsModuleManager jsModuleManager) {
        this.jsModuleManager = jsModuleManager;
    }

    @JavascriptInterface
    public boolean exec(String path, String callback, String serviceId, String args) {
        return jsModuleManager.exec(path, args, callback, serviceId);
    }

}
