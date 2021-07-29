package com.swift.dating.ui.createAccountScreen.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.swift.dating.data.network.Resource;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountBirthModel;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountCityModel;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountDrinkModel;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountEducationModel;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountEmailModel;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountExerciseModel;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountGenderModel;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountHeightModel;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountInterestedModel;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountKidsModel;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountLocationModel;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountNameModel;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountOccupationModel;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountPetModel;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountPoliticalModel;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountQuestionModel;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountRelegionModel;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountSchoolModel;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountSignModel;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountSmokeModel;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccoutAboutModel;
import com.swift.dating.model.responsemodel.VerificationResponseModel;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccoutAmbitionModel;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccoutLookingModel;

public class CreateAccountViewModel extends AndroidViewModel {

    private MutableLiveData<CreateAccountNameModel> nameModel = new MutableLiveData<>();
    private MutableLiveData<CreateAccountEmailModel> emailModel = new MutableLiveData<>();
    private MutableLiveData<CreateAccountLocationModel> locationModel = new MutableLiveData<>();
    private MutableLiveData<CreateAccountBirthModel> birthModel = new MutableLiveData<>();
    private MutableLiveData<CreateAccountGenderModel> genderModel = new MutableLiveData<>();
    private MutableLiveData<CreateAccountInterestedModel> interestedModel = new MutableLiveData<>();
    private MutableLiveData<CreateAccountPetModel> ethnicityModel = new MutableLiveData<>();
    private MutableLiveData<CreateAccountKidsModel> KidsModel = new MutableLiveData<>();
    private MutableLiveData<CreateAccountHeightModel> heightModel = new MutableLiveData<>();
    private MutableLiveData<CreateAccountSignModel> SignModel = new MutableLiveData<>();
    private MutableLiveData<CreateAccountEducationModel> educationModel = new MutableLiveData<>();
    private MutableLiveData<CreateAccountSchoolModel> schoolModel = new MutableLiveData<>();
    private MutableLiveData<CreateAccountOccupationModel> occupationModel = new MutableLiveData<>();
    private MutableLiveData<CreateAccountRelegionModel> religionModel = new MutableLiveData<>();
    private MutableLiveData<CreateAccountPoliticalModel> politicalModel = new MutableLiveData<>();
    private MutableLiveData<CreateAccountDrinkModel> drinkModel = new MutableLiveData<>();
    private MutableLiveData<CreateAccountSmokeModel> smokeModel = new MutableLiveData<>();
    private MutableLiveData<CreateAccountExerciseModel> exerciseModel = new MutableLiveData<>();
    private MutableLiveData<CreateAccountCityModel> cityModel = new MutableLiveData<>();
    private MutableLiveData<CreateAccoutAboutModel> aboutMeModel = new MutableLiveData<>();
    private MutableLiveData<CreateAccoutAmbitionModel> ambitionModel = new MutableLiveData<>();
    private MutableLiveData<CreateAccoutLookingModel> lookingModel = new MutableLiveData<>();
    private MutableLiveData<CreateAccountQuestionModel> questionModel = new MutableLiveData<>();

    private LiveData<Resource<VerificationResponseModel>> nameLD;
    private LiveData<Resource<VerificationResponseModel>> emailLD;
    private LiveData<Resource<VerificationResponseModel>> locationLD;
    private LiveData<Resource<VerificationResponseModel>> birthLD;
    private LiveData<Resource<VerificationResponseModel>> genderLD;
    private LiveData<Resource<VerificationResponseModel>> interestedLD;
    private LiveData<Resource<VerificationResponseModel>> ethnicityLD;
    private LiveData<Resource<VerificationResponseModel>> KidsLD;
    private LiveData<Resource<VerificationResponseModel>> heightLD;
    private LiveData<Resource<VerificationResponseModel>> SignLD;
    private LiveData<Resource<VerificationResponseModel>> educationLD;
    private LiveData<Resource<VerificationResponseModel>> schoolLD;
    private LiveData<Resource<VerificationResponseModel>> occupationLD;
    private LiveData<Resource<VerificationResponseModel>> religionLD;
    private LiveData<Resource<VerificationResponseModel>> politicalLD;
    private LiveData<Resource<VerificationResponseModel>> drinkLD;
    private LiveData<Resource<VerificationResponseModel>> smokeLD;
    private LiveData<Resource<VerificationResponseModel>> exerciseLD;
    private LiveData<Resource<VerificationResponseModel>> cityLD;
    private LiveData<Resource<VerificationResponseModel>> aboutMeLD;
    private LiveData<Resource<VerificationResponseModel>> questionLD;
    private LiveData<Resource<VerificationResponseModel>> ambitionLD;
    private LiveData<Resource<VerificationResponseModel>> lookingLD;

