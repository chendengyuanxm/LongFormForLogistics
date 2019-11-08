package com.unis.longformforlogistics;

import android.app.Application;

/**
 * Author:Arnold
 * Date:2019/9/16 18:13
 */
public class App extends Application {
    private static App mApplication = null;
    public static String mAppKey = "42b99285d25a4dc9bd0d28306f67b406";
    public static String mRole = "admin";
    public static String mToken = "dce422af-8530-421d-bbc5-bc1e1a0f7a9c";

    public static App getApplication() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }
}
