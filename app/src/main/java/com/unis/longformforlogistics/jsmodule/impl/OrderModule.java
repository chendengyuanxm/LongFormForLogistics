package com.unis.longformforlogistics.jsmodule.impl;

import android.content.Intent;

import com.unis.longformforlogistics.jsmodule.IOrderModule;
import com.unis.longformforlogistics.map.AMapActivity;
import com.unis.longformlib.AbstractJsModule;
import com.unis.longformlib.CallbackContext;

/**
 * Author:Arnold
 * Date:2019/11/15 17:14
 */
public class OrderModule extends AbstractJsModule implements IOrderModule {

    @Override
    public void openMap(CallbackContext callbackContext) {
        Intent intent = new Intent(context, AMapActivity.class);
        context.startActivity(intent);
    }
}
