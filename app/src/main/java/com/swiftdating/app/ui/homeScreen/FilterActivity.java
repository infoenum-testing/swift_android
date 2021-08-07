package com.swiftdating.app.ui.homeScreen;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.swiftdating.app.R;
import com.swiftdating.app.callbacks.OnItemClickListener;
import com.swiftdating.app.common.CommonDialogs;
import com.swiftdating.app.common.CommonUtils;
import com.swiftdating.app.common.RangeSeekBar;
import com.swiftdating.app.common.wheelpicker.LoopListener;
import com.swiftdating.app.data.preference.SharedPreference;
import com.swiftdating.app.model.FlexModel;
import com.swiftdating.app.model.requestmodel.DeluxeTokenCountModel;
import com.swiftdating.app.model.requestmodel.FilterRequest;
import com.swiftdating.app.ui.base.BaseActivity;
import com.swiftdating.app.ui.createAccountScreen.adapter.FlexAdapter;
import com.swiftdating.app.ui.homeScreen.viewmodel.HomeViewModel;

import static com.swiftdating.app.common.AppConstants.LICENSE_KEY;

public class FilterActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, OnSeekChangeListener, OnItemClickListener, View.OnScrollChangeListener, LoopListener, CommonDialogs.onProductConsume, BillingProcessor.IBillingHandler {
    private static final String TAG = "FilterActivity";
    int noPr;
    private ImageView img_close;
    private BottomSheetDialog bottomSheetDialog;
    private RelativeLayout rl_distance, rl_gender, rl_age, rl_relation, rl_height, rl_education, rl_child, rl_politic, rl_religion, rl_smoke;
    private Context context;
    private View view;
    private RelativeLayout rl_close;
    private Button btnContinue, btn_reset, btn_apply;
    private TextView tvDistance, tv_dis, tv_gender, tv_smoke, tv_religion, tv_politics, tv_child, tv_education, tv_height, tv_relation, tv_age;
    private ArrayList<String> ageArray, heightlist;
    private ArrayList<String> heightDigitlist;
    private ArrayList<FlexModel> relationlist = new ArrayList<>(), educationlist = new ArrayList<>(), childernlist = new ArrayList<>(), politicalList = new ArrayList<>(), religionList = new ArrayList<>(), smokeList = new ArrayList<>();
    private FlexAdapter adapter;
    private int lastPosition;
    private Type type;
    private boolean[] selectBool;
    private FlexboxLayoutManager layoutManager;
    private int count;
    private String txt = "";
    private FilterRequest filterRequest;
    private String txt2;
    private boolean isReset;
    private BillingProcessor bp;
    private String productId, tokenSType;
    private String[] arraydigit= { "3.9","4.0","4.1","4.2","4.3","4.4", "4.5","4.6","4.7","4.8","4.9","4.10", "4.11","5.0","5.1","5.2","5.3","5.4", "5.5","5.6","5.7","5.8","5.9","5.10", "5.11","6.0","6.1","6.2","6.3","6.4", "6.5","6.6","6.7","6.8","6.9","6.10", "6.11","7.0","7.1"};
    /* public FilterRequest(Integer pageNumber, Integer limit, Integer distance, String gender, Integer maxAgePrefer, Integer minAgePrefer, String lookingFor, String maxHeight, String minHeight, String education, String kids, String political, String religion, String smoke) {
          this.pageNumber = pageNumber;
          this.limit = limit;
          this.distance = distance;
          this.gender = gender;
          this.maxAgePrefer = maxAgePrefer;
          this.minAgePrefer = minAgePrefer;
          this.lookingFor = lookingFor;
          this.maxHeight = maxHeight;
          this.minHeight = minHeight;
          this.education = education;
          this.kids = kids;
          this.political = political;
          this.religion = religion;
          this.smoke = smoke;
      }*/
    private double price;
    private HomeViewModel homeViewModel;
    private int selectedPosition;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        initViews();
        initBillingProcess();
    }

    /**
     * **  Method to Initialize Billing Process
     */
    private void initBillingProcess() {
        bp = new BillingProcessor(this, LICENSE_KEY, this);
        bp.initialize();
    }

    private void initViews() {
        isReset = false;
        sp = new SharedPreference(this);
        context = this;
        btn_apply = findViewById(R.id.btn_apply);
        tv_dis = findViewById(R.id.tv_dis);
        tv_age = findViewById(R.id.tv_age);
        tv_gender = findViewById(R.id.tv_gender);
        tv_relation = findViewById(R.id.tv_relation);
        tv_height = findViewById(R.id.tv_height);
        tv_height.setText("No Preference");//< 4'0" to > 7'0"
        tv_education = findViewById(R.id.tv_education);
        tv_child = findViewById(R.id.tv_child);
        tv_politics = findViewById(R.id.tv_politics);
        tv_religion = findViewById(R.id.tv_religion);
        tv_smoke = findViewById(R.id.tv_smoke);
        img_close = findViewById(R.id.img_close);
        img_close.setOnClickListener(this::onClick);
        rl_distance = findViewById(R.id.rl_distance);
        rl_gender = findViewById(R.id.rl_gender);
        rl_age = findViewById(R.id.rl_age);
        rl_relation = findViewById(R.id.rl_relation);
        rl_height = findViewById(R.id.rl_height);
        rl_education = findViewById(R.id.rl_education);
        rl_politic = findViewById(R.id.rl_politic);
        rl_religion = findViewById(R.id.rl_religion);
        rl_smoke = findViewById(R.id.rl_smoke);
        rl_child = findViewById(R.id.rl_child);
        btn_reset = findViewById(R.id.btn_reset);
        if (sp.getFilterModel() != null) {
            if (!sp.getDeluxe()) {
                sp.removeFilter();
            } else {
                filterRequest = sp.getFilterModel();
                setDataFromPreference();
            }
        }
        rl_distance.setOnClickListener(this::onClick);
        btn_apply.setOnClickListener(this::onClick);
        rl_gender.setOnClickListener(this::onClick);
        rl_age.setOnClickListener(this::onClick);
        rl_smoke.setOnClickListener(this::onClick);
        rl_religion.setOnClickListener(this::onClick);
        rl_politic.setOnClickListener(this::onClick);
        rl_education.setOnClickListener(this::onClick);
        rl_height.setOnClickListener(this::onClick);
        rl_child.setOnClickListener(this::onClick);
        rl_relation.setOnClickListener(this::onClick);
        btn_reset.setOnClickListener(this::onClick);
        bottomSheetDialog = new BottomSheetDialog(context);
        ageArray = new ArrayList<>();
        heightlist = new ArrayList<>();
        heightDigitlist = new ArrayList<>();
        for (int i = 18; i <= 80; i++) ageArray.add("" + i);
        for (int i = 4; i <= 6; i++) {
            for (int j = 0; j <= 11; j++) {
                heightlist.add("" + i + "'" + j + "\"");
            }
        }
        heightlist.add("7'0\"");
        heightlist.add("> 7'0\"");
        heightlist.add(0, "< 4'0\"");
        /*for (int i = 4; i <= 6; i++)
            for (int j = 0; j <= 11; j++) {
                if (j==10){
                    heightDigitlist.add(Float.parseFloat("" + i + "." + j));
                }else
                heightDigitlist.add(Float.parseFloat("" + i + "." + j));
            }
        heightDigitlist.add(7.0f);
        heightDigitlist.add(7.1f);
        heightDigitlist.add(0, 3.9f);*/
        Collections.addAll(heightDigitlist, arraydigit);
        ArrayList<String> list = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.relationArray)));
        for (int i = 0; i < list.size(); i++) relationlist.add(new FlexModel(list.get(i)));
        list = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.educationArray2)));
        for (int i = 0; i < list.size(); i++) educationlist.add(new FlexModel(list.get(i)));
        list = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.kidsArray2)));
        for (int i = 0; i < list.size(); i++) childernlist.add(new FlexModel(list.get(i)));
        list = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.politicalArray2)));
        for (int i = 0; i < list.size(); i++) politicalList.add(new FlexModel(list.get(i)));
        list = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.religionArray2)));
        for (int i = 0; i < list.size(); i++) religionList.add(new FlexModel(list.get(i)));
        list = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.smokingArray)));
        for (int i = 0; i < list.size(); i++) smokeList.add(new FlexModel(list.get(i)));
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        homeViewModel.addDeluxeResponse().observe(this, resource -> {
            if (resource == null)
                return;
            switch (resource.status) {
                case LOADING:
                    break;
                case SUCCESS:
                    hideLoading();
                    if (resource.data.getSuccess()) {
                        sp.saveDeluxe(true);
                    } else if (resource.code == 401) {
                        openActivityOnTokenExpire();
                    } else {
                        showSnackbar(btn_apply, "Something went wrong");
                    }
                    break;
                case ERROR:
                    hideLoading();
                    showSnackbar(btn_apply, resource.message);
                    break;
            }

        });
    }

    private void setDataFromPreference() {
        FilterActivity.this.tv_dis.setText("" + filterRequest.getDistance() + " miles");
        if (filterRequest.getSmoke() != null) {
            FilterActivity.this.tv_smoke.setText("" + getSmallName(filterRequest.getSmoke()));
        }
        if (filterRequest.getReligion() != null) {
            FilterActivity.this.tv_religion.setText("" + getSmallName(filterRequest.getReligion()));
        }
        if (filterRequest.getPolitical() != null) {
            FilterActivity.this.tv_politics.setText("" + getSmallName(filterRequest.getPolitical()));
        }
        if (filterRequest.getKids() != null) {
            FilterActivity.this.tv_child.setText("" + getSmallName(filterRequest.getKids()));
        }
        if (filterRequest.getEducation() != null) {
            FilterActivity.this.tv_education.setText("" + getSmallName(filterRequest.getEducation()));
        }
        if (filterRequest.getLookingFor() != null) {
            FilterActivity.this.tv_relation.setText("" + getSmallName(filterRequest.getLookingFor()));
        }
        //if (filterRequest.getGender() != null) {
        if (filterRequest.getGender().equalsIgnoreCase("Both")) {
            FilterActivity.this.tv_gender.setText("Everyone");
        } else
            FilterActivity.this.tv_gender.setText("" + filterRequest.getGender());
        //}
        if (filterRequest.getMaxHeight() != null && filterRequest.getMinHeight() != null) {
            String maxH = filterRequest.getMaxHeight().equalsIgnoreCase("7.1") ? "> 7'0\"" : ("" + filterRequest.getMaxHeight()).replace(".", "'") + "\"";
            String minH = filterRequest.getMinHeight().equalsIgnoreCase("3.9") ? "< 4'0\"" : ("" + filterRequest.getMinHeight()).replace(".", "'") + "\"";
            //FilterActivity.this.tv_height.setText(("" + filterRequest.getMinHeight()).replace(".", "'") + "\"" + " to " + ("" + filterRequest.getMaxHeight()).replace(".", "'") + "\"");
            FilterActivity.this.tv_height.setText(minH + " to " + maxH);
        }
        FilterActivity.this.tv_age.setText("" + filterRequest.getMinAgePrefer() + " to " + filterRequest.getMaxAgePrefer());
    }

    private String getSmallName(String name) {
        String[] n1 = name.split(",", 2);
        return n1.length == 2 ? n1[0] + "..." : n1[0];
    }

    private void showGenderBottomsheet() {
        view = LayoutInflater.from(context).inflate(R.layout.fragment_gender, null);
        bottomSheetDialog.setContentView(view);
        RadioGroup tgGender = view.findViewById(R.id.tgGender);
        tgGender.setOnCheckedChangeListener(this);
        TextView tv_gender = view.findViewById(R.id.tv_gender);
       // RadioButton bothTb = view.findViewById(R.id.bothTb);
        RadioButton otherTb = view.findViewById(R.id.otherTb);
        RadioButton tbMale = view.findViewById(R.id.tbMale);
        RadioButton femaleTb = view.findViewById(R.id.femaleTb);
        otherTb.setText("Everyone");
        tbMale.setText("Men");
        femaleTb.setText("Women");
        if (filterRequest != null && filterRequest.getGender() != null) {
            switch (filterRequest.getGender()) {
                case "Men":
                    tbMale.setChecked(true);
                    break;
                case "Women":
                    femaleTb.setChecked(true);
                    break;
                case "Both":
                    otherTb.setChecked(true);
                    break;
            }
        }
        //bothTb.setVisibility(View.VISIBLE);
        setupCrossandContinuebtn(view);
        btnContinue.setOnClickListener(v -> {
            if (filterRequest == null) {
                filterRequest = new FilterRequest();
            }
            switch (tgGender.getCheckedRadioButtonId()) {
                case R.id.tbMale:
                    FilterActivity.this.tv_gender.setText("Men");
                    filterRequest.setGender(FilterActivity.this.tv_gender.getText().toString());
                    break;
                case R.id.femaleTb:
                    FilterActivity.this.tv_gender.setText("Women");
                    filterRequest.setGender(FilterActivity.this.tv_gender.getText().toString());
                    break;
                case R.id.otherTb:
                    FilterActivity.this.tv_gender.setText("Everyone");
                    filterRequest.setGender("Both");
                    break;
            }
//            filterRequest.setGender(FilterActivity.this.tv_gender.getText().toString());
            sp.saveFilterModel(filterRequest);
            bottomSheetDialog.cancel();
        });
        rl_close.setVisibility(View.VISIBLE);
        tv_gender.setText("Select Gender");
        bottomSheetDialog.show();
    }

    private void showDistanceBottomsheet() {
        view = LayoutInflater.from(context).inflate(R.layout.distance_bottomsheet_layout, null);
        bottomSheetDialog.setContentView(view);
        tvDistance = view.findViewById(R.id.tvDistance);
        tvDistance.setText("500 miles");
        IndicatorSeekBar seekDistance = view.findViewById(R.id.seek_distance);
        seekDistance.setOnSeekChangeListener(this);
        setupCrossandContinuebtn(view);
        if (filterRequest != null) {
            seekDistance.setProgress(filterRequest.getDistance());
            tvDistance.setText("" + filterRequest.getDistance() + " miles");
        } else {
            seekDistance.setProgress(500);
        }
        btnContinue.setOnClickListener(v -> {
            FilterActivity.this.tv_dis.setText("" + seekDistance.getProgress() + " miles");
            if (filterRequest == null) {
                filterRequest = new FilterRequest();
            }
            filterRequest.setDistance(seekDistance.getProgress());
            sp.saveFilterModel(filterRequest);
            bottomSheetDialog.cancel();
        });
        bottomSheetDialog.show();
    }

    private void showAgeBottomsheet(ArrayList<String> list, String title) {
        view = LayoutInflater.from(context).inflate(R.layout.age_bottomsheet_layout, null);
        bottomSheetDialog.setContentView(view);
        // LoopView picker_age = view.findViewById(R.id.picker_age);
        // LoopView picker_age_to = view.findViewById(R.id.picker_age_to);
        RangeSeekBar seekAgeRange = view.findViewById(R.id.sb_age);
        TextView tvRange = view.findViewById(R.id.tvRange);
        setupCrossandContinuebtn(view);
        seekAgeRange.setRangeValues(0, list.size() - 1);
        seekAgeRange.setSelectedMinValue(0);
        seekAgeRange.setSelectedMaxValue(list.size() - 1);
        seekAgeRange.setOnRangeSeekBarChangeListener((bar, minValue, maxValue) -> {
            try {
                tvRange.setText(list.get((int) minValue) + " to " + list.get((int) maxValue));
            } catch (Exception e) {
                Log.e(TAG, "showAgeBottomsheet: " + e.toString());
            }
        });
        // picker_age.setNotLoop();
        TextView tv_age = view.findViewById(R.id.tv_age);
        tv_age.setText(title);
      /*  picker_age_to.setNotLoop();
        picker_age.setArrayList(list);
        picker_age.setListener(this);
        picker_age_to.setListener(this);
        picker_age.setInitPosition(0);
        picker_age_to.setArrayList(list);
        picker_age_to.setInitPosition(list.size() - 1);*/
        if (filterRequest != null) {
            if (!title.equalsIgnoreCase("Select Height")) {
                for (int i = 0; i < ageArray.size(); i++) {
                    if (ageArray.get(i).equalsIgnoreCase("" + filterRequest.getMinAgePrefer())) {
                        seekAgeRange.setSelectedMinValue(i);
                        //  picker_age.setInitPosition(i);
                        break;
                    }
                }
                for (int i = 0; i < ageArray.size(); i++) {
                    if (ageArray.get(i).equalsIgnoreCase("" + filterRequest.getMaxAgePrefer())) {
                        seekAgeRange.setSelectedMaxValue(i);
                        //   picker_age_to.setInitPosition(i);
                        break;
                    }
                }
                tvRange.setText(list.get((int) seekAgeRange.getSelectedMinValue()) + " to " + list.get((int) seekAgeRange.getSelectedMaxValue()));
            } else {
                for (int i = 0; i < heightlist.size(); i++) {
                    if (heightlist.get(i).equalsIgnoreCase(("" + filterRequest.getMinHeight()).replace(".", "'") + "\"")) {
                        seekAgeRange.setSelectedMinValue(i);
                        //  picker_age.setInitPosition(i);
                        break;
                    }
                }
                for (int i = 0; i < heightlist.size(); i++) {
                    if (heightlist.get(i).equalsIgnoreCase(("" + filterRequest.getMaxHeight()).replace(".", "'") + "\"")) {
                        seekAgeRange.setSelectedMaxValue(i);
                        //   picker_age_to.setInitPosition(i);
                        break;
                    }
                }
            }
        }

        btnContinue.setOnClickListener(v -> {
            if (filterRequest == null) filterRequest = new FilterRequest();
            if (title.equalsIgnoreCase("Select Height")) {
                FilterActivity.this.tv_height.setText("" + list.get((int) seekAgeRange.getSelectedMinValue()) + " to " + list.get((int) seekAgeRange.getSelectedMaxValue()));
                /*if (picker_age.getSelectedItem() > picker_age_to.getSelectedItem()) {
                    filterRequest.setMaxHeight("" + heightDigitlist.get(picker_age.getSelectedItem()));
                    filterRequest.setMinHeight("" + heightDigitlist.get(picker_age_to.getSelectedItem()));
                } else {*/
                filterRequest.setMaxHeight("" + heightDigitlist.get((int) seekAgeRange.getSelectedMaxValue()));
                filterRequest.setMinHeight("" + heightDigitlist.get((int) seekAgeRange.getSelectedMinValue()));
                //}
            } else {
                FilterActivity.this.tv_age.setText("" + FilterActivity.this.ageArray.get((int) seekAgeRange.getSelectedMinValue()) + " to " + FilterActivity.this.ageArray.get((int) seekAgeRange.getSelectedMaxValue()));
               /* if (picker_age.getSelectedItem() > picker_age_to.getSelectedItem()) {
                    filterRequest.setMaxAgePrefer(Integer.parseInt(FilterActivity.this.ageArray.get(picker_age.getSelectedItem())));
                    filterRequest.setMinAgePrefer(Integer.parseInt(FilterActivity.this.ageArray.get(picker_age_to.getSelectedItem())));
                } else {*/
                filterRequest.setMaxAgePrefer(Integer.parseInt(FilterActivity.this.ageArray.get((int) seekAgeRange.getSelectedMaxValue())));
                filterRequest.setMinAgePrefer(Integer.parseInt(FilterActivity.this.ageArray.get((int) seekAgeRange.getSelectedMinValue())));
                //}
            }
            sp.saveFilterModel(filterRequest);
            bottomSheetDialog.cancel();
        });
        bottomSheetDialog.show();
    }

    private void showRelationBottomsheet() {
        type = Type.RELATION;
        //lastPosition = 0;
        selectBool = new boolean[relationlist.size()];
        Arrays.fill(selectBool, false);
        if (filterRequest != null && filterRequest.getLookingFor() != null) {
            for (String value : filterRequest.getLookingFor().split(",")) {
                for (int j = 0; j < relationlist.size(); j++) {
                    if (relationlist.get(j).getName().equalsIgnoreCase(value)) {
                        selectBool[j] = true;
                        break;
                    }
                }
            }
        }
        // selectBool[0] = true;
        for (int i = 0; i < selectBool.length; i++) relationlist.get(i).setChecked(selectBool[i]);
        view = LayoutInflater.from(context).inflate(R.layout.relation_bottomsheet_layout, null);
        bottomSheetDialog.setContentView(view);
        RecyclerView rv_education = view.findViewById(R.id.rv_relation);
        layoutManager = new FlexboxLayoutManager(view.getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setAlignItems(AlignItems.CENTER);
        rv_education.setLayoutManager(layoutManager);
        adapter = new FlexAdapter(context, relationlist);
        adapter.setOnItemClickListener(this);
        rv_education.setAdapter(adapter);
        setupCrossandContinuebtn(view);
        btnContinue.setOnClickListener(v -> {
            txt = "";
            txt2 = "";
            for (int i = 0; i < selectBool.length; i++) {
                if (selectBool[i]) {
                    if (TextUtils.isEmpty(txt)) {
                        txt = relationlist.get(i).getName();
                        txt2 = relationlist.get(i).getName();
                    } else {
                        if (!txt.contains("..."))
                            txt += "...";
                        txt2 += "," + relationlist.get(i).getName();
                        // break;
                    }
                }
            }
            if (filterRequest == null) {
                filterRequest = new FilterRequest();
            }
            filterRequest.setLookingFor(!txt2.equalsIgnoreCase("No Preference") ? txt2 : null);
            sp.saveFilterModel(filterRequest);
            FilterActivity.this.tv_relation.setText(txt);
            bottomSheetDialog.cancel();
        });
        changeBtnBackground();
        bottomSheetDialog.show();
    }

    private void showEducationBottomsheet() {
        type = Type.EDUCATION;
        lastPosition = 0;
        selectBool = new boolean[educationlist.size()];
        Arrays.fill(selectBool, false);
        if (filterRequest != null && filterRequest.getEducation() != null) {
            for (String value : filterRequest.getEducation().split(",")) {
                for (int j = 0; j < educationlist.size(); j++) {
                    if (educationlist.get(j).getName().equalsIgnoreCase(value)) {
                        selectBool[j] = true;
                        break;
                    }
                }
            }
        }
        //selectBool[0] = true;
        for (int i = 0; i < selectBool.length; i++) educationlist.get(i).setChecked(selectBool[i]);
        view = LayoutInflater.from(context).inflate(R.layout.education_bottomsheet_layout, null);
        bottomSheetDialog.setContentView(view);
        RecyclerView rv_education = view.findViewById(R.id.rv_education);
        layoutManager = new FlexboxLayoutManager(view.getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        rv_education.setLayoutManager(layoutManager);
        adapter = new FlexAdapter(context, educationlist);
        adapter.setOnItemClickListener(this);
        rv_education.setAdapter(adapter);
        setupCrossandContinuebtn(view);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt = "";
                txt2 = "";
                for (int i = 0; i < selectBool.length; i++) {
                    if (selectBool[i]) {
                        if (TextUtils.isEmpty(txt)) {
                            txt = educationlist.get(i).getName();
                            txt2 = educationlist.get(i).getName();
                        } else {
                            if (!txt.contains("..."))
                                txt += "...";
                            txt2 += "," + educationlist.get(i).getName();
                        }
                    }
                }
                if (filterRequest == null) filterRequest = new FilterRequest();
                filterRequest.setEducation(!txt2.equalsIgnoreCase("No Preference") ? txt2 : null);
                sp.saveFilterModel(filterRequest);
                FilterActivity.this.tv_education.setText(txt);
                bottomSheetDialog.cancel();
            }
        });
        bottomSheetDialog.show();
    }

    private void showChildPrefBottomsheet() {
        type = Type.CHILD;
        lastPosition = 0;
        selectBool = new boolean[childernlist.size()];
        Arrays.fill(selectBool, false);
        if (filterRequest != null && filterRequest.getKids() != null) {
            for (String value : filterRequest.getKids().split(",")) {
                for (int j = 0; j < childernlist.size(); j++) {
                    if (childernlist.get(j).getName().equalsIgnoreCase(value)) {
                        selectBool[j] = true;
                        break;
                    }
                }
            }
        }
        //selectBool[0] = true;
        for (int i = 0; i < selectBool.length; i++) childernlist.get(i).setChecked(selectBool[i]);
        view = LayoutInflater.from(context).inflate(R.layout.child_bottomsheet_layout, null);
        bottomSheetDialog.setContentView(view);
        RecyclerView rv_education = view.findViewById(R.id.rv_children);
        layoutManager = new FlexboxLayoutManager(view.getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        rv_education.setLayoutManager(layoutManager);
        adapter = new FlexAdapter(context, childernlist);
        adapter.setOnItemClickListener(this);
        rv_education.setAdapter(adapter);
        setupCrossandContinuebtn(view);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt = "";
                txt2 = "";
                for (int i = 0; i < selectBool.length; i++) {
                    if (selectBool[i]) {
                        if (TextUtils.isEmpty(txt)) {
                            txt = childernlist.get(i).getName();
                            txt2 = childernlist.get(i).getName();
                        } else {
                            if (!txt.contains("..."))
                                txt += "...";
                            txt2 += "," + childernlist.get(i).getName();
                        }
                    }
                }
                if (filterRequest == null) filterRequest = new FilterRequest();
                filterRequest.setKids(!txt2.equalsIgnoreCase("No Preference") ? txt2 : null);
                sp.saveFilterModel(filterRequest);
                FilterActivity.this.tv_child.setText(txt);
                bottomSheetDialog.cancel();
            }
        });
        bottomSheetDialog.show();
    }

    private void showReligionBottomsheet() {
        type = Type.RELIGION;
        lastPosition = 0;
        selectBool = new boolean[religionList.size()];
        Arrays.fill(selectBool, false);
        if (filterRequest != null && filterRequest.getReligion() != null) {
            for (String value : filterRequest.getReligion().split(",")) {
                for (int j = 0; j < religionList.size(); j++) {
                    if (religionList.get(j).getName().equalsIgnoreCase(value)) {
                        selectBool[j] = true;
                        break;
                    }
                }
            }
        }
        // selectBool[0] = true;
        for (int i = 0; i < selectBool.length; i++) religionList.get(i).setChecked(selectBool[i]);
        view = LayoutInflater.from(context).inflate(R.layout.religion_bottomsheet_layout, null);
        bottomSheetDialog.setContentView(view);
        RecyclerView rv_education = view.findViewById(R.id.rv_religion);
        layoutManager = new FlexboxLayoutManager(view.getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        rv_education.setLayoutManager(layoutManager);
        adapter = new FlexAdapter(context, religionList);
        adapter.setOnItemClickListener(this);
        rv_education.setAdapter(adapter);
        setupCrossandContinuebtn(view);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt = "";
                txt2 = "";
                for (int i = 0; i < selectBool.length; i++) {
                    if (selectBool[i]) {
                        if (TextUtils.isEmpty(txt)) {
                            txt = religionList.get(i).getName();
                            txt2 = religionList.get(i).getName();
                        } else {
                            if (!txt.contains("..."))
                                txt += "...";
                            txt2 += "," + religionList.get(i).getName();
                        }
                    }
                }
                if (filterRequest == null) filterRequest = new FilterRequest();
                filterRequest.setReligion(!txt2.equalsIgnoreCase("No Preference") ? txt2 : null);
                sp.saveFilterModel(filterRequest);
                FilterActivity.this.tv_religion.setText(txt);
                bottomSheetDialog.cancel();
            }
        });
        bottomSheetDialog.show();
    }

    /* for (int i = 0; i < selectbool.length; i++) {
                       if (selectbool[i]) {
                           FilterActivity.this.tv_religion.setText(religionList.get(i).getName());
                           break;
                       }
                   }*/
    private void showPoliticalBottomsheet() {
        type = Type.POLITICAL;
        lastPosition = 0;
        selectBool = new boolean[politicalList.size()];
        Arrays.fill(selectBool, false);
        if (filterRequest != null && filterRequest.getPolitical() != null) {
            for (String value : filterRequest.getPolitical().split(",")) {
                for (int j = 0; j < politicalList.size(); j++) {
                    if (politicalList.get(j).getName().equalsIgnoreCase(value)) {
                        selectBool[j] = true;
                        break;
                    }
                }
            }
        }
        //   selectBool[0] = true;
        for (int i = 0; i < selectBool.length; i++) politicalList.get(i).setChecked(selectBool[i]);
        view = LayoutInflater.from(context).inflate(R.layout.political_bottomsheet_layout, null);
        bottomSheetDialog.setContentView(view);
        RecyclerView rv_education = view.findViewById(R.id.rv_political);
        layoutManager = new FlexboxLayoutManager(view.getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        rv_education.setLayoutManager(layoutManager);
        adapter = new FlexAdapter(context, politicalList);
        adapter.setOnItemClickListener(this);
        rv_education.setAdapter(adapter);
        setupCrossandContinuebtn(view);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt = "";
                txt2 = "";
                for (int i = 0; i < selectBool.length; i++) {
                    if (selectBool[i]) {
                        if (TextUtils.isEmpty(txt)) {
                            txt = politicalList.get(i).getName();
                            txt2 = politicalList.get(i).getName();
                        } else {
                            if (!txt.contains("..."))
                                txt += "...";
                            txt2 += "," + politicalList.get(i).getName();
                        }
                    }
                }
                if (filterRequest == null) filterRequest = new FilterRequest();
                filterRequest.setPolitical(!txt2.equalsIgnoreCase("No Preference") ? txt2 : null);
                sp.saveFilterModel(filterRequest);
                FilterActivity.this.tv_politics.setText(txt);
                bottomSheetDialog.cancel();
            }
        });
        bottomSheetDialog.show();
    }

    private void showSmokeBottomsheet() {
        type = Type.SMOKE;
        lastPosition = 0;
        selectBool = new boolean[smokeList.size()];
        Arrays.fill(selectBool, false);
        if (filterRequest != null && filterRequest.getSmoke() != null) {
            for (String value : filterRequest.getSmoke().split(",")) {
                for (int j = 0; j < smokeList.size(); j++) {
                    if (smokeList.get(j).getName().equalsIgnoreCase(value)) {
                        selectBool[j] = true;
                        break;
                    }
                }
            }
        }
        //selectBool[0] = true;
        for (int i = 0; i < selectBool.length; i++) smokeList.get(i).setChecked(selectBool[i]);
        view = LayoutInflater.from(context).inflate(R.layout.smoking_bottomsheet_layout, null);
        bottomSheetDialog.setContentView(view);
        RecyclerView rv_education = view.findViewById(R.id.rv_smoke);
        layoutManager = new FlexboxLayoutManager(view.getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setAlignItems(AlignItems.CENTER);
        rv_education.setLayoutManager(layoutManager);
        adapter = new FlexAdapter(context, smokeList);
        adapter.setOnItemClickListener(this);
        rv_education.setAdapter(adapter);
        setupCrossandContinuebtn(view);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt = "";
                txt2 = "";
                for (int i = 0; i < selectBool.length; i++) {
                    if (selectBool[i]) {
                        if (TextUtils.isEmpty(txt)) {
                            txt = smokeList.get(i).getName();
                            txt2 = smokeList.get(i).getName();
                        } else {
                            if (!txt.contains("..."))
                                txt += "...";
                            txt2 += "," + smokeList.get(i).getName();
                        }
                    }
                }
                if (filterRequest == null) filterRequest = new FilterRequest();
                filterRequest.setSmoke(!txt2.equalsIgnoreCase("No Preference") ? txt2 : null);
                sp.saveFilterModel(filterRequest);
                FilterActivity.this.tv_smoke.setText(txt);
                bottomSheetDialog.cancel();
            }
        });
        bottomSheetDialog.show();
    }

    private void setupCrossandContinuebtn(View v) {
        btnContinue = v.findViewById(R.id.btn_continue);
        //btn_reset.setPaintFlags(btn_reset.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        rl_close = v.findViewById(R.id.rl_close);
        rl_close.setOnClickListener(this::onClick);

    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_distance:
                showDistanceBottomsheet();
                break;
            case R.id.rl_age:
                showAgeBottomsheet(ageArray, "Select Age");
                break;
            case R.id.rl_gender:
                showGenderBottomsheet();
                break;
            case R.id.rl_relation:
                //CommonDialogs.PremuimPurChaseDialog(this,this);
                showRelationBottomsheet();
                break;
            case R.id.rl_education:
                showEducationBottomsheet();
                break;
            case R.id.rl_child:
                showChildPrefBottomsheet();
                break;
            case R.id.rl_politic:
                showPoliticalBottomsheet();
                break;
            case R.id.rl_religion:
                showReligionBottomsheet();
                break;
            case R.id.rl_smoke:
                showSmokeBottomsheet();
                break;
            case R.id.rl_height:
                showAgeBottomsheet(heightlist, "Select Height");
                break;
            case R.id.img_close:
                if (isReset) {
                    setResult(2323);
                }
                finish();
                overridePendingTransition(R.anim.filter_fast_nothing, R.anim.slide_out_down_fast);
                break;
            case R.id.rl_close:
                bottomSheetDialog.cancel();
                break;
            case R.id.btn_reset:
                if (sp.getDeluxe()&&sp.getFilterModel()!=null) {
                    isReset = true;
                }
                sp.removeFilter();
                tv_height.setText("No Preference");//< 4'0" to > 7'0"
                tv_relation.setText("No Preference");
                tv_education.setText("No Preference");
                tv_child.setText("No Preference");
                tv_politics.setText("No Preference");
                tv_religion.setText("No Preference");
                tv_dis.setText("500 miles");
                tv_gender.setText("Everyone");
                tv_age.setText("18 to 80");
                tv_smoke.setText("No Preference");
                filterRequest = new FilterRequest();
                break;
            case R.id.btn_apply:
                if (sp.getDeluxe()) {
                    if (filterRequest != null)
                        setResult(2323);
                    finish();
                    overridePendingTransition(R.anim.filter_fast_nothing, R.anim.slide_out_down_fast);
                } else {
                    CommonDialogs.DeluxePurChaseDialog(this, this);
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        changeBtnBackground();
    }

    private void changeBtnBackground() {
        if (btnContinue != null) {
            btnContinue.setEnabled(true);
            btnContinue.setBackground(context.getResources().getDrawable(R.drawable.gradientbtn));
        }
    }

    @Override
    public void onSeeking(SeekParams seekParams) {
        changeBtnBackground();
        tvDistance.setText(String.valueOf(seekParams.progress).concat(" ").concat(getString(R.string.miles)));
    }

    @Override
    public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

    }

    @Override
    public void OnItemClick(int position) {
        switch (type) {
            case RELATION:
                noPr = 2;
                changeFilter(relationlist, position, selectBool);
                break;
            case EDUCATION:
                noPr = (selectBool.length - 1);
                changeFilter(educationlist, position, selectBool);
                break;
            case CHILD:
                noPr = (selectBool.length - 1);
                changeFilter(childernlist, position, selectBool);
                break;
            case POLITICAL:
                noPr = (selectBool.length - 1);
                changeFilter(politicalList, position, selectBool);
                break;
            case RELIGION:
                noPr = (selectBool.length - 1);
                changeFilter(religionList, position, selectBool);
                break;
            case SMOKE:
                noPr = (selectBool.length - 1);
                changeFilter(smokeList, position, selectBool);
                break;
        }
        adapter.notifyDataSetChanged();
        //lastPosition = position;
    }

    private void changeFilter(ArrayList<FlexModel> list, int pos, boolean[] select) {
        changeBtnBackground();
        if (pos == noPr) {
            Arrays.fill(select, false);
            select[noPr] = true;
            for (int i = 0; i < select.length; i++) list.get(i).setChecked(select[i]);
        } else {
            select[noPr] = false;
            select[pos] = !select[pos];
            for (int i = 0; i < select.length; i++)
                list.get(i).setChecked(select[i]);
        }
        count = 0;
        for (Boolean aBoolean : select) if (!aBoolean) count++;
        if (count == select.length) {
            btnContinue.setEnabled(false);
            btnContinue.setBackground(context.getResources().getDrawable(R.drawable.disabledbtn));
        }
    }

    @Override
    public void onBackPressed() {
        img_close.performClick();
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        changeBtnBackground();
    }

    @Override
    public void onItemSelect(int item) {
        changeBtnBackground();
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        if (tokenSType.equalsIgnoreCase("DeluxePurChase")) {
            Toast.makeText(this, "Item Purchased", Toast.LENGTH_LONG).show();
            showLoading();
            homeViewModel.addDeluxeRequest(new DeluxeTokenCountModel("2", productId,
                    price,
                    selectedPosition,
                    details.purchaseInfo.purchaseData.orderId,
                    details.purchaseInfo.purchaseData.purchaseToken, CommonUtils.getDateForPurchase(details), details.purchaseInfo.signature,
                    details.purchaseInfo.purchaseData.purchaseState.toString()));
            Log.e("purchase success DeluxePurChase ", details.purchaseInfo.responseData);
        }
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    public void onClickToken(String tokenType, int tokensNum, int selectedPos) {
        tokenSType = tokenType;
        selectedPosition = tokensNum;
        if (tokenType.equalsIgnoreCase("DeluxePurChase")) {
            price = CommonDialogs.DeluxePriceList.get(selectedPos).getPriceValue();
            productId = CommonDialogs.DeluxeArr[selectedPos];
            bp.subscribe(this, productId);
        }
    }

    private enum Type {
        RELATION,
        EDUCATION,
        CHILD,
        POLITICAL,
        RELIGION,
        SMOKE
    }
/*changeBtnBackground();
noPr = (selectBool.length-1);
if (position==noPr){
    Arrays.fill(selectBool,false);
    selectBool[noPr]=true;
    for (int i = 0; i < selectBool.length; i++) childernlist.get(i).setChecked(selectBool[i]);
}else {
    selectBool[position] = !selectBool[position];
    for (int i = 0; i < selectBool.length; i++)
        childernlist.get(i).setChecked(selectBool[i]);
}
count = 0;
for (Boolean aBoolean : selectBool) if (!aBoolean) count++;
if (count == selectBool.length) {
    btnContinue.setEnabled(false);
    btnContinue.setBackground(context.getResources().getDrawable(R.drawable.disabledbtn));
}*/
}