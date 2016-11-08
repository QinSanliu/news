package com.example.parting_soul.news.fragment.collection;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.parting_soul.news.R;
import com.example.parting_soul.news.adapter.JokeFragmentAdapter;
import com.example.parting_soul.news.bean.Joke;
import com.example.parting_soul.news.fragment.support.CollectionBaseFragment;
import com.example.parting_soul.news.utils.cache.database.CollectionJokeThread;
import com.example.parting_soul.news.utils.support.CommonInfo;
import com.example.parting_soul.news.utils.support.LogUtils;

import java.util.List;

import static com.example.parting_soul.news.R.id.lists;

/**
 * Created by parting_soul on 2016/11/7.
 */

public class JokeCollectionFragment extends CollectionBaseFragment<Joke> implements JokeFragmentAdapter.JokeCollectionCallBack {

    private JokeFragmentAdapter mJokeFragmentAdapter;

    private ListView mListView;

    private CollectionJokeThread mCollectionJokeThread;

    private CheckBox mCurrentCheckBox;

    private boolean checkBoxState;

    private int mCurrentPosition;


    @Override
    public void updateUI(Message msg) {
        if (msg.what == FROM_ACTIVITY) {
            mLists = (List<Joke>) msg.obj;
            LogUtils.d(CommonInfo.TAG, "--->ffff msg" + mLists.size());
            if (mLists == null || mLists.size() == 0) {
                setEmptyView();
            } else {
                mJokeFragmentAdapter = new JokeFragmentAdapter(getActivity(), mLists);
                mJokeFragmentAdapter.setJokeCollectionCallBack(this);
                mListView.setAdapter(mJokeFragmentAdapter);
                mJokeFragmentAdapter.notifyDataSetChanged();
                setEmptyView();
                LogUtils.d(CommonInfo.TAG, "-->" + mLists.size());
            }
        } else if (msg.what == FROM_JOKE_FRAGMENT) {
            if ((Boolean) msg.obj) {
                mLists.remove(mCurrentPosition);
                mJokeFragmentAdapter.notifyDataSetChanged();
            } else {
                mCurrentCheckBox.setChecked(checkBoxState);
            }
            LogUtils.d(CommonInfo.TAG, "--->ffff" + mLists.size());
            setEmptyView();
        }
    }

    @Override
    public void updateFragmentAdapter() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCollectionJokeThread = new CollectionJokeThread();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_funny_collection, container, false);
        mListView = (ListView) view.findViewById(lists);
        mEmpty = (ImageView) view.findViewById(R.id.empty);
        mCollectionJokeThread.getCollectionJoke(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void setJokeCollectedState(View view, String hashID, int position, boolean isSelected) {
        mCurrentCheckBox = (CheckBox) view;
        checkBoxState = isSelected;
        mCurrentPosition = position;
        if (isSelected) {
            mCollectionJokeThread.setCollectionJoke(this, hashID);
        } else {
            mCollectionJokeThread.cancelCollectionJoke(this, hashID);
        }
    }

}
