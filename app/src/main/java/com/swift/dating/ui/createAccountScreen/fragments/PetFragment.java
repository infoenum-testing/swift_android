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
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountPetModel;
import com.swift.dating.model.responsemodel.VerificationResponseModel;
import com.swift.dating.ui.base.BaseFragment;
import com.swift.dating.ui.createAccountScreen.CreateAccountActivity;
import com.swift.dating.ui.createAccountScreen.viewmodel.CreateAccountViewModel;

public class PetFragment extends BaseFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private Button btnContinue;
    private CreateAccountViewModel model;
    private RadioGroup tgPet;
    View view;
    private String strPets;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_pet;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        initialize(view);
        subscribeModel();

    }

    @Override
    public void onPause() {
        super.onPause();
        getBaseActivity().hideLoading();
    }

    /***
     *  Method to send Intent on Api Hit
     */
    private void sendIntent() {
        if (((CreateAccountActivity) Objects.requireNonNull(getActivity())).isEdit) {
            getActivity().finish();
            getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    /**
     ***  Method to Handle api Response
     */
    private void subscribeModel() {
        model = ViewModelProviders.of(this).get(CreateAccountViewModel.class);
        model.ethnicityResponse().observe(this, new Observer<Resource<VerificationResponseModel>>() {
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
                            if (resource.data.getError() != null && resource.data.getError().getCode().equalsIgnoreCase("401")) {
                                getBaseActivity().openActivityOnTokenExpire();
                            } else {
                                Gson gson = new Gson();
                                String user = getBaseActivity().sp.getUser();
                                VerificationResponseModel obj = gson.fromJson(user, VerificationResponseModel.class);
                                obj.setUser(resource.data.getUser());
                                getBaseActivity().sp.saveUserData(obj.getUser().getProfileOfUser(),resource.data.getUser().getProfileOfUser().getCompleted().toString());
                                ((CreateAccountActivity) getActivity()).updateParseCount(15);
                                sendIntent();
                            }
                        } else {
                            getBaseActivity().hideLoading();
                            getBaseActivity().showSnackbar(btnContinue, resource.message);
                            if (resource.data.getError() != null && resource.data.getError().getCode().equalsIgnoreCase("401"))
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
        tgPet = view.findViewById(R.id.tgPet);
        btnContinue.setOnClickListener(this);
        tgPet.setOnCheckedChangeListener(this);
        if (!TextUtils.isEmpty(((CreateAccountActivity) getActivity()).getUserData().getPets())) {
            tgPet.check(((CreateAccountActivity) getActivity()).getUserData().getPets().equalsIgnoreCase("No") ?
                    R.id.noTb : R.id.yesTb);
            strPets = ((CreateAccountActivity) getActivity()).getUserData().getPets();
            btnContinue.setBackground(getContext().getResources().getDrawable(R.drawable.gradientbtn));
            btnContinue.setEnabled(true);
            if (((CreateAccountActivity) getActivity()).isEdit) {
                btnContinue.setText("Done");
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btnContinue) {
            if(isNetworkConnected()){
                getBaseActivity().showLoading();
                hideKeyboard();
                model.verifyRequest(new CreateAccountPetModel(strPets));
            }else{
                getBaseActivity().showSnackbar(btnContinue, "Please connect to internet");
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == tgPet) {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton radioDrinkButton = view.findViewById(selectedId);
            if (radioDrinkButton != null) {
                btnContinue.setEnabled(true);
                btnContinue.setBackground(getContext().getResources().getDrawable(R.drawable.gradientbtn));
                strPets = radioDrinkButton.getText().toString();
            } else
                strPets = "";
        }
    }

    /**
     *** Method to Skip Question/Field
     */
    public void skipFragments() {
        strPets = "";
        btnContinue.performClick();
    }
}
