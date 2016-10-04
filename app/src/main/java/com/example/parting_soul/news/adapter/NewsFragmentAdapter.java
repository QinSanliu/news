package com.example.parting_soul.news.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by parting_soul on 2016/10/4.
 */

public class NewsFragmentAdapter extends FragmentStatePagerAdapter {

    /**
     * 数据源
     */
    private List<Fragment> mLists;

    public NewsFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public NewsFragmentAdapter(FragmentManager fm, List<Fragment> lists) {
        super(fm);
        mLists = lists;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        return mLists.get(position);
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return mLists.size();
    }

}
