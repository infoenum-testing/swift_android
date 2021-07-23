package com.swift.dating.model.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApplyVipTokenResponse {
    @SerializedName("success")
    @Expose
    boolean success;
    @SerializedName("newExpiry")
    @Expose
    String  newExpiry;
    @SerializedName("error")
    @Expose
    private Error error;


    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getNewExpiry() {
        return newExpiry;
    }

    public void setNewExpiry(String newExpiry) {
        this.newExpiry = newExpiry;
    }

    public ApplyVipTokenResponse(boolean success, String newExpiry, Error error) {
        this.success = success;
        this.newExpiry = newExpiry;
        this.error = error;
    }
}
