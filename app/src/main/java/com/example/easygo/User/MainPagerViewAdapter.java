package com.example.easygo.User;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.easygo.User.Flight.OngoingFlightFragment;
import com.example.easygo.User.Flight.PastFlightFragment;
import com.example.easygo.User.Trip.OngoingTripFragment;
import com.example.easygo.User.Trip.PastTripFragment;

public class MainPagerViewAdapter extends FragmentPagerAdapter {
    private Context context;
    private int check;
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public MainPagerViewAdapter(FragmentManager fm,int check) {
        super(fm);
        this.check=check;
    }

    @Override
    public Fragment getItem(int position) {
        if (check==0){
            switch (position) {
                case 0:
                    return new OngoingFlightFragment();
                case 1:
                    return new PastFlightFragment();

                default:
                    return null;
            }
        }else {
            switch (position) {
                case 0:
                    return new OngoingTripFragment();
                case 1:
                    return new PastTripFragment();

                default:
                    return null;
            }
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Ongoing";
            case 1:
                return "Past";

        }
        return null;
    }
}
