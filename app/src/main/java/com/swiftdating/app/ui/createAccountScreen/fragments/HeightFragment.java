package com.swiftdating.app.ui.createAccountScreen.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import com.swiftdating.app.R;
import com.swiftdating.app.common.wheelpicker.LoopView;
import com.swiftdating.app.data.network.Resource;
import com.swiftdating.app.model.requestmodel.createaccountmodel.CreateAccountHeightModel;
import com.swiftdating.app.model.responsemodel.VerificationResponseModel;
import com.swiftdating.app.ui.base.BaseFragment;
import com.swiftdating.app.ui.createAccountScreen.CreateAccountActivity;
import com.swiftdating.app.ui.createAccountScreen.viewmodel.CreateAccountViewModel;

public class HeightFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "HeightFragment";
    Button btnContinue;
    CreateAccountViewModel model;
    String strHeight = "";
    boolean clickOnSkip = false;
    private int selectedPos;
    private ArrayList<String> heightDigitlist;
    private final ArrayList<String> heightList = new ArrayList<>();
    private final String[] heightArray = {"< 4'0", "4'0\" (122 cm)", "4'1\" (125 cm)", "4'2\" (127 cm)", "4'3\" (130 cm)", "4'4\" (132 cm)",
            "4'5\" (135 cm)", "4'6\" (137 cm)", "4'7\" (140 cm)", "4'8\" (142 cm)", "4'9\" (144 cm)", "4'10\" (146 cm)",
            "4'11\" (150 cm)", "5'0\" (152 cm)", "5'1\" (155 cm)", "5'2\" (157 cm)", "5'3\" (160 cm)", "5'4\" (163 cm)",
            "5'5\" (165 cm)", "5'6\" (168 cm)", "5'7\" (170 cm)", "5'8\" (173 cm)", "5'9\" (175 cm)", "5'10\" (178 cm)",
            "5'11\" (180 cm)", "6'0\" (183 cm)", "6'1\" (185 cm)", "6'2\" (188 cm)", "6'3\" (191 cm)", "6'4\" (193 cm)",
            "6'5\" (196 cm)", "6'6\" (198 cm)", "6'7\" (200 cm)", "6'8\" (203 cm)", "6'9\" (206 cm)", "6'10\" (208 cm)",
            "6'11\" (211 cm)", "7'0\" (213 cm)", "> 7’0"};
    private final String[] arraydigit= { "3.9","4.0","4.1","4.2","4.3","4.4",
            "4.5","4.6","4.7","4.8","4.9","4.10",
            "4.11","5.0","5.1","5.2","5.3","5.4",
            "5.5","5.6","5.7","5.8","5.9","5.10",
            "5.11","6.0","6.1","6.2","6.3","6.4",
            "6.5","6.6","6.7","6.8","6.9","6.10",
            "6.11","7.0","7.1"};
    /*private float[] arraydigit = {3.9f, 4.0f, 4.1f, 4.2f, 4.3f, 4.4f, 4.5f, 4.6f, 4.7f, 4.8f, 4.9f, 4.10f, 4.11f, 5.0f, 5.1f, 5.2f, 5.3f, 5.4f, 5.5f, 5.6f, 5.7f, 5.8f, 5.9f, 5.10f, 5.11f, 6.0f, 6.1f, 6.2f, 6.3f, 6.4f, 6.5f, 6.6f, 6.7f, 6.8f, 6.9f
            , 6.10f, 6.11f, 7.0f, 7.1f};*/

    @Override
    public int getLayoutId() {
        return R.layout.fragment_height;
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

    /**
     * **  Method to Handle api Response
     */
    private void subscribeModel() {
        model = ViewModelProviders.of(this).get(CreateAccountViewModel.class);

        model.heightResponse().observe(this, new Observer<Resource<VerificationResponseModel>>() {
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
                                ((CreateAccountActivity) getActivity()).updateParseCount(6);
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
        heightDigitlist = new ArrayList<>();
       /* for (int i = 4; i <= 6; i++)
            for (int j = 0; j <= 11; j++) heightDigitlist.add(Float.parseFloat("" + i + "." + j));
        heightDigitlist.add(7.0f);
        heightDigitlist.add(7.1f);
        heightDigitlist.add(0, 3.9f);*/
        Collections.addAll(heightDigitlist, arraydigit);
        LoopView pickerArray = view.findViewById(R.id.picker_height);
        pickerArray.setNotLoop();
        pickerArray.setListener(item -> {
            selectedPos = item;
        });

        heightList.addAll(Arrays.asList(heightArray));

        Log.e(TAG, "initialize: " + heightDigitlist.size() + "    " + heightList.size());
        pickerArray.setArrayList(heightList);
        pickerArray.setInitPosition(selectedPos);

        btnContinue = view.findViewById(R.id.btn_continue);
        btnContinue.setText(((CreateAccountActivity) getActivity()).btn_text);
        btnContinue.setOnClickListener(this);

        btnContinue.setEnabled(true);
        if (!TextUtils.isEmpty(((CreateAccountActivity) getActivity()).getUserData().getHeight())) {
            String het=((CreateAccountActivity) getActivity()).getUserData().getHeight();
            if (!TextUtils.isEmpty(het)){
                for (int i = 0; i <heightDigitlist.size() ; i++) {
                    if (het.equalsIgnoreCase(""+heightDigitlist.get(i))){
                        het=heightArray[i];
                        //selectedPos=i;
                        break;
                    }
                }
            }
            selectedPos = heightList.indexOf(het);
            pickerArray.setInitPosition(selectedPos);
            btnContinue.setEnabled(true);
            if (((CreateAccountActivity) getActivity()).isEdit) {
                btnContinue.setText("Done");
            }
        }


    }

    /***
     *  Method to send Intent on Api Hit
     */
    private void sendIntent() {
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View view) {
        if (view == btnContinue) {
            hideKeyboard();
            getBaseActivity().showLoading();
            if (clickOnSkip) {
                strHeight = "";
            } else
                strHeight = "" + heightDigitlist.get(selectedPos);
            Log.e(TAG, "onClick: " + strHeight);
            model.verifyRequest(new CreateAccountHeightModel(strHeight));
        }
    }

    /**
     * ** Method to Skip Question/Field
     */
    public void skipFragments() {
        strHeight = "";
        clickOnSkip = true;
        btnContinue.performClick();
    }

}