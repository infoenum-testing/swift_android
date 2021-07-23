package com.swift.dating.model.requestmodel;

public class MessageListRequestModel {


    private String fromid,toid;

    public MessageListRequestModel(String fromid, String toid) {
        this.fromid = fromid;
        this.toid = toid;
    }

    public String getFromid() {
        return fromid;
    }

    public String getToid() {
        return toid;
    }
}
