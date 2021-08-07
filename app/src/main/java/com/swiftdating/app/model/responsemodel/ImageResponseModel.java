package com.swiftdating.app.model.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import com.swiftdating.app.model.BaseModel;
import com.swiftdating.app.model.ImageModel;

public class ImageResponseModel extends BaseModel {

    @SerializedName("images")
    @Expose
    private ArrayList<ImageModel> images;

    public ImageModel getImage() {
        return image;
    }

    @SerializedName("image")
    @Expose
    private ImageModel image;

    public ArrayList<ImageModel> getImages() {
        return images;
    }

    @Override
    public String toString() {
        return "ImageResponseModel{" +
                "images=" + images +
                ", image=" + image +
                '}';
    }
}
