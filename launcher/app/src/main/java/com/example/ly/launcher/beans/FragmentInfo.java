package com.example.ly.launcher.beans;

import android.support.v4.app.Fragment;

/**
 */
public class FragmentInfo {
    //标记
    public String mark;
    //展示的fragment
    public Fragment fragment;

    public FragmentInfo(String mark,Fragment fragment) {
        this.mark = mark;
        this.fragment = fragment;
    }


}
