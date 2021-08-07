package com.swiftdating.app.ui.createAccountScreen.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.Objects;

import com.swiftdating.app.R;
import com.swiftdating.app.data.network.Resource;
import com.swiftdating.app.model.requestmodel.createaccountmodel.CreateAccountInterestedModel;
import com.swiftdating.app.model.responsemodel.VerificationResponseModel;
import com.swiftdating.app.ui.base.BaseFragment;
import com.swiftdating.app.ui.createAccountScreen.CreateAccountActivity;
import com.swiftdating.app.ui.createAccountScreen.viewmodel.CreateAccountViewModel;
import com.swiftdating.app.ui.homeScreen.fragment.SearchFragment;
import com.swiftdating.app.ui.settingScreen.SettingsActivity;

public class ShowMeFragment extends BaseFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    CreateAccountViewModel model;
    private FloatingActionButton btnContinue;
    private RadioGroup tgShowMe;
    private String strShowMe = "Men";

    @Override
    public int getLayoutId() {
        return R.layout.fragment_show_me;
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

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * **  Method to Handle api Response
     */
    private void subscribeModel() {
        model = ViewModelProviders.of(this).get(CreateAccountViewModel.class);
        model.interestedResponse().observe(this, new Observer<Resource<VerificationResponseModel>>() {
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
                                getBaseActivity().sp.saveisSettingsChanged(true);
                                if (((CreateAccountActivity) Objects.requireNonNull(getActivity())).isEdit) {
                                    if (!TextUtils.isEmpty(getBaseActivity().sp.getMyString(SearchFragment.SearchResponse)))
                                        getBaseActivity().sp.removeKey(SearchFragment.SearchResponse);
                                    if (!TextUtils.isEmpty(getBaseActivity().sp.getMyString(SearchFragment.FilterResponse)))
                                        getBaseActivity().sp.removeKey(SearchFragment.FilterResponse);
                                }
                                getBaseActivity().sp.saveUserData(obj.getUser().getProfileOfUser(), resource.data.getUser().getProfileOfUser().getCompleted().toString());
                                ((CreateAccountActivity) getActivity()).updateParseCount(5);
                                sendIntent();
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
        btnContinue = view.findViewById(R.id.btn_continue);
        btnContinue.setOnClickListener(this);
        tgShowMe = view.findViewById(R.id.tgShowMe);
        tgShowMe.setOnCheckedChangeListener(this);
        if (((CreateAccountActivity) getActivity()).getUserData() != null && !TextUtils.isEmpty(((CreateAccountActivity) getActivity()).getUserData().getInterested())) {
            tgShowMe.check(((CreateAccountActivity) getActivity()).getUserData().getInterested().equalsIgnoreCase("men") ?
                    R.id.Maletb : ((CreateAccountActivity) getActivity()).getUserData().getInterested().equalsIgnoreCase("women") ? R.id.tbWomen
                    : R.id.everyoneTb);
            strShowMe = ((CreateAccountActivity) getActivity()).getUserData().getInterested();
            btnContinue.setBackground(getContext().getResources().getDrawable(R.drawable.gradientbtn));
            btnContinue.setEnabled(true);
            /*if (((CreateAccountActivity) getActivity()).isEdit) {
                btnContinue.setText("Done");
            }*/
        }

    }

    /**
     * **  Method to Handle api Response
     */
    private void sendIntent() {
        if (((CreateAccountActivity) Objects.requireNonNull(getActivity())).isEdit) {
            SettingsActivity.isSettingChanged = true;
            getActivity().finish();
        } else {
            ((CreateAccountActivity) getActivity()).addFragment();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_continue) {
            getBaseActivity().showLoading();
            hideKeyboard();
            model.verifyRequest(new CreateAccountInterestedModel(strShowMe));
        }
/*        if ( ((CreateAccountActivity) getActivity()).preference.getIsFromNumber())
        {
            ((CreateAccountActivity) getActivity()).updateParseCount(5);
            sendIntent();
        }else {

        getBaseActivity().showLoading();
        hideKeyboard();
        model.verifyRequest(new CreateAccountInterestedModel(strShowMe));
         }*/
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == tgShowMe) {
            btnContinue.setEnabled(true);
            btnContinue.setBackground(getContext().getResources().getDrawable(R.drawable.gradientbtn));
            if (i == R.id.Maletb)
                strShowMe = "Men";
            else if (i == R.id.tbWomen)
                strShowMe = "Women";
            else if (i == R.id.everyoneTb) {
                strShowMe = "Both";
            } else {
                strShowMe = "";
            }
        }
    }
}