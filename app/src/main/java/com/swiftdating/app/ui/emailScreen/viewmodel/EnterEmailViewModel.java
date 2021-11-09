package com.swiftdating.app.ui.emailScreen.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.swiftdating.app.data.network.Resource;
import com.swiftdating.app.model.BaseModel;
import com.swiftdating.app.model.requestmodel.ResendRequest;
import com.swiftdating.app.model.requestmodel.SignUpRequestModel;
import com.swiftdating.app.model.responsemodel.VerificationResponseModel;

public class EnterEmailViewModel extends AndroidViewModel {

    private MutableLiveData<SignUpRequestModel> signIn = new MutableLiveData<>();
    private LiveData<Resource<VerificationResponseModel>> signInLD;
    private MutableLiveData<ResendRequest> resendRequest = new MutableLiveData<>();
    private LiveData<Resource<BaseModel>> resendLD;

    public EnterEmailViewModel(@NonNull Application application) {
        super(application);

        signInLD = Transformations.switchMap(signIn, new Function<SignUpRequestModel, LiveData<Resource<VerificationResponseModel>>>() {
            @Override
            public LiveData<Resource<VerificationResponseModel>> apply(SignUpRequestModel input) {
                return EnterEmailRepo.get().loginRegister(getApplication().getApplicationContext(), input);
            }
        });


        resendLD = Transformations.switchMap(resendRequest, new Function<ResendRequest, LiveData<Resource<BaseModel>>>() {
            @Override
            public LiveData<Resource<BaseModel>> apply(ResendRequest input) {
                return EnterEmailRepo.get().resendOtp(getApplication().getApplicationContext(), input);
            }
        });

    }

    public void signInRequest(SignUpRequestModel s) {
        signIn.setValue(s);
    }

    public LiveData<Resource<VerificationResponseModel>> signInResponse() {
        return signInLD;
    }




    public void resendRequest(ResendRequest s) {
        resendRequest.setValue(s);
    }


    public LiveData<Resource<BaseModel>> resendResponse() {
        return resendLD;
    }

}
