package com.unis.longformlib;

import android.content.Intent;


/**
 * Author:Arnold
 * Date:2019/10/10 11:14
 */
public interface JsBridgeInterface {

    void startActivityForResult(AbstractJsModule module, Intent intent, int requestCode);

    void setActivityResultCallback(AbstractJsModule module);

    void onActivityResult(int requestCode, int resultCode, Intent data);
}
