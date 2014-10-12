package com.chongwu.activity;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.chongwu.R;
 
//import com.taobao.utils.AppConstants;
//import com.taobao.top.config.AppConstants;
//import com.taobao.top.utils.HttpOperation;
//import com.taobao.top.utils.Util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity {
	public static final String TAG = "WebViewActivity";
	WebView webview;
	String url1 = "";
	String code;

	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);

		webview = (WebView) findViewById(R.id.webView);

		// 这行很重要一点要有，不然网页的认证按钮会无效
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		// wv.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
		webSettings.setBuiltInZoomControls(true);
		// webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) { // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, android.net.http.SslError error) { // 重写此方法可以让webview处理https请求
				handler.proceed();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				url1 = webview.getUrl();
				if (url1 != null) {
					Log.i(TAG, "webview.getUrl() = " + url1);

				}
				super.onPageFinished(view, url);
			} 
		});

		Intent intent = getIntent();
		String loginUrl = intent.getStringExtra("loginUrl");
		Log.i(TAG, "webView url = "+loginUrl);
		webview.loadUrl(loginUrl);
	}
	 
}

