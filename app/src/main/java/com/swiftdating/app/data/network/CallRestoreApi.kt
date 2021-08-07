package com.swiftdating.app.data.network

import android.util.Log
import com.swiftdating.app.common.CommonDialogs

import com.swiftdating.app.data.preference.SharedPreference
import com.swiftdating.app.model.BaseModel
import com.swiftdating.app.model.requestmodel.PremiumTokenCountModel
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit

class CallRestoreApi {


    public fun callApi(mPremiumTokenCountModel: PremiumTokenCountModel, spr: SharedPreference, subscriptiontype: String,onPurchaseRestore:CommonDialogs.onPurchaseRestore) {
        var api = CallServer.get().apiName
        val github = Retrofit.Builder()
                .baseUrl(CallServer.BASE_URL)
                .build()
                .create(ApiUtils::class.java)

        var call = api.checkSubscription(spr.token, mPremiumTokenCountModel)

        call.enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val gson = Gson()
                    if (response.isSuccessful) {
                        val responseBean: BaseModel = gson.fromJson(response.body()!!.string(), BaseModel::class.java)
                        if (responseBean.isavaiable) {
                            if (subscriptiontype == "1")
                                spr.savePremium(false)
                            else if (subscriptiontype == "2") {
                                spr.saveDeluxe(false)
                            }
                            onPurchaseRestore.onError("Purchase Restore unavailable.")
                        } else {
                            if (subscriptiontype == "1")
                                spr.savePremium(true)
                            else if (subscriptiontype == "2")
                                spr.saveDeluxe(true)
                            onPurchaseRestore.onSucces()
                        }
                    } else {
                        onPurchaseRestore.onError(response.errorBody().toString())
                        Log.e("check1", response.errorBody().toString())
                    }
                } catch (e: java.lang.Exception) {
                    onPurchaseRestore.onError("Something went wrong")
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable?) {
                onPurchaseRestore.onError("Purchase Restore unavailable.")
                Log.e("check1", t.toString())
            }
        })
    }

}