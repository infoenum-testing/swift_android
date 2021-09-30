package com.swiftdating.app.data.network;


import com.swiftdating.app.common.SubscriptionResponse;
import com.swiftdating.app.model.responsemodel.AccessTokenResponce;
import com.swiftdating.app.model.responsemodel.FilterResponse;
import com.swiftdating.app.model.responsemodel.PhoneLoginResponse;
import com.swiftdating.app.model.responsemodel.VerificationResponseModel;
import com.swiftdating.app.model.responsemodel.WhoLikedYouReponce;
import okhttp3.ResponseBody;

public interface ApiCallback {


    interface PhoneLoginCallBack extends BaseInterFace {
        void onSuccessPhoneLogin(PhoneLoginResponse response);
    }

    interface OtpVerifyCallBack extends BaseInterFace {
        void onSuccessOtpVerify(VerificationResponseModel response);
    }

    interface EmailExistCallBack extends BaseInterFace {
        void onSuccessEmailExist(PhoneLoginResponse response);
    }

    interface LinkEmailCallBack extends BaseInterFace {
        void onSuccessLinkEmail(PhoneLoginResponse response);
    }

    interface LinkNumberCallBack extends BaseInterFace {
        void onSuccessLinkNumber(VerificationResponseModel response);
    }

    interface ReportUserCallBack extends BaseInterFace {
        void onSuccessReportUser(ResponseBody response);
    }

    interface GetListWhoLikedYouCallback extends BaseInterFace {
        void onSuccessWhoLikedYou(WhoLikedYouReponce response);
    }

    interface ResetSkippedProfileCallback extends BaseInterFace {
        void onSuccess(String body);
    }

    interface GetSearchFilterCallback extends BaseInterFace {
        void onSuccessSearchFilterList(WhoLikedYouReponce response);

        void NoUsermatched(String messge);
    }

    interface GetUserDislikedListCallback extends BaseInterFace {
        void onSuccessDislikedList(WhoLikedYouReponce response);

        void onErrorNoDeluxe(String errorMessage);
    }


     interface FilterCallBack extends BaseInterFace {
        void onSuccessFilter(FilterResponse filterRequest);
    }

    interface RefreshTokenCallback extends BaseInterFace {
        void onSuccessAccessToken(AccessTokenResponce body, String purchaseToken, String subscriptionId);
    }
    interface PurchaseDetailCallback extends BaseInterFace {
        void onSuccessPurchaseDetail(SubscriptionResponse body);
        void refreshAccessToken(String purchaseToken,String subscriptionId);
    }
}
