package com.example.parting_soul.news.fragment.joke;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.parting_soul.news.R;
import com.example.parting_soul.news.activity.MainActivity;
import com.example.parting_soul.news.adapter.JokeFragmentAdapter;
import com.example.parting_soul.news.bean.Joke;
import com.example.parting_soul.news.customview.LoadMoreItemListView;
import com.example.parting_soul.news.customview.LoadingPager;
import com.example.parting_soul.news.fragment.support.BaseFragment;
import com.example.parting_soul.news.utils.network.HttpUtils;
import com.example.parting_soul.news.utils.network.JsonParseTool;
import com.example.parting_soul.news.utils.support.CommonInfo;
import com.example.parting_soul.news.utils.support.LogUtils;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.List;

/**
 * Created by parting_soul on 2016/11/1.
 */

public class JokeFragment extends BaseFragment<Joke> implements PullToRefreshView.OnRefreshListener {
    public static final String NAME = "JokeFragment";

    private JokeFragmentAdapter mJokeFragmentAdapter;

    private LoadMoreItemListView mListView;

    private PullToRefreshView mPullToRefreshView;

    private List<Joke> mLists;

    private int mRequestPage = 1;

    private static final int REQUEST_ITEM_NUMS = 20;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setTitleName(R.string.funny);
    }

    @Override
    public View createSuccessPage() {
        View view = View.inflate(getActivity(), R.layout.funny_fragment_layout, null);
        mListView = (LoadMoreItemListView) view.findViewById(R.id.list_view);
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
        String json = HttpUtils.HttpPostMethod(CommonInfo.JokeApI.Param.JOKR_REQUEST_URL,
                initRequestUrlParam(), CommonInfo.ENCODE_TYPE);
        List<Joke> lists = parseJsonData(json);
        mLists = lists;
        return getCheckResult(mLists);
    }


    @Override
    public List<Joke> parseJsonData(String result) {
        return JsonParseTool.parseJokeJsonWidthJSONObject(result);
    }

    @Override
    public void onRefresh() {
        mPullToRefreshView.setRefreshing(false);
    }

}
