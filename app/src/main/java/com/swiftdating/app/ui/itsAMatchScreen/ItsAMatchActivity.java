package com.swiftdating.app.ui.itsAMatchScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import com.swiftdating.app.R;
import com.swiftdating.app.data.network.CallServer;
import com.swiftdating.app.data.preference.SharedPreference;
import com.swiftdating.app.model.ImageModel;
import com.swiftdating.app.model.NotificationModel;
import com.swiftdating.app.ui.chatScreen.ChatWindow;

public class ItsAMatchActivity extends AppCompatActivity implements View.OnClickListener {

    SimpleDraweeView ivMyPic, ivUserPic;
    Button btnAnswerNow,ivCross;
    NotificationModel mNotificationModel;
    TextView tvDescription;
    SharedPreference sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_its_amatch);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        getWindow().setBackgroundDrawableResource(R.color.transparent_alpha);
        init();
    }



    /**
     ***  Method to Initialize
     */
    void init() {
        sp = new SharedPreference(this);
        ivMyPic = findViewById(R.id.ivmypic);
        ivCross = findViewById(R.id.ivcross);
        ivUserPic = findViewById(R.id.ivuserpic);
        tvDescription = findViewById(R.id.tvDescription);
        btnAnswerNow = findViewById(R.id.btn_answer_now);
        if (getIntent().hasExtra("match")) {
            mNotificationModel = (NotificationModel) getIntent().getSerializableExtra("match");
            setData();
            btnAnswerNow.setOnClickListener(this);
            ivCross.setOnClickListener(this);
        }
    }

    /**
     ***  Method to Set Data
     */
    private void setData() {
        Gson gson = new Gson();
        SharedPreference sp = new SharedPreference(this);
        String jsonImage = sp.getUserImage();
        Type type = new TypeToken<List<ImageModel>>() {
        }.getType();
        List<ImageModel> imagelist = gson.fromJson(jsonImage, type);
        ivMyPic.setImageURI(CallServer.BaseImage + imagelist.get(0).getImageUrl());
        ivUserPic.setImageURI(CallServer.BaseImage + mNotificationModel.getImage().getImageUrl());
        tvDescription.setText(String.format(this.getResources().getString(R.string.youhave48), mNotificationModel.getMatch().getName()));
    }

    @Override
    public void onClick(View view) {
        if(view == ivCross){
            finish();
            overridePendingTransition(R.anim.nothing, R.anim.slide_out_down);
        }else if(view == btnAnswerNow){
//TODO send to Chat Window
            startActivityForResult(new Intent(this, ChatWindow.class).putExtra("id", mNotificationModel.getUserId())
                    .putExtra("name", mNotificationModel.getMatch().getName())
                    .putExtra("tabPos", 2)
                    .putExtra("isExpired", false)
                    .putExtra("hitApi",true)
                    .putExtra("timeLeft", 24*60*60*1000)
                    .putExtra("image", mNotificationModel.getImage().getImageUrl()), 10000);
            finish();
            overridePendingTransition(R.anim.nothing, R.anim.slide_out_down);
        }
    }
}
