package com.example.easygo.User.Trip;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.example.easygo.BaseActivity;
import com.example.easygo.R;
import com.example.easygo.User.MainPagerViewAdapter;

public class YourTripActivity extends BaseActivity {
    private MainPagerViewAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_tracking);

        showBackBtn("Your Trips");

        mSectionsPagerAdapter = new MainPagerViewAdapter(getSupportFragmentManager(),1);
        mSectionsPagerAdapter.setContext(YourTripActivity.this);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.main_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        /*if (Locale.getDefault().getLanguage().equals("ar")) {
            mViewPager.setRotationY(180);
        }*/


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        //to make the tabs icons scroll
        //tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
        tabLayout.setSelectedTabIndicatorHeight((int) (3 * getResources().getDisplayMetrics().density));
        tabLayout.setTabTextColors(Color.parseColor("#000000"), getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void doSomethingWhenAnotherActivityFinish(String result) {

    }
}
