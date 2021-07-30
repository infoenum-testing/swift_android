package com.swift.dating.ui.splashScreen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import com.swift.dating.DummyActivity;
import com.swift.dating.R;
import com.swift.dating.common.ScreenUtils;
import com.swift.dating.data.preference.SharedPreference;
import com.swift.dating.model.ImageModel;
import com.swift.dating.model.responsemodel.ProfileOfUser;
import com.swift.dating.model.responsemodel.VerificationResponseModel;
import com.swift.dating.ui.base.BaseActivity;
import com.swift.dating.ui.createAccountScreen.CreateAccountActivity;
import com.swift.dating.ui.emailScreen.EmailActivity;
import com.swift.dating.ui.homeScreen.HomeActivity;
import com.swift.dating.ui.loginScreen.LoginActivity;
import com.swift.dating.ui.selfieScreen.SelfieActivity;
import com.swift.dating.ui.welcomeScreen.WelcomeActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        int i = ScreenUtils.getScreenHeight(mActivity) - (ScreenUtils.getActionBarHeight(mActivity));
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(mActivity);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.IMMEDIATE,
                            this,
                            1000);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }

            } else {
                getDeviceToken();

            }
        });
        appUpdateInfoTask.addOnFailureListener(appUpdateInfo -> {
            getDeviceToken();
        });
        sp.setDialogOpen(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode != RESULT_OK) {
                showToast("Update flow failed! Result code: " + resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.
            } else {
                getDeviceToken();
            }
        }
    }
/*  @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000) {
            if (resultCode != RESULT_OK) {
                showToast("Update flow failed! Result code: " + resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.
            } else {
                getDeviceToken();
            }
        }
    }*/

    /**
     * Method to get Device Token
     */
    private void getDeviceToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("Token", "getInstanceId failed", task.getException());
                        return;
                    }

                    // Get new Instance ID token
                    sp.saveDeviceToken(task.getResult().getToken());
                    intentViaHandler();
                });
    }

    /**
     * ** Method to handle handler for the Intent
     */
    private void intentViaHandler() {
        new Handler().postDelayed(() -> {
            Intent i;
            if (sp.isloggedIn().equalsIgnoreCase("true")) {
                Gson gson = new Gson();
                String json = sp.getUser();
                String userStatus;
                userStatus = sp.getMyString(SharedPreference.userStatus);
                ProfileOfUser obj = gson.fromJson(json, ProfileOfUser.class);
                String jsonImage = sp.getUserImage();
                Type type = new TypeToken<List<ImageModel>>() {
                }.getType();
                List<ImageModel> imagelist = gson.fromJson(jsonImage, type);

                if (obj != null) {
                    if (TextUtils.isEmpty(obj.getUseremail())) {
                        i = new Intent(mActivity, EmailActivity.class);
                        i.putExtra("fromOtp", "yes");
                    } else if (TextUtils.isEmpty(obj.getName())) {
                        i = new Intent(mActivity, CreateAccountActivity.class).putExtra("parseCount", 1);
                    } else if (TextUtils.isEmpty(obj.getDob())) {
                        i = new Intent(mActivity, CreateAccountActivity.class).putExtra("parseCount", 2);
                    }else if (TextUtils.isEmpty(obj.getGender())) {
                        i = new Intent(mActivity, CreateAccountActivity.class).putExtra("parseCount", 3);
                    } else if (TextUtils.isEmpty(obj.getInterested())) {
                        i = new Intent(mActivity, CreateAccountActivity.class).putExtra("parseCount", 4);
                    } else if (imagelist == null || imagelist.size() == 0) {
                        i = new Intent(mActivity, CreateAccountActivity.class).putExtra("parseCount", 5);
                    } else if (!userStatus.equalsIgnoreCase("Active")) {
                        i = new Intent(mActivity, WelcomeActivity.class);
                    } else {
                        // i = new Intent(mActivity, HomeActivity.class);
                        i = new Intent(mActivity, DummyActivity.class);
                    }
                } else {
                    i = new Intent(mActivity, LoginActivity.class);
                }
            } else {
                i = new Intent(mActivity, LoginActivity.class);
            }
            startActivity(i);
            //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }, 2000);

    }
}
