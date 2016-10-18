package com.example.parting_soul.news.fragment.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.parting_soul.news.R;

/**
 * Created by parting_soul on 2016/10/18.
 * settings fragment
 */

public class SettingsPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //添加对应的Settings布局
        addPreferencesFromResource(R.xml.settings);
    }

}
