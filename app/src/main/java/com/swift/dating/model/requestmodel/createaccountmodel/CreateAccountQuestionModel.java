package com.swift.dating.model.requestmodel.createaccountmodel;

public class CreateAccountQuestionModel {

    private String Question1,Question2,Question3;

    public CreateAccountQuestionModel(String ques1, String ques2, String ques3) {
        this.Question1 = ques1;
        this.Question2 = ques2;
        this.Question3 = ques3;
    }

    public String getQuestion1() {
        return Question1;
    }

    public String getQuestion2() {
        return Question2;
    }

    public String getQuestion3() {
        return Question3;
    }
}
