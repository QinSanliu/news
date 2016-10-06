package com.example.parting_soul.news.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.parting_soul.news.utils.LogUtils;

import static com.example.parting_soul.news.utils.CommonInfo.TAG;

/**
 * Created by parting_soul on 2016/10/6.
 * 抽象类 使得fragment无法预加载下一页的数据
 */

public abstract class BaseFragment extends Fragment {
    /**
     * UI是否已经绘制完成
     */
    protected boolean mIsPrepared;

    /**
     * 当前页面是否对用户可见
     */
    protected boolean mIsVisibleToUser;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mIsVisibleToUser = true;
            onVisible();
        } else {
            mIsVisibleToUser = false;
            onInVisible();
        }
        LogUtils.d(TAG, "isVisibleToUser -->fragment " + isVisibleToUser + " " + this);
    }

    /**
     * 当前页面可见是进行的操作
     */
    private void onVisible() {
        loadData();
    }

    /**
     * 当前页面不可见时操作
     */
    private void onInVisible() {

    }

    /**
     * 加载数据
     */
    public abstract void loadData();

    /**
     * 当布局被销毁时UI准备状态标志位置为false
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIsPrepared = false;
    }
}
