package com.swift.dating.model.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import com.swift.dating.model.BaseModel;
import com.swift.dating.model.ChatModel;

public class MessageListModel extends BaseModel {
        @Expose
        @SerializedName("chatList")
        private ArrayList<ChatModel> chatModels;

    public ArrayList<ChatModel> getChatModels() {
        return chatModels;
    }

    public void setChatModels(ArrayList<ChatModel> chatModels) {
        this.chatModels = chatModels;
    }
}
