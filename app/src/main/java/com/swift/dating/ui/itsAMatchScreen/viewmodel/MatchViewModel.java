package com.swift.dating.ui.itsAMatchScreen.viewmodel;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.swift.dating.data.network.Resource;
import com.swift.dating.model.requestmodel.AnswerProfileRequest;
import com.swift.dating.model.BaseModel;
import com.swift.dating.model.responsemodel.VerificationResponseModel;

public class MatchViewModel extends AndroidViewModel {

    private MutableLiveData<String> unmatch = new MutableLiveData<>();
    private LiveData<Resource<BaseModel>> unmatchLD;

    private MutableLiveData<AnswerProfileRequest> answersProfile = new MutableLiveData<>();
    private LiveData<Resource<VerificationResponseModel>> answersProfileLD;

    public MatchViewModel(@NonNull Application application) {
        super(application);

        unmatchLD = Transformations.switchMap(unmatch, new Function<String, LiveData<Resource<BaseModel>>>() {
            @Override
            public LiveData<Resource<BaseModel>> apply(String input) {
                return MatchRepo.get().unmatch(getApplication().getApplicationContext(), input);
            }
        });

        answersProfileLD = Transformations.switchMap(answersProfile, new Function<AnswerProfileRequest, LiveData<Resource<VerificationResponseModel>>>() {
            @Override
            public LiveData<Resource<VerificationResponseModel>> apply(AnswerProfileRequest input) {
                return MatchRepo.get().answerQuestions(getApplication().getApplicationContext(), input);
            }
        });

    }

    public void unmatchRequest (String s){
        unmatch.setValue(s);
    }

    public LiveData<Resource<BaseModel>> unmatchResponse () {
        return unmatchLD;
    }

    public void answersProfileRequest (AnswerProfileRequest s){
        answersProfile.setValue(s);
    }

    public LiveData<Resource<VerificationResponseModel>> answersProfileResponse () {
        return answersProfileLD;
    }
}
