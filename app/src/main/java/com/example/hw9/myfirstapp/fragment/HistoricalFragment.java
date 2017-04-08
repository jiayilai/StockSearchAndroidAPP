package com.example.hw9.myfirstapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.hw9.myfirstapp.R;

public class HistoricalFragment extends Fragment {
	//fragment receives message from activity
	private String url = "http://stocksearchback.appspot.com/historical.php?symbol=";
	private WebView webView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        //debug used
        Log.i("historicalFragment", "historicalFragment is created");
		View view = inflater.inflate(R.layout.historical, container, false);
		webView = (WebView) view.findViewById(R.id.webView);
		//fragment receives message from activity
		String text=getArguments().getString("Symbol");
		Log.i("hisFragmentReceive", text);
		url = url + text;
		// WebView加载web资源
		webView.loadUrl(url);
		// 覆盖WebView默认通过第三方或者是系统浏览器打开网页的行为，使得网页可以在WebVIew中打开
		webView.setWebViewClient(new WebViewClient(){

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				//返回值是true的时候控制网页在WebView中去打开，如果为false调用系统浏览器或第三方浏览器去打开
				view.loadUrl(url);
				return true;
			}
			//WebViewClient帮助WebView去处理一些页面控制和请求通知

		});
		//启用支持JavaScript
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
        Log.i("historicalFragment", "web is loading");

		return view;
	}
}
