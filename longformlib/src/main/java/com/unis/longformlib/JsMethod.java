package com.unis.longformlib;

import java.lang.reflect.Method;

/**
 * Author:Arnold
 * Date:2019/10/15 16:17
 */
public class JsMethod {

    private AbstractJsModule jsModule;
    private Method method;

    public JsMethod(AbstractJsModule jsModule, Method method) {
        this.jsModule = jsModule;
        this.method = method;
    }

    public boolean exec(String args, String callback, String serviceId) {
        return jsModule.exec(method, args, callback, serviceId);
    }
}
