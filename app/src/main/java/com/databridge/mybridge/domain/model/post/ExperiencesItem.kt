package com.databridge.mybridge.domain.model.post

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ExperiencesItem(

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("employment_type")
	val employmentType: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("start_year")
	val startYear: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("industry")
	val industry: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("profile_headline")
	val profileHeadline: String? = null,

	@field:SerializedName("zipcode")
	val zipcode: String? = null,

	@field:SerializedName("state_region")
	val stateRegion: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("company_image")
	val companyImage: String? = null,

	@field:SerializedName("company_name")
	val companyName: String? = null,

	@field:SerializedName("start_month")
	val startMonth: String? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("end_month")
	val endMonth: String? = null,

	@field:SerializedName("end_year")
	val endYear: String? = null,

	@field:SerializedName("user")
	val user: Int? = null,

	@field:SerializedName("desc")
	val desc: String? = null,

	@field:SerializedName("currently_working")
	val currentlyWorking: Boolean? = null
) : Parcelable