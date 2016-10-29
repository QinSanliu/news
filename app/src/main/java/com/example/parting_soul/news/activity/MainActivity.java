package com.example.parting_soul.news.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parting_soul.news.R;
import com.example.parting_soul.news.adapter.LeftMenuItemAdapter;
import com.example.parting_soul.news.adapter.NewsFragmentAdapter;
import com.example.parting_soul.news.bean.MenuItemApi;
import com.example.parting_soul.news.bean.NewsKinds;
import com.example.parting_soul.news.bean.Settings;
import com.example.parting_soul.news.customview.HorizontalNavigation;
import com.example.parting_soul.news.fragment.NewsFragment;
import com.example.parting_soul.news.utils.CommonInfo;
import com.example.parting_soul.news.utils.LogUtils;
import com.example.parting_soul.news.utils.WindowSizeTool;
import com.example.parting_soul.news.utils.style.LanguageChangeManager;
import com.example.parting_soul.news.utils.style.ThemeChangeManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
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
     * 左侧菜单顶部布局
     */
    private RelativeLayout mLeft_menu_top_layout;

    /**
     * 左侧菜单适配器
     */
    private LeftMenuItemAdapter mMenuAdpater;

    /**
     * 左侧菜单列表UI
     */
    private ListView mLeftMenuListView;

    /**
     * 横向导航条的布局
     */
    private LinearLayout mNavigationLayout;

    /**
     * 导航条中导航项的宽度
     */
    private int mNavigation_item_width;

    /**
     * 屏幕宽度(像素)
     */
    private int mScreenWith;

    /**
     * 屏幕能显示菜单项的最大数量
     */
    private static final int itemNum = 7;

    /**
     * 新闻侧滑页面
     */
    private ViewPager mNewsViewPager;

    /**
     * 存放所有新闻页面碎片,用作适配器的数据源
     */
    private List<Fragment> mNewsFragmentLists;

    /**
     * 用于连接viewpager和fragment的适配器
     */
    private NewsFragmentAdapter mNewsPagerAdapter;

    /**
     * 用作水平滚动导航条
     */
    private HorizontalNavigation mHorizontalNavigation;

    /**
     * 左侧菜单底部设置View
     */
    private TextView mLeftMenuSettingsView;

    /**
     * 左侧菜单底部退出View
     */
    private TextView mLeftMenuExitView;

    /**
     * 设置
     */
    private Settings mSettings;

    /**
     * 按下返回键的时间
     */
    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChangeManager.changeThemeMode(this);
        LanguageChangeManager.changeLanguage();
        setContentView(R.layout.drawer_layout);
        init();
        initLeftDrawerLayout();
        initViewPager();
        initHorizontalNavigationItem();
        mSettings = Settings.newsInstance();
        LogUtils.currentLevel = LogUtils.DEBUG;
    }

    /**
     * 进行一些初始化操作
     */
    private void init() {
        mOpenDraw = (ImageView) findViewById(R.id.open_draw);
        mTitle = (TextView) findViewById(R.id.title_name);
        mTitle_layout = (RelativeLayout) findViewById(R.id.title_layout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeft_draw_menu_layout = (LinearLayout) findViewById(R.id.left_navigation_layout);

        mNavigationLayout = (LinearLayout) findViewById(R.id.navigation_layout);
        mNewsViewPager = (ViewPager) findViewById(R.id.news_viewpager);
        mHorizontalNavigation = (HorizontalNavigation) findViewById(R.id.horizontal_navigation);

        //得到导航条的导航类别名
        NEWS_TYPES = NewsKinds.getNewsTypes();
        //得到屏幕的宽度
        mScreenWith = WindowSizeTool.getScreenWidth(this);
        //计算出每一项的宽度
        mNavigation_item_width = mScreenWith / itemNum;
        //为ViewPager绑定监听器
        mNewsViewPager.addOnPageChangeListener(this);
    }

    /**
     * 给左侧侧滑菜单适配器填充数据源，并把适配器绑定在
     * 菜单列表ListView中<br>
     * 给左上方的图片控件添加监听,触发则打开侧滑左菜单
     */
    private void initLeftDrawerLayout() {
        //给左侧菜单的适配器填充数据项
        mLeftMenuListView = (ListView) findViewById(R.id.menu_listview);
        mMenuAdpater = new LeftMenuItemAdapter(this, MenuItemApi.getMenuItems());
        mLeftMenuListView.setAdapter(mMenuAdpater);
        mMenuAdpater.notifyDataSetChanged();

        //给左上方的图片控件添加监听,触发则打开侧滑左菜单
        mOpenDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(mLeft_draw_menu_layout);
            }
        });
        //初始化左侧底部按钮
        getLeftMenuBottomView();
    }

    /**
     * 根据新闻种类数生成相应数目的fragment,并填充到适配器
     * 且将适配器绑定在ViewPager
     */
    private void initViewPager() {
        mNewsFragmentLists = new ArrayList<Fragment>();
        for (int i = 0; i < NEWS_TYPES.length; i++) {
            NewsFragment fragment = new NewsFragment();
            Bundle bundle = new Bundle();
            bundle.putString(CommonInfo.NewsAPI.Params.REQUEST_TYPE_PARAM_NAME, NewsKinds.getNewsTypeMap()
                    .get(NEWS_TYPES[i]));
            fragment.setArguments(bundle);
            mNewsFragmentLists.add(fragment);
        }
        mNewsPagerAdapter = new NewsFragmentAdapter(getSupportFragmentManager(), mNewsFragmentLists);
        mNewsViewPager.setAdapter(mNewsPagerAdapter);
    }

    /**
     * 初始化水平导航<br>
     * <p>根据新闻类别的个数生成相应个数的文本项用于显示新闻类<br>
     * 将每一个生成的TextView动态加入到水平滚动视图用作新闻类别项，为每个新闻项设置监听
     */
    private void initHorizontalNavigationItem() {
        for (int i = 0; i < NEWS_TYPES.length; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mNavigation_item_width,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 10;
            params.rightMargin = 10;

            //为TextView设置内容,位置,背景,字体等属性
            TextView textView = new TextView(this);
            textView.setText(NEWS_TYPES[i]);
            textView.setGravity(Gravity.CENTER);
            if (Settings.is_night_mode) {
                textView.setBackgroundResource(R.drawable.navigation_item_state_bc_night);
                textView.setTextAppearance(this, R.style.navigation_item_front_state_style_night);
            } else {
                textView.setBackgroundResource(ThemeChangeManager.getNavigationResoureStateBK());
                textView.setTextAppearance(this, R.style.navigation_item_front_state_style);
            }

            textView.setLayoutParams(params);
            //设置唯一的识别标志
            textView.setId(i);
            //默认第一个新闻项为选中状态
            if (i == 0) {
                textView.setSelected(true);
            }
            //为新闻类项设置监听
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //根据父组件找到所有的子组件
                    for (int i = 0; i < mNavigationLayout.getChildCount(); i++) {
                        View view = mNavigationLayout.getChildAt(i);
                        //只要有一个被选中，则改项设为选中 状态,其他应恢复为未选中状态
                        if (view.getId() != v.getId()) {
                            view.setSelected(false);
                        } else {
                            view.setSelected(true);
                            //该类项被选中，跳到对应的新闻fragment
                            mNewsViewPager.setCurrentItem(i);
                        }
                    }
                }
            });
            //将textview添加到父组件对应位置
            mNavigationLayout.addView(textView, i);
        }
    }

    /**
     * 根据当前fragment所在的页面号跳转到对应的菜单项目,使得两者对应
     *
     * @param position 当前fragment所在的页面号
     */
    private void navigationToLocation(int position) {
        //得到当前新闻类项
        View currentItem = mNavigationLayout.getChildAt(position);
        //得到该空间距离屏幕最左端的距离
        int itemLeft = currentItem.getLeft();
        //得到该控件的宽度
        int itemWidth = currentItem.getMeasuredWidth();
        //以屏幕中间为起点，若选择的项目超过了屏幕中间，则导航条会滑动
        int x = (itemLeft + itemWidth / 2) - mScreenWith / 2;
        //滑动到x坐标的位置
        mHorizontalNavigation.smoothScrollTo(x, 0);

        for (int i = 0; i < mNavigationLayout.getChildCount(); i++) {
            View view = mNavigationLayout.getChildAt(i);
            if (i == position) {
                view.setSelected(true);
            } else {
                view.setSelected(false);
            }
        }
    }

    /**
     * 找到左侧侧滑菜单底部的设置和退出按钮，并设置监听，点击触发不同的事件
     */
    public void getLeftMenuBottomView() {
        mLeftMenuSettingsView = (TextView) mLeft_draw_menu_layout.findViewById(R.id.settings);
        mLeftMenuExitView = (TextView) mLeft_draw_menu_layout.findViewById(R.id.exit);
        mLeftMenuSettingsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置View被点击，则启动设置的Activity
                SettingsPreferenceActivity.startActivity(MainActivity.this);
            }
        });
        mLeftMenuExitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finish();
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 滑到一个页面使得导航栏的选项与页面相对应
     *
     * @param position 页面的位置
     */
    @Override
    public void onPageSelected(int position) {
        navigationToLocation(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mLeft_draw_menu_layout)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        } else if (Settings.is_back_by_twice && (System.currentTimeMillis() - mExitTime) > Settings.EXIT_TIME) {
            mExitTime = System.currentTimeMillis();
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            return;
        }
        super.onBackPressed();
    }

}
