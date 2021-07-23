package com.swift.dating.model.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuperLikeResponseModel {

    @SerializedName("success")
    @Expose
    boolean success;
    @SerializedName("totalSuperlikes")
    @Expose
    int totalSuperlikes;
    @SerializedName("totalTimeTokens")
    @Expose
    int totalTimeTokens;
    @SerializedName("totalVIPToken")
    @Expose
    int totalVIPToken;


    public int getTotalVIPToken() {
        return totalVIPToken;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getTotalSuperlikes() {
        return totalSuperlikes;
    }

    public int getTotalTimeTokens() {
        return totalTimeTokens;
    }
}
