package com.example.parting_soul.news.utils.cache.database;

import android.content.Context;

import com.example.parting_soul.news.Interface.callback.CollectionCallBack;
import com.example.parting_soul.news.bean.Joke;
import com.example.parting_soul.news.utils.support.CommonInfo;
import com.example.parting_soul.news.utils.support.LogUtils;
import com.example.parting_soul.news.utils.support.NewsApplication;

import java.util.List;

import static com.example.parting_soul.news.utils.cache.database.DBManager.getDBManager;

/**
 * Created by parting_soul on 2016/11/8.
 */

public class CollectionJokeThread {
    private Context mContext;

    public CollectionJokeThread() {
        mContext = NewsApplication.getContext();
    }

    public void getCollectionJoke(final CollectionCallBack<Joke> callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Joke> lists = getDBManager(mContext).readCollectionJokes();
                callBack.getResult(lists);
            }
        }).start();
    }

    public void setCollectionJoke(final CollectionCallBack<Joke> callBack, final String hashID) {
        LogUtils.d(CommonInfo.TAG, "asdff--->" + "setCollectionNews");
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isSuccess = DBManager.getDBManager(mContext).addJokeCollectionToDataBase(hashID);
                LogUtils.d(CommonInfo.TAG, "asdff--->" + "run");
                callBack.isSuccess(isSuccess);
            }
        }).start();
    }

    public void cancelCollectionJoke(final CollectionCallBack<Joke> callBack, final String hashID) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isSuccess = DBManager.getDBManager(mContext).deleteJokeCollectionFromDataBase(hashID);
                callBack.isSuccess(isSuccess);
            }
        }).start();
    }
}
