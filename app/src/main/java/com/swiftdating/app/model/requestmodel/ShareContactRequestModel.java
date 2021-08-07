package com.swiftdating.app.model.requestmodel;

public class ShareContactRequestModel {

    private int matchId;
    private String contact;

    public ShareContactRequestModel(int matchId, String contact) {
        this.matchId = matchId;
        this.contact = contact;
    }

    public int getMatchId() {
        return matchId;
    }

    public String getContact() {
        return contact;
    }
}
