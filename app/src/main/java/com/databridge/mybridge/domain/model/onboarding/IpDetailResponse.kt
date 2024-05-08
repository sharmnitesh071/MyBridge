package com.databridge.mybridge.domain.model.onboarding

import com.google.gson.annotations.SerializedName

data class IpDetailResponse(
    @SerializedName("city")
    val city: String?,
    @SerializedName("region")
    val region: String?,
    @SerializedName("region_code")
    val regionCode: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("country_name")
    val countryName: String?,
    @SerializedName("country_code")
    val countryCode: String?
)


