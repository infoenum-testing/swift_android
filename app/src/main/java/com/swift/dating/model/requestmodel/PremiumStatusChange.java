package com.swift.dating.model.requestmodel;

public class PremiumStatusChange {

//      "subscriptionId": "string",
//              "subscriptionStatus": "Active/Cancelled"
//  "subscriptionType": 2
    private String subscriptionId,subscriptionStatus;
    private int subscriptionType;

    public PremiumStatusChange(String subscriptionId, String subscriptionStatus) {
        this.subscriptionId = subscriptionId;
        this.subscriptionStatus = subscriptionStatus;
    }

    public PremiumStatusChange(String subscriptionId, String subscriptionStatus, int subscriptionType) {
        this.subscriptionId = subscriptionId;
        this.subscriptionStatus = subscriptionStatus;
        this.subscriptionType = subscriptionType;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public int getSubscriptionType() {
        return subscriptionType;
    }
}
