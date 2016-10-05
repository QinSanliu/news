package com.example.parting_soul.news.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

/**
 * Created by parting_soul on 2016/10/5.
 * 用于下载图片
 */

public class ImageLoader {
    /**
     * 下载完成标识字段
     */
    private static final int DOWNLOAD_FINISH = 1;

    private ImageView mImageView;
    private String mUrl;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == DOWNLOAD_FINISH && mImageView.getTag().equals(mUrl)) {
                Bitmap bitmap = (Bitmap) msg.obj;
                mImageView.setImageBitmap(bitmap);
            }
        }
    };

    public ImageLoader() {

    }

    public void downLoadImage(final String path, ImageView imageView) {
        mImageView = imageView;
        mUrl = path;
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] data = HttpUtils.HttpGetMethod(path);
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Message msg = Message.obtain();
                msg.obj = bitmap;
                msg.what = DOWNLOAD_FINISH;
                mHandler.sendMessage(msg);
            }
        }).start();
    }
}
