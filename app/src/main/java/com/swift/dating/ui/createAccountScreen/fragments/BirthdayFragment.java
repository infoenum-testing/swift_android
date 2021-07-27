package com.swift.dating.ui.createAccountScreen.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
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
import java.util.Calendar;
import java.util.Objects;

import com.swift.dating.R;
import com.swift.dating.common.CommonDialogs;
import com.swift.dating.common.CommonUtils;
import com.swift.dating.common.ValidationUtils;
import com.swift.dating.common.wheelpicker.LoopView;
import com.swift.dating.data.network.Resource;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountBirthModel;
import com.swift.dating.model.responsemodel.VerificationResponseModel;
import com.swift.dating.ui.base.BaseFragment;
import com.swift.dating.ui.createAccountScreen.CreateAccountActivity;
import com.swift.dating.ui.createAccountScreen.viewmodel.CreateAccountViewModel;

public class BirthdayFragment extends BaseFragment implements View.OnClickListener {

    private int yearPos, monthPos, dayPos, minYear, maxYear;
    private ArrayList yearList = new ArrayList(), monthList = new ArrayList(), dayList = new ArrayList();
    private CreateAccountViewModel model;
    private FloatingActionButton btnContinue;
    private RelativeLayout rlDays, rlMonth, rlYear;
    TextView tvDayOfBirth, tvMonthOfBirth, tvYearOfBirth;

    /**
     * ** method to add 0 before the date less than 10
     */
    public static String format2LenStr(int num) {
        return (num < 10) ? "0" + num : String.valueOf(num);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_birthday;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
        listener();
        subscribeModel();
    }

    @Override
    public void onPause() {
        super.onPause();
        getBaseActivity().hideLoading();
    }

