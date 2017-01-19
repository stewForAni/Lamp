package com.lamps.lamps.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by stew on 16/8/30.
 * mail: stewforani@gmail.com
 */
public class MyPagerAdapter extends FragmentPagerAdapter{
    private List<String> tabNames;
    private List<Fragment> fragments;

    public MyPagerAdapter(FragmentManager fm, List<String> tabNames, List<Fragment> fragments) {
        super(fm);
        this.tabNames = tabNames;
        this.fragments = fragments;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabNames.get(position);
    }
}
