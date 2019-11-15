package com.unis.longformforlogistics.jsmodule.impl;

import android.content.Intent;
import android.util.Log;

import com.amap.api.maps.model.LatLng;
import com.unis.longformforlogistics.jsmodule.IScanModule;
import com.unis.longformforlogistics.map.AMapActivity;
import com.unis.longformforlogistics.model.GetBarcodeTypeResponseEntity;
import com.unis.longformforlogistics.model.ScanRequestEntity;
import com.unis.longformlib.AbstractJsModule;
import com.unis.longformlib.CallbackContext;
import com.unis.longformlib.utils.GsonUtil;

//import com.unis.longformforlogistics.MyFlutterActivity;

/**
 * Author:Arnold
 * Date:2019/9/4 14:17
 */
public class JsModuleScan extends AbstractJsModule implements IScanModule {
    private static final String TAG = JsModuleScan.class.getSimpleName();
    private CallbackContext callbackContext;

    public JsModuleScan(){}

    LatLng p1 = new LatLng(39.993266, 116.473193);//首开广场
    LatLng p2 = new LatLng(39.917337, 116.397056);//故宫博物院
    LatLng p3 = new LatLng(39.904556, 116.427231);//北京站
    LatLng p4 = new LatLng(39.773801, 116.368984);//新三余公园(南5环)
    LatLng p5 = new LatLng(40.041986, 116.414496);//立水桥(北5环)

    @Override
    public void scan(ScanRequestEntity req, CallbackContext callbackContext) {
        Log.i(TAG, "scan barcode, args = " + req);
        this.callbackContext = callbackContext;
//        Intent intent = new Intent(context, MyFlutterActivity.class);
//        context.startActivity(intent);

        Intent intent = new Intent(context, AMapActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void getBarcodeType(CallbackContext callbackContext) {
        String result = GsonUtil.toJson(new GetBarcodeTypeResponseEntity("LP"));
        callbackContext.success(result);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }
}
