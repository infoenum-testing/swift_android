package com.swift.dating.model.requestmodel.createaccountmodel;

public class CreateAccountLocationModel {

    private String City;
    private String State;
    private String Country;

    public CreateAccountLocationModel(String city, String state, String country) {
        City = city;
        State = state;
        Country = country;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }
}
