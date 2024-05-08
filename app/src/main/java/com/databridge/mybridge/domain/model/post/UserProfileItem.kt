package com.databridge.mybridge.domain.model.post

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class UserProfileItem(

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("former_name")
	val formerName: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("default_profile_picture")
	val defaultProfilePicture: String? = null,

	@field:SerializedName("profile_pic")
	val profilePic: String? = null,

	@field:SerializedName("last_name")
	val lastName: String? = null,

	@field:SerializedName("company")
	val company: String? = null,

	@field:SerializedName("educations")
	val educations: List<EducationsItem?>? = null,

	@field:SerializedName("experiences")
	val experiences: List<ExperiencesItem?>? = null,

	@field:SerializedName("matrix_presence")
	val matrixPresence: String? = null,

	@field:SerializedName("user_uuid")
	val userUuid: String? = null,

	@field:SerializedName("matrix_user_id")
	val matrixUserId: String? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("position")
	val position: String? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("matrix_status")
	val matrixStatus: String? = null,

	@field:SerializedName("postal_code")
	val postalCode: String? = null,

	@field:SerializedName("first_name")
	val firstName: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("slug")
	val slug: String? = null
) : Parcelable