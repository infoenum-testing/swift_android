package com.swift.dating.ui.chatScreen.viewModel;

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
import com.swift.dating.model.requestmodel.MessageListRequestModel;
import com.swift.dating.model.responsemodel.MessageListModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRepo {

    public static ChatRepo get() {
        return new ChatRepo();
    }

    //Api to Get Messages of chat
    LiveData<Resource<MessageListModel>> chatList(final Context c, MessageListRequestModel obj) {
        final MutableLiveData<Resource<MessageListModel>> data = new MutableLiveData<>();
        SharedPreference sp = new SharedPreference(c);
        data.setValue(Resource.<MessageListModel>loading(null));
        CallServer.get().getAPIName().getMessageList(sp.getToken(),obj).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                try {
                    Gson gson = new GsonBuilder().setLenient().create();

                    if (response.code() == 200) {
                        MessageListModel responseBean = gson.fromJson(response.body().string(), MessageListModel.class);
                        data.setValue(Resource.success(responseBean));
                    }else if(response.code() == 401){
                        MessageListModel responseBean = gson.fromJson(response.errorBody().string(), MessageListModel.class);
                        data.setValue(Resource.success(responseBean));
                    } else {
                        MessageListModel responseBean = gson.fromJson(response.errorBody().string(), MessageListModel.class);
                        data.setValue(Resource.success(responseBean));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    data.setValue(Resource.<MessageListModel>error(CallServer.serverError, null, 0, e));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                data.setValue(Resource.<MessageListModel>error(CallServer.serverError, null, 0, t));
            }
        });

        return data;
    }


    //Api for changing Status from unread to red
    LiveData<Resource<BaseModel>> changeStatus(final Context c, Integer obj) {
        final MutableLiveData<Resource<BaseModel>> data = new MutableLiveData<>();
        SharedPreference sp = new SharedPreference(c);
        data.setValue(Resource.<BaseModel>loading(null));
        CallServer.get().getAPIName().changeStatus(sp.getToken(),obj).enqueue(new Callback<ResponseBody>() {
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

}
