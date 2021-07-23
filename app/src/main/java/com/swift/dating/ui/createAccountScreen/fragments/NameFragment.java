package com.swift.dating.ui.createAccountScreen.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;

import com.swift.dating.R;
import com.swift.dating.data.network.Resource;
import com.swift.dating.data.preference.SharedPreference;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountNameModel;
import com.swift.dating.model.responsemodel.VerificationResponseModel;
import com.swift.dating.ui.base.BaseFragment;
import com.swift.dating.ui.createAccountScreen.CreateAccountActivity;
import com.swift.dating.ui.createAccountScreen.viewmodel.CreateAccountViewModel;


public class NameFragment extends BaseFragment implements View.OnClickListener, TextWatcher {

    private EditText etName;
    private Button btnContinue;
    private CreateAccountViewModel model;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_name;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialize(view);
        subscribeModel();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (((CreateAccountActivity) getActivity()).getUserData() != null && !TextUtils.isEmpty(((CreateAccountActivity) getActivity()).getUserData().getName())) {
            etName.setText(((CreateAccountActivity) getActivity()).getUserData().getName());
        }
    }

    /**
     * **  Method to Handle api Response
     */
    private void subscribeModel() {
        model = ViewModelProviders.of(this).get(CreateAccountViewModel.class);
        model.nameResponse().observe(this, new Observer<Resource<VerificationResponseModel>>() {
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
                                getBaseActivity().sp.saveUserData(obj.getUser().getProfileOfUser(), resource.data.getUser().getProfileOfUser().getCompleted().toString());
                                ((CreateAccountActivity) getActivity()).updateParseCount(2);
                                ((CreateAccountActivity) getActivity()).addFragment();
                            }
                        } else {
                            getBaseActivity().hideLoading();
                            getBaseActivity().showSnackbar(btnContinue, resource.data.getMessage());
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
     * **  Method to Initialize
     */
    private void initialize(View view) {
        etName = view.findViewById(R.id.et_name);
        btnContinue = view.findViewById(R.id.btn_continue);
        btnContinue.setOnClickListener(this);
        etName.addTextChangedListener(this);
        if (((CreateAccountActivity) getActivity()).getUserData() != null && !TextUtils.isEmpty(((CreateAccountActivity) getActivity()).getUserData().getName())) {
            etName.setText(((CreateAccountActivity) getActivity()).getUserData().getName());
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
            if (((CreateAccountActivity) getActivity()).preference.getIsFromNumber()) {
                ((CreateAccountActivity) getActivity()).updateParseCount(2);
                ((CreateAccountActivity) getActivity()).addFragment();
            } else {
                hideKeyboard();
                getBaseActivity().showLoading();
                boolean isFromPhoneFlow = (((CreateAccountActivity) getActivity()).preference.getBoolean(SharedPreference.fromPhoneBool));
                String email = (((CreateAccountActivity) getActivity()).preference.getMyString(SharedPreference.fromPhoneFlow));
                if (isFromPhoneFlow && !TextUtils.isEmpty(email)) {
                    model.verifyRequest(new CreateAccountNameModel(etName.getText().toString(), email));
                } else {
                    model.verifyRequest(new CreateAccountNameModel(etName.getText().toString()));
                }
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onPause() {
        super.onPause();
        getBaseActivity().hideLoading();
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (!TextUtils.isEmpty(charSequence.toString().trim())) {
            btnContinue.setEnabled(true);
            btnContinue.setBackground(getContext().getResources().getDrawable(R.drawable.gradientbtn));
        } else {
            btnContinue.setEnabled(false);
            btnContinue.setBackground(getContext().getResources().getDrawable(R.drawable.disabledbtn));
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
