package com.databridge.mybridge.domain.rest


import com.databridge.mybridge.domain.model.BaseResponse
import com.databridge.mybridge.domain.model.VerifyCodeResponse
import com.databridge.mybridge.domain.model.login.LoginRegisterResponse
import com.databridge.mybridge.domain.model.login.OnboardingDataResponse
import com.databridge.mybridge.domain.model.login.OnboardingResponse
import com.databridge.mybridge.domain.model.login.UserInfo
import com.databridge.mybridge.domain.model.onboarding.UseSuggestions
import com.databridge.mybridge.domain.model.post.PostItem
import com.databridge.mybridge.domain.model.post.PostResponse
import com.databridge.mybridge.domain.model.post.RepostResponse
import com.databridge.mybridge.domain.model.post.UserConnectionResponse
import com.databridge.mybridge.domain.model.post.request.AgreeDisagreeRequest
import com.databridge.mybridge.domain.model.post.request.FollowUnfollowRequest
import com.databridge.mybridge.domain.model.post.request.ReportBlockPostRequest
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.QueryMap
import retrofit2.http.Url

interface RetrofitService {

    @POST("api/token/")
    suspend fun loginApi(@Body reqBody: Map<String, String>): Response<LoginRegisterResponse>

//    @POST("api/token/refresh/")
//    fun refreshApiToken(@Body reqBody: Map<String, String>): Response<LoginRegisterResponse>

    @POST("api/v1/google/register")
    suspend fun socialLoginAPI(@Body reqBody: Map<String, String>): Response<LoginRegisterResponse>

    @POST("api/v1/register")
    suspend fun signupApi(@Body reqBody: Map<String, String>): Response<LoginRegisterResponse>

    @PUT("api/v1/register/verification/send")
    suspend fun sendOtpToVerifyEmail(@Body reqBody: Map<String, String>): Response<BaseResponse>

    @GET("api/v1/activate/{otp}")
    suspend fun verifyActivateAccount(@Path("otp") otp: String): Response<BaseResponse>

    @POST("api/v1/user/change-email")
    suspend fun editEmail(@Body reqBody: Map<String, String>): Response<BaseResponse>

    @POST("api/v1/user/password-reset/otp/send/")
    suspend fun sendOtpToVerify(@Body reqBody: Map<String, String>): Response<BaseResponse>

    @POST("api/v1/user/password-reset/otp/verify/")
    suspend fun verifyPasswordReset(@Body reqBody: Map<String, String>): Response<VerifyCodeResponse>

    @POST("api/v1/user/password-reset/otp/confirm/")
    suspend fun confirmPassword(@Body reqBody: Map<String, String>): Response<BaseResponse>

    @POST("api/v1/user/onboarding/")
    suspend fun onboardingAPI(
        @QueryMap queryParam: Map<String, String>,
        @Body reqBody: Map<String, @JvmSuppressWildcards Any>
    ): Response<OnboardingResponse>

    @POST("api/v1/user/onboarding/")
    suspend fun onboarding1API(
        @QueryMap queryParam: Map<String, String>,
        @Body reqBody: Map<String, String>
    ): Response<OnboardingResponse>

    @POST("api/v1/user/onboarding/")
    suspend fun onboardingProfileImageAPI(@QueryMap queryParam: Map<String, String>, @Body params: MultipartBody): Response<OnboardingResponse>

    @GET("api/v1/user/onboarding/")
    suspend fun getonboardingData(@QueryMap queryParam: Map<String, String>): Response<OnboardingDataResponse>

    @GET("api/v1/user/profile/info/")
    suspend fun getProfileInfo(@QueryMap queryParam: Map<String, String>): Response<UserInfo>

    @GET("api/v1/user/connection/manage-invite/suggestion")
    suspend fun getUserSuggestions(@QueryMap queryParam: Map<String, String>): Response<UseSuggestions>

    @POST("api/v1/user/follower")
    suspend fun sendFollowRequest(@Body reqBody: Map<String, String>): Response<BaseResponse>

    @DELETE("api/v1/user/follower")
    suspend fun sendUnFollowRequest(@Body reqBody: Map<String, String>): Response<BaseResponse>

    @POST("api/v1/user/connection")
    suspend fun sendHandshakeRequest(
        @Body reqBody: Map<String, @JvmSuppressWildcards Any>
    ): Response<BaseResponse>


    @GET("api/v1/search/public/post/")
    suspend fun getSearchPublicPost(@QueryMap reqParam: Map<String, Int>): Response<PostResponse>

    @GET("api/v1/posts")
    suspend fun getPosts(@QueryMap reqParam: Map<String, Int>): Response<PostResponse>

    @GET("api/v1/post/{postId}/detail")
    suspend fun getPostDetail(@Path("postId") postId: Int): Response<PostItem>

    @PUT("api/v1/post/{postId}/like")
    suspend fun postAgree(
        @Path("postId") postId: Int,
        @Body requestBody: AgreeDisagreeRequest
    ): Response<Any>

    @HTTP(method = "DELETE", path = "api/v1/post/{postId}/dislike", hasBody = true)
    suspend fun postDisagree(
        @Path("postId") postId: Int,
        @Body requestBody: AgreeDisagreeRequest
    ): Response<Any>

    @POST("api/v1/event/{postId}/accept")
    suspend fun setRsvpEvent(@Path("postId") postId: Int): Response<PostItem>

    @POST("api/v1/post/{postId}/ignore")
    suspend fun setNotInterestedPost(@Path("postId") postId: Int): Response<List<String>>

    @POST("api/v1/user/follower")
    suspend fun followUser(@Body requestBody: FollowUnfollowRequest): Response<Any>

    @HTTP(method = "DELETE", path = "api/v1/user/follower", hasBody = true)
    suspend fun unFollowUser(@Body requestBody: FollowUnfollowRequest): Response<Any>

    @GET("api/v1/user/profile/connection/")
    suspend fun userProfileConnection(): Response<UserConnectionResponse>

    @POST("api/v1/report/post")
    suspend fun reportBlockPost(@Body reqParam: ReportBlockPostRequest): Response<Any>

    @DELETE("api/v1/post/{postId}")
    suspend fun deletePost(@Path("postId") postId: Int): Response<Any>

    @POST("api/v1/repost/{postId}")
    suspend fun rePost(@Path("postId") postId: Int): Response<RepostResponse>



}