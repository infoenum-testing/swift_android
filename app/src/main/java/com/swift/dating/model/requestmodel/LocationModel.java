package com.swift.dating.model.requestmodel;

public class LocationModel {

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    private String latitude;
    private String longitude;

    public LocationModel(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
