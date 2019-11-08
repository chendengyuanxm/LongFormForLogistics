package com.unis.longformforlogistics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.unis.longformforlogistics.jsmodule.impl.JsModuleScan;
import com.unis.longformlib.JsBridge;
import com.unis.longformlib.webview.PageFinishedListener;
import com.unis.longformlib.webview.SelfWebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * Author:Arnold
 * Date:2019/9/16 17:02
 */
public class WebFragment extends Fragment implements PageFinishedListener {
    private Activity context;
    private View mRootView;
    private SelfWebView mWebView;
    private JsBridge jsBridge;

    private String pageId;

    public static WebFragment getFragment() {
        WebFragment fragment = new WebFragment();
        Bundle bundle = new Bundle();
//        bundle.putString("page", pageId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (Activity) context;
//        this.pageId = getArguments().getString("page");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.activity_webview, container, false);
        init();
        return mRootView;
    }

    private void init() {
        jsBridge = new JsBridge(this);
        mWebView = new SelfWebView(context);
        mWebView.init(jsBridge);
        mWebView.setPageFinishedListener(this);
        mWebView.addJsModule(new JsModuleScan());

        FrameLayout webContainer = mRootView.findViewById(R.id.container);
        webContainer.addView(mWebView);

        mWebView.loadUrl("file:///android_asset/www/index.html");
//        mWebView.loadUrl("http://192.168.199.88:8024");
    }

    private void callJs(String js) {
        final int version = Build.VERSION.SDK_INT;
        if (version < 18) {
            mWebView.loadUrl(js);
        } else {
            mWebView.evaluateJavascript(js, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    //此处为 js 返回的结果
                    Log.d("Devin", value);
                }
            });
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        Log.d("devin", "onPageFinished");
//        loadPage();
    }

    private void loadPage() {
        mWebView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pageId != null && !"".equals(pageId)) {
                    String js = String.format("javascript:app.renderWebPage('%s','%s', '%s','%s', {'username':'longform'})",
                            App.mAppKey,
                            pageId,
                            App.mRole,
                            App.mToken
                    );
                    Log.e("Devin", js);
                    callJs(js);
                }
            }
        }, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        jsBridge.onActivityResult(requestCode, resultCode, data);
    }
}
