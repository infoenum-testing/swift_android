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
import com.swiftdating.app.model.requestmodel.createaccountmodel.CreateAccountDrinkModel;
import com.swiftdating.app.model.responsemodel.VerificationResponseModel;
import com.swiftdating.app.ui.base.BaseFragment;
import com.swiftdating.app.ui.createAccountScreen.CreateAccountActivity;
import com.swiftdating.app.ui.createAccountScreen.viewmodel.CreateAccountViewModel;

public class DrinkFragment extends BaseFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private Button btnContinue;
    private CreateAccountViewModel model;
    private RadioGroup tgDrink;
    View view;
    private String strDrink;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_drink;
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

    /**
     * Method to handle intent after Api Hit
     */
    private void sendIntent() {
        if (((CreateAccountActivity) Objects.requireNonNull(getActivity())).isEdit) {
            getActivity().finish();
            getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    /**
     * **  Method to Handle api Response
     */
    private void subscribeModel() {
        model = ViewModelProviders.of(this).get(CreateAccountViewModel.class);
        model.drinkResponse().observe(this, new Observer<Resource<VerificationResponseModel>>() {
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
                                ((CreateAccountActivity) getActivity()).updateParseCount(14);
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
        tgDrink = view.findViewById(R.id.tgDrink);
        btnContinue.setOnClickListener(this);
        tgDrink.setOnCheckedChangeListener(this);
        if (!TextUtils.isEmpty(((CreateAccountActivity) getActivity()).getUserData().getDrink())) {
            tgDrink.check(((CreateAccountActivity) getActivity()).getUserData().getDrink().equalsIgnoreCase("Never") ?
                    R.id.nevetbDrink : ((CreateAccountActivity) getActivity()).getUserData().getDrink().equalsIgnoreCase("Often") ? R.id.oftendrinktb
                    : R.id.sociallydrinktb);
            strDrink = ((CreateAccountActivity) getActivity()).getUserData().getDrink();
            btnContinue.setEnabled(true);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btnContinue) {
            if (isNetworkConnected()) {
                getBaseActivity().showLoading();
                hideKeyboard();
                model.verifyRequest(new CreateAccountDrinkModel(strDrink));
            } else {
                getBaseActivity().showSnackbar(btnContinue, "Please connect to internet");
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == tgDrink) {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton radioDrinkButton = view.findViewById(selectedId);
            if (radioDrinkButton != null) {
                btnContinue.setEnabled(true);
                strDrink = radioDrinkButton.getText().toString();
                if (strDrink.equalsIgnoreCase("never")) {
                    strDrink = "Never";
                }
            }
        }
    }

    /**
     * ** Method to Skip Question/Field
     */
    public void skipFragments() {
        strDrink = "";
        btnContinue.performClick();
    }
}
