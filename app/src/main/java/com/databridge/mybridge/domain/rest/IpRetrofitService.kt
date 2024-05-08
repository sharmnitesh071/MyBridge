package com.databridge.mybridge.domain.rest


import com.databridge.mybridge.domain.model.onboarding.IpDetailResponse
import com.databridge.mybridge.domain.model.onboarding.IpResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface IpRetrofitService {

    @GET
    suspend fun getIpAddress(@Url url:String): Response<IpResponse>
    @GET
    suspend fun getIpDetail(@Url url:String): Response<IpDetailResponse>


}