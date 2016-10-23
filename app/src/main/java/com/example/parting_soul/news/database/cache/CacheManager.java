package com.example.parting_soul.news.database.cache;

import com.example.parting_soul.news.NewsApplication;
import com.example.parting_soul.news.utils.ImageLoader;

import java.text.DecimalFormat;

/**
 * Created by parting_soul on 2016/10/23.
 */

public class CacheManager {

    public static final long B = 1;

    public static final long KB = 1024 * B;

    public static final long MB = 1024 * KB;

    public static final long GB = 1024 * MB;

    public static String getPicSize() {
        ImageLoader loader = ImageLoader.newInstance(NewsApplication.getContext());
        long size = loader.getImageCacheSize();
        StringBuilder cacheSize = new StringBuilder();
        DecimalFormat format = new DecimalFormat("0.00");
        int gb = (int) (size / GB);
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

    public static boolean clearPicCache() {
        return false;
    }

    public static boolean clearAllCache() {
        return false;
    }

}
