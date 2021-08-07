package com.swift.dating.ui.createAccountScreen.fragments;

import android.os.Bundle;
import android.util.Log;
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

import com.swift.dating.R;
import com.swift.dating.callbacks.OnItemClickListener;
import com.swift.dating.data.network.Resource;
import com.swift.dating.model.FlexModel;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountKidsModel;
import com.swift.dating.model.responsemodel.VerificationResponseModel;
import com.swift.dating.ui.base.BaseFragment;
import com.swift.dating.ui.createAccountScreen.CreateAccountActivity;
import com.swift.dating.ui.createAccountScreen.adapter.FlexAdapter;
import com.swift.dating.ui.createAccountScreen.viewmodel.CreateAccountViewModel;

public class KidsFragment extends BaseFragment implements View.OnClickListener, OnItemClickListener {

    private CreateAccountViewModel model;
    private Button btnContinue;
    private FlexAdapter adapter;
    private ArrayList<FlexModel> kidsList = new ArrayList<>();
    private int lastPosition = -1;
    private String strKids = "";

    @Override
    public int getLayoutId() {
        return R.layout.fragment_kids;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initialize(view);
        getKids();
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
        if (!model.KidsResponse().hasObservers()) {
            subscribeModel();
        }
    }

    /**
     * **  Method to Handle api Response
     */
    private void subscribeModel() {
        model = ViewModelProviders.of(this).get(CreateAccountViewModel.class);
        model.KidsResponse().observe(this, new Observer<Resource<VerificationResponseModel>>() {
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
                                ((CreateAccountActivity) getActivity()).updateParseCount(7);
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
        btnContinue.setText(((CreateAccountActivity) getActivity()).btn_text);
        btnContinue.setOnClickListener(this);

        if (((CreateAccountActivity) Objects.requireNonNull(getActivity())).isEdit) {
            btnContinue.setText("Done");
        }

        RecyclerView rv_kids = view.findViewById(R.id.rv_kids);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getActivity());
        layoutManager.setFlexDirection(FlexDirection.COLUMN);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        rv_kids.setLayoutManager(layoutManager);

        adapter = new FlexAdapter(getContext(), kidsList);
        adapter.setOnItemClickListener(this);
        rv_kids.setAdapter(adapter);


    }

    /***
     *  Method to set Toogle Button
     */
    private void getKids() {
        kidsList.clear();
        ArrayList<String> list = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.kidsArray)));
        for (int i = 0; i < list.size(); i++) {
            kidsList.add(new FlexModel(list.get(i)));
            if (((CreateAccountActivity) getActivity()).getUserData().getKids() != null && kidsList.get(i).getName().equalsIgnoreCase(((CreateAccountActivity) getActivity()).getUserData().getKids())) {
                kidsList.get(i).setChecked(true);
                strKids = kidsList.get(i).getName();
                btnContinue.setEnabled(true);
                lastPosition = i;
                if (((CreateAccountActivity) getActivity()).isEdit) {
                    btnContinue.setText("Done");
                }
            }
        }


    }

    private static final String TAG = "KidsFragment";

    @Override
    public void onClick(View view) {
        if (view == btnContinue) {
            getBaseActivity().showLoading();
            hideKeyboard();
            Log.d(TAG, "onClick: " + strKids);
            model.verifyRequest(new CreateAccountKidsModel(strKids));
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
        strKids = kidsList.get(position).getName();
        kidsList.get(position).setChecked(true);
        if (lastPosition != -1 && lastPosition != position)
            kidsList.get(lastPosition).setChecked(false);
        adapter.notifyDataSetChanged();
        lastPosition = position;
        btnContinue.setEnabled(true);
    }

    /**
     * ** Method to Skip Question/Field
     */
    public void skipFragments() {
        strKids = "";
        btnContinue.performClick();
    }
}
