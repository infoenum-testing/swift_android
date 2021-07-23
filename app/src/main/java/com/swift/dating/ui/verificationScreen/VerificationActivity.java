package com.swift.dating.ui.verificationScreen;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.swift.dating.R;
import com.swift.dating.data.network.ApiCall;
import com.swift.dating.data.network.ApiCallback;
import com.swift.dating.data.network.Resource;
import com.swift.dating.data.preference.SharedPreference;
import com.swift.dating.model.BaseModel;
import com.swift.dating.model.ImageModel;
import com.swift.dating.model.requestmodel.ResendRequest;
import com.swift.dating.model.requestmodel.VerficationRequestModel;
import com.swift.dating.model.responsemodel.PhoneLoginResponse;
import com.swift.dating.model.responsemodel.ProfileOfUser;
import com.swift.dating.model.responsemodel.VerificationResponseModel;
import com.swift.dating.ui.base.BaseActivity;
import com.swift.dating.ui.createAccountScreen.CreateAccountActivity;
import com.swift.dating.ui.emailScreen.EmailActivity;
import com.swift.dating.ui.emailScreen.viewmodel.EnterEmailViewModel;
import com.swift.dating.ui.homeScreen.HomeActivity;
import com.swift.dating.ui.selfieScreen.SelfieActivity;
import com.swift.dating.ui.verificationScreen.viewmodel.VerificationViewModel;
import com.swift.dating.ui.welcomeScreen.WelcomeActivity;

public class VerificationActivity extends BaseActivity implements View.OnClickListener, OnOtpCompletionListener, ApiCallback.OtpVerifyCallBack, ApiCallback.LinkNumberCallBack, ApiCallback.PhoneLoginCallBack {

