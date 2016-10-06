package com.example.parting_soul.news.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.parting_soul.news.R;
import com.example.parting_soul.news.bean.News;
import com.example.parting_soul.news.utils.ImageLoader;
import com.example.parting_soul.news.utils.LogUtils;

import java.util.List;

import static com.example.parting_soul.news.utils.CommonInfo.TAG;

/**
 * Created by parting_soul on 2016/10/4.
 * 新闻内容适配器
 */

public class NewsInfoAdapter extends BaseAdapter {
    /**
     * 新闻数组
     */
    private List<News> mLists;
    /**
     * 上下文对象
     */
    private Context mContext;

    public NewsInfoAdapter(Context context, List<News> lists) {
        mContext = context;
        mLists = lists;
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int position) {
        return mLists.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        NewsHolder holder = null;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.news_listview_item, null);
            holder = new NewsHolder();
            holder.title = (TextView) view.findViewById(R.id.news_title);
            holder.pic = (ImageView) view.findViewById(R.id.news_pic);
            holder.authorName = (TextView) view.findViewById(R.id.news_author_name);
            holder.date = (TextView) view.findViewById(R.id.news_date);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (NewsHolder) view.getTag();
        }
        holder.title.setText(mLists.get(position).getTitle());
        holder.authorName.setText(mLists.get(position).getAuthor_name());
        holder.date.setText(mLists.get(position).getDate());
        holder.pic.setImageResource(R.mipmap.imageview_default_bc);

        String url = mLists.get(position).getPicPath();
        holder.pic.setTag(url);
        ImageLoader imageLoader = new ImageLoader();
        imageLoader.downLoadImage(url, holder.pic);
        LogUtils.d(TAG, "getView: " + mLists.get(position).getTitle() + " " + position + " " + mLists.get(position).getPicPath());
        return view;
    }

    class NewsHolder {
        ImageView pic;
        TextView title, date, authorName;
    }
}
