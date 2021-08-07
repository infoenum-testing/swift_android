package com.swiftdating.app.ui.chatScreen.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.swiftdating.app.data.network.Resource;
import com.swiftdating.app.model.BaseModel;
import com.swiftdating.app.model.requestmodel.MessageListRequestModel;
import com.swiftdating.app.model.responsemodel.MessageListModel;

public class ChatViewModel extends AndroidViewModel {
    private MutableLiveData<MessageListRequestModel> chatMessage = new MutableLiveData<>();
    private LiveData<Resource<MessageListModel>> chatMessageLD;

    private MutableLiveData<Integer> readStatus = new MutableLiveData<>();
    private LiveData<Resource<BaseModel>> readStatusLD;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        chatMessageLD = Transformations.switchMap(chatMessage, new Function<MessageListRequestModel, LiveData<Resource<MessageListModel>>>() {
            @Override
            public LiveData<Resource<MessageListModel>> apply(MessageListRequestModel input) {
                return ChatRepo.get().chatList(getApplication().getApplicationContext(), input);
            }
        });

        readStatusLD = Transformations.switchMap(readStatus, new Function<Integer, LiveData<Resource<BaseModel>>>() {
            @Override
            public LiveData<Resource<BaseModel>> apply(Integer input) {
                return ChatRepo.get().changeStatus(getApplication().getApplicationContext(), input);
            }
        });
    }

    public void chatMessageRequest (MessageListRequestModel s){
        chatMessage.setValue(s);
    }

    public LiveData<Resource<MessageListModel>> chatMessageResponse () {
        return chatMessageLD;
    }

    public void readStatusRequest (Integer s){
        readStatus.setValue(s);
    }

    public LiveData<Resource<BaseModel>> readStatusResponse () {
        return readStatusLD;
    }



}
