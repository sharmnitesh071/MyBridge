package com.databridge.mybridge.domain.model

import com.google.gson.annotations.SerializedName

data class VerifyCodeResponse(
    @SerializedName("message")
    val message: String?,
    @SerializedName("data") var data       : BaseData? = BaseData(),
    @SerializedName("status_code" ) var statusCode : Int?  = null
)


