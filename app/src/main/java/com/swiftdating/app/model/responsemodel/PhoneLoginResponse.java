package com.swiftdating.app.model.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhoneLoginResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("otp")
    @Expose
    private Integer otp;
    @SerializedName("emailType")
    @Expose
    private Integer emailType;
    @SerializedName("newuser")
    @Expose
    private Boolean newuser;

    public Integer getEmailType() {
        return emailType;
    }

    public void setEmailType(Integer emailType) {
        this.emailType = emailType;
    }

    public Boolean getNewuser() {
        return newuser;
    }

    public void setNewuser(Boolean newuser) {
        this.newuser = newuser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    @Override
    public String toString() {
        return "PhoneLoginResponse{" +
                "message='" + message + '\'' +
                ", success=" + success +
                ", otp=" + otp +
                ", emailType=" + emailType +
                ", newuser=" + newuser +
                '}';
    }
}
