package com.example.parting_soul.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.parting_soul.news.R;
import com.example.parting_soul.news.bean.News;
import com.example.parting_soul.news.utils.CommonInfo;
import com.example.parting_soul.news.utils.image.ImageLoader;
import com.example.parting_soul.news.utils.LogUtils;
import com.example.parting_soul.news.utils.style.FontChangeManager;

import java.util.List;

/**
 * Created by parting_soul on 2016/10/4.
 * 新闻内容适配器
 */

public class NewsInfoAdapter extends BaseAdapter implements AbsListView.OnScrollListener {
    /**
     * 新闻数组
     */
    private List<News> mLists;
    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 图片的url地址数组,注意不能用静态
     */
    public String[] IMAGE_URLS;

    /**
     * listview可见项的起始位置
     */
    private int mStart;

    /**
     * listview可见项的终止位置
     */
    private int mEnd;

    /**
     * 图片加载类
     */
    private ImageLoader mImageLoader;

    /**
     * 是否是第一次打开
     */
    private boolean isFirstIn = true;

    /**
     * 新闻数据项
     */
    private ListView mListView;

    /**
     * 是否可以加载图片
     */
    boolean mCanLoagImage;

    private static int textStyleId;

    public NewsInfoAdapter(Context context, List<News> lists, ListView listView) {
        mContext = context;
        mLists = lists;
        mListView = listView;
        mImageLoader = ImageLoader.newInstance(context);
        getPicUrl();
        //为listview添加滚动监听
        listView.setOnScrollListener(this);
        textStyleId = FontChangeManager.changeFontSize();
    }

    /**
     * 得到所有图片的URL地址
     */
    public void getPicUrl() {
        IMAGE_URLS = new String[mLists.size()];
        for (int i = 0; i < mLists.size(); i++) {
            IMAGE_URLS[i] = mLists.get(i).getPicPath();
        }
    }

    /**
     * 适配器数据源
     *
     * @return List<News>
     */
    public List<News> getData() {
        return mLists;
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
            //没有可重用的布局，就加载一个布局
            view = LayoutInflater.from(mContext).inflate(R.layout.news_listview_item, null);
            //找到布局中所有的UI控件并且绑定在Holder中
            holder = new NewsHolder();
            holder.title = (TextView) view.findViewById(R.id.news_title);
            holder.pic = (ImageView) view.findViewById(R.id.news_pic);
            holder.authorName = (TextView) view.findViewById(R.id.news_author_name);
            holder.date = (TextView) view.findViewById(R.id.news_date);
            //给新加载的布局绑定Holder，以便布局重用
            view.setTag(holder);
        } else {
            //有可重用的布局
            view = convertView;
            //得到绑定的Holder
            holder = (NewsHolder) view.getTag();
        }
        //为UI控件设置值
        holder.title.setText(mLists.get(position).getTitle());
        holder.title.setTextAppearance(mContext,textStyleId);
        holder.authorName.setText(mLists.get(position).getAuthor_name());
        holder.date.setText(mLists.get(position).getDate());
//        holder.pic.setImageResource(R.mipmap.imageview_default_bc);

        String url = mLists.get(position).getPicPath();
        LogUtils.d(CommonInfo.TAG, "---->" + url + " ++ " + holder.pic);
        holder.pic.setTag(url);
        mImageLoader.loadImage(url, holder.pic);
//        LogUtils.d(TAG, "getView: " + mLists.get(position).getTitle() + " " + position + " " + mLists.get(position).getPicPath());
        return view;
    }

    class NewsHolder {
        ImageView pic;
        TextView title, date, authorName;
    }

    /**
     * 设置是否可以加载图片
     *
     * @param canLoagImage
     */
    public void setIsCanLoadImage(boolean canLoagImage) {
        mCanLoagImage = canLoagImage;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //如果停止滑动就加载当前可见项的图片
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            mImageLoader.loadImage(mStart, mEnd, mListView);
        } else {
            mImageLoader.cancelAllAsyncTask();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStart = firstVisibleItem;
        mEnd = firstVisibleItem + visibleItemCount;
        LogUtils.i(CommonInfo.TAG, "NewsInfoAdapter-->onScroll-->onScroll start = " + mStart + " end = " + mEnd);
        if (isFirstIn && visibleItemCount > 0 && mCanLoagImage) {
            mImageLoader.loadImage(mStart, mEnd, mListView);
            isFirstIn = false;
        }
    }

}
