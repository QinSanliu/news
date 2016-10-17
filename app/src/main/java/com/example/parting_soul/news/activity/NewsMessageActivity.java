package com.example.parting_soul.news.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.parting_soul.news.R;

import static com.example.parting_soul.news.R.id.webView;

/**
 * Created by parting_soul on 2016/10/17.
 * 显示新闻内容的Activity
 */

public class NewsMessageActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * activity间交互信息的KEY
     */
    private static final String URL_KEY = "url";

    /**
     * 显示网页新闻
     */
    private WebView mWebView;

    /**
     * 标题的布局
     */
    private RelativeLayout mTitleLayout;

    /**
     * 标题中的返回按钮
     */
    private ImageView mBackView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_news_msg);
        mTitleLayout = (RelativeLayout) findViewById(R.id.title_layout);
        mBackView = (ImageView) mTitleLayout.findViewById(R.id.open_draw);
        mBackView.setImageResource(R.mipmap.back);
        mBackView.setOnClickListener(this);

        mWebView = (WebView) findViewById(webView);
        Intent result = getIntent();
        if (result != null) {
            String url = result.getStringExtra(URL_KEY);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setWebViewClient(new WebViewClient());
            mWebView.loadUrl(url);
        }
    }

    /**
     * 其他activity启动该Activity的接口
     *
     * @param context 启动该activity的上下文
     * @param url     要显示新闻的url地址
     */
    public static void startActivity(Context context, String url) {
        Intent intent = new Intent();
        intent.putExtra(URL_KEY, url);
        intent.setClass(context, NewsMessageActivity.class);
        context.startActivity(intent);
    }

    /**
     * 若当前页面可返回到上一页且按下了返回键，则返回上一页
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 触发返回事件，销毁该Activity
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        this.finish();
    }
}
