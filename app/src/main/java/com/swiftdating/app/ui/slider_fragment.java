package com.swiftdating.app.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.swiftdating.app.R;
import com.swiftdating.app.callbacks.PremiumCallback;
import com.swiftdating.app.common.CommonDialogs;
import com.swiftdating.app.data.network.Resource;
import com.swiftdating.app.model.BaseModel;
import com.swiftdating.app.ui.base.BaseFragment;
import com.swiftdating.app.ui.homeScreen.viewmodel.HomeViewModel;
import com.swiftdating.app.ui.settingScreen.SliderAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class slider_fragment extends BaseFragment implements CommonDialogs.onProductConsume, SliderAdapter.OnItemClicked {

    private ViewPager viewPager;
    private TabLayout text_pager_indicator;
    private SliderAdapter sliderAdapter;
    int currentPage;
    private static final long TIME_PERIOD = 3000;
    Runnable Update;
    private View view;
    onReceiveClickCallback callback;
    private TextView tv_subscribe;


    public slider_fragment(onReceiveClickCallback callback) {
        this.callback = callback;
    }

    public void addPremiumTxt() {
        tv_subscribe.setVisibility(getBaseActivity().sp.getPremium() ? View.VISIBLE : View.GONE);
    }

    public interface onReceiveClickCallback {
        void onPremiumCallback(String tokenType, int tokensNum, int selectedPos);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_slider_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        setPager();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            tv_subscribe.setVisibility(getBaseActivity().sp.getPremium() ? View.VISIBLE : View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPager() {
        currentPage = 0;
        text_pager_indicator = view.findViewById(R.id.text_pager_indicator);
        RelativeLayout rlRootView = view.findViewById(R.id.rlRootView);
        viewPager = view.findViewById(R.id.pagerSlider);
        tv_subscribe = view.findViewById(R.id.tv_subscribe);

        rlRootView.setOnClickListener(v -> {
            openPurchaseDialog();
        });

        String[] tab_names = getResources().getStringArray(R.array.arr_premium_txt);
        List<String> titleList = new ArrayList<>();
        Collections.addAll(titleList, tab_names);
        sliderAdapter = new SliderAdapter(Objects.requireNonNull(getContext()), titleList, this);
        viewPager.setAdapter(sliderAdapter);
        text_pager_indicator.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        Handler handler = new Handler();
        Update = () -> {
            if (currentPage == viewPager.getAdapter().getCount()) {
                currentPage = 0;
            }
            viewPager.setCurrentItem(currentPage, true);
            currentPage++;
        };

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 0, TIME_PERIOD);

    }

    private void openPurchaseDialog() {
        CommonDialogs.PremuimPurChaseDialog(getContext(), this,getBaseActivity().sp);
    }

    @Override
    public void onClickToken(String tokenType, int tokensNum, int selectedPos) {
        callback.onPremiumCallback(tokenType, tokensNum, selectedPos);
    }

    @Override
    public void onPagerItemClick() {
        Log.e("TAG", "onPagerItemClick: ");
        CommonDialogs.PremuimPurChaseDialog(getContext(), this,getBaseActivity().sp);

    }
}