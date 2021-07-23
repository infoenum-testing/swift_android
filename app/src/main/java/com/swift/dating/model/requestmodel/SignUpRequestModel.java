package com.swift.dating.model.requestmodel;

public class SignUpRequestModel {
    private String email,socialId;
    private String socialType,name;
    private String deviceType,devicetoken,linkedinId;

    public SignUpRequestModel(String email,String socialType,String socialId,String devicetoken,String linkedinId,String name) {
        this.email = email;
        this.socialId = socialId;
        this.socialType = socialType;
        this.devicetoken = devicetoken;
        this.deviceType = "ANDROID";
        this.linkedinId = linkedinId;
        this.name = name;
    }
    public SignUpRequestModel(String email,String socialType,String socialId,String devicetoken) {
        this.email = email;
        this.socialId = socialId;
        this.socialType = socialType;
        this.devicetoken = devicetoken;
        this.deviceType = "ANDROID";
    }

}
