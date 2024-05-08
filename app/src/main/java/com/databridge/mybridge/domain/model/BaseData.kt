package com.databridge.mybridge.domain.model

import com.google.gson.annotations.SerializedName

class BaseData {
    @SerializedName("reset_token" ) var resetToken : String? = null
}