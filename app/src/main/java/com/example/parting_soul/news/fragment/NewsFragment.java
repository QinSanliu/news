package com.example.parting_soul.news.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.parting_soul.news.Interface.HttpCallBack;
import com.example.parting_soul.news.R;
import com.example.parting_soul.news.adapter.NewsInfoAdapter;
import com.example.parting_soul.news.bean.News;
import com.example.parting_soul.news.utils.CommonInfo;
import com.example.parting_soul.news.utils.DownLoadHandler;
import com.example.parting_soul.news.utils.HttpUtils;
import com.example.parting_soul.news.utils.JsonParseTool;
import com.example.parting_soul.news.utils.LogUtils;

import java.util.List;

import static com.example.parting_soul.news.utils.CommonInfo.TAG;


/**
 * Created by parting_soul on 2016/10/4.
 * 新闻碎片类
 */

public class NewsFragment extends BaseFragment implements AdapterView.OnItemClickListener, HttpCallBack {
    /**
     * 存放新闻项的listview
     */
    private ListView mListView;
    /**
     * 新闻信息适配器
     */
    private NewsInfoAdapter mNewsInfoAdapter;
    /**
     * 新闻数组
     */
    private List<News> mLists;
    /**
     * 请求地址中的新闻类别参数
     */
    private String mNewTypeParam;
    /**
     * 进度条对话框
     */
    private ProgressDialog mDialog;
    /**
     * 请求地址的所有参数
     */
    private String mParams;

    /**
     * 消息处理类
     */
    private DownLoadHandler mHandler = new DownLoadHandler() {
        /**
         * 该方法在主线程调用
         * @param msg Looper从消息队列取出的消息
         */
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
            //关闭进度条对话框
            mDialog.dismiss();
        }

        /**
         * 下载错误时要处理的逻辑代码段
         */
        @Override
        protected void showError() {

        }

        /**
         * 下载正确则更新相应的UI
         * @param msg
         */
        @Override
        protected void updateUI(Message msg) {
            //数据下载完成
            if (msg.obj != null) {
                mLists = (List<News>) msg.obj;
                //将数据源绑定到适配器
                mNewsInfoAdapter = new NewsInfoAdapter(getContext(), mLists);
                //为listview设置适配器
                mListView.setAdapter(mNewsInfoAdapter);
                mNewsInfoAdapter.notifyDataSetChanged();
            }
        }
    };

    /**
     * 初始化下载进度条
     */
    private void initProgressDialog() {
        mDialog = new ProgressDialog(getContext());
        mDialog.setTitle("加载");
        mDialog.setMessage("正在加载...");
        mDialog.setCancelable(false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtils.d(TAG, "onAttach -->fragment");
    }

    /**
     * 当页面可见并且UI已经绘制完成，异步任务加载数据
     */
    @Override
    public void loadData() {
        if (mIsVisibleToUser && mIsPrepared) {
            new DownloadNewsThread().start();
            //子线程下载的同时显示进度条对话框
            mDialog.show();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNewTypeParam = getArguments().getString(CommonInfo.NewsAPI.Params.REQUEST_TYPE_PARAM_NAME);
        initProgressDialog();
        mParams = initRequestUrlParam();
        LogUtils.d(TAG, "onCreate -->fragment " + mNewTypeParam + " " + this);
    }

    /**
     * 拼接http url的地址参数
     *
     * @return String 返回拼接后的参数
     */
    private String initRequestUrlParam() {
        StringBuilder params = new StringBuilder();
        params.append(CommonInfo.NewsAPI.Params.REQUEST_URL_TYPR_NAME).append("=").append(mNewTypeParam)
                .append("&").append(CommonInfo.NewsAPI.Params.REQUEST_URL_KEY_NAME).append("=")
                .append(CommonInfo.NewsAPI.Params.REQUEST_URL_KEY_VALUE);
        return params.toString();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_fragment, container, false);
        mListView = (ListView) view.findViewById(R.id.news_lists);
        mListView.setOnItemClickListener(this);
        //UI加载完成的标志置为true
        mIsPrepared = true;
        loadData();
        Log.d(TAG, "onCreateView -->fragment " + mNewTypeParam);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.d(TAG, "onActivityCreated -->fragment " + mNewTypeParam);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    /**
     * 下载成功时调用,该方法在子线程调用
     *
     * @param result 字符串形式的下载的结果
     */
    @Override
    public void onResult(String result) {
        List<News> lists = null;
        //解析下载的数据
        lists = JsonParseTool.parseJsonWidthJSONObject(result);
        //从消息池取出一个空的消息对象
        Message msg = Message.obtain();
        //将解析后的数据绑定在消息对象上
        msg.obj = lists;
        //标记为下载完成
        msg.what = CommonInfo.DownloadStatus.DOWNLOAD_FINISH_MSG;
        //Handler将消息发送到消息队列
        mHandler.sendMessage(msg);
    }

    /**
     * 下载成功时调用,该方法在子线程调用
     *
     * @param result 字符数组形式的下载结果
     */
    @Override
    public void onResult(byte[] result) {

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

    /**
     * 子线程进行新闻数据下载，得到json数据并且进行解析,返回新闻类的数组
     */
    class DownloadNewsThread extends Thread {
        @Override
        public void run() {
            //下载数据
            HttpUtils.HttpPostMethod(CommonInfo.NewsAPI.Params.REQUEST_URL,
                    mParams, CommonInfo.ENCODE_TYPE, NewsFragment.this);
        }
    }

}
