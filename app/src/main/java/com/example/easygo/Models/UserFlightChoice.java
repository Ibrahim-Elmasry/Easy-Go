package com.example.easygo.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.easygo.Models.DbModels.FlightModel;
import com.example.easygo.Models.DbModels.TripModel;

import java.util.List;

public class UserFlightChoice implements Parcelable {

   private String classType, classPrice,childCount , adultCount ;

    public UserFlightChoice() {
    }

    public UserFlightChoice(String classType, String classPrice, String childCount, String adultCount) {
        this.classType = classType;
        this.classPrice = classPrice;
        this.childCount = childCount;
        this.adultCount = adultCount;
    }

    protected UserFlightChoice(Parcel in) {
        classType = in.readString();
        classPrice = in.readString();
        childCount = in.readString();
        adultCount = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(classType);
        dest.writeString(classPrice);
        dest.writeString(childCount);
        dest.writeString(adultCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserFlightChoice> CREATOR = new Creator<UserFlightChoice>() {
        @Override
        public UserFlightChoice createFromParcel(Parcel in) {
            return new UserFlightChoice(in);
        }

        @Override
        public UserFlightChoice[] newArray(int size) {
            return new UserFlightChoice[size];
        }
    };

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getClassPrice() {
        return classPrice;
    }

    public void setClassPrice(String classPrice) {
        this.classPrice = classPrice;
    }

    public String getChildCount() {
        return childCount;
    }

    public void setChildCount(String childCount) {
        this.childCount = childCount;
    }

    public String getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(String adultCount) {
        this.adultCount = adultCount;
    }

    public String getTotalPrice() {
        int x = Integer.valueOf(classPrice);
        int z = Integer.valueOf(childCount);
        int y = Integer.valueOf(adultCount);
        return String.valueOf( ((x*y)+((x/2)*z)) );
    }
    public int getTotalCount(){
        int z = Integer.valueOf(childCount);
        int y = Integer.valueOf(adultCount);
        return z+y;
    }

}
