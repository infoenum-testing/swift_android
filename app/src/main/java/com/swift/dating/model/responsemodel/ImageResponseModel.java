package com.swift.dating.model.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import com.swift.dating.model.BaseModel;
import com.swift.dating.model.ImageModel;

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
}
