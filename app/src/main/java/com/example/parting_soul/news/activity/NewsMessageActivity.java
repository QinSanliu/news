package com.example.parting_soul.news.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.parting_soul.news.Interface.callback.CollectionNewsCallBack;
import com.example.parting_soul.news.NewsApplication;
import com.example.parting_soul.news.R;
import com.example.parting_soul.news.bean.News;
import com.example.parting_soul.news.utils.CollectionCheckStateManager;
import com.example.parting_soul.news.utils.CommonInfo;
import com.example.parting_soul.news.utils.LogUtils;
import com.example.parting_soul.news.utils.cache.database.CollectionNewsThread;
import com.example.parting_soul.news.utils.style.LanguageChangeManager;
import com.example.parting_soul.news.utils.style.ThemeChangeManager;

import java.util.List;

import static com.example.parting_soul.news.R.id.webView;
import static com.example.parting_soul.news.utils.CollectionCheckStateManager.FROM_COLLECTIONACTIVITY;

/**
 * Created by parting_soul on 2016/10/17.
 * 显示新闻内容的Activity
 */

public class NewsMessageActivity extends AppCompatActivity implements View.OnClickListener
        , CompoundButton.OnCheckedChangeListener {
    /**
     * url的Key
     */
    private static final String URL_KEY = "url";

    /**
     * title的key
     */
    private static final String TITLE_KEY = "title";

    /**
     * 新闻是否被收藏
     */
    private static final String IS_COLLECTED = "is_collected";

    /**
     * 收藏结束
     */
    private static final int COLLECTION_FINISH = 0X2222;

    /**
     * 取消收藏结束
     */
    private static final int DIS_COLLECTION_FINISH = 0X333;

    /**
     * 显示网页新闻
     */
    private WebView mWebView;

    /**
     * 标题中的返回按钮
     */
    private ImageButton mBackView;

    /**
     * 收藏单选框
     */
    private static CheckBox mIsCollected;

    /**
     * 新闻url
     */
    private String mUrl;

    /**
     * 新闻标题
     */
    private String mTitle;

    /**
     * 是否被收藏
     */
    private boolean is_Collected;

    /**
     * 收藏回调接口管理类
     */
    private static CollectionCheckStateManager mCalllBackManager = CollectionCheckStateManager.newInstance();

    private static int from_where_activity;

    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case COLLECTION_FINISH:
                    judgeResult(msg, R.string.is_collected);
                    break;
                case DIS_COLLECTION_FINISH:
                    judgeResult(msg, R.string.cancel_collected);
                    break;
            }
        }

        private void judgeResult(Message msg, int is_collected) {
            if ((Boolean) (msg.obj)) {
                Toast.makeText(NewsApplication.getContext(), is_collected, Toast.LENGTH_SHORT).show();
                //若是从CollectionActivity则通知其更新
                if (from_where_activity == FROM_COLLECTIONACTIVITY) {
                    mCalllBackManager.getNotifyCollectionActivityCallBack().collectedStateChange(mIsCollected.isChecked());
                }
                //通知newsfragment将该新闻变为当前收藏的状态
                mCalllBackManager.getNotifyNewsFragmentCallBack().collectedStateChange(mIsCollected.isChecked());
            } else {
                mIsCollected.setChecked(!mIsCollected.isChecked());
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChangeManager.changeThemeMode(this);
        LanguageChangeManager.changeLanguage();
        setContentView(R.layout.layout_news_msg);
        mBackView = (ImageButton) findViewById(R.id.back_forward);
        mBackView.setOnClickListener(this);
        mIsCollected = (CheckBox) findViewById(R.id.is_collectedBox);
        mWebView = (WebView) findViewById(webView);
        mIsCollected.setOnCheckedChangeListener(this);

        Intent result = getIntent();
        if (result != null) {
            mUrl = result.getStringExtra(URL_KEY);
            mTitle = result.getStringExtra(TITLE_KEY);
            is_Collected = result.getBooleanExtra(IS_COLLECTED, false);
            mIsCollected.setChecked(is_Collected);
            LogUtils.d(CommonInfo.TAG, "--->" + mTitle);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            mWebView.getSettings().setDomStorageEnabled(true);
            mWebView.loadUrl(mUrl);
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            mWebView.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 其他activity启动该Activity的接口
     *
     * @param context 启动该activity的上下文
     * @param url     要显示新闻的url地址
     */
    public static void startActivity(Context context, String url, String title, boolean isCollected, int fromWhere) {
        Intent intent = new Intent();
        intent.putExtra(URL_KEY, url);
        intent.putExtra(TITLE_KEY, title);
        intent.putExtra(IS_COLLECTED, isCollected);
        intent.setClass(context, NewsMessageActivity.class);
        from_where_activity = fromWhere;
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
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView != null && mWebView.canGoBack()) {
            if (mWebView.getUrl().equals(mUrl)) {
                return super.onKeyDown(keyCode, event);
            } else {
                mWebView.goBack();
                return true;
            }
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
        mIsCollected.setChecked(isChecked);
        if (isChecked) {
            if (!is_Collected) {
                LogUtils.d(CommonInfo.TAG, "asdff" + isChecked);
                new CollectionNewsThread().setCollectionNews(new CollectionNewsCallBack() {
                    @Override
                    public void getResult(List<News> newsList) {

                    }

                    @Override
                    public void isSuccess(boolean isSuccess) {
                        Message msg = Message.obtain();
                        msg.obj = isSuccess;
                        msg.what = COLLECTION_FINISH;
                        mHandler.sendMessage(msg);
                    }
                }, mTitle);
            }
        } else {
            new CollectionNewsThread().cancelCollectionNews(new CollectionNewsCallBack() {
                @Override
                public void getResult(List<News> newsList) {

                }

                @Override
                public void isSuccess(boolean isSuccess) {
                    Message msg = Message.obtain();
                    msg.obj = isSuccess;
                    msg.what = DIS_COLLECTION_FINISH;
                    mHandler.sendMessage(msg);
                }
            }, mTitle);
            is_Collected = false;
        }
    }
}
