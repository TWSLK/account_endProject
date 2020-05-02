package com.account.work.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * viewpager adapter
 * Use when viewpager is loaded with fragment
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;
    private String titles[];


    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public FragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragments, String[] titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    public FragmentAdapter setTitles(String[] titles) {
        this.titles = titles;
        return this;
    }

    public FragmentAdapter addFragment(Fragment fragment) {
        if (fragments == null) {
            fragments = new ArrayList<>();
        }
        fragments.add(fragment);
        return this;
    }

    public FragmentAdapter setFragments(ArrayList<Fragment> fragments) {
        this.fragments = fragments;
        return this;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles == null) {
            return super.getPageTitle(position);
        }
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
