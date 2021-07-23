package com.swift.dating.model.requestmodel;

public class SettingsRequestModel {

    //{
    //  "visible": "True/False",
    //  "maxAgePrefer": 0,
    //  "minAgePrefer": 0,
    //  "distance": 0,
    //  "callNotify": "On/Off",
    //  "matchNotify": "On/Off"
    //}

    private String visible;
    private String matchNotify;
    private String emailNotify;
    private String reactionNotify;
    private String expiredMatches;
    private String chatNotify;
    private int maxAgePrefer,minAgePrefer;
    private Integer distance;

    public SettingsRequestModel(String visible, String matchNotify,String emailNotify,String reactionNotify,String expiredMatches,String chatNotify, int maxAgePrefer, int minAgePrefer, Integer distance) {
        this.visible = visible;
        this.matchNotify = matchNotify;
        this.emailNotify = emailNotify;
        this.reactionNotify = reactionNotify;
        this.expiredMatches = expiredMatches;
        this.chatNotify = chatNotify;
        this.maxAgePrefer = maxAgePrefer;
        this.minAgePrefer = minAgePrefer;
        this.distance = distance;
    }

    public String getVisible() {
        return visible;
    }

    public String getMatchNotify() {
        return matchNotify;
    }

    public int getMaxAgePrefer() {
        return maxAgePrefer;
    }

    public int getMinAgePrefer() {
        return minAgePrefer;
    }

    public Integer getDistance() {
        return distance;
    }

    public String getExpiredMatches() {
        return expiredMatches;
    }
    public String getChatNotify() {
        return chatNotify;
    }

}
