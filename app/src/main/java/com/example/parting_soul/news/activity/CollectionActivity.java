package com.example.parting_soul.news.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.parting_soul.news.Interface.callback.CollectionCheckStateNotifiyCallBack;
import com.example.parting_soul.news.Interface.callback.CollectionNewsCallBack;
import com.example.parting_soul.news.R;
import com.example.parting_soul.news.adapter.NewsInfoAdapter;
import com.example.parting_soul.news.bean.News;
import com.example.parting_soul.news.utils.support.CollectionCheckStateManager;
import com.example.parting_soul.news.utils.support.CommonInfo;
import com.example.parting_soul.news.utils.support.LogUtils;
import com.example.parting_soul.news.utils.cache.database.CollectionNewsThread;
import com.example.parting_soul.news.utils.style.LanguageChangeManager;
import com.example.parting_soul.news.utils.style.ThemeChangeManager;

import java.util.List;

/**
 * Created by parting_soul on 2016/10/30.
 */

public class CollectionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
        , CollectionNewsCallBack, CollectionCheckStateNotifiyCallBack {
    private ImageButton mBack;

    private ListView mListView;

    private NewsInfoAdapter mNewsInfoAdapter;

    private CollectionCheckStateManager mCollectionCheckStateManager;

    private List<News> mNewsList;

    private News mCurrentSelectedNews;

    int currentPos;

    private ImageView mEmpty;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChangeManager.changeThemeMode(this);
        LanguageChangeManager.changeLanguage();
        setContentView(R.layout.layout_collection);
        ((TextView) findViewById(R.id.title_name)).setText(R.string.collection);
        mBack = (ImageButton) findViewById(R.id.back_forward);
        mListView = (ListView) findViewById(R.id.news_lists);
        mEmpty = (ImageView) findViewById(R.id.empty);
        mListView.setOnItemClickListener(this);
        mCollectionCheckStateManager = CollectionCheckStateManager.newInstance();
        mCollectionCheckStateManager.setNotifyCollectionActivityCallBack(this);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        new CollectionNewsThread().getCollectionNews(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCurrentSelectedNews = (News) mNewsInfoAdapter.getItem(position);
        currentPos = mNewsList.indexOf(mCurrentSelectedNews);
        NewsMessageActivity.startActivity(this, mCurrentSelectedNews.getUrl(),
                mCurrentSelectedNews.getTitle(), mCurrentSelectedNews.is_collected(),
                CollectionCheckStateManager.FROM_COLLECTIONACTIVITY);
    }


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CollectionActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void getResult(List<News> newsList) {
        mNewsList = newsList;
        if (mNewsList == null || mNewsList.size() == 0) {
            setEmptyView();
        } else {
            mNewsInfoAdapter = new NewsInfoAdapter(this, mNewsList, mListView);
            mListView.setAdapter(mNewsInfoAdapter);
            mNewsInfoAdapter.setIsCanLoadImage(true);
            mNewsInfoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void isSuccess(boolean isSuccess) {

    }

    @Override
    public void collectedStateChange(boolean isChange) {
        if (!isChange) {
            mNewsList.remove(mCurrentSelectedNews);
            setEmptyView();
            LogUtils.d(CommonInfo.TAG, "--->" + mNewsList.size());
        } else {
            if (!mNewsList.contains(mCurrentSelectedNews)) {
                mNewsList.add(currentPos, mCurrentSelectedNews);
            }
        }
        mNewsInfoAdapter.notifyDataSetChanged();
    }

    public void setEmptyView() {
        if (mNewsList.size() == 0) {
            mEmpty.setVisibility(View.VISIBLE);
        } else {
            mEmpty.setVisibility(View.GONE);
        }
    }
}
