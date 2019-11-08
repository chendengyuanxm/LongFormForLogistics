package com.unis.longformforlogistics.jsmodule.impl;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.unis.longformforlogistics.jsmodule.IScanModule;
import com.unis.longformforlogistics.model.GetBarcodeTypeResponseEntity;
import com.unis.longformforlogistics.model.ScanRequestEntity;
import com.unis.longformforlogistics.model.ScanResponseEntity;
import com.unis.longformlib.AbstractJsModule;
import com.unis.longformlib.CallbackContext;
import com.unis.longformlib.utils.GsonUtil;

import org.yaml.snakeyaml.scanner.Constant;

import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Author:Arnold
 * Date:2019/9/4 14:17
 */
public class JsModuleScan extends AbstractJsModule implements IScanModule {
    private static final String TAG = JsModuleScan.class.getSimpleName();
    private CallbackContext callbackContext;

    public JsModuleScan(){}

    @Override
    public void scan(ScanRequestEntity req, CallbackContext callbackContext) {
        Log.i(TAG, "scan barcode, args = " + req);
        this.callbackContext = callbackContext;
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
