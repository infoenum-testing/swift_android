package com.swift.dating.ui.emailScreen;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Objects;

import com.swift.dating.R;
import com.swift.dating.common.CommonDialogs;
import com.swift.dating.common.ValidationUtils;
import com.swift.dating.data.network.ApiCall;
import com.swift.dating.data.network.ApiCallback;
import com.swift.dating.data.network.Resource;
import com.swift.dating.data.preference.SharedPreference;
import com.swift.dating.model.requestmodel.SignUpRequestModel;
import com.swift.dating.model.responsemodel.PhoneLoginResponse;
import com.swift.dating.model.responsemodel.VerificationResponseModel;
import com.swift.dating.ui.base.BaseActivity;
import com.swift.dating.ui.emailScreen.viewmodel.EnterEmailViewModel;
import com.swift.dating.ui.verificationScreen.VerificationActivity;
import com.swift.dating.ui.welcomeScreen.WelcomeActivity;

public class EmailActivity extends BaseActivity implements View.OnClickListener, TextWatcher, ApiCallback.EmailExistCallBack, ApiCallback.LinkEmailCallBack {
    Button btnContinue;
    EditText etEmail;
    EnterEmailViewModel model;
    String linkedInId = "";
    private ImageView img_cross;
    private Spinner spin;
    private TextView tv_title;
    private String fromOtp;

