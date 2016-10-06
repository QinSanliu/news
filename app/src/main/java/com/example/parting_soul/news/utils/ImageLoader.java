package com.example.parting_soul.news.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.parting_soul.news.Interface.HttpCallBack;
import com.example.parting_soul.news.R;
import com.example.parting_soul.news.adapter.NewsInfoAdapter;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by parting_soul on 2016/10/5.
 * 用于下载图片
 */

public class ImageLoader {
    /**
     * 新闻数据项
     */
    private ListView mListView;

    /**
     * 下载线程的集合
     */
    private Set<Thread> mThreadSets;

    /**
     * 一级缓存，url和图片的映射
     */
    private LruCache<String, Bitmap> mCache;

    /**
     * 一级缓存的大小
     */
    private int mCachesMemory = 10 * 1024 * 1024;

    /**
     * 构造方法
     *
     * @param listview 新闻数据项
     */
    public ImageLoader(ListView listview) {
        mListView = listview;
        mThreadSets = new HashSet<Thread>();
        mCache = new LruCache<String, Bitmap>(mCachesMemory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                // 每次存入缓存时调用
                return value.getByteCount();
            }
        };
    }

    /**
     * 加载从位置start开始到end之间所有的图片<br>
     * 滚动停止时调用，若存在图片则加载，若不存在则从网络下载
     *
     * @param start 可见新闻项的开始位置
     * @param end   可见新闻项的结束位置
     */
    public void loadImage(int start, int end) {
        for (int i = start; i < end; i++) {
            //找到对应的图片url地址
            String url = NewsInfoAdapter.IMAGE_URLS[i];
            //根据url从一级缓存中找是否有该图片
            Bitmap bitmap = getBitmapFromCache(url);
            //根据图片控件Ta属性上绑定的url从listview中找到图片控件
            ImageView imageView = (ImageView) mListView.findViewWithTag(url);
            if (bitmap != null) {
                //若成功从缓存中找到图片
                imageView.setImageBitmap(bitmap);
                LogUtils.d(CommonInfo.TAG, "bitmap from cache");
            } else {
                //找不到图片则从网络下载图片
                downLoadImage(url, imageView);
                LogUtils.d(CommonInfo.TAG, "bitmap from web");
            }
        }

    }

    /**
     * 根据url加载图片到指定的图片控件<br>
     * 滚动时调用，若缓存中有该图片，则加载，若没有该图片显示默认图片
     *
     * @param url       图片url地址
     * @param imageView 图片控件的引用
     */
    public void loadImage(String url, ImageView imageView) {
        Bitmap bitmap = getBitmapFromCache(url);
        if (bitmap != null) {
            //加载缓存中的图片
            imageView.setImageBitmap(bitmap);
            LogUtils.d(CommonInfo.TAG, "bitmap from cache");
        } else {
            //缓存中没有找到该图片，显示默认图片
            imageView.setImageResource(R.mipmap.imageview_default_bc);
            LogUtils.d(CommonInfo.TAG, "bitmap from default");
        }
    }

    /**
     * 以图片的url为键找到对应的bitmap值
     *
     * @param url
     * @return Bitmap 找到则返回图片，找不到返回null
     */
    @Nullable
    private Bitmap getBitmapFromCache(String url) {
        return mCache.get(url);
    }

    /**
     * 若缓存中没有该图片，则加入缓存
     *
     * @param url    图片的url地址(键)
     * @param bitmap 图片Bitmap对象(值)
     */
    public void addBitmapToCache(String url, Bitmap bitmap) {
        if (mCache.get(url) == null) {
            mCache.put(url, bitmap);
        }
    }

    /**
     * 停止所有正在执行的线程
     */
    public void cancelAllDownThreads() {
        if (mThreadSets != null) {
            for (Thread t : mThreadSets) {
                if (t.isAlive()) {
                    //                 t.stop();
                }
            }
        }
    }

    /**
     * 子线程下载图片，并显示在对应的控件上
     *
     * @param path      图片路径
     * @param imageView 显示图片的控件
     */
    public void downLoadImage(final String url, ImageView imageView) {
        DownLoadHandler handler = new DownLoadHandler(url, imageView);
        DownLoadThread thread = new DownLoadThread(url, handler);
        thread.start();
        mThreadSets.add(thread);
    }

    /**
     * 图片下载线程
     */
    class DownLoadThread extends Thread {
        private String mUrl;
        private DownLoadHandler mHandler;

        public DownLoadThread(String url, DownLoadHandler handler) {
            mUrl = url;
            mHandler = handler;
        }

        @Override
        public void run() {
            HttpUtils.HttpGetMethod(mUrl, new HttpCallBack() {

                @Override
                public void onResult(String result) {

                }

                @Override
                public void onResult(byte[] result) {
                    //将字符数组解析为Bitmap对象
                    Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0, result.length);

                    if (bitmap != null) {
                        //将图片加入到内存中
                        addBitmapToCache(mUrl, bitmap);
                        //得到消息
                        Message msg = Message.obtain();
                        msg.obj = bitmap;
                        msg.what = CommonInfo.DownloadStatus.DOWNLOAD_FINISH_MSG;
                        //发送消息
                        mHandler.sendMessage(msg);
                    } else {
                        //如果图片为空就交给异常代码块处理
                        onError(new Exception());
                    }
                    mThreadSets.remove(this);
                }

                @Override
                public void onError(Exception e) {
                    mHandler.sendEmptyMessage(CommonInfo.DownloadStatus.DOWNLOAD_FAILED_MSG);
                    mThreadSets.remove(this);
                }

            });
        }
    }

    class DownLoadHandler extends AbstractDownLoadHandler {
        /**
         * 待显示图片的控件
         */
        private ImageView mImageView;
        /**
         * 图片的URL
         */
        private String mUrl;

        public DownLoadHandler(String url, ImageView imageView) {
            mImageView = imageView;
            mUrl = url;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CommonInfo.DownloadStatus.DOWNLOAD_FINISH_MSG:
                    updateUI(msg);
                    break;
                case CommonInfo.DownloadStatus.DOWNLOAD_FAILED_MSG:
                    showError();
                    break;
            }
        }

        /**
         * 下载错误时要处理的逻辑代码段
         */
        @Override
        protected void showError() {
            //图片下载错误就显示默认图片
            mImageView.setImageResource(R.mipmap.imageview_error_bc);
        }

        /**
         * 下载正确则更新相应的UI
         *
         * @param msg
         */
        @Override
        protected void updateUI(Message msg) {
            Log.i(CommonInfo.TAG, "-->" + mImageView.getTag());
            if (mImageView.getTag().equals(mUrl)) {
                Bitmap bitmap = (Bitmap) msg.obj;
                mImageView.setImageBitmap(bitmap);
            }
        }
    }

}
