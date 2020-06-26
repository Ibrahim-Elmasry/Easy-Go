package com.example.easygo.Models.DbModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.easygo.Models.UserFlightChoiceSorting;

public class BookedTrips implements Parcelable {

   private String userId,tripId,count,totalPrice;

   private TripModel flight;

    public BookedTrips() {
    }

    public BookedTrips(String userId, String count, String totalPrice, TripModel flight) {
        this.userId = userId;
        this.count = count;
        this.totalPrice = totalPrice;
        this.flight = flight;
    }

    protected BookedTrips(Parcel in) {
        userId = in.readString();
        tripId = in.readString();
        count = in.readString();
        totalPrice = in.readString();
        flight = in.readParcelable(TripModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(tripId);
        dest.writeString(count);
        dest.writeString(totalPrice);
        dest.writeParcelable(flight, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BookedTrips> CREATOR = new Creator<BookedTrips>() {
        @Override
        public BookedTrips createFromParcel(Parcel in) {
            return new BookedTrips(in);
        }

        @Override
        public BookedTrips[] newArray(int size) {
            return new BookedTrips[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public TripModel getFlight() {
        return flight;
    }

    public void setFlight(TripModel flight) {
        this.flight = flight;
    }
}
