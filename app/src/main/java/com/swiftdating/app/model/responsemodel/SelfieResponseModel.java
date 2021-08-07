package com.swiftdating.app.model.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.swiftdating.app.model.BaseModel;

public class SelfieResponseModel extends BaseModel {
    @SerializedName("selfieData")
    @Expose
    private SelfieData selfieData;



    public SelfieData getSelfieData() {
        return selfieData;
    }

    public void setSelfieData(SelfieData selfieData) {
        this.selfieData = selfieData;
    }

    public class SelfieData {

        @SerializedName("isVerified")
        @Expose
        private String isVerified;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("userId")
        @Expose
        private Integer userId;
        @SerializedName("selfieUrl")
        @Expose
        private String selfieUrl;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;

        public String getIsVerified() {
            return isVerified;
        }

        public void setIsVerified(String isVerified) {
            this.isVerified = isVerified;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getSelfieUrl() {
            return selfieUrl;
        }

        public void setSelfieUrl(String selfieUrl) {
            this.selfieUrl = selfieUrl;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

    }
}
