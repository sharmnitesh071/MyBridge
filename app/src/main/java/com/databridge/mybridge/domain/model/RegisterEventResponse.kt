package com.databridge.mybridge.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterEventResponse(
    @field:SerializedName("message")
    val message: String? = null
) : Parcelable