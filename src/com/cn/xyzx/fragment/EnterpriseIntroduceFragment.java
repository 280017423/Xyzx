package com.cn.xyzx.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cn.xyzx.R;
import com.cn.xyzx.activity.InfoCenterActivity;

public class EnterpriseIntroduceFragment extends FragmentBase {

	private String mLoadUrl;
	private WebView mWebview;

	public static final EnterpriseIntroduceFragment newInstance(String url) {
		EnterpriseIntroduceFragment fragment = new EnterpriseIntroduceFragment();
		Bundle bundle = new Bundle();
		bundle.putString("loadUrl", url);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		initVariables();
		super.onCreate(savedInstanceState);
	}

	private void initVariables() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mLoadUrl = getArguments().getString("loadUrl");
		View view = inflater.inflate(R.layout.fragment_enterprise_introduce, container, false);
		mWebview = (WebView) view.findViewById(R.id.wv_layout);
		configWebview();
		mWebview.loadUrl(mLoadUrl);
		return view;
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
				if (isAdded()) {
					((InfoCenterActivity) getActivity()).showLoading();
				}
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView webView, String url) {
				if (isAdded()) {
					((InfoCenterActivity) getActivity()).dismissLoading();
				}
				webView.loadUrl("javascript:(function() { var videos = document.getElementsByTagName('video'); for(var i=0;i<videos.length;i++){videos[i].play();}})()");
				super.onPageFinished(webView, url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				if (isAdded()) {
					((InfoCenterActivity) getActivity()).dismissLoading();
				}
				toast("加载失败");
				super.onReceivedError(view, errorCode, description, failingUrl);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView webView, String url) {
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
				if (isAdded()) {
					getActivity().setProgress(newProgress * 100);
				}
				if (newProgress == 100) {
					if (isAdded()) {
						((InfoCenterActivity) getActivity()).dismissLoading();
					}
				}
			}
		});
	}
}
