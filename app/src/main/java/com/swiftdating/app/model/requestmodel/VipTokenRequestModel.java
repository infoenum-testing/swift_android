package com.swiftdating.app.model.requestmodel;

public class VipTokenRequestModel {
    private int vipTokens;
    private double price;

    public VipTokenRequestModel(int vipTokens, double price) {
        this.vipTokens = vipTokens;
        this.price = price;
    }

    public int getVipTokens() {
        return vipTokens;
    }
}
