package com.swift.dating.model.requestmodel.createaccountmodel;

public class CreateAccountGenderModel {
    private String gender;
    private String showmeto;

    public CreateAccountGenderModel(String gender,String showmeto) {
        this.gender = gender;
        this.showmeto = showmeto;
    }

    public String getShowmeto() {
        return showmeto;
    }

    public String getGender() {
        return gender;
    }
}
