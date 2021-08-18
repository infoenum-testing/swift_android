package com.swiftdating.app.data.network;


import com.swiftdating.app.model.requestmodel.FilterRequest;
import com.swiftdating.app.model.responsemodel.FilterResponse;
import com.swiftdating.app.model.responsemodel.PhoneLoginResponse;
import com.swiftdating.app.model.responsemodel.VerificationResponseModel;
import com.swiftdating.app.model.responsemodel.WhoLikedYouReponce;
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


     interface FilterCallBack extends BaseInterFace {
        void onSuccessFilter(FilterResponse filterRequest);
    }
}
