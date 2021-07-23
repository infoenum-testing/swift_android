package com.swift.dating.ui.emailScreen.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.swift.dating.data.network.CallServer;
import com.swift.dating.data.network.Resource;
import com.swift.dating.model.BaseModel;
import com.swift.dating.model.requestmodel.ResendRequest;
import com.swift.dating.model.requestmodel.SignUpRequestModel;
import com.swift.dating.model.responsemodel.VerificationResponseModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterEmailRepo {

    public static EnterEmailRepo get() {
        return new EnterEmailRepo();
    }

    LiveData<Resource<VerificationResponseModel>> loginRegister(final Context c, SignUpRequestModel obj) {
        final MutableLiveData<Resource<VerificationResponseModel>> data = new MutableLiveData<>();
        data.setValue(Resource.<VerificationResponseModel>loading(null));

        CallServer.get().getAPIName().loginRegisterApi(obj).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                try {
                    Gson gson = new GsonBuilder().setLenient().create();

                    if (response.code() == 200) {
                        VerificationResponseModel responseBean = gson.fromJson(response.body().string(), VerificationResponseModel.class);
                        data.setValue(Resource.success(responseBean));
                    } else if (response.code() == 400) {
                        VerificationResponseModel responseBean = gson.fromJson(response.errorBody().string(), VerificationResponseModel.class);
                        data.setValue(Resource.success(responseBean));
                    }else {
                        VerificationResponseModel responseBean = gson.fromJson(response.errorBody().string(), VerificationResponseModel.class);
                        data.setValue(Resource.success(responseBean));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                data.setValue(Resource.<VerificationResponseModel>error(CallServer.serverError, null, 0, t));
            }
        });

        return data;
    }

    LiveData<Resource<BaseModel>> resendOtp(final Context c, ResendRequest obj) {
        final MutableLiveData<Resource<BaseModel>> data = new MutableLiveData<>();
        data.setValue(Resource.<BaseModel>loading(null));

        CallServer.get().getAPIName().resend(obj).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                try {
                    Gson gson = new GsonBuilder().setLenient().create();

                    if (response.code() == 200) {
                        BaseModel responseBean = gson.fromJson(response.body().string(), BaseModel.class);
                        data.setValue(Resource.success(responseBean));
                    } else if (response.code() == 400) {
                        BaseModel responseBean = gson.fromJson(response.errorBody().string(), BaseModel.class);
                        data.setValue(Resource.success(responseBean));
                    }else if(response.code() == 404) {
                        BaseModel responseBean = gson.fromJson(response.errorBody().string(), BaseModel.class);
                        data.setValue(Resource.success(responseBean));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                data.setValue(Resource.<BaseModel>error(CallServer.serverError, null, 0, t));
            }
        });

        return data;
    }


}
