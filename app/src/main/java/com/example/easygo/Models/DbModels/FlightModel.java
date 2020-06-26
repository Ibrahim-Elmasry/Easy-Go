package com.example.easygo.Models.DbModels;

import android.os.Parcel;
import android.os.Parcelable;

public class FlightModel implements Parcelable {

    private String id;
    private int vipPrice, vipCount, economicPrice, economicCount, businessPrice, businessCount;
    private String duration, stopsNo, departDate, returnDate, meal, refund, imageUrl;

    private CitiesModel source, destination;
    private FlightCompaniesModel company;

    public FlightModel() {
    }

    public FlightModel(String id, int vipPrice, int vipCount, int economicPrice, int economicCount, int businessPrice, int businessCount, String duration, String stopsNo, String departDate, String returnDate, String meal, String refund, CitiesModel source, CitiesModel destination, FlightCompaniesModel company) {
        this.id = id;
        this.vipPrice = vipPrice;
        this.vipCount = vipCount;
        this.economicPrice = economicPrice;
        this.economicCount = economicCount;
        this.businessPrice = businessPrice;
        this.businessCount = businessCount;
        this.duration = duration;
        this.stopsNo = stopsNo;
        this.departDate = departDate;
        this.returnDate = returnDate;
        this.meal = meal;
        this.refund = refund;
        this.source = source;
        this.destination = destination;
        this.company = company;
    }

    public FlightModel(String id, int vipPrice, int vipCount, int economicPrice, int economicCount, int businessPrice, int businessCount, String duration, String stopsNo, String departDate, String returnDate, String meal, String refund, String imageUrl, CitiesModel source, CitiesModel destination, FlightCompaniesModel company) {
        this.id = id;
        this.vipPrice = vipPrice;
        this.vipCount = vipCount;
        this.economicPrice = economicPrice;
        this.economicCount = economicCount;
        this.businessPrice = businessPrice;
        this.businessCount = businessCount;
        this.duration = duration;
        this.stopsNo = stopsNo;
        this.departDate = departDate;
        this.returnDate = returnDate;
        this.meal = meal;
        this.refund = refund;
        this.imageUrl = imageUrl;
        this.source = source;
        this.destination = destination;
        this.company = company;
    }

    protected FlightModel(Parcel in) {
        id = in.readString();
        vipPrice = in.readInt();
        vipCount = in.readInt();
        economicPrice = in.readInt();
        economicCount = in.readInt();
        businessPrice = in.readInt();
        businessCount = in.readInt();
        duration = in.readString();
        stopsNo = in.readString();
        departDate = in.readString();
        returnDate = in.readString();
        meal = in.readString();
        refund = in.readString();
        imageUrl = in.readString();
        source = in.readParcelable(CitiesModel.class.getClassLoader());
        destination = in.readParcelable(CitiesModel.class.getClassLoader());
        company = in.readParcelable(FlightCompaniesModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(vipPrice);
        dest.writeInt(vipCount);
        dest.writeInt(economicPrice);
        dest.writeInt(economicCount);
        dest.writeInt(businessPrice);
        dest.writeInt(businessCount);
        dest.writeString(duration);
        dest.writeString(stopsNo);
        dest.writeString(departDate);
        dest.writeString(returnDate);
        dest.writeString(meal);
        dest.writeString(refund);
        dest.writeString(imageUrl);
        dest.writeParcelable(source, flags);
        dest.writeParcelable(destination, flags);
        dest.writeParcelable(company, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FlightModel> CREATOR = new Creator<FlightModel>() {
        @Override
        public FlightModel createFromParcel(Parcel in) {
            return new FlightModel(in);
        }

        @Override
        public FlightModel[] newArray(int size) {
            return new FlightModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(int vipPrice) {
        this.vipPrice = vipPrice;
    }

    public int getVipCount() {
        return vipCount;
    }

    public void setVipCount(int vipCount) {
        this.vipCount = vipCount;
    }

    public int getEconomicPrice() {
        return economicPrice;
    }

    public void setEconomicPrice(int economicPrice) {
        this.economicPrice = economicPrice;
    }

    public int getEconomicCount() {
        return economicCount;
    }

    public void setEconomicCount(int economicCount) {
        this.economicCount = economicCount;
    }

    public int getBusinessPrice() {
        return businessPrice;
    }

    public void setBusinessPrice(int businessPrice) {
        this.businessPrice = businessPrice;
    }

    public int getBusinessCount() {
        return businessCount;
    }

    public void setBusinessCount(int businessCount) {
        this.businessCount = businessCount;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getStopsNo() {
        return stopsNo;
    }

    public void setStopsNo(String stopsNo) {
        this.stopsNo = stopsNo;
    }

    public String getDepartDate() {
        return departDate;
    }

    public void setDepartDate(String departDate) {
        this.departDate = departDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public String getRefund() {
        return refund;
    }

    public void setRefund(String refund) {
        this.refund = refund;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public CitiesModel getSource() {
        return source;
    }

    public void setSource(CitiesModel source) {
        this.source = source;
    }

    public CitiesModel getDestination() {
        return destination;
    }

    public void setDestination(CitiesModel destination) {
        this.destination = destination;
    }

    public FlightCompaniesModel getCompany() {
        return company;
    }

    public void setCompany(FlightCompaniesModel company) {
        this.company = company;
    }
}
