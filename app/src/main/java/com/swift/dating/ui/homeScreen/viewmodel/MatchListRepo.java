package com.swift.dating.ui.homeScreen.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.swift.dating.data.network.CallServer;
import com.swift.dating.data.network.Resource;
import com.swift.dating.data.preference.SharedPreference;
import com.swift.dating.model.responsemodel.ChatListModel;
import com.swift.dating.model.responsemodel.MatchListResponseModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchListRepo {

    public static MatchListRepo get() {
        return new MatchListRepo();
    }

    LiveData<Resource<MatchListResponseModel>> getMatchList(final Context c, String obj) {
        final MutableLiveData<Resource<MatchListResponseModel>> data = new MutableLiveData<>();
        SharedPreference sp = new SharedPreference(c);
        data.setValue(Resource.<MatchListResponseModel>loading(null));
        CallServer.get().getAPIName().getMatchList(sp.getToken(),obj).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                try {
                    Gson gson = new GsonBuilder().setLenient().create();

                    if (response.code() == 200) {
                        MatchListResponseModel responseBean = gson.fromJson(response.body().string(), MatchListResponseModel.class);
                        data.setValue(Resource.success(responseBean));
                    } else {
                        MatchListResponseModel responseBean = gson.fromJson(response.errorBody().string(), MatchListResponseModel.class);
                        data.setValue(Resource.success(responseBean));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    data.setValue(Resource.<MatchListResponseModel>error(CallServer.serverError, null, 0, e));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                data.setValue(Resource.<MatchListResponseModel>error(CallServer.serverError, null, 0, t));
            }
        });

        return data;
    }


    LiveData<Resource<ChatListModel>> getChatList(final Context c, String obj) {
        final MutableLiveData<Resource<ChatListModel>> data = new MutableLiveData<>();
        SharedPreference sp = new SharedPreference(c);
        data.setValue(Resource.<ChatListModel>loading(null));
        CallServer.get().getAPIName().getChatList(sp.getToken()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                try {
                    Gson gson = new GsonBuilder().setLenient().create();

                    if (response.code() == 200) {
                        ChatListModel responseBean = gson.fromJson(response.body().string(), ChatListModel.class);
                        data.setValue(Resource.success(responseBean));
                    } else {
                        ChatListModel responseBean = gson.fromJson(response.errorBody().string(), ChatListModel.class);
                        data.setValue(Resource.success(responseBean));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    data.setValue(Resource.<ChatListModel>error(CallServer.serverError, null, 0, e));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                data.setValue(Resource.<ChatListModel>error(CallServer.serverError, null, 0, t));
            }
        });

        return data;
    }


}
