package com.databridge.mybridge.domain.model.post

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class EducationsItem(

	@field:SerializedName("courses")
	val courses: String? = null,

	@field:SerializedName("start_year")
	val startYear: String? = null,

	@field:SerializedName("degree")
	val degree: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("is_graduate")
	val isGraduate: Boolean? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("start_month")
	val startMonth: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("end_month")
	val endMonth: String? = null,

	@field:SerializedName("end_year")
	val endYear: String? = null,

	@field:SerializedName("user")
	val user: Int? = null,

	@field:SerializedName("desc")
	val desc: String? = null
) : Parcelable