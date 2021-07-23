package com.swift.dating.ui.createAccountScreen.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import com.swift.dating.R;
import com.swift.dating.common.wheelpicker.LoopView;
import com.swift.dating.data.network.Resource;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountSignModel;
import com.swift.dating.model.responsemodel.VerificationResponseModel;
import com.swift.dating.ui.base.BaseFragment;
import com.swift.dating.ui.createAccountScreen.CreateAccountActivity;
import com.swift.dating.ui.createAccountScreen.viewmodel.CreateAccountViewModel;

public class SignFragment extends BaseFragment implements View.OnClickListener {

    Button btnContinue;
    CreateAccountViewModel model;
    boolean clickOnSkip = false;
    private ArrayList<String> signList = new ArrayList<>();
    private int selectedPos;
    String strSign = "";

    @Override
    public int getLayoutId() {
        return R.layout.fragment_sign;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialize(view);
        subscribeModel();
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
        model.SignResponse().observe(this, new Observer<Resource<VerificationResponseModel>>() {
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
                                ((CreateAccountActivity) getActivity()).updateParseCount(8);
                                sendIntent();
                            }
                        } else {
                            getBaseActivity().showSnackbar(btnContinue, resource.data.getMessage());
                            if(resource.data.getError()!=null && resource.data.getError().getCode().equalsIgnoreCase("401"))
                                getBaseActivity().openActivityOnTokenExpire();
                        }
                        break;
                    case ERROR:
                        getBaseActivity().hideLoading();
                        getBaseActivity().showSnackbar(btnContinue, resource.message);
                        if(resource.code == 401)
                            getBaseActivity().openActivityOnTokenExpire();
                        break;
                }
            }
        });
    }

    /**
     ***  Method to Handle api Response
     */
    private void sendIntent() {
        if (((CreateAccountActivity) Objects.requireNonNull(getActivity())).isEdit) {
            getActivity().finish();
            getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            ((CreateAccountActivity) getActivity()).addFragment();
        }
    }

    /**
     ***  Method to Initialize
     */
    private void initialize(View view) {
        btnContinue = view.findViewById(R.id.btn_continue);
        btnContinue.setText(((CreateAccountActivity)getActivity()).btn_text);
        LoopView pickerArray = view.findViewById(R.id.picker_sign);
        pickerArray.setNotLoop();
        pickerArray.setListener(item -> {
            selectedPos = item;
        });

        signList.addAll(Arrays.asList(getResources().getStringArray(R.array.signArray)));

        pickerArray.setArrayList(signList);
        pickerArray.setInitPosition(selectedPos);


        if (!TextUtils.isEmpty(((CreateAccountActivity) getActivity()).getUserData().getZodiacSign())) {
            selectedPos = signList.indexOf(((CreateAccountActivity) getActivity()).getUserData().getZodiacSign());
            pickerArray.setInitPosition(selectedPos);
            btnContinue.setBackground(getContext().getResources().getDrawable(R.drawable.gradientbtn));
            btnContinue.setEnabled(true);
            if (((CreateAccountActivity) getActivity()).isEdit) {
                btnContinue.setText("Done");
            }
        }
        btnContinue.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if (view == btnContinue) {
            hideKeyboard();
            getBaseActivity().showLoading();
            if(clickOnSkip){
                strSign = "";
            }else{
                strSign= signList.get(selectedPos);
            }
            model.verifyRequest(new CreateAccountSignModel(strSign));
        }
    }

    /**
     *** Method to Skip Question/Field
     */
    public void skipFragments() {
        strSign = "";
        clickOnSkip = true;
        btnContinue.performClick();
    }
}
