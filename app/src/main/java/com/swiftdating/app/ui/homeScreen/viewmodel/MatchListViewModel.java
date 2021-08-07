package com.swiftdating.app.ui.homeScreen.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.swiftdating.app.data.network.Resource;
import com.swiftdating.app.model.responsemodel.ChatListModel;
import com.swiftdating.app.model.responsemodel.MatchListResponseModel;

public class MatchListViewModel extends AndroidViewModel {

    private MutableLiveData<String> getMatchList = new MutableLiveData<>();
    private LiveData<Resource<MatchListResponseModel>> matchListLD;

    private MutableLiveData<String> getChatListRequest = new MutableLiveData<>();
    private LiveData<Resource<ChatListModel>> chatListLD;

    public MatchListViewModel(@NonNull Application application) {
        super(application);

        matchListLD = Transformations.switchMap(getMatchList, new Function<String, LiveData<Resource<MatchListResponseModel>>>() {
            @Override
            public LiveData<Resource<MatchListResponseModel>> apply(String input) {
                return MatchListRepo.get().getMatchList(getApplication().getApplicationContext(), input);
            }
        });

        chatListLD = Transformations.switchMap(getChatListRequest, new Function<String, LiveData<Resource<ChatListModel>>>() {
            @Override
            public LiveData<Resource<ChatListModel>> apply(String input) {
                return MatchListRepo.get().getChatList(getApplication().getApplicationContext(), input);
            }
        });
    }

    public void getMatchListRequest(String s) {
        getMatchList.setValue(s);
    }

    public LiveData<Resource<MatchListResponseModel>> matchListResponse() {
        return matchListLD;
    }

    public void getChatListRequest(String s) {
        getChatListRequest.setValue(s);
    }

    public LiveData<Resource<ChatListModel>> chatListResponse() {
        return chatListLD;
    }
}
