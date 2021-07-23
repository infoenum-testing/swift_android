package com.swift.dating.model.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.swift.dating.model.BaseModel;
import com.swift.dating.model.ImageModel;

public class EditImageResponseMode extends BaseModel {

        @SerializedName("images")
        @Expose
        private ImageModel image;

        public ImageModel getImages() {
            return image;
        }
    }
