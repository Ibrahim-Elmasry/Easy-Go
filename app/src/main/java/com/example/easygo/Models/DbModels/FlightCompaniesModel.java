package com.example.easygo.Models.DbModels;

import android.os.Parcel;
import android.os.Parcelable;

public class FlightCompaniesModel implements Parcelable{
    private String id,title,logo,email,fax,phone,address;

    public FlightCompaniesModel() {
    }

    public FlightCompaniesModel(String id, String title, String logo) {
        this.id = id;
        this.title = title;
        this.logo = logo;
    }

    public FlightCompaniesModel(String id, String title, String logo, String email, String fax, String phone, String address) {
        this.id = id;
        this.title = title;
        this.logo = logo;
        this.email = email;
        this.fax = fax;
        this.phone = phone;
        this.address = address;
    }


    protected FlightCompaniesModel(Parcel in) {
        id = in.readString();
        title = in.readString();
        logo = in.readString();
        email = in.readString();
        fax = in.readString();
        phone = in.readString();
        address = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(logo);
        dest.writeString(email);
        dest.writeString(fax);
        dest.writeString(phone);
        dest.writeString(address);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FlightCompaniesModel> CREATOR = new Creator<FlightCompaniesModel>() {
        @Override
        public FlightCompaniesModel createFromParcel(Parcel in) {
            return new FlightCompaniesModel(in);
        }

        @Override
        public FlightCompaniesModel[] newArray(int size) {
            return new FlightCompaniesModel[size];
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
