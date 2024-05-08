package com.databridge.mybridge.domain.repository

import com.databridge.mybridge.domain.model.BaseResponse
import com.databridge.mybridge.domain.model.VerifyCodeResponse
import com.databridge.mybridge.domain.model.login.LoginRegisterResponse
import com.databridge.mybridge.domain.model.login.OnboardingDataResponse
import com.databridge.mybridge.domain.model.login.OnboardingResponse
import com.databridge.mybridge.domain.model.login.UserInfo
import com.databridge.mybridge.domain.model.onboarding.CollageResults
import com.databridge.mybridge.domain.model.onboarding.CompanyResults
import com.databridge.mybridge.domain.model.onboarding.IpDetailResponse
import com.databridge.mybridge.domain.model.onboarding.IpResponse
import com.databridge.mybridge.domain.model.onboarding.SearchCityResponse
import com.databridge.mybridge.domain.model.onboarding.SearchResponse
import com.databridge.mybridge.domain.model.onboarding.UseSuggestions
import com.databridge.mybridge.domain.rest.GetApiRetrofitService
import com.databridge.mybridge.domain.rest.IpRetrofitService
import com.databridge.mybridge.domain.rest.RetrofitService
import com.databridge.mybridge.utils.Resource
import com.databridge.mybridge.utils.BaseApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val service: RetrofitService,
    private  val ipService: IpRetrofitService,
    private  val getApiService: GetApiRetrofitService
) : BaseApiResponse() {
//     login
    suspend fun login(reqBody: Map<String, String>): Flow<Resource<LoginRegisterResponse>> {
        return flow {
            emit(safeApiCall { service.loginApi(reqBody) })
        }.flowOn(Dispatchers.IO)
    }
//    suspend fun refreshApiToken(reqBody: Map<String, String>): Flow<Resource<LoginRegisterResponse>> {
//        return flow {
//            emit(safeApiCall { service.refreshApiToken(reqBody) })
//        }.flowOn(Dispatchers.IO)
//    }

    suspend fun socialLogin(reqBody: Map<String, String>): Flow<Resource<LoginRegisterResponse>> {
        return flow {
            emit(safeApiCall { service.socialLoginAPI(reqBody) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun signupApi(reqBody: Map<String, String>): Flow<Resource<LoginRegisterResponse>> {
        return flow {
            emit(safeApiCall { service.signupApi(reqBody) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun sendOtpToVerify(reqBody: Map<String, String>): Flow<Resource<BaseResponse>> {
        return flow {
            emit(safeApiCall { service.sendOtpToVerify(reqBody) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun verifyPasswordReset(reqBody: Map<String, String>): Flow<Resource<VerifyCodeResponse>> {
        return flow {
            emit(safeApiCall { service.verifyPasswordReset(reqBody) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun confirmPassword(reqBody: Map<String, String>): Flow<Resource<BaseResponse>> {
        return flow {
            emit(safeApiCall { service.confirmPassword(reqBody) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun editEmail(reqBody: Map<String, String>): Flow<Resource<BaseResponse>> {
        return flow {
            emit(safeApiCall { service.editEmail(reqBody) })
        }.flowOn(Dispatchers.IO)
    }
    suspend fun sendOtpToVerifyEmail(reqBody: Map<String, String>): Flow<Resource<BaseResponse>> {
        return flow {
            emit(safeApiCall { service.sendOtpToVerifyEmail(reqBody) })
        }.flowOn(Dispatchers.IO)
    }
   suspend fun verifyActivateAccount(otp:String): Flow<Resource<BaseResponse>> {
        return flow {
            emit(safeApiCall { service.verifyActivateAccount(otp) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getIpAddress(url: String): Flow<Resource<IpResponse>> {
        return flow {
            emit(safeApiCall { ipService.getIpAddress(url) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getIpDetail(url: String): Flow<Resource<IpDetailResponse>> {
        return flow {
            emit(safeApiCall { ipService.getIpDetail(url) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun onboardingAPI(
        queryParam: Map<String, String>,
        reqBody: Map<String, Any>
    ): Flow<Resource<OnboardingResponse>> {
        return flow {
            emit(safeApiCall {
                service.onboardingAPI(queryParam,reqBody)
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun onboarding1API(
        queryParam: Map<String, String>,
        reqBody: Map<String, String>
    ): Flow<Resource<OnboardingResponse>> {
        return flow {
            emit(safeApiCall {
                service.onboarding1API(queryParam,reqBody)
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun onboardingProfileImageAPI(
        queryParam: Map<String, String>,
        reqBody: MultipartBody
    ): Flow<Resource<OnboardingResponse>> {
        return flow {
            emit(safeApiCall {
                service.onboardingProfileImageAPI(queryParam,reqBody)
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getonboardingData(
        queryParam: Map<String, String>
    ): Flow<Resource<OnboardingDataResponse>> {
        return flow {
            emit(safeApiCall {
                service.getonboardingData(queryParam)
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getProfileInfo(
        queryParam: Map<String, String>
    ): Flow<Resource<UserInfo>> {
        return flow {
            emit(safeApiCall {
                service.getProfileInfo(queryParam)
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getUserSuggestions(
        queryParam: Map<String, String>
    ): Flow<Resource<UseSuggestions>> {
        return flow {
            emit(safeApiCall {
                service.getUserSuggestions(queryParam)
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun searchCityCountryAPI(queryParam: Map<String, String>): Flow<Resource<ArrayList<SearchCityResponse>>> {
        return flow {
            emit(safeApiCall { getApiService.searchCityCountryAPI(queryParam) })
        }.flowOn(Dispatchers.IO)
    }


    suspend fun searchCompanyAPI(queryParam: Map<String, String>): Flow<Resource<SearchResponse<CompanyResults>>> {
        return flow {
            emit(safeApiCall { getApiService.searchCompanyAPI(queryParam) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun searchSchoolAPI(queryParam: Map<String, String>): Flow<Resource<SearchResponse<CollageResults>>> {
        return flow {
            emit(safeApiCall { getApiService.searchSchoolAPI(queryParam) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun sendFollowRequest(reqBody: Map<String, String>): Flow<Resource<BaseResponse>> {
        return flow {
            emit(safeApiCall { service.sendFollowRequest(reqBody) })
        }.flowOn(Dispatchers.IO)
    }
    suspend fun sendUnFollowRequest(reqBody: Map<String, String>): Flow<Resource<BaseResponse>> {
        return flow {
            emit(safeApiCall { service.sendUnFollowRequest(reqBody) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun sendHandshakeRequest(reqBody: Map<String, Any>): Flow<Resource<BaseResponse>> {
        return flow {
            emit(safeApiCall { service.sendHandshakeRequest(reqBody) })
        }.flowOn(Dispatchers.IO)
    }

}