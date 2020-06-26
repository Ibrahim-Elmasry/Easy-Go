package com.example.easygo.Utils;

import android.os.Build;

import com.example.easygo.Models.UserFlightChoiceSorting;
import com.google.common.base.Predicate;
import com.squareup.picasso.Picasso;

import java.util.regex.Pattern;

import javax.annotation.Nullable;


public class FlightFilter implements Predicate<UserFlightChoiceSorting>
{
    private final Pattern pattern;
    private final String  numOfStops;
    public FlightFilter(final String id , final String stop)
    {
        pattern = Pattern.compile(id);
        numOfStops = stop;
    }


    @Override
    public boolean apply(@Nullable UserFlightChoiceSorting input) {


        if (numOfStops.equals("null")){
            return pattern.matcher(input.getModel().getCompany().getId()).find();
        }else {
            if (Integer.valueOf(input.getModel().getStopsNo()).equals(Integer.valueOf(numOfStops))){
                return true;
            }else if (Integer.valueOf(numOfStops) == 2){
                return Integer.valueOf(input.getModel().getStopsNo()) > 1;
            }else {
                return false;
            }
        }

    }
}
