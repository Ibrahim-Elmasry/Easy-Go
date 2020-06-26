package com.example.easygo.Models.DbModels;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class CitiesModel implements Parcelable {
    private String id,name;
    private List<String> landMarks;

    public CitiesModel() {
    }

    public CitiesModel(String id, String name, List<String> landMarks) {
        this.id = id;
        this.name = name;
        this.landMarks = landMarks;
    }

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

    public List<String> getLandMarks() {
        return landMarks;
    }

    public void setLandMarks(List<String> landMarks) {
        this.landMarks = landMarks;
    }

    protected CitiesModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        landMarks = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeStringList(landMarks);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CitiesModel> CREATOR = new Creator<CitiesModel>() {
        @Override
        public CitiesModel createFromParcel(Parcel in) {
            return new CitiesModel(in);
        }

        @Override
        public CitiesModel[] newArray(int size) {
            return new CitiesModel[size];
        }
    };
}
