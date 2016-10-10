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
import com.example.parting_soul.news.database.DBManager;
import com.example.parting_soul.news.utils.AbstractDownLoadHandler;
import com.example.parting_soul.news.utils.CommonInfo;
import com.example.parting_soul.news.utils.HttpUtils;
import com.example.parting_soul.news.utils.ImageLoader;
import com.example.parting_soul.news.utils.JsonParseTool;
import com.example.parting_soul.news.utils.LogUtils;

import java.util.List;

import static android.R.attr.data;
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
     * 数据库管理类
     */
    private DBManager manager;

    /**
     * 旧状态时第一个ListView可见项
     */
    private int oldFirstVisibleItem;
    /**
     * 第一个listview item距离listview的位置
     */
    private int top;

    /**
     * 图片加载类
     */
    private ImageLoader mImageLoader;

    /**
     * 消息处理类
     */
    private AbstractDownLoadHandler mHandler = new AbstractDownLoadHandler() {
        /**
         * 该方法在主线程调用
         * @param msg Looper从消息队列取出的消息
         */
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CommonInfo.LoaderStatus.DOWNLOAD_FINISH_MSG:
                    updateUI(msg);
                    mDialog.dismiss();
                    break;
                case CommonInfo.LoaderStatus.DOWNLOAD_FAILED_MSG:
                    showError();
                    mDialog.dismiss();
                    break;
                case CommonInfo.LoaderStatus.READ_CACHE_FROM_DATABASE_FINISH:
                    updateUI(msg);
                    break;
                case CommonInfo.LoaderStatus.SHOW_PROGRESS_DIALOG:
                    mDialog.show();
                    break;
            }
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
                mNewsInfoAdapter = new NewsInfoAdapter(getContext(), mLists, mListView);
                //设置可以加载图片
                mNewsInfoAdapter.setIsCanLoadImage(true);
                //为listview设置适配器
                mListView.setAdapter(mNewsInfoAdapter);
                mNewsInfoAdapter.notifyDataSetChanged();
                if (msg.what == CommonInfo.LoaderStatus.READ_CACHE_FROM_DATABASE_FINISH) {
                    //恢复原来的位置
                    mListView.setSelectionFromTop(oldFirstVisibleItem, top);
                }
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
            new LoaderNewsThread().start();
           // mDialog.show();
        }
    }

    @Override
    protected void onInVisible() {
        super.onInVisible();
        if (mNewsInfoAdapter != null) {
            //当前页面不可见就设置为不加载图片
            mNewsInfoAdapter.setIsCanLoadImage(false);
            //保存侧滑前listview中第一个可见项目的位置
            oldFirstVisibleItem = mListView.getFirstVisiblePosition();
            View view = mListView.getChildAt(0);
            if (view != null) {
                top = view.getTop();
            }
        }
        LogUtils.d(CommonInfo.TAG, "InvisibletoUser " + oldFirstVisibleItem + " " + top + " " + mNewTypeParam);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNewTypeParam = getArguments().getString(CommonInfo.NewsAPI.Params.REQUEST_TYPE_PARAM_NAME);
        //初始化进度条对话框
        initProgressDialog();
        //得到所有请求参数
        mParams = initRequestUrlParam();
        //实例化数据库管理类
        manager = DBManager.getDBManager(getContext());
        //得到图片加载类
        mImageLoader = ImageLoader.newInstance(getContext());
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
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume -->fragment " + mNewTypeParam);
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause -->fragment " + mNewTypeParam);
        if (mImageLoader != null) {
            //将图片同步记录写入journal文件
            mImageLoader.fluchCache();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView -->fragment " + mNewTypeParam);
        //退出当前pager，停止所有加载图片的异步任务
        if (mImageLoader != null) {
            mImageLoader.cancelAllAsyncTask();
        }
        //UI被销毁，item位置重新初始化
        top = 0;
        oldFirstVisibleItem = 0;
        mNewsInfoAdapter = null;
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
        msg.what = CommonInfo.LoaderStatus.DOWNLOAD_FINISH_MSG;
        //Handler将消息发送到消息队列
        mHandler.sendMessage(msg);
        //将数据加入数据库缓存
        DBManager.getDBManager(getContext()).updataNewsCacheToDatabase(lists, mNewTypeParam);
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
        mHandler.sendEmptyMessage(CommonInfo.LoaderStatus.DOWNLOAD_FAILED_MSG);
    }


    /**
     * 子线程进行新闻数据下载，得到json数据并且进行解析,返回新闻类的数组
     */
    class LoaderNewsThread extends Thread {
        @Override
        public void run() {
            List<News> data = manager.readNewsCacheFromDatabase(mNewTypeParam);
            //先从数据库读取对应缓存，若有直接读取，没有则去下载
            if (data != null && data.size() != 0) {
                Message msg = Message.obtain();
                msg.obj = data;
                msg.what = CommonInfo.LoaderStatus.READ_CACHE_FROM_DATABASE_FINISH;
                mHandler.sendMessage(msg);
                LogUtils.d(CommonInfo.TAG, " load from sqlitedatabase " + oldFirstVisibleItem + " " + top);
            } else {
                //通知主线程显示进度条
                mHandler.sendEmptyMessage(CommonInfo.LoaderStatus.SHOW_PROGRESS_DIALOG);
                //下载数据
                HttpUtils.HttpPostMethod(CommonInfo.NewsAPI.Params.REQUEST_URL,
                        mParams, CommonInfo.ENCODE_TYPE, NewsFragment.this);
                LogUtils.d(CommonInfo.TAG, " load from web");
            }
        }
    }

}
