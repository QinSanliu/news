package com.example.parting_soul.news.fragment.weichat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.parting_soul.news.R;
import com.example.parting_soul.news.activity.MainActivity;
import com.example.parting_soul.news.adapter.WeiChatDetailFragmentAdapter;
import com.example.parting_soul.news.bean.WeiChat;
import com.example.parting_soul.news.customview.LoadingPager;
import com.example.parting_soul.news.fragment.support.BaseFragment;
import com.example.parting_soul.news.utils.cache.database.DBManager;
import com.example.parting_soul.news.utils.network.HttpUtils;
import com.example.parting_soul.news.utils.network.JsonParseTool;
import com.example.parting_soul.news.utils.network.NetworkInfo;
import com.example.parting_soul.news.utils.support.CommonInfo;
import com.example.parting_soul.news.utils.support.LogUtils;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.List;

/**
 * Created by parting_soul on 2016/11/1.
 */

public class WeiChatFragment extends BaseFragment<WeiChat> {
    public static final String NAME = "weichatfragment";

    /**
     * 数据库管理类
     */
    private DBManager manager;

    private List<WeiChat> mLists;

    private String mParams;

    private int mCurrentPage;

    private int mRequestPage;

    private ListView mListView;

    private PullToRefreshView mPullToRefreshView;

    private WeiChatDetailFragmentAdapter mWeiChatDetailFragmentAdapter;

    public static int REQUEST_ITEM_NUMS = 100;

    public static int REQUEST_MAX_PAGE_NUMS = 5;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setTitleName(R.string.weichat);
        mParams = initRequestUrlParam();
        LogUtils.d(CommonInfo.TAG, "--->111" + "onCreate() " + mParams);
    }

    @Override
    protected String initRequestUrlParam() {
        StringBuilder params = new StringBuilder();
        params.append(CommonInfo.WeiChatAPI.Params.REQUEST_PAGE_NO_NAME).append("=").append(mRequestPage)
                .append("&").append(CommonInfo.WeiChatAPI.Params.REQUEST_NUMS).append("=")
                .append(REQUEST_ITEM_NUMS).append("&").append(CommonInfo.WeiChatAPI.Params.REQUEST_DATATYPE).append("=")
                .append(CommonInfo.WeiChatAPI.Params.REQUEST_TYPE).append("&").append(CommonInfo.WeiChatAPI.Params.REQUEST_KEY_NAME)
                .append("=").append(CommonInfo.WeiChatAPI.Params.REQUEST_KEY_VALUE);
        LogUtils.d(CommonInfo.TAG, "-->" + params.toString());
        return params.toString();
    }

    @Override
    public View createSuccessPage() {
        View view = View.inflate(getActivity(), R.layout.weichat_fragment_layout, null);
        mListView = (ListView) view.findViewById(R.id.list_view);
        LogUtils.d(CommonInfo.TAG, "--->111" + "createSuccess()");
        return view;
    }

    @Override
    public void updataUI() {
        //将数据源绑定到适配器
        mWeiChatDetailFragmentAdapter = new WeiChatDetailFragmentAdapter(getActivity(), mLists, mListView);
        //设置可以加载图片
        mWeiChatDetailFragmentAdapter.setIsCanLoadImage(true);
        //为listview设置适配器
        mListView.setAdapter(mWeiChatDetailFragmentAdapter);
        mWeiChatDetailFragmentAdapter.notifyDataSetChanged();

        LogUtils.d(CommonInfo.TAG, "-->lists 1" + mLists.size());
    }

    @Override
    public LoadingPager.LoadState loadData() {
        LogUtils.d(CommonInfo.TAG, "--->111" + "loadData()");
        List<WeiChat> lists = null;
        if (!NetworkInfo.isNetworkAvailable()) {
            //           lists = manager.readNewsCacheFromDatabase(mNewTypeParam);
            LogUtils.d(CommonInfo.TAG, "network unavailable ");
        } else {
            String result = HttpUtils.HttpPostMethod(CommonInfo.WeiChatAPI.Params.REQUEST_URL,
                    mParams, CommonInfo.ENCODE_TYPE);
            Log.d(CommonInfo.TAG, "-->" + result);
            lists = parseJsonData(result);
        }
        mLists = lists;
        return getCheckResult(mLists);
    }

    @Override
    public List<WeiChat> parseJsonData(String result) {
        return JsonParseTool.parseWeiChatJsonWidthJSONObject(result);
    }

}
