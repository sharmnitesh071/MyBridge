package com.databridge.mybridge.domain.model.post

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserConnectionResponse(

    @field:SerializedName("circle")
    val circle: List<Int>? = null,

    @field:SerializedName("followers")
    val followers: List<Int>? = null,

    @field:SerializedName("following")
    val following: List<Int>? = null,

    @field:SerializedName("blocked")
    val blocked: List<Int>? = null,

    @field:SerializedName("handshake_requests")
    val handshakeRequests: List<Int>? = null,

    @field:SerializedName("handshake_received")
    val handshakeReceived: List<Int>? = null,

    @field:SerializedName("company_following_ids")
    val companyFollowingIds: List<Int>? = null,

    @field:SerializedName("event_invited_ids")
    val eventInvitedIds: List<Int>? = null,

    @field:SerializedName("event_accepted_ids")
    val eventAcceptedIds: List<Int>? = null,

    @field:SerializedName("team_requested_ids")
    val teamRequestedIds: List<Int>? = null,

    @field:SerializedName("team_accepted_ids")
    val teamAcceptedIds: List<Int>? = null,

    @field:SerializedName("testimonaial_asked_user_ids")
    val testimonialAskedUserIds: List<Int>? = null,

    @field:SerializedName("testimonaial_giver_user_ids")
    val testimonialGiverUserIds: List<Int>? = null

) : Parcelable
