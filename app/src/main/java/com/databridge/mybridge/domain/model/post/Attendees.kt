package com.databridge.mybridge.domain.model.post

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Attendees(

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("user")
    val user: UserProfileItem? = null,

    @field:SerializedName("event_role")
    val eventRole: String? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("joined")
    val joined: Boolean? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("event")
    val event: Int? = null,

    ) : Parcelable
