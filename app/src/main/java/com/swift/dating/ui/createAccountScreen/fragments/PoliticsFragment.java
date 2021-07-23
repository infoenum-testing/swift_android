package com.swift.dating.ui.createAccountScreen.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
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

import com.swift.dating.R;
import com.swift.dating.callbacks.OnItemClickListener;
import com.swift.dating.model.FlexModel;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountPoliticalModel;
import com.swift.dating.model.responsemodel.VerificationResponseModel;
import com.swift.dating.ui.base.BaseFragment;
import com.swift.dating.ui.createAccountScreen.CreateAccountActivity;
import com.swift.dating.ui.createAccountScreen.adapter.FlexAdapter;
import com.swift.dating.ui.createAccountScreen.viewmodel.CreateAccountViewModel;

public class PoliticsFragment extends BaseFragment implements View.OnClickListener, OnItemClickListener {

    private CreateAccountViewModel model;
    private Button btnContinue;
    private FlexAdapter adapter;
    private ArrayList<FlexModel> politicalList = new ArrayList<>();
    private int lastPosition = -1;
    private String strPolitical = "";

    @Override
    public int getLayoutId() {
        return R.layout.fragment_politic;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
        getPolitics();
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
        model.politicalResponse().observe(this, resource -> {
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
        });

    }

    /**
     ***  Method to Initialize
     */
    private void initialize(View view) {
        btnContinue = view.findViewById(R.id.btn_continue);
        btnContinue.setText(((CreateAccountActivity)getActivity()).btn_text);
        btnContinue.setOnClickListener(this);
        if(((CreateAccountActivity) Objects.requireNonNull(getActivity())).isEdit){
            btnContinue.setText("Done");
        }
        RecyclerView rv_political = view.findViewById(R.id.rv_political);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getActivity());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        rv_political.setLayoutManager(layoutManager);

        adapter = new FlexAdapter(getContext(), politicalList);
        adapter.setOnItemClickListener(this);
        rv_political.setAdapter(adapter);

        btnContinue.setOnClickListener(this);

    }

    /***
     *  Method to set Toggle Button for Politics
     */
    private void getPolitics() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.politicalArray)));
        for (int i = 0; i < list.size(); i++) {
            politicalList.add(new FlexModel(list.get(i)));

            if(((CreateAccountActivity) getActivity()).getUserData().getPolitical()!=null &&politicalList.get(i).getName().equalsIgnoreCase(((CreateAccountActivity) getActivity()).getUserData().getPolitical())){
                politicalList.get(i).setChecked(true);
                strPolitical = politicalList.get(i).getName();
                btnContinue.setBackground(getContext().getResources().getDrawable(R.drawable.gradientbtn));
                btnContinue.setEnabled(true);
                lastPosition = i;
                if (((CreateAccountActivity) getActivity()).isEdit) {
                    btnContinue.setText("Done");
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btnContinue) {
            if (isNetworkConnected()) {
                hideKeyboard();
                getBaseActivity().showLoading();
                model.verifyRequest(new CreateAccountPoliticalModel(strPolitical));
            } else {
                getBaseActivity().showSnackbar(btnContinue, "Please connect to internet");
            }
        }
    }

    @Override
    public void OnItemClick(int position) {
        strPolitical = politicalList.get(position).getName();
        politicalList.get(position).setChecked(true);
        if (lastPosition != -1 && lastPosition != position)
            politicalList.get(lastPosition).setChecked(false);
        adapter.notifyDataSetChanged();
        lastPosition = position;
        btnContinue.setEnabled(true);
        btnContinue.setBackground(getContext().getResources().getDrawable(R.drawable.gradientbtn));
    }

    /**
     *** Method to Skip Question/Field
     */
    public void skipFragments() {
        strPolitical = "";
        btnContinue.performClick();
    }
}
