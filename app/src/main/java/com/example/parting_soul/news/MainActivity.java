package com.example.parting_soul.news;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.parting_soul.news.adapter.LeftMenuItemAdapter;
import com.example.parting_soul.news.bean.MenuItemApi;
import com.example.parting_soul.news.bean.NewsKinds;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyTest";

    /**
     * 新闻的类型，显示在导航条中
     */
    private static String[] NEWS_TYPES;

    /**
     * 左上方打开侧滑抽屉的图片
     */
    private ImageView mOpenDraw;

    /**
     * 应用程序的名字，显示于标题中间
     */
    private TextView mTitle;

    /**
     * 自定义标题的布局
     */
    private RelativeLayout mTitle_layout;

    /**
     * 主布局
     */
    private DrawerLayout mDrawerLayout;

    /**
     * 左侧侧滑菜单布局
     */
    private LinearLayout mLeft_draw_menu_layout;

    /**
     * 左侧菜单适配器
     */
    private LeftMenuItemAdapter mMenuAdpater;

    /**
     * 左侧菜单列表UI
     */
    private ListView mLeftMenuListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        init();
    }

    /**
     * 初始化UI和变量,并且绑定监听事件
     */
    private void init() {
        NEWS_TYPES = NewsKinds.getNewsTypes();
        mOpenDraw = (ImageView) findViewById(R.id.open_draw);
        mTitle = (TextView) findViewById(R.id.title_name);
        mTitle_layout = (RelativeLayout) findViewById(R.id.title_layout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeft_draw_menu_layout = (LinearLayout) findViewById(R.id.left_navigation_layout);

        //给左侧菜单的适配器填充数据项
        mLeftMenuListView = (ListView) findViewById(R.id.menu_listview);
        mMenuAdpater = new LeftMenuItemAdapter(this, MenuItemApi.getMenuItems());
        mLeftMenuListView.setAdapter(mMenuAdpater);
        mMenuAdpater.notifyDataSetChanged();

        mOpenDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(mLeft_draw_menu_layout);
            }
        });
    }
}
