package com.example.parting_soul.news.bean;

import com.example.parting_soul.news.MyApplication;
import com.example.parting_soul.news.R;

/**
 * Created by parting_soul on 2016/10/3.
 * 新闻的种类，显示在横向导航条上
 */

public class NewsKinds {

    public static String[] getNewsTypes() {
        return MyApplication.getContext().getResources().getStringArray(R.array.news_type);
    }

}
