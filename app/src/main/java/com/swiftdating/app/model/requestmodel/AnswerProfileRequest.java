package com.swiftdating.app.model.requestmodel;

import android.text.TextUtils;

public class AnswerProfileRequest {
    public AnswerProfileRequest(String question1 ,String answer1,String question2,String answer2,
                                String question3, String answer3) {
        if(!TextUtils.isEmpty(answer1)) {
            Answer1 = answer1;
            Question1 = question1;
        }else{
            Answer1 = "";
            Question1 = "";
        }
        if(!TextUtils.isEmpty(answer2)) {
            Answer2= answer2;
            Question2 = question2;
        }else{
            Answer2 = "";
            Question2 = "";
        }
        if(!TextUtils.isEmpty(answer3)) {
            Answer3= answer3;
            Question3 = question3;
        }else{
            Answer3 = "";
            Question3 = "";
        }
    }

    private String Answer1,Answer2,Answer3,Question1,Question2,Question3;

}
