package com.example.task;

import android.os.Parcel;
import android.os.Parcelable;

public class TripModel implements Parcelable {
    private String trip_title , trip_number, price, destination, departure_time, return_time, description ,company;

    public TripModel() {
    }

    public TripModel(String trip_title, String trip_number, String price, String destination, String departure_time, String return_time, String description, String company) {
        this.trip_title = trip_title;
        this.trip_number = trip_number;
        this.price = price;
        this.destination = destination;
        this.departure_time = departure_time;
        this.return_time = return_time;
        this.description = description;
        this.company = company;
    }

    protected TripModel(Parcel in) {
        trip_title = in.readString();
        trip_number = in.readString();
        price = in.readString();
        destination = in.readString();
        departure_time = in.readString();
        return_time = in.readString();
        description = in.readString();
        company = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trip_title);
        dest.writeString(trip_number);
        dest.writeString(price);
        dest.writeString(destination);
        dest.writeString(departure_time);
        dest.writeString(return_time);
        dest.writeString(description);
        dest.writeString(company);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TripModel> CREATOR = new Creator<TripModel>() {
        @Override
        public TripModel createFromParcel(Parcel in) {
            return new TripModel(in);
        }

        @Override
        public TripModel[] newArray(int size) {
            return new TripModel[size];
        }
    };

    public String getTrip_title() {
        return trip_title;
    }

    public void setTrip_title(String trip_title) {
        this.trip_title = trip_title;
    }

    public String getTrip_number() {
        return trip_number;
    }

    public void setTrip_number(String trip_number) {
        this.trip_number = trip_number;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(String departure_time) {
        this.departure_time = departure_time;
    }

    public String getReturn_time() {
        return return_time;
    }

    public void setReturn_time(String return_time) {
        this.return_time = return_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
