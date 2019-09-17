package com.htbr.statistaa.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import com.htbr.statistaa.Fragments.RecyclerViewFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    static int NUM_ITEMS;
    static final String[] tabTitles = new String[]{"Box1", "Box2", "Box3"};

    public PagerAdapter(FragmentManager fm, int num) {
        //  super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        super(fm);
        NUM_ITEMS = num;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
       return RecyclerViewFragment.newInstance(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}