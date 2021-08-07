package com.swiftdating.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.swiftdating.app.model.responsemodel.SubscriptionModel;

public class BaseModel {

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("isavailable")
    @Expose
    private Boolean isavaiable;

    @SerializedName("unreadmessages")
    @Expose
    private Boolean unreadmessages;

    @SerializedName("subscription")
    @Expose
    private SubscriptionModel subscription;
    @SerializedName("swipedata")
    @Expose
    public SwipesData swipesData;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("ServerTime")
    @Expose
    private String serverTime;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("error")
    @Expose
    private Error error;

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getMessage() {
        return message;
    }

    public boolean getUnreadMessages() {
        return unreadmessages;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getKey() {
        return key;
    }

    public void setError(Error error) {
        this.error = error;
    }


    public Error getError() {
        return error;
    }

    public Boolean getIsavaiable() {
        return isavaiable;
    }

    public void setIsavaiable(Boolean isavaiable) {
        this.isavaiable = isavaiable;
    }

    public class Error{
        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String code;

    }

    public class SwipesData {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("userId")
        @Expose
        private Integer userId;
        @SerializedName("likesCounter")
        @Expose
        private Integer likesCounter;
        @SerializedName("swipesGivenAt")
        @Expose
        private String swipesGivenAt;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;

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

        public Integer getLikesCounter() {
            return likesCounter;
        }

        public void setLikesCounter(Integer likesCounter) {
            this.likesCounter = likesCounter;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getSwipesGivenAt() {
            return swipesGivenAt;
        }
    }

    @Override
    public String toString() {
        return "BaseModel{" +
                "success=" + success +
                ", isavaiable=" + isavaiable +
                ", unreadmessages=" + unreadmessages +
                ", subscription=" + subscription +
                ", swipesData=" + swipesData +
                ", latitude='" + latitude + '\'' +
                ", serverTime='" + serverTime + '\'' +
                ", key='" + key + '\'' +
                ", message='" + message + '\'' +
                ", error=" + error +
                '}';
    }
}
