package com.databridge.mybridge.domain.model.login

import com.google.gson.annotations.SerializedName

class OnboardingDataResponse {
    @SerializedName("previous_step")
    var previousStep: String? = null

    @SerializedName("next_step")
    var nextStep: String? = null

    @SerializedName("message")
    val message: String? = null

    @SerializedName("data")
    var data: OnboardingData? = null

    @SerializedName("status_code")
    var statusCode: Int? = null

    @SerializedName("is_student")
    var isStudent: Boolean? = null
}


