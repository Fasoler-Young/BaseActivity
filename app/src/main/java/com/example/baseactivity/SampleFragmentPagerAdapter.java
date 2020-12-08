package com.example.baseactivity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.TableLayout;

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    private int numOfTabs;
    private String[] page_names;
    SampleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        page_names=("one,two,three").split(",");
        this.numOfTabs = 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return page_names[position];
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new AllView();
            case 1:
                return new AllView();
            case 2:
                return new AllView();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}