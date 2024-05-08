package com.databridge.mybridge.domain.rest


import com.databridge.mybridge.domain.model.onboarding.CollageResults
import com.databridge.mybridge.domain.model.onboarding.CompanyResults
import com.databridge.mybridge.domain.model.onboarding.SearchCityResponse
import com.databridge.mybridge.domain.model.onboarding.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface GetApiRetrofitService {

    @GET("api/v1/location/google/search")
    suspend fun searchCityCountryAPI(@QueryMap queryParam: Map<String, String>): Response<ArrayList<SearchCityResponse>>

    @GET("api/v1/gd/companies/")
    suspend fun searchCompanyAPI(@QueryMap queryParam: Map<String, String>): Response<SearchResponse<CompanyResults>>

    @GET("api/v1/gd/schools/")
    suspend fun searchSchoolAPI(@QueryMap queryParam: Map<String, String>): Response<SearchResponse<CollageResults>>


}