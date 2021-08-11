package com.swiftdating.app.ui.selfieScreen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.swiftdating.app.R;
import com.swiftdating.app.common.CommonDialogs;
import com.swiftdating.app.data.network.ApiUtils;
import com.swiftdating.app.data.network.CallServer;
import com.swiftdating.app.model.responsemodel.ImageResponseModel;
import com.swiftdating.app.model.responsemodel.SelfieResponseModel;
import com.swiftdating.app.ui.base.BaseActivity;
import com.swiftdating.app.ui.homeScreen.HomeActivity;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.swiftdating.app.data.network.CallServer.BASE_URL;

public class SelfieActivity extends BaseActivity implements View.OnClickListener {

    SimpleDraweeView ivSelfie;
    ImageView cameraIcon;
    Button btn_continue, btnBack;
    private ImageView llBack;
    boolean isEdit;
    ArrayList<String> imageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie);
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().containsKey("isEdit")) {
            isEdit = getIntent().getExtras().getBoolean("isEdit");
        }

        ivSelfie = findViewById(R.id.ivSelfie);
        btn_continue = findViewById(R.id.btn_continue);
        btnBack = findViewById(R.id.btnBack);
        llBack = findViewById(R.id.image_back);

     /*   if (isEdit) {
            btn_continue.setText("Resubmit");
            btnBack.setVisibility(View.VISIBLE);
            btnBack.setOnClickListener(this);
        }*/

        cameraIcon = findViewById(R.id.cameraIcon);
        cameraIcon.setOnClickListener(this);
        llBack.setOnClickListener(this);
        btn_continue.setOnClickListener(this);
        CommonDialogs.selfieDialog(SelfieActivity.this, this);
        sp.setDialogOpen(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            String imageUri = returnValue.get(0);
            imageList.clear();
            imageList.add(imageUri);
            Bitmap bitmap = BitmapFactory.decodeFile(imageUri);
            ivSelfie.setImageBitmap(bitmap);
            btn_continue.setVisibility(View.VISIBLE);
        } else {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        if (!sp.isDialogOpen()) {
            CommonDialogs.selfieDialog(SelfieActivity.this, this);
            sp.setDialogOpen(true);
        }
    }

    /**
     * **  Method to checkPermissions to add image
     */
    private void checkPermission() {
        if (checkPermissionCG()) {
            openCamera();
        } else
            requestPermissionCG();
    }

    private void openCamera() {
        Options options = Options.init()
                .setRequestCode(100)                                           //Request code for activity results
                .setCount(1)                                                   //Number of images to restict selection count
                .setFrontfacing(true)                                         //Front Facing camera on start
                .setMode(Options.Mode.Picture)                                     //Option to select only pictures or videos or both
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                .setPath("/pix/images");                                       //Custom Path For media Storage
        Pix.start(this, options);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE_CG) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(SelfieActivity.this, Manifest.permission.CAMERA) ||
                        !ActivityCompat.shouldShowRequestPermissionRationale(SelfieActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                        !ActivityCompat.shouldShowRequestPermissionRationale(SelfieActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    alertDialogDeny(getString(R.string.camera_gallery_permission_text));
                } else {
                    alertDialogPermission("camera");
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_ready) {
            if (!sp.getIsFromNumber()) {
                CommonDialogs.dismiss();
                checkPermission();
                sp.setDialogOpen(false);
            }
        } else if (view.getId() == R.id.tvBack) {
            CommonDialogs.dismiss();
            onBackPressed();
        } else if (view.getId() == R.id.cameraIcon) {
            checkPermission();
        } else if (view == btn_continue) {
            uploadImage();
        } else if (view == btnBack) {
            finish();
        } else if (view == llBack) {
            finish();
        }

    }

    /**
     * Method to Upload Selfie Image
     */
    private void uploadImage() {
        showLoading();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient
                .Builder()
                .connectTimeout(80, TimeUnit.SECONDS)
                .readTimeout(80, TimeUnit.SECONDS)
                .writeTimeout(80, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        ApiUtils getResponse = retrofit.create(ApiUtils.class);

        MultipartBody.Part[] profilePic = null;
        RequestBody id = null;
        if (imageList.size() > 0) {
            profilePic = new MultipartBody.Part[imageList.size()];
            File fl = null;
            try {
                fl = new Compressor(this).compressToFile(new File(imageList.get(0)));
            } catch (IOException e) {
                e.printStackTrace();
                fl = new File(imageList.get(0));
            }
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), fl);
            profilePic[0] = MultipartBody.Part.createFormData("profilePic", fl.getName(), requestBody);
        } else {
            showSnackbar(btn_continue, CallServer.somethingWentWrong);
            hideLoading();
            return;
        }

        Call<ResponseBody> call = getResponse.uploadSelfie(sp.getToken(), profilePic);

        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Gson gson = new GsonBuilder().setLenient().create();
                    SelfieResponseModel responseBean = gson.fromJson(response.body().string(), SelfieResponseModel.class);
                    if (responseBean.getSuccess()) {
                        if (response.code() == 200) {
                            hideLoading();
                            sp.saveSelfie(responseBean.getSelfieData().getSelfieUrl());
                            sp.saveVerified(responseBean.getSelfieData().getIsVerified());
                            sp.saveSelfieVerificationStatus("Pending");
                            sp.saveIsRejected(false);
                            finish();
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        } else if (responseBean.getError() != null && responseBean.getError().getCode().equalsIgnoreCase("401")) {
                            openActivityOnTokenExpire();
                        } else {
                            showSnackbar(btn_continue, responseBean.getMessage());
                        }
                    } else {
                        ImageResponseModel responseBean2 = gson.fromJson(response.errorBody().string(), ImageResponseModel.class);
                        showSnackbar(btn_continue, responseBean2.getMessage());
                    }
                } catch (Exception ae) {
                    ae.printStackTrace();
                    showSnackbar(btn_continue, CallServer.somethingWentWrong);
                }
                hideLoading();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideLoading();
                showSnackbar(btn_continue, CallServer.serverError);
            }
        });
    }
}
