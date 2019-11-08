package com.unis.longformlib;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.unis.longformlib.utils.GsonUtil;
import com.unis.longformlib.utils.ReflectionUtils;
import com.unis.longformlib.webview.SelfWebView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;

import javax.ws.rs.Path;

/**
 * Author:Arnold
 * Date:2019/9/4 14:19
 */
public abstract class AbstractJsModule {
    protected Context context;
    protected SelfWebView webView;
    protected JsBridge jsBridge;

    public void initModule(Context context, SelfWebView webView, JsBridge jsBridge) {
        this.context = context;
        this.webView = webView;
        this.jsBridge = jsBridge;
    }

    public boolean exec(Method method, String args, String callback, String serviceId) {
        Log.d("Devin", "Javascript[" + serviceId + ", " + callback + ", " + args);
        CallbackContext callbackContext = new CallbackContext(webView, callback, serviceId);

        if (method == null) {
            callbackContext.error("can't find native method");
            return false;
        }

        try {
            if (!"undefined".equals(args)) {
                Class[] clazzes = method.getParameterTypes();
                Object params = GsonUtil.fromJson(args, clazzes[0]);
                method.invoke(this, params, callbackContext);
            } else {
                method.invoke(this, callbackContext);
            }
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            callbackContext.error("exec failed:[" + e.getLocalizedMessage() + "]");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            callbackContext.error("exec failed:[" + e.getLocalizedMessage() + "]");
        }

        return false;
    }

    public LinkedHashMap<String, JsMethod> getAllJsMethods() {
        LinkedHashMap<String, JsMethod> jsMethods = new LinkedHashMap<String, JsMethod>();
        Path modulePath = ReflectionUtils.getAnnotation(getClass(), Path.class);
        if (modulePath == null) return jsMethods;

        Method[] methods = getClass().getMethods();
        for (Method method : methods) {
            Path methodPath = ReflectionUtils.getAnnotation(method, Path.class);
            if (methodPath != null) {
                String key = modulePath.value() + methodPath.value();
                JsMethod jsMethod = new JsMethod(this, method);
                jsMethods.put(key, jsMethod);
            }
        }
        Log.d("Devin", jsMethods.toString());

        return jsMethods;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }
}
