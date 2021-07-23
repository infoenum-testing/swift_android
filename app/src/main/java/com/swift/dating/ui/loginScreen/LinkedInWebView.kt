package com.swift.dating.ui.loginScreen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.swift.dating.R
import com.swift.dating.model.responsemodel.GetEmailResponseModel
import com.swift.dating.ui.base.BaseActivity
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_common_web_view.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class LinkedInWebView : BaseActivity() {
    private val API_KEY = "7884wouiik0t5j"
    private val STATE = "DCEeFWf45A53sdfKef4242"
    private val REDIRECT_URI = "http://192.168.1.75:3020/api/users/callback"
    private val AUTHORIZATION_URL = "https://www.linkedin.com/uas/oauth2/authorization"
    private val RESPONSE_TYPE_VALUE = "code"
    private val STATE_PARAM = "state"
    private val ACCESS_TOKEN_URL = "https://www.linkedin.com/uas/oauth2/accessToken"

    private val authorizationUrl: String
        get() = ("$AUTHORIZATION_URL?response_type=code&client_id=$API_KEY&redirect_uri=$REDIRECT_URI" +
                "&state=$STATE&scope=r_liteprofile%20r_emailaddress%20w_member_social")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_web_view)
        webView1.requestFocus(View.FOCUS_DOWN)
        webView1.clearHistory()
        webView1.clearCache(true)
        webView1.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, authorizationUrl: String): Boolean {
//                This method will be called when the Auth proccess redirect to our RedirectUri.
//                We will check the url looking for our RedirectUri.
                if (!authorizationUrl.contains("user_cancelled_login")) {

                    if (authorizationUrl.startsWith(REDIRECT_URI)) {
                        val uri = Uri.parse(authorizationUrl)
                        //We take from the url the authorizationToken and the state token. We have to check that the state token returned by the Service is the same we sent.
                        //If not, that means the request may be a linkedInProgress of CSRF and must be rejected.
                        val stateToken = uri.getQueryParameter(STATE_PARAM)
                        if (stateToken == null || stateToken != STATE) {
                            Log.e("text", "State token doesn't match")
                            return true
                        }

                        //If the user doesn't allow authorization to our application, the authorizationToken Will be null.
                        val authorizationToken = uri.getQueryParameter(RESPONSE_TYPE_VALUE)
                        if (authorizationToken == null) {
                            Log.e("text", "The user doesn't allow authorization.")
                            return true
                        }
                        Log.e("text", "Auth token received: $authorizationToken!!")

                        webView1.loadUrl(authorizationUrl)
                        //Generate URL for requesting Access Token

                        getAccessToke(authorizationToken)
                    } else {
                        //Default behaviour
                        Log.e("text", "Redirecting to: $authorizationUrl")
                        webView1.loadUrl(authorizationUrl)
                    }
                    return true
                } else {
                    finish();
                    return true
                }
            }
        }

        Log.e("text", "Loading Url: $authorizationUrl")
        webView1.loadUrl(authorizationUrl)
    }

    private fun getAccessTokenUrl(authorizationToken: String?): String {
        return ("$ACCESS_TOKEN_URL?grant_type=authorization_code&"
                + "code=$authorizationToken&client_id=$API_KEY&redirect_uri=$REDIRECT_URI&"
                + "client_secret=MI2UWHX5dLArfJ05")
    }

    fun getAccessToke(code: String) {
        Log.e("Text ", "Auth code received: $code")
        val url = getAccessTokenUrl(code)
        Log.e("Text ", "Auth code url: $url")

        val request = Request.Builder()
                .url(url)
                .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val res = response.body?.string()
                    try {
                        val json = JSONObject(res)
                        val token = json.getString("access_token")
                        getProfile(token)
                    } catch (ex: JSONException) {
                        ex.printStackTrace()
                        //_progress.postValue(false)
                    }
                } else {
                    //_progress.postValue(false)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                //_progress.postValue(false)
            }

        })

    }

    private var PROFILE_URL =
            "https://api.linkedin.com/v2/me?projection=(id,firstName,lastName,profilePicture(displayImage~:playableStreams))"


    private val TOKEN_TYPE = "Bearer "
    private fun getProfile(token: String) {

        val request = Request.Builder()
                .url(PROFILE_URL)
                .header("Authorization", TOKEN_TYPE + token)
                .build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val res = response.body?.string()
                    try {
                        var firstName = ""
                        var id = ""
                        val json = JSONObject(res)
                        if (json.has("firstName") && json.getJSONObject("firstName")
                                        .has("localized") && json.getJSONObject("firstName")
                                        .getJSONObject("localized").has("en_US"))
                            firstName = json.getJSONObject("firstName")
                                    .getJSONObject("localized").getString("en_US")

                        if (json.has("id"))
                            id = json.getString("id")
                        getEmail(token, firstName, id)
//

                    } catch (ex: JSONException) {
                        ex.printStackTrace()

                    }
                } else {
                    showSnackbar(webView1, response.message)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                //_progress.postValue(false)
            }

        })
    }


    private var EMAIL_URL =
            "https://api.linkedin.com/v2/emailAddress?q=members&projection=(elements*(handle~))"

    private fun getEmail(token: String, firstName: String, id: String) {
        val request = Request.Builder()
                .url(EMAIL_URL)
                .header("Authorization", "Bearer $token")
                .build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    var gson = GsonBuilder().setLenient().create()

                    if (response.code == 200) {
                        val responseBean: GetEmailResponseModel = gson.fromJson<GetEmailResponseModel>(response.body!!.string(), GetEmailResponseModel::class.java)

                        val data = Intent()
                        data.putExtra("name", firstName)
                        data.putExtra("id", id)
                        data.putExtra("email", responseBean.elements[0].handle.emailAddress)
                        setResult(Activity.RESULT_OK, data);
                        finish()
                    }
                } else {
                    showSnackbar(webView1, response.message)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                //_progress.postValue(false)
            }

        })
    }

}