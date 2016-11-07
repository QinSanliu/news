package com.example.parting_soul.news.utils.cache;

import android.content.Context;

import com.example.parting_soul.news.Interface.callback.CollectionCallBack;
import com.example.parting_soul.news.utils.cache.database.DBManager;
import com.example.parting_soul.news.utils.support.NewsApplication;
import com.example.parting_soul.news.bean.News;
import com.example.parting_soul.news.utils.support.CommonInfo;
import com.example.parting_soul.news.utils.support.LogUtils;

import java.util.List;

import static com.example.parting_soul.news.utils.cache.database.DBManager.getDBManager;

/**
 * Created by parting_soul on 2016/10/31.
 */

public class CollectionNewsThread {
    private Context mContext;

    public CollectionNewsThread() {
        mContext = NewsApplication.getContext();
    }

    public void getCollectionNews(final CollectionCallBack<News> callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<News> lists = getDBManager(mContext).readCollectionNews();
                callBack.getResult(lists);
            }
        }).start();
    }

    public void setCollectionNews(final CollectionCallBack<News> callBack, final String title) {
        LogUtils.d(CommonInfo.TAG, "asdff--->" + "setCollectionNews");
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isSuccess = DBManager.getDBManager(mContext).addNewsCollectionToDataBase(title);
                LogUtils.d(CommonInfo.TAG, "asdff--->" + "run");
                callBack.isSuccess(isSuccess);
            }
        }).start();
    }

    public void cancelCollectionNews(final CollectionCallBack<News> callBack, final String title) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isSuccess = DBManager.getDBManager(mContext).deleteNewsCollectionFromDataBase(title);
                callBack.isSuccess(isSuccess);
            }
        }).start();
    }

}