    public CreateAccountViewModel(@NonNull Application application) {
        super(application);

        nameLD = Transformations.switchMap(nameModel, new Function<CreateAccountNameModel, LiveData<Resource<VerificationResponseModel>>>() {
            @Override
            public LiveData<Resource<VerificationResponseModel>> apply(CreateAccountNameModel input) {
                return CreateAccountRepo.get().verify(getApplication().getApplicationContext(), input);
            }
        });

        emailLD = Transformations.switchMap(emailModel, new Function<CreateAccountEmailModel, LiveData<Resource<VerificationResponseModel>>>() {
            @Override
            public LiveData<Resource<VerificationResponseModel>> apply(CreateAccountEmailModel input) {
                return CreateAccountRepo.get().verify(getApplication().getApplicationContext(), input);
            }
        });

        locationLD = Transformations.switchMap(locationModel, new Function<CreateAccountLocationModel, LiveData<Resource<VerificationResponseModel>>>() {
            @Override
            public LiveData<Resource<VerificationResponseModel>> apply(CreateAccountLocationModel input) {
                return CreateAccountRepo.get().verify(getApplication().getApplicationContext(), input);
            }
        });

        birthLD = Transformations.switchMap(birthModel, new Function<CreateAccountBirthModel, LiveData<Resource<VerificationResponseModel>>>() {
            @Override
            public LiveData<Resource<VerificationResponseModel>> apply(CreateAccountBirthModel input) {
                return CreateAccountRepo.get().verify(getApplication().getApplicationContext(), input);
            }
        });

        genderLD = Transformations.switchMap(genderModel, new Function<CreateAccountGenderModel, LiveData<Resource<VerificationResponseModel>>>() {
            @Override
            public LiveData<Resource<VerificationResponseModel>> apply(CreateAccountGenderModel input) {
                return CreateAccountRepo.get().verify(getApplication().getApplicationContext(), input);
            }
        });

        interestedLD = Transformations.switchMap(interestedModel, new Function<CreateAccountInterestedModel, LiveData<Resource<VerificationResponseModel>>>() {
            @Override
            public LiveData<Resource<VerificationResponseModel>> apply(CreateAccountInterestedModel input) {
                return CreateAccountRepo.get().verify(getApplication().getApplicationContext(), input);
            }
        });

        ethnicityLD = Transformations.switchMap(ethnicityModel, new Function<CreateAccountPetModel, LiveData<Resource<VerificationResponseModel>>>() {
            @Override
            public LiveData<Resource<VerificationResponseModel>> apply(CreateAccountPetModel input) {
                return CreateAccountRepo.get().verify(getApplication().getApplicationContext(), input);
            }
        });

        KidsLD = Transformations.switchMap(KidsModel, new Function<CreateAccountKidsModel, LiveData<Resource<VerificationResponseModel>>>() {
            @Override
            public LiveData<Resource<VerificationResponseModel>> apply(CreateAccountKidsModel input) {
                return CreateAccountRepo.get().verify(getApplication().getApplicationContext(), input);
            }
        });

        heightLD = Transformations.switchMap(heightModel, new Function<CreateAccountHeightModel, LiveData<Resource<VerificationResponseModel>>>() {
            @Override
            public LiveData<Resource<VerificationResponseModel>> apply(CreateAccountHeightModel input) {
                return CreateAccountRepo.get().verify(getApplication().getApplicationContext(), input);
            }
        });

        SignLD = Transformations.switchMap(SignModel, new Function<CreateAccountSignModel, LiveData<Resource<VerificationResponseModel>>>() {
            @Override
            public LiveData<Resource<VerificationResponseModel>> apply(CreateAccountSignModel input) {
                return CreateAccountRepo.get().verify(getApplication().getApplicationContext(), input);
            }
        });

        educationLD = Transformations.switchMap(educationModel, new Function<CreateAccountEducationModel, LiveData<Resource<VerificationResponseModel>>>() {
            @Override
            public LiveData<Resource<VerificationResponseModel>> apply(CreateAccountEducationModel input) {
                return CreateAccountRepo.get().verify(getApplication().getApplicationContext(), input);
            }
        });

        schoolLD = Transformations.switchMap(schoolModel, new Function<CreateAccountSchoolModel, LiveData<Resource<VerificationResponseModel>>>() {
            @Override
            public LiveData<Resource<VerificationResponseModel>> apply(CreateAccountSchoolModel input) {
                return CreateAccountRepo.get().verify(getApplication().getApplicationContext(), input);
            }
        });

        occupationLD = Transformations.switchMap(occupationModel, new Function<CreateAccountOccupationModel, LiveData<Resource<VerificationResponseModel>>>() {
            @Override
            public LiveData<Resource<VerificationResponseModel>> apply(CreateAccountOccupationModel input) {
                return CreateAccountRepo.get().verify(getApplication().getApplicationContext(), input);
            }
        });

        religionLD = Transformations.switchMap(religionModel, new Function<CreateAccountRelegionModel, LiveData<Resource<VerificationResponseModel>>>() {
            @Override
            public LiveData<Resource<VerificationResponseModel>> apply(CreateAccountRelegionModel input) {
                return CreateAccountRepo.get().verify(getApplication().getApplicationContext(), input);
            }
        });

        politicalLD = Transformations.switchMap(politicalModel, new Function<CreateAccountPoliticalModel, LiveData<Resource<VerificationResponseModel>>>() {
            @Override
            public LiveData<Resource<VerificationResponseModel>> apply(CreateAccountPoliticalModel input) {
                return CreateAccountRepo.get().verify(getApplication().getApplicationContext(), input);
            }
        });

        drinkLD = Transformations.switchMap(drinkModel, new Function<CreateAccountDrinkModel, LiveData<Resource<VerificationResponseModel>>>() {
            @Override
            public LiveData<Resource<VerificationResponseModel>> apply(CreateAccountDrinkModel input) {
                return CreateAccountRepo.get().verify(getApplication().getApplicationContext(), input);
            }
        });

        smokeLD = Transformations.switchMap(smokeModel, new Function<CreateAccountSmokeModel, LiveData<Resource<VerificationResponseModel>>>() {
            @Override
            public LiveData<Resource<VerificationResponseModel>> apply(CreateAccountSmokeModel input) {
                return CreateAccountRepo.get().verify(getApplication().getApplicationContext(), input);
            }
        });

        exerciseLD = Transformations.switchMap(exerciseModel, new Function<CreateAccountExerciseModel, LiveData<Resource<VerificationResponseModel>>>() {
            @Override
            public LiveData<Resource<VerificationResponseModel>> apply(CreateAccountExerciseModel input) {
                return CreateAccountRepo.get().verify(getApplication().getApplicationContext(), input);
            }
        });

        cityLD = Transformations.switchMap(cityModel, new Function<CreateAccountCityModel, LiveData<Resource<VerificationResponseModel>>>() {
            @Override
            public LiveData<Resource<VerificationResponseModel>> apply(CreateAccountCityModel input) {
                return CreateAccountRepo.get().verify(getApplication().getApplicationContext(), input);
            }
        });

        aboutMeLD = Transformations.switchMap(aboutMeModel, new Function<CreateAccoutAboutModel, LiveData<Resource<VerificationResponseModel>>>() {
            @Override
            public LiveData<Resource<VerificationResponseModel>> apply(CreateAccoutAboutModel input) {
                return CreateAccountRepo.get().verify(getApplication().getApplicationContext(), input);
            }
        });

        questionLD = Transformations.switchMap(questionModel, new Function<CreateAccountQuestionModel, LiveData<Resource<VerificationResponseModel>>>() {
            @Override
            public LiveData<Resource<VerificationResponseModel>> apply(CreateAccountQuestionModel input) {
                return CreateAccountRepo.get().verify(getApplication().getApplicationContext(), input);
            }
        });

        ambitionLD = Transformations.switchMap(ambitionModel, new Function<CreateAccoutAmbitionModel, LiveData<Resource<VerificationResponseModel>>>() {
            @Override
            public LiveData<Resource<VerificationResponseModel>> apply(CreateAccoutAmbitionModel input) {
                return CreateAccountRepo.get().verify(getApplication().getApplicationContext(), input);
            }
        });

        lookingLD = Transformations.switchMap(lookingModel, new Function<CreateAccoutLookingModel, LiveData<Resource<VerificationResponseModel>>>() {
            @Override
            public LiveData<Resource<VerificationResponseModel>> apply(CreateAccoutLookingModel input) {
                return CreateAccountRepo.get().verify(getApplication().getApplicationContext(), input);
            }
        });


    }

