package com.example.easygo.Models.DbModels;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class TripModel implements Parcelable {
    private String id;
    private String title,price, duration, numberOfTravelers, description,imageUrl,startAt,endAt;
    private CitiesModel cmCity;
    private FlightCompaniesModel fcmCompany;
    private List<String> programs;

    public TripModel() {
    }

    public TripModel(String id, String title, String price, String duration, String numberOfTravelers, String description, String imageUrl, String startAt, String endAt, CitiesModel cmCity, FlightCompaniesModel fcmCompany, List<String> programs) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.duration = duration;
        this.numberOfTravelers = numberOfTravelers;
        this.description = description;
        this.imageUrl = imageUrl;
        this.startAt = startAt;
        this.endAt = endAt;
        this.cmCity = cmCity;
        this.fcmCompany = fcmCompany;
        this.programs = programs;
    }

    protected TripModel(Parcel in) {
        id = in.readString();
        title = in.readString();
        price = in.readString();
        duration = in.readString();
        numberOfTravelers = in.readString();
        description = in.readString();
        imageUrl = in.readString();
        startAt = in.readString();
        endAt = in.readString();
        cmCity = in.readParcelable(CitiesModel.class.getClassLoader());
        fcmCompany = in.readParcelable(FlightCompaniesModel.class.getClassLoader());
        programs = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(price);
        dest.writeString(duration);
        dest.writeString(numberOfTravelers);
        dest.writeString(description);
        dest.writeString(imageUrl);
        dest.writeString(startAt);
        dest.writeString(endAt);
        dest.writeParcelable(cmCity, flags);
        dest.writeParcelable(fcmCompany, flags);
        dest.writeStringList(programs);
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getNumberOfTravelers() {
        return numberOfTravelers;
    }

    public void setNumberOfTravelers(String numberOfTravelers) {
        this.numberOfTravelers = numberOfTravelers;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public String getEndAt() {
        return endAt;
    }

    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    public CitiesModel getCmCity() {
        return cmCity;
    }

    public void setCmCity(CitiesModel cmCity) {
        this.cmCity = cmCity;
    }

    public FlightCompaniesModel getFcmCompany() {
        return fcmCompany;
    }

    public void setFcmCompany(FlightCompaniesModel fcmCompany) {
        this.fcmCompany = fcmCompany;
    }

    public List<String> getPrograms() {
        return programs;
    }

    public void setPrograms(List<String> programs) {
        this.programs = programs;
    }
}
