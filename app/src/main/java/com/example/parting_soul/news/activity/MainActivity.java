package com.example.parting_soul.news.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parting_soul.news.R;
import com.example.parting_soul.news.adapter.LeftMenuItemAdapter;
import com.example.parting_soul.news.bean.MenuItemApi;
import com.example.parting_soul.news.bean.MenuItemInfo;
import com.example.parting_soul.news.bean.Settings;
import com.example.parting_soul.news.customview.CircleImageView;
import com.example.parting_soul.news.fragment.funny.FunnyFragment;
import com.example.parting_soul.news.fragment.news.NewsFragment;
import com.example.parting_soul.news.fragment.weichat.WeiChatFragment;
import com.example.parting_soul.news.utils.style.LanguageChangeManager;
import com.example.parting_soul.news.utils.style.ThemeChangeManager;
import com.example.parting_soul.news.utils.support.CommonInfo;
import com.example.parting_soul.news.utils.support.LogUtils;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    /**
     * 左上方打开侧滑抽屉的图片
     */
    private ImageButton mOpenDraw;

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
     * 左侧圆形图片控件
     */
    private CircleImageView mLoginImageView;

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

    /**
     * fragment管理
     */
    private FragmentManager mFragmentManager;

    /**
     * 当前显示的fragment
     */
    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChangeManager.changeThemeMode(this);
        LanguageChangeManager.changeLanguage();
        setContentView(R.layout.drawer_layout);
        init();
        initLeftDrawerLayout();
        mSettings = Settings.newsInstance();
        LogUtils.currentLevel = LogUtils.DEBUG;
    }

    /**
     * 进行一些初始化操作
     */
    private void init() {
        mOpenDraw = (ImageButton) findViewById(R.id.open_draw);
        mTitle = (TextView) findViewById(R.id.title_name);
        mTitle_layout = (RelativeLayout) findViewById(R.id.title_layout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeft_draw_menu_layout = (LinearLayout) findViewById(R.id.left_navigation_layout);
        mFragmentManager = getSupportFragmentManager();

        mCurrentFragment = new NewsFragment();
        mFragmentManager.beginTransaction().add(R.id.main_fragment, mCurrentFragment, NewsFragment.NAME).commit();
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
        mLeftMenuListView.setOnItemClickListener(this);
        mMenuAdpater.notifyDataSetChanged();
        mLoginImageView = (CircleImageView) findViewById(R.id.login_image);

        mLoginImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutActivity.startActivity(MainActivity.this);
            }
        });

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MenuItemInfo info = (MenuItemInfo) mMenuAdpater.getItem(position);
        switch (info.getNameId()) {
            case R.string.news:
                if (mFragmentManager.findFragmentByTag(NewsFragment.NAME) == null) {
                    mCurrentFragment = new NewsFragment();
                    setCurrentFragment(mCurrentFragment);
                }
                break;
            case R.string.weichat:
                if (mFragmentManager.findFragmentByTag(WeiChatFragment.NAME) == null) {
                    mCurrentFragment = new WeiChatFragment();
                    setCurrentFragment(mCurrentFragment);
                }
                break;
            case R.string.funny:
                if (mFragmentManager.findFragmentByTag(FunnyFragment.NAME) == null) {
                    mCurrentFragment = new FunnyFragment();
                    setCurrentFragment(mCurrentFragment);
                }
                break;
            case R.string.collection:
                CollectionActivity.startActivity(this);
                break;
            case R.string.night:
                ThemeChangeManager.setNightMode(this);
                MainActivity.this.recreate();
                break;
            case R.string.about:
                AboutActivity.startActivity(this);
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    /**
     * 重启Activity
     */
    public static void refreshActivity(Context context) {
        Intent mIntent = new Intent(context, MainActivity.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(mIntent);
    }

    public void setCurrentFragment(Fragment fragment) {
        if (fragment instanceof NewsFragment) {
            mFragmentManager.beginTransaction().replace(R.id.main_fragment, (NewsFragment) fragment, NewsFragment.NAME).commit();
            LogUtils.d(CommonInfo.TAG, "--->" + NewsFragment.NAME);
        } else if (fragment instanceof WeiChatFragment) {
            mFragmentManager.beginTransaction().replace(R.id.main_fragment, (WeiChatFragment) fragment, WeiChatFragment.NAME).commit();
            LogUtils.d(CommonInfo.TAG, "--->" + WeiChatFragment.NAME);
        } else if (fragment instanceof FunnyFragment) {
            mFragmentManager.beginTransaction().replace(R.id.main_fragment, (FunnyFragment) fragment, FunnyFragment.NAME).commit();
            LogUtils.d(CommonInfo.TAG, "--->" + FunnyFragment.NAME);
        }
    }

    @Override
    public void recreate() {
        try {
            //避免重启太快 恢复
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.remove(mCurrentFragment);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (Exception e) {
        }
        super.recreate();
    }

}
