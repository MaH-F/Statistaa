package com.htbr.statistaa.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.htbr.statistaa.Activities.ArchiveActivity;
import com.htbr.statistaa.Fragments.ArrayListFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    static final int NUM_ITEMS = 3;
    static final String[] tabTitles = new String[]{"Box1", "Box2", "Box3"};

    public PagerAdapter(FragmentManager fm) {
        //  super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        super(fm);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        return ArrayListFragment.newInstance(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}