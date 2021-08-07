package com.swiftdating.app.model.requestmodel.createaccountmodel;

import com.google.gson.annotations.SerializedName;

public class CreateAccountNameModel {
    @SerializedName("name")
    private String name;
    @SerializedName("useremail")
    private String useremail;

    public CreateAccountNameModel(String name) {
        this.name = name;
    }

    public CreateAccountNameModel(String name, String useremail) {
        this.name = name;
        this.useremail = useremail;
    }



    public String getUseremail() {
        return useremail;
    }

    public String getName() {
        return name;
    }
}