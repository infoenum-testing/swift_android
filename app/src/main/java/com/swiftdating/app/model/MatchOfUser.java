package com.swiftdating.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MatchOfUser implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("fromId")
    @Expose
    private int fromId;
    @SerializedName("toId")
    @Expose
    private int toId;
    @SerializedName("CalltimerExpiry")
    @Expose
    private String calltimerExpiry;
    @SerializedName("answersGiven")
    @Expose
    private String answersGiven;
    @SerializedName("isChatStarted")
    @Expose
    private String isChatStarted;
    @SerializedName("isNotified")
    @Expose
    private int isNotified;
    @SerializedName("timetokenAppliedOn")
    @Expose
    private String timetokenAppliedOn;
    @SerializedName("deletedAt")
    @Expose
    private Object deletedAt;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public String getCalltimerExpiry() {
        return calltimerExpiry;
    }

    public void setCalltimerExpiry(String calltimerExpiry) {
        this.calltimerExpiry = calltimerExpiry;
    }

    public String getAnswersGiven() {
        return answersGiven;
    }

    public void setAnswersGiven(String answersGiven) {
        this.answersGiven = answersGiven;
    }

    public String getIsChatStarted() {
        return isChatStarted;
    }

    public void setIsChatStarted(String isChatStarted) {
        this.isChatStarted = isChatStarted;
    }

    public int getIsNotified() {
        return isNotified;
    }

    public void setIsNotified(int isNotified) {
        this.isNotified = isNotified;
    }

    public String getTimetokenAppliedOn() {
        return timetokenAppliedOn;
    }

    public void setTimetokenAppliedOn(String timetokenAppliedOn) {
        this.timetokenAppliedOn = timetokenAppliedOn;
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

    @Override
    public String toString() {
        return "MatchOfUser{" +
                "id=" + id +
                ", fromId=" + fromId +
                ", toId=" + toId +
                ", calltimerExpiry='" + calltimerExpiry + '\'' +
                ", answersGiven='" + answersGiven + '\'' +
                ", isChatStarted='" + isChatStarted + '\'' +
                ", isNotified=" + isNotified +
                ", timetokenAppliedOn=" + timetokenAppliedOn +
                ", deletedAt=" + deletedAt +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}