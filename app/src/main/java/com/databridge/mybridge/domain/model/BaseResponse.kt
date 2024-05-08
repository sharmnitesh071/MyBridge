package com.databridge.mybridge.domain.model

import com.google.gson.annotations.SerializedName

open class BaseResponse {
    @SerializedName("room_id")
    val roomId: String? = null

    @SerializedName("message")
    val message: String? = null

    @SerializedName("data")
    var data: Any? = null

    @SerializedName("detail")
    var detail: Any? = null

    @SerializedName("code")
    var code: String? = null

    @SerializedName("status_code")
    var statusCode: Int? = null
}


