package com.swiftdating.app.ui.editProfileScreen;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.swiftdating.app.model.responsemodel.InstagramImageModel;

import okhttp3.OkHttpClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.swiftdating.app.R;
import com.swiftdating.app.callbacks.AuthenticationListener;
import com.swiftdating.app.common.CommonUtils;
import com.swiftdating.app.common.CommonDialogs;
import com.swiftdating.app.data.network.ApiUtils;
import com.swiftdating.app.data.network.CallServer;
import com.swiftdating.app.data.network.Resource;
import com.swiftdating.app.model.BaseModel;
import com.swiftdating.app.model.ImageModel;
import com.swiftdating.app.model.requestmodel.ImagesModels;
import com.swiftdating.app.model.requestmodel.OrderImageModel;
import com.swiftdating.app.model.responsemodel.EditImageResponseMode;
import com.swiftdating.app.model.responsemodel.ImageResponseModel;
import com.swiftdating.app.model.responsemodel.OrderImageResponseModel;
import com.swiftdating.app.model.responsemodel.ProfileOfUser;
import com.swiftdating.app.model.responsemodel.VerificationResponseModel;
import com.swiftdating.app.ui.answerNowScreen.AnswerNowActivity;
import com.swiftdating.app.ui.base.BaseActivity;
import com.swiftdating.app.ui.base.CropImage;
import com.swiftdating.app.ui.base.SwipeAndDragHelper;
import com.swiftdating.app.ui.createAccountScreen.CreateAccountActivity;
import com.swiftdating.app.ui.homeScreen.adapter.ImageAdapter;
import com.swiftdating.app.ui.editProfileScreen.viewmodel.EditProfileViewModel;
import com.swiftdating.app.ui.questionListScreen.QuestionListActivity;
import com.swiftdating.app.ui.selfieScreen.SelfieActivity;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.view.DragEvent.*;
import static com.swiftdating.app.data.network.CallServer.BASE_URL;

public class EditProfileActivity extends BaseActivity implements View.OnLongClickListener, View.OnDragListener, View.OnClickListener, ImageAdapter.OnClickListener, AuthenticationListener {

