package com.swiftdating.app.model.requestmodel;

public class SignUpRequestModel {
    private String email,socialId;
    private String socialType,name;
    private String deviceType,devicetoken,linkedinId;

    public SignUpRequestModel(String email,String socialType,String socialId,String devicetoken,String linkedinId,String name) {
        this.email = email;
        this.socialId = socialId;
        this.socialType = socialType;
        this.devicetoken = devicetoken;
        this.deviceType = "ANDROID";
        this.linkedinId = linkedinId;
        this.name = name;
    }
    public SignUpRequestModel(String email,String socialType,String socialId,String devicetoken) {
        this.email = email;
        this.socialId = socialId;
        this.socialType = socialType;
        this.devicetoken = devicetoken;
        this.deviceType = "ANDROID";
    }

}/*

Parameter: ["useremail": "sad@fd.com"]
        Response: SUCCESS: {
        ProfileCompleted = 0;
        message = "Profile updated successfully.";
        success = 1;
        user =   {
        AppleID = "<null>";
        VIPExpiry = "2021-07-29T13:55:09.000Z";
        adminCreated = No;
        approvesIn = 5;
        createdAt = "2021-07-29T13:55:09.000Z";
        deleteProfileName = "";
        deletedAt = "<null>";
        email = "";
        id = 447;
        instaToken = "<null>";
        instaTokenCreated = "<null>";
        isDeleted = No;
        isPremium = No;
        isReported = No;
        isVip = No;
        mobile = "+15654646545";
        profileOfUser =     {
        Answer1 = "<null>";
        Answer2 = "<null>";
        Answer3 = "<null>";
        City = "<null>";
        Country = "<null>";
        Drink = "<null>";
        Exercise = "<null>";
        Kids = "<null>";
        Political = "<null>";
        Question1 = "<null>";
        Question2 = "<null>";
        Question3 = "<null>";
        Relegion = "<null>";
        Smoke = "<null>";
        State = "<null>";
        ZodiacSign = "<null>";
        aboutme = "<null>";
        ambitions = "<null>";
        callReminder = On;
        chatNotify = On;
        completed = 0;
        createdAt = "2021-07-29T13:55:09.000Z";
        deletedAt = "<null>";
        directMessageCount = 5;
        distance = 500;
        dob = "<null>";
        education = "<null>";
        emailNotify = On;
        ethnicity = "<null>";
        expiredMatches = On;
        gender = "<null>";
        height = "<null>";
        "height_cm" = "<null>";
        "height_string" = "<null>";
        id = 443;
        interested = "<null>";
        latitude = "<null>";
        longitude = "<null>";
        lookingFor = "<null>";
        matchNotify = On;
        matchUpdates = On;
        maxAgePrefer = 80;
        minAgePrefer = 18;
        name = "<null>";
        occupation = "<null>";
        pets = "<null>";
        reactionNotify = On;
        school = "<null>";
        showmeto = "<null>";
        superLikesCount = 5;
        timeToken = 1;
        updatedAt = "2021-07-29T13:55:30.000Z";
        userId = 447;
        useremail = "sad@fd.com";
        vipToken = 0;
        visible = True;
        };
        roleId = 2;
        selfieVerificationStatus = No;
        status = Incomplete;
        updatedAt = "2021-07-29T13:55:09.000Z";
        viptokenAppliedOn = "<null>";
        };
        }*/