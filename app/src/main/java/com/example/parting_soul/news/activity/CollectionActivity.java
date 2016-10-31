package com.example.parting_soul.news.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.parting_soul.news.Interface.callback.CollectionNewsCallBack;
import com.example.parting_soul.news.R;
import com.example.parting_soul.news.adapter.NewsInfoAdapter;
import com.example.parting_soul.news.bean.News;
import com.example.parting_soul.news.utils.cache.database.CollectionNewsThread;
import com.example.parting_soul.news.utils.style.LanguageChangeManager;
import com.example.parting_soul.news.utils.style.ThemeChangeManager;

import java.util.List;

/**
 * Created by parting_soul on 2016/10/30.
 */

public class CollectionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
        , CollectionNewsCallBack {
    private ImageButton mBack;

    private ListView mListView;

    private NewsInfoAdapter mNewsInfoAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChangeManager.changeThemeMode(this);
        LanguageChangeManager.changeLanguage();
        setContentView(R.layout.layout_collection);
        ((TextView) findViewById(R.id.title_name)).setText(R.string.collection);
        mBack = (ImageButton) findViewById(R.id.back_forward);
        mListView = (ListView) findViewById(R.id.news_lists);
        mListView.setOnItemClickListener(this);
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
        News news = (News) mNewsInfoAdapter.getItem(position);
        NewsMessageActivity.startActivity(this, news.getUrl(), news.getTitle(), news.is_collected(),
                NewsMessageActivity.FROM_NEWSFRAGMENT);
    }


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CollectionActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void getResult(List<News> newsList) {
        if (newsList == null || newsList.size() == 0) {

        } else {
            mNewsInfoAdapter = new NewsInfoAdapter(this, newsList, mListView);
            mListView.setAdapter(mNewsInfoAdapter);
            mNewsInfoAdapter.setIsCanLoadImage(true);
            mNewsInfoAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void isSuccess(boolean isSuccess) {

    }
}
