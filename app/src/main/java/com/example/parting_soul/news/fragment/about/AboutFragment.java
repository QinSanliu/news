package com.example.parting_soul.news.fragment.about;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.parting_soul.news.R;
import com.example.parting_soul.news.bean.About;

/**
 * Created by parting_soul on 2016/10/29.
 */

public class AboutFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(About.ABOUT_XML_NAME);
        //添加对应的About布局
        addPreferencesFromResource(R.xml.about);
    }


}
