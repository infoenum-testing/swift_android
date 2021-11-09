package com.swiftdating.app.ui.answerNowScreen;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import com.swiftdating.app.R;
import com.swiftdating.app.common.CommonDialogs;
import com.swiftdating.app.data.network.CallServer;
import com.swiftdating.app.data.network.Resource;
import com.swiftdating.app.model.requestmodel.AnswerProfileRequest;
import com.swiftdating.app.model.responsemodel.ProfileOfUser;
import com.swiftdating.app.model.responsemodel.VerificationResponseModel;
import com.swiftdating.app.ui.base.BaseActivity;
import com.swiftdating.app.ui.itsAMatchScreen.viewmodel.MatchViewModel;
import com.swiftdating.app.ui.loginScreen.LoginActivity;
import com.swiftdating.app.ui.questionListScreen.QuestionListActivity;

public class AnswerNowActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    EditText etAnswer1;
    ImageView backBtn;
    TextView tvQuestion1, tvCounter;
    Button btnSubmit, btnUpdate;
    MatchViewModel matchViewModel;
    String strQuestion = "", counter;

    private static final String TAG = "AnswerNowActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_now);
        init();
        subscribeModel();
    }

    /**
     * **  Method to Initialize
     */
    private void init() {
        etAnswer1 = findViewById(R.id.et_answer1);
        tvQuestion1 = findViewById(R.id.tvQuestion1);
        etAnswer1 = findViewById(R.id.et_answer1);
        etAnswer1 = findViewById(R.id.et_answer1);
        backBtn = findViewById(R.id.ivback);
        btnSubmit = findViewById(R.id.btn_submit);
        btnUpdate = findViewById(R.id.btn_update);
        tvCounter = findViewById(R.id.tvCounter);

        handleData();
        implementClickLister();

    }

    /**
     * **  Method to handle Data through Intent
     */
    private void handleData() {
        if (getIntent().hasExtra("Question")) { // Coming from Question screen
            tvQuestion1.setText(getIntent().getExtras().getString("Question"));
            btnUpdate.setVisibility(View.GONE);

        } else if (getIntent().hasExtra("question")) {// Coming direct from edit profile screen
            tvQuestion1.setText(getIntent().getExtras().getString("question"));
            etAnswer1.setText(getIntent().getExtras().getString("answer"));
            btnUpdate.setVisibility(View.VISIBLE);
            strQuestion = getIntent().getExtras().getString("question");
            btnSubmit.setEnabled(true);
        }
        counter = String.valueOf(150 - etAnswer1.getText().toString().trim().length());
        tvCounter.setText(counter);
    }

    /**
     * **  Method to Implement ClickListener
     */
    private void implementClickLister() {
        backBtn.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        etAnswer1.addTextChangedListener(this);
    }

    /**
     * **  Method to Handle Response of Api
     */
    private void subscribeModel() {
        matchViewModel = ViewModelProviders.of(this).get(MatchViewModel.class);
        matchViewModel.answersProfileResponse().observe(this, new Observer<Resource<VerificationResponseModel>>() {
            @Override
            public void onChanged(@Nullable Resource<VerificationResponseModel> resource) {
                if (resource == null) {
                    return;
                }
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        hideLoading();
                        if (resource.data.getSuccess()) {
                            if (resource.data.getError() != null && resource.data.getError().getCode().equalsIgnoreCase("401")) {
                                String deviceToken = sp.getDeviceToken();
                                sp.clearData();
                                sp.saveDeviceToken(deviceToken);
                                startActivity(new Intent(AnswerNowActivity.this, LoginActivity.class));
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                finishAffinity();
                            } else {
                                hideKeyboard();
                                sp.saveUserData(resource.data.getUser().getProfileOfUser(), resource.data.getProfileCompleted().toString());
                                setResult(RESULT_OK, new Intent());
                                finish();
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            }
                        } else {
                            showSnackbar(etAnswer1, resource.data.getMessage());
                        }
                        break;
                    case ERROR:
                        hideKeyboard();
                        showSnackbar(etAnswer1, CallServer.somethingWentWrong);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == backBtn) {
            hideKeyboard();
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (view == btnSubmit) {
            showLoading();
            ProfileOfUser obj = new Gson().fromJson(sp.getUser(), ProfileOfUser.class);
            if (getIntent().getExtras().getString("isQuestion1").equalsIgnoreCase("1")) {
                matchViewModel.answersProfileRequest(new AnswerProfileRequest(getIntent().getExtras().getString("Question"),
                        etAnswer1.getText().toString(), obj.getQuestion2(), obj.getAnswer2(), obj.getQuestion3(), obj.getAnswer3()));
            } else if (getIntent().getExtras().getString("isQuestion1").equalsIgnoreCase("2")) {
                matchViewModel.answersProfileRequest(new AnswerProfileRequest(obj.getQuestion1(), obj.getAnswer1(),
                        getIntent().getExtras().getString("Question"),
                        etAnswer1.getText().toString(), obj.getQuestion3(), obj.getAnswer3()));
            } else {
                matchViewModel.answersProfileRequest(new AnswerProfileRequest(obj.getQuestion1(), obj.getAnswer1(),
                        obj.getQuestion2(), obj.getAnswer2(), getIntent().getExtras().getString("Question"),
                        etAnswer1.getText().toString()));
            }

        } else if (view.getId() == R.id.tv_yes) {
            CommonDialogs.dismiss();

        } else if (view == btnUpdate) {
            Intent intent = new Intent(this, QuestionListActivity.class);
            intent.putExtra("ques", strQuestion);
            intent.putExtra("isQuestion1", getIntent().getExtras().getString("isQuestion1"));
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        btnSubmit.setEnabled(!TextUtils.isEmpty(s.toString().trim()));
        counter = String.valueOf(150 - etAnswer1.getText().toString().trim().length());
        tvCounter.setText(counter);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
