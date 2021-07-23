package com.swift.dating.model.requestmodel;

public class ApplyTimeTokenRequest {

    public int getMatchId() {
        return matchId;
    }

    public int getTime() {
        return time;
    }

    public int getNumberOfTokens() {
        return numberOfTokens;
    }

    private int matchId;
    private int time;
    private int numberOfTokens;

    public ApplyTimeTokenRequest(int matchId, int time, int numberOfTokens) {
        this.matchId = matchId;
        this.time = time;
        this.numberOfTokens = numberOfTokens;
    }


}
