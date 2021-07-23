package com.swift.dating.ui.editProfileScreen.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.swift.dating.data.network.Resource;
import com.swift.dating.model.BaseModel;
import com.swift.dating.model.requestmodel.OrderImageModel;
import com.swift.dating.model.responsemodel.InstagramImageModel;
import com.swift.dating.model.responsemodel.OrderImageResponseModel;
import com.swift.dating.model.responsemodel.VerificationResponseModel;

public class EditProfileViewModel extends AndroidViewModel {

    private MutableLiveData<String> deleteImage = new MutableLiveData<>();
    private LiveData<Resource<BaseModel>> deleteImageLD;


    private MutableLiveData<String> myprofileRequest = new MutableLiveData<>();
    private LiveData<Resource<VerificationResponseModel>> myprofileLD;


    private MutableLiveData<OrderImageModel> orderImage = new MutableLiveData<>();
    private LiveData<Resource<OrderImageResponseModel>> orderImageLD;


    private MutableLiveData<String> sendToken = new MutableLiveData<>();
    private LiveData<Resource<InstagramImageModel>> sendTokenLD;

    public EditProfileViewModel(@NonNull Application application) {
        super(application);

        deleteImageLD = Transformations.switchMap(deleteImage, new Function<String, LiveData<Resource<BaseModel>>>() {
            @Override
            public LiveData<Resource<BaseModel>> apply(String input) {
                return EditRepo.get().deleteImage(getApplication().getApplicationContext(), input);
            }
        });

        orderImageLD = Transformations.switchMap(orderImage, new Function<OrderImageModel, LiveData<Resource<OrderImageResponseModel>>>() {
            @Override
            public LiveData<Resource<OrderImageResponseModel>> apply(OrderImageModel input) {
                return EditRepo.get().orderImage(getApplication().getApplicationContext(), input);
            }
        });

        myprofileLD = Transformations.switchMap(myprofileRequest, new Function<String, LiveData<Resource<VerificationResponseModel>>>() {
            @Override
            public LiveData<Resource<VerificationResponseModel>> apply(String input) {
                return EditRepo.get().myProfile(getApplication().getApplicationContext(), input);
            }
        });

        sendTokenLD = Transformations.switchMap(sendToken, new Function<String, LiveData<Resource<InstagramImageModel>>>() {
            @Override
            public LiveData<Resource<InstagramImageModel>> apply(String input) {
                return EditRepo.get().sendToken(getApplication().getApplicationContext(), input);
            }
        });
    }

    public void deleteRequest(String s) {
        deleteImage.setValue(s);
    }

    public LiveData<Resource<BaseModel>> deleteResponse() {
        return deleteImageLD;
    }

    public void myProfileRequest(String s) {
        myprofileRequest.setValue(s);
    }

    public LiveData<Resource<VerificationResponseModel>> myProfileResponse() {
        return myprofileLD;
    }


    public void orderImageRequest(OrderImageModel orderImageModel) {
        orderImage.setValue(orderImageModel);
    }

    public LiveData<Resource<OrderImageResponseModel>> orderImageResponse() {
        return orderImageLD;
    }

    public void sendTokenRequest(String s) {
        sendToken.setValue(s);
    }

    public LiveData<Resource<InstagramImageModel>> sendTokenResponse() {
        return sendTokenLD;
    }
}
