package com.example.parting_soul.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.parting_soul.news.R;
import com.example.parting_soul.news.bean.WeiChat;
import com.example.parting_soul.news.utils.image.ImageLoader;
import com.example.parting_soul.news.utils.style.FontChangeManager;
import com.example.parting_soul.news.utils.support.CommonInfo;
import com.example.parting_soul.news.utils.support.LogUtils;

import java.util.List;

/**
 * Created by parting_soul on 2016/11/3.
 */

public class WeiChatDetailFragmentAdapter extends BaseFragmentAdapter<WeiChat> {

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 图片加载类
     */
    private ImageLoader mImageLoader;


    private ListView mListView;

    private static int textStyleId;

    public WeiChatDetailFragmentAdapter(Context context, List<WeiChat> list, ListView listView) {
        mContext = context;
        mLists = list;
        mListView = listView;
        getPicUrl();
        mImageLoader = ImageLoader.newInstance(context);
        mListView.setOnScrollListener(this);
        textStyleId = FontChangeManager.changeFontSize();
    }

    @Override
    public void getPicUrl() {
        IMAGE_URLS = new String[mLists.size()];
        for (int i = 0; i < mLists.size(); i++) {
            IMAGE_URLS[i] = mLists.get(i).getPicPath();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WeiChatHolder holder = null;
        View view = null;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.weichat_fragment_item, null);
            holder = new WeiChatHolder();
            holder.pic = (ImageView) view.findViewById(R.id.weichat_pic);
            holder.title = (TextView) view.findViewById(R.id.weichat_title);
            holder.source = (TextView) view.findViewById(R.id.weichat_source);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (WeiChatHolder) convertView.getTag();
        }
        holder.title.setText(mLists.get(position).getTitle());
        holder.title.setTextAppearance(mContext, textStyleId);
        holder.source.setText(mLists.get(position).getSource());

        String url = mLists.get(position).getPicPath();
        holder.pic.setTag(url);
        mImageLoader.loadImage(url, holder.pic);
        return view;
    }

    class WeiChatHolder {
        ImageView pic;
        TextView title;
        TextView source;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //如果停止滑动就加载当前可见项的图片
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            mImageLoader.loadImage(mStart, mEnd, mListView, this);
        } else {
            mImageLoader.cancelAllAsyncTask();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStart = firstVisibleItem;
        mEnd = firstVisibleItem + visibleItemCount;
        LogUtils.i(CommonInfo.TAG, "WeiChatDetailFragmentAdapter-->onScroll-->onScroll start = " + mStart + " end = " + mEnd);
        if (isFirstIn && visibleItemCount > 0 && mCanLoagImage) {
            mImageLoader.loadImage(mStart, mEnd, mListView, this);
            LogUtils.i(CommonInfo.TAG, "WeiChatDetailFragmentAdapter-->onScroll-->onScroll " + isFirstIn);
            isFirstIn = false;
        }
    }

}
