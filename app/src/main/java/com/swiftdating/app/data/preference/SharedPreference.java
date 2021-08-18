package com.swiftdating.app.data.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

import com.swiftdating.app.model.ImageModel;
import com.swiftdating.app.model.requestmodel.FilterRequest;
import com.swiftdating.app.model.responsemodel.ProfileOfUser;
import com.swiftdating.app.model.responsemodel.VerificationResponseModel;

public class SharedPreference {

    public static final String userEmail = "emailOfUser";
    public static final String userPhone = "phoneOfUser";
    public static final String fromPhoneFlow = "fromPhoneFlow";
    public static final String fromPhoneBool = "fromPhoneBool";
    private static final String MyPREFERENCES = "MyPrefs";
    private final String isDeluxe = "isDeluxe";
    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor editor;
    private String user = "user";
    private String profileCompleted = "profileCompleted";
    private String userId = "userId";
    private String token = "token";
    private String deviceToken = "deviceToken";
    private String loggedIn = "loggedIn";
    private String images = "images";
    private String firstImage = "firstImage";
    private String selfie = "selfie";
    private String isVerified = "isVerified";
    private String selfieVerificationStatus = "selfieVerificationStatus";
    private String isPremium = "isPremium";
    private String firstTime = "firstTime";
    private String isChatOpen = "isChatOpen";
    private String isDialogOpen = "isDialogOpen";
    private String chatUserId = "chatUserId";
    private String rejected = "rejected";
    private String swipeCount = "swipeCount";
    private String linkedin = "linkedin";
    private String instagramImages = "instaImages";
    private String isSettingsChanged = "isSettingsChanged";
    private String isNumber = "isNumber";
    private String isEmail = "isEmail";
    private String phone = "phone";
    private String NoOfLikes = "noOfLikes";
    private String FilterReq = "FilterReq";
    private Context context;
    private String disLiked = "Disliked";
    private String status = "status";
    public static final String userStatus = "userStatus";


    public SharedPreference(Context context) {
        this.context = context;
        sharedPreference = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreference.edit();
        editor.apply();
    }

    public Context getContext() {
        return context;
    }

    public void saveFilterModel(FilterRequest model) {
        Gson gson = new Gson();
        String json = gson.toJson(model);
        editor.putString(FilterReq, json);
        editor.apply();
        editor.commit();
    }

    public void removeFilter() {
        editor.remove(FilterReq);
        editor.apply();
        editor.commit();
    }

    public FilterRequest getFilterModel() {
        Gson gson = new Gson();
        String json = sharedPreference.getString(FilterReq, "");
        return gson.fromJson(json, FilterRequest.class);
    }

    public String getPhone() {
        return sharedPreference.getString(phone, "");
    }

    public void setPhone(String phone1) {
        editor.putString(phone, phone1);
        editor.apply();
    }

    public void saveLocation(boolean bool) {
        editor.putBoolean("loc", bool).commit();
    }

    public boolean isLocation() {
        return sharedPreference.getBoolean("loc", false);
    }

