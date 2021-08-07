package com.swiftdating.app.model.requestmodel;

public class DeluxeTokenCountModel {
    private String subscriptionType ,subscriptionId,loginType,orderId,purchaseToken,purchaseTime,signature,purchaseState;
    private double price;
    private int subscriptionPeriod;


    public DeluxeTokenCountModel(String subscriptionType,String subscriptionId, double price, int subscriptionPeriod,
                                 String orderId, String purchaseToken, String purchaseTime, String signature,
                                 String purchaseState) {
        this.subscriptionType = subscriptionType;
        this.subscriptionId = subscriptionId;
        this.price = price;
        this.subscriptionPeriod = subscriptionPeriod;
        this.loginType = "ANDROID";
        this.orderId = orderId;
        this.purchaseToken = purchaseToken;
        this.purchaseTime = purchaseTime;
        this.purchaseState = purchaseState;
        this.signature = signature;
    }


    public int getSubscriptionPeriod() {
        return subscriptionPeriod;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public double getPrice() {
        return price;
    }

    public String getLoginType() {
        return loginType;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getPurchaseToken() {
        return purchaseToken;
    }

    public String getPurchaseTime() {
        return purchaseTime;
    }

    public String getSignature() {
        return signature;
    }

    public String getPurchaseState() {
        return purchaseState;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    @Override
    public String toString() {
        return "DeluxeTokenCountModel{" +
                "subscriptionType='" + subscriptionType + '\'' +
                ", subscriptionId='" + subscriptionId + '\'' +
                ", loginType='" + loginType + '\'' +
                ", orderId='" + orderId + '\'' +
                ", purchaseToken='" + purchaseToken + '\'' +
                ", purchaseTime='" + purchaseTime + '\'' +
                ", signature='" + signature + '\'' +
                ", purchaseState='" + purchaseState + '\'' +
                ", price=" + price +
                ", subscriptionPeriod=" + subscriptionPeriod +
                '}';
    }
}
