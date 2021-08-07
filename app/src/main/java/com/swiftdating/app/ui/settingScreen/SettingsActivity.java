package com.swiftdating.app.ui.settingScreen;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.gson.Gson;
import com.swiftdating.app.ui.slider_fragment;

import java.util.Objects;

import com.swiftdating.app.BuildConfig;
import com.swiftdating.app.R;
import com.swiftdating.app.callbacks.OnInAppInterface;
import com.swiftdating.app.common.CommonDialogs;
import com.swiftdating.app.common.CommonUtils;
import com.swiftdating.app.data.network.ApiCall;
import com.swiftdating.app.data.network.ApiCallback;
import com.swiftdating.app.data.network.Resource;
import com.swiftdating.app.data.preference.SharedPreference;
import com.swiftdating.app.model.BaseModel;
import com.swiftdating.app.model.requestmodel.DeluxeTokenCountModel;
import com.swiftdating.app.model.requestmodel.PremiumTokenCountModel;
import com.swiftdating.app.model.requestmodel.SettingsRequestModel;
import com.swiftdating.app.model.responsemodel.ProfileOfUser;
import com.swiftdating.app.ui.base.BaseActivity;
import com.swiftdating.app.ui.base.CommonWebViewActivity;
import com.swiftdating.app.ui.homeScreen.fragment.SearchFragment;
import com.swiftdating.app.ui.homeScreen.viewmodel.HomeViewModel;
import com.swiftdating.app.model.responsemodel.SettingsResponseModel;

import okhttp3.ResponseBody;

import static com.swiftdating.app.common.AppConstants.LICENSE_KEY;

public class SettingsActivity extends BaseActivity implements View.OnClickListener, OnInAppInterface, BillingProcessor.IBillingHandler, DialogInterface.OnClickListener, CommonDialogs.onProductConsume, ApiCallback.ResetSkippedProfileCallback, CompoundButton.OnCheckedChangeListener, slider_fragment.onReceiveClickCallback {

    private static final String TAG = "SettingsActivity";
    public static boolean isSettingChanged = false;
    ConstraintLayout btnBGPremium, rootLay;
    TextView tvRestoreSubscription, tvVersion;
    CardView cardTermnService, cardHelp, cardPrivacyPolicy;
    boolean isFromCardScreen;
    double price;
    String productId, tokenSType;
    int selectedPosition;
    boolean isRangeChange = false;
    private Button btnLogout, btnShare, btn_reset_skiped_profile;
    private TextView tv_done, tv_delete, tvPhone, tvEmail;
    ImageView tv_cancel;
    private Switch showMeSwitch, newMatchSwitch, callSwitch, expireSwitch, matchSwitch, emailNotifySwitch, pushNotifySwitch;
    private HomeViewModel homeViewModel;
    private BillingProcessor bp;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.grey));
        setContentView(R.layout.fragment_settings);
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("isCardScreen")) {
            isFromCardScreen = getIntent().getExtras().getBoolean("isCardScreen");
        }
        initialize();
        initBillingProcess();
        setSlider();
    }


    private void setSlider() {
        getSupportFragmentManager().beginTransaction().replace(R.id.sliderFragment, new slider_fragment(this)).commit();
    }


    /**
     * Method to Initialize Billing Process
     */
    private void initBillingProcess() {
        bp = new BillingProcessor(SettingsActivity.this, LICENSE_KEY, this);
        bp.initialize();
        isRestoreAllowed();
    }

    private void isRestoreAllowed() {
        Log.e("isPremium", sp.getPremium() + "");
        if (sp.getPremium()) {
            if (bp.getSubscriptionTransactionDetails("premium_1") != null ||
                    bp.getSubscriptionTransactionDetails("premium_6") != null ||
                    bp.getSubscriptionTransactionDetails("premium_12") != null) {
                String productId = "premium_12";
                if (bp.getSubscriptionTransactionDetails("premium_1") != null) {
                    productId = "premium_1";
                } else if (bp.getSubscriptionTransactionDetails("premium_6") != null) {
                    productId = "premium_6";
                }
                Log.e("state", bp.getSubscriptionTransactionDetails(productId).purchaseInfo.purchaseData.purchaseState.name().toString());
                if (!bp.getSubscriptionTransactionDetails(productId).purchaseInfo.purchaseData.autoRenewing) {
                    tvRestoreSubscription.setVisibility(View.VISIBLE);
                } else {
                    tvRestoreSubscription.setVisibility(View.GONE);
                }

            } else
                tvRestoreSubscription.setVisibility(View.GONE);
        } else {
            tvRestoreSubscription.setVisibility(View.GONE);
        }
    }

    /**
     * **  Method to Initialize
     */
    private void initialize() {
        btn_reset_skiped_profile = findViewById(R.id.btn_reset_skiped_profile);
        rootLay = findViewById(R.id.rootLay);
        emailNotifySwitch = findViewById(R.id.emailNotifySwitch);
        pushNotifySwitch = findViewById(R.id.pushNotifySwitch);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        btnLogout = findViewById(R.id.btnlogout);
        btnShare = findViewById(R.id.btnShare);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_done = findViewById(R.id.tv_done);
        tv_delete = findViewById(R.id.tvdelete);
        tvRestoreSubscription = findViewById(R.id.tvRestoreSubscription);
        showMeSwitch = findViewById(R.id.simpleSwitch);
        tvVersion = findViewById(R.id.tvVersion);
        cardHelp = findViewById(R.id.cardHelp);
        cardPrivacyPolicy = findViewById(R.id.cardPrivacyPolicy);
        cardTermnService = findViewById(R.id.cardTermnService);
        newMatchSwitch = findViewById(R.id.simpleSwitch2);

        callSwitch = findViewById(R.id.callSwitch);
        matchSwitch = findViewById(R.id.messageSwitch);
        expireSwitch = findViewById(R.id.expiredSwitch);
        btnBGPremium = findViewById(R.id.btn_bg_premium);
        try {
//            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo("app.blackgentry", 0);
//            String version = pInfo.versionName;
            tvVersion.setText("v" + BuildConfig.VERSION_NAME);
        } catch (Exception e) {
            tvVersion.setText("v1.1");
        }
        subscribeModel();
        tvRestoreSubscription.setVisibility(View.GONE);
        tv_delete.setPaintFlags(tv_delete.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        listener();

    }

    /**
     * Method to Implement Listener
     **/
    private void listener() {
        showMeSwitch.setOnCheckedChangeListener(this);
        newMatchSwitch.setOnCheckedChangeListener(this);
        emailNotifySwitch.setOnCheckedChangeListener(this);
        pushNotifySwitch.setOnCheckedChangeListener(this);
        expireSwitch.setOnCheckedChangeListener(this);
        matchSwitch.setOnCheckedChangeListener(this);
        btn_reset_skiped_profile.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        tv_delete.setOnClickListener(this);
        tv_done.setOnClickListener(this);
        btnBGPremium.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        tvRestoreSubscription.setOnClickListener(this);
        cardHelp.setOnClickListener(this);
        cardTermnService.setOnClickListener(this);
        cardPrivacyPolicy.setOnClickListener(this);
    }

    /**
     * **  Method to Handle api Response
     */
    private void subscribeModel() {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        homeViewModel.settingsResponse().observe(this, new Observer<Resource<SettingsResponseModel>>() {
            @Override
            public void onChanged(@Nullable Resource<SettingsResponseModel> resource) {
                if (resource == null) {
                    return;
                }
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        hideLoading();
                        if (resource.data.getError() != null && resource.data.getError().getCode().equalsIgnoreCase("401")) {
                            openActivityOnTokenExpire();
                        } else {

                            Gson gson = new Gson();
                            String user = sp.getUser();
                            ProfileOfUser obj = gson.fromJson(user, ProfileOfUser.class);
                            obj.setVisible(resource.data.getSettings().getVisible());
                            obj.setMatchNotify(resource.data.getSettings().getMatchNotify());
                            obj.setEmailNotify(resource.data.getSettings().getEmailNotify());
                            obj.setReactionNotify(resource.data.getSettings().getReactionNotify());
                            obj.setChatNotify(resource.data.getSettings().getChatNotify());
                            obj.setExpiredMatches(resource.data.getSettings().getExpiredMatches());
                            obj.setMaxAgePrefer(resource.data.getSettings().getMaxAgePrefer());
                            obj.setMinAgePrefer(resource.data.getSettings().getMinAgePrefer());
                            obj.setDistance(resource.data.getSettings().getDistance().doubleValue());
                            sp.saveUserData(obj, sp.getProfileCompleted());
                            sp.saveisSettingsChanged(true);
                            if (!TextUtils.isEmpty(sp.getMyString(SearchFragment.SearchResponse))) {
                                sp.removeKey(SearchFragment.SearchResponse);
                            }
                            if (!TextUtils.isEmpty(sp.getMyString(SearchFragment.FilterResponse))) {
                                sp.removeKey(SearchFragment.FilterResponse);
                            }
                            if (!TextUtils.isEmpty(sp.getMyString(SearchFragment.MyPageIndex))) {
                                sp.removeKey(SearchFragment.MyPageIndex);
                            }
                            if (!TextUtils.isEmpty(sp.getMyString(SearchFragment.RecycleLastPos))) {
                                sp.removeKey(SearchFragment.RecycleLastPos);
                            }
                            if (isFromCardScreen) {
                                Intent intent = new Intent();
                                intent.putExtra("hitApi", true);
                                setResult(RESULT_OK, intent);
                                finish();
                                overridePendingTransition(R.anim.nothing_fast, R.anim.slide_out_down_fast);
//overridePendingTransition(R.anim.nothing, R.anim.slide_out_down);
                            } else {
                                onBackPressed();
                            }
                        }
                        break;
                    case ERROR:
                        hideLoading();
                        showSnackbar(rootLay, resource.message);
                        break;
                }
            }
        });

        homeViewModel.logoutResponse().observe(this, new Observer<Resource<ResponseBody>>() {
            @Override
            public void onChanged(@Nullable Resource<ResponseBody> resource) {
                if (resource == null) {
                    return;
                }
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        hideLoading();
                        openActivityOnTokenExpire();
                        break;
                    case ERROR:
                        hideLoading();
                        openActivityOnTokenExpire();
                        showSnackbar(rootLay, resource.message);
                        break;
                }
            }
        });


        homeViewModel.addPremiumResponse().observe(this, new Observer<Resource<BaseModel>>() {
            @Override
            public void onChanged(@Nullable Resource<BaseModel> resource) {
                if (resource == null) {
                    return;
                }
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        hideLoading();
                        if (resource.data.getSuccess()) {
                            sp.savePremium(true);
                        } else if (resource.code == 401) {
                            openActivityOnTokenExpire();
                        } else {
                            showSnackbar(cardTermnService, "Something went wrong");
                        }
                        break;
                    case ERROR:
                        hideLoading();
                        showSnackbar(cardTermnService, resource.message);
                        break;
                }
            }
        });


        homeViewModel.checkSubscriptionResponse().observe(this, new Observer<Resource<BaseModel>>() {
            @Override
            public void onChanged(@Nullable Resource<BaseModel> resource) {
                if (resource == null) {
                    return;
                }
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        hideLoading();
                        if (resource.data.getSuccess()) {
                            sp.savePremium(true);
                        } else if (resource.code == 401) {
                            openActivityOnTokenExpire();
                        } else {
                            showSnackbar(cardTermnService, "Something went wrong");
                        }
                        break;
                    case ERROR:
                        hideLoading();
                        showSnackbar(cardTermnService, resource.message);
                        break;
                }
            }
        });

        homeViewModel.deleteResponse().observe(this, new Observer<Resource<ResponseBody>>() {
            @Override
            public void onChanged(@Nullable Resource<ResponseBody> resource) {
                if (resource == null) {
                    return;
                }
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        hideLoading();
                        CookieManager cookieManager = CookieManager.getInstance();
                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
                                @Override
                                public void onReceiveValue(Boolean value) {
                                    //Removed?
                                }
                            });
                            cookieManager.flush();
                        } else {
                            CookieSyncManager.createInstance(SettingsActivity.this);
                            cookieManager.removeAllCookie();
                        }
                        openActivityOnTokenExpire();
                        break;
                    case ERROR:
                        hideLoading();
                        showSnackbar(rootLay, resource.message);
                        break;
                }
            }
        });

        homeViewModel.addPremiumResponse().observe(this, new Observer<Resource<BaseModel>>() {
            @Override
            public void onChanged(@Nullable Resource<BaseModel> resource) {
                if (resource == null) return;
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        hideLoading();
                        if (resource.data.getSuccess()) {
                            sp.savePremium(true);
                        } else if (resource.code == 401) {
                            openActivityOnTokenExpire();
                        } else {
                            showSnackbar(cardTermnService, "Something went wrong");
                        }
                        break;
                    case ERROR:
                        hideLoading();
                        showSnackbar(cardTermnService, resource.message);
                        break;
                }
            }
        });

        homeViewModel.addDeluxeResponse().observe(this, new Observer<Resource<BaseModel>>() {
            @Override
            public void onChanged(Resource<BaseModel> resource) {
                if (resource == null)
                    return;
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        hideLoading();
                        if (resource.data.getSuccess()) {
                            sp.saveDeluxe(true);
                        } else if (resource.code == 401) {
                            openActivityOnTokenExpire();
                        } else {
                            showSnackbar(rootLay, "Something went wrong");
                        }
                        break;
                    case ERROR:
                        hideLoading();
                        showSnackbar(rootLay, resource.message);
                        break;
                }
            }
        });
    }

    /**
     * Method to Set Data
     */
    private void setData() {
        Gson gson = new Gson();
        String json = sp.getUser();
        Log.e(TAG, "setData: " + json);
        ProfileOfUser obj = gson.fromJson(json, ProfileOfUser.class);
        try {
            if (obj != null) {
                if (sp != null && !TextUtils.isEmpty(sp.getMyString(SharedPreference.userEmail))) {
                    tvEmail.setText(sp.getMyString(SharedPreference.userEmail));
                } else if (!TextUtils.isEmpty(obj.getUseremail())) {
                    tvEmail.setText(obj.getUseremail());
                }
                if (sp != null && !TextUtils.isEmpty(sp.getMyString(SharedPreference.userPhone))) {
                    tvPhone.setText(sp.getMyString(SharedPreference.userPhone));
                }
                if (!TextUtils.isEmpty(obj.getExpiredMatches())) {
                    expireSwitch.setChecked(obj.getExpiredMatches().equalsIgnoreCase("on"));
                }
                if (!TextUtils.isEmpty(obj.getChatNotify())) {
                    matchSwitch.setChecked(obj.getChatNotify().equalsIgnoreCase("on"));
                }
                if (!TextUtils.isEmpty(obj.getMatchNotify())) {
                    newMatchSwitch.setChecked(obj.getMatchNotify().equalsIgnoreCase("on"));
                }
                if (!TextUtils.isEmpty(obj.getEmailNotify())) {
                    emailNotifySwitch.setChecked(obj.getEmailNotify().equalsIgnoreCase("on"));
                }
                if (!TextUtils.isEmpty(obj.getReactionNotify())) {
                    pushNotifySwitch.setChecked(obj.getReactionNotify().equalsIgnoreCase("on"));
                }
                if (!TextUtils.isEmpty(obj.getVisible()))
                    showMeSwitch.setChecked(obj.getVisible().equalsIgnoreCase("true"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btnLogout) {
            final Dialog dialog = new Dialog(Objects.requireNonNull(this));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_two_button);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.show();
            TextView tv_message = dialog.findViewById(R.id.tv_message);
            TextView tv_yes = dialog.findViewById(R.id.tv_yes);
            TextView tv_no = dialog.findViewById(R.id.tv_no);
            tv_message.setText(this.getResources().getString(R.string.do_you_want_to_logout_text));

            tv_yes.setOnClickListener(view12 -> {
                showLoading();
                dialog.dismiss();
                homeViewModel.logoutRequest(sp.getToken());
                CookieManager cookieManager = CookieManager.getInstance();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
                        @Override
                        public void onReceiveValue(Boolean value) {
                            //Removed?
                        }
                    });
                    cookieManager.flush();
                } else {
                    CookieSyncManager.createInstance(SettingsActivity.this);
                    cookieManager.removeAllCookie();
                }


            });
            tv_no.setOnClickListener(view13 -> dialog.dismiss());
        } else if (view == tv_done) {
            if (isSettingChanged) {
                showLoading();
                homeViewModel.settingsRequest(new SettingsRequestModel(showMeSwitch.isChecked() ? "True" : "False",
                        newMatchSwitch.isChecked() ? "On" : "Off",
                        emailNotifySwitch.isChecked() ? "On" : "Off",
                        pushNotifySwitch.isChecked() ? "On" : "Off",
                        expireSwitch.isChecked() ? "On" : "Off",
                        matchSwitch.isChecked() ? "On" : "Off"
                ));
            } else {
                onBackPressed();
            }
        } else if (view == tvRestoreSubscription) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/account/subscriptions"));
            startActivity(browserIntent);
        } else if (view == tv_cancel) {
            onBackPressed();
        } else if (view == btn_reset_skiped_profile) {
            if (sp.getDeluxe()) {
                CommonDialogs.alertDialogConfirm(mActivity, v -> {
                    if (v.getId() == R.id.tv_yes) {
                        CommonDialogs.dismiss();
                        showLoading();
                        ApiCall.resetAllSkippedProfile(sp.getToken(), SettingsActivity.this);
                    } else {
                        CommonDialogs.dismiss();
                    }
                }, "Do you want to reset all skipped profiles? Users that you have skipped will appear on your card stack.");
            } else {
                CommonDialogs.PremuimPurChaseDialog(this, this);
            }
            //onBackPressed();
        } else if (view.getId() == R.id.tv_yes) {
            bp.subscribe(SettingsActivity.this, "premium_6");

        } else if (view == tv_delete) {
            final Dialog dialog = new Dialog(Objects.requireNonNull(this));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_two_button);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.show();
            TextView tv_message = dialog.findViewById(R.id.tv_message);
            TextView tv_yes = dialog.findViewById(R.id.tv_yes);
            TextView tv_no = dialog.findViewById(R.id.tv_no);
            tv_message.setText(this.getResources().getString(R.string.do_you_want_to_delete_text));

            tv_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    showLoading();
                    homeViewModel.deleteRequest(sp.getToken());
                }
            });
            tv_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        } else if (view.getId() == R.id.btn_bg_premium) {
            sp.setDialogOpen(true);
            if (sp.getDeluxe()) {
                CommonDialogs.showAlreadyDeluxe(mActivity);
            } else if (sp.getPremium()) {
                CommonDialogs.showAlreadyPremiumUser(this, mActivity.getResources().getString(R.string.you_have_active_subscription));
            } else {
                //    CommonDialogs.purchaseDialog(this, "BlackGentry Premium", "", this);
                CommonDialogs.PremuimPurChaseDialog(this, this);
            }
        } else if (view.getId() == R.id.btn_bg_deluxe) {
            sp.setDialogOpen(true);
            if (sp.getDeluxe()) {
                CommonDialogs.showAlreadyPremiumUser(this, mActivity.getResources().getString(R.string.you_have_active_deluxe_subscription));
            } else {
                CommonDialogs.DeluxePurChaseDialog(this, this);
                //CommonDialogs.purchaseDialog(this, "BlackGentry Premium", "", this);
            }
        } else if (view == btnShare) {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "BlackGentry");
                String shareMessage = "\nCheck out the BlackGentry App.\n\n";
                shareMessage = shareMessage + "http://onelink.to/shzeps" + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (view == cardPrivacyPolicy) {
            startActivity(new Intent(this, CommonWebViewActivity.class)
                    .putExtra("url", "https://swiftdatingapp.com/privacy/"));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (view == cardTermnService) {
            startActivity(new Intent(this, CommonWebViewActivity.class)
                    .putExtra("url", "https://swiftdatingapp.com/terms/"));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (view == cardHelp) {
            startActivity(new Intent(this, CommonWebViewActivity.class)
                    .putExtra("url", "https://swiftdatingapp.com/faq/"));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (view.getId() == R.id.view) {
            if (showMeSwitch.isChecked()) {
                Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.dialog_two_button);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.show();
                TextView tv_message = dialog.findViewById(R.id.tv_message);
                TextView tv_yes = dialog.findViewById(R.id.tv_yes);
                TextView tv_no = dialog.findViewById(R.id.tv_no);
                tv_message.setText("Are you sure you want to hide your profile?");
                tv_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMeSwitch.setChecked(false);
                        dialog.dismiss();
                    }
                });
                tv_no.setOnClickListener(view1 -> dialog.dismiss());
            } else {
                showMeSwitch.setChecked(true);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        setData();
        isRangeChange = false;
        isSettingChanged = false;
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.nothing_fast, R.anim.slide_out_down_fast);
    }

    // IBillingHandler implementation

    @Override
    public void OnItemClick(int position, int type, String id) {
        bp.subscribe(SettingsActivity.this, id);
    }

    @Override
    public void onBillingInitialized() {
        /*
         * Called when BillingProcessor was initialized and it's ready to purchase
         */

        Log.e("in-app purchase", "initialize");

    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        /*
         * Called when requested PRODUCT ID was successfully purchased
         */

        showLoading();
        Toast.makeText(mActivity, "Item Purchased", Toast.LENGTH_LONG).show();
        if (tokenSType.equalsIgnoreCase("PremiumPurchase")) {
            bp.consumePurchase(productId);
            homeViewModel.addPremiumRequest(new PremiumTokenCountModel("1", productId, price,
                    Integer.parseInt(productId.split("_")[1]), details.purchaseInfo.purchaseData.orderId,
                    details.purchaseInfo.purchaseData.purchaseToken, CommonUtils.getDateForPurchase(details), details.purchaseInfo.signature,
                    details.purchaseInfo.purchaseData.purchaseState.toString()));
        } else if (tokenSType.equalsIgnoreCase("DeluxePurChase")) {
            bp.consumePurchase(productId);
            homeViewModel.addDeluxeRequest(new DeluxeTokenCountModel("2", productId,
                    price,
                    selectedPosition,
                    details.purchaseInfo.purchaseData.orderId,
                    details.purchaseInfo.purchaseData.purchaseToken, CommonUtils.getDateForPurchase(details), details.purchaseInfo.signature,
                    details.purchaseInfo.purchaseData.purchaseState.toString()));
        }
        Log.e("DeluxePurChaseSuccess", details.purchaseInfo.responseData);
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        /*
         * Called when some error occurred. See Constants class for more details
         *
         * Note - this includes handling the case where the user canceled the buy dialog:
         * errorCode = Constants.BILLING_RESPONSE_RESULT_USER_CANCELED
         */

        Log.e("purchase failure", "Error" + " errorCode=" + errorCode);
    }

    @Override
    public void onPurchaseHistoryRestored() {
        /*
         * Called when purchase history was restored and the list of all owned PRODUCT ID's
         * was loaded from Google Play
         */

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_NEGATIVE:
                // int which = -2
                dialog.dismiss();
                showMeSwitch.setChecked(true);
                break;
            case DialogInterface.BUTTON_POSITIVE:
                // int which = -1
                dialog.dismiss();
                showMeSwitch.setChecked(false);
                break;
        }
    }

    @Override
    public void onClickToken(String tokenType, int tokensNum, int selectedPos) {
        handleCallBack(tokenType, tokensNum, selectedPos);
    }

    void handleCallBack(String tokenType, int tokensNum, int selectedPos) {
        tokenSType = tokenType;
        selectedPosition = selectedPos;
        if (tokenType.equalsIgnoreCase("PremiumPurchase")) {
            // here in this case tokenNum is month or year
            price = CommonDialogs.DeluxePriceList.get(selectedPos).getPriceValue();
            productId = CommonDialogs.DeluxeArr[selectedPos];
            bp.subscribe(mActivity, productId);
        } else if (tokenType.equalsIgnoreCase("DeluxePurChase")) {
            // here in this case tokenNum is month or year
            price = CommonDialogs.DeluxePriceList.get(selectedPos).getPriceValue();
            productId = CommonDialogs.DeluxeArr[selectedPos];
            bp.subscribe(mActivity, productId);
        }
    }


    @Override
    public void onSuccess(String body) {
        if (isFromCardScreen) {
            Intent intent = new Intent();
            intent.putExtra("hitApi", true);
            setResult(RESULT_OK, intent);
        }
        hideLoading();
        showSnackbar(btn_reset_skiped_profile, body);
    }

    @Override
    public void onError(String error) {
        hideLoading();
        showSnackbar(btn_reset_skiped_profile, error);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.e(TAG, "onCheckedChanged: ");
        isSettingChanged = true;
    }

    @Override
    public void onPremiumCallback(String tokenType, int tokensNum, int selectedPos) {
        handleCallBack(tokenType, tokensNum, selectedPos);
    }

    /*bp.purchase(YOUR_ACTIVITY, "YOUR PRODUCT ID FROM GOOGLE PLAY CONSOLE HERE");
    bp.subscribe(YOUR_ACTIVITY, "YOUR SUBSCRIPTION ID FROM GOOGLE PLAY CONSOLE HERE");*/
}
