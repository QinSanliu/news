package com.example.parting_soul.news.utils;

import com.example.parting_soul.news.Interface.callback.CollectionCheckStateNotifiyCallBack;

/**
 * Created by parting_soul on 2016/10/31.
 * 收藏状态回调管理类
 */

public class CollectionCheckStateManager {
    private CollectionCheckStateNotifiyCallBack mCallBack;

    private static CollectionCheckStateManager manager;

    public static CollectionCheckStateManager newInstance() {
        if (manager == null) {
            manager = new CollectionCheckStateManager();
        }
        return manager;
    }

    public CollectionCheckStateNotifiyCallBack getmCallBack() {
        return mCallBack;
    }

    public void setmCallBack(CollectionCheckStateNotifiyCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }
}
