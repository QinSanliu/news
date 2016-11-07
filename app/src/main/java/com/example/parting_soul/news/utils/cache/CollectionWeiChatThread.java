package com.example.parting_soul.news.utils.cache;

import android.content.Context;

import com.example.parting_soul.news.Interface.callback.CollectionCallBack;
import com.example.parting_soul.news.bean.WeiChat;
import com.example.parting_soul.news.utils.cache.database.DBManager;
import com.example.parting_soul.news.utils.support.CommonInfo;
import com.example.parting_soul.news.utils.support.LogUtils;
import com.example.parting_soul.news.utils.support.NewsApplication;

import java.util.List;

import static com.example.parting_soul.news.utils.cache.database.DBManager.getDBManager;

/**
 * Created by parting_soul on 2016/11/7.
 */

public class CollectionWeiChatThread {
    private Context mContext;

    public CollectionWeiChatThread() {
        mContext = NewsApplication.getContext();
    }

    public void getCollectionWeiChat(final CollectionCallBack<WeiChat> callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<WeiChat> lists = getDBManager(mContext).readCollectionWeiChat();
                callBack.getResult(lists);
            }
        }).start();
    }

    public void setCollectionWeiChat(final CollectionCallBack<WeiChat> callBack, final String title) {
        LogUtils.d(CommonInfo.TAG, "asdff--->" + "setCollectionWeiChat");
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isSuccess = DBManager.getDBManager(mContext).addWeiChatCollectionToDataBase(title);
                LogUtils.d(CommonInfo.TAG, "asdff--->" + "run");
                callBack.isSuccess(isSuccess);
            }
        }).start();
    }

    public void cancelCollectionWeiChat(final CollectionCallBack<WeiChat> callBack, final String title) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isSuccess = DBManager.getDBManager(mContext).deleteWeiChatCollectionFromDataBase(title);
                callBack.isSuccess(isSuccess);
            }
        }).start();
    }
}
