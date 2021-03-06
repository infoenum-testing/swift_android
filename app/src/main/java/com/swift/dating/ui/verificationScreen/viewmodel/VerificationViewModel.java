package com.swift.dating.ui.verificationScreen.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.swift.dating.data.network.Resource;
import com.swift.dating.model.requestmodel.VerficationRequestModel;
import com.swift.dating.model.responsemodel.VerificationResponseModel;

public class VerificationViewModel extends AndroidViewModel {

    private MutableLiveData<VerficationRequestModel> signIn = new MutableLiveData<>();
    private LiveData<Resource<VerificationResponseModel>> signInLD;

    public VerificationViewModel(@NonNull Application application) {
        super(application);

        signInLD = Transformations.switchMap(signIn, new Function<VerficationRequestModel, LiveData<Resource<VerificationResponseModel>>>() {
            @Override
            public LiveData<Resource<VerificationResponseModel>> apply(VerficationRequestModel input) {
                return VerificationRepo.get().verify(getApplication().getApplicationContext(), input);
            }
        });

    }

    public void verifyRequest(VerficationRequestModel s) {
        signIn.setValue(s);
    }

    public LiveData<Resource<VerificationResponseModel>> verifyResponse() {
        return signInLD;
    }
}
