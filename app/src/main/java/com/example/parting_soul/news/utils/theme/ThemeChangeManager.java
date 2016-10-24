package com.example.parting_soul.news.utils.theme;

import android.app.Activity;
import android.content.Context;

import com.example.parting_soul.news.NewsApplication;
import com.example.parting_soul.news.R;
import com.example.parting_soul.news.bean.Settings;
import com.example.parting_soul.news.utils.CommonInfo;
import com.example.parting_soul.news.utils.LogUtils;

import java.util.HashMap;
import java.util.Map;

import static com.example.parting_soul.news.utils.theme.ThemeChangeManager.Color.BLUE;
import static com.example.parting_soul.news.utils.theme.ThemeChangeManager.Color.GREEN;
import static com.example.parting_soul.news.utils.theme.ThemeChangeManager.Color.PURPLE;
import static com.example.parting_soul.news.utils.theme.ThemeChangeManager.Color.RED;

/**
 * Created by parting_soul on 2016/10/24.
 * 主题颜色管理类
 */

public class ThemeChangeManager {
    public static Color currentColor;

    public static Map<String, Color> map;

    public static Context mContext = NewsApplication.getContext();

    public static String[] colorKeys = mContext.getResources().getStringArray(R.array.theme_key);

    static {
        map = new HashMap<String, Color>();
        map.put(colorKeys[0], RED);
        map.put(colorKeys[1], BLUE);
        map.put(colorKeys[2], GREEN);
        map.put(colorKeys[3], PURPLE);
    }

    public enum Color {
        RED, GREEN, BLUE, PURPLE, BLACK
    }

    public static void changeTitleTheme(Activity activity) {
        currentColor = map.get(Settings.newsInstance().getString(Settings.THEME_CHANGE_KEY, ""));
        LogUtils.d(CommonInfo.TAG, "-->" + currentColor);
        if (currentColor != null) {
            switch (currentColor) {
                case RED:
                    activity.setTheme(R.style.DayTheme_red);
                    break;
                case GREEN:
                    activity.setTheme(R.style.DayTheme_green);
                    break;
                case BLUE:
                    activity.setTheme(R.style.DayTheme_blue);
                    break;
                case PURPLE:
                    activity.setTheme(R.style.DayTheme_purple);
                    break;
                case BLACK:
                    break;
            }
        }
    }

    public static int getNavigationResoureStateBK() {
        currentColor = map.get(Settings.newsInstance().getString(Settings.THEME_CHANGE_KEY, ""));
        if (currentColor != null) {
            switch (currentColor) {
                case GREEN:
                    return R.drawable.navigation_item_state_bc_green;
                case BLUE:
                    return R.drawable.navigation_item_state_bc_blue;
                case PURPLE:
                    return R.drawable.navigation_item_state_bc_purple;
                case BLACK:
                    return -1;
                case RED:
                default:
                    return R.drawable.navigation_item_state_bc_red;
            }
        }
        return R.drawable.navigation_item_state_bc_red;
    }
}
