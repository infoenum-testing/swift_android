package com.swift.dating.model.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.swift.dating.model.BaseModel;

public class ReactResponseModel  extends BaseModel {

        @SerializedName("react")
        @Expose
        private React react;

        public React getReact() {
            return react;
        }

        public void setReact(React react) {
            this.react = react;
        }

    public class React {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("reaction")
        @Expose
        private String reaction;
        @SerializedName("toId")
        @Expose
        private Integer toId;
        @SerializedName("fromId")
        @Expose
        private Integer fromId;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getReaction() {
            return reaction;
        }

        public void setReaction(String reaction) {
            this.reaction = reaction;
        }

        public Integer getToId() {
            return toId;
        }

        public void setToId(Integer toId) {
            this.toId = toId;
        }

        public Integer getFromId() {
            return fromId;
        }

        public void setFromId(Integer fromId) {
            this.fromId = fromId;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

    }

}
