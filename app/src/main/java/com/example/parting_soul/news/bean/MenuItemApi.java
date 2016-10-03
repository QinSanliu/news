package com.example.parting_soul.news.bean;

import com.example.parting_soul.news.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by parting_soul on 2016/10/3.
 * 左侧菜单的所有项目
 */

public class MenuItemApi {
    /**
     * 所有菜单项的列表
     */
    private static List<MenuItemInfo> mLists;

    static {

        mLists = new ArrayList<MenuItemInfo>();
        MenuItemInfo item = new MenuItemInfo();

        item.setNameId(R.string.message);
        item.setImageId(R.mipmap.ic_drawer_message_normal);
        mLists.add(item);

        item = new MenuItemInfo();
        item.setNameId(R.string.collection);
        item.setImageId(R.mipmap.ic_drawer_favorite_normal);
        mLists.add(item);

        item = new MenuItemInfo();
        item.setNameId(R.string.activity);
        item.setImageId(R.mipmap.left_drawer_activity);
        mLists.add(item);

        item = new MenuItemInfo();
        item.setNameId(R.string.offline);
        item.setImageId(R.mipmap.ic_drawer_offline_normal);
        mLists.add(item);

        item = new MenuItemInfo();
        item.setNameId(R.string.night);
        item.setImageId(R.mipmap.ic_night);
        mLists.add(item);

        item = new MenuItemInfo();
        item.setNameId(R.string.feedback);
        item.setImageId(R.mipmap.ic_drawer_feedback_normal);
        mLists.add(item);

        item = new MenuItemInfo();
        item.setNameId(R.string.abdout);
        item.setImageId(R.mipmap.ic_about);
        mLists.add(item);

    }

    public static List<MenuItemInfo> getMenuItems() {
        return mLists;
    }
}
