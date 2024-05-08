package com.databridge.mybridge.domain.model.onboarding

import com.google.gson.annotations.SerializedName

data class IpResponse(
    @SerializedName("ip")
    val ip: String?
)


