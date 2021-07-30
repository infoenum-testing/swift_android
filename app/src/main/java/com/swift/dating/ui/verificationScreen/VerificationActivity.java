package com.swift.dating.ui.verificationScreen;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.swift.dating.DummyActivity;
import com.swift.dating.R;
import com.swift.dating.data.network.ApiCall;
import com.swift.dating.data.network.ApiCallback;
import com.swift.dating.data.preference.SharedPreference;
import com.swift.dating.model.ImageModel;

import com.swift.dating.model.responsemodel.PhoneLoginResponse;
import com.swift.dating.model.responsemodel.ProfileOfUser;
import com.swift.dating.model.responsemodel.VerificationResponseModel;
import com.swift.dating.ui.base.BaseActivity;
import com.swift.dating.ui.createAccountScreen.CreateAccountActivity;
import com.swift.dating.ui.emailScreen.EmailActivity;
import com.swift.dating.ui.welcomeScreen.WelcomeActivity;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class VerificationActivity extends BaseActivity implements View.OnClickListener, ApiCallback.OtpVerifyCallBack, ApiCallback.LinkNumberCallBack, ApiCallback.PhoneLoginCallBack, OTPListener {

    Button btnVerify;
    OtpTextView otpView;
    String email, linkedinId, phone;
    TextView tv_resend, tv_mobile_number;
    ImageView ivback;
    boolean isResend;
    private SharedPreference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.white));
        email = getIntent().getExtras().getString("email");
        phone = getIntent().getExtras().getString("phone");
        linkedinId = getIntent().getExtras() != null && getIntent().hasExtra("linkedInId") ? getIntent().getExtras().getString("linkedInId") : "";
        if (getIntent().getExtras() != null && getIntent().hasExtra("isResend"))
            isResend = getIntent().getExtras().getBoolean("isResend");
        setContentView(R.layout.activity_verification);
        preference = new SharedPreference(this);
        btnVerify = findViewById(R.id.btn_verify);
        tv_mobile_number = findViewById(R.id.tv_mobile_number);
        ivback = findViewById(R.id.ivback);
        tv_resend = findViewById(R.id.tv_resend);
        otpView = findViewById(R.id.otp_view);
        tv_resend.setOnClickListener(this);
        otpView.setOtpListener(this);
        if (!preference.getIsFromNumber()) {
            tv_mobile_number.setText("Code sent to " + email + "\n Enter code below");
        } else {
            tv_mobile_number.setText("Code sent to " + phone + "\n Enter code below");
            btnVerify.setEnabled(true);
            btnVerify.setBackground(this.getResources().getDrawable(R.drawable.gradientbtn));
        }
        btnVerify.setOnClickListener(this);
        ivback.setOnClickListener(this);
        if (isResend) {
            tv_resend.performClick();
        }
    }

    /**
     * **  Method to Handle api
     */


    /**
     * Method to set Intent on Basis of Data entered
     */
    private void sendIntent() {
        String user = sp.getUser();
        ProfileOfUser obj = new Gson().fromJson(user, ProfileOfUser.class);
        String userStatus;
        userStatus = sp.getMyString(SharedPreference.userStatus);
        Log.e("TAG", "sendIntent: " + userStatus);
        String jsonImage = sp.getUserImage();
        Type type = new TypeToken<List<ImageModel>>() {
        }.getType();
        ArrayList<ImageModel> imagelist = new Gson().fromJson(jsonImage, type);

        Intent i;
        //if (obj == null || TextUtils.isEmpty(obj.getName())) {
        if (TextUtils.isEmpty(obj.getUseremail())) {
            i = new Intent(mActivity, EmailActivity.class);
            i.putExtra("fromOtp", "yes");
        } else if (TextUtils.isEmpty(obj.getName())) {
            i = new Intent(mActivity, CreateAccountActivity.class).putExtra("parseCount", 1);
        } else if (TextUtils.isEmpty(obj.getDob())) {
            i = new Intent(mActivity, CreateAccountActivity.class).putExtra("parseCount", 2);
        } else if (TextUtils.isEmpty(obj.getGender())) {
            i = new Intent(mActivity, CreateAccountActivity.class).putExtra("parseCount", 3);
        } else if (TextUtils.isEmpty(obj.getInterested())) {
            i = new Intent(mActivity, CreateAccountActivity.class).putExtra("parseCount", 4);
        } else if (imagelist == null || imagelist.isEmpty()) {
            i = new Intent(mActivity, CreateAccountActivity.class).putExtra("parseCount", 5);
        } else if (!userStatus.equalsIgnoreCase("Active")) {
            i = new Intent(mActivity, WelcomeActivity.class);
        } else {
            // i = new Intent(mActivity, HomeActivity.class);
            i = new Intent(mActivity, DummyActivity.class);
        }
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finishAffinity();

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_verify) {
            hideKeyboard();
            if (otpView.getOTP().length() == 6) {
                hideKeyboard();
                VerifyOtpCall();
            } else {
                showSnackbar(btnVerify, "Please enter OTP");
            }
        } else if (view.getId() == R.id.tv_resend) {
            hideKeyboardFromView(view);
            CallPhoneLogin();
        } else if (view.getId() == R.id.ivback) {
            onBackPressed();
        }
    }

    private void CallPhoneLogin() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("mobile", phone);
        map.put("socialType", 6);
        showLoading();
        ApiCall.phoneLogin(map, this);
    }

    private void VerifyOtpCall() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("mobile", phone);
        map.put("otp", Integer.parseInt(otpView.getOTP()));
        map.put("socialType", 6);
        map.put("deviceType", "ANDROID");
        map.put("devicetoken", sp.getDeviceToken());
        showLoading();
        ApiCall.OTPVerify(map, this);
    }

    @Override
    public void onSuccessOtpVerify(VerificationResponseModel response) {
        hideLoading();
        if (response.getSuccess()) {
            sp.saveString(SharedPreference.userStatus, response.getUser().getStatus());
            if (response.getUser().getProfileOfUser() != null) {
                sp.saveUserData(response.getUser().getProfileOfUser(), response.getUser().getProfileOfUser().getCompleted().toString());
                sp.saveToken("bearer " + response.getToken(), String.valueOf(response.getUser().getProfileOfUser().getUserId()), true);
            } else {
                sp.saveToken("bearer " + response.getToken(), String.valueOf(response.getUser().getId()), true);
            }
            sp.savePremium(response.getUser().getIsPremium().equalsIgnoreCase("Yes"));
//            sp.saveDeluxe(response.getUser().getIsDeluxe().equalsIgnoreCase("Yes"));
            sp.saveLinkedIn(!TextUtils.isEmpty(linkedinId));
            sp.saveString(SharedPreference.userEmail, response.getUser().getEmail());
            sp.saveString(SharedPreference.userPhone, response.getUser().getMobile());

            if (response.getImagedata() != null)
                sp.saveUserImage(response.getImagedata().getData());
            if (response.getUser().getSelfiesForUser() != null && response.getUser().getSelfiesForUser().getSelfieUrl() != null) {
                sp.saveSelfie(response.getUser().getSelfiesForUser().getSelfieUrl());
                sp.saveVerified(response.getUser().getIsVerified());
                //   sp.saveIsRejected(response.getUser().getisRejected().equals("1"));
            }
            preference.setIsFromEmail(true);
            preference.setIsFromNumber(false);
            if (response.getUser().getEmail() == null || response.getUser().getEmail().equalsIgnoreCase("")) {
                if (response.getUser().getProfileOfUser() != null && !TextUtils.isEmpty(response.getUser().getProfileOfUser().getName())) {
                    sendIntent();
                } else {
                    callEmailScreen();
                }
            } else {
                //Intent intent = new Intent(mActivity, HomeActivity.class);
                Intent intent = new Intent(mActivity, DummyActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finishAffinity();
            }
        } else {
            showSnackbar(btnVerify, "" + response.getMessage());
        }
    }

    private void callEmailScreen() {
        Intent intent = new Intent(mActivity, EmailActivity.class);
        intent.putExtra("fromOtp", "yes");
        startActivity(intent);
        finish();
    }

    @Override
    public void onError(String error) {
        hideLoading();
        showSnackbar(btnVerify, error.contains("false") ? "Wrong OTP Entered." : error);
    }

    @Override
    public void onSuccessLinkNumber(VerificationResponseModel response) {
        hideLoading();
        if (response.getSuccess()) {
            if (response.getUser().getProfileOfUser() != null) {
                sp.saveUserData(response.getUser().getProfileOfUser(), response.getUser().getProfileOfUser().getCompleted().toString());
                sp.saveToken("bearer " + response.getToken(), String.valueOf(response.getUser().getProfileOfUser().getUserId()), true);
            }
            sp.savePremium(response.getUser().getIsPremium().equalsIgnoreCase("Yes"));
            //          sp.saveDeluxe(response.getUser().getIsDeluxe().equalsIgnoreCase("Yes"));
            sp.saveLinkedIn(!TextUtils.isEmpty(linkedinId));

            if (response.getImagedata() != null)
                sp.saveUserImage(response.getImagedata().getData());
            if (response.getUser().getSelfiesForUser() != null &&
                    response.getUser().getSelfiesForUser().getSelfieUrl() != null) {
                sp.saveSelfie(response.getUser().getSelfiesForUser().getSelfieUrl());
                sp.saveVerified(response.getUser().getIsVerified());
                //    sp.saveIsRejected(response.getUser().getisRejected().equals("1"));
            }
            sp.saveString(SharedPreference.userEmail, response.getUser().getEmail());
            sp.saveString(SharedPreference.userPhone, response.getUser().getMobile());
            sendIntent();
        } else {
            showSnackbar(btnVerify, response.getMessage());
        }
    }

    @Override
    public void onSuccessPhoneLogin(PhoneLoginResponse response) {
        hideLoading();
        if (response.getSuccess()) {
            if (sp != null)
                sp.setPhone(phone);
        }
        showSnackbar(tv_resend, response.getMessage());
    }

    @Override
    public void onInteractionListener() {

    }

    @Override
    public void onOTPComplete(String otp) {

    }
}
