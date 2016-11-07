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

import com.example.parting_soul.news.Interface.callback.CollectionCallBack;
import com.example.parting_soul.news.Interface.callback.CollectionCheckStateNotifiyCallBack;
import com.example.parting_soul.news.R;
import com.example.parting_soul.news.activity.MessageActivity;
import com.example.parting_soul.news.adapter.WeiChatDetailFragmentAdapter;
import com.example.parting_soul.news.bean.WeiChat;
import com.example.parting_soul.news.customview.LoadMoreItemListView;
import com.example.parting_soul.news.utils.cache.CollectionWeiChatThread;
import com.example.parting_soul.news.utils.network.AbstractDownLoadHandler;
import com.example.parting_soul.news.utils.support.CollectionCheckStateManager;
import com.example.parting_soul.news.utils.support.CommonInfo;
import com.example.parting_soul.news.utils.support.LogUtils;

import java.util.List;

/**
 * Created by parting_soul on 2016/11/7.
 */

public class WeiChatCollectionFragment extends Fragment implements AdapterView.OnItemClickListener,
        CollectionCallBack<WeiChat>, CollectionCheckStateNotifiyCallBack {
    public static final String NAME = "WeiChatCollectionFragment";

    private LoadMoreItemListView mListView;

    private WeiChatDetailFragmentAdapter mWeiChatDetailFragmentAdapter;

    private CollectionCheckStateManager mCollectionCheckStateManager;

    private List<WeiChat> mWeiChatList;

    private WeiChat mCurrentSelectedWeiChat;

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
            mWeiChatList = (List<WeiChat>) msg.obj;
            if (mWeiChatList == null || mWeiChatList.size() == 0) {
                setEmptyView();
            } else {
                mWeiChatDetailFragmentAdapter = new WeiChatDetailFragmentAdapter(getActivity(), mWeiChatList, mListView);
                mListView.setAdapter(mWeiChatDetailFragmentAdapter);
                mWeiChatDetailFragmentAdapter.setIsCanLoadImage(true);
                mWeiChatDetailFragmentAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCollectionCheckStateManager = CollectionCheckStateManager.newInstance();
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
        mCurrentSelectedWeiChat = mWeiChatList.get(position);
        currentPos = mWeiChatList.indexOf(mCurrentSelectedWeiChat);
        MessageActivity.startActivity(getActivity(), mCurrentSelectedWeiChat.getUrl(),
                mCurrentSelectedWeiChat.getTitle(), mCurrentSelectedWeiChat.is_collected(),
                CollectionCheckStateManager.FROM_WEICHATFRAGMENT);
    }

    @Override
    public void getResult(List<WeiChat> lists) {
        Message msg = Message.obtain();
        msg.obj = lists;
        mHandler.sendMessage(msg);
    }

    @Override
    public void isSuccess(boolean isSuccess) {

    }

    /**
     * 没有数据时加载空界面
     */
    public void setEmptyView() {
        if (mWeiChatList.size() == 0) {
            mEmpty.setVisibility(View.VISIBLE);
        } else {
            mEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void collectedStateChange(boolean isChange) {
        if (!isChange) {
            mWeiChatList.remove(mCurrentSelectedWeiChat);
            setEmptyView();
            LogUtils.d(CommonInfo.TAG, "--->" + mWeiChatList.size());
        } else {
            if (!mWeiChatList.contains(mCurrentSelectedWeiChat)) {
                mWeiChatList.add(currentPos, mCurrentSelectedWeiChat);
            }
        }
        mWeiChatDetailFragmentAdapter.notifyDataSetChanged();
    }
}
