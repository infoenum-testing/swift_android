package com.swiftdating.app.model.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.swiftdating.app.model.requestmodel.FilterRequest;

import java.io.Serializable;

public class FilterResponse implements Serializable {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("settings")
    @Expose
    private FilterRequest settings;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public FilterRequest getSettings() {
        return settings;
    }

    public void setSettings(FilterRequest settings) {
        this.settings = settings;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }}
