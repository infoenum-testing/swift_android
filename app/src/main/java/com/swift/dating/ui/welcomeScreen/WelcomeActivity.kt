package com.swift.dating.ui.welcomeScreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.swift.dating.R
import com.swift.dating.common.CommonUtils
import com.swift.dating.data.network.CallServer
import com.swift.dating.data.network.Resource
import com.swift.dating.model.responsemodel.VerificationResponseModel
import com.swift.dating.ui.base.BaseActivity
import com.swift.dating.ui.createAccountScreen.CreateAccountActivity
import kotlinx.android.synthetic.main.activity_welcome.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class WelcomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        btn_continue.setOnClickListener {
           // callApi()
            val i: Intent? = Intent(mActivity, CreateAccountActivity::class.java).putExtra("parseCount", 1)
            startActivity(i)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    private fun callApi() {
        val map = HashMap<String, String>()
        map["agree"] = "Yes"

        val data = MutableLiveData<Resource<VerificationResponseModel>>()
        data.value = Resource.loading(null)


        CallServer.get().apiName.completeRegistration(sp.token, map).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val gson = GsonBuilder().setLenient().create()
                    if (response.code() == 200) {
                        sp.saveBoolean(CommonUtils.isCompleteRegistration, true);
                        val responseBean = gson.fromJson(response.body()!!.string(), VerificationResponseModel::class.java)
                        data.setValue(Resource.success(responseBean))
                    } else if (response.code() == 400) {
                        val responseBean = gson.fromJson(response.errorBody()!!.string(), VerificationResponseModel::class.java)
                        data.setValue(Resource.success(responseBean))
                    } else {
                        val responseBean = gson.fromJson(response.errorBody()!!.string(), VerificationResponseModel::class.java)
                        data.setValue(Resource.success(responseBean))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                data.value = Resource.error(CallServer.serverError, null, 0, t)
            }
        })
    }
}
