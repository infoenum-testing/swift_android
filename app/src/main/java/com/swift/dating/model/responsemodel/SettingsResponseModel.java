package com.swift.dating.model.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.swift.dating.model.BaseModel;

public class SettingsResponseModel extends BaseModel {


    @SerializedName("settings")
    @Expose
    private Settings settings;


    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }


    public class Settings {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("visible")
        @Expose
        private String visible;
        @SerializedName("maxAgePrefer")
        @Expose
        private Integer maxAgePrefer;
        @SerializedName("minAgePrefer")
        @Expose
        private Integer minAgePrefer;
        @SerializedName("distance")
        @Expose
        private Integer distance;
        @SerializedName("expiredMatches")
        @Expose
        private String expiredMatches;
        @SerializedName("matchNotify")
        @Expose
        private String matchNotify;
        @SerializedName("emailNotify")
        @Expose
        private String emailNotify;
        @SerializedName("reactionNotify")
        @Expose
        private String reactionNotify;
        @SerializedName("callReminder")
        @Expose
        private String callReminder;
        @SerializedName("chatNotify")
        @Expose
        private String chatNotify;

        public String getEmailNotify() {
            return emailNotify;
        }

        public String getReactionNotify() {
            return reactionNotify;
        }

        public void setReactionNotify(String reactionNotify) {
            this.reactionNotify = reactionNotify;
        }

        public void setEmailNotify(String emailNotify) {
            this.emailNotify = emailNotify;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getVisible() {
            return visible;
        }

        public void setVisible(String visible) {
            this.visible = visible;
        }

        public Integer getMaxAgePrefer() {
            return maxAgePrefer;
        }

        public void setMaxAgePrefer(Integer maxAgePrefer) {
            this.maxAgePrefer = maxAgePrefer;
        }

        public Integer getMinAgePrefer() {
            return minAgePrefer;
        }

        public void setMinAgePrefer(Integer minAgePrefer) {
            this.minAgePrefer = minAgePrefer;
        }

        public Integer getDistance() {
            return distance;
        }

        public void setDistance(Integer distance) {
            this.distance = distance;
        }

        public String getExpiredMatches() {
            return expiredMatches;
        }

        public void setExpiredMatches(String expiredMatches) {
            this.expiredMatches = expiredMatches;
        }

        public String getMatchNotify() {
            return matchNotify;
        }

        public void setMatchNotify(String matchNotify) {
            this.matchNotify = matchNotify;
        }

        public String getCallReminder() {
            return callReminder;
        }

        public void setCallReminder(String callReminder) {
            this.callReminder = callReminder;
        }

        public String getChatNotify() {
            return chatNotify;
        }

        public void setChatNotify(String chatNotify) {
            this.chatNotify = chatNotify;
        }

    }
}
