package com.swiftdating.app.ui.editProfileScreen.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.swiftdating.app.data.network.CallServer;
import com.swiftdating.app.data.network.Resource;
import com.swiftdating.app.data.preference.SharedPreference;
import com.swiftdating.app.model.BaseModel;
import com.swiftdating.app.model.requestmodel.OrderImageModel;
import com.swiftdating.app.model.responsemodel.InstagramImageModel;
import com.swiftdating.app.model.responsemodel.OrderImageResponseModel;
import com.swiftdating.app.model.responsemodel.VerificationResponseModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditRepo {

    public static EditRepo get() {
        return new EditRepo();
    }

    LiveData<Resource<BaseModel>> deleteImage(final Context c, String obj) {
        final MutableLiveData<Resource<BaseModel>> data = new MutableLiveData<>();
        SharedPreference sp = new SharedPreference(c);
        data.setValue(Resource.<BaseModel>loading(null));
        CallServer.get().getAPIName().deleteImage(sp.getToken(),obj).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                try {
                    Gson gson = new GsonBuilder().setLenient().create();

                    if (response.code() == 200) {
                        BaseModel responseBean = gson.fromJson(response.body().string(), BaseModel.class);
                        data.setValue(Resource.success(responseBean));
                    }else if(response.code() == 401){
                        BaseModel responseBean = gson.fromJson(response.errorBody().string(), BaseModel.class);
                        data.setValue(Resource.success(responseBean));
                    } else {
                        BaseModel responseBean = gson.fromJson(response.errorBody().string(), BaseModel.class);
                        data.setValue(Resource.success(responseBean));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    data.setValue(Resource.<BaseModel>error(CallServer.serverError, null, 0, e));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                data.setValue(Resource.<BaseModel>error(CallServer.serverError, null, 0, t));
            }
        });

        return data;
    }

    LiveData<Resource<OrderImageResponseModel>> orderImage(final Context c, OrderImageModel obj) {
        final MutableLiveData<Resource<OrderImageResponseModel>> data = new MutableLiveData<>();
        SharedPreference sp = new SharedPreference(c);
        data.setValue(Resource.<OrderImageResponseModel>loading(null));
        CallServer.get().getAPIName().orderApi(sp.getToken(),obj).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                try {
                    Gson gson = new GsonBuilder().setLenient().create();

                    if (response.code() == 200) {
                        OrderImageResponseModel responseBean = gson.fromJson(response.body().string(), OrderImageResponseModel.class);
                        data.setValue(Resource.success(responseBean));
                    }else if(response.code() == 401){
                        OrderImageResponseModel responseBean = gson.fromJson(response.errorBody().string(), OrderImageResponseModel.class);
                        data.setValue(Resource.success(responseBean));
                    } else {
                        OrderImageResponseModel responseBean = gson.fromJson(response.errorBody().string(), OrderImageResponseModel.class);
                        data.setValue(Resource.success(responseBean));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    data.setValue(Resource.<OrderImageResponseModel>error(CallServer.serverError, null, 0, e));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                data.setValue(Resource.<OrderImageResponseModel>error(CallServer.serverError, null, 0, t));
            }
        });

        return data;
    }

    LiveData<Resource<InstagramImageModel>> sendToken(final Context c, String obj) {
        final MutableLiveData<Resource<InstagramImageModel>> data = new MutableLiveData<>();
        data.setValue(Resource.<InstagramImageModel>loading(null));
        SharedPreference sp = new SharedPreference(c);
        CallServer.get().getAPIName().sendToken(sp.getToken(), obj).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                try {
                    Gson gson = new GsonBuilder().setLenient().create();

                    if (response.code() == 200) {
                        InstagramImageModel responseBean = gson.fromJson(response.body().string(), InstagramImageModel.class);
                        data.setValue(Resource.success(responseBean));
                    } else if (response.code() == 401) {
                        data.setValue(Resource.<InstagramImageModel>error("Something Went Wrong", null, 401, null));
                    } else {
                        data.setValue(Resource.<InstagramImageModel>error(response.message(), null, 401, null));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    data.setValue(Resource.<InstagramImageModel>error("Something Went Wrong", null, response.code(), e));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                data.setValue(Resource.<InstagramImageModel>error(CallServer.serverError, null, 0, t));
            }
        });

        return data;
    }


    LiveData<Resource<VerificationResponseModel>> myProfile(final Context c, String obj) {
        final MutableLiveData<Resource<VerificationResponseModel>> data = new MutableLiveData<>();
        SharedPreference sp = new SharedPreference(c);
        data.setValue(Resource.<VerificationResponseModel>loading(null));
        CallServer.get().getAPIName().profile(sp.getToken()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                try {
                    Gson gson = new GsonBuilder().setLenient().create();

                    if (response.code() == 200) {
                        VerificationResponseModel responseBean = gson.fromJson(response.body().string(), VerificationResponseModel.class);
                        data.setValue(Resource.success(responseBean));
                    }else if(response.code() == 401){
                        VerificationResponseModel responseBean = gson.fromJson(response.errorBody().string(), VerificationResponseModel.class);
                        data.setValue(Resource.success(responseBean));
                    } else {
                        VerificationResponseModel responseBean = gson.fromJson(response.errorBody().string(), VerificationResponseModel.class);
                        data.setValue(Resource.success(responseBean));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    data.setValue(Resource.<VerificationResponseModel>error(CallServer.serverError, null, 0, e));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                data.setValue(Resource.<VerificationResponseModel>error(CallServer.serverError, null, 0, t));
            }
        });

        return data;
    }
}
