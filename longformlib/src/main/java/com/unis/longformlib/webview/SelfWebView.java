/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package com.unis.longformlib.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.unis.longformlib.AbstractJsModule;
import com.unis.longformlib.ExposedJsApi;
import com.unis.longformlib.JsBridge;
import com.unis.longformlib.JsModuleManager;
import com.unis.longformlib.utils.ReflectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import io.swagger.annotations.Api;

@SuppressLint("NewApi")
public class SelfWebView extends WebView {
	private static final String TAG = SelfWebView.class.getSimpleName();

	private Activity context;

	DefaultPageFinishedListener defaultPageFinishedListener = new DefaultPageFinishedListener();

	volatile boolean hasBeenLoad = false;

	private PageFinishedListener pageFinishedListener = null;

	private JsModuleManager jsModuleManager;

	private JsBridge jsBridge;

	public SelfWebView(Activity context) {
		super(context);
		this.context = context;
	}
	
	public SelfWebView(Activity context , AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	private void initSettings() {
		setWebViewClient(new SelfWebViewClient(context, defaultPageFinishedListener));
		setWebChromeClient(new WebChromeClient());

		setInitialScale(0);
		setVerticalScrollBarEnabled(false);
		// Enable JavaScript
		final WebSettings settings = getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);

		//We don't save any form data in the application
		settings.setSaveFormData(false);
		settings.setSavePassword(false);

		// Jellybean rightfully tried to lock this down. Too bad they didn't give us a whitelist
		// while we do this
		settings.setAllowUniversalAccessFromFileURLs(true);
		settings.setMediaPlaybackRequiresUserGesture(false);

		// Enable database
		// We keep this disabled because we use or shim to get around DOM_EXCEPTION_ERROR_16
		String databasePath = context.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
		settings.setDatabaseEnabled(true);
		settings.setDatabasePath(databasePath);


		//Determine whether we're in debug or release mode, and turn on Debugging!
		ApplicationInfo appInfo = context.getApplicationContext().getApplicationInfo();
		if ((appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
			enableRemoteDebugging();
		}

		settings.setGeolocationDatabasePath(databasePath);

		// Enable DOM storage
		settings.setDomStorageEnabled(true);

		// Enable built-in geolocation
		settings.setGeolocationEnabled(true);

		// Enable AppCache
		// Fix for CB-2282
		settings.setAppCacheMaxSize(5 * 1048576);
		settings.setAppCachePath(databasePath);
		settings.setAppCacheEnabled(false);

		addJavascriptInterface(new ExposedJsApi(jsModuleManager), "android");
	}

	private void enableRemoteDebugging() {
		try {
			WebView.setWebContentsDebuggingEnabled(true);
		} catch (IllegalArgumentException e) {
			Log.d(TAG, "You have one job! To turn on Remote Web Debugging! YOU HAVE FAILED! ");
			e.printStackTrace();
		}
	}

	public void init(JsBridge jsBridge){
		if(hasBeenLoad) return;
		hasBeenLoad = true;

		this.jsBridge = jsBridge;
		jsModuleManager = new JsModuleManager(context, this, jsBridge);
		initSettings();
	}

	public void addJsModules(List<AbstractJsModule> modules) {
		for (AbstractJsModule module : modules) {
			addJsModule(module);
		}
	}

	public void addJsModule(AbstractJsModule module) {
		Api api = ReflectionUtils.getAnnotation(module.getClass(), Api.class);
		if (api != null) {
			jsModuleManager.addModule(api.value(), module);
		}
	}

	public void setPageFinishedListener(PageFinishedListener pageFinishedListener) {
		this.pageFinishedListener = pageFinishedListener;
	}

	public void evaluateJavascript(String js) {
		final int version = Build.VERSION.SDK_INT;
		if (version < 18) {
			loadUrl(js);
		} else {
			evaluateJavascript(js, new ValueCallback<String>() {
				@Override
				public void onReceiveValue(String value) {
					//此处为 js 返回的结果
					Log.d("Devin", value);
				}
			});
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event){
		HitTestResult rslt = getHitTestResult();
		if(rslt != null && rslt.getExtra() != null){
			//追加判定
			if(rslt.getType() == HitTestResult.PHONE_TYPE){
				return true;
			}
		}
		return super.onTouchEvent(event);
	}

	void pageFinish(){
		loadUrl("javascript:if(window.onPageFinished != undefined){window.onPageFinished();}");
	}
	
	void pageStart(){
		defaultPageFinishedListener.post(new PageFinishedWorker() {
			@Override
			public void doWhenPageFinished() {
				((Activity)context).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						loadUrl("javascript:if(window.onPageStarted != undefined) {window.onPageStarted()}");
					}
				});
			}
		});
	}
	
	interface PageFinishedWorker{
		public void doWhenPageFinished();
	}
	
	class DefaultPageFinishedListener implements PageFinishedListener{
		Boolean isPageFinished = false;
		LinkedBlockingQueue<PageFinishedWorker> workers = new LinkedBlockingQueue<PageFinishedWorker>();
		
		@Override
		public void onPageFinished(WebView view, String url) {
			if (pageFinishedListener != null) {
				pageFinishedListener.onPageFinished(view, url);
			}

			synchronized (isPageFinished) {
				isPageFinished = true;
				List getAll = new ArrayList();
				workers.drainTo(getAll);
				for(Object o:getAll){
					((PageFinishedWorker)o).doWhenPageFinished();
				}
			}
		}
		
		public void post(PageFinishedWorker worker){
			synchronized (isPageFinished) {
				if(isPageFinished){
					worker.doWhenPageFinished();
				}else{
					workers.offer(worker);
				}
			}
		}
	}
}
