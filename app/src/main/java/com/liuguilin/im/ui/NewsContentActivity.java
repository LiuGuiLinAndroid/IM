package com.liuguilin.im.ui;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liuguilin.im.R;
import com.liuguilin.im.base.BaseActivity;
import com.liuguilin.im.http.HttpHelper;
import com.liuguilin.im.utils.CommonUtils;

/**
 * FileName: NewsContentActivity
 * Founder: LiuGuiLin
 * Create Date: 2018/12/21 15:11
 * Email: lgl@szokl.com.cn
 * Profile: 新闻详情
 */
public class NewsContentActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout include_title_iv_back;
    private TextView include_title_text;
    private TextView title_right_text;

    private String newsUrl;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);

        initView();
    }

    private void initView() {

        newsUrl = getIntent().getStringExtra("url");

        include_title_iv_back = (RelativeLayout) findViewById(R.id.include_title_iv_back);
        include_title_text = (TextView) findViewById(R.id.include_title_text);
        title_right_text = (TextView) findViewById(R.id.title_right_text);
        mWebView = (WebView) findViewById(R.id.mWebView);

        include_title_iv_back.setOnClickListener(this);
        title_right_text.setOnClickListener(this);

        include_title_text.setText(getString(R.string.str_news_title_text));
        title_right_text.setText(getString(R.string.str_common_share));

        WebSettings settings = mWebView.getSettings();
        if (mWebView.isHardwareAccelerated()) {
            settings.setJavaScriptEnabled(true);
        }
        // 避免中文乱码
        settings.setDefaultTextEncodingName("utf-8");
        // 设置 缓存模式
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        settings.setDomStorageEnabled(true);
        // 开启 database storage API 功能
        settings.setDatabaseEnabled(true);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.loadUrl(newsUrl);

        //监听返回
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (i == KeyEvent.KEYCODE_BACK) {
                        if (mWebView.canGoBack()) {
                            mWebView.goBack();
                            return true;

                        }
                    }
                }
                return false;
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.destroy();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.include_title_iv_back:
                finish();
                break;
            case R.id.title_right_text:

                break;
        }
    }

    private class MyWebViewClient extends WebViewClient {

        public MyWebViewClient() {

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
            super.onReceivedSslError(view, handler, error);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }
}
