package com.swift.dating.ui.loginScreen;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.login.LoginResult;
import com.swift.dating.DummyActivity;
import com.swift.dating.R;
import com.swift.dating.data.network.Resource;
import com.swift.dating.model.requestmodel.SignUpRequestModel;
import com.swift.dating.model.responsemodel.VerificationResponseModel;
import com.swift.dating.ui.base.BaseActivity;
import com.swift.dating.ui.base.CommonWebViewActivity;
import com.swift.dating.ui.createAccountScreen.CreateAccountActivity;
import com.swift.dating.ui.emailScreen.PhoneActivity;
import com.swift.dating.ui.emailScreen.viewmodel.EnterEmailViewModel;
import com.swift.dating.ui.welcomeScreen.WelcomeActivity;

import java.util.Arrays;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    TextView tvTermOfService, tvPrivacyPolicy, tvRule1;
    //TextView tvCreateAccount;
    Button create_account_with_num;
    ConstraintLayout cl_main, login_button_fb;
    CallbackManager callbackManager;
    String providerId, name, email, profilePic;
    EnterEmailViewModel model;
    String linkedInId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //   this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        subscribeModel();
        tvTermOfService = findViewById(R.id.tvTermOfService);
        tvPrivacyPolicy = findViewById(R.id.tvPrivacyPolicy);
        create_account_with_num = findViewById(R.id.create_account_with_num);
        //tvCreateAccount = findViewById(R.id.create_account);
        tvRule1 = findViewById(R.id.tvRule1);
        tvTermOfService.setPaintFlags(tvTermOfService.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvPrivacyPolicy.setPaintFlags(tvPrivacyPolicy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //tvCreateAccount.setPaintFlags(tvCreateAccount.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        cl_main = findViewById(R.id.cl_main);
        callbackManager = CallbackManager.Factory.create();
        //tvCreateAccount.setOnClickListener(this);
        login_button_fb = findViewById(R.id.login_button_fb);
        login_button_fb.setOnClickListener(this);
        create_account_with_num.setOnClickListener(this);
        tvTermOfService.setOnClickListener(this);
        tvPrivacyPolicy.setOnClickListener(this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getExtras() != null && intent.getExtras().containsKey("deactivated")) {
            openActivityOnTokenExpire();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == RESULT_OK) {
                showLoading();
                linkedInId = data.getExtras().getString("id");
                email = data.getExtras().getString("email");
                name = data.getExtras().getString("name");
                hitApi(true);
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.login_button_fb) {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            Log.e("test", "test2");
                            RequestData();
                        }

                        @Override
                        public void onCancel() {
                            // App code
                            Log.e("test", "test3");

                        }

                        @Override
                        public void onError(FacebookException exception) {
                            exception.printStackTrace();
                            // App code
                        }
                    });
        } else if (view == create_account_with_num) {
            Intent intent = new Intent(this, PhoneActivity.class);
            // sp.setIsFromNumber(true);
            //  intent.putExtra("Title", "My PhoneNumberAc is");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            if (view == tvTermOfService) {
                startActivity(new Intent(this, CommonWebViewActivity.class)
                        .putExtra("url", "https://swiftdatingapp.com/terms/"));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (view == tvPrivacyPolicy) {
                startActivity(new Intent(this, CommonWebViewActivity.class)
                        .putExtra("url", "https://swiftdatingapp.com/privacy/"));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }
    }

    /**
     * **  Method to Request Data from Facebook
     */
    private void RequestData() {
        Log.e("test", "test1");
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                (object, response) -> {
                    JSONObject json = response.getJSONObject();
                    try {
                        if (json != null) {
                            if (json.has("id"))
                                providerId = json.getString("id");
                            if (json.has("name"))
                                name = json.getString("name");
                            if (json.has("email"))
                                email = json.getString("email");

                            profilePic = "https://graph.facebook.com/" + LoginActivity.this.getResources().getString(R.string.facebook_app_id) + "/picture?type=normal";
                            LoginManager.getInstance().logOut();

                            hideLoading();
                            hitApi(false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, email, picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    /**
     * **  Method to hit login Api
     */
    private void hitApi(boolean isLinkedIn) {
        showLoading();
        if (!isLinkedIn)
            model.signInRequest(new SignUpRequestModel(email, "2", providerId, sp.getDeviceToken()));
        else {
            model.signInRequest(new SignUpRequestModel(email, "4", "0", sp.getDeviceToken(), linkedInId, name));
        }
    }

    /**
     * **  Method to Handle api Response
     */
    private void subscribeModel() {
        model = ViewModelProviders.of(this).get(EnterEmailViewModel.class);
        model.signInResponse().observe(this, new Observer<Resource<VerificationResponseModel>>() {
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
                            sp.savePremium(resource.data.getUser().getIsPremium().equalsIgnoreCase("Yes"));
                            setData(resource.data);
                        } else {
                            showSnackbar(cl_main, resource.data.getMessage());
                        }
                        break;
                    case ERROR:
                        hideLoading();
                        showSnackbar(cl_main, resource.message);
                        break;
                }
            }
        });
    }

    /**
     * **  Method to save data in preferences
     */
    private void setData(VerificationResponseModel data) {
        String userStatus = "";
        if (data != null && data.getUser() != null && !TextUtils.isEmpty(data.getUser().getStatus())) {
            userStatus = data.getUser().getStatus();
        }
        sp.saveUserData(data.getUser().getProfileOfUser(), data.getUser().getProfileOfUser().getCompleted().toString());
        sp.saveLinkedIn(!TextUtils.isEmpty(linkedInId));
        sp.saveToken("bearer " + data.getToken(), String.valueOf(data.getUser().getProfileOfUser().getUserId()), true);
        if (data.getImagedata() != null)
            sp.saveUserImage(data.getImagedata().getData());
        if (data.getUser().getSelfiesForUser() != null &&
                data.getUser().getSelfiesForUser().getSelfieUrl() != null) {
            sp.saveSelfie(data.getUser().getSelfiesForUser().getSelfieUrl());
            sp.saveVerified(data.getUser().getIsVerified());
        }
        sp.saveVerified(data.getUser().getIsVerified());
        //     sp.saveIsRejected(data.getUser().getisRejected().equals("1"));
        Intent i;
        if (TextUtils.isEmpty(data.getUser().getProfileOfUser().getName())) {
            i = new Intent(mActivity, CreateAccountActivity.class).putExtra("parseCount", 1);
        } else if (TextUtils.isEmpty(data.getUser().getProfileOfUser().getDob())) {
            i = new Intent(mActivity, CreateAccountActivity.class).putExtra("parseCount", 2);
        } else if (TextUtils.isEmpty(data.getUser().getProfileOfUser().getGender())) {
            i = new Intent(mActivity, CreateAccountActivity.class).putExtra("parseCount", 3);
        } else if (TextUtils.isEmpty(data.getUser().getProfileOfUser().getInterested())) {
            i = new Intent(mActivity, CreateAccountActivity.class).putExtra("parseCount", 4);
        } else if (data.getImagedata().getData() == null || data.getImagedata().getData().size() == 0) {
            i = new Intent(mActivity, CreateAccountActivity.class).putExtra("parseCount", 5);
        } else if (!userStatus.equalsIgnoreCase("Active")) {
            i = new Intent(mActivity, WelcomeActivity.class);
        } else {
            // i = new Intent(mActivity, HomeActivity.class);
            i = new Intent(mActivity, DummyActivity.class);
        }
        startActivity(i);
        finishAffinity();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