    public void saveUserData(ProfileOfUser user, String completed) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(this.user, json);
        if (completed != null)
            editor.putString(this.profileCompleted, completed);
        editor.apply();
        editor.commit();
    }


    public void saveNoOfLikes(VerificationResponseModel.NoOfLikes noOfLikes) {
        Gson gson = new Gson();
        String json = gson.toJson(noOfLikes);
        editor.putString(NoOfLikes, json);
        editor.apply();
        editor.commit();
    }

    public void savePreviousLikeCount(int noOfLikes) {
        editor.putInt("PreviousCount", noOfLikes);
        editor.apply();
        editor.commit();
    }

    public int getPreviousLikeCount() {
        return sharedPreference.getInt("PreviousCount", 0);
    }

    public VerificationResponseModel.NoOfLikes getNoOfLikes() {
        Gson gson = new Gson();
        String json = sharedPreference.getString(NoOfLikes, "{}");
        return gson.fromJson(json, VerificationResponseModel.NoOfLikes.class);
    }

    public void saveSelfie(String selfieUrl) {
        editor.putString(this.selfie, selfieUrl);
        editor.apply();
        editor.commit();
    }

    public void saveString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
        editor.commit();
    }

    public String getMyString(String key) {
        return sharedPreference.getString(key, "");
    }

    public void setInstagramConnected(boolean isConnected) {
        editor.putBoolean(this.instagramImages, isConnected);
        editor.apply();
        editor.commit();
    }

    public void saveVerified(String verified) {
        editor.putString(this.isVerified, verified);
        editor.apply();
        editor.commit();
    }

    public void saveStatus(String status) {
        editor.putString(this.status, status);
        editor.apply();
        editor.commit();
    }



    public void saveSelfieVerificationStatus(String selfieVerificationStatus) {
        editor.putString(this.selfieVerificationStatus, selfieVerificationStatus);
        editor.apply();
        editor.commit();
    }


    public void savePremium(boolean isPremium) {
        editor.putBoolean(this.isPremium, isPremium);
        editor.apply();
        editor.commit();
    }

    public void saveDeluxe(boolean isDeluxe) {
        editor.putBoolean(this.isDeluxe, isDeluxe);
        editor.apply();
        editor.commit();
    }

    public void saveDate(String date) {
        editor.putString("checkDate", date);
        editor.apply();
        editor.commit();
    }

    public String getDate() {
        return sharedPreference.getString("checkDate", null);
    }

    public void removeKey(String key) {
        editor.remove(key);
        editor.apply();
        editor.commit();
    }

    public void saveDeviceToken(String token) {
        editor.putString(this.deviceToken, token);
        editor.apply();
        editor.commit();
    }

    public void saveFirstTime(String token) {
        editor.putString(this.firstTime, token);
        editor.apply();
        editor.commit();
    }

    public void saveUserImage(List<ImageModel> images) {
        Gson gson = new Gson();
        String json = gson.toJson(images);
        editor.putString(this.images, json);
        editor.apply();
        editor.commit();
    }

    public void saveFirstImage(String firstImage) {
        Gson gson = new Gson();
        editor.putString(this.firstImage, firstImage);
        editor.apply();
        editor.commit();
    }

    public void saveToken(String token, String userId, boolean loggedIn) {
        editor.putString(this.token, token);
        editor.putString(this.loggedIn, loggedIn + "");
        editor.putString(this.userId, userId);
        editor.apply();
        editor.commit();
    }

    public void saveIsRejected(boolean isRejected) {
        editor.putBoolean(rejected, isRejected);
        editor.apply();
    }

    public boolean getIsFromNumber() {
        return sharedPreference.getBoolean(isNumber, false);
    }

    public void setIsFromNumber(boolean bool) {
        editor.putBoolean(isNumber, bool);
        editor.apply();
    }

    public void saveBoolean(String key, boolean bool) {
        editor.putBoolean(key, bool);
        editor.apply();
    }

    public boolean getBoolean(String key) {
        return sharedPreference.getBoolean(key, false);
    }

    public boolean getIsFromEmail() {
        return sharedPreference.getBoolean(isEmail, false);
    }

    public void setIsFromEmail(boolean bool) {
        editor.putBoolean(isEmail, bool);
        editor.apply();
    }

    public void saveSwipeCount(int id) {
        editor.putInt(swipeCount, id);
        editor.apply();
        editor.commit();
    }

    public void saveLinkedIn(boolean isLinkedIN) {
        editor.putBoolean(linkedin, isLinkedIN);
        editor.apply();
    }

    public void saveisSettingsChanged(boolean isSettingsChange) {
        Log.e("TAG", "saveisSettingsChanged: " + isSettingsChanged);
        editor.putBoolean(isSettingsChanged, isSettingsChange);
        editor.apply();
    }

    public String getUser() {
        return sharedPreference.getString(user, "");
    }

    public boolean isInstaConnected() {
        return sharedPreference.getBoolean(instagramImages, false);
    }

    public String getSelfie() {
        return sharedPreference.getString(selfie, "");
    }

    public String getVerified() {
        return sharedPreference.getString(isVerified, "");
    }


    public String getStatus() {
        return sharedPreference.getString(status, "");
    }


    public String getSelfieVerificationStatus() {
        return sharedPreference.getString(selfieVerificationStatus, "");
    }

    public Boolean getPremium() {
        return sharedPreference.getBoolean(isPremium, false);
    }

    public Boolean getDeluxe() {
        return sharedPreference.getBoolean(isDeluxe, false);
    }

    public String getUserImage() {
        return sharedPreference.getString(images, "");
    }

    public String getFirstImage() {
        return sharedPreference.getString(firstImage, "");
    }

    public String getUserId() {
        return sharedPreference.getString(userId, "");
    }

    public String getToken() {
        return sharedPreference.getString(token, "");
    }

    public String getDeviceToken() {
        return sharedPreference.getString(deviceToken, "");
    }

    public String isloggedIn() {
        return sharedPreference.getString(loggedIn, "");
    }

    public String isFirstTime() {
        return sharedPreference.getString(firstTime, "");
    }

    public boolean isChatOpen() {
        return sharedPreference.getBoolean(isChatOpen, false);
    }

    public void setChatOpen(boolean isOpen) {
        editor.putBoolean(isChatOpen, isOpen);
        editor.apply();
    }

    public boolean isDialogOpen() {
        return sharedPreference.getBoolean(isDialogOpen, false);
    }

    public void setDialogOpen(boolean isOpen) {
        editor.putBoolean(isDialogOpen, isOpen);
        editor.apply();
    }

    public boolean getDislikeApi() {
        return sharedPreference.getBoolean(disLiked, false);
    }

    public void setDislikeApi(boolean isOpen) {
        editor.putBoolean(disLiked, isOpen);
        editor.apply();
    }

    public boolean isRejected() {
        return sharedPreference.getBoolean(rejected, false);
    }

    public String getChatUserId() {
        return sharedPreference.getString(chatUserId, "");
    }

    public void setChatUserId(String id) {
        editor.putString(chatUserId, id);
        editor.apply();
    }

    public int getSwipeCount() {
        return sharedPreference.getInt(swipeCount, 0);
    }

    public boolean getLinkedIn() {
        return sharedPreference.getBoolean(linkedin, false);
    }

    public boolean isSettingsChanged() {
        return sharedPreference.getBoolean(isSettingsChanged, false);
    }

    public String getProfileCompleted() {
        return sharedPreference.getString(profileCompleted, "");
    }

    public void clearData() {
        editor.clear();
        editor.commit();
    }

    public void clearByKey(String key) {
        editor.remove(key);
        editor.commit();
    }

}
