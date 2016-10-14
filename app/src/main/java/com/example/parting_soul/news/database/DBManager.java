package com.example.parting_soul.news.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.parting_soul.news.bean.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by parting_soul on 2016/10/9.
 * 数据库管理类
 */

public class DBManager {
    /**
     * 表名
     */
    public static final String NEWS_TABLE_NAME = "newsinfo";

    /**
     * 主键_id
     */
    public static final String NEWS_TABLE_ID = "_id";

    /**
     * 新闻标题
     */
    public static final String NEWS_TABLE_TITLE = "title";

    /**
     * 图片路径
     */
    public static final String NEWS_TABLE_PICPATH = "pic_path";

    /**
     * 新闻url
     */
    public static final String NEWS_TABLE_URL = "url";

    /**
     * 新闻发布日期
     */
    public static final String NEWS_TABLE_DATE = "date";

    /**
     * 作者名字
     */
    public static final String NEWS_TABLE_AUTHOR_NAME = "author_name";

    /**
     * 新闻类型
     */
    public static final String NEWS_TABLE_NEWS_TYPE = "news_type";

    /**
     * 数据库帮助类
     */
    private SQLiteDatabaseHelper helper;

    /**
     * 数据库对象
     */
    private static SQLiteDatabase database;

    /**
     * 保证只有一个数据库管理类
     */
    private static DBManager manager;


    private DBManager(Context context) {
        helper = new SQLiteDatabaseHelper(context);
    }

    public static DBManager getDBManager(Context context) {
        if (manager == null) {
            manager = new DBManager(context);
        }
        return manager;
    }

    /**
     * 打开或创建数据库
     */
    public void getConnected() {
        database = helper.getReadableDatabase();
    }

    /**
     * 关闭数据库
     */
    public void disConnected() {
        database.close();
    }

    public void addNewsCacheToDataBase(List<News> news, String newsType) {
        getConnected();
        if (news != null && newsType != null) {
            for (News n : news) {
                ContentValues values = new ContentValues();
                values.put(NEWS_TABLE_TITLE, n.getTitle());
                values.put(NEWS_TABLE_PICPATH, n.getPicPath());
                values.put(NEWS_TABLE_AUTHOR_NAME, n.getAuthor_name());
                values.put(NEWS_TABLE_URL, n.getUrl());
                values.put(NEWS_TABLE_DATE, n.getDate());
                values.put(NEWS_TABLE_NEWS_TYPE, newsType);
                database.insert(NEWS_TABLE_NAME, null, values);
            }
        }
    }

    /**
     * 移除缓存
     *
     * @param newsType
     */
    public void deleteNewsCacheFromDataBase(String newsType) {
        getConnected();
        database.delete(NEWS_TABLE_NAME, NEWS_TABLE_NEWS_TYPE + " = ? ", new String[]{newsType});
    }

    /**
     * 从数据库读取缓存
     *
     * @param newsType 新闻类型
     * @return List<News> 新闻缓存项
     */
    public List<News> readNewsCacheFromDatabase(String newsType) {
        getConnected();
        Cursor cursor = database.query(NEWS_TABLE_NAME, null, NEWS_TABLE_NEWS_TYPE + " = ? ",
                new String[]{newsType}, null, null, null, null);
        List<News> lists = null;
        boolean isHaveCache = false;
        if (cursor != null) {
            lists = new ArrayList<News>();
            while (cursor.moveToNext()) {
                News news = new News();
                news.setTitle(cursor.getString(cursor.getColumnIndex(NEWS_TABLE_TITLE)));
                news.setAuthor_name(cursor.getString(cursor.getColumnIndex(NEWS_TABLE_AUTHOR_NAME)));
                news.setUrl(cursor.getString(cursor.getColumnIndex(NEWS_TABLE_URL)));
                news.setDate(cursor.getString(cursor.getColumnIndex(NEWS_TABLE_DATE)));
                news.setPicPath(cursor.getString(cursor.getColumnIndex(NEWS_TABLE_PICPATH)));
                lists.add(news);
                isHaveCache = true;
            }
        }
        if (!isHaveCache) lists = null;
        return lists;
    }

    /**
     * 更新数据库的缓存
     *
     * @param news     新的新闻
     * @param newsType 新闻类型
     */
    public void updataNewsCacheToDatabase(List<News> news, String newsType) {
        deleteNewsCacheFromDataBase(newsType);
        addNewsCacheToDataBase(news, newsType);
    }

}
