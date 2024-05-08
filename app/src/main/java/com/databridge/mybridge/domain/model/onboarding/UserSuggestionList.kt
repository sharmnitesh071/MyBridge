package com.databridge.mybridge.domain.model.onboarding

import com.google.gson.annotations.SerializedName

class UserSuggestionList(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("first_name") var firstName: String? = null,
    @SerializedName("last_name") var lastName: String? = null,
    @SerializedName("profile_pic") var profilePic: String? = null,
    @SerializedName("slug") var slug: String? = null,
    @SerializedName("location") var location: String? = null,
    @SerializedName("user_uuid") var userUuid: String? = null,
    @SerializedName("position") var position: String? = null,
    @SerializedName("company") var company: String? = null,
    @SerializedName("experiences") var experiences: ArrayList<Experiences> = arrayListOf(),
    @SerializedName("educations") var educations: ArrayList<Educations> = arrayListOf(),
    @SerializedName("matrix_user_id") var matrixUserId: String? = null,
    @SerializedName("matrix_presence") var matrixPresence: String? = null,
    @SerializedName("matrix_status") var matrixStatus: String? = null,
    @SerializedName("mutual_connection_count") var mutualConnectionCount: Int? = null,
    @SerializedName("followers_count") var followersCount: Int? = null,
    @SerializedName("first_degree_count") var firstDegreeCount: Int? = null,
    @SerializedName("former_name") var formerName: String? = null,
    @SerializedName("display_name") var displayName: String? = null,
    @SerializedName("public_profile_link") var publicProfileLink: String? = null,
    @SerializedName("default_profile_picture") var defaultProfilePicture: String? = null,
    @SerializedName("profile_pic_chars") var profilePicChars: String? = null,
    @SerializedName("is_following") var isFollowing: Boolean = false,
    @SerializedName("is_handshake") var isHandshake: Boolean = false

)