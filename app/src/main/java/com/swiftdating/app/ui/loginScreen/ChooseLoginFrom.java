package com.swiftdating.app.ui.loginScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.swiftdating.app.R;
import com.swiftdating.app.data.preference.SharedPreference;

public class ChooseLoginFrom extends AppCompatActivity {
    private Button btn_link_number, btn_create_new;
    private ImageView img_cross;
    private SharedPreference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login_from);
        initViews();
    }

    private void initViews() {
        preference = new SharedPreference(this);
        btn_create_new = findViewById(R.id.btn_create_new);
        btn_link_number = findViewById(R.id.btn_link_number);
        btn_create_new.setOnClickListener(this::onClick);
        img_cross = findViewById(R.id.img_cross);
        img_cross.setOnClickListener(this::onClick);
        btn_link_number.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_create_new:
/*
                startActivity(new Intent(this, WelcomeActivity.class));
                finishAffinity();
*/
                break;
            case R.id.btn_link_number:
                /*startActivity(new Intent(this, EmailActivity.class));
                preference.setIsFromNumber(false);
                finishAffinity();
                */
                break;
            case R.id.img_cross:
                finishAffinity();
                break;
        }
    }
}