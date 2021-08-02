package com.swift.dating.ui.createAccountScreen.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
//import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import com.swift.dating.R;
import com.swift.dating.common.wheelpicker.LoopView;
import com.swift.dating.data.network.Resource;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountLocationModel;
import com.swift.dating.model.responsemodel.VerificationResponseModel;
import com.swift.dating.ui.base.BaseFragment;
import com.swift.dating.ui.createAccountScreen.CreateAccountActivity;
import com.swift.dating.ui.createAccountScreen.viewmodel.CreateAccountViewModel;

public class LocationFragment extends BaseFragment {
    private final String US = "United States";
    private final String TAG = LocationFragment.class.getSimpleName();
    private final String[] us_state_names = new String[]{"Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "IllinoisIndiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "MontanaNebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "PennsylvaniaRhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"};
    private FloatingActionButton btn_continue;
    //private TextView tv_state;
    private EditText edit_city;
    private ArrayList<String> country;
    //  private Spinner spin_country, spin_state;
    private CreateAccountViewModel model;
    TextView tv_country, tv_state;
    private String state = "";
    private String contry = "";
    int countryPos = 0, statePost;
    private ArrayList<String> stateList;
    boolean isUsSelected = false;
    private RelativeLayout rlState, rlCountry;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_location;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        subscribeModel();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (((CreateAccountActivity) getActivity()).getUserData() != null)
            if (!TextUtils.isEmpty(((CreateAccountActivity) getActivity()).getUserData().getCity())) {
                edit_city.setText(((CreateAccountActivity) getActivity()).getUserData().getCity());
            }
        if (((CreateAccountActivity) getActivity()).getUserData() != null && !TextUtils.isEmpty(((CreateAccountActivity) getActivity()).getUserData().getCountry())) {
            contry = ((CreateAccountActivity) getActivity()).getUserData().getCountry();
            for (int i = 0; i < country.size(); i++) {
                if (country.get(i).equalsIgnoreCase(contry)) {
                    countryPos = i;
                    //    spin_country.setSelection(i);
                    tv_country.setText(country.get(countryPos));
                    setVisibility(country.get(countryPos).equalsIgnoreCase(US));
                    // edit_city.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }
        if (((CreateAccountActivity) getActivity()).getUserData() != null && !TextUtils.isEmpty(((CreateAccountActivity) getActivity()).getUserData().getState())) {
            state = ((CreateAccountActivity) getActivity()).getUserData().getState();
            for (int i = 0; i < us_state_names.length; i++) {
                if (us_state_names[i].equalsIgnoreCase(state)) {
                    statePost = i;
                    //     spin_state.setSelection(i);
                    tv_state.setText(stateList.get(statePost));
                    break;
                }
            }
        }
    }

    private void initView(View view) {
        edit_city = view.findViewById(R.id.edit_city);
        rlCountry = view.findViewById(R.id.rlCountry);
        rlState = view.findViewById(R.id.rlState);
        tv_state = view.findViewById(R.id.tv_state);
        tv_country = view.findViewById(R.id.tv_country);

     /*   spin_country = view.findViewById(R.id.spin_country);
        spin_state = view.findViewById(R.id.spin_state);
     */
        country = new ArrayList<>();
        String[] isoCountryCodes = Locale.getISOCountries();
        for (String countryCode : isoCountryCodes) {
            Locale locale = new Locale("", countryCode);
            country.add(locale.getDisplayCountry());
        }
        Collections.sort(country);
        // country.add(0, "Select Country");
        String[] test = new String[]{"Canada", "United Kingdom", "United States"};
        for (String s : test) {
            for (int i = 0; i < country.size(); i++) {
                if (country.get(i).equalsIgnoreCase(s)) {
                    country.remove(i);
                    country.add(0, s);
                    break;
                }
            }
        }
        stateList = new ArrayList<>();
        stateList.addAll(Arrays.asList(us_state_names));

        rlCountry.setOnClickListener(vs -> {
            View v = LocationFragment.this.getLayoutInflater().inflate(R.layout.bottom_sheet_day, null);
            BottomSheetDialog dialog = new BottomSheetDialog(LocationFragment.this.getContext());
            dialog.setContentView(v);
            dialog.show();
            v.setNestedScrollingEnabled(true);
            dialog.getBehavior().setDraggable(false);
            LoopView loopView;
            TextView btn_done;
            loopView = v.findViewById(R.id.picker);
            btn_done = v.findViewById(R.id.btn_done);
            loopView.setNotLoop();
            loopView.setArrayList(country);
            loopView.setInitPosition(countryPos);
            loopView.setListener(item -> {
                countryPos = item;
                tv_country.setText(country.get(countryPos));
                setVisibility(country.get(countryPos).equalsIgnoreCase(US));
            });

            btn_done.setOnClickListener((View v1) -> {
                tv_country.setText(country.get(countryPos));
                dialog.cancel();
                contry = country.get(countryPos);
            });
        });

        rlState.setOnClickListener(vs -> {
            View v = LocationFragment.this.getLayoutInflater().inflate(R.layout.bottom_sheet_day, null);
            BottomSheetDialog dialog = new BottomSheetDialog(LocationFragment.this.getContext());
            dialog.setContentView(v);
            dialog.show();
            v.setNestedScrollingEnabled(true);
            dialog.getBehavior().setDraggable(false);
            LoopView loopView;
            TextView btn_done;
            loopView = v.findViewById(R.id.picker);
            btn_done = v.findViewById(R.id.btn_done);
            loopView.setNotLoop();
            loopView.setArrayList(stateList);
            loopView.setInitPosition(statePost);

            loopView.setListener(item -> {
                statePost = item;
                tv_state.setText(stateList.get(item));
                state = stateList.get(statePost);
            });

            btn_done.setOnClickListener((View v1) -> {
                tv_state.setText(stateList.get(statePost));
                state = stateList.get(statePost);
                dialog.cancel();
            });
        });


        btn_continue = view.findViewById(R.id.btn_continue);
       /* if (((CreateAccountActivity) Objects.requireNonNull(getActivity())).isEdit) {
            btn_continue.setText("Done");
        }*/


        btn_continue.setOnClickListener(v -> {
            if (((CreateAccountActivity) Objects.requireNonNull(getActivity())).isEdit) {
                CreateAccountActivity.location = "" + edit_city.getText().toString() + ", " +country.get(countryPos);
            }
            if (!TextUtils.isEmpty(contry)) {
                //if (spin_state.getVisibility() == View.VISIBLE) {
                if (isUsSelected) {
                    if (!TextUtils.isEmpty(state)) {
                        if (!TextUtils.isEmpty(edit_city.getText().toString())) {
                            hideKeyboard();
                            getBaseActivity().showLoading();
                            model.verifyRequest(new CreateAccountLocationModel(edit_city.getText().toString(), state, contry));
                        } else {
                            showSnackBar(btn_continue, "Please enter city");
                        }
                    } else {
                        showSnackBar(btn_continue, "Please Select State");
                    }
                } else {
                    if (!TextUtils.isEmpty(edit_city.getText().toString())) {
                        hideKeyboard();
                        getBaseActivity().showLoading();
                        model.verifyRequest(new CreateAccountLocationModel(edit_city.getText().toString(), state, contry));
                    } else {
                        showSnackBar(btn_continue, "Please enter city");
                    }
                }
            } else {
                showSnackBar(btn_continue, "Please Select Country");
            }

        });

     /*   ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, country);
        spin_country.setAdapter(adapter);
        spin_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    edit_city.setVisibility(View.VISIBLE);
                    if (country.get(position).equalsIgnoreCase(US)) {
                        edit_city.setVisibility(View.GONE);
                    }
                    setVisibility(country.get(position).equalsIgnoreCase(US));
                    contry = country.get(position);

                } else {
                    edit_city.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spin_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    state = us_state_names[position];
                    edit_city.setVisibility(View.VISIBLE);
                } else {
                    edit_city.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //   tv_state = view.findViewById(R.id.tv_state);
        ArrayList<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(us_state_names));
        ArrayAdapter<String> adapter_state = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, list);
        spin_state.setAdapter(adapter_state);*/


    }

    private void setVisibility(boolean visible) {
        if (visible) {
            isUsSelected = true;
            rlState.setVisibility(View.VISIBLE);
        } else {
            isUsSelected = false;
            rlState.setVisibility(View.GONE);
        }

    }

    private void subscribeModel() {
        model = ViewModelProviders.of(this).get(CreateAccountViewModel.class);
        model.locationResponse().observe(this, new Observer<Resource<VerificationResponseModel>>() {
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
                                if (((CreateAccountActivity) Objects.requireNonNull(getActivity())).isEdit) {
                                    ((CreateAccountActivity) getActivity()).onBackPressed();
                                } else {
                                    ((CreateAccountActivity) getActivity()).updateParseCount(3);
                                    ((CreateAccountActivity) getActivity()).addFragment();
                                }
                            }
                        } else {
                            getBaseActivity().hideLoading();
                            getBaseActivity().showSnackbar(btn_continue, resource.data.getMessage());
                            if (resource.data.getError() != null && resource.data.getError().getCode().equalsIgnoreCase("401"))
                                getBaseActivity().openActivityOnTokenExpire();
                        }

                        break;
                    case ERROR:
                        getBaseActivity().hideLoading();
                        getBaseActivity().showSnackbar(btn_continue, resource.message);
                        break;
                }
            }
        });
    }

}