package com.example.parting_soul.news.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.parting_soul.news.R;
import com.example.parting_soul.news.adapter.NewsInfoAdapter;
import com.example.parting_soul.news.bean.News;
import com.example.parting_soul.news.utils.CommonInfo;
import com.example.parting_soul.news.utils.HttpUtils;
import com.example.parting_soul.news.utils.JsonParseTool;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.parting_soul.news.utils.JsonParseTool.parseJsonWidthJSONObject;


/**
 * Created by parting_soul on 2016/10/4.
 * 新闻碎片类
 */

public class NewsFragment extends Fragment implements AdapterView.OnItemClickListener {
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
     * 是否是用户当前看到的页面
     */
    private boolean isUserVisiblePage;

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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isUserVisiblePage = isVisibleToUser;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNewTypeParam = getArguments().getString(CommonInfo.NewsAPI.Params.REQUEST_TYPE_PARAM_NAME);
        initProgressDialog();
        mParams = initRequestUrlParam();
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
        //if (isUserVisiblePage) {
            new DownLoadNewsInfoAsyncTask().execute(mParams);
        //}
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    /**
     * 异步任务进行新闻数据下载，得到json数据并且进行解析,返回新闻类的数组
     */
    class DownLoadNewsInfoAsyncTask extends AsyncTask<String, Void, List<News>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.show();
        }

        @Override
        protected List<News> doInBackground(String... params) {
            List<News> lists = null;
            //下载数据
            String jsonString = HttpUtils.HttpPostMethod(CommonInfo.NewsAPI.Params.REQUEST_URL,
                    params[0], CommonInfo.ENCODE_TYPE);
            //解析下载的数据
            lists = JsonParseTool.parseJsonWidthJSONObject(jsonString);
            return lists;
        }

        @Override
        protected void onPostExecute(List<News> lists) {
            super.onPostExecute(lists);
            mLists = lists;
            mNewsInfoAdapter = new NewsInfoAdapter(getContext(), mLists);
            mListView.setAdapter(mNewsInfoAdapter);
            mNewsInfoAdapter.notifyDataSetChanged();
            mDialog.dismiss();
        }

    }

}