package com.example.parting_soul.news.utils.style;

import android.content.Context;
import android.content.res.Configuration;

import com.example.parting_soul.news.NewsApplication;
import com.example.parting_soul.news.R;
import com.example.parting_soul.news.bean.Settings;

import java.util.Locale;

/**
 * Created by parting_soul on 2016/10/29.
 */

public class LanguageChangeManager {
    public static Context mContext = NewsApplication.getContext();

    public static String[] languageKeys = mContext.getResources().getStringArray(R.array.language_key);

    public static void changeLanguage() {
        String key = Settings.newsInstance().getString(Settings.LANUAGE_KEY, "");
        Configuration confi = mContext.getResources().getConfiguration();
        if (key.equals("zh")) {
            confi.locale = Locale.SIMPLIFIED_CHINESE;
        } else if (key.equals("en")) {
            confi.locale = Locale.ENGLISH;
        }
        mContext.getResources().updateConfiguration(confi, mContext.getResources().getDisplayMetrics());
    }
}
