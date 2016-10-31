package com.example.parting_soul.news.Interface.callback;

import com.example.parting_soul.news.bean.News;

import java.util.List;

/**
 * Created by parting_soul on 2016/10/31.
 */

public interface CollectionNewsCallBack {
    public void getResult(List<News> newsList);

    public void isSuccess(boolean isSuccess);
}
