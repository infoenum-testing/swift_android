package com.swiftdating.app.model.requestmodel.createaccountmodel;

import com.google.gson.annotations.SerializedName;

public class CreateAccountEmailModel {

    @SerializedName("useremail")
    private String useremail;


    public CreateAccountEmailModel(String useremail) {
        this.useremail = useremail;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    @Override
    public String toString() {
        return "CreateAccountEmailModel{" +
                "useremail='" + useremail + '\'' +
                '}';
    }
}
