package com.example.easygo.Utils;

import com.example.easygo.Models.DbModels.TripModel;
import com.example.easygo.Models.UserFlightChoiceSorting;
import com.google.common.base.Predicate;

import java.util.regex.Pattern;

import javax.annotation.Nullable;


public class TripFilter implements Predicate<TripModel>
{
    private final Pattern pattern;
    public TripFilter(final String id)
    {
        pattern = Pattern.compile(id);

    }


    @Override
    public boolean apply(@Nullable TripModel input) {


        if (input != null) {
            return pattern.matcher(input.getFcmCompany().getId()).find();
        }else {
            return false;
        }

    }
}
