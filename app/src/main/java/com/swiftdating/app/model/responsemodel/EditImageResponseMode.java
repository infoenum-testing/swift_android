package com.swiftdating.app.model.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.swiftdating.app.model.BaseModel;
import com.swiftdating.app.model.ImageModel;

public class EditImageResponseMode extends BaseModel {

        @SerializedName("images")
        @Expose
        private ImageModel image;

        public ImageModel getImages() {
            return image;
        }
    }
