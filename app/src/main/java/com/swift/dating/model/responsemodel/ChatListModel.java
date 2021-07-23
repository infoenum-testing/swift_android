package com.swift.dating.model.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import com.swift.dating.model.BaseModel;

public class ChatListModel extends BaseModel {



    public static class ChatList {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("isDirectChat")
        @Expose
        private String isDirectChat;
        @SerializedName("roleId")
        @Expose
        private Integer roleId;
        @SerializedName("isLinkedinUser")
        @Expose
        private String isLinkedinUser;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("statusByadmin")
        @Expose
        private String statusByadmin;
        @SerializedName("isRejected")
        @Expose
        private Integer isRejected;
        @SerializedName("isReported")
        @Expose
        private String isReported;
        @SerializedName("isVerified")
        @Expose
        private String isVerified;
        @SerializedName("isPremium")
        @Expose
        private String isPremium;
        @SerializedName("approvesIn")
        @Expose
        private String approvesIn;
        @SerializedName("deletedAt")
        @Expose
        private String deletedAt;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("profileOfUser")
        @Expose
        private ProfileOfUser profileOfUser;
        @SerializedName("ImageForUser")
        @Expose
        private List<ImageForUser> imageForUser = null;
        @SerializedName("ChatByUser")
        @Expose
        private ChatByUser chatByUser;
        @SerializedName("UnreadMessages")
        @Expose
        private int UnreadMessages;

        public int getUnreadMessages() {
            return UnreadMessages;
        }

        public void setUnreadMessages(int unreadMessages) {
            UnreadMessages = unreadMessages;
        }

        public ChatList(ProfileOfUser profileOfUser, ArrayList<ImageForUser> imageForUsers, ChatByUser chat) {
            this.profileOfUser = profileOfUser;
            this.imageForUser = imageForUsers;
            this.chatByUser = chat;
        }

        public String getIsDirectChat() {
            return isDirectChat;
        }

        public void setIsDirectChat(String isDirectChat) {
            this.isDirectChat = isDirectChat;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Integer getRoleId() {
            return roleId;
        }

        public void setRoleId(Integer roleId) {
            this.roleId = roleId;
        }

        public String getIsLinkedinUser() {
            return isLinkedinUser;
        }

        public void setIsLinkedinUser(String isLinkedinUser) {
            this.isLinkedinUser = isLinkedinUser;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatusByadmin() {
            return statusByadmin;
        }

        public void setStatusByadmin(String statusByadmin) {
            this.statusByadmin = statusByadmin;
        }

        public Integer getIsRejected() {
            return isRejected;
        }

        public void setIsRejected(Integer isRejected) {
            this.isRejected = isRejected;
        }

        public String getIsReported() {
            return isReported;
        }

        public void setIsReported(String isReported) {
            this.isReported = isReported;
        }

        public String getIsVerified() {
            return isVerified;
        }

        public void setIsVerified(String isVerified) {
            this.isVerified = isVerified;
        }

        public String getIsPremium() {
            return isPremium;
        }

        public void setIsPremium(String isPremium) {
            this.isPremium = isPremium;
        }

        public String getApprovesIn() {
            return approvesIn;
        }

        public void setApprovesIn(String approvesIn) {
            this.approvesIn = approvesIn;
        }

        public String  getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(String deletedAt) {
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

        public ProfileOfUser getProfileOfUser() {
            return profileOfUser;
        }

        public void setProfileOfUser(ProfileOfUser profileOfUser) {
            this.profileOfUser = profileOfUser;
        }

        public List<ImageForUser> getImageForUser() {
            return imageForUser;
        }

        public void setImageForUser(List<ImageForUser> imageForUser) {
            this.imageForUser = imageForUser;
        }

        public ChatByUser getChatByUser() {
            return chatByUser;
        }

        public void setChatByUser(ChatByUser chatByUser) {
            this.chatByUser = chatByUser;
        }

    }

        @SerializedName("chatList")
        @Expose
        private List<ChatList> chatList = null;

        public List<ChatList> getChatList() {
            return chatList;
        }
        public void setChatList(List<ChatList> chatList) {
            this.chatList = chatList;
        }


}
