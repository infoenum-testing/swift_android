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

import com.swiftdating.app.R;
import com.swiftdating.app.callbacks.OnItemClickListener;
import com.swiftdating.app.data.network.Resource;
import com.swiftdating.app.model.FlexModel;
import com.swiftdating.app.model.requestmodel.createaccountmodel.CreateAccountRelegionModel;
import com.swiftdating.app.model.responsemodel.VerificationResponseModel;
import com.swiftdating.app.ui.base.BaseFragment;
import com.swiftdating.app.ui.createAccountScreen.CreateAccountActivity;
import com.swiftdating.app.ui.createAccountScreen.adapter.FlexAdapter;
import com.swiftdating.app.ui.createAccountScreen.viewmodel.CreateAccountViewModel;

public class ReligionFragment extends BaseFragment implements View.OnClickListener, OnItemClickListener {

    CreateAccountViewModel model;
    Button btnContinue;

    private FlexAdapter adapter;
    private ArrayList<FlexModel> religionList = new ArrayList<>();

    private int lastPosition = -1;
    private String strReligion = "";

    @Override
    public int getLayoutId() {
        return R.layout.fragment_religion;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
        getReligion();
        subscribeModel();
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
        model.religionResponse().observe(this, new Observer<Resource<VerificationResponseModel>>() {
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
                                ((CreateAccountActivity) getActivity()).updateParseCount(9);
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
     * **  Method to Initialize
     */
    private void initialize(View view) {
        btnContinue = view.findViewById(R.id.btn_continue);
        btnContinue.setText(((CreateAccountActivity) getActivity()).btn_text);
        if (((CreateAccountActivity) Objects.requireNonNull(getActivity())).isEdit) {
            btnContinue.setText("Done");
        }
        RecyclerView rv_religion = view.findViewById(R.id.rv_religion);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getActivity());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        rv_religion.setLayoutManager(layoutManager);

        adapter = new FlexAdapter(getContext(), religionList);
        adapter.setOnItemClickListener(this);
        rv_religion.setAdapter(adapter);

        btnContinue.setOnClickListener(this);


    }

    /***
     *  Method to set toggle button for religion list
     */
    private void getReligion() {
        religionList.clear();
        ArrayList<String> list = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.religionArray)));
        for (int i = 0; i < list.size(); i++) {
            religionList.add(new FlexModel(list.get(i)));
            if (((CreateAccountActivity) getActivity()).getUserData().getRelegion() != null && religionList.get(i).getName().equalsIgnoreCase(((CreateAccountActivity) getActivity()).getUserData().getRelegion())) {
                religionList.get(i).setChecked(true);
                strReligion = religionList.get(i).getName();
                btnContinue.setEnabled(true);
                lastPosition = i;
            }
        }


    }

    @Override
    public void onClick(View view) {
        if (view == btnContinue) {
            if (isNetworkConnected()) {
                hideKeyboard();
                getBaseActivity().showLoading();
                model.verifyRequest(new CreateAccountRelegionModel(strReligion));
            } else {
                getBaseActivity().showSnackbar(btnContinue, "Please connect to internet");
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
    public void OnItemClick(int position) {
        strReligion = religionList.get(position).getName();
        religionList.get(position).setChecked(true);
        if (lastPosition != -1 && lastPosition != position)
            religionList.get(lastPosition).setChecked(false);
        adapter.notifyDataSetChanged();
        lastPosition = position;
        btnContinue.setEnabled(true);
    }

    /**
     * ** Method to Skip Question/Field
     */
    public void skipFragments() {
        strReligion = "";
        btnContinue.performClick();
    }
}