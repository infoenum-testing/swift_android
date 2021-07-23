package com.swift.dating.data.network;


import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import com.swift.dating.model.requestmodel.ReportRequestModel;
import com.swift.dating.model.responsemodel.PhoneLoginResponse;
import com.swift.dating.model.responsemodel.VerificationResponseModel;
import com.swift.dating.model.responsemodel.WhoLikedYouReponce;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCall {
    private static final String Bearer = "Bearer ";

    public static void phoneLogin(HashMap<String, Object> map, ApiCallback.PhoneLoginCallBack callBack) {
        ApiUtils apiInterface = CallServer.getClient().create(ApiUtils.class);
        Call<PhoneLoginResponse> forgotPassword = apiInterface.PhoneloginApi(map);
        forgotPassword.enqueue(new Callback<PhoneLoginResponse>() {
            @Override
            public void onResponse(Call<PhoneLoginResponse> call, Response<PhoneLoginResponse> response) {
                if (response.isSuccessful()) {
                    Log.e("json", response.body().toString());
                    callBack.onSuccessPhoneLogin(response.body());
                } else {
                    JSONObject object = null;
                    try {
                        object = new JSONObject(response.errorBody().string());
                        if (response.code() == 404 && object.getString("message")!=null) {
                            //callBack.onError("Your account has been banned for activity that violates our terms & policies.");
                            callBack.onError(object.getString("message"));
                        } else
                            callBack.onError(object.getString("error"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        callBack.onError(CallServer.serverError);
                    }
                }
            }

            @Override
            public void onFailure(Call<PhoneLoginResponse> call, Throwable t) {
                if (t instanceof IOException) {
                    callBack.onError("Server not responding. Please try again later." + call.toString());
                } else {
                    t.printStackTrace();
                    callBack.onError("Something went wrong Please try again later.");
                }
            }
        });


    }
    public static void getListWhoLikedYou(String token, String pagecount, ApiCallback.GetListWhoLikedYouCallback callBack) {
        ApiUtils apiInterface = CallServer.getClient().create(ApiUtils.class);
        Call<ResponseBody> forgotPassword = apiInterface.getUserListWhoLikedYou(token, pagecount);
        forgotPassword.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Gson gson = new Gson();
                        WhoLikedYouReponce reponce = gson.fromJson(response.body().string(), WhoLikedYouReponce.class);
                        callBack.onSuccessWhoLikedYou(reponce);
                    } catch (Exception e) {
                        e.printStackTrace();
                        callBack.onError("Something went wrong.");
                    }
                } else {
                    JSONObject object = null;
                    try {
                        object = new JSONObject(response.errorBody().string());
                        callBack.onError(object.getString("error"));
                    } catch (Exception e) {
                        callBack.onError("Something went wrong.");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t instanceof IOException) {
                    callBack.onError("Server not responding. Please try again later." + call.toString());
                } else {
                    t.printStackTrace();
                    callBack.onError("Something went wrong Please try again later.");
                }
            }
        });


    }

    public static void getUserDislikedList(String token, ApiCallback.GetUserDislikedListCallback callBack) {
        ApiUtils apiInterface = CallServer.getClient().create(ApiUtils.class);
        Call<ResponseBody> forgotPassword = apiInterface.getUserDislikedList(token);
        forgotPassword.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Gson gson = new Gson();
                        WhoLikedYouReponce reponce = gson.fromJson(response.body().string(), WhoLikedYouReponce.class);
                        callBack.onSuccessDislikedList(reponce);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == 404) {
                    Gson gson = new Gson();
                    WhoLikedYouReponce reponce = null;
                    try {
                        reponce = gson.fromJson(response.errorBody().string(), WhoLikedYouReponce.class);
                        callBack.onErrorNoDeluxe(reponce.getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    JSONObject object = null;
                    try {
                        object = new JSONObject(response.errorBody().string());
                        callBack.onError(object.getString("error"));
                    } catch (Exception e) {
                        callBack.onError("Something went wrong Please try again later.");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t instanceof IOException) {
                    callBack.onError("Server not responding. Please try again later." + call.toString());
                } else {
                    t.printStackTrace();
                    callBack.onError("Something went wrong Please try again later.");
                }
            }
        });


    }

    public static void getSearchFilterList(String token, HashMap<String, Object> map, ApiCallback.GetSearchFilterCallback callBack) {
        ApiUtils apiInterface = CallServer.getClient().create(ApiUtils.class);
        Call<ResponseBody> forgotPassword = apiInterface.getSearchFilterList(token, map);
        forgotPassword.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Gson gson = new Gson();
                        WhoLikedYouReponce reponce = gson.fromJson(response.body().string(), WhoLikedYouReponce.class);
                        callBack.onSuccessSearchFilterList(reponce);
                    } catch (IOException e) {
                        callBack.onError("Something went wrong.");
                    }
                } else if (response.code() == 404) {
                    Gson gson = new Gson();
                    WhoLikedYouReponce reponce = null;
                    try {
                        reponce = gson.fromJson(response.errorBody().string(), WhoLikedYouReponce.class);
                        callBack.NoUsermatched(reponce.getMessage());
                    } catch (IOException e) {
                        callBack.onError("Something went wrong.");
                    }
                } else {
                    JSONObject object = null;
                    try {
                        object = new JSONObject(response.errorBody().string());
                        callBack.onError(object.getString("error"));
                    } catch (Exception e) {
                        callBack.onError("Something went wrong.");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t instanceof IOException) {
                    callBack.onError("Server not responding. Please try again later." + call.toString());
                } else {
                    t.printStackTrace();
                    callBack.onError("Something went wrong Please try again later.");
                }
            }
        });
    }

    public static void resetAllSkippedProfile(String token, ApiCallback.ResetSkippedProfileCallback callBack) {
        ApiUtils apiInterface = CallServer.getClient().create(ApiUtils.class);
        Call<ResponseBody> forgotPassword = apiInterface.resetAllSkippedProfile(token);
        forgotPassword.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                JSONObject object;
                if (response.isSuccessful()) {
                    try {
                        object = new JSONObject(response.body().string());
                        callBack.onSuccess(object.getString("message"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        object = new JSONObject(response.errorBody().string());
                        callBack.onError(object.getString("error"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t instanceof IOException) {
                    callBack.onError("Server not responding. Please try again later." + call.toString());
                } else {
                    t.printStackTrace();
                    callBack.onError("Something went wrong Please try again later.");
                }
            }
        });
    }

    public static void OTPVerify(HashMap<String, Object> map, ApiCallback.OtpVerifyCallBack callBack) {
        ApiUtils apiInterface = CallServer.getClient().create(ApiUtils.class);
        Call<VerificationResponseModel> verifyOtpApi = apiInterface.VerifyOtpApi(map);
        verifyOtpApi.enqueue(new Callback<VerificationResponseModel>() {
            @Override
            public void onResponse(Call<VerificationResponseModel> call, Response<VerificationResponseModel> response) {
                if (response.isSuccessful()) {
                    Log.e("json", response.body().toString());
                    callBack.onSuccessOtpVerify(response.body());
                } else {
                    JSONObject object = null;
                    try {
                        object = new JSONObject(response.errorBody().string());
                        callBack.onError(String.valueOf(object.getBoolean("success")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<VerificationResponseModel> call, Throwable t) {
                if (t instanceof IOException) {
                    callBack.onError("Server not responding. Please try again later." + call.toString());
                } else {
                    t.printStackTrace();
                    callBack.onError("Something went wrong Please try again later.");
                }
            }
        });
    }

    public static void CheckEmailExistOrNot(HashMap<String, Object> map, ApiCallback.EmailExistCallBack callBack) {
        ApiUtils apiInterface = CallServer.getClient().create(ApiUtils.class);
        Call<PhoneLoginResponse> forgotPassword = apiInterface.PhoneloginApi(map);
        forgotPassword.enqueue(new Callback<PhoneLoginResponse>() {
            @Override
            public void onResponse(Call<PhoneLoginResponse> call, Response<PhoneLoginResponse> response) {
                if (response.isSuccessful()) {
                    Log.e("json", response.body().toString());
                    callBack.onSuccessEmailExist(response.body());
                } else {
                    JSONObject object = null;
                    try {
                        object = new JSONObject(response.errorBody().string());
                        callBack.onError(object.getString("message"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<PhoneLoginResponse> call, Throwable t) {
                if (t instanceof IOException) {
                    callBack.onError("Server not responding. Please try again later." + call.toString());
                } else {
                    t.printStackTrace();
                    callBack.onError("Something went wrong Please try again later.");
                }
            }
        });

    }

    public static void LinkAccountForEmail(HashMap<String, Object> map, ApiCallback.LinkEmailCallBack callBack) {
        ApiUtils apiInterface = CallServer.getClient().create(ApiUtils.class);
        Call<PhoneLoginResponse> forgotPassword = apiInterface.PhoneloginApi(map);
        forgotPassword.enqueue(new Callback<PhoneLoginResponse>() {
            @Override
            public void onResponse(Call<PhoneLoginResponse> call, Response<PhoneLoginResponse> response) {
                if (response.isSuccessful()) {
                    Log.e("json", response.body().toString());
                    callBack.onSuccessLinkEmail(response.body());
                } else {
                    JSONObject object = null;
                    try {
                        object = new JSONObject(response.errorBody().string());
                        callBack.onError(object.getString("message"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<PhoneLoginResponse> call, Throwable t) {
                if (t instanceof IOException) {
                    callBack.onError("Server not responding. Please try again later." + call.toString());
                } else {
                    t.printStackTrace();
                    callBack.onError("Something went wrong Please try again later.");
                }
            }
        });

    }

    public static void LinkNumber(HashMap<String, Object> map, ApiCallback.LinkNumberCallBack callBack) {
        ApiUtils apiInterface = CallServer.getClient().create(ApiUtils.class);
        Call<VerificationResponseModel> forgotPassword = apiInterface.LinkNumber(map);
        forgotPassword.enqueue(new Callback<VerificationResponseModel>() {
            @Override
            public void onResponse(Call<VerificationResponseModel> call, Response<VerificationResponseModel> response) {
                if (response.isSuccessful()) {
                    Log.e("json", response.body().toString());
                    callBack.onSuccessLinkNumber(response.body());
                } else {
                    JSONObject object = null;
                    try {
                        object = new JSONObject(response.errorBody().string());
                        callBack.onError(object.getString("message"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<VerificationResponseModel> call, Throwable t) {
                if (t instanceof IOException) {
                    callBack.onError("Server not responding. Please try again later." + call.toString());
                } else {
                    t.printStackTrace();
                    callBack.onError("Something went wrong Please try again later.");
                }
            }
        });

    }


    public static void reportUser(String token, ReportRequestModel reportRequestModel, ApiCallback.ReportUserCallBack callBack) {
        ApiUtils apiInterface = CallServer.getClient().create(ApiUtils.class);
        Call<ResponseBody> forgotPassword = apiInterface.reportProfile(token, reportRequestModel);
        forgotPassword.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callBack.onSuccessReportUser(response.body());
                } else {
                    if (response.code() == 500) {
                        callBack.onError("User has already been reported.");
                    } else if (response.code() == 401) {
                        callBack.onError("401");
                    } else {
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response.errorBody().string());
                            callBack.onError(object.getString("message"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t instanceof IOException) {
                    callBack.onError("Server not responding. Please try again later." + call.toString());
                } else {
                    t.printStackTrace();
                    callBack.onError("Something went wrong Please try again later.");
                }
            }
        });

    }

}
