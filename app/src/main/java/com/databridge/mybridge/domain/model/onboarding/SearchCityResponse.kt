package com.databridge.mybridge.domain.model.onboarding

import com.google.gson.annotations.SerializedName

data class SearchCityResponse(
    @SerializedName("city") var city: String? = null,
    @SerializedName("postal_code") var postalCode: String? = null,
    @SerializedName("state") var state: String? = null,
    @SerializedName("country") var country: String? = null
)


