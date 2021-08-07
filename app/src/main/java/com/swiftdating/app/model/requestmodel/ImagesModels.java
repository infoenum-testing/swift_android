package com.swiftdating.app.model.requestmodel;

import com.google.gson.annotations.SerializedName;

public class ImagesModels {
    @SerializedName("imageUrl")
    String imageUrl;
    @SerializedName("orderId")
    int orderId;
    @SerializedName("imageId")
    int imageId;

    public ImagesModels(String imageUrl, int orderId) {
        this.imageUrl = imageUrl;
        this.orderId = orderId;
        this.imageId = imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getOrderId() {
        return orderId;
    }

    @Override
    public String toString() {
        return "ImagesModels{" +
                "imageUrl='" + imageUrl + '\'' +
                ", orderId=" + orderId +
                ", imageId=" + imageId +
                '}';
    }
}