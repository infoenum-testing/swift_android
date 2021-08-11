package com.swiftdating.app.ui.createAccountScreen.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import com.swiftdating.app.ui.base.BaseFragment;
import com.swiftdating.app.R;
import com.swiftdating.app.callbacks.OnItemClickListener;
import com.swiftdating.app.data.network.Resource;
import com.swiftdating.app.model.FlexModel;
import com.swiftdating.app.model.requestmodel.createaccountmodel.CreateAccountEducationModel;
import com.swiftdating.app.model.responsemodel.VerificationResponseModel;
import com.swiftdating.app.ui.createAccountScreen.CreateAccountActivity;
import com.swiftdating.app.ui.createAccountScreen.adapter.FlexAdapter;
import com.swiftdating.app.ui.createAccountScreen.viewmodel.CreateAccountViewModel;

public class EducationFragment extends BaseFragment implements View.OnClickListener, OnItemClickListener {

    private Button btnContinue;
    View view;
    private CreateAccountViewModel model;

    private FlexAdapter adapter;
    private ArrayList<FlexModel> educationList = new ArrayList<>();

    private int lastPosition = -1;
    private String strEducation = "";

    @Override
    public int getLayoutId() {
        return R.layout.fragment_education;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;


        initialize(view);
        getEducation();
        subscribeModel();
    }

    /**
     *   Method to Send intent after Api Hit
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
        model.educationResponse().observe(this, new Observer<Resource<VerificationResponseModel>>() {
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
                            Gson gson = new Gson();
                            String user = getBaseActivity().sp.getUser();
                            VerificationResponseModel obj = gson.fromJson(user, VerificationResponseModel.class);

                            obj.setUser(resource.data.getUser());
                            getBaseActivity().sp.saveUserData(obj.getUser().getProfileOfUser(),resource.data.getUser().getProfileOfUser().getCompleted().toString());
                            ((CreateAccountActivity) getActivity()).updateParseCount(11);
                            sendIntent();
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
     ** Method to Initialize
     */
    private void initialize(View view) {
        btnContinue = view.findViewById(R.id.btn_continue);
        btnContinue.setText(((CreateAccountActivity)getActivity()).btn_text);
        btnContinue.setOnClickListener(this);
        if (((CreateAccountActivity) Objects.requireNonNull(getActivity())).isEdit) {
            btnContinue.setText("Done");
        }
        RecyclerView rv_education = view.findViewById(R.id.rv_education);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getActivity());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        rv_education.setLayoutManager(layoutManager);
        adapter = new FlexAdapter(getContext(), educationList);
        adapter.setOnItemClickListener(this);
        rv_education.setAdapter(adapter);

    }

    /**
     *  Method to set List in toggle Button
     */
    private void getEducation() {
        educationList.clear();
        ArrayList<String> list = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.educationArray)));
        for (int i = 0; i < list.size(); i++) {
            educationList.add(new FlexModel(list.get(i)));
            if (((CreateAccountActivity) getActivity()).getUserData().getEducation() != null && educationList.get(i).getName().equalsIgnoreCase(((CreateAccountActivity) getActivity()).getUserData().getEducation())) {
                educationList.get(i).setChecked(true);
                strEducation = educationList.get(i).getName();
                btnContinue.setEnabled(true);
                lastPosition = i;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getEducation();
    }

    @Override
    public void onClick(View view) {
        if (view == btnContinue) {
            if (isNetworkConnected()) {
                getBaseActivity().showLoading();
                hideKeyboard();
                model.verifyRequest(new CreateAccountEducationModel(strEducation));
            } else {
                getBaseActivity().showSnackbar(btnContinue, "Please connect to internet");
            }
        }
    }

    @Override
    public void OnItemClick(int position) {
        strEducation = educationList.get(position).getName();
        educationList.get(position).setChecked(true);
        if (lastPosition != -1 && lastPosition != position)
            educationList.get(lastPosition).setChecked(false);
        adapter.notifyDataSetChanged();
        lastPosition = position;
        btnContinue.setEnabled(true);
    }

    /**
     *** Method to Skip Question/Field
     */
    public void skipFragments() {
        strEducation = "";
        btnContinue.performClick();
    }
}
