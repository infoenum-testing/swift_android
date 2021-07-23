package com.swift.dating.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ImageModel implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("userId")
    @Expose
    private Integer userId;

    @SerializedName("orderId")
    @Expose
    private Integer orderId;

    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    public ImageModel(Integer id, Integer userId, Integer orderId, String imageUrl) {
        this.id = id;
        this.userId = userId;
        this.orderId = orderId;
        this.imageUrl = imageUrl;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "ImageModel{" +
                "id=" + id +
                ", userId=" + userId +
                ", orderId=" + orderId +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