    public void verifyRequest(CreateAccountNameModel s) {
        nameModel.setValue(s);
    }


    public void verifyRequest(CreateAccountEmailModel s) {
        emailModel.setValue(s);
    }

    public void verifyRequest(CreateAccountLocationModel s) {
        locationModel.setValue(s);
    }

    public void verifyRequest(CreateAccountBirthModel s) {
        birthModel.setValue(s);
    }

    public void verifyRequest(CreateAccountGenderModel s) {
        genderModel.setValue(s);
    }

    public void verifyRequest(CreateAccountInterestedModel s) {
        interestedModel.setValue(s);
    }

    public void verifyRequest(CreateAccountPetModel s) {
        ethnicityModel.setValue(s);
    }

    public void verifyRequest(CreateAccountKidsModel s) {
        KidsModel.setValue(s);
    }

    public void verifyRequest(CreateAccountHeightModel s) {
        heightModel.setValue(s);
    }

    public void verifyRequest(CreateAccountSignModel s) {
        SignModel.setValue(s);
    }

    public void verifyRequest(CreateAccountEducationModel s) {
        educationModel.setValue(s);
    }

    public void verifyRequest(CreateAccountSchoolModel s) {
        schoolModel.setValue(s);
    }

    public void verifyRequest(CreateAccountOccupationModel s) {
        occupationModel.setValue(s);
    }