    private static final String TAG = "EditProfileActivity";
    boolean /*isFirstTime = false,*/ isOrderChanged;
    private ImageAdapter imageAdapter;
    private TextView tvDone;
    private RecyclerView rvImage;
    private Button btnSelfie;
    private ImageView image_back;
    private List<ImageModel> imageList = new ArrayList<>();
    private CoordinatorLayout bottomSheetGallery;
    private EditProfileViewModel editProfileViewModel;
    private int position = -1;
    private boolean fromReplace = false;
    private String imageId = "-1";
    private ProfileOfUser obj;
    private ConstraintLayout btn_continue;
    private int pos = -1;
    private String token;
    private SimpleDraweeView ivPhoto, ivPhoto2, ivPhoto3, ivPhoto4, ivPhoto5, ivPhoto6;
    private TextView tvGender, tvDob, tvLoc, tvCompletion, tvSign, tvAboutMe, tv_ambitions, tvOccupation, tv_personality1, tvEducation, tvSchool, tvEditChemQuestions, tvHeight, tvPets, tvKids, tvDrink, tvSmoke, tvExercise, tvReligion, tvPolitics, tv_LookingFor, tv_personality2, tv_personality3, tv_personality3_answer, tv_personality2_answer, tv_personality1_answer, tvInsta, tvVerifyTxt;
    private CardView cvHeight, cvKids, cvSmoke, cvDrink, cvExercise, cvReligion, cvPolitics, cvPets, cvSign, cvPersonality3, cvPersonality2, cvPersonality1, cvLookingFor, cvSelfie, cvInstagram;
    private BottomSheetDialog mBottomSheetDialog;
    private ArrayList<String> heightDigitlist;
    private String[] heightArray = {"< 4'0", "4'0\" (122 cm)", "4'1\" (125 cm)", "4'2\" (127 cm)", "4'3\" (130 cm)", "4'4\" (132 cm)", "4'5\" (135 cm)", "4'6\" (137 cm)", "4'7\" (140 cm)", "4'8\" (142 cm)", "4'9\" (144 cm)", "4'10\" (146 cm)", "4'11\" (150 cm)", "5'0\" (152 cm)", "5'1\" (155 cm)", "5'2\" (157 cm)", "5'3\" (160 cm)", "5'4\" (163 cm)", "5'5\" (165 cm)", "5'6\" (168 cm)", "5'7\" (170 cm)", "5'8\" (173 cm)", "5'9\" (175 cm)", "5'10\" (178 cm)", "5'11\" (180 cm)", "6'0\" (183 cm)", "6'1\" (185 cm)", "6'2\" (188 cm)", "6'3\" (191 cm)", "6'4\" (193 cm)", "6'5\" (196 cm)", "6'6\" (198 cm)", "6'7\" (200 cm)", "6'8\" (203 cm)", "6'9\" (206 cm)", "6'10\" (208 cm)", "6'11\" (211 cm)", "7'0\" (213 cm)", "> 7’0"};
    private String[] arraydigit = {"3.9", "4.0", "4.1", "4.2", "4.3", "4.4", "4.5", "4.6", "4.7", "4.8", "4.9", "4.10", "4.11", "5.0", "5.1", "5.2", "5.3", "5.4", "5.5", "5.6", "5.7", "5.8", "5.9", "5.10", "5.11", "6.0", "6.1", "6.2", "6.3", "6.4", "6.5", "6.6", "6.7", "6.8", "6.9", "6.10", "6.11", "7.0", "7.1"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_profile);
        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.grey));
        getMyProfile(null);
        initialize();
        subscribeModel();
        //isFirstTime = true;
        showLoading();
        editProfileViewModel.myProfileRequest(sp.getToken());
    }

    /**
     * **  Method to Handle api Response
     */
    private void subscribeModel() {
        editProfileViewModel = ViewModelProviders.of(this).get(EditProfileViewModel.class);
        editProfileViewModel.deleteResponse().observe(this, new Observer<Resource<BaseModel>>() {
            @Override
            public void onChanged(@Nullable Resource<BaseModel> resource) {
                if (resource == null) {
                    return;
                }
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        hideLoading();
                        if (resource.data.getSuccess()) {
                            //isFirstTime = false;
                            showLoading();
                            editProfileViewModel.myProfileRequest(sp.getToken());
                        } else if (resource.data.getError() != null && resource.data.getError().getCode().equalsIgnoreCase("401")) {
                            openActivityOnTokenExpire();
                        } else {
                            showSnackbar(rvImage, resource.data.getMessage());
                        }
                        break;
                    case ERROR:
                        hideLoading();
                        showSnackbar(rvImage, resource.message);
                        break;
                }
            }
        });

        editProfileViewModel.orderImageResponse().observe(this, new Observer<Resource<OrderImageResponseModel>>() {
            @Override
            public void onChanged(@Nullable Resource<OrderImageResponseModel> resource) {
                if (resource == null) {
                    return;
                }
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        hideLoading();
                        if (resource.data.getSuccess()) {
                            for (int i = 0; i < resource.data.getImages().getData().size(); i++) {
                                if (resource.data.getImages().getData().get(i).getImageUrl().contains("images/"))
                                    resource.data.getImages().getData().get(i).setImageUrl(resource.data.getImages().getData().get(i).getImageUrl());
                                else
                                    resource.data.getImages().getData().get(i).setImageUrl("images/" + resource.data.getImages().getData().get(i).getImageUrl());
                            }
                            sp.saveUserImage(resource.data.getImages().getData());
                            if (isOrderChanged) {
                               /* if (!TextUtils.isEmpty(sp.getMyString(SearchFragment.SearchResponse))) {
                                    sp.removeKey(SearchFragment.SearchResponse);
                                }
                                if (!TextUtils.isEmpty(sp.getMyString(SearchFragment.FilterResponse))) {
                                    sp.removeKey(SearchFragment.FilterResponse);
                                }*/
                                finish();
                                overridePendingTransition(R.anim.nothing_fast, R.anim.slide_out_down_fast);
                            } else {
                                showLoading();
                                editProfileViewModel.myProfileRequest(sp.getToken());
                            }
                        } else if (resource.data.getError() != null && resource.data.getError().getCode().equalsIgnoreCase("401")) {
                            openActivityOnTokenExpire();
                        } else {
                            showSnackbar(rvImage, "Something Went Wrong");
                        }
                        break;
                    case ERROR:
                        hideLoading();
                        showSnackbar(rvImage, resource.message);
                        break;
                }
            }

        });

        editProfileViewModel.myProfileResponse().observe(this, new Observer<Resource<VerificationResponseModel>>() {
            @Override
            public void onChanged(@Nullable Resource<VerificationResponseModel> resource) {
                if (resource == null) {
                    return;
                }
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        hideLoading();
                        if (resource.data.getSuccess()) {
                           /* if (!isFirstTime) {
                                isFirstTime=true;
                                if (!TextUtils.isEmpty(sp.getMyString(SearchFragment.SearchResponse))) {
                                    sp.removeKey(SearchFragment.SearchResponse);
                                }
                                if (!TextUtils.isEmpty(sp.getMyString(SearchFragment.FilterResponse))) {
                                    sp.removeKey(SearchFragment.FilterResponse);
                                }
                            }*/
                            if (resource.data.getNoOfLikes() != null)
                                sp.saveNoOfLikes(resource.data.getNoOfLikes());

                            Gson gson = new Gson();
                            String json = sp.getUser();
                            obj = gson.fromJson(json, ProfileOfUser.class);
                            sp.saveUserData(obj, resource.data.getUser().getProfileOfUser().getCompleted().toString());
                            tvCompletion.setText(resource.data.getUser().getProfileOfUser().getCompleted().toString() + "% Profile Completed");
                            imageList.clear();
                            imageList.addAll(resource.data.getImagedata().getData());
                            sp.saveUserImage(imageList);
                            imageAdapter.notifyDataSetChanged();
                            if (resource.data.getInsta() != null && resource.data.getInsta().size() > 0) {
                                sp.setInstagramConnected(true);
                                btn_continue.setVisibility(View.GONE);
                                cvInstagram.setVisibility(View.GONE);
                                tvInsta.setVisibility(View.GONE);
                            }
                        }
                        break;
                    case ERROR:
                        hideLoading();
                        showSnackbar(rvImage, resource.message);
                        break;
                }
            }

        });

        editProfileViewModel.sendTokenResponse().observe(this, new Observer<Resource<InstagramImageModel>>() {
            @Override
            public void onChanged(@Nullable Resource<InstagramImageModel> resource) {
                if (resource == null) {
                    return;
                }
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        hideLoading();
                        Log.e("apiResponse", new Gson().toJson(resource.data.getData().getData()));
                        sp.setInstagramConnected(true);
                        btn_continue.setVisibility(View.GONE);
                        cvInstagram.setVisibility(View.GONE);
                        tvInsta.setVisibility(View.GONE);
                        break;
                    case ERROR:
                        hideLoading();
                        showSnackbar(rvImage, resource.message);
                        break;
                }
            }
        });

    }


    /**
     * **  Method to Upload Image
     */
    private void uploadImage(String name) {
        showLoading();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient
                .Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
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
                fl = new Compressor(EditProfileActivity.this).compressToFile(new File(name));
            } catch (IOException e) {
                e.printStackTrace();
                fl = new File(name);
            }
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), fl);
            profilePic[0] = MultipartBody.Part.createFormData("profilePic", fl.getName(), requestBody);
            if (fromReplace) {
                id = RequestBody.create(MediaType.parse("text/plain"), imageId);
            }
        } else {
            showSnackbar(cvDrink, CallServer.somethingWentWrong);
            hideLoading();
            return;
        }
        if (!fromReplace) {
            Call<ResponseBody> call = getResponse.uploadFilesAPI(sp.getToken(), profilePic);

            call.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    hideLoading();
                    try {

                        Gson gson = new GsonBuilder().setLenient().create();
                        if (response.code() == 200) {
                            ImageResponseModel responseBean = gson.fromJson(response.body().string(), ImageResponseModel.class);
                            if (responseBean.getSuccess()) {
                                Log.e(TAG, "onResponse: " + imageList);
                                imageList.addAll(responseBean.getImages());
                                imageAdapter.notifyDataSetChanged();
                                Log.e(TAG, "onResponse: " + imageList);
                                sp.saveUserImage(imageList);
                                showLoading();
                                editProfileViewModel.myProfileRequest(sp.getToken());

                            } else if (responseBean.getError() != null && responseBean.getError().getCode().equalsIgnoreCase("401")) {
                                openActivityOnTokenExpire();
                            } else {
                                showSnackbar(cvDrink, responseBean.getMessage());
                            }
                        } else if (response.code() == 401) {
                            openActivityOnTokenExpire();
                        } else {
                            ImageResponseModel responseBean = gson.fromJson(response.errorBody().string(), ImageResponseModel.class);
                            showSnackbar(cvDrink, responseBean.getMessage());
                        }
                    } catch (Exception ae) {
                        ae.printStackTrace();
                        showSnackbar(cvDrink, CallServer.somethingWentWrong);
                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideLoading();
                    showSnackbar(cvDrink, CallServer.serverError);
                }
            });
        } else {
            Call<ResponseBody> call = getResponse.replaceImages(sp.getToken(), profilePic, id);

            call.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Gson gson = new GsonBuilder().setLenient().create();
                        if (response.code() == 200) {
                            EditImageResponseMode responseBean = gson.fromJson(response.body().string(), EditImageResponseMode.class);
                            if (responseBean.getSuccess()) {
                                //imageList.add(position, responseBean.getImages());
                                //imageList.remove(position + 1);
                                imageList.set(position, responseBean.getImages());
                                //imageAdapter.notifyDataSetChanged();
                                imageAdapter.notifyItemChanged(position);
                                sp.saveUserImage(imageList);

                            } else if (responseBean.getError() != null && responseBean.getError().getCode().equalsIgnoreCase("401")) {
                                openActivityOnTokenExpire();
                            } else {
                                showSnackbar(cvDrink, responseBean.getMessage());
                            }
                        } else if (response.code() == 401) {
                            openActivityOnTokenExpire();
                        } else {
                            ImageResponseModel responseBean = gson.fromJson(response.errorBody().string(), ImageResponseModel.class);
                            showSnackbar(cvDrink, responseBean.getMessage());
                        }
                    } catch (Exception ae) {
                        ae.printStackTrace();
                        showSnackbar(cvDrink, CallServer.somethingWentWrong);
                    }

                    hideLoading();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideLoading();
                    showSnackbar(cvDrink, CallServer.serverError);
                }
            });
        }
    }

    /**
     * ** Method to Hide bottom sheet
     */
    private void hideBottomSheet() {
        if (mBottomSheetDialog != null && mBottomSheetDialog.isShowing())
            mBottomSheetDialog.dismiss();
    }

    /**
     * **  Method to Initialize
     */
    private void initialize() {
        heightDigitlist = new ArrayList<>();
        /*for (int i = 4; i <= 6; i++)
            for (int j = 0; j <= 11; j++) heightDigitlist.add(Float.parseFloat("" + i + "." + j));
        heightDigitlist.add(7.0f);
        heightDigitlist.add(7.1f);
        heightDigitlist.add(0, 3.9f);*/
        Collections.addAll(heightDigitlist, arraydigit);
        tvVerifyTxt = findViewById(R.id.tvVerifyTxt);
        ivPhoto = findViewById(R.id.iv_photo);
        image_back = findViewById(R.id.image_back);
        rvImage = findViewById(R.id.rv_image);
        ivPhoto2 = findViewById(R.id.iv_photo2);
        ivPhoto3 = findViewById(R.id.iv_photo3);
        ivPhoto4 = findViewById(R.id.iv_photo4);
        ivPhoto5 = findViewById(R.id.iv_photo5);
        ivPhoto6 = findViewById(R.id.iv_photo6);
        tvGender = findViewById(R.id.tv_gender);
        tvDob = findViewById(R.id.tv_dob);
        tvLoc = findViewById(R.id.tv_loc);
        tvCompletion = findViewById(R.id.tv_completion);
        tvSign = findViewById(R.id.tv_sign);
        tvAboutMe = findViewById(R.id.tv_about_me);
        tvOccupation = findViewById(R.id.tv_occupation);
        tvEducation = findViewById(R.id.tv_education);
        tvSchool = findViewById(R.id.tv_school);
        tvPets = findViewById(R.id.tv_pets);
        tvEditChemQuestions = findViewById(R.id.tv_editchemques);
        tvHeight = findViewById(R.id.tv_height);
        tv_ambitions = findViewById(R.id.tv_ambitions);
        btn_continue = findViewById(R.id.btn_continue);
        tv_personality1 = findViewById(R.id.tv_personality1);
        tv_personality2 = findViewById(R.id.tv_personality2);
        tv_personality3 = findViewById(R.id.tv_personality3);
        tv_personality1_answer = findViewById(R.id.tv_personality1_answer);
        tv_personality2_answer = findViewById(R.id.tv_personality2_answer);
        tv_personality3_answer = findViewById(R.id.tv_personality3_answer);
        tv_LookingFor = findViewById(R.id.tv_LookingFor);
        cvPersonality3 = findViewById(R.id.cv_personality3);
        cvPersonality2 = findViewById(R.id.cv_personality2);
        cvPersonality1 = findViewById(R.id.cv_personality1);
        cvLookingFor = findViewById(R.id.cvLookingFor);
        cvSelfie = findViewById(R.id.cvSelfie);
        btnSelfie = findViewById(R.id.btnVerifyProfile);
        tvReligion = findViewById(R.id.tv_religion);
        cvHeight = findViewById(R.id.cvHeight);
        tvKids = findViewById(R.id.tv_kids);
        cvKids = findViewById(R.id.cvKids);
        tvSmoke = findViewById(R.id.tv_smoke);
        cvSmoke = findViewById(R.id.cvSmoke);
        tvDrink = findViewById(R.id.tv_drink);
        cvDrink = findViewById(R.id.cvDrink);
        tvExercise = findViewById(R.id.tv_Exercise);
        cvExercise = findViewById(R.id.cvExercise);
        cvReligion = findViewById(R.id.cvReligion);
        tvPolitics = findViewById(R.id.tv_politics);
        cvPolitics = findViewById(R.id.cvPolitics);
        cvPets = findViewById(R.id.cvPets);
        cvSign = findViewById(R.id.cvSign);
        tvDone = findViewById(R.id.tv_done);
        cvInstagram = findViewById(R.id.cvInstagram);
        tvInsta = findViewById(R.id.tv_insta);
        btn_continue = findViewById(R.id.btn_continue);
        bottomSheetGallery = findViewById(R.id.bottomSheetGallery);
        CommonUtils.setBottomSheetBehaviour(bottomSheetGallery);
        if (sp.isInstaConnected()) {
            btn_continue.setVisibility(View.GONE);
            cvInstagram.setVisibility(View.GONE);
            tvInsta.setVisibility(View.GONE);
        } else {
//            btn_continue.setVisibility(View.VISIBLE);
//            cvInstagram.setVisibility(View.VISIBLE);
//            tvInsta.setVisibility(View.VISIBLE);
        }
        listener();
        setProfileImage();
        if (!sp.isRejected()) {
            cvSelfie.setVisibility(View.GONE);
        } else {
            cvSelfie.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        hideKeyboard();
        setData();
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

    /**
     * **  Method to Implement Listeners
     */
    private void listener() {
        ivPhoto.setOnLongClickListener(this);
        ivPhoto2.setOnLongClickListener(this);
        ivPhoto3.setOnLongClickListener(this);
        ivPhoto4.setOnLongClickListener(this);
        ivPhoto5.setOnLongClickListener(this);
        ivPhoto6.setOnLongClickListener(this);
        image_back.setOnClickListener(this);
        tvGender.setOnClickListener(this);
        tvLoc.setOnClickListener(this);
        btnSelfie.setOnClickListener(this);
        tv_ambitions.setOnClickListener(this);
        tvSign.setOnClickListener(this);
        tvAboutMe.setOnClickListener(this);
        cvPersonality2.setOnClickListener(this);
        cvPersonality1.setOnClickListener(this);
        cvPersonality3.setOnClickListener(this);
        cvSelfie.setOnClickListener(this);
        tvOccupation.setOnClickListener(this);
        tvEducation.setOnClickListener(this);
        tvSchool.setOnClickListener(this);
        tvEditChemQuestions.setOnClickListener(this);
        cvHeight.setOnClickListener(this);
        cvKids.setOnClickListener(this);
        cvSmoke.setOnClickListener(this);
        cvDrink.setOnClickListener(this);
        cvExercise.setOnClickListener(this);
        cvLookingFor.setOnClickListener(this);
        cvReligion.setOnClickListener(this);
        cvPolitics.setOnClickListener(this);
        cvPets.setOnClickListener(this);
        cvSign.setOnClickListener(this);
        tvDone.setOnClickListener(this);
        btn_continue.setOnClickListener(this);

        ivPhoto.setOnDragListener(this);
        ivPhoto2.setOnDragListener(this);
        ivPhoto3.setOnDragListener(this);
        ivPhoto4.setOnDragListener(this);
        ivPhoto5.setOnDragListener(this);
        ivPhoto6.setOnDragListener(this);
    }

    /**
     * **  Method to set Data
     */
    private void setData() {
        Gson gson = new Gson();
        String json = sp.getUser();
        obj = gson.fromJson(json, ProfileOfUser.class);
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
            Date date = dateFormat.parse(obj.getDob());
            tvDob.setText(obj.getDob() != null ? "Birthday: " + dateFormat2.format(date) : "Birthday: ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvGender.setText(obj.getGender() != null ? obj.getGender() : "");
        tvLoc.setText(obj.getCity() != null ? obj.getCity() : "");
        tvSign.setText(obj.getZodiacSign() != null ? obj.getZodiacSign() : "");
        tv_ambitions.setText(obj.getAmbitions() != null ? obj.getAmbitions() : "");
        tv_LookingFor.setText(obj.getLookingFor() != null ? obj.getLookingFor() : "");
        tv_personality1.setText(obj.getQuestion1() != null ? obj.getQuestion1() : "");
        tv_personality2.setText(obj.getQuestion2() != null ? obj.getQuestion2() : "");
        tv_personality3.setText(obj.getQuestion3() != null ? obj.getQuestion3() : "");
        tv_personality1_answer.setText(obj.getAnswer1() != null ? obj.getAnswer1() : "");
        tv_personality2_answer.setText(obj.getAnswer2() != null ? obj.getAnswer2() : "");
        tv_personality3_answer.setText(obj.getAnswer3() != null ? obj.getAnswer3() : "");
        tvCompletion.setText(sp.getProfileCompleted() != null ? sp.getProfileCompleted() + "% Profile Completed" : "");
        tvAboutMe.setText(obj.getAboutme() != null ? obj.getAboutme() : "");
        tvOccupation.setText(obj.getOccupation() != null ? obj.getOccupation() : "");
        tvSchool.setText(obj.getSchool() != null ? obj.getSchool() : "");
        tvEducation.setText(obj.getEducation() != null ? obj.getEducation() : "");
        String het = obj.getHeight() != null ? obj.getHeight() : "";
        if (!TextUtils.isEmpty(het)) {
            for (int i = 0; i < heightDigitlist.size(); i++) {
                if (het.equalsIgnoreCase("" + heightDigitlist.get(i))) {
                    het = heightArray[i];
                    break;
                }
            }
        }
        tvHeight.setText(het);
        tvPets.setText(obj.getPets() != null ? obj.getPets() : "");
        tvKids.setText(obj.getKids() != null ? obj.getKids() : "");
        tvDrink.setText(obj.getDrink() != null ? obj.getDrink() : "");
        tvSmoke.setText(obj.getSmoke() != null ? obj.getSmoke() : "");
        tvExercise.setText(obj.getExercise() != null ? obj.getExercise() : "");
        tvReligion.setText(obj.getRelegion() != null ? obj.getRelegion() : "");
        tvPolitics.setText(obj.getPolitical() != null ? obj.getPolitical() : "");
        tvCompletion.setVisibility(View.VISIBLE);
        String status = "Verification status: ".concat(sp.getSelfieVerificationStatus().equalsIgnoreCase("No") ? "Not submitted " : sp.getSelfieVerificationStatus());
        tvVerifyTxt.setText(status);

    }

    /**
     * **  Method to Set user Images in Drag view
     */
    void setProfileImage() {
        Gson gson = new Gson();
        String jsonImage = sp.getUserImage();
        Type type = new TypeToken<List<ImageModel>>() {
        }.getType();
        imageList = gson.fromJson(jsonImage, type);
        imageAdapter = new ImageAdapter(this, imageList, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(EditProfileActivity.this, 3);
        rvImage.setHasFixedSize(true);
        rvImage.setNestedScrollingEnabled(false);
        rvImage.setLayoutManager(gridLayoutManager);
        rvImage.setAdapter(imageAdapter);
        SwipeAndDragHelper swipeAndDragHelper = new SwipeAndDragHelper(imageAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(swipeAndDragHelper);
        // attaching touch helper to recyclerview to implement drag and drop functionality
        touchHelper.attachToRecyclerView(rvImage);
    }

    @Override
    public boolean onLongClick(View view) {

        view.startDrag(null, new View.DragShadowBuilder(view), null, 0);
        return false;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()) {
            case ACTION_DRAG_STARTED:
            case ACTION_DROP:
                v.invalidate();
                return true;
            case ACTION_DRAG_ENTERED:
            case ACTION_DRAG_LOCATION:
            case ACTION_DRAG_EXITED:
                return true;
            case ACTION_DRAG_ENDED:
                ((ImageView) v).clearColorFilter();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        if (view == image_back) {
            finish();
            overridePendingTransition(R.anim.slide_out_down_fast, R.anim.nothing_fast);
        } else if (view == tvDone) {
            isOrderChanged = true;
            //callChangeOrderApi(imageList);
            finish();
            overridePendingTransition(R.anim.slide_out_down_fast, R.anim.nothing_fast);
        } else if (view.getId() == R.id.ll_camera) {
            hideBottomSheet();
            EasyImage.openCamera(EditProfileActivity.this, 0);
        } else if (view.getId() == R.id.ll_gallery) {
            hideBottomSheet();
            EasyImage.openGallery(EditProfileActivity.this, 0);
        } else if (view == btn_continue) {
//            AuthenticationDialog authenticationDialog = new AuthenticationDialog(this, this);
//            authenticationDialog.setCancelable(true);
//            authenticationDialog.show();
        } else if (view.getId() == R.id.tv_yes) {
            showLoading();
            CommonDialogs.dismiss();
            editProfileViewModel.deleteRequest(String.valueOf(imageList.get(pos).getId()));
        } else if (view == cvSelfie || view == btnSelfie) {
            if (!sp.getSelfieVerificationStatus().equalsIgnoreCase("Pending")) {
                Intent i = new Intent(this, SelfieActivity.class);
                i.putExtra("isEdit", true);
                startActivityForResult(i, 1002);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                CommonDialogs.showPendingSelfieStatus(this);
            }
        } else {
            if (view == tvSign) {
                intent = new Intent(this, CreateAccountActivity.class);
                intent.putExtra("parseCount", 21);
                intent.putExtra("FromEdit", true);
            } else if (view == tvGender) {
                intent = new Intent(this, CreateAccountActivity.class);
                intent.putExtra("parseCount", 3);
                intent.putExtra("FromEdit", true);
            } else if (view == tvAboutMe) {
                intent = new Intent(this, CreateAccountActivity.class);
                intent.putExtra("parseCount", 8);
                intent.putExtra("FromEdit", true);
            } else if (view == cvLookingFor) {
                intent = new Intent(this, CreateAccountActivity.class);
                intent.putExtra("parseCount", 7);
                intent.putExtra("FromEdit", true);
            } else if (view == cvHeight) {
                intent = new Intent(this, CreateAccountActivity.class);
                intent.putExtra("parseCount", 19);
                intent.putExtra("FromEdit", true);
            } else if (view == cvKids) {
                intent = new Intent(this, CreateAccountActivity.class);
                intent.putExtra("parseCount", 20);
                intent.putExtra("FromEdit", true);
            } else if (view == cvReligion) {
                intent = new Intent(this, CreateAccountActivity.class);
                intent.putExtra("parseCount", 22);
                intent.putExtra("FromEdit", true);
            } else if (view == cvDrink) {
                intent = new Intent(this, CreateAccountActivity.class);
                intent.putExtra("parseCount", 13);
                intent.putExtra("FromEdit", true);
            } else if (view == cvSmoke) {
                intent = new Intent(this, CreateAccountActivity.class);
                intent.putExtra("parseCount", 14);
                intent.putExtra("FromEdit", true);
            } else if (view == tv_ambitions) {
                intent = new Intent(this, CreateAccountActivity.class);
                intent.putExtra("parseCount", 9);
                intent.putExtra("FromEdit", true);
            } else if (view == cvExercise) {
                intent = new Intent(this, CreateAccountActivity.class);
                intent.putExtra("parseCount", 15);
                intent.putExtra("FromEdit", true);
            } else if (view == cvPolitics) {
                intent = new Intent(this, CreateAccountActivity.class);
                intent.putExtra("parseCount", 16);
                intent.putExtra("FromEdit", true);
            } else if (view == tvOccupation) {
                intent = new Intent(this, CreateAccountActivity.class);
                intent.putExtra("parseCount", 12);
                intent.putExtra("FromEdit", true);
            } else if (view == tvSchool) {
                intent = new Intent(this, CreateAccountActivity.class);
                intent.putExtra("parseCount", 11);
                intent.putExtra("FromEdit", true);
            } else if (view == tvEducation) {
                intent = new Intent(this, CreateAccountActivity.class);
                intent.putExtra("parseCount", 10);
                intent.putExtra("FromEdit", true);
            } else if (view == tvLoc) {
                intent = new Intent(this, CreateAccountActivity.class);
                intent.putExtra("parseCount", 23);
                intent.putExtra("isEdit", true);
            } else if (view == cvSign) {
                intent = new Intent(this, CreateAccountActivity.class);
                intent.putExtra("parseCount", 21);
                intent.putExtra("FromEdit", true);
            } else if (view == cvPets) {
                intent = new Intent(this, CreateAccountActivity.class);
                intent.putExtra("parseCount", 18);
                intent.putExtra("FromEdit", true);
            } else if (view == cvPersonality1) {
                if (tv_personality1.getText().toString().equalsIgnoreCase("")) {
                    intent = new Intent(this, QuestionListActivity.class);
                    intent.putExtra("isQuestion1", "1");
                } else {
                    intent = new Intent(this, AnswerNowActivity.class);
                    intent.putExtra("isQuestion1", "1");
                    intent.putExtra("question", obj.getQuestion1());
                    intent.putExtra("answer", obj.getAnswer1());
                }
            } else if (view == cvPersonality2) {
                if (tv_personality2.getText().toString().equalsIgnoreCase("")) {
                    intent = new Intent(this, QuestionListActivity.class);
                    intent.putExtra("isQuestion1", "2");
                } else {
                    intent = new Intent(this, AnswerNowActivity.class);
                    intent.putExtra("isQuestion1", "2");
                    intent.putExtra("question", obj.getQuestion2());
                    intent.putExtra("answer", obj.getAnswer2());
                }
            } else if (view == cvPersonality3) {
                if (tv_personality3.getText().toString().equalsIgnoreCase("")) {
                    intent = new Intent(this, QuestionListActivity.class);
                    intent.putExtra("isQuestion1", "3");
                } else {
                    intent = new Intent(this, AnswerNowActivity.class);
                    intent.putExtra("isQuestion1", "3");
                    intent.putExtra("question", obj.getQuestion3());
                    intent.putExtra("answer", obj.getAnswer3());
                }
            } else if (view.getId() == R.id.img_close) {
                mBottomSheetDialog.dismiss();
                return;
            }

            intent.putExtra("isEdit", true);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    private void callChangeOrderApi(List<ImageModel> list) {
        ArrayList<ImagesModels> imageModelsList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getImageUrl().contains("images/")) {
                imageModelsList.add(new ImagesModels(list.get(i).getImageUrl(), i + 1));
            } else {
                imageModelsList.add(new ImagesModels("images/" + list.get(i).getImageUrl(), i + 1));
            }
        }
        Log.e("TAG", "onClick: " + imageModelsList);
        showLoading();
        editProfileViewModel.orderImageRequest(new OrderImageModel(imageModelsList));
    }

    @Override
    public void onItemClick(int pos) {
        fromReplace = false;
        checkPermission();
    }

    /**
     * **  Method to checkPermissions to add image
     */
    private void checkPermission() {
        if (checkPermissionCG())
            mBottomSheetDialog = CommonUtils.showCameraGalleryBottomSheet(EditProfileActivity.this, this);
        else
            EditProfileActivity.this.requestPermissionCG();
    }

    @Override
    public void onDeleteClick(int pos) {
        Log.e(TAG, "onDeleteClick: " + pos);
        Log.e(TAG, "onDeleteClick: " + imageList.get(pos));
        if (imageList.size() > 1) {
            position = pos;
            fromReplace = false;
            if (pos < imageList.size()) {
                this.pos = pos;
                CommonDialogs.alertDialogTwoButtons(this, this, "Are you sure you want to delete this image?");
            }
        }
    }

    @Override
    public void onReplaceClick(int pos) {
        if (imageList.size() == 3 && imageList.size() > pos) {
            position = pos;
            fromReplace = true;
            imageId = imageList.get(pos).getId() + "";
            checkPermission();
        }
    }

    @Override
    public void onItemMoved(List<ImageModel> list) {
        isOrderChanged = false;
        callChangeOrderApi(list);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE_CG:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    mBottomSheetDialog = CommonUtils.showCameraGalleryBottomSheet(EditProfileActivity.this, this);
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this, Manifest.permission.CAMERA) ||
                            !ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                            !ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        alertDialogDeny(getString(R.string.camera_gallery_permission_text));
                    } else {
                        alertDialogPermission("camera");
                    }
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5) {
            if (data != null) {
                if (resultCode == RESULT_OK) {
                    showLoading();
                    uploadImage(data.getExtras().getString("Image"));
                }
            }
        } else if (requestCode == 1002) {
            if (resultCode == RESULT_OK) {
                cvSelfie.setVisibility(View.GONE);
            }
        } else {
            EasyImage.handleActivityResult(requestCode, resultCode, data, EditProfileActivity.this, new DefaultCallback() {
                @Override
                public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                    Uri imageUri = Uri.fromFile(imageFiles.get(0));
                    startActivityForResult(new Intent(EditProfileActivity.this, CropImage.class).putExtra("imageUri", imageUri.toString()), 5);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // tvDone.performClick();

        finish();
        overridePendingTransition(R.anim.nothing_fast, R.anim.slide_out_down_fast);
    }

    @Override
    public void onTokenReceived(String auth_token) {
        if (auth_token == null)
            return;
        showLoading();
        editProfileViewModel.sendTokenRequest(auth_token);
        Log.e("token_", auth_token);
    }

}


