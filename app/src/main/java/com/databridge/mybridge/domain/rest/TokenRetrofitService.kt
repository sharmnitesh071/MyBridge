package com.databridge.mybridge.domain.rest


import com.databridge.mybridge.domain.model.login.LoginRegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface TokenRetrofitService {
    @POST("api/token/refresh/")
    fun refreshApiToken(@Body reqBody: Map<String, String>): Call<LoginRegisterResponse>

}