package com.swiftdating.app.ui.createAccountScreen.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import java.util.List;
import java.util.Objects;

import com.swiftdating.app.R;
import com.swiftdating.app.common.CommonDialogs;
import com.swiftdating.app.common.CommonUtils;
import com.swiftdating.app.common.ValidationUtils;
import com.swiftdating.app.common.wheelpicker.LoopView;
import com.swiftdating.app.data.network.Resource;
import com.swiftdating.app.model.requestmodel.createaccountmodel.CreateAccountBirthModel;
import com.swiftdating.app.model.responsemodel.ProfileOfUser;
import com.swiftdating.app.model.responsemodel.VerificationResponseModel;
import com.swiftdating.app.ui.base.BaseFragment;
import com.swiftdating.app.ui.createAccountScreen.CreateAccountActivity;
import com.swiftdating.app.ui.createAccountScreen.viewmodel.CreateAccountViewModel;

public class BirthdayFragment extends BaseFragment implements View.OnClickListener {

    private int yearPos, monthPos, dayPos, minYear, maxYear;
    private ArrayList yearList = new ArrayList(), monthList = new ArrayList(), dayList = new ArrayList();
    private CreateAccountViewModel model;
    private FloatingActionButton btnContinue;
    private RelativeLayout rlDays, rlMonth, rlYear;
    TextView tvDayOfBirth, tvMonthOfBirth, tvYearOfBirth;
    private static final String TAG = "BirthdayFragment";

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
    public void onResume() {
        super.onResume();
        setDob();
    }

    private void setDob() {
        try {
            Gson gson = new Gson();
            String user = getBaseActivity().sp.getUser();
            ProfileOfUser obj = gson.fromJson(user, ProfileOfUser.class);
            if (obj != null && !TextUtils.isEmpty(obj.getDob())) {
                String[] arr = obj.getDob().split("-");
                String yearStr = arr[0];
                String monthStr = arr[1];
                String dayStr = arr[2];
                yearPos = yearList.indexOf(yearStr);
                if (monthStr.substring(0, 1).equalsIgnoreCase("0")) {
                    monthStr = monthStr.substring(1);
                }
                List<String> mList = Arrays.asList(getResources().getStringArray(R.array.monthsArray));
                String monthName = mList.get(Integer.parseInt(monthStr) - 1);
                monthPos = monthList.indexOf(monthName);
                dayPos = dayList.indexOf(dayStr);
                tvDayOfBirth.setText(String.valueOf(dayList.get(dayPos)));
                tvMonthOfBirth.setText(String.valueOf(monthList.get(monthPos)));
                tvYearOfBirth.setText(String.valueOf(yearList.get(yearPos)));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
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
        initDayPickerView();

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

            loopView.setListener(item -> {
                yearPos = item;
                tvYearOfBirth.setText(String.valueOf(yearList.get(item)));
            });
        });

        btnContinue.setOnClickListener(this);
    }

    /**
     * ** method to set the Day Picker view
     */
    private void initDayPickerView() {

        int dayMaxInMonth;
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, minYear + yearPos);
        calendar.set(Calendar.MONTH, monthPos);
        //get max day in month
        dayMaxInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < dayMaxInMonth; i++) {
            dayList.add(format2LenStr(i + 1));
        }

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
            loopView.setArrayList(dayList);
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
                tvDayOfBirth.setText(String.valueOf(dayList.get(item)));
                // initDayPickerView();
            });

            btn_done.setOnClickListener((View v1) -> {
                tvDayOfBirth.setText(String.valueOf(dayList.get(dayPos)));
                dialog.cancel();
            });
        });
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
        Log.e("TAG", "onClick: " + " " + dayPos + monthPos + " " + yearPos);
        if (view == btnContinue) {
            if (!tvMonthOfBirth.getText().toString().trim().equalsIgnoreCase("Month") && !tvYearOfBirth.getText().toString().trim().equalsIgnoreCase("Year") && !tvDayOfBirth.getText().toString().trim().equalsIgnoreCase("Date")) {
                Log.e("TAG", "onClick: done date is selected ");
                int year = minYear + yearPos;
                int month = monthPos + 1;
                int day = dayPos + 1;
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
                        Log.e("TAG", "onClick: " + strDate);
                    }, "Please confirm that your birthday is " + strCustomDate + " and you are " + CommonUtils.getAge(strDate) + " years old. Your birthday cannot be changed once registration is complete.");
                }
            } else {
                showSnackBar(btnContinue, "Please select date");
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
                CommonDialogs.alertDialogOneButton(mActivity, getString(R.string.ageAbove18Text));
                return false;
            }
        } else {
            getBaseActivity().showSnackbar(btnContinue, getString(R.string.selectValidDateText));
            return false;
        }
    }
}
