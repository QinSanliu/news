package com.example.parting_soul.news.utils;

/**
 * Created by parting_soul on 2016/10/14.
 * 子线程进行新闻数据下载，得到json数据并且进行解析,返回新闻类的数组
 */

import android.content.Context;
import android.os.Message;

import com.example.parting_soul.news.bean.News;
import com.example.parting_soul.news.fragment.NewsFragment;

import java.util.List;

import static android.R.attr.top;


class LoaderNewsThread extends Thread {
    private AbstractDownLoadHandler mHandler;
    private String mParams;
    private Context mContext;

    public LoaderNewsThread(Context context, AbstractDownLoadHandler handler, String params) {
        mHandler = handler;
        mParams = params;
        mContext = context;
    }

    @Override
    public void run() {
        if (true) {
//            LogUtils.d(CommonInfo.TAG, " load from sqlitedatabase " + oldFirstVisibleItem + " " + top);
        } else {
            //通知主线程显示进度条
            //               mHandler.sendEmptyMessage(CommonInfo.LoaderStatus.SHOW_PROGRESS_DIALOG);
            //下载数据
//            HttpUtils.HttpPostMethod(CommonInfo.NewsAPI.Params.REQUEST_URL,
//                    mParams, CommonInfo.ENCODE_TYPE, );
//            LogUtils.d(CommonInfo.TAG, " load from web");
        }
    }
}