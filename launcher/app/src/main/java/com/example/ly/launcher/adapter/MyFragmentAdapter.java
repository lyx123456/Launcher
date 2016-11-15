package com.example.ly.launcher.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.ly.launcher.beans.FragmentInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建者 Administrator
 * @创建时间 2016/9/27 14:05
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class MyFragmentAdapter extends FragmentPagerAdapter {

    private List<FragmentInfo> mShowItems = new ArrayList<>();

    public MyFragmentAdapter(FragmentManager fm, List<FragmentInfo> showItems ) {
        super(fm);
        this.mShowItems = showItems;
    }

    @Override
    public Fragment getItem(int position) {
        return mShowItems.get(position).fragment;
    }

    @Override
    public int getCount() {
        return mShowItems.size();
    }

}
