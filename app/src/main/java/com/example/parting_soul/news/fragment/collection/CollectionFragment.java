package com.example.parting_soul.news.fragment.collection;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.parting_soul.news.Interface.callback.CollectionCheckStateNotifiyCallBack;
import com.example.parting_soul.news.Interface.callback.CollectionNewsCallBack;
import com.example.parting_soul.news.R;
import com.example.parting_soul.news.activity.MainActivity;
import com.example.parting_soul.news.activity.NewsMessageActivity;
import com.example.parting_soul.news.adapter.NewsInfoAdapter;
import com.example.parting_soul.news.bean.News;
import com.example.parting_soul.news.utils.cache.database.CollectionNewsThread;
import com.example.parting_soul.news.utils.network.AbstractDownLoadHandler;
import com.example.parting_soul.news.utils.support.CollectionCheckStateManager;
import com.example.parting_soul.news.utils.support.CommonInfo;
import com.example.parting_soul.news.utils.support.LogUtils;

import java.util.List;

/**
 * Created by parting_soul on 2016/11/2.
 */

public class CollectionFragment extends Fragment implements AdapterView.OnItemClickListener
        , CollectionNewsCallBack, CollectionCheckStateNotifiyCallBack {
    public static final String NAME = "CollectionFragment";

    private ListView mListView;

    private NewsInfoAdapter mNewsInfoAdapter;

    private CollectionCheckStateManager mCollectionCheckStateManager;

    private List<News> mNewsList;

    private News mCurrentSelectedNews;

    int currentPos;

    private ImageView mEmpty;


    private Handler mHandler = new AbstractDownLoadHandler() {
        @Override
        public void handleMessage(Message msg) {
            updateUI(msg);
        }

        @Override
        protected void showError() {

        }

        @Override
        protected void updateUI(Message msg) {
            mNewsList = (List<News>) msg.obj;
            if (mNewsList == null || mNewsList.size() == 0) {
                setEmptyView();
            } else {
                mNewsInfoAdapter = new NewsInfoAdapter(getActivity(), mNewsList, mListView);
                mListView.setAdapter(mNewsInfoAdapter);
                mNewsInfoAdapter.setIsCanLoadImage(true);
                mNewsInfoAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setTitleName(R.string.collection);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_collection, container, false);
        mListView = (ListView) view.findViewById(R.id.news_lists);
        mEmpty = (ImageView) view.findViewById(R.id.empty);
        mListView.setOnItemClickListener(this);
        mCollectionCheckStateManager = CollectionCheckStateManager.newInstance();
        mCollectionCheckStateManager.setNotifyCollectionActivityCallBack(this);
        new CollectionNewsThread().getCollectionNews(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCurrentSelectedNews = (News) mNewsInfoAdapter.getItem(position);
        currentPos = mNewsList.indexOf(mCurrentSelectedNews);
        NewsMessageActivity.startActivity(getActivity(), mCurrentSelectedNews.getUrl(),
                mCurrentSelectedNews.getTitle(), mCurrentSelectedNews.is_collected(),
                CollectionCheckStateManager.FROM_COLLECTIONFRAGMENT);
    }

    @Override
    public void getResult(List<News> newsList) {
        Message msg = Message.obtain();
        msg.obj = newsList;
        mHandler.sendMessage(msg);
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

    /**
     * 没有数据时加载空界面
     */
    public void setEmptyView() {
        if (mNewsList.size() == 0) {
            mEmpty.setVisibility(View.VISIBLE);
        } else {
            mEmpty.setVisibility(View.GONE);
        }
    }
}
