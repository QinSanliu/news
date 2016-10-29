package com.example.parting_soul.news.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.parting_soul.news.R;
import com.example.parting_soul.news.bean.Settings;
import com.example.parting_soul.news.fragment.settings.SettingsPreferenceFragment;
import com.example.parting_soul.news.utils.style.LanguageChangeManager;
import com.example.parting_soul.news.utils.style.ThemeChangeManager;

/**
 * Created by parting_soul on 2016/10/18.
 * 用于显示Settings界面的Activity
 */

public class SettingsPreferenceActivity extends AppCompatActivity {

    /**
     * 设置界面
     */
    private SettingsPreferenceFragment mPreferenceFragment;

    /**
     * 返回按钮
     */
    private ImageView mBackView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChangeManager.changeThemeMode(this);
        LanguageChangeManager.changeLanguage();
        setContentView(R.layout.layout_settings);
        mBackView = (ImageView) findViewById(R.id.back_to_left_menu);
        if (savedInstanceState == null) {
            mPreferenceFragment = new SettingsPreferenceFragment();
            replaceFragment(R.id.settings_container, mPreferenceFragment);
        }
        mBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRefresh();
                SettingsPreferenceActivity.this.finish();
            }
        });
    }

    /**
     * 替换指定布局的view为设置界面的fragment
     *
     * @param viewId
     * @param fragment
     */
    public void replaceFragment(int viewId, Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(viewId, fragment).commit();
    }

    /**
     * 其他Activity启动该Activity的接口
     *
     * @param context
     */
    public static void startActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SettingsPreferenceActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        checkRefresh();
        finish();
    }

    /**
     * 判断是否刷新Activity
     */
    public void checkRefresh() {
        if (Settings.isRefresh) {
            refreshActivity();
            Settings.isRefresh = false;
        }
    }

    /**
     * 重启Activity
     */
    public void refreshActivity() {
        Intent mIntent = new Intent(this, MainActivity.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mIntent);
    }
}
