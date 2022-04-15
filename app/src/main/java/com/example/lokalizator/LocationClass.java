package com.example.lokalizator;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationClass implements Parcelable{
    double xCoordinate;
    double yCoordinate;
    String title;
    String type;
    String description;
    float rating;



    public LocationClass(double xCoordinate, double yCoordinate, String title, TypMiejsca type ,String description,    float rating) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.title=title;
        this.type = type.toString();
        this.description = description;
        this.rating = rating;

    }

    protected LocationClass(Parcel in) {
        xCoordinate = in.readDouble();
        yCoordinate = in.readDouble();
        title = in.readString();
        type = in.readString();
        description = in.readString();
        rating = in.readFloat();
    }

    public static final Creator<LocationClass> CREATOR = new Creator<LocationClass>() {
        @Override
        public LocationClass createFromParcel(Parcel in) {
            return new LocationClass(in);
        }

        @Override
        public LocationClass[] newArray(int size) {
            return new LocationClass[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(xCoordinate);
        parcel.writeDouble(yCoordinate);
        parcel.writeString(title);
        parcel.writeString(type);
        parcel.writeString(description);
        parcel.writeFloat(rating);
    }



    public double getxCoordinate() {
        return xCoordinate;
    }

    public double getyCoordinate() {
        return yCoordinate;
    }

    public float getRating() {
        return rating;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

}
