package com.swift.dating.model.requestmodel;

public class ApplyVipTokenRequest {
    public int getTime() {
        return time;
    }

    public int getNumberOfTokens() {
        return numberOfTokens;
    }

    private int time;
    private int numberOfTokens;

    public ApplyVipTokenRequest(int time, int numberOfTokens) {
        this.time = time;
        this.numberOfTokens = numberOfTokens;
    }
}
