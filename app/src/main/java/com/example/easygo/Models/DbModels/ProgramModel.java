package com.example.easygo.Models.DbModels;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ProgramModel implements Parcelable {
    private String id,name;
    private List<CitiesModel> cities;

    public ProgramModel() {
    }

    public ProgramModel(String id, String name, List<CitiesModel> cities) {
        this.id = id;
        this.name = name;
        this.cities = cities;
    }

    protected ProgramModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        cities = in.createTypedArrayList(CitiesModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeTypedList(cities);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProgramModel> CREATOR = new Creator<ProgramModel>() {
        @Override
        public ProgramModel createFromParcel(Parcel in) {
            return new ProgramModel(in);
        }

        @Override
        public ProgramModel[] newArray(int size) {
            return new ProgramModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CitiesModel> getCities() {
        return cities;
    }

    public void setCities(List<CitiesModel> cities) {
        this.cities = cities;
    }
}