    /**
     * **  Method to Handle api response
     */
    private void subscribeModel() {
        model = ViewModelProviders.of(this).get(CreateAccountViewModel.class);
        model.birthResponse().observe(this, new Observer<Resource<VerificationResponseModel>>() {
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
                                ((CreateAccountActivity) getActivity()).updateParseCount(23);
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
     * **  Method to Handle intent after api hit
     */
    private void sendIntent() {
        if (((CreateAccountActivity) Objects.requireNonNull(getActivity())).isEdit) {
            getActivity().finish();
        } else {
            ((CreateAccountActivity) getActivity()).addFragment();
        }
    }

    /**
     * **  Method to initialize
     */
    private void initialize(View view) {
        rlDays = view.findViewById(R.id.rlDays);
        rlMonth = view.findViewById(R.id.rlMonth);
        rlYear = view.findViewById(R.id.rlYear);
        btnContinue = view.findViewById(R.id.btn_continue);
        tvDayOfBirth = view.findViewById(R.id.tvDayOfBirth);
        tvMonthOfBirth = view.findViewById(R.id.tvMonthOfBirth);
        tvYearOfBirth = view.findViewById(R.id.tvYearOfBirth);

       /* if (((CreateAccountActivity) getActivity()).isEdit) {
            btnContinue.setText("Done");
        }*/
        btnContinue.setEnabled(true);
        btnContinue.setBackground(getContext().getResources().getDrawable(R.drawable.gradientbtn));
        minYear = Calendar.getInstance().get(Calendar.YEAR) - 100;
        maxYear = Calendar.getInstance().get(Calendar.YEAR) + 1;
    }

    /**
     * **  Method to implement Listeners
     */
    private void listener() {
        initPickerViews(); //

        rlDays.setOnClickListener(v -> {
            View view = getLayoutInflater().inflate(R.layout.bottom_sheet_day, null);
            BottomSheetDialog dialog = new BottomSheetDialog(getContext());
            dialog.setContentView(view);
            dialog.show();
            view.setNestedScrollingEnabled(true);
            dialog.getBehavior().setDraggable(false);
            LoopView loopView;
            TextView btn_done;
            loopView = view.findViewById(R.id.picker);
            btn_done = view.findViewById(R.id.btn_done);

            loopView.setNotLoop();
            int dayMaxInMonth;
            Calendar calendar = Calendar.getInstance();
            ArrayList daysList = new ArrayList<String>();

            calendar.set(Calendar.YEAR, minYear + yearPos);
            calendar.set(Calendar.MONTH, monthPos);
            //get max day in month
            dayMaxInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            for (int i = 0; i < dayMaxInMonth; i++) {
                daysList.add(format2LenStr(i + 1));
            }
            loopView.setArrayList(daysList);
            loopView.setInitPosition(dayPos);
            if (((CreateAccountActivity) getActivity()).getUserData() != null && !TextUtils.isEmpty(((CreateAccountActivity) getActivity()).getUserData().getDob())) {
                ((CreateAccountActivity) getActivity()).getUserData().getDob().split("-");
                loopView.setInitPosition(Integer.parseInt(((CreateAccountActivity) getActivity()).getUserData().getDob().split("-")[2]) - 1);
                monthPos = Integer.parseInt(((CreateAccountActivity) getActivity()).getUserData().getDob().split("-")[1]) - 1;
                dayPos = Integer.parseInt(((CreateAccountActivity) getActivity()).getUserData().getDob().split("-")[2]) - 1;
                yearPos = yearList.indexOf(((CreateAccountActivity) getActivity()).getUserData().getDob().split("-")[0]);
            }
            loopView.setListener(item -> {
                dayPos = item;
                tvDayOfBirth.setText(String.valueOf(daysList.get(item)));
                // initDayPickerView();
            });

            btn_done.setOnClickListener((View v1) -> {
                tvDayOfBirth.setText(String.valueOf(daysList.get(dayPos)));
                dialog.cancel();
            });
        });
        /////////////////////// month picker ///////////////////////
        rlMonth.setOnClickListener(v -> {
            View view = getLayoutInflater().inflate(R.layout.bottom_sheet_day, null);
            BottomSheetDialog dialog = new BottomSheetDialog(getContext());
            dialog.setContentView(view);
            dialog.show();
            view.setNestedScrollingEnabled(true);
            dialog.getBehavior().setDraggable(false);
            LoopView loopView;
            TextView btn_done;
            loopView = view.findViewById(R.id.picker);
            btn_done = view.findViewById(R.id.btn_done);
            btn_done.setOnClickListener(v1 -> {
                tvMonthOfBirth.setText(String.valueOf(monthList.get(monthPos)));
                dialog.cancel();
            });
            loopView.setNotLoop();
            loopView.setArrayList(monthList);
            loopView.setInitPosition(monthPos);

            if (((CreateAccountActivity) getActivity()).getUserData() != null && !TextUtils.isEmpty(((CreateAccountActivity) getActivity()).getUserData().getDob())) {
                ((CreateAccountActivity) getActivity()).getUserData().getDob().split("-");
                loopView.setInitPosition(Integer.parseInt(((CreateAccountActivity) getActivity()).getUserData().getDob().split("-")[1]) - 1);
                monthPos = Integer.parseInt(((CreateAccountActivity) getActivity()).getUserData().getDob().split("-")[1]) - 1;
            }
            loopView.setListener(item -> {
                monthPos = item;
                tvMonthOfBirth.setText(String.valueOf(monthList.get(item)));
                // initDayPickerView();
            });
        });


        /////////////////////// Year picker ///////////////////////
        rlYear.setOnClickListener(v -> {
            View view = getLayoutInflater().inflate(R.layout.bottom_sheet_day, null);
            BottomSheetDialog dialog = new BottomSheetDialog(getContext());
            dialog.setContentView(view);
            dialog.show();
            view.setNestedScrollingEnabled(true);
            dialog.getBehavior().setDraggable(false);
            LoopView loopView;
            TextView btn_done;
            loopView = view.findViewById(R.id.picker);
            btn_done = view.findViewById(R.id.btn_done);
            btn_done.setOnClickListener(v1 -> {
                tvYearOfBirth.setText(String.valueOf(yearList.get(yearPos)));
                dialog.cancel();
            });
            loopView.setNotLoop();
            loopView.setArrayList(yearList);
            loopView.setInitPosition(yearPos);

            if (((CreateAccountActivity) getActivity()).getUserData() != null && !TextUtils.isEmpty(((CreateAccountActivity) getActivity()).getUserData().getDob())) {
                ((CreateAccountActivity) getActivity()).getUserData().getDob().split("-");
                loopView.setInitPosition(Integer.parseInt(((CreateAccountActivity) getActivity()).getUserData().getDob().split("-")[1]) - 1);
                yearPos = yearList.indexOf(((CreateAccountActivity) getActivity()).getUserData().getDob().split("-")[0]);
            }
            loopView.setListener(item -> {
                yearPos = item;
                tvYearOfBirth.setText(String.valueOf(yearList.get(item)));
                // initDayPickerView();
            });
        });


        btnContinue.setOnClickListener(this);
        //  initDayPickerView();
    }

    /**
     * ** method to set the Day Picker view
     */
    private void initDayPickerView() {

        int dayMaxInMonth;
        Calendar calendar = Calendar.getInstance();
        ArrayList dayList = new ArrayList<String>();

        calendar.set(Calendar.YEAR, minYear + yearPos);
        calendar.set(Calendar.MONTH, monthPos);
        //get max day in month
        dayMaxInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < dayMaxInMonth; i++) {
            dayList.add(format2LenStr(i + 1));
        }
        // dayLoopView.setArrayList(dayList);
        // dayLoopView.setInitPosition(dayPos);
    }

    /**
     * ** method to set the rest Picker view
     */
    private void initPickerViews() {

        int yearCount = maxYear - minYear;

        for (int i = 0; i < yearCount; i++) {
            yearList.add(format2LenStr(minYear + i));
        }

        monthList.addAll(Arrays.asList(getResources().getStringArray(R.array.monthsArray)));

        // yearLoopView.setArrayList(yearList);
      //  yearPos = yearList.size(); // set year position already at 18 years
        // yearLoopView.setInitPosition(yearPos);

        // monthLoopView.setArrayList(monthList);
        //  monthLoopView.setInitPosition(monthPos);

        if (((CreateAccountActivity) getActivity()).getUserData() != null && !TextUtils.isEmpty(((CreateAccountActivity) getActivity()).getUserData().getDob())) {
            ((CreateAccountActivity) getActivity()).getUserData().getDob().split("-");
            //   monthLoopView.setInitPosition(Integer.parseInt(((CreateAccountActivity) getActivity()).getUserData().getDob().split("-")[1]) - 1);
            //   dayLoopView.setInitPosition(Integer.parseInt(((CreateAccountActivity) getActivity()).getUserData().getDob().split("-")[2]) - 1);
            // yearLoopView.setInitPosition(yearList.indexOf(((CreateAccountActivity) getActivity()).getUserData().getDob().split("-")[0]));

            monthPos = Integer.parseInt(((CreateAccountActivity) getActivity()).getUserData().getDob().split("-")[1]) - 1;
            dayPos = Integer.parseInt(((CreateAccountActivity) getActivity()).getUserData().getDob().split("-")[2]) - 1;
            yearPos = yearList.indexOf(((CreateAccountActivity) getActivity()).getUserData().getDob().split("-")[0]);
        }
    }

    @Override
    public void onClick(View view) {
        Log.e("TAG", "onClick: "+" "+dayPos +monthPos+" "+yearPos);
        if (view == btnContinue) {
            if (((CreateAccountActivity) getActivity()).preference.getIsFromNumber()) {
                ((CreateAccountActivity) getActivity()).updateParseCount(23);
                sendIntent();
            } else {
                int year = minYear + yearPos;
                int month = monthPos + 1;
                int day = dayPos + 1;
                ((CreateAccountActivity) getActivity()).updateParseCount(3);
                if (onDatePickCompleted(year, month, day)) {
                    String strCustomDate = month < 10 ? "0" + month : "" + month;
                    strCustomDate += day < 10 ? "/0" + day : "/" + day;
                    strCustomDate += "/" + ("" + year).charAt(2) + ("" + year).charAt(3);
                    String strDate = year + "-" + month + "-" + day;
                    CommonDialogs.alertDialogToSureBirthDate(mActivity, v -> {
                        CommonDialogs.dismiss();
                        getBaseActivity().showLoading();
                        hideKeyboard();
                        model.verifyRequest(new CreateAccountBirthModel(strDate));
                    }, "Please confirm that your birthday is " + strCustomDate + " and you are " + CommonUtils.getAge(strDate) + " years old. Your birthday cannot be changed once registration is complete.");
                }
            }
        }
    }


    /*
     ***  Method to validate date after selection
     */
    private boolean onDatePickCompleted(int year, int month, int day) {
        String date = year + "-" + format2LenStr(month) + "-" + format2LenStr(day);

        if (ValidationUtils.isValidDate(date)) {
            if (ValidationUtils.checkAgeIsValid(date)) {
                return true;
            } else {
                getBaseActivity().showSnackbar(btnContinue, getString(R.string.ageAbove18Text));
                return false;
            }
        } else {
            getBaseActivity().showSnackbar(btnContinue, getString(R.string.selectValidDateText));
            return false;
        }
    }
}
