package com.swift.dating.ui.welcomeScreen

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.swift.dating.DummyActivity
import com.swift.dating.R
import com.swift.dating.data.network.CallServer
import com.swift.dating.data.network.Resource
import com.swift.dating.data.preference.SharedPreference
import com.swift.dating.model.responsemodel.ProfileOfUser
import com.swift.dating.model.responsemodel.VerificationResponseModel
import com.swift.dating.ui.base.BaseActivity
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
            callApi()
        }
    }

    private fun callApi() {
        val map = HashMap<String, String>()
        map["agree"] = "Yes"

        showLoading()
        val data = MutableLiveData<Resource<VerificationResponseModel>>()
        data.value = Resource.loading(null)


        CallServer.get().apiName.completeRegistration(sp.token, map).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                hideLoading()
                try {
                    val gson = Gson()
                    if (response.code() == 200) {
                        val verificationResponseModel = gson.fromJson(response.body()?.string(), VerificationResponseModel::class.java)
                        val user: String = sp.user
                        val obj = gson.fromJson(user, verificationResponseModel::class.java)
                        obj.user = verificationResponseModel.user
                        sp.saveUserData(obj.user.profileOfUser, verificationResponseModel.user.profileOfUser.completed.toString())
                        sp.saveString(SharedPreference.userStatus, verificationResponseModel.user.status);
                        val i: Intent? = Intent(mActivity, DummyActivity::class.java)
                        startActivity(i)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        finishAffinity()
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
                hideLoading()
                //data.value = Resource.error(CallServer.serverError, null, 0, t)
                showSnackbar(btn_continue, t.message)
            }
        })
    }
}
