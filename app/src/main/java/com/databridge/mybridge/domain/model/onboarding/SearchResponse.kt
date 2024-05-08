package com.databridge.mybridge.domain.model.onboarding

import com.google.gson.annotations.SerializedName

data class SearchResponse<T>(
    @SerializedName("count"    ) var count    : Int?               = null,
    @SerializedName("next"     ) var next     : String?            = null,
    @SerializedName("previous" ) var previous : String?            = null,
    @SerializedName("results"  ) var results  : ArrayList<T> = ArrayList()
)


