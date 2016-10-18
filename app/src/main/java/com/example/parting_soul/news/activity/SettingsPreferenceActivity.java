package com.example.parting_soul.news.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.parting_soul.news.R;
import com.example.parting_soul.news.fragment.settings.SettingsPreferenceFragment;

/**
 * Created by parting_soul on 2016/10/18.
 * 用于显示Settings界面的Activity
 */

public class SettingsPreferenceActivity extends Activity {

    private SettingsPreferenceFragment mPreferenceFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_settings);
        if (savedInstanceState == null) {
            mPreferenceFragment = new SettingsPreferenceFragment();
            replaceFragment(R.id.settings_container, mPreferenceFragment);
        }
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
}
