package com.swift.dating.ui.viewpagerScreen;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import com.swift.dating.R;
import com.swift.dating.model.responsemodel.ImageForUser;
import com.swift.dating.model.responsemodel.InstagramImageModel;
import com.swift.dating.ui.base.BaseActivity;

public class ViewPagerActivity extends BaseActivity {

    String data = "";
    int position = 0;
    boolean isInstaImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.primaryTextColor));
        setContentView(R.layout.activity_view_pager);

        init();

    }

    private void init() {
        ViewPager viewPager = findViewById(R.id.view_pager);
        ImageView backBtn = findViewById(R.id.backBtn);
        if (getIntent().hasExtra("images")) {
            data = getIntent().getStringExtra("images");
            position = getIntent().getIntExtra("position", 0);
            if (getIntent().hasExtra("isInstaImage")) {
                isInstaImage = true;
            }
            Log.e("pos_view", position + "");
        }

        if (!isInstaImage) {
            List<ImageForUser> imageForUser = new Gson().fromJson(data, new TypeToken<List<ImageForUser>>() {
            }.getType());

            ViewPagerImageAdapter adapter = new ViewPagerImageAdapter(this, imageForUser);
            viewPager.setAdapter(adapter);
        }else{
            List<InstagramImageModel.Datum> instaImageList = new Gson().fromJson(data, new TypeToken<List<InstagramImageModel.Datum>>() {
            }.getType());

            ViewPagerImageAdapter2 adapter = new ViewPagerImageAdapter2(this, instaImageList.subList(0,18));
            viewPager.setAdapter(adapter);
        }
        viewPager.setCurrentItem(position);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver),
                new IntentFilter("MyData"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }
}
