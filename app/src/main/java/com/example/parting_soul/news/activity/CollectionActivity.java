package com.example.parting_soul.news.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.parting_soul.news.R;
import com.example.parting_soul.news.adapter.NewsInfoAdapter;
import com.example.parting_soul.news.bean.News;
import com.example.parting_soul.news.utils.cache.database.DBManager;

import java.util.List;

/**
 * Created by parting_soul on 2016/10/30.
 */

public class CollectionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ImageButton mBack;

    private ListView mListView;

    private NewsInfoAdapter mNewsInfoAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        new DatabaseAsyncTask().execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        News news = (News) mNewsInfoAdapter.getItem(position);
        NewsMessageActivity.startActivity(this, news.getUrl());
    }

    class DatabaseAsyncTask extends AsyncTask<Void, Void, List<News>> {

        @Override
        protected List<News> doInBackground(Void... params) {
            return DBManager.getDBManager(CollectionActivity.this).readNewsCacheFromDatabase("top");
        }

        @Override
        protected void onPostExecute(List<News> result) {
            super.onPostExecute(result);
            mNewsInfoAdapter = new NewsInfoAdapter(CollectionActivity.this, result, mListView);
            mListView.setAdapter(mNewsInfoAdapter);
            mNewsInfoAdapter.notifyDataSetChanged();
        }
    }


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CollectionActivity.class);
        context.startActivity(intent);
    }
}
