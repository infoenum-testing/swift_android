package com.swift.dating.ui.createAccountScreen.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;

import java.util.Objects;

import com.swift.dating.R;
import com.swift.dating.data.network.Resource;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccoutAmbitionModel;
import com.swift.dating.model.responsemodel.VerificationResponseModel;
import com.swift.dating.ui.base.BaseFragment;
import com.swift.dating.ui.createAccountScreen.CreateAccountActivity;
import com.swift.dating.ui.createAccountScreen.viewmodel.CreateAccountViewModel;

public class InterestAmbitionFragment  extends BaseFragment implements View.OnClickListener, TextWatcher {

    private CreateAccountViewModel model;
    private Button btnContinue;
    private EditText etAboutMe;
    String strAmbition;
    private TextView tvCounter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_ambition;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialize(view);
        subscribeModel();
    }

    /***
     *  Method to send Intent on Api Hit
     */
    private void sendIntent() {

        if (((CreateAccountActivity) Objects.requireNonNull(getActivity())).isEdit) {
            getActivity().finish();
            getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }else {
            ((CreateAccountActivity) getActivity()).addFragment();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(etAboutMe!=null && !TextUtils.isEmpty(etAboutMe.getText()) && tvCounter!=null) {
            int length = etAboutMe.getText().toString().length();
            tvCounter.setText(String.valueOf(300 - length));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getBaseActivity().hideLoading();
    }

    /**
     ***  Method to Handle api Response
     */
    private void subscribeModel() {
        model = ViewModelProviders.of(this).get(CreateAccountViewModel.class);
        model.ambitionResponse().observe(this, new Observer<Resource<VerificationResponseModel>>() {
            @Override
            public void onChanged(@Nullable Resource<VerificationResponseModel> resource) {
                if (resource == null) {
                    return;
                }
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        getBaseActivity().hideLoading();
                        if (resource.data.getSuccess()) {
                            if(resource.data.getError()!=null && resource.data.getError().getCode().equalsIgnoreCase("401")){
                                getBaseActivity().openActivityOnTokenExpire();
                            }else {
                                Gson gson = new Gson();
                                String user = getBaseActivity().sp.getUser();
                                VerificationResponseModel obj = gson.fromJson(user, VerificationResponseModel.class);
                                obj.setUser(resource.data.getUser());
                                getBaseActivity().sp.saveUserData(obj.getUser().getProfileOfUser(),resource.data.getUser().getProfileOfUser().getCompleted().toString());
                                ((CreateAccountActivity) getActivity()).updateParseCount(10);
                                sendIntent();
                            }
                        } else {
                            getBaseActivity().hideLoading();
                            getBaseActivity().showSnackbar(btnContinue, resource.message);
                            if(resource.data.getError()!=null && resource.data.getError().getCode().equalsIgnoreCase("401"))
                                getBaseActivity().openActivityOnTokenExpire();
                        }
                        break;
                    case ERROR:
                        getBaseActivity().hideLoading();
                        getBaseActivity().showSnackbar(btnContinue, resource.message);
                        break;
                }
            }
        });
    }

    /**
     ***  Method to Initialize
     */
    private void initialize(View view) {
        btnContinue = view.findViewById(R.id.btn_continue);
        btnContinue.setText(((CreateAccountActivity)getActivity()).btn_text);
        etAboutMe = view.findViewById(R.id.etAboutMe);
        tvCounter = view.findViewById(R.id.tvCounter);
        if(!TextUtils.isEmpty(((CreateAccountActivity)getActivity()).getUserData().getAmbitions())){
            etAboutMe.setText(((CreateAccountActivity)getActivity()).getUserData().getAmbitions());
            btnContinue.setBackground(getContext().getResources().getDrawable(R.drawable.gradientbtn));
            btnContinue.setEnabled(true);
            if(((CreateAccountActivity)getActivity()).isEdit){
                btnContinue.setText("Done");
            }
        }

        btnContinue.setOnClickListener(this);
        etAboutMe.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnContinue) {
            getBaseActivity().showLoading();
            hideKeyboard();
            strAmbition = etAboutMe.getText().toString();
            model.verifyRequest(new CreateAccoutAmbitionModel(strAmbition));

        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.toString().trim().length() > 0) {
            btnContinue.setEnabled(true);
            btnContinue.setBackground(getContext().getResources().getDrawable(R.drawable.gradientbtn));
        } else {
            btnContinue.setEnabled(false);
            btnContinue.setBackground(getContext().getResources().getDrawable(R.drawable.disabledbtn));
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        int length = etAboutMe.getText().toString().length();
        tvCounter.setText(String.valueOf(300 - length));
    }

    /**
     *** Method to Skip Question/Field
     */
    public void skipFragments() {
        etAboutMe.setText("");
        btnContinue.performClick();
    }
}
