package com.example.parting_soul.news.utils;

import com.example.parting_soul.news.Interface.callback.CollectionCheckStateNotifiyCallBack;

/**
 * Created by parting_soul on 2016/10/31.
 * 收藏状态回调管理类
 */

public class CollectionCheckStateManager {
    /**
     * 来自NewsFragment
     */
    public static final int FROM_NEWSFRAGMENT = 0x1111;

    /**
     * 来自CollectionActivity
     */
    public static final int FROM_COLLECTIONACTIVITY = 0x1112;

    /**
     * 通知NewsFragment更新
     */
    private CollectionCheckStateNotifiyCallBack mNotifyNewsFragmentCallBack;

    /**
     * 通知CollectionActivity更新
     */
    private CollectionCheckStateNotifiyCallBack mNotifyCollectionActivityCallBack;

    private static CollectionCheckStateManager manager;

    public static CollectionCheckStateManager newInstance() {
        if (manager == null) {
            manager = new CollectionCheckStateManager();
        }
        return manager;
    }

    public CollectionCheckStateNotifiyCallBack getNotifyNewsFragmentCallBack() {
        return mNotifyNewsFragmentCallBack;
    }

    public void setNotifyNewsFragmentCallBack(CollectionCheckStateNotifiyCallBack mNotifyNewsFragmentCallBack) {
        this.mNotifyNewsFragmentCallBack = mNotifyNewsFragmentCallBack;
    }

    public CollectionCheckStateNotifiyCallBack getNotifyCollectionActivityCallBack() {
        return mNotifyCollectionActivityCallBack;
    }

    public void setNotifyCollectionActivityCallBack(CollectionCheckStateNotifiyCallBack mNotifyCollectionActivityCallBack) {
        this.mNotifyCollectionActivityCallBack = mNotifyCollectionActivityCallBack;
    }
}
