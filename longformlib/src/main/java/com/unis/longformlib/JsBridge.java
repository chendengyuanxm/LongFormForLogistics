package com.unis.longformlib;

import android.content.Intent;

import androidx.fragment.app.Fragment;

/**
 * Author:Arnold
 * Date:2019/10/10 16:45
 */
public class JsBridge implements JsBridgeInterface {
    protected Fragment activity;
    private AbstractJsModule activityResultCallback;

    public JsBridge(Fragment activity) {
        this.activity = activity;
    }

    @Override
    public void startActivityForResult(AbstractJsModule module, Intent intent, int requestCode) {
        setActivityResultCallback(module);
        try {
            activity.startActivityForResult(intent, requestCode);
        } catch (RuntimeException e) {
            activityResultCallback = null;
            throw e;
        }
    }

    @Override
    public void setActivityResultCallback(AbstractJsModule module) {
        this.activityResultCallback = module;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (activityResultCallback != null) {
            activityResultCallback.onActivityResult(requestCode, resultCode, data);
        }
    }
}
