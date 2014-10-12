package com.chongwu.utils.common;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class WebUtil {

	//封装WebView
	public static void wrapWebView(WebView webView, final ProgressBar progress) {
		WebSettings webSettings = webView.getSettings();
		webSettings.setSavePassword(false);
		webSettings.setSaveFormData(false);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(false);
		webView.setWebChromeClient(new WebChromeClient());
		webView.setWebViewClient(new WebViewClient() {
			
			//加载链接
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			//链接开始加载时
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				if (progress != null) {
					progress.setVisibility(View.VISIBLE);
				}
			}

			//链接加载完成
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				if (progress != null) {
					progress.setVisibility(View.GONE);
				}
			}

			//webview无网络情况下的人性化处理
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
					view.loadData("", "text/html", "utf-8");
					Toast.makeText(view.getContext(), "网络不给力", android.widget.Toast.LENGTH_LONG).show();
			}
		});

	}
}
