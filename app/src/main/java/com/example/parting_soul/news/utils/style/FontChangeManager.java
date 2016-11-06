package com.example.parting_soul.news.utils.style;

import android.content.Context;

import com.example.parting_soul.news.utils.support.NewsApplication;
import com.example.parting_soul.news.R;
import com.example.parting_soul.news.bean.Settings;

/**
 * Created by parting_soul on 2016/10/28.
 * 字体大小管理类
 */

public class FontChangeManager {
    public static Context mContext = NewsApplication.getContext();

    public static final String[] fontSizeKey = mContext.getResources().getStringArray(R.array.font_size_key);

    public static int changeItemTitleFontSize() {
        String key = Settings.newsInstance().getString(Settings.FONT_SIZE_KEY, "");
        if (key.equals(fontSizeKey[1])) {
            return R.style.news_item_font_style_normal;
        } else if (key.equals(fontSizeKey[2])) {
            return R.style.news_item_font_style_large;
        } else {
            return R.style.news_item_font_style_small;
        }
    }

    public static int changeJokeFontSize() {
        String key = Settings.newsInstance().getString(Settings.FONT_SIZE_KEY, "");
        if (key.equals(fontSizeKey[1])) {
            return R.style.joke_item_font_style_normal;
        } else if (key.equals(fontSizeKey[2])) {
            return R.style.joke_item_font_style_large;
        } else {
            return R.style.joke_item_font_style_small;
        }
    }

}
