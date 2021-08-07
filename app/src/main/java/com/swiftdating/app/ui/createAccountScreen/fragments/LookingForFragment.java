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
import com.swiftdating.app.model.requestmodel.createaccountmodel.CreateAccoutLookingModel;
import com.swiftdating.app.model.responsemodel.VerificationResponseModel;
import com.swiftdating.app.ui.base.BaseFragment;
import com.swiftdating.app.ui.createAccountScreen.CreateAccountActivity;
import com.swiftdating.app.ui.createAccountScreen.adapter.FlexAdapter;
import com.swiftdating.app.ui.createAccountScreen.viewmodel.CreateAccountViewModel;

public class LookingForFragment extends BaseFragment implements View.OnClickListener, OnItemClickListener {

    CreateAccountViewModel model;
    Button btnContinue;
    View view;

    private FlexAdapter adapter;
    private ArrayList<FlexModel> lookingForList = new ArrayList<>();

    private int lastPosition = -1;
    private String strLookingFor = "";

    @Override
    public int getLayoutId() {
        return R.layout.fragment_lookingfor;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
        getLookingForList();
        subscribeModel();
    }

    @Override
    public void onPause() {
        super.onPause();
        getBaseActivity().hideLoading();
    }

    /**
     *** Method to Skip Question/Field
     */
    public void skipFragments() {
        strLookingFor="";
        btnContinue.performClick();
    }

    /**
     ***  Method to Handle api Response
     */
    private void subscribeModel() {
        model = ViewModelProviders.of(this).get(CreateAccountViewModel.class);
        model.lookingResponse().observe(this, new Observer<Resource<VerificationResponseModel>>() {
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
                            getBaseActivity().hideLoading();
                            getBaseActivity().showSnackbar(btnContinue, resource.data.getMessage());
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
        btnContinue = view.findViewById(R.id.btn_continue);
        btnContinue.setText(((CreateAccountActivity)getActivity()).btn_text);
        btnContinue.setOnClickListener(this);

        if(((CreateAccountActivity) Objects.requireNonNull(getActivity())).isEdit){
            btnContinue.setText("Done");
        }

        RecyclerView rv_kids = view.findViewById(R.id.rv_kids);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getActivity());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        rv_kids.setLayoutManager(layoutManager);

        adapter = new FlexAdapter(getContext(), lookingForList);
        adapter.setOnItemClickListener(this);
        rv_kids.setAdapter(adapter);


    }

    /***
     *  Method to send Toogle Button
     */
    private void getLookingForList() {
        lookingForList.clear();
        ArrayList<String> list = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.lookingArray)));
        for (int i = 0; i < list.size(); i++) {
            lookingForList.add(new FlexModel(list.get(i)));
            if(((CreateAccountActivity) getActivity()).getUserData().getLookingFor()!=null && lookingForList.get(i).getName().equalsIgnoreCase(((CreateAccountActivity) getActivity()).getUserData().getLookingFor())){
                lookingForList.get(i).setChecked(true);
                strLookingFor = lookingForList.get(i).getName();
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
        if(view == btnContinue){
            getBaseActivity().showLoading();
            hideKeyboard();
            model.verifyRequest(new CreateAccoutLookingModel(strLookingFor));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getLookingForList();
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
        strLookingFor = lookingForList.get(position).getName();
        lookingForList.get(position).setChecked(true);
        if (lastPosition != -1 && lastPosition != position)
            lookingForList.get(lastPosition).setChecked(false);
        adapter.notifyDataSetChanged();
        lastPosition = position;
        btnContinue.setEnabled(true);
        btnContinue.setBackground(getContext().getResources().getDrawable(R.drawable.gradientbtn));
    }
}

