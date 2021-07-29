package com.swift.dating.ui.createAccountScreen.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import com.swift.dating.ui.base.BaseFragment;
import com.swift.dating.R;
import com.swift.dating.common.wheelpicker.LoopView;
import com.swift.dating.data.network.Resource;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountGenderModel;
import com.swift.dating.model.responsemodel.VerificationResponseModel;
import com.swift.dating.ui.createAccountScreen.CreateAccountActivity;
import com.swift.dating.ui.createAccountScreen.viewmodel.CreateAccountViewModel;
import com.swift.dating.ui.homeScreen.fragment.SearchFragment;

public class GenderFragment extends BaseFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    CreateAccountViewModel model;
    FloatingActionButton btnContinue;
    private RadioGroup tgGender, tgShowMeTo;
    private NumberPicker pickerGender;
    private String strGender, strShowMeTo = "";
    private int genderPos;
    // private ArrayList<String> genderList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_gender;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialize(view);
        subscribeModel();
    }

    /**
     * **  Method to Initialize
     */
    private void initialize(View view) {
        btnContinue = view.findViewById(R.id.btn_continue);
        pickerGender = view.findViewById(R.id.picker_gender);
        pickerGender.setWrapSelectorWheel(true);
        // btnContinue.setText(((CreateAccountActivity)getActivity()).btn_text);
        tgGender = view.findViewById(R.id.tgGender);
        tgShowMeTo = view.findViewById(R.id.tgshowMeTo);
        pickerGender = view.findViewById(R.id.picker_gender);
        pickerGender.setWrapSelectorWheel(false);
        pickerGender.setMinValue(0);
        pickerGender.setMaxValue(getResources().getStringArray(R.array.genderArray).length - 1);

        pickerGender.setOnValueChangedListener((picker, oldVal, newVal) -> genderPos = newVal);

        // genderList.addAll(Arrays.asList(getResources().getStringArray(R.array.genderArray)));
        pickerGender.setDisplayedValues(getResources().getStringArray(R.array.genderArray));
        pickerGender.setValue(genderPos);

        if (((CreateAccountActivity) getActivity()).getUserData() != null && !TextUtils.isEmpty(((CreateAccountActivity) getActivity()).getUserData().getGender())) {
            if (((CreateAccountActivity) getActivity()).getUserData().getGender().equalsIgnoreCase("Male")) {
                tgGender.check(R.id.tbMale);
                strGender = "Male";
                strShowMeTo = "";
            } else if (((CreateAccountActivity) getActivity()).getUserData().getGender().equalsIgnoreCase("Female")) {
                tgGender.check(R.id.femaleTb);
                strGender = "Female";
                strShowMeTo = "";
            } else {
                tgGender.check(R.id.otherTb);
                pickerGender.setVisibility(View.VISIBLE);
                tgShowMeTo.setVisibility(View.VISIBLE);
                //  pickerGender.setInitPosition(genderList.indexOf(((CreateAccountActivity) getActivity()).getUserData().getGender()));
                if (!TextUtils.isEmpty(((CreateAccountActivity) getActivity()).getUserData().getShowmeto()))
                    tgShowMeTo.check(((CreateAccountActivity) getActivity()).getUserData().getShowmeto().equalsIgnoreCase("male") ?
                            R.id.tvshowMen : R.id.tvshowWomen);
                strGender = ((CreateAccountActivity) getActivity()).getUserData().getGender();
                if (tgShowMeTo.getCheckedRadioButtonId() == R.id.tvshowMen) {
                    strShowMeTo = "Male";
                    tgShowMeTo.check(R.id.tvshowMen);
                } else {
                    strShowMeTo = "Female";
                    tgShowMeTo.check(R.id.tvshowWomen);
                }

            }
            btnContinue.setBackground(getContext().getResources().getDrawable(R.drawable.gradientbtn));
            btnContinue.setEnabled(true);
            /*if (((CreateAccountActivity) getActivity()).isEdit) {
                btnContinue.setText("Done");
            }*/
        }
        btnContinue.setOnClickListener(this);
        tgGender.setOnCheckedChangeListener(this);
        tgShowMeTo.setOnCheckedChangeListener(this);


    }

    @Override
    public void onPause() {
        super.onPause();
        getBaseActivity().hideLoading();
    }

    /**
     * **  Method to Handle api Response
     */
    private void subscribeModel() {
        model = ViewModelProviders.of(this).get(CreateAccountViewModel.class);

        model.genderResponse().observe(this, new Observer<Resource<VerificationResponseModel>>() {
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
                                getBaseActivity().sp.saveisSettingsChanged(true);
                                if (((CreateAccountActivity) Objects.requireNonNull(getActivity())).isEdit) {
                                    if (!TextUtils.isEmpty(getBaseActivity().sp.getMyString(SearchFragment.SearchResponse)))
                                        getBaseActivity().sp.removeKey(SearchFragment.SearchResponse);
                                    if (!TextUtils.isEmpty(getBaseActivity().sp.getMyString(SearchFragment.FilterResponse)))
                                        getBaseActivity().sp.removeKey(SearchFragment.FilterResponse);
                                }
                                obj.setUser(resource.data.getUser());
                                getBaseActivity().sp.saveUserData(obj.getUser().getProfileOfUser(), resource.data.getUser().getProfileOfUser().getCompleted().toString());
                                ((CreateAccountActivity) getActivity()).updateParseCount(4);
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

    @Override
    public void onClick(View view) {
        if (view == btnContinue) {
            if (((CreateAccountActivity) getActivity()).preference.getIsFromNumber()) {
                ((CreateAccountActivity) getActivity()).updateParseCount(4);
                sendIntent();
            } else {
                getBaseActivity().showLoading();
                hideKeyboard();
                if (!strGender.equalsIgnoreCase("male") && !strGender.equalsIgnoreCase("female"))
                    strGender = getContext().getResources().getStringArray(R.array.genderArray)[pickerGender.getValue()];
                model.verifyRequest(new CreateAccountGenderModel(strGender, strShowMeTo));
            }
        }
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
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == tgGender) {
            if (i == R.id.otherTb) {
                btnContinue.setEnabled(false);
                btnContinue.setBackground(getContext().getResources().getDrawable(R.drawable.disabledbtn));
                pickerGender.setVisibility(View.VISIBLE);
                tgShowMeTo.setVisibility(View.VISIBLE);
            } else {
                btnContinue.setEnabled(true);
                btnContinue.setBackground(getContext().getResources().getDrawable(R.drawable.gradientbtn));
                strGender = i == R.id.tbMale ? "Male" : "Female";
                strShowMeTo = "";
                pickerGender.setVisibility(View.GONE);
                tgShowMeTo.setVisibility(View.GONE);
            }
        } else if (radioGroup == tgShowMeTo) {
            strGender = this.getResources().getStringArray(R.array.genderArray)[pickerGender.getValue()];
            strShowMeTo = i == R.id.tvshowMen ? "Male" : "Female";
            btnContinue.setEnabled(true);
            btnContinue.setBackground(getContext().getResources().getDrawable(R.drawable.gradientbtn));
        }
    }
}