package com.example.parting_soul.news.utils.cache.database;

import android.content.Context;

import com.example.parting_soul.news.Interface.callback.CollectionNewsCallBack;
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

    public void getCollectionNews(final CollectionNewsCallBack callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<News> lists = getDBManager(mContext).readCollectionNews();
                callBack.getResult(lists);
            }
        }).start();
    }

    public void setCollectionNews(final CollectionNewsCallBack callBack, final String title) {
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

    public void cancelCollectionNews(final CollectionNewsCallBack callBack, final String title) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isSuccess = DBManager.getDBManager(mContext).deleteNewsCollectionFromDataBase(title);
                callBack.isSuccess(isSuccess);
            }
        }).start();
    }

}