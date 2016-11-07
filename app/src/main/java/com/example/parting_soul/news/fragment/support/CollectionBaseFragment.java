package com.example.parting_soul.news.fragment.support;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.example.parting_soul.news.Interface.callback.CollectionCallBack;
import com.example.parting_soul.news.Interface.callback.CollectionCheckStateNotifiyCallBack;
import com.example.parting_soul.news.utils.network.AbstractDownLoadHandler;
import com.example.parting_soul.news.utils.support.CollectionCheckStateManager;
import com.example.parting_soul.news.utils.support.CommonInfo;
import com.example.parting_soul.news.utils.support.LogUtils;

import java.util.List;

/**
 * Created by parting_soul on 2016/11/7.
 */

public abstract class CollectionBaseFragment<T> extends Fragment implements AdapterView.OnItemClickListener,
        CollectionCallBack<T>, CollectionCheckStateNotifiyCallBack {

    protected CollectionCheckStateManager mCollectionCheckStateManager;

    protected List<T> mLists;

    protected T mCurrentSelectedItem;

    protected int currentPos;

    protected ImageView mEmpty;

    protected Handler mHandler = new AbstractDownLoadHandler() {
        @Override
        public void handleMessage(Message msg) {
            updateUI(msg);
        }

        @Override
        protected void showError() {

        }

        @Override
        protected void updateUI(Message msg) {
            CollectionBaseFragment.this.updateUI(msg);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCollectionCheckStateManager = CollectionCheckStateManager.newInstance();
    }


    /**
     * 方法在handleMessage内执行，更新UI
     *
     * @param msg
     */
    public abstract void updateUI(Message msg);

    @Override
    public void getResult(List<T> lists) {
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
        if (mLists.size() == 0 || mLists == null) {
            mEmpty.setVisibility(View.VISIBLE);
        } else {
            mEmpty.setVisibility(View.GONE);
        }
    }


    @Override
    public void collectedStateChange(boolean isChange) {
        if (!isChange) {
            mLists.remove(mCurrentSelectedItem);
            LogUtils.d(CommonInfo.TAG, "--->" + mLists.size());
        } else {
            if (!mLists.contains(mCurrentSelectedItem)) {
                mLists.add(currentPos, mCurrentSelectedItem);
            }
        }
        updateFragmentAdapter();
        setEmptyView();
    }

    /**
     * 通知适配器改变数据
     */
    public abstract void updateFragmentAdapter();
}