    Button btnVerify;
    OtpView otpView;
    String email, linkedinId, phone;
    TextView tv_resend, tv_mobile_number;
    ImageView ivback;
    boolean isResend;
    VerificationViewModel model;
    EnterEmailViewModel enterEmailViewModel;
    private SharedPreference preference;
    private String forLinkNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.primaryTextColor));
        email = getIntent().getExtras().getString("email");
        phone = getIntent().getExtras().getString("phone");
        linkedinId = getIntent().getExtras() != null && getIntent().hasExtra("linkedInId") ? getIntent().getExtras().getString("linkedInId") : "";
        if (getIntent().getExtras() != null && getIntent().hasExtra("isResend"))
            isResend = getIntent().getExtras().getBoolean("isResend");
        if (getIntent().getExtras() != null && getIntent().hasExtra("forLinkNumber"))
            forLinkNumber = getIntent().getExtras().getString("forLinkNumber");
        setContentView(R.layout.activity_verification);
        preference = new SharedPreference(this);
        btnVerify = findViewById(R.id.btn_verify);
        tv_mobile_number = findViewById(R.id.tv_mobile_number);
        ivback = findViewById(R.id.ivback);
        tv_resend = findViewById(R.id.tv_resend);
        otpView = findViewById(R.id.otp_view);
        tv_resend.setOnClickListener(this);
        otpView.setOtpCompletionListener(this);
        if (!preference.getIsFromNumber()) {
            tv_mobile_number.setText("Code sent to " + email + "\n Enter code below");
        } else {
            tv_mobile_number.setText("Code sent to " + phone + "\n Enter code below");
            btnVerify.setEnabled(true);
            btnVerify.setBackground(this.getResources().getDrawable(R.drawable.gradientbtn));
        }
        btnVerify.setOnClickListener(this);
        ivback.setOnClickListener(this);
        subscribeModel();
        if (isResend) {
            tv_resend.performClick();
        }
    }

    /**
     * **  Method to Handle api
     */
    private void subscribeModel() {
        model = ViewModelProviders.of(this).get(VerificationViewModel.class);
        enterEmailViewModel = ViewModelProviders.of(this).get(EnterEmailViewModel.class);
        //Verify OTP
        model.verifyResponse().observe(this, new Observer<Resource<VerificationResponseModel>>() {
            @Override
            public void onChanged(@Nullable Resource<VerificationResponseModel> resource) {
                if (resource == null) {
                    hideLoading();
                    return;
                }
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        hideLoading();
                        if (resource.data.getSuccess()) {
                            sp.saveUserData(resource.data.getUser().getProfileOfUser(), resource.data.getUser().getProfileOfUser().getCompleted().toString());
                            if (resource.data.getNoOfLikes() != null)
                                sp.saveNoOfLikes(resource.data.getNoOfLikes());
                            sp.savePremium(resource.data.getUser().getIsPremium().equalsIgnoreCase("Yes"));
                            sp.saveDeluxe(resource.data.getUser().getIsDeluxe().equalsIgnoreCase("Yes"));
                            sp.saveLinkedIn(!TextUtils.isEmpty(linkedinId));
                            sp.saveString(SharedPreference.userEmail, resource.data.getUser().getEmail());
                            sp.saveString(SharedPreference.userPhone, resource.data.getUser().getMobile());
                            sp.saveToken("bearer " + resource.data.getToken(), String.valueOf(resource.data.getUser().getProfileOfUser().getUserId()),
                                    true);
                            if (resource.data.getImagedata() != null)
                                sp.saveUserImage(resource.data.getImagedata().getData());
                            if (resource.data.getUser().getSelfiesForUser() != null &&
                                    resource.data.getUser().getSelfiesForUser().getSelfieUrl() != null) {
                                sp.saveSelfie(resource.data.getUser().getSelfiesForUser().getSelfieUrl());
                                sp.saveVerified(resource.data.getUser().getIsVerified());
                                sp.saveIsRejected(resource.data.getUser().getisRejected().equals("1"));
                            }
                            sendIntent();
                        } else {
                            showSnackbar(btnVerify, resource.data.getMessage());
                        }
                        break;
                    case ERROR:
                        hideLoading();
                        showSnackbar(btnVerify, resource.message);
                        break;
                }
            }
        });
        //Resend OTP
        enterEmailViewModel.resendResponse().observe(this, new Observer<Resource<BaseModel>>() {
            @Override
            public void onChanged(@Nullable Resource<BaseModel> resource) {
                if (resource == null) {
                    hideLoading();
                    return;
                }
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        hideLoading();
                        if (resource.data.getSuccess()) {
                            showSnackbar(btnVerify, resource.data.getMessage());
                        } else {
                            showSnackbar(btnVerify, resource.data.getMessage());
                        }
                        break;
                    case ERROR:
                        hideLoading();
                        showSnackbar(btnVerify, resource.message);
                        break;
                }
            }
        });
    }


    /**
     * Method to set Intent on Basis of Data entered
     */
    private void sendIntent() {
        String user = sp.getUser();
        ProfileOfUser obj = new Gson().fromJson(user, ProfileOfUser.class);

        String jsonImage = sp.getUserImage();
        Type type = new TypeToken<List<ImageModel>>() {
        }.getType();
        ArrayList<ImageModel> imagelist = new Gson().fromJson(jsonImage, type);

        Intent i;
        if (obj==null||TextUtils.isEmpty(obj.getName())) {
            i = new Intent(mActivity, WelcomeActivity.class);
        } else if (TextUtils.isEmpty(obj.getDob())) {
            i = new Intent(mActivity, CreateAccountActivity.class).putExtra("parseCount", 2);
        } else if (TextUtils.isEmpty(obj.getGender())) {
            i = new Intent(mActivity, CreateAccountActivity.class).putExtra("parseCount", 3);
        } else if (TextUtils.isEmpty(obj.getInterested())) {
            i = new Intent(mActivity, CreateAccountActivity.class).putExtra("parseCount", 4);
        } else if (imagelist == null || imagelist.isEmpty()) {
            i = new Intent(mActivity, CreateAccountActivity.class).putExtra("parseCount", 5);
        } else if (TextUtils.isEmpty(sp.getSelfie())) {
            i = new Intent(mActivity, SelfieActivity.class);
        } else {
            i = new Intent(mActivity, HomeActivity.class);
        }
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finishAffinity();

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_verify) {
            if (preference.getIsFromNumber()) {
                hideKeyboard();
                if (otpView.getText().toString().length() == 6) {
                    hideKeyboard();
                    VerifyOtpCall();
                } else {
                    showSnackbar(btnVerify, "Please enter OTP");
                }

                //finishAffinity();
            } else {
                hideKeyboard();
                if (otpView.getText().toString().length() == 6) {
                    hideKeyboard();
                    if (forLinkNumber != null && forLinkNumber.equalsIgnoreCase("yes")) {
                        LinkNumberCall();
                    } else {
                        showLoading();
                        model.verifyRequest(new VerficationRequestModel(email, Integer.parseInt(otpView.getText().toString()), sp.getDeviceToken()));
                    }
                } else {
                    showSnackbar(btnVerify, "Please enter OTP");
                }
            }
        } else if (view.getId() == R.id.tv_resend) {
            hideKeyboardFromView(view);
            if (preference.getIsFromNumber()) {
                CallPhoneLogin();
            } else {
                showLoading();
                enterEmailViewModel.resendRequest(new ResendRequest(email));
            }
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

    @Override
    public void onOtpCompleted(String otp) {
        btnVerify.setEnabled(true);
        btnVerify.setBackground(this.getResources().getDrawable(R.drawable.gradientbtn));
    }

    private void VerifyOtpCall() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("mobile", phone);
        map.put("otp", Integer.parseInt(otpView.getText().toString()));
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
            if (response.getUser().getProfileOfUser() != null) {
                sp.saveUserData(response.getUser().getProfileOfUser(), response.getUser().getProfileOfUser().getCompleted().toString());
                sp.saveToken("bearer " + response.getToken(), String.valueOf(response.getUser().getProfileOfUser().getUserId()), true);
            } else {
                sp.saveToken("bearer " + response.getToken(), String.valueOf(response.getUser().getId()), true);
            }
            sp.savePremium(response.getUser().getIsPremium().equalsIgnoreCase("Yes"));
            sp.saveDeluxe(response.getUser().getIsDeluxe().equalsIgnoreCase("Yes"));
            sp.saveLinkedIn(!TextUtils.isEmpty(linkedinId));
            sp.saveString(SharedPreference.userEmail, response.getUser().getEmail());
            sp.saveString(SharedPreference.userPhone, response.getUser().getMobile());

            if (response.getImagedata() != null)
                sp.saveUserImage(response.getImagedata().getData());
            if (response.getUser().getSelfiesForUser() != null && response.getUser().getSelfiesForUser().getSelfieUrl() != null) {
                sp.saveSelfie(response.getUser().getSelfiesForUser().getSelfieUrl());
                sp.saveVerified(response.getUser().getIsVerified());
                sp.saveIsRejected(response.getUser().getisRejected().equals("1"));
            }
            preference.setIsFromEmail(true);
            preference.setIsFromNumber(false);
            if (response.getUser().getEmail() == null || response.getUser().getEmail().equalsIgnoreCase("")) {
                if (response.getUser().getProfileOfUser() != null && !TextUtils.isEmpty(response.getUser().getProfileOfUser().getName())) {
                    sendIntent();
                   /* Intent intent = new Intent(mActivity, EmailActivity.class);
                    intent.putExtra("fromOtp", "yes");
                    startActivity(intent);
                    finish();*/
                } else {
                    Intent intent = new Intent(mActivity, EmailActivity.class);
                    intent.putExtra("fromOtp", "yes");
                    startActivity(intent);
                    finish();
                }
            } else {
                Intent intent = new Intent(mActivity, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finishAffinity();
            }
        } else {
            showSnackbar(btnVerify,  "" + response.getMessage() );
        }
    }

    @Override
    public void onError(String error) {
        hideLoading();
        showSnackbar(btnVerify, error.contains("false") ? "Wrong OTP Entered." : error);
    }

    private void LinkNumberCall() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("mobile", sp.getPhone());
        map.put("email", email);
        map.put("otp", Integer.parseInt(otpView.getText().toString()));
        map.put("deviceType", "ANDROID");
        map.put("devicetoken", sp.getDeviceToken());
        showLoading();
        ApiCall.LinkNumber(map, this);
    }

    @Override
    public void onSuccessLinkNumber(VerificationResponseModel response) {
        hideLoading();
        if (response.getSuccess()) {
            if (response.getUser().getProfileOfUser()!=null) {
                sp.saveUserData(response.getUser().getProfileOfUser(), response.getUser().getProfileOfUser().getCompleted().toString());
                sp.saveToken("bearer " + response.getToken(), String.valueOf(response.getUser().getProfileOfUser().getUserId()), true);
            }
            sp.savePremium(response.getUser().getIsPremium().equalsIgnoreCase("Yes"));
            sp.saveDeluxe(response.getUser().getIsDeluxe().equalsIgnoreCase("Yes"));
            sp.saveLinkedIn(!TextUtils.isEmpty(linkedinId));

            if (response.getImagedata() != null)
                sp.saveUserImage(response.getImagedata().getData());
            if (response.getUser().getSelfiesForUser() != null &&
                    response.getUser().getSelfiesForUser().getSelfieUrl() != null) {
                sp.saveSelfie(response.getUser().getSelfiesForUser().getSelfieUrl());
                sp.saveVerified(response.getUser().getIsVerified());
                sp.saveIsRejected(response.getUser().getisRejected().equals("1"));
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
}
