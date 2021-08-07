package com.swiftdating.app.ui.addImageScreen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.swiftdating.app.R;
import com.swiftdating.app.common.CommonUtils;
import com.swiftdating.app.data.network.ApiUtils;
import com.swiftdating.app.data.network.CallServer;
import com.swiftdating.app.data.network.Resource;
import com.swiftdating.app.model.ImageModel;
import com.swiftdating.app.model.responsemodel.ImageResponseModel;
import com.swiftdating.app.model.responsemodel.VerificationResponseModel;
import com.swiftdating.app.ui.addImageScreen.adapter.PhotoAdapter;
import com.swiftdating.app.ui.base.BaseFragment;
import com.swiftdating.app.ui.base.CropImage;
import com.swiftdating.app.ui.createAccountScreen.CreateAccountActivity;
import com.swiftdating.app.ui.editProfileScreen.viewmodel.EditProfileViewModel;
import com.swiftdating.app.ui.welcomeScreen.WelcomeActivity;

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

import static android.app.Activity.RESULT_OK;
import static com.swiftdating.app.data.network.CallServer.BASE_URL;
import static com.swiftdating.app.ui.base.BaseActivity.PERMISSION_REQUEST_CODE_CG;


public class AddImagesFragment extends BaseFragment implements PhotoAdapter.OnClickListener, View.OnClickListener {

