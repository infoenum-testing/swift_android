package com.swiftdating.app.ui.createAccountScreen.fragments;

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

import com.swiftdating.app.R;
import com.swiftdating.app.data.network.Resource;
import com.swiftdating.app.model.requestmodel.createaccountmodel.CreateAccountSmokeModel;
import com.swiftdating.app.model.responsemodel.VerificationResponseModel;
import com.swiftdating.app.ui.base.BaseFragment;
import com.swiftdating.app.ui.createAccountScreen.CreateAccountActivity;
import com.swiftdating.app.ui.createAccountScreen.viewmodel.CreateAccountViewModel;

public class SmokeFragment extends BaseFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private Button btnContinue;
    CreateAccountViewModel model;
    private RadioGroup tgSmoke;
    private String strSmoke;
    View view;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_smoke;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialize(view);
        subscribeViewModel();
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

    @Override
    public void onPause() {
        super.onPause();
        getBaseActivity().hideLoading();
    }

    /**
     ***  Method to Handle api Response
     */
    private void subscribeViewModel() {
        model = ViewModelProviders.of(this).get(CreateAccountViewModel.class);
        model.smokeResponse().observe(this, new Observer<Resource<VerificationResponseModel>>() {
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
                                ((CreateAccountActivity) getActivity()).updateParseCount(15);
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
        this.view = view;
        btnContinue = view.findViewById(R.id.btn_continue);
        btnContinue.setText(((CreateAccountActivity)getActivity()).btn_text);
        tgSmoke = view.findViewById(R.id.tgSmoke);
        btnContinue.setOnClickListener(this);
        tgSmoke.setOnCheckedChangeListener(this);
        if (!TextUtils.isEmpty(((CreateAccountActivity) getActivity()).getUserData().getSmoke())) {
            tgSmoke.check(((CreateAccountActivity) getActivity()).getUserData().getSmoke().equalsIgnoreCase("Never") ?
                    R.id.notb : ((CreateAccountActivity) getActivity()).getUserData().getSmoke().equalsIgnoreCase("Often") ? R.id.oftentb
                    : R.id.socialltTb);
            strSmoke = ((CreateAccountActivity) getActivity()).getUserData().getSmoke();
            btnContinue.setEnabled(true);
        }

    }

    @Override
    public void onClick(View view) {
        if(view == btnContinue){
            if (isNetworkConnected()) {
                hideKeyboard();
                getBaseActivity().showLoading();
                model.verifyRequest(new CreateAccountSmokeModel(strSmoke));

            } else {
                getBaseActivity().showSnackbar(btnContinue, "Please connect to internet");
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == tgSmoke) {
            btnContinue.setEnabled(true);
            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton radioSmokeButton = view.findViewById(selectedId);
            if (radioSmokeButton != null) {
                strSmoke = radioSmokeButton.getText().toString();
                if (strSmoke.equalsIgnoreCase("never")) {
                    strSmoke = "Never";
                }
            } else
                strSmoke = "";
        }
    }

    /**
     *** Method to Skip Question/Field
     */
    public void skipFragments() {
        strSmoke = "";
        btnContinue.performClick();
    }
}
