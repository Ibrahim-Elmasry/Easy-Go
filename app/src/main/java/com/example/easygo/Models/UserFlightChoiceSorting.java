package com.example.easygo.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.easygo.Models.DbModels.FlightModel;

public class UserFlightChoiceSorting implements Parcelable {

   private FlightModel model ;
   private UserFlightChoice choice ;

    public UserFlightChoiceSorting(FlightModel model, UserFlightChoice choice) {
        this.model = model;
        this.choice = choice;
    }

    public UserFlightChoiceSorting() {
    }

    protected UserFlightChoiceSorting(Parcel in) {
        model = in.readParcelable(FlightModel.class.getClassLoader());
        choice = in.readParcelable(UserFlightChoice.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(model, flags);
        dest.writeParcelable(choice, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserFlightChoiceSorting> CREATOR = new Creator<UserFlightChoiceSorting>() {
        @Override
        public UserFlightChoiceSorting createFromParcel(Parcel in) {
            return new UserFlightChoiceSorting(in);
        }

        @Override
        public UserFlightChoiceSorting[] newArray(int size) {
            return new UserFlightChoiceSorting[size];
        }
    };

    public FlightModel getModel() {
        return model;
    }

    public void setModel(FlightModel model) {
        this.model = model;
    }

    public UserFlightChoice getChoice() {
        return choice;
    }

    public void setChoice(UserFlightChoice choice) {
        this.choice = choice;
    }
}
