package com.example.parting_soul.news.utils.cache.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.parting_soul.news.bean.News;
import com.example.parting_soul.news.utils.support.CommonInfo;
import com.example.parting_soul.news.utils.support.LogUtils;

import java.util.ArrayList;
import java.util.List;

import static com.example.parting_soul.news.utils.cache.database.NewsTable.NEWS_TABLE_AUTHOR_NAME;
import static com.example.parting_soul.news.utils.cache.database.NewsTable.NEWS_TABLE_DATE;
import static com.example.parting_soul.news.utils.cache.database.NewsTable.NEWS_TABLE_IS_COLLECTION;
import static com.example.parting_soul.news.utils.cache.database.NewsTable.NEWS_TABLE_NAME;
import static com.example.parting_soul.news.utils.cache.database.NewsTable.NEWS_TABLE_PICPATH;
import static com.example.parting_soul.news.utils.cache.database.NewsTable.NEWS_TABLE_TITLE;
import static com.example.parting_soul.news.utils.cache.database.NewsTable.NEWS_TABLE_URL;

/**
 * Created by parting_soul on 2016/10/9.
 * 数据库管理类
 */

public class DBManager {


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

    /**
     * 是否是第一次打开数据库
     */
    public static boolean isFirst = true;

    /**
     * 数据库为空时的文件大小
     */
    public static long databaseSize;

    private Context mContext;

    private DBManager(Context context) {
        helper = new SQLiteDatabaseHelper(context);
        mContext = context;
    }

    public static DBManager getDBManager(Context context) {
        if (manager == null) {
            manager = new DBManager(context);
        }
        return manager;
    }

    /**
     * 打开或创建数据库,并得到数据库为空时的文件大小
     */
    public void getConnected() {
        database = helper.getReadableDatabase();
        if (isFirst) {
            databaseSize = mContext.getDatabasePath(SQLiteDatabaseHelper.DATABASE_NAME).length();
            LogUtils.d(CommonInfo.TAG, "--->123 " + databaseSize);
            isFirst = false;
        }
    }

    /**
     * 关闭数据库
     */
    public void disConnected() {
        database.close();
    }

    /**
     * 添加新闻缓存到数据库，存在就忽略插入
     *
     * @param news
     * @param newsType
     */
    public void addNewsCacheToDataBase(List<News> news, String newsType) {
        getConnected();
        long re = -1;
        if (news != null && newsType != null) {
            for (News n : news) {
                StringBuilder sql = new StringBuilder();
                sql.append("insert or replace into ").append(NewsTable.NEWS_TABLE_NAME).append(" ( ")
                        .append(NewsTable.NEWS_TABLE_TITLE + "," + NewsTable.NEWS_TABLE_PICPATH + "," + NewsTable.NEWS_TABLE_AUTHOR_NAME
                                + "," + NewsTable.NEWS_TABLE_URL + "," + NewsTable.NEWS_TABLE_DATE + "," + NewsTable.NEWS_TABLE_NEWS_TYPE + "," + NewsTable.NEWS_TABLE_IS_COLLECTION)
                        .append(" ) values( ").append("'").append(n.getTitle() + "','" + n.getPicPath() + "','" + n.getAuthor_name()
                        + "','" + n.getUrl() + "','" + n.getDate() + "','" + newsType).append("',(").append("select ")
                        .append(NewsTable.NEWS_TABLE_IS_COLLECTION).append(" from ").append(NewsTable.NEWS_TABLE_NAME).append(" where title = '" + n.getTitle() + "') )");
                database.execSQL(sql.toString());
                LogUtils.d(CommonInfo.TAG, "--->" + sql.toString());
            }

        }
    }

    /**
     * 移除所有缓存
     *
     * @param newsType
     */
    public void deleteNewsCacheFromDataBase(String newsType) {
        getConnected();
        database.delete(NewsTable.NEWS_TABLE_NAME, NewsTable.NEWS_TABLE_NEWS_TYPE + " = ? and (" + NewsTable.NEWS_TABLE_IS_COLLECTION + " = ? or  "
                + NEWS_TABLE_IS_COLLECTION + " is null )", new String[]{newsType, "0"});
    }

    /**
     * 移除所有数据缓存
     */
    public boolean deleteAllCacheFromDataBase() {
        getConnected();
        int result = database.delete(NewsTable.NEWS_TABLE_NAME, NewsTable.NEWS_TABLE_IS_COLLECTION + " != ? or  "
                + NEWS_TABLE_IS_COLLECTION + " is null ", new String[]{"0"});
        LogUtils.d(CommonInfo.TAG, "--->123 result " + result);
        if (result != -1) return true;
        return false;
    }


