package com.example.parting_soul.news.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.parting_soul.news.MyApplication;
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

import static com.example.parting_soul.news.customview.LoadingPager.LoadState;
import static com.example.parting_soul.news.utils.CommonInfo.TAG;


/**
 * Created by parting_soul on 2016/10/4.
 * 新闻碎片类
 */

public class NewsFragment extends BaseFragment<News> implements AdapterView.OnItemClickListener {
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


//    /**
//     * 消息处理类
//     */
//    private AbstractDownLoadHandler mHandler = new AbstractDownLoadHandler() {
//
//        /**
//         * 该方法在主线程调用
//         * @param msg Looper从消息队列取出的消息
//         */
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case CommonInfo.LoaderStatus.DOWNLOAD_FINISH_MSG:
//
//                    break;
//                case CommonInfo.LoaderStatus.READ_CACHE_FROM_DATABASE_FINISH:
//
//                    break;
//            }
//        }
//
//        /**
//         * 下载错误时要处理的逻辑代码段
//         */
//        @Override
//        protected void showError() {
//
//        }
//
//        /**
//         * 下载正确则更新相应的UI
//         * @param msg
//         */
//        @Override
//        protected void updateUI(Message msg) {
//            //数据下载完成
//            if (msg.obj != null) {
//                mLists = (List<News>) msg.obj;
//                //将数据源绑定到适配器
//                mNewsInfoAdapter = new NewsInfoAdapter(getActivity(), mLists, mListView);
//                //设置可以加载图片
//                mNewsInfoAdapter.setIsCanLoadImage(true);
//                //为listview设置适配器
//                mListView.setAdapter(mNewsInfoAdapter);
//                mNewsInfoAdapter.notifyDataSetChanged();
//
//                LogUtils.d(CommonInfo.TAG, "-->lists 1" + mLists.size());
//
//
//                if (msg.what == CommonInfo.LoaderStatus.READ_CACHE_FROM_DATABASE_FINISH) {
//                    //恢复原来的位置
//                    mListView.setSelectionFromTop(oldFirstVisibleItem, top);
//                }
//            }
//        }
//    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LogUtils.d(TAG, "onAttach -->fragment");
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
        //得到所有请求参数
        mParams = initRequestUrlParam();
        //实例化数据库管理类
        manager = DBManager.getDBManager(getActivity());
        //得到图片加载类
        mImageLoader = ImageLoader.newInstance(getActivity());
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

    /**
     * 创建显示数据的界面
     *
     * @return View
     */
    @Override
    public View createSuccessPage() {
        View view = View.inflate(getActivity(), R.layout.news_fragment, null);
        mListView = (ListView) view.findViewById(R.id.news_lists);
        mListView.setOnItemClickListener(this);
        Log.d(TAG, "createSuccessPage -->fragment " + mNewTypeParam);
        return view;
    }

    @Override
    public void updataUI() {
        //将数据源绑定到适配器
        mNewsInfoAdapter = new NewsInfoAdapter(getActivity(), mLists, mListView);
        //设置可以加载图片
        mNewsInfoAdapter.setIsCanLoadImage(true);
        //为listview设置适配器
        mListView.setAdapter(mNewsInfoAdapter);
        mNewsInfoAdapter.notifyDataSetChanged();

        LogUtils.d(CommonInfo.TAG, "-->lists 1" + mLists.size());

//        if (msg.what == CommonInfo.LoaderStatus.READ_CACHE_FROM_DATABASE_FINISH) {
//            //恢复原来的位置
//            mListView.setSelectionFromTop(oldFirstVisibleItem, top);
//        }
    }

    /**
     * 从网络或数据库加载数据，工作在子线程，不需另外开辟线程<br>
     * 若有网络，则从网络获取数据，若没有则从数据库加载
     */
    @Override
    public LoadState loadData() {
        List<News> lists = null;
        if (!isNetworkAvailable()) {
            lists = manager.readNewsCacheFromDatabase(mNewTypeParam);
        } else {
            String result = HttpUtils.HttpPostMethod(CommonInfo.NewsAPI.Params.REQUEST_URL,
                    mParams, CommonInfo.ENCODE_TYPE);
            LogUtils.d(CommonInfo.TAG, "---->" + result);
            lists = parseJsonData(result);
        }
        mLists = lists;
        return getCheckResult(mLists);
    }

    /**
     * 解析下载的内容，并把数据缓存入数据库
     *
     * @param result 带解析的网络数据
     * @return List<News>
     */
    public List<News> parseJsonData(String result) {
        List<News> lists = null;
        //解析下载的数据
        lists = JsonParseTool.parseJsonWidthJSONObject(result);
        //将数据写入数据库
        manager.updataNewsCacheToDatabase(mLists, mNewTypeParam);
        return lists;
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

    /**
     * 检测当的网络（WLAN、4G/3G/2G）状态
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) MyApplication.getContext().
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

//    /**
//     * 下载成功时调用,该方法在子线程调用
//     *
//     * @param result 字符串形式的下载的结果
//     */
//    @Override
//    public void onResult(String result) {
//
//    }
//
//    /**
//     * 下载成功时调用,该方法在子线程调用
//     *
//     * @param result 字符数组形式的下载结果
//     */
//    @Override
//    public void onResult(byte[] result) {
//
//    }
//
//    /**
//     * 产生异常时调用,该方法在子线程调用
//     *
//     * @param e 异常类型
//     */
//    @Override
//    public void onError(Exception e) {
//        //       mHandler.sendEmptyMessage(CommonInfo.LoaderStatus.DOWNLOAD_FAILED_MSG);
//    }

}
