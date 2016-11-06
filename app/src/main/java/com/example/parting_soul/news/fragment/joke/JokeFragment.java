package com.example.parting_soul.news.fragment.joke;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.parting_soul.news.R;
import com.example.parting_soul.news.activity.MainActivity;
import com.example.parting_soul.news.adapter.JokeFragmentAdapter;
import com.example.parting_soul.news.bean.Joke;
import com.example.parting_soul.news.customview.LoadingPager;
import com.example.parting_soul.news.fragment.support.BaseFragment;
import com.example.parting_soul.news.utils.cache.database.DBManager;
import com.example.parting_soul.news.utils.network.HttpUtils;
import com.example.parting_soul.news.utils.network.JsonParseTool;
import com.example.parting_soul.news.utils.network.NetworkInfo;
import com.example.parting_soul.news.utils.support.CommonInfo;
import com.example.parting_soul.news.utils.support.LogUtils;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by parting_soul on 2016/11/1.
 */

public class JokeFragment extends BaseFragment<Joke> implements PullToRefreshView.OnRefreshListener,
        AbsListView.OnScrollListener {
    public static final String NAME = "JokeFragment";

    private JokeFragmentAdapter mJokeFragmentAdapter;

    private ListView mListView;

    private PullToRefreshView mPullToRefreshView;

    private List<Joke> mLists;

    private int mRequestPage = 1;

    private DBManager mDBManager;

    private String mParams;

    private static final int REQUEST_ITEM_NUMS = 20;

    private LinearLayout mLoadMore;

    private int mTotalItemNums;

    private Set<LoadDataAsync> mSets;

    private boolean isLoadingMore;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setTitleName(R.string.funny);
        mDBManager = DBManager.getDBManager(getActivity());
        mSets = new HashSet<LoadDataAsync>();
    }

    @Override
    public View createSuccessPage() {
        View view = View.inflate(getActivity(), R.layout.funny_fragment_layout, null);
        mListView = (ListView) view.findViewById(R.id.list_view);
        //      mListView.setOnLoadMoreListener(this);
        mListView.setOnScrollListener(this);
        mPullToRefreshView = (PullToRefreshView) view.findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void updataUI() {
        mJokeFragmentAdapter = new JokeFragmentAdapter(getActivity(), mLists);
        mListView.setAdapter(mJokeFragmentAdapter);
        mJokeFragmentAdapter.notifyDataSetChanged();
        LogUtils.d(CommonInfo.TAG, "-->lists  " + NAME + " " + mLists.size());
    }


    @Override
    protected String initRequestUrlParam() {
        StringBuilder params = new StringBuilder();
        params.append(CommonInfo.JokeApI.Param.JOKE_REQUEST_PAGE).append("=").append(mRequestPage)
                .append("&").append(CommonInfo.JokeApI.Param.JOKE_REQUEST_PAGE_SIZE).append("=")
                .append(REQUEST_ITEM_NUMS).append("&").append(CommonInfo.JokeApI.Param.JOKE_REQUEST_KEY)
                .append("=").append(CommonInfo.JokeApI.Param.JOKE_REQUEST_KEY_VALUE);
        LogUtils.d(CommonInfo.TAG, "-->" + params.toString());
        return params.toString();
    }


    @Override
    public LoadingPager.LoadState loadData() {
        List<Joke> lists = null;
        if (!NetworkInfo.isNetworkAvailable()) {
            lists = mDBManager.readJokeCacheFromDataBase(mRequestPage);
        } else {
            String json = HttpUtils.HttpPostMethod(CommonInfo.JokeApI.Param.JOKR_REQUEST_URL,
                    initRequestUrlParam(), CommonInfo.ENCODE_TYPE);
            lists = parseJsonData(json);
            addToDataBase(lists);
        }
        mLists = lists;
        return getCheckResult(mLists);
    }


    @Override
    public List<Joke> parseJsonData(String result) {
        return JsonParseTool.parseJokeJsonWidthJSONObject(result, mRequestPage);
    }

    @Override
    public void onRefresh() {
        mRequestPage = 1;
        LoadDataAsync asyn = new LoadDataAsync(true);
        asyn.execute();
        mSets.add(asyn);
    }


    public void onLoadMore() {
        if (!isLoadingMore) {
            mRequestPage++;
            mParams = initRequestUrlParam();
            LoadDataAsync asyn = new LoadDataAsync(false);
            asyn.execute();
            mSets.add(asyn);
        }
    }

    /**
     * 将数据加入数据库
     *
     * @param lists
     */
    public void addToDataBase(final List<Joke> lists) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //将数据写入数据库
                mDBManager.addJokeCaCheToDataBase(lists);
            }
        }).start();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (view.getLastVisiblePosition() == mTotalItemNums - 1 && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            mLoadMore = (LinearLayout) view.getChildAt(view.getLastVisiblePosition() - view.getFirstVisiblePosition()).findViewById(R.id.load_more);
            mLoadMore.setVisibility(View.VISIBLE);
            onLoadMore();
            isLoadingMore = true;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mTotalItemNums = totalItemCount;
    }

    class LoadDataAsync extends AsyncTask<Void, Void, List<Joke>> {
        /**
         * 异步任务来自下拉刷新
         */
        private boolean mIsFromPullDownRefresh;

        public LoadDataAsync(boolean fromPullDownRefresh) {
            mIsFromPullDownRefresh = fromPullDownRefresh;
        }

        @Override
        protected List<Joke> doInBackground(Void... params) {
            List<Joke> lists = null;
            if (NetworkInfo.isNetworkAvailable()) {
                String result = HttpUtils.HttpPostMethod(CommonInfo.JokeApI.Param.JOKR_REQUEST_URL,
                        mParams, CommonInfo.ENCODE_TYPE);
                lists = parseJsonData(result);
                addToDataBase(lists);
            }
            if (!NetworkInfo.isNetworkAvailable() || lists == null) {
                lists = mDBManager.readJokeCacheFromDataBase(mRequestPage);
            }
            return lists;
        }

        @Override
        protected void onPostExecute(List<Joke> result) {
            if (result != null && result.size() != 0) {
                LogUtils.d(CommonInfo.TAG, "joke aa result size " + result.size());
                if (mIsFromPullDownRefresh) {
                    mLists.clear();
                    mLists.addAll(result);
                    mPullToRefreshView.setRefreshing(false);
                } else {
                    mLists.addAll(result);

                }
            } else {
                LogUtils.d(CommonInfo.TAG, "joke aa result null ");
                if (mIsFromPullDownRefresh) {
                    mPullToRefreshView.setRefreshing(false);
                }
            }
            if (mLoadMore != null) {
                mLoadMore.setVisibility(View.GONE);
                isLoadingMore = false;
            }
            mJokeFragmentAdapter.notifyDataSetChanged();
            if (mSets != null) {
                mSets.remove(this);
            }
            //          mListView.setLoadCompleted();
        }
    }

    public void cancelAllAsync() {
        if (mSets != null) {
            for (AsyncTask task : mSets) {
                task.cancel(false);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        cancelAllAsync();
    }
}