    /**
     * 从数据库读取缓存
     *
     * @param newsType 新闻类型
     * @return List<News> 新闻缓存项
     */
    public List<News> readNewsCacheFromDatabase(String newsType) {
        getConnected();
        Cursor cursor = database.query(NewsTable.NEWS_TABLE_NAME, null, NewsTable.NEWS_TABLE_NEWS_TYPE + " = ? ",
                new String[]{newsType}, null, null, null, null);
        //   Cursor cursor = database.rawQuery("select rowid,* from newsinfo where news_type = ? ", new String[]{newsType});
        List<News> lists = null;
        boolean isHaveCache = false;
        if (cursor != null) {
            lists = new ArrayList<News>();
            while (cursor.moveToNext()) {
                News news = new News();
                //               int rowid = cursor.getInt(cursor.getColumnIndex("rowid"));
                news.setTitle(cursor.getString(cursor.getColumnIndex(NewsTable.NEWS_TABLE_TITLE)));
                news.setAuthor_name(cursor.getString(cursor.getColumnIndex(NewsTable.NEWS_TABLE_AUTHOR_NAME)));
                news.setUrl(cursor.getString(cursor.getColumnIndex(NewsTable.NEWS_TABLE_URL)));
                news.setDate(cursor.getString(cursor.getColumnIndex(NewsTable.NEWS_TABLE_DATE)));
                news.setPicPath(cursor.getString(cursor.getColumnIndex(NewsTable.NEWS_TABLE_PICPATH)));
                news.setIs_collected(cursor.getInt(cursor.getColumnIndex(NewsTable.NEWS_TABLE_IS_COLLECTION)) == 1);
                lists.add(news);
                //             LogUtils.d(CommonInfo.TAG, "-->read" + rowid + " " + news.getTitle() + " " + news.is_collected());
                isHaveCache = true;
            }
        }
        if (!isHaveCache) lists = null;
        LogUtils.d(CommonInfo.TAG, "-->" + lists);
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

    /**
     * 读取收藏的新闻项
     *
     * @return
     */
    public List<News> readCollectionNews() {
        getConnected();
        Cursor cursor = database.query(NEWS_TABLE_NAME, null, NEWS_TABLE_IS_COLLECTION + " = ? ",
                new String[]{"1"}, null, null, null, null);
        List<News> lists = new ArrayList<News>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                News news = new News();
                news.setTitle(cursor.getString(cursor.getColumnIndex(NEWS_TABLE_TITLE)));
                news.setAuthor_name(cursor.getString(cursor.getColumnIndex(NEWS_TABLE_AUTHOR_NAME)));
                news.setUrl(cursor.getString(cursor.getColumnIndex(NEWS_TABLE_URL)));
                news.setDate(cursor.getString(cursor.getColumnIndex(NEWS_TABLE_DATE)));
                news.setPicPath(cursor.getString(cursor.getColumnIndex(NEWS_TABLE_PICPATH)));
                news.setIs_collected(cursor.getInt(cursor.getColumnIndex(NEWS_TABLE_IS_COLLECTION)) == 1);
                lists.add(news);
            }
        }
        return lists;
    }

    /**
     * 添加收藏新闻项
     *
     * @param title
     * @return boolean
     */
    public boolean addNewsCollectionToDataBase(String title) {
        getConnected();
        int result = -1;
        if (title != null) {
            ContentValues values = new ContentValues();
            values.put(NEWS_TABLE_IS_COLLECTION, "1");
            result = database.update(NEWS_TABLE_NAME, values, NEWS_TABLE_TITLE + " = ? ", new String[]{title});
        }
        LogUtils.d(CommonInfo.TAG, "asdff" + result);
        return result == -1 ? false : true;
    }

    /**
     * 移除收藏新闻项
     *
     * @param title
     * @return boolean
     */
    public boolean deleteNewsCollectionFromDataBase(String title) {
        getConnected();
        int result = -1;
        if (title != null) {
            ContentValues values = new ContentValues();
            values.put(NEWS_TABLE_IS_COLLECTION, "0");
            result = database.update(NEWS_TABLE_NAME, values, NEWS_TABLE_TITLE + " = ? ", new String[]{title});
        }
        return result == -1 ? false : true;
    }
}