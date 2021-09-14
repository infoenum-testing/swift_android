package com.swiftdating.app.model.requestmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilterRequest {
    @SerializedName("pageNumber")
    @Expose
    private Integer pageNumber = 1;
    @SerializedName("limit")
    @Expose
    private Integer limit = 15;
    @SerializedName("distance")
    @Expose
    private Integer distance=500;
    @SerializedName("gender")
    @Expose
    private String gender="Both";
    @SerializedName("maxAgePrefer")
    @Expose
    private Integer maxAgePrefer=80;
    @SerializedName("minAgePrefer")
    @Expose
    private Integer minAgePrefer=18;
    @SerializedName("lookingFor")
    @Expose
    private String lookingFor;
    @SerializedName("maxHeight")
    @Expose
    private String maxHeight="7.1";
    @SerializedName("minHeight")
    @Expose
    private String minHeight="3.9";
    @SerializedName("education")
    @Expose
    private String education;
    @SerializedName("kids")
    @Expose
    private String kids;
    @SerializedName("political")
    @Expose
    private String political;
    @SerializedName("religion")
    @Expose
    private String religion;
    @SerializedName("smoke")
    @Expose
    private String smoke;

    @SerializedName("interested")
    @Expose
    private String interested;


    public FilterRequest(Integer pageNumber, Integer limit, Integer distance, String gender, Integer maxAgePrefer, Integer minAgePrefer, String lookingFor, String maxHeight, String minHeight, String education, String kids, String political, String religion, String smoke) {
        this.pageNumber = pageNumber;
        this.limit = limit;
        this.distance = distance;
        this.gender = gender;
        this.maxAgePrefer = maxAgePrefer;
        this.minAgePrefer = minAgePrefer;
        this.lookingFor = lookingFor;
        this.maxHeight = maxHeight;
        this.minHeight = minHeight;
        this.education = education;
        this.kids = kids;
        this.political = political;
        this.religion = religion;
        this.smoke = smoke;
    }

    public FilterRequest() {
    }


    public String getInterested() {
        return interested;
    }

    public void setInterested(String interested) {
        this.interested = interested;
    }

    public String getPageNumberKey() {
        return "pageNumber";
    }

    public String getLimitKey() {
        return "limit";
    }

    public String getDistanceKey() {
        return "distance";
    }

    public String getGenderKey() {
        return "gender";
    }

    public String getMaxAgePreferKey() {
        return "maxAgePrefer";
    }

    public String getMinAgePreferKey() {
        return "minAgePrefer";
    }

    public String getLookingForKey() {
        return "lookingFor";
    }

    public String getMaxHeightKey() {
        return "maxHeight";
    }

    public String getMinHeightKey() {
        return "minHeight";
    }

    public String getEducationKey() {
        return "education";
    }

    public String getKidsKey() {
        return "kids";
    }

    public String getPoliticalKey() {
        return "political";
    }

    public String getReligionKey() {
        return "religion";
    }

    public String getSmokeKey() {
        return "smoke";
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getMaxAgePrefer() {
        return maxAgePrefer;
    }

    public void setMaxAgePrefer(Integer maxAgePrefer) {
        this.maxAgePrefer = maxAgePrefer;
    }

    public Integer getMinAgePrefer() {
        return minAgePrefer;
    }

    public void setMinAgePrefer(Integer minAgePrefer) {
        this.minAgePrefer = minAgePrefer;
    }

    public String getLookingFor() {
        return lookingFor;
    }

    public void setLookingFor(String lookingFor) {
        this.lookingFor = lookingFor;
    }

    public String getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(String maxHeight) {
        this.maxHeight = maxHeight;
    }

    public String getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(String minHeight) {
        this.minHeight = minHeight;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getKids() {
        return kids;
    }

    public void setKids(String kids) {
        this.kids = kids;
    }

    public String getPolitical() {
        return political;
    }

    public void setPolitical(String political) {
        this.political = political;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getSmoke() {
        return smoke;
    }

    public void setSmoke(String smoke) {
        this.smoke = smoke;
    }

    @Override
    public String toString() {
        return "FilterRequest{" +
                "pageNumber=" + pageNumber +
                ", limit=" + limit +
                ", distance=" + distance +
                ", gender='" + gender + '\'' +
                ", maxAgePrefer=" + maxAgePrefer +
                ", minAgePrefer=" + minAgePrefer +
                ", lookingFor='" + lookingFor + '\'' +
                ", maxHeight='" + maxHeight + '\'' +
                ", minHeight='" + minHeight + '\'' +
                ", education='" + education + '\'' +
                ", kids='" + kids + '\'' +
                ", political='" + political + '\'' +
                ", religion='" + religion + '\'' +
                ", smoke='" + smoke + '\'' +
                ", interested='" + interested + '\'' +
                '}';
    }
}
