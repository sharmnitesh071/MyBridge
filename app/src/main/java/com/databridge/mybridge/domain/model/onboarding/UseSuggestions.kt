package com.databridge.mybridge.domain.model.onboarding

import com.google.gson.annotations.SerializedName

class UseSuggestions(

    @SerializedName("university") var university: ArrayList<UserSuggestionList> = ArrayList(),
    @SerializedName("role") var role: ArrayList<UserSuggestionList> = ArrayList(),
    @SerializedName("industry") var industry: ArrayList<UserSuggestionList> = ArrayList(),
    @SerializedName("degree") var degree: ArrayList<UserSuggestionList> = ArrayList(),
    @SerializedName("location") var location: ArrayList<UserSuggestionList> = ArrayList(),
    @SerializedName("follow") var follow: ArrayList<UserSuggestionList> = ArrayList()
)