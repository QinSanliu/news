package com.example.parting_soul.news.fragment.collection;


import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.example.parting_soul.news.R;
import com.example.parting_soul.news.activity.MessageActivity;
import com.example.parting_soul.news.adapter.WeiChatDetailFragmentAdapter;
import com.example.parting_soul.news.bean.WeiChat;
import com.example.parting_soul.news.customview.LoadMoreItemListView;
import com.example.parting_soul.news.fragment.support.CollectionBaseFragment;
import com.example.parting_soul.news.utils.cache.CollectionWeiChatThread;
import com.example.parting_soul.news.utils.support.CollectionCheckStateManager;
import com.example.parting_soul.news.utils.support.CommonInfo;
import com.example.parting_soul.news.utils.support.LogUtils;

import java.util.List;

/**
 * Created by parting_soul on 2016/11/7.
 */

public class WeiChatCollectionFragment extends CollectionBaseFragment<WeiChat> {
    public static final String NAME = "WeiChatCollectionFragment";

    private LoadMoreItemListView mListView;

    private WeiChatDetailFragmentAdapter mWeiChatDetailFragmentAdapter;


    @Override
    public void updateUI(Message msg) {
        mLists = (List<WeiChat>) msg.obj;
        if (mLists == null || mLists.size() == 0) {
            setEmptyView();
        } else {
            mWeiChatDetailFragmentAdapter = new WeiChatDetailFragmentAdapter(getActivity(), mLists, mListView);
            mListView.setAdapter(mWeiChatDetailFragmentAdapter);
            mWeiChatDetailFragmentAdapter.setIsCanLoadImage(true);
            mWeiChatDetailFragmentAdapter.notifyDataSetChanged();
            setEmptyView();
            LogUtils.d(CommonInfo.TAG, "-->" + mLists.size());
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_weichat_collection, container, false);
        mListView = (LoadMoreItemListView) view.findViewById(R.id.lists);
        mListView.setOnItemClickListener(this);
        mEmpty = (ImageView) view.findViewById(R.id.empty);
        new CollectionWeiChatThread().getCollectionWeiChat(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCollectionCheckStateManager.setNotifyVisibleNewsFragmentCallBack(this);
        mCurrentSelectedItem = mLists.get(position);
        currentPos = mLists.indexOf(mCurrentSelectedItem);
        MessageActivity.startActivity(getActivity(), mCurrentSelectedItem.getUrl(),
                mCurrentSelectedItem.getTitle(), mCurrentSelectedItem.is_collected(),
                CollectionCheckStateManager.FROM_WEICHATFRAGMENT);
    }

    @Override
    public void updateFragmentAdapter() {
        mWeiChatDetailFragmentAdapter.notifyDataSetChanged();
    }

}
