package com.databridge.mybridge.domain.model.post

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RepostResponse(

    @field:SerializedName("post_id")
    val postId: Int? = null,

    @field:SerializedName("message")
    val message: String? = null

) : Parcelable
