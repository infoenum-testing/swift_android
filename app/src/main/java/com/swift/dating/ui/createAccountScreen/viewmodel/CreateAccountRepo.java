package com.swift.dating.ui.createAccountScreen.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.swift.dating.data.network.CallServer;
import com.swift.dating.data.network.Resource;
import com.swift.dating.data.preference.SharedPreference;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountBirthModel;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountCityModel;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountDrinkModel;
import com.swift.dating.model.requestmodel.createaccountmodel.CreateAccountEducationModel;
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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccountRepo {

    public static CreateAccountRepo get() {
        return new CreateAccountRepo();
    }

    LiveData<Resource<VerificationResponseModel>> verify(final Context c, CreateAccountNameModel obj) {
        final MutableLiveData<Resource<VerificationResponseModel>> data = new MutableLiveData<>();
        data.setValue(Resource.<VerificationResponseModel>loading(null));
        SharedPreference sp = new SharedPreference(c);
        CallServer.get().getAPIName().updateProfile(sp.getToken(), obj).enqueue(new Callback<ResponseBody>() {
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
                    data.setValue(Resource.<VerificationResponseModel>error("Something Went Wrong", null, 0,e));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                data.setValue(Resource.<VerificationResponseModel>error(CallServer.serverError, null, 0, t));
            }
        });

        return data;
    }

    LiveData<Resource<VerificationResponseModel>> verify(final Context c, CreateAccountLocationModel obj) {
        final MutableLiveData<Resource<VerificationResponseModel>> data = new MutableLiveData<>();
        data.setValue(Resource.<VerificationResponseModel>loading(null));
        SharedPreference sp = new SharedPreference(c);
        CallServer.get().getAPIName().updateProfile(sp.getToken(), obj).enqueue(new Callback<ResponseBody>() {
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
                    data.setValue(Resource.<VerificationResponseModel>error("Something Went Wrong", null, 0,e));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                data.setValue(Resource.<VerificationResponseModel>error(CallServer.serverError, null, 0, t));
            }
        });

        return data;
    }


    LiveData<Resource<VerificationResponseModel>> verify(final Context c, CreateAccountBirthModel obj) {
        final MutableLiveData<Resource<VerificationResponseModel>> data = new MutableLiveData<>();
        data.setValue(Resource.<VerificationResponseModel>loading(null));
        SharedPreference sp = new SharedPreference(c);
        CallServer.get().getAPIName().updateProfile(sp.getToken(),obj).enqueue(new Callback<ResponseBody>() {
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
                    data.setValue(Resource.<VerificationResponseModel>error("Something Went Wrong", null, 0,e));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                data.setValue(Resource.<VerificationResponseModel>error(CallServer.serverError, null, 0, t));
            }
        });

        return data;
    }

    LiveData<Resource<VerificationResponseModel>> verify(final Context c, CreateAccountGenderModel obj) {
        final MutableLiveData<Resource<VerificationResponseModel>> data = new MutableLiveData<>();
        data.setValue(Resource.<VerificationResponseModel>loading(null));
        SharedPreference sp = new SharedPreference(c);
        CallServer.get().getAPIName().updateProfile(sp.getToken(),obj).enqueue(new Callback<ResponseBody>() {
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
                    data.setValue(Resource.<VerificationResponseModel>error("Something Went Wrong", null, 0,e));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                data.setValue(Resource.<VerificationResponseModel>error(CallServer.serverError, null, 0, t));
            }
        });

        return data;
    }

    LiveData<Resource<VerificationResponseModel>> verify(final Context c, CreateAccountInterestedModel obj) {
        final MutableLiveData<Resource<VerificationResponseModel>> data = new MutableLiveData<>();
        data.setValue(Resource.<VerificationResponseModel>loading(null));
        SharedPreference sp = new SharedPreference(c);
        CallServer.get().getAPIName().updateProfile(sp.getToken(),obj).enqueue(new Callback<ResponseBody>() {
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
                    data.setValue(Resource.<VerificationResponseModel>error("Something Went Wrong", null, 0,e));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                data.setValue(Resource.<VerificationResponseModel>error(CallServer.serverError, null, 0, t));
            }
        });

        return data;
    }

    LiveData<Resource<VerificationResponseModel>> verify(final Context c, CreateAccountPetModel obj) {
        final MutableLiveData<Resource<VerificationResponseModel>> data = new MutableLiveData<>();
        data.setValue(Resource.<VerificationResponseModel>loading(null));
        SharedPreference sp = new SharedPreference(c);
        CallServer.get().getAPIName().updateProfile(sp.getToken(),obj).enqueue(new Callback<ResponseBody>() {
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
                    data.setValue(Resource.<VerificationResponseModel>error("Something Went Wrong", null, 0,e));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                data.setValue(Resource.<VerificationResponseModel>error(CallServer.serverError, null, 0, t));
            }
        });

        return data;
    }

    LiveData<Resource<VerificationResponseModel>> verify(final Context c, CreateAccountKidsModel obj) {
        final MutableLiveData<Resource<VerificationResponseModel>> data = new MutableLiveData<>();
        data.setValue(Resource.<VerificationResponseModel>loading(null));
        SharedPreference sp = new SharedPreference(c);
        CallServer.get().getAPIName().updateProfile(sp.getToken(),obj).enqueue(new Callback<ResponseBody>() {
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
                    data.setValue(Resource.<VerificationResponseModel>error("Something Went Wrong", null, 0,e));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                data.setValue(Resource.<VerificationResponseModel>error(CallServer.serverError, null, 0, t));
            }
        });

        return data;
    }

    LiveData<Resource<VerificationResponseModel>> verify(final Context c, CreateAccountHeightModel obj) {
        final MutableLiveData<Resource<VerificationResponseModel>> data = new MutableLiveData<>();
        data.setValue(Resource.<VerificationResponseModel>loading(null));
        SharedPreference sp = new SharedPreference(c);
        CallServer.get().getAPIName().updateProfile(sp.getToken(),obj).enqueue(new Callback<ResponseBody>() {
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
                    data.setValue(Resource.<VerificationResponseModel>error("Something Went Wrong", null, 0,e));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                data.setValue(Resource.<VerificationResponseModel>error(CallServer.serverError, null, 0, t));
            }
        });

        return data;
    }

    LiveData<Resource<VerificationResponseModel>> verify(final Context c, CreateAccountSignModel obj) {
        final MutableLiveData<Resource<VerificationResponseModel>> data = new MutableLiveData<>();
        data.setValue(Resource.<VerificationResponseModel>loading(null));
        SharedPreference sp = new SharedPreference(c);
        CallServer.get().getAPIName().updateProfile(sp.getToken(),obj).enqueue(new Callback<ResponseBody>() {
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
                    data.setValue(Resource.<VerificationResponseModel>error("Something Went Wrong", null, 0,e));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                data.setValue(Resource.<VerificationResponseModel>error(CallServer.serverError, null, 0, t));
            }
        });

        return data;
    }

    LiveData<Resource<VerificationResponseModel>> verify(final Context c, CreateAccountEducationModel obj) {
        final MutableLiveData<Resource<VerificationResponseModel>> data = new MutableLiveData<>();
        data.setValue(Resource.<VerificationResponseModel>loading(null));
        SharedPreference sp = new SharedPreference(c);
        CallServer.get().getAPIName().updateProfile(sp.getToken(),obj).enqueue(new Callback<ResponseBody>() {
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
                    data.setValue(Resource.<VerificationResponseModel>error("Something Went Wrong", null, 0,e));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                data.setValue(Resource.<VerificationResponseModel>error(CallServer.serverError, null, 0, t));
            }
        });

        return data;
    }

    LiveData<Resource<VerificationResponseModel>> verify(final Context c, CreateAccountSchoolModel obj) {
        final MutableLiveData<Resource<VerificationResponseModel>> data = new MutableLiveData<>();
        data.setValue(Resource.<VerificationResponseModel>loading(null));
        SharedPreference sp = new SharedPreference(c);
        CallServer.get().getAPIName().updateProfile(sp.getToken(),obj).enqueue(new Callback<ResponseBody>() {
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
                    data.setValue(Resource.<VerificationResponseModel>error("Something Went Wrong", null, 0,e));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                data.setValue(Resource.<VerificationResponseModel>error(CallServer.serverError, null, 0, t));
            }
        });

        return data;
    }

    LiveData<Resource<VerificationResponseModel>> verify(final Context c, CreateAccountOccupationModel obj) {
        final MutableLiveData<Resource<VerificationResponseModel>> data = new MutableLiveData<>();
        data.setValue(Resource.<VerificationResponseModel>loading(null));
        SharedPreference sp = new SharedPreference(c);
        CallServer.get().getAPIName().updateProfile(sp.getToken(),obj).enqueue(new Callback<ResponseBody>() {
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
                    data.setValue(Resource.<VerificationResponseModel>error("Something Went Wrong", null, 0,e));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                data.setValue(Resource.<VerificationResponseModel>error(CallServer.serverError, null, 0, t));
            }
        });

        return data;
    }

    LiveData<Resource<VerificationResponseModel>> verify(final Context c, CreateAccountRelegionModel obj) {
        final MutableLiveData<Resource<VerificationResponseModel>> data = new MutableLiveData<>();
        data.setValue(Resource.<VerificationResponseModel>loading(null));
        SharedPreference sp = new SharedPreference(c);
        CallServer.get().getAPIName().updateProfile(sp.getToken(),obj).enqueue(new Callback<ResponseBody>() {
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
                    data.setValue(Resource.<VerificationResponseModel>error("Something Went Wrong", null, 0,e));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                data.setValue(Resource.<VerificationResponseModel>error(CallServer.serverError, null, 0, t));
            }
        });

        return data;
    }

    LiveData<Resource<VerificationResponseModel>> verify(final Context c, CreateAccountPoliticalModel obj) {
        final MutableLiveData<Resource<VerificationResponseModel>> data = new MutableLiveData<>();
        data.setValue(Resource.<VerificationResponseModel>loading(null));
        SharedPreference sp = new SharedPreference(c);
        CallServer.get().getAPIName().updateProfile(sp.getToken(),obj).enqueue(new Callback<ResponseBody>() {
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
                    data.setValue(Resource.<VerificationResponseModel>error("Something Went Wrong", null, 0,e));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                data.setValue(Resource.<VerificationResponseModel>error(CallServer.serverError, null, 0, t));
            }
        });

        return data;
    }

    LiveData<Resource<VerificationResponseModel>> verify(final Context c, CreateAccountDrinkModel obj) {
        final MutableLiveData<Resource<VerificationResponseModel>> data = new MutableLiveData<>();
        data.setValue(Resource.<VerificationResponseModel>loading(null));
        SharedPreference sp = new SharedPreference(c);
        CallServer.get().getAPIName().updateProfile(sp.getToken(),obj).enqueue(new Callback<ResponseBody>() {
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
                    data.setValue(Resource.<VerificationResponseModel>error("Something Went Wrong", null, 0,e));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                data.setValue(Resource.<VerificationResponseModel>error(CallServer.serverError, null, 0, t));
            }
        });

        return data;
    }

    LiveData<Resource<VerificationResponseModel>> verify(final Context c, CreateAccountSmokeModel obj) {
        final MutableLiveData<Resource<VerificationResponseModel>> data = new MutableLiveData<>();
        data.setValue(Resource.<VerificationResponseModel>loading(null));
        SharedPreference sp = new SharedPreference(c);
        CallServer.get().getAPIName().updateProfile(sp.getToken(),obj).enqueue(new Callback<ResponseBody>() {
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
                    data.setValue(Resource.<VerificationResponseModel>error("Something Went Wrong", null, 0,e));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                data.setValue(Resource.<VerificationResponseModel>error(CallServer.serverError, null, 0, t));
            }
        });

        return data;
    }

    LiveData<Resource<VerificationResponseModel>> verify(final Context c, CreateAccountExerciseModel obj) {
        final MutableLiveData<Resource<VerificationResponseModel>> data = new MutableLiveData<>();
        data.setValue(Resource.<VerificationResponseModel>loading(null));
        SharedPreference sp = new SharedPreference(c);
        CallServer.get().getAPIName().updateProfile(sp.getToken(),obj).enqueue(new Callback<ResponseBody>() {
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
                    data.setValue(Resource.<VerificationResponseModel>error("Something Went Wrong", null, 0,e));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                data.setValue(Resource.<VerificationResponseModel>error(CallServer.serverError, null, 0, t));
            }
        });

        return data;
    }

    LiveData<Resource<VerificationResponseModel>> verify(final Context c, CreateAccountCityModel obj) {
        final MutableLiveData<Resource<VerificationResponseModel>> data = new MutableLiveData<>();
        data.setValue(Resource.<VerificationResponseModel>loading(null));
        SharedPreference sp = new SharedPreference(c);
        CallServer.get().getAPIName().updateProfile(sp.getToken(),obj).enqueue(new Callback<ResponseBody>() {
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
                    data.setValue(Resource.<VerificationResponseModel>error("Something Went Wrong", null, 0,e));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                data.setValue(Resource.<VerificationResponseModel>error(CallServer.serverError, null, 0, t));
            }
        });

        return data;
    }

    LiveData<Resource<VerificationResponseModel>> verify(final Context c, CreateAccoutAboutModel obj) {
        final MutableLiveData<Resource<VerificationResponseModel>> data = new MutableLiveData<>();
        data.setValue(Resource.<VerificationResponseModel>loading(null));
        SharedPreference sp = new SharedPreference(c);
        CallServer.get().getAPIName().updateProfile(sp.getToken(),obj).enqueue(new Callback<ResponseBody>() {
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
                    data.setValue(Resource.<VerificationResponseModel>error("Something Went Wrong", null, 0,e));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                data.setValue(Resource.<VerificationResponseModel>error(CallServer.serverError, null, 0, t));
            }
        });

        return data;
    }

    LiveData<Resource<VerificationResponseModel>> verify(final Context c, CreateAccountQuestionModel obj) {
        final MutableLiveData<Resource<VerificationResponseModel>> data = new MutableLiveData<>();
        data.setValue(Resource.<VerificationResponseModel>loading(null));
        SharedPreference sp = new SharedPreference(c);
        CallServer.get().getAPIName().updateProfile(sp.getToken(),obj).enqueue(new Callback<ResponseBody>() {
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
                    data.setValue(Resource.<VerificationResponseModel>error("Something Went Wrong", null, 0,e));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                data.setValue(Resource.<VerificationResponseModel>error(CallServer.serverError, null, 0, t));
            }
        });

        return data;
    }

    LiveData<Resource<VerificationResponseModel>> verify(final Context c, CreateAccoutAmbitionModel obj) {
        final MutableLiveData<Resource<VerificationResponseModel>> data = new MutableLiveData<>();
        data.setValue(Resource.<VerificationResponseModel>loading(null));
        SharedPreference sp = new SharedPreference(c);
        CallServer.get().getAPIName().updateProfile(sp.getToken(),obj).enqueue(new Callback<ResponseBody>() {
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
                    data.setValue(Resource.<VerificationResponseModel>error("Something Went Wrong", null, 0,e));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                data.setValue(Resource.<VerificationResponseModel>error(CallServer.serverError, null, 0, t));
            }
        });

        return data;
    }

    LiveData<Resource<VerificationResponseModel>> verify(final Context c, CreateAccoutLookingModel obj) {
        final MutableLiveData<Resource<VerificationResponseModel>> data = new MutableLiveData<>();
        data.setValue(Resource.<VerificationResponseModel>loading(null));
        SharedPreference sp = new SharedPreference(c);
        CallServer.get().getAPIName().updateProfile(sp.getToken(),obj).enqueue(new Callback<ResponseBody>() {
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
                    data.setValue(Resource.<VerificationResponseModel>error("Something Went Wrong", null, 0,e));
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