    EditProfileViewModel editProfileViewModel;
    int pos;
    private ArrayList<String> photoList = new ArrayList<>();
    private ArrayList<ImageModel> imageList = new ArrayList<>();
    private RecyclerView rvPhotos;
    private PhotoAdapter photoAdapter;
    private FloatingActionButton btnDone;
    private BottomSheetDialog mBottomSheetDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_images;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

    }

    /**
     * **  Method to initialize the view
     */
    private void init(View view) {
        subscribeModel();
        rvPhotos = view.findViewById(R.id.rv_photos);
        btnDone = view.findViewById(R.id.btn_continue);
        CoordinatorLayout bottomSheetGallery = view.findViewById(R.id.bottomSheetGallery);

        rvPhotos.setLayoutManager(new GridLayoutManager(getContext(), 3));
        photoAdapter = new PhotoAdapter(getContext(), photoList, this);
        rvPhotos.setAdapter(photoAdapter);
        btnDone.setOnClickListener(this);
        CommonUtils.setBottomSheetBehaviour(bottomSheetGallery);


    }

    private void subscribeModel() {
        editProfileViewModel = ViewModelProviders.of(this).get(EditProfileViewModel.class);
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
                        getBaseActivity().hideLoading();
                        if (resource.data.getSuccess()) {
                            imageList.clear();
                            imageList.addAll(resource.data.getImagedata().getData());
                            getBaseActivity().sp.saveUserImage(imageList);
                            getBaseActivity().sp.saveUserData(resource.data.getUser().getProfileOfUser(), resource.data.getUser().getProfileOfUser().getCompleted().toString());
                            if (imageList.size() > 2) {
                                ((CreateAccountActivity) getActivity()).updateParseCount(6);
                                Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                                intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                                startActivity(intent);
                                getActivity().finishAffinity();
                            }
                        }
                        break;
                    case ERROR:
                        getBaseActivity().hideLoading();
                        getBaseActivity().showSnackbar(rvPhotos, resource.message);
                        break;
                }
            }

        });

    }

    @Override
    public void onItemClick(int pos) {
        checkPermission();
    }

    @Override
    public void onDeleteClick(int pos) {
        photoList.remove(pos);
        photoAdapter.notifyDataSetChanged();
    }


    /**
     * **  Method to checkPermissions to add image
     */
    private void checkPermission() {
        if (getBaseActivity().checkPermissionCG())
            mBottomSheetDialog = CommonUtils.showCameraGalleryBottomSheet(getActivity(), this);
        else
            requestPermissionCG();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE_CG:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    mBottomSheetDialog = CommonUtils.showCameraGalleryBottomSheet(getActivity(), this);
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA) ||
                            !ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) ||
                            !ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        getBaseActivity().alertDialogDeny(getString(R.string.camera_gallery_permission_text));
                    } else {
                        getBaseActivity().alertDialogPermission("camera");
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
                    photoList.add(data.getExtras().getString("Image"));
                    photoAdapter = new PhotoAdapter(getContext(), photoList, this);
                    rvPhotos.setAdapter(photoAdapter);
                }
            }
        } else {
            EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
                @Override
                public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                    Uri imageUri = Uri.fromFile(imageFiles.get(0));
                    startActivityForResult(new Intent(getContext(), CropImage.class).putExtra("imageUri", imageUri.toString()), 5);
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_continue) {
            if (photoList.size() > 0) {
                uploadImage();

                /*if (((CreateAccountActivity) getActivity()).preference.getIsFromNumber()) {
                    ((CreateAccountActivity) getActivity()).updateParseCount(6);
                    getBaseActivity().hideLoading();
                    Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                    intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                    startActivity(intent);
                    getActivity().finishAffinity();
                } else {
                    uploadImage();
                }*/
            } else {
                getBaseActivity().showSnackbar(btnDone, "Please add a minimum of 1 photo");
            }
        } else if (view.getId() == R.id.ll_camera) {
            hideBottomSheet();
            EasyImage.openCamera(getActivity(), 0);
        } else if (view.getId() == R.id.ll_gallery) {
            hideBottomSheet();
            EasyImage.openGallery(getActivity(), 0);
        } else if (view.getId() == R.id.img_close) {
            hideBottomSheet();
            //EasyImage.openGallery(getActivity(), 0);
        }
    }

    /**
     * **  Method to Upload Images
     */
    private void uploadImage() {
        getBaseActivity().showLoading();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient
                .Builder()
                .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BuildConfig.SERVER_URL)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        ApiUtils getResponse = retrofit.create(ApiUtils.class);

        MultipartBody.Part[] profilePic = null;
        RequestBody id = null;
        if (photoList.size() > 0) {
            profilePic = new MultipartBody.Part[photoList.size()];
            for (int i = 0; i < photoList.size(); i++) {
                File fl = null;
                try {
                    fl = new Compressor(getContext()).compressToFile(new File(photoList.get(i)));
                } catch (IOException e) {
                    e.printStackTrace();
                    fl = new File(photoList.get(i));
                }
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), fl);
                profilePic[i] = MultipartBody.Part.createFormData("profilePic", fl.getName(), requestBody);
            }
        } else {
            getBaseActivity().showSnackbar(btnDone, CallServer.somethingWentWrong);
            editProfileViewModel.myProfileRequest(getBaseActivity().sp.getToken());
            return;
        }

        Call<ResponseBody> call = getResponse.uploadFilesAPI(getBaseActivity().sp.getToken(), profilePic);

        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    // Gson gson = new GsonBuilder().setLenient().create();
                    Gson gson = new Gson();
                    if (response.code() == 200) {
                        String myResStr = response.body().string();
                        Log.e("TAG", "onResponse:1 " + myResStr);
                        // ProfileOfUser user = gson.fromJson(myResStr, ProfileOfUser.class);

                        ImageResponseModel responseBean = gson.fromJson(myResStr, ImageResponseModel.class);
/*
                        if (user != null && user.getCompleted() != null) {
                            getBaseActivity().sp.saveUserData(user, user.getCompleted().toString());
                            Log.e("TAG", "onResponse: if" );
                        }*/
                        // ImageResponseModel responseBean = gson1.fromJson(myResStr, ImageResponseModel.class);
                        Log.e("TAG", "onResponse:2 " + myResStr);
                        Log.e("TAG", "onResponse:3 " + responseBean);
                        if (responseBean != null && responseBean.getSuccess() != null && responseBean.getSuccess()) {
                            getBaseActivity().sp.saveUserImage(responseBean.getImages());
                            getBaseActivity().hideLoading();
                            Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                            startActivity(intent);
                            getActivity().finishAffinity();
                        } else if (responseBean.getError() != null && responseBean.getError().getCode().equalsIgnoreCase("401")) {
                            getBaseActivity().openActivityOnTokenExpire();
                            getBaseActivity().hideLoading();
                        } else {
                            getBaseActivity().hideLoading();
                            editProfileViewModel.myProfileRequest(getBaseActivity().sp.getToken());
                            getBaseActivity().showSnackbar(btnDone, responseBean.getMessage());
                        }
                    } else {
                        getBaseActivity().hideLoading();
                        ImageResponseModel responseBean = gson.fromJson(response.errorBody().string(), ImageResponseModel.class);
                        getBaseActivity().showSnackbar(btnDone, CallServer.somethingWentWrong);
                    }
                } catch (Exception ae) {
                    if (getBaseActivity() != null) {
                        getBaseActivity().hideLoading();
                        editProfileViewModel.myProfileRequest(getBaseActivity().sp.getToken());
                    } else {
                        getBaseActivity().showSnackbar(btnDone, CallServer.somethingWentWrong);
                    }
                    ae.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (getBaseActivity() != null) {
                    getBaseActivity().hideLoading();
                    editProfileViewModel.myProfileRequest(getBaseActivity().sp.getToken());
                    getBaseActivity().showSnackbar(btnDone, CallServer.serverError);
                }
            }
        });
    }

    /**
     * ** Method to Hide bottom sheet
     */
    private void hideBottomSheet() {
        if (mBottomSheetDialog != null && mBottomSheetDialog.isShowing())
            mBottomSheetDialog.dismiss();
    }

}



