package com.example.easygo.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class UserModel implements Parcelable {

    private String id, type, name, email, password, phone;


    public UserModel(String id, String type,
                     String name, String email,
                     String password, String phone) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public UserModel() {
    }

    protected UserModel(Parcel in) {
        id = in.readString();
        type = in.readString();
        name = in.readString();
        email = in.readString();
        password = in.readString();
        phone = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(type);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(phone);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
