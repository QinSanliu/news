package com.example.parting_soul.news.utils.cache;

import com.example.parting_soul.news.Interface.ClearCacheCallBack;
import com.example.parting_soul.news.NewsApplication;
import com.example.parting_soul.news.utils.ImageLoader;
import com.example.parting_soul.news.utils.cache.database.DBManager;
import com.example.parting_soul.news.utils.cache.database.SQLiteDatabaseHelper;

import java.text.DecimalFormat;

/**
 * Created by parting_soul on 2016/10/23.
 */

public class CacheManager {

    public static final long B = 1;

    public static final long KB = 1024 * B;

    public static final long MB = 1024 * KB;

    public static final long GB = 1024 * MB;

    public static long DATABASE_INIT_SIZE = DBManager.databaseSize;

    /**
     * 得到缓存大小
     *
     * @return String
     */
    public static String getCacheSize() {
        ImageLoader loader = ImageLoader.newInstance(NewsApplication.getContext());
        long picSize = loader.getImageCacheSize();
        long dataSize = getDataBaseCacheSize();
        return transformToString(picSize + dataSize - DATABASE_INIT_SIZE);
    }

    /**
     * 清除所有缓存
     *
     * @param callback
     */
    public static void clearAllCache(ClearCacheCallBack callback) {
        ImageLoader loader = ImageLoader.newInstance(NewsApplication.getContext());
        DBManager manager = DBManager.getDBManager(NewsApplication.getContext());
        boolean isDataBaseClear = manager.deleteAllCacheFromDataBase();
        boolean isPicClear = loader.deleteImageCache();
        callback.finish(isDataBaseClear && isPicClear);
    }

    public static String transformToString(long size) {
        int gb = (int) (size / GB);
        StringBuilder cacheSize = new StringBuilder();
        DecimalFormat format = new DecimalFormat("0.00");
        if ((size / GB) >= 1) {
            cacheSize.append(format.format(size / (float) GB)).append("GB");
        } else if ((size % GB / MB) >= 1) {
            cacheSize.append(format.format((size % GB / (float) MB))).append("MB");
        } else if ((size % GB % MB / KB) >= 1) {
            cacheSize.append(format.format((size % GB % MB / (float) KB))).append("KB");
        } else if ((size & GB % MB % KB / B) >= 1) {
            cacheSize.append((size % GB % MB % KB / (float) B)).append("B");
        } else {
            cacheSize.append("0B");
        }
        return cacheSize.toString();
    }

    /**
     * 得到数据缓存大小
     *
     * @return
     */
    public static long getDataBaseCacheSize() {
        return NewsApplication.getContext().getDatabasePath(SQLiteDatabaseHelper.DATABASE_NAME).length();
    }

}
