package com.swift.dating.model.requestmodel;

public class VerficationRequestModel {

    private String email, devicetoken, deviceType;
    private int otp;

    public VerficationRequestModel(String email, int otp,String deviceToken) {
        this.email = email;
        this.otp = otp;
        this.devicetoken = deviceToken;
        this.deviceType = "ANDROID";
    }

    public VerficationRequestModel() {
    }

    public String getEmail() {
        return email;
    }

    public int getOtp() {
        return otp;
    }
}
