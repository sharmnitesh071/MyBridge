package com.databridge.mybridge.domain.model.login

import com.databridge.mybridge.domain.model.BaseResponse
import com.google.gson.annotations.SerializedName

class LoginRegisterResponse : BaseResponse() {

    @SerializedName("token")
    val token: String? = null

    @SerializedName("error")
    val error: String? = null

    @SerializedName("refresh")
    var refresh: String? = null

    @SerializedName("access")
    var access: String? = null

    @SerializedName("matrix_access_token")
    var matrixAccessToken: String? = null

    @SerializedName("matrix_user_id")
    var matrixUserId: String? = null

    @SerializedName("matrix_refresh_token")
    var matrixRefreshToken: String? = null

    @SerializedName("matrix_device_id")
    var matrixDeviceId: String? = null
}