package com.example.easygo.Models.DbModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.easygo.Models.UserFlightChoice;
import com.example.easygo.Models.UserFlightChoiceSorting;

public class BookedFlights implements Parcelable {

   private String id;
   private String flightId;
   private UserFlightChoiceSorting flight;

    public BookedFlights() {
    }

    public BookedFlights(String id, UserFlightChoiceSorting flight) {
        this.id = id;
        this.flight = flight;
    }

    protected BookedFlights(Parcel in) {
        id = in.readString();
        flightId = in.readString();
        flight = in.readParcelable(UserFlightChoiceSorting.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(flightId);
        dest.writeParcelable(flight, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BookedFlights> CREATOR = new Creator<BookedFlights>() {
        @Override
        public BookedFlights createFromParcel(Parcel in) {
            return new BookedFlights(in);
        }

        @Override
        public BookedFlights[] newArray(int size) {
            return new BookedFlights[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public UserFlightChoiceSorting getFlight() {
        return flight;
    }

    public void setFlight(UserFlightChoiceSorting flight) {
        this.flight = flight;
    }
}
