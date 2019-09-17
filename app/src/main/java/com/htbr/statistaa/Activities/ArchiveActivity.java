package com.htbr.statistaa.Activities;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.htbr.statistaa.Adapters.PagerAdapter;
import com.htbr.statistaa.R;

public class ArchiveActivity extends FragmentActivity {

    static final int NUM_ITEMS = 3;
    PagerAdapter mAdapter;

    ViewPager mPager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

        mAdapter = new PagerAdapter(getSupportFragmentManager(), NUM_ITEMS);



        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mPager);



    }



}