package com.example.parting_soul.news.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by parting_soul on 2016/10/7.
 * 大分辨率图缩放工具类
 */

public class ImageZoom {

    /**
     * @param options
     * @param requestWidth  自定义缩放的宽度
     * @param requestHeight 自定义缩放的高度
     * @return int 缩放级别
     */
    public static int calculateInSimpleSize(BitmapFactory.Options options,
                                            int requestWidth, int requestHeight) {
        //得到从网络下载图片真实的宽度
        int realWidth = options.outWidth;
        //真实的高度
        int realHeight = options.outHeight;
        //默认缩放级别
        int inSimpleSize = 1;
        if (realWidth > requestWidth || realHeight > requestHeight) {
            //若真实图片比所需图片要大
            //得到宽度的比率
            int widthRadio = (int) Math.round(realWidth / requestWidth);
            //得到高度的比率
            int heightRadio = (int) Math.round(realHeight / requestHeight);
            //取缩放比率小的为缩放比率，使得图片至少比规定图片略大
            inSimpleSize = widthRadio > heightRadio ? heightRadio : widthRadio;
        }
        return inSimpleSize;
    }

    /**
     * 将从网络以字节流方式下载的大分辨图片缩放为小分辨率图片
     *
     * @param data          从网络下载的字节流图片
     * @param requestWidth  自定义缩放的宽度
     * @param requestHeight 自定义缩放的高度
     * @return Bitmap 缩放后解析出的位图对象
     */
    public static Bitmap decodeSimpleBitmapFromByte(byte[] data, int requestWidth, int requestHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //标识为true则禁止给Bitmap分配内存，只是取得真实的图片高度和宽度
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        //得到缩放级别
        int inSimpleSize = calculateInSimpleSize(options, requestWidth, requestHeight);
        //根据缩放级别解析出Bitmap对象
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }
}
