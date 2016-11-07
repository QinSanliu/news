package com.example.parting_soul.news.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.parting_soul.news.fragment.collection.CollectionFragment;
import com.example.parting_soul.news.utils.support.NewsApplication;

import java.util.List;

/**
 * Created by parting_soul on 2016/11/7.
 */

public class CollectionViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;

    public CollectionViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public CollectionViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return NewsApplication.getContext().getString(CollectionFragment.TITLES[position]);
    }
}
