package com.example.parting_soul.news.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.widget.ImageView;

import com.example.parting_soul.news.Interface.HttpCallBack;
import com.example.parting_soul.news.R;

import static android.R.attr.data;

/**
 * Created by parting_soul on 2016/10/5.
 * 用于下载图片
 */

public class ImageLoader implements HttpCallBack {
    private ImageView mImageView;
    private String mUrl;

    private DownLoadHandler mHandler = new DownLoadHandler() {
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
         * @param msg
         */
        @Override
        protected void updateUI(Message msg) {
            if (mImageView.getTag().equals(mUrl)) {
                Bitmap bitmap = (Bitmap) msg.obj;
                mImageView.setImageBitmap(bitmap);
            }
        }
    };

    public ImageLoader() {

    }

    /**
     * 子线程下载图片，并显示在对应的控件上
     *
     * @param path      图片路径
     * @param imageView 显示图片的控件
     */
    public void downLoadImage(final String path, ImageView imageView) {
        mImageView = imageView;
        mUrl = path;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtils.HttpGetMethod(mUrl, ImageLoader.this);
            }
        }).start();
    }

    /**
     * 下载成功时调用,该方法在子线程调用
     *
     * @param result 字符串形式的下载的结果
     */
    @Override
    public void onResult(String result) {

    }

    /**
     * 下载成功时调用,该方法在子线程调用
     *
     * @param result 字符数组形式的下载结果
     */
    @Override
    public void onResult(byte[] result) {
        //将字符数组解析为Bitmap对象
        Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0, result.length);
        //得到消息
        Message msg = Message.obtain();

        msg.obj = bitmap;
        msg.what = CommonInfo.DownloadStatus.DOWNLOAD_FINISH_MSG;
        //发送消息
        mHandler.sendMessage(msg);
    }

    /**
     * 产生异常时调用,该方法在子线程调用
     *
     * @param e 异常类型
     */
    @Override
    public void onError(Exception e) {
        mHandler.sendEmptyMessage(CommonInfo.DownloadStatus.DOWNLOAD_FAILED_MSG);
    }
}