    public void verifyRequest(CreateAccountRelegionModel s) {
        religionModel.setValue(s);
    }

    public void verifyRequest(CreateAccountPoliticalModel s) {
        politicalModel.setValue(s);
    }

    public void verifyRequest(CreateAccountDrinkModel s) {
        drinkModel.setValue(s);
    }

    public void verifyRequest(CreateAccountSmokeModel s) {
        smokeModel.setValue(s);
    }

    public void verifyRequest(CreateAccountExerciseModel s) {
        exerciseModel.setValue(s);
    }

    public void verifyRequest(CreateAccountCityModel s) {
        cityModel.setValue(s);
    }

    public void verifyRequest(CreateAccoutAboutModel s) {
        aboutMeModel.setValue(s);
    }

    public void verifyRequest(CreateAccoutLookingModel s) {
        lookingModel.setValue(s);
    }

    public void verifyRequest(CreateAccoutAmbitionModel s) {
        ambitionModel.setValue(s);
    }

    public void verifyRequest(CreateAccountQuestionModel s) {
        questionModel.setValue(s);
    }


    public LiveData<Resource<VerificationResponseModel>> nameResponse() {
        return nameLD;
    }
    public LiveData<Resource<VerificationResponseModel>> emailResponse() {
        return emailLD;
    }
    public LiveData<Resource<VerificationResponseModel>> locationResponse() {
        return locationLD;
    }public LiveData<Resource<VerificationResponseModel>> birthResponse() {
        return birthLD;
    }public LiveData<Resource<VerificationResponseModel>> genderResponse() {
        return genderLD;
    }public LiveData<Resource<VerificationResponseModel>> interestedResponse() {
        return interestedLD;
    }public LiveData<Resource<VerificationResponseModel>> ethnicityResponse() {
        return ethnicityLD;
    }public LiveData<Resource<VerificationResponseModel>> KidsResponse() {
        return KidsLD;
    }public LiveData<Resource<VerificationResponseModel>> heightResponse() {
        return heightLD;
    }public LiveData<Resource<VerificationResponseModel>> SignResponse() {
        return SignLD;
    }public LiveData<Resource<VerificationResponseModel>> educationResponse() {
        return educationLD;
    }public LiveData<Resource<VerificationResponseModel>> schoolResponse() {
        return schoolLD;
    }public LiveData<Resource<VerificationResponseModel>> occupationResponse() {
        return occupationLD;
    }public LiveData<Resource<VerificationResponseModel>> religionResponse() {
        return religionLD;
    }public LiveData<Resource<VerificationResponseModel>> politicalResponse() {
        return politicalLD;
    }public LiveData<Resource<VerificationResponseModel>> drinkResponse() {
        return drinkLD;
    }public LiveData<Resource<VerificationResponseModel>> smokeResponse() {
        return smokeLD;
    }public LiveData<Resource<VerificationResponseModel>> exerciseResponse() {
        return exerciseLD;
    }public LiveData<Resource<VerificationResponseModel>> cityResponse() {
        return cityLD;
    }public LiveData<Resource<VerificationResponseModel>> aboutResponse() {
        return aboutMeLD;
    }public LiveData<Resource<VerificationResponseModel>> ambitionResponse() {
        return ambitionLD;
    }public LiveData<Resource<VerificationResponseModel>> lookingResponse() {
        return lookingLD;
    }public LiveData<Resource<VerificationResponseModel>> questionResponse() {
        return questionLD;
    }

}

