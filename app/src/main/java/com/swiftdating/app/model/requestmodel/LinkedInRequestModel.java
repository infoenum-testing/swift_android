package com.swiftdating.app.model.requestmodel;

public class LinkedInRequestModel {
    private String linkedinId,name;
    private  String deviceType,devicetoken;

    public LinkedInRequestModel(String linkedinId,String name,String devicetoken) {
        this.linkedinId = linkedinId;
        this.name = name;
        this.deviceType = "ANDROID";
        this.devicetoken = devicetoken;

    }

    public String getLinkedinId() {
        return linkedinId;
    }

    public String getName() {
        return name;
    }
}
