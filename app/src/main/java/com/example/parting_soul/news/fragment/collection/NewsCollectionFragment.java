package com.example.parting_soul.news.fragment.collection;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.parting_soul.news.R;
import com.example.parting_soul.news.activity.MessageActivity;
import com.example.parting_soul.news.adapter.NewsInfoAdapter;
import com.example.parting_soul.news.bean.News;
import com.example.parting_soul.news.fragment.support.CollectionBaseFragment;
import com.example.parting_soul.news.utils.cache.CollectionNewsThread;
import com.example.parting_soul.news.utils.support.CollectionCheckStateManager;

import java.util.List;

/**
 * Created by parting_soul on 2016/11/2.
 */

public class NewsCollectionFragment extends CollectionBaseFragment<News> {
    public static final String NAME = "NewsCollectionFragment";

    private ListView mListView;

    private NewsInfoAdapter mNewsInfoAdapter;


    @Override
    public void updateUI(Message msg) {
        mLists = (List<News>) msg.obj;
        if (mLists == null || mLists.size() == 0) {
            setEmptyView();
        } else {
            mNewsInfoAdapter = new NewsInfoAdapter(getActivity(), mLists, mListView);
            mListView.setAdapter(mNewsInfoAdapter);
            mNewsInfoAdapter.setIsCanLoadImage(true);
            mNewsInfoAdapter.notifyDataSetChanged();
            setEmptyView();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_news_collection, container, false);
        mListView = (ListView) view.findViewById(R.id.news_lists);
        mEmpty = (ImageView) view.findViewById(R.id.empty);
        mListView.setOnItemClickListener(this);
        new CollectionNewsThread().getCollectionNews(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCollectionCheckStateManager.setNotifyVisibleNewsFragmentCallBack(this);
        mCurrentSelectedItem = (News) mNewsInfoAdapter.getItem(position);
        currentPos = mLists.indexOf(mCurrentSelectedItem);
        MessageActivity.startActivity(getActivity(), mCurrentSelectedItem.getUrl(),
                mCurrentSelectedItem.getTitle(), mCurrentSelectedItem.is_collected(),
                CollectionCheckStateManager.FROM_NEWSFRAGMENT);
    }

    @Override
    public void updateFragmentAdapter() {
        mNewsInfoAdapter.notifyDataSetChanged();
    }
}
