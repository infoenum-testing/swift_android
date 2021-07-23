package com.swift.dating.ui.itsAMatchScreen.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.swift.dating.data.network.CallServer;
import com.swift.dating.data.network.Resource;
import com.swift.dating.data.preference.SharedPreference;
import com.swift.dating.model.BaseModel;
import com.swift.dating.model.requestmodel.AnswerProfileRequest;
import com.swift.dating.model.responsemodel.VerificationResponseModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchRepo  {
    public static MatchRepo get() {
    return new MatchRepo();
}

    LiveData<Resource<BaseModel>> unmatch(final Context c, String obj) {
        final MutableLiveData<Resource<BaseModel>> data = new MutableLiveData<>();
        data.setValue(Resource.<BaseModel>loading(null));
        SharedPreference sp = new SharedPreference(c);
        CallServer.get().getAPIName().unmatchUser(sp.getToken(),obj).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                try {
                    Gson gson = new GsonBuilder().setLenient().create();

                    if (response.code() == 200) {
                        BaseModel responseBean = gson.fromJson(response.body().string(), BaseModel.class);
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

    LiveData<Resource<VerificationResponseModel>> answerQuestions(final Context c, AnswerProfileRequest obj) {
        final MutableLiveData<Resource<VerificationResponseModel>> data = new MutableLiveData<>();
        data.setValue(Resource.<VerificationResponseModel>loading(null));
        SharedPreference sp = new SharedPreference(c);
        CallServer.get().getAPIName().answerQuestions(sp.getToken(),obj).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                try {
                    Gson gson = new GsonBuilder().setLenient().create();

                    if (response.code() == 200) {
                        VerificationResponseModel responseBean = gson.fromJson(response.body().string(), VerificationResponseModel.class);
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
