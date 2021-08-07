package com.swiftdating.app.model.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import com.swiftdating.app.model.BaseModel;
import com.swiftdating.app.model.ImageModel;

public class OrderImageResponseModel extends BaseModel {

    @SerializedName("images")
    @Expose
    private Image images = null;

    public OrderImageResponseModel() {
    }


    public Image getImages() {
        return images;
    }


    public class Image {

        @SerializedName("success")
        @Expose
        private boolean success;
        @SerializedName("data")
        @Expose
        private ArrayList<ImageModel> data;

        public boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public ArrayList<ImageModel> getData() {
            return data;
        }

        public void setData(ArrayList<ImageModel> data) {
            this.data = data;
        }

    }
}
