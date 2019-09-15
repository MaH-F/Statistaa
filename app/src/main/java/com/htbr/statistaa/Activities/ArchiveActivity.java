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

        mAdapter = new PagerAdapter(getSupportFragmentManager());



        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mPager);


//        // Watch for button clicks.
//        Button button = (Button)findViewById(R.id.goto_first);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPager.setCurrentItem(0);
//            }
//        });
//        button = (Button)findViewById(R.id.goto_last);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPager.setCurrentItem(NUM_ITEMS-1);
//            }
//        });
    }



}