    // private final String[] countrycode = new String[]{"+93", "+355", "+213", "+1 684", "+376", "+244", "+1 264", "+672", "+1 268", "+54", "+374", "+297", "+61", "+43", "+994", "+1 242", "+973", "+880", "+1 246", "+375", "+32", "+501", "+229", "+1 441", "+975", "+591", "+387", "+267", "+55", "+246", "+1 284", "+673", "+359", "+226", "+95", "+257", "+855", "+237", "+1", "+238", "+1 345", "+236", "+235", "+56", "+86", "+61", "+891", "+57", "+269", "+682", "+506", "+385", "+53", "+357", "+420", "+243", "+45", "+253", "+1 767", "+1 849", "+1 829", "+1 809", "+593", "+20", "+503", "+240", "+291", "+372", "+251", "+500", "+298", "+679", "+358", "+33", "+689", "+241", "+220", "+970", "+995", "+49", "+233", "+350", "+30", "+299", "+1 473", "+1 671", "+502", "+224", "+245", "+592", "+509", "+379", "+504", "+852", "+36", "+354", "+91", "+62", "+98", "+964", "+353", "+44", "+972", "+39", "+225", "+1 876", "+81", "+44", "+962", "+7", "+254", "+686", "+381", "+965", "+996", "+856", "+371", "+961", "+266", "+231", "+218", "+423", "+370", "+352", "+853", "+389", "+261", "+265", "+60", "+960", "+223", "+356", "+692", "+222", "+230", "+262", "+52", "+691", "+373", "+377", "+976", "+382", "+1 664", "+212", "+258", "+264", "+674", "+977", "+31", "+599", "+687", "+64", "+505", "+227", "+234", "+683", "+672", "+850", "+1 670", "+47", "+968", "+92", "+680", "+507", "+675", "+595", "+51", "+63", "+870", "+48", "+351", "+1", "+974", "+242", "+40", "+7", "+250", "+590", "+290", "+1 869", "+1 758", "+1 599", "+508", "+1 784", "+685", "+378", "+239", "+966", "+221", "+381", "+248", "+232", "+65", "+421", "+386", "+677", "+252", "+27", "+82", "+34", "+94", "+249", "+597", "+268", "+46", "+41", "+963", "+886", "+992", "+255", "+66", "+670", "+228", "+690", "+676", "+1 868", "+216", "+90", "+993", "+1 649", "+688", "+256", "+380", "+971", "+44", "+1", "+598", "+1 340", "+998", "+678", "+58", "+84", "+681", "+970", "+967", "+260", "+263"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.primaryTextColor));
        setContentView(R.layout.activity_email);
        linkedInId = getIntent().getExtras() != null && getIntent().getExtras().containsKey("linkedInId") ? getIntent().getExtras().getString("linkedInId") : "";
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("fromOtp"))
            fromOtp = getIntent().getExtras().getString("fromOtp");
        sp = new SharedPreference(this);
        init();
        subscribeModel();
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
                            CommonDialogs.dismiss();

                            startActivity(new Intent(EmailActivity.this, VerificationActivity.class)
                                    .putExtra("email", etEmail.getText().toString())
                                    .putExtra("linkedInId", linkedInId));
                        } else {
                            if (resource.data.getMessage().equalsIgnoreCase("You are already Registered ...Please Login !")) {
                                CommonDialogs.dismiss();
                                startActivity(new Intent(EmailActivity.this, VerificationActivity.class)
                                        .putExtra("email", etEmail.getText().toString())
                                        .putExtra("isResend", true)
                                        .putExtra("linkedInId", linkedInId));
                            }
                            showSnackbar(btnContinue, resource.data.getMessage());
                        }
                        break;
                    case ERROR:
                        hideLoading();
                        showSnackbar(btnContinue, resource.message);
                        break;
                }
            }
        });
    }

    /**
     * **  Method to Initialize
     */
    private void init() {
        img_cross = findViewById(R.id.img_cross);
        img_cross.setOnClickListener(this);
        tv_title = findViewById(R.id.tv_mobile_number);
        btnContinue = findViewById(R.id.btn_continue);
        etEmail = findViewById(R.id.et_email);
        spin = findViewById(R.id.spin);
        etEmail.setVisibility(View.VISIBLE);
        spin.setVisibility(View.GONE);
        tv_title.setText(getString(R.string.my_email));
        etEmail.setHint("Enter your email");
        btnContinue.setOnClickListener(this);
        etEmail.addTextChangedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mActivity = this;
    }

    @Override
    public void onClick(View view) {
        if (view == btnContinue) {
            if (sp.getIsFromEmail()) {
                hideKeyboard();
                if (!TextUtils.isEmpty(etEmail.getText().toString().trim())) {
                    if (ValidationUtils.isEmailValid(etEmail.getText().toString())) {
                        //CallForCheckEmailExistOrNot();
                        CommonDialogs.alertDialogTwoButtonsWithConfirm(mActivity, this, "Please confirm that " + etEmail.getText().toString() + " is your correct email address");
                    } else {
                        etEmail.requestFocus();
                        showSnackbar(view, "Please enter valid email");
                    }
                } else {
                    etEmail.requestFocus();
                    showSnackbar(view, "Please enter email");
                }
                return;
            }
            if (sp.getIsFromNumber()) {
                CommonDialogs.alertDialogConfirm(mContext, this, mContext.getResources().getString(R.string.link_email_txt));
            } else {
                hideKeyboard();
                if (!TextUtils.isEmpty(etEmail.getText().toString().trim())) {
                    if (ValidationUtils.isEmailValid(etEmail.getText().toString()))
                        CommonDialogs.alertDialogTwoButtonsWithConfirm(mActivity, this, "Please confirm that " + etEmail.getText().toString() + " is your correct email address");
                    else showSnackbar(etEmail, "Please enter valid email");
                } else showSnackbar(etEmail, "Please enter email");
            }
        } else if (view.getId() == R.id.tv_yes) {
            CommonDialogs.dismiss();
            if (sp.getIsFromEmail()) {
                CallForCheckEmailExistOrNot();
            } else if (sp.getIsFromNumber()) {
                sp.setIsFromEmail(true);
                startActivity(new Intent(this, VerificationActivity.class).putExtra("email", etEmail.getText().toString()));
                /*sp.setIsFromEmail(false);
                sp.setIsFromNumber(false);
                startActivity(new Intent(EmailActivity.this, Login.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finishAffinity();*/
            } else {
                showLoading();
                model.signInRequest(new SignUpRequestModel(etEmail.getText().toString(), "0", "0", sp.getDeviceToken()));
            }
        } else if (view.getId() == R.id.img_cross) {
            onBackPressed();
        } else if (view.getId() == R.id.tv_no) {
            CommonDialogs.dismiss();
            startActivity(new Intent(EmailActivity.this, WelcomeActivity.class));
            finishAffinity();
            // onBackPressed();
        }

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (!TextUtils.isEmpty(charSequence.toString())) {
            btnContinue.setEnabled(true);
            btnContinue.setBackground(this.getResources().getDrawable(R.drawable.gradientbtn));
        } else {
            btnContinue.setEnabled(false);
            btnContinue.setBackground(this.getResources().getDrawable(R.drawable.disabledbtn));
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    private void CallForCheckEmailExistOrNot() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("email", etEmail.getText().toString().trim());
        map.put("socialType", 7);
        showLoading();
        ApiCall.CheckEmailExistOrNot(map, this);
    }

    @Override
    public void onSuccessEmailExist(PhoneLoginResponse response) {
        hideLoading();
        if (response.getSuccess()&&response.getEmailType() != null && response.getEmailType() == 1) {
            Dialog dialog = new Dialog(EmailActivity.this);
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
            tv_no.setText("No");
            tv_message.setText(mContext.getResources().getString(R.string.link_email_txt));
            tv_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    CallForLinkAccount();
                }
            });

            tv_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonDialogs.dismiss();
                    dialog.dismiss();
                    //showAlertOfExiting("");
                    /*sp.setIsFromNumber(false);
                    sp.setIsFromEmail(true);
                    sp.saveBoolean(SharedPreference.fromPhoneBool, true);
                    sp.saveString(SharedPreference.fromPhoneFlow, etEmail.getText().toString());
                    CommonDialogs.dismiss();
                    startActivity(new Intent(EmailActivity.this, WelcomeActivity.class));*/
                }
            });
        } else if (response.getEmailType() != null && response.getEmailType() == 2) {
            showAlertOfExiting("This email is linked to an existing account. Please enter another email address.");
        } else {
            sp.setIsFromNumber(false);
            sp.setIsFromEmail(true);
            sp.saveBoolean(SharedPreference.fromPhoneBool, true);
            sp.saveString(SharedPreference.fromPhoneFlow, etEmail.getText().toString());
            CommonDialogs.dismiss();
            startActivity(new Intent(EmailActivity.this, WelcomeActivity.class));
        }
    }

    private void showAlertOfExiting(String s) {
        Dialog dialog2 = new Dialog(EmailActivity.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog2.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog2.setContentView(R.layout.already_exist_dialog);
        dialog2.setCancelable(false);
        dialog2.setCanceledOnTouchOutside(false);
        Window window = dialog2.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView tvOk = dialog2.findViewById(R.id.tvOk);
        if (!TextUtils.isEmpty(s)) {
            TextView tv_message = dialog2.findViewById(R.id.tv_message);
            tv_message.setText(s);
        }
        dialog2.show();
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
            }
        });
    }

    @Override
    public void onError(String error) {
        hideLoading();
        if (error.equalsIgnoreCase("Email does not exist.")) {
            sp.setIsFromNumber(false);
            sp.setIsFromEmail(true);
            sp.saveBoolean(SharedPreference.fromPhoneBool, true);
            sp.saveString(SharedPreference.fromPhoneFlow, etEmail.getText().toString());
            CommonDialogs.dismiss();
            startActivity(new Intent(EmailActivity.this, WelcomeActivity.class));
        } else
            showSnackbar(etEmail, error);
    }

    private void CallForLinkAccount() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("email", etEmail.getText().toString().trim());
        map.put("socialType", 8);
        showLoading();
        ApiCall.LinkAccountForEmail(map, this);
    }

    @Override
    public void onSuccessLinkEmail(PhoneLoginResponse response) {
        hideLoading();
        if (response.getSuccess()) {
            Toast.makeText(EmailActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(EmailActivity.this, VerificationActivity.class);
            intent.putExtra("email", etEmail.getText().toString());
            intent.putExtra("otp", String.valueOf(response.getOtp()));
            intent.putExtra("forLinkNumber", "yes");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            showSnackbar(etEmail, response.getMessage());
        }
    }
}