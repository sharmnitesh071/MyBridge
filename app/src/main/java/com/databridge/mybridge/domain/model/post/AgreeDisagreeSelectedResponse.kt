package com.databridge.mybridge.domain.model.post

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AgreeDisagreeSelectedResponse(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("owner")
    val owner: String? = null,

    @field:SerializedName("post")
    val post: Int? = null,

    @field:SerializedName("type")
    val type: Int? = null
) : Parcelable