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

import android.content.Context;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class SelfWebViewClient extends WebViewClient {

	private Context context;

	private final PageFinishedListener pageFinishedListener;

	public SelfWebViewClient(Context context , PageFinishedListener pageFinishedListener) {
		this.context = context;
		this.pageFinishedListener = pageFinishedListener;
	}

	@Override
	public void onPageFinished(WebView view, String url){
		if(pageFinishedListener != null){
			pageFinishedListener.onPageFinished(view, url);
		}
	}

	@Override
	public boolean shouldOverrideUrlLoading (WebView view, String url){
		return super.shouldOverrideUrlLoading(view, url);
	}
	


	@Override
	public WebResourceResponse shouldInterceptRequest(final WebView view, String url) {
		return super.shouldInterceptRequest(view, url);
	}

}
