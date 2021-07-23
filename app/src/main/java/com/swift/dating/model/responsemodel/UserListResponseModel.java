package com.swift.dating.model.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserListResponseModel  {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName(value = "users",alternate = "matches")
    @Expose
    private ArrayList<User> users = null;
    @SerializedName("data")
    @Expose
    private User data;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("error")
    @Expose
    private Error error;

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }



    public User getData() {
        return data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getKey() {
        return key;
    }

    public String getMessage() {
        return message;
    }

    public Error getError() {
        return error;
    }


    public class Error{
        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        String code;

    }
}