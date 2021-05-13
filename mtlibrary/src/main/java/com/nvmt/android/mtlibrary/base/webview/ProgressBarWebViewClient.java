package com.nvmt.android.mtlibrary.base.webview;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

//    binding.webView.webViewClient = ProgressBarWebViewClient(binding.progressBar)
public class ProgressBarWebViewClient extends WebViewClient {
    private ProgressBar progressBar;

    public ProgressBarWebViewClient(ProgressBar progressBar) {
        this.progressBar = progressBar;
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // TODO Auto-generated method stub
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        // TODO Auto-generated method stub
        super.onPageFinished(view, url);
        progressBar.setVisibility(View.GONE);
    }
}