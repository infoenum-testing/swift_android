package com.swiftdating.app.model.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubscriptionDetailResponseModel {


    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("subscription")
    @Expose
    private Subscription subscription;
    @SerializedName("error")
    @Expose
    private Error error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public Error getError() {
        return error;
    }

    public class Error {
        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        String code;

    }


    public class Subscription {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("roleId")
        @Expose
        private Integer roleId;
        @SerializedName("linkedinId")
        @Expose
        private Object linkedinId;
        @SerializedName("isLinkedinUser")
        @Expose
        private String isLinkedinUser;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("statusByadmin")
        @Expose
        private String statusByadmin;
        @SerializedName("isRejected")
        @Expose
        private Integer isRejected;
        @SerializedName("isReported")
        @Expose
        private String isReported;
        @SerializedName("isDeluxe")
        @Expose
        private String isDeluxe;
        @SerializedName("isVerified")
        @Expose
        private String isVerified;
        @SerializedName("isPremium")
        @Expose
        private String isPremium;
        @SerializedName("approvesIn")
        @Expose
        private String approvesIn;
        @SerializedName("deletedAt")
        @Expose
        private Object deletedAt;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("subscriptionForUser")
        @Expose
        private SubscriptionForUser subscriptionForUser;

        public String getIsDeluxe() {
            return isDeluxe;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Integer getRoleId() {
            return roleId;
        }

        public void setRoleId(Integer roleId) {
            this.roleId = roleId;
        }

        public Object getLinkedinId() {
            return linkedinId;
        }

        public void setLinkedinId(Object linkedinId) {
            this.linkedinId = linkedinId;
        }

        public String getIsLinkedinUser() {
            return isLinkedinUser;
        }

        public void setIsLinkedinUser(String isLinkedinUser) {
            this.isLinkedinUser = isLinkedinUser;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatusByadmin() {
            return statusByadmin;
        }

        public void setStatusByadmin(String statusByadmin) {
            this.statusByadmin = statusByadmin;
        }

        public Integer getIsRejected() {
            return isRejected;
        }

        public void setIsRejected(Integer isRejected) {
            this.isRejected = isRejected;
        }

        public String getIsReported() {
            return isReported;
        }

        public void setIsReported(String isReported) {
            this.isReported = isReported;
        }

        public String getIsVerified() {
            return isVerified;
        }

        public void setIsVerified(String isVerified) {
            this.isVerified = isVerified;
        }

        public String getIsPremium() {
            return isPremium;
        }

        public void setIsPremium(String isPremium) {
            this.isPremium = isPremium;
        }

        public String getApprovesIn() {
            return approvesIn;
        }

        public void setApprovesIn(String approvesIn) {
            this.approvesIn = approvesIn;
        }

        public Object getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(Object deletedAt) {
            this.deletedAt = deletedAt;
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

        public SubscriptionForUser getSubscriptionForUser() {
            return subscriptionForUser;
        }

        public void setSubscriptionForUser(SubscriptionForUser subscriptionForUser) {
            this.subscriptionForUser = subscriptionForUser;
        }

        @Override
        public String toString() {
            return "Subscription{" +
                    "email='" + email + '\'' +
                    ", status='" + status + '\'' +
                    ", isVerified='" + isVerified + '\'' +
                    ", isPremium='" + isPremium + '\'' +
                    ", subscriptionForUser=" + subscriptionForUser +
                    '}';
        }
    }

    public class SubscriptionForUser {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("userId")
        @Expose
        private Integer userId;
        @SerializedName("subscriptionId")
        @Expose
        private String subscriptionId;
        @SerializedName("loginType")
        @Expose
        private String loginType;
        @SerializedName("orderId")
        @Expose
        private String orderId;
        @SerializedName("purchaseToken")
        @Expose
        private String purchaseToken;
        @SerializedName("purchaseTime")
        @Expose
        private String purchaseTime;
        @SerializedName("signature")
        @Expose
        private String signature;
        @SerializedName("purchaseState")
        @Expose
        private String purchaseState;
        @SerializedName("autoRenewing")
        @Expose
        private String autoRenewing;
        @SerializedName("price")
        @Expose
        private Double price;
        @SerializedName("subscriptionPeriod")
        @Expose
        private Integer subscriptionPeriod;
        @SerializedName("subscriptionStatus")
        @Expose
        private String subscriptionStatus;
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

        public String getSubscriptionId() {
            return subscriptionId;
        }

        public void setSubscriptionId(String subscriptionId) {
            this.subscriptionId = subscriptionId;
        }

        public String getLoginType() {
            return loginType;
        }

        public void setLoginType(String loginType) {
            this.loginType = loginType;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getPurchaseToken() {
            return purchaseToken;
        }

        public void setPurchaseToken(String purchaseToken) {
            this.purchaseToken = purchaseToken;
        }

        public String getPurchaseTime() {
            return purchaseTime;
        }

        public void setPurchaseTime(String purchaseTime) {
            this.purchaseTime = purchaseTime;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getPurchaseState() {
            return purchaseState;
        }

        public void setPurchaseState(String purchaseState) {
            this.purchaseState = purchaseState;
        }

        public String getAutoRenewing() {
            return autoRenewing;
        }

        public void setAutoRenewing(String autoRenewing) {
            this.autoRenewing = autoRenewing;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Integer getSubscriptionPeriod() {
            return subscriptionPeriod;
        }

        public void setSubscriptionPeriod(Integer subscriptionPeriod) {
            this.subscriptionPeriod = subscriptionPeriod;
        }

        public String getSubscriptionStatus() {
            return subscriptionStatus;
        }

        public void setSubscriptionStatus(String subscriptionStatus) {
            this.subscriptionStatus = subscriptionStatus;
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

        @Override
        public String toString() {
            return "SubscriptionForUser{" +
                    "id=" + id +
                    ", userId=" + userId +
                    ", subscriptionId='" + subscriptionId + '\'' +
                    ", loginType='" + loginType + '\'' +
                    ", orderId='" + orderId + '\'' +
                    ", purchaseToken='" + purchaseToken + '\'' +
                    ", purchaseTime='" + purchaseTime + '\'' +
                    ", signature='" + signature + '\'' +
                    ", purchaseState='" + purchaseState + '\'' +
                    ", autoRenewing='" + autoRenewing + '\'' +
                    ", price=" + price +
                    ", subscriptionPeriod=" + subscriptionPeriod +
                    ", subscriptionStatus='" + subscriptionStatus + '\'' +
                    ", createdAt='" + createdAt + '\'' +
                    ", updatedAt='" + updatedAt + '\'' +
                    '}';
        }
    }
}
