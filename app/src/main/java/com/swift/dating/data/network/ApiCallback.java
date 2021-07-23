package com.swift.dating.data.network;


import com.swift.dating.model.responsemodel.PhoneLoginResponse;
import com.swift.dating.model.responsemodel.VerificationResponseModel;
import com.swift.dating.model.responsemodel.WhoLikedYouReponce;
import okhttp3.ResponseBody;

public interface ApiCallback {


    public interface PhoneLoginCallBack extends BaseInterFace {
        void onSuccessPhoneLogin(PhoneLoginResponse response);
    }

    public interface OtpVerifyCallBack extends BaseInterFace {
        void onSuccessOtpVerify(VerificationResponseModel response);
    }

    public interface EmailExistCallBack extends BaseInterFace {
        void onSuccessEmailExist(PhoneLoginResponse response);
    }

    public interface LinkEmailCallBack extends BaseInterFace {
        void onSuccessLinkEmail(PhoneLoginResponse response);
    }

    public interface LinkNumberCallBack extends BaseInterFace {
        void onSuccessLinkNumber(VerificationResponseModel response);
    }

    public interface ReportUserCallBack extends BaseInterFace {
        void onSuccessReportUser(ResponseBody response);
    }

    public interface GetListWhoLikedYouCallback extends BaseInterFace {
        void onSuccessWhoLikedYou(WhoLikedYouReponce response);
    }

    public interface ResetSkippedProfileCallback extends BaseInterFace {
        void onSuccess(String body);
    }

    public interface GetSearchFilterCallback extends BaseInterFace {
        void onSuccessSearchFilterList(WhoLikedYouReponce response);

        void NoUsermatched(String messge);
    }

    public interface GetUserDislikedListCallback extends BaseInterFace {
        void onSuccessDislikedList(WhoLikedYouReponce response);

        void onErrorNoDeluxe(String errorMessage);
    }
}
