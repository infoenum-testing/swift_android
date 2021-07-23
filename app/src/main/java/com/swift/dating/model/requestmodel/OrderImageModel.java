package com.swift.dating.model.requestmodel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrderImageModel {


    @SerializedName("images")
    ArrayList<ImagesModels> images;

    public OrderImageModel(ArrayList<ImagesModels> images) {
        this.images = images;
    }

    public ArrayList<ImagesModels> getImages() {
        return images;
    }


}
