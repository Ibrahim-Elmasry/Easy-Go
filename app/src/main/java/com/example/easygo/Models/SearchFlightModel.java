package com.example.easygo.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.easygo.Models.DbModels.CitiesModel;

public class SearchFlightModel implements Parcelable {
    private CitiesModel cityFrom,cityTo;
    private String timeDepart, timeReturn, countAdult, countChild, flightClass;


    public SearchFlightModel() {
    }

    public SearchFlightModel(CitiesModel cityFrom, CitiesModel cityTo, String timeDepart, String timeReturn, String countAdult, String countChild, String flightClass) {
        this.cityFrom = cityFrom;
        this.cityTo = cityTo;
        this.timeDepart = timeDepart;
        this.timeReturn = timeReturn;
        this.countAdult = countAdult;
        this.countChild = countChild;
        this.flightClass = flightClass;
    }

    protected SearchFlightModel(Parcel in) {
        cityFrom = in.readParcelable(CitiesModel.class.getClassLoader());
        cityTo = in.readParcelable(CitiesModel.class.getClassLoader());
        timeDepart = in.readString();
        timeReturn = in.readString();
        countAdult = in.readString();
        countChild = in.readString();
        flightClass = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(cityFrom, flags);
        dest.writeParcelable(cityTo, flags);
        dest.writeString(timeDepart);
        dest.writeString(timeReturn);
        dest.writeString(countAdult);
        dest.writeString(countChild);
        dest.writeString(flightClass);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SearchFlightModel> CREATOR = new Creator<SearchFlightModel>() {
        @Override
        public SearchFlightModel createFromParcel(Parcel in) {
            return new SearchFlightModel(in);
        }

        @Override
        public SearchFlightModel[] newArray(int size) {
            return new SearchFlightModel[size];
        }
    };

    public CitiesModel getCityFrom() {
        return cityFrom;
    }

    public void setCityFrom(CitiesModel cityFrom) {
        this.cityFrom = cityFrom;
    }

    public CitiesModel getCityTo() {
        return cityTo;
    }

    public void setCityTo(CitiesModel cityTo) {
        this.cityTo = cityTo;
    }

    public String getTimeDepart() {
        return timeDepart;
    }

    public void setTimeDepart(String timeDepart) {
        this.timeDepart = timeDepart;
    }

    public String getTimeReturn() {
        return timeReturn;
    }

    public void setTimeReturn(String timeReturn) {
        this.timeReturn = timeReturn;
    }

    public String getCountAdult() {
        return countAdult;
    }

    public void setCountAdult(String countAdult) {
        this.countAdult = countAdult;
    }

    public String getCountChild() {
        return countChild;
    }

    public void setCountChild(String countChild) {
        this.countChild = countChild;
    }

    public String getFlightClass() {
        return flightClass;
    }

    public void setFlightClass(String flightClass) {
        this.flightClass = flightClass;
    }
}
