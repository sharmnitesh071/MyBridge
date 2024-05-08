package com.databridge.mybridge.common

import com.databridge.mybridge.domain.model.login.LoginRegisterResponse
import com.databridge.mybridge.domain.rest.TokenRetrofitService
import com.databridge.mybridge.utils.Debug
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class TokenInterceptor(val sharedPref: SharedPref) :
    Interceptor {
    private var isRefreshingToken: Boolean = false // Flag to track token refresh status


    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val modifiedRequest = originalRequest.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .apply {
                val token = sharedPref.getPrefString("prefAccess")
                if (!token.isNullOrEmpty() && sharedPref.getPrefBoolean("prefIsLogin")) {
                    val authToken = "Bearer $token"
                    Debug.e("----","--authToken---$authToken")
                    addHeader("Authorization", authToken)
                }
            }
            .build()


        var response = chain.proceed(modifiedRequest)

        Debug.e("----","--response--code-${response.code}")
        // Check if the response indicates that the access token is expired
        try {
            if (response.code == 401) {
                // Call the refresh token API to obtain a new access token
                if (!isRefreshingToken) {
                    isRefreshingToken = true
                    runBlocking {
                        try {
                            val newAccessToken =
                                refreshToken() // Suspend the interceptor chain until token refresh completes
                            Debug.e("----","--newAccessToken-${newAccessToken}")
                            isRefreshingToken = false // Reset token refresh flag
                            // Create a new request with the updated access token
                            val newRequest = originalRequest.newBuilder()
                                .addHeader("Content-Type", "application/json")
                                .addHeader("Accept", "application/json")
                                .addHeader("Authorization", "Bearer $newAccessToken")
                                .build()
                            response.close()
                            response = chain.proceed(newRequest)
                            Debug.e("----","--response--code-2-${response.code}")
                            if (response.code == 401) {
                                sharedPref.logout()
                            }

                        } catch (e: Exception) {
                            isRefreshingToken = false // Reset token refresh flag
                            e.printStackTrace()
                        }
                    }

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return response
    }

    // Create a suspend function to handle the token refresh process
    suspend fun refreshToken(): String {
        return suspendCoroutine { continuation ->
            callRefreshTokenAPI(object : TokenRefreshCallback {
                override fun onTokenRefreshed(accessToken: String) {
                    continuation.resume(accessToken) // Resume the coroutine with the new access token
                }

                override fun onTokenRefreshFailed(error: Throwable) {
                    continuation.resumeWithException(error) // Resume the coroutine with the exception
                }
            })
        }
    }


    interface TokenRefreshCallback {
        fun onTokenRefreshed(accessToken: String)
        fun onTokenRefreshFailed(error: Throwable)
    }


    private fun callRefreshTokenAPI(callback: TokenRefreshCallback) {
        val map = HashMap<String, String>()
        map["refresh"] = sharedPref.getPrefString("prefRefresh").toString()

        Debug.e("-----", "--callRefreshTokenAPI--")
        val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(Conts.MAIN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val call = retrofit.create(TokenRetrofitService::class.java).refreshApiToken(map)

        call.enqueue(object : Callback<LoginRegisterResponse> {
            override fun onResponse(
                call: retrofit2.Call<LoginRegisterResponse>,
                response: retrofit2.Response<LoginRegisterResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()
                    Debug.e("-----", "--callRefreshTokenAPI-refresh-" + data?.refresh)
                    Debug.e("-----", "--callRefreshTokenAPI-access-" + data?.access)
                    data?.access?.let {
                        sharedPref.setPrefString("prefAccess", it)
                        callback.onTokenRefreshed(it) // Invoke callback with new access token
                    }
                    data?.refresh?.let {
                        sharedPref.setPrefString("prefRefresh", it)
                    }
                } else {
//                    sharedPref.logout()
                    callback.onTokenRefreshFailed(Throwable("Token refresh failed"))
                }
            }

            override fun onFailure(call: retrofit2.Call<LoginRegisterResponse>, t: Throwable) {
                t.printStackTrace()
                callback.onTokenRefreshFailed(t) // Invoke callback with error
            }
        })
    }


}