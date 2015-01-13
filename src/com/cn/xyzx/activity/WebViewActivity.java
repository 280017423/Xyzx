package com.cn.xyzx.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cn.xyzx.R;
import com.qianjiang.framework.util.EvtLog;
import com.qianjiang.framework.util.StringUtil;
import com.qianjiang.framework.widget.LoadingUpView;

public class WebViewActivity extends ActivityBase implements OnClickListener {
	private final String TAG = "WebViewActivity ";
	private WebView mWebview;
	private String mLoadUrl;
	private LoadingUpView mLoadingUpView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		initVariables();
		initView();
		configWebview();
		loadURL();
	}

	private void initVariables() {
		mLoadingUpView = new LoadingUpView(this);
		mLoadUrl = getIntent().getDataString();
		EvtLog.d(TAG, mLoadUrl);
		if (StringUtil.isNullOrEmpty(mLoadUrl)) {
			finish();
		}
	}

	private void initView() {
		mWebview = (WebView) findViewById(R.id.wv_layout);

	}

	private void loadURL() {
		mWebview.loadUrl(mLoadUrl);
	}

	@Override
	protected void onDestroy() {
		mWebview.destroy();
		super.onDestroy();
	}

	private void configWebview() {
		// 允许javascript代码执行
		WebSettings settings = mWebview.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setDomStorageEnabled(true);
		settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		settings.setAppCacheMaxSize(8 * 1024 * 1024);
		settings.setRenderPriority(RenderPriority.HIGH);
		settings.setAppCacheEnabled(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setAllowFileAccess(true);
		settings.setDefaultTextEncodingName("utf-8");
		mWebview.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				Log.d(TAG, "---onPageStarted---");
				showLoadingUpView(mLoadingUpView);
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView webView, String url) {
				Log.d(TAG, "---onPageFinished---");
				dismissLoadingUpView(mLoadingUpView);
				webView.loadUrl("javascript:(function() { var videos = document.getElementsByTagName('video'); for(var i=0;i<videos.length;i++){videos[i].play();}})()");
				super.onPageFinished(webView, url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				dismissLoadingUpView(mLoadingUpView);
				toast("加载失败");
				super.onReceivedError(view, errorCode, description, failingUrl);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView webView, String url) {
				Log.d(TAG, "---shouldOverrideUrlLoading---");
				// shouldOverrideUrlLoading并不是每次都在onPageStarted之前开始调用的，
				// 就是说一个新的URL不是每次都经过shouldOverrideUrlLoading的，只有在调用webview.loadURL的时候才会调用。
				webView.loadUrl(url);
				return true;
			}
		});

		mWebview.setWebChromeClient(new WebChromeClient() {
			// 使webview可以更新进度条
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				WebViewActivity.this.setProgress(newProgress * 100);
				if (newProgress == 100) {
					dismissLoadingUpView(mLoadingUpView);
				}
			}
		});
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.ibtn_back:
				finish();
				break;
			default:
				break;
		}

	}
}
