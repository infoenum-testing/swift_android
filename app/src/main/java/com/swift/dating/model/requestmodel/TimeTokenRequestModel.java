package com.swift.dating.model.requestmodel;

public class TimeTokenRequestModel {
    private int timeTokens;
    private double price;
    public TimeTokenRequestModel(int timeTokens, double price) {
        this.timeTokens = timeTokens;
        this.price = price;
    }

    public int getTimeTokens() {
        return timeTokens;
    }
}
