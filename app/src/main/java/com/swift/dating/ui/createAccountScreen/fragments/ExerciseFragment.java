package com.swift.dating.ui.createAccountScreen.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;

import java.util.Objects;

import com.swift.dating.R;
import com.swift.dating.data.network.Resource;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountExerciseModel;
import com.swift.dating.model.responsemodel.VerificationResponseModel;
import com.swift.dating.ui.base.BaseFragment;
import com.swift.dating.ui.createAccountScreen.CreateAccountActivity;
import com.swift.dating.ui.createAccountScreen.viewmodel.CreateAccountViewModel;

public class ExerciseFragment extends BaseFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private View view;
    private CreateAccountViewModel model;
    private RadioGroup tgExercise;
    private Button btnContinue;
    private String strExercise;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_exercise;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
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
        } else {
            ((CreateAccountActivity) getActivity()).addFragment();
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
        model.exerciseResponse().observe(this, new Observer<Resource<VerificationResponseModel>>() {
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
                                ((CreateAccountActivity) getActivity()).updateParseCount(16);
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
        tgExercise = view.findViewById(R.id.tgExercise);
        btnContinue = view.findViewById(R.id.btn_continue);
        btnContinue.setText(((CreateAccountActivity)getActivity()).btn_text);
        if (!TextUtils.isEmpty(((CreateAccountActivity) getActivity()).getUserData().getExercise())) {
            tgExercise.check(((CreateAccountActivity) getActivity()).getUserData().getExercise().equalsIgnoreCase("Almost Never") ?
                    R.id.nevetbExercise : ((CreateAccountActivity) getActivity()).getUserData().getExercise().equalsIgnoreCase("Often") ? R.id.oftenExercisetb
                    : R.id.sometimesExercisetb);
            strExercise = ((CreateAccountActivity) getActivity()).getUserData().getExercise();
            btnContinue.setBackground(getContext().getResources().getDrawable(R.drawable.gradientbtn));
            btnContinue.setEnabled(true);
            if (((CreateAccountActivity) getActivity()).isEdit) {
                btnContinue.setText("Done");
            }
        }
        tgExercise.setOnCheckedChangeListener(this);
        btnContinue.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == btnContinue) {
            if (isNetworkConnected()) {
                getBaseActivity().showLoading();
                hideKeyboard();
                model.verifyRequest(new CreateAccountExerciseModel(strExercise));
            } else {
                getBaseActivity().showSnackbar(btnContinue, "Please connect to internet");
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if(radioGroup == tgExercise){
            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton radioDrinkButton = view.findViewById(selectedId);
            if (radioDrinkButton != null) {
                btnContinue.setEnabled(true);
                btnContinue.setBackground(getContext().getResources().getDrawable(R.drawable.gradientbtn));
                strExercise = radioDrinkButton.getText().toString();
            }
        }

    }

    /**
     *** Method to Skip Question/Field
     */
    public void skipFragments() {
        strExercise = "";
        btnContinue.performClick();
    }
}