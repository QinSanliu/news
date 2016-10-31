package com.example.parting_soul.news.utils.cache.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.parting_soul.news.utils.CommonInfo;
import com.example.parting_soul.news.utils.LogUtils;

/**
 * Created by parting_soul on 2016/10/9.
 * 数据库帮助类
 */

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {
    /**
     * 数据库名
     */
    public static final String DATABASE_NAME = "news.db";
    /**
     * 数据库版本
     */
    private static final int DATABASE_VERSION = 1;


    public static final String CREATE_NEWS_TABLE = "create table " + DBManager.NEWS_TABLE_NAME + "("
            + DBManager.NEWS_TABLE_TITLE + " text primary key,"
            + DBManager.NEWS_TABLE_URL + " text,"
            + DBManager.NEWS_TABLE_PICPATH + " text,"
            + DBManager.NEWS_TABLE_DATE + " text,"
            + DBManager.NEWS_TABLE_AUTHOR_NAME + " text,"
            + DBManager.NEWS_TABLE_NEWS_TYPE + " text,"
            + DBManager.NEWS_TABLE_IS_COLLECTION + " integer default 0 " + ")";

    public SQLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        LogUtils.d(CommonInfo.TAG, CREATE_NEWS_TABLE);
    }

    /**
     * 初始化数据库，创建表
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NEWS_TABLE);
    }

    /**
     * 升级数据库
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
