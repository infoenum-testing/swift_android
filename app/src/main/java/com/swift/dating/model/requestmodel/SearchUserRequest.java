package com.swift.dating.model.requestmodel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SearchUserRequest implements Serializable {
    @SerializedName("pageNumber")
    int pageNumber;
    @SerializedName("limit")
    int limit;
    @SerializedName("distance")
    int distance;
    @SerializedName("maxAgePrefer")
    int maxAgePrefer;
    @SerializedName("minAgePrefer")
    int minAgePrefer;
    @SerializedName("gender")
    String gender;
    @SerializedName("lookingFor")
    String lookingFor;
    @SerializedName("maxHeight")
    String maxHeight;
    @SerializedName("minHeight")
    String minHeight;
    @SerializedName("education")
    String education;
    @SerializedName("Kids")
    String Kids;
    @SerializedName("political")
    String political;
    @SerializedName("religion")
    String religion;
    @SerializedName("smoke")
    String smoke;

    public SearchUserRequest(int pageNumber, int limit, int distance, int maxAgePrefer, int minAgePrefer, String gender, String lookingFor, String maxHeight, String minHeight, String education, String kids, String political, String religion, String smoke) {
        this.pageNumber = pageNumber;
        this.limit = limit;
        this.distance = distance;
        this.maxAgePrefer = maxAgePrefer;
        this.minAgePrefer = minAgePrefer;
        this.gender = gender;
        this.lookingFor = lookingFor;
        this.maxHeight = maxHeight;
        this.minHeight = minHeight;
        this.education = education;
        Kids = kids;
        this.political = political;
        this.religion = religion;
        this.smoke = smoke;
    }

    @Override
    public String toString() {
        return "SearchUserRequest{" +
                "pageNumber=" + pageNumber +
                ", limit=" + limit +
                ", distance=" + distance +
                ", maxAgePrefer=" + maxAgePrefer +
                ", minAgePrefer=" + minAgePrefer +
                ", gender='" + gender + '\'' +
                ", lookingFor='" + lookingFor + '\'' +
                ", maxHeight='" + maxHeight + '\'' +
                ", minHeight='" + minHeight + '\'' +
                ", education='" + education + '\'' +
                ", Kids='" + Kids + '\'' +
                ", political='" + political + '\'' +
                ", religion='" + religion + '\'' +
                ", smoke='" + smoke + '\'' +
                '}';
    }
}
