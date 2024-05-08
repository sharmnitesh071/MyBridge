package com.databridge.mybridge.domain.model.post

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Company(

    @field:SerializedName("why_work_here")
    val whyWorkHere: String? = null,

    @field:SerializedName("brochure_first_name")
    val brochureFirstName: String? = null,

    @field:SerializedName("country")
    val country: String? = null,

    @field:SerializedName("company_type")
    val companyType: String? = null,

    @field:SerializedName("city")
    val city: String? = null,

    @field:SerializedName("year_founded")
    val yearFounded: String? = null,

//	@field:SerializedName("banner_image_metadata")
//	val bannerImageMetadata: Any? = null, // todo - remove If not getting data type

    @field:SerializedName("main_industry")
    val mainIndustry: String? = null,

//	@field:SerializedName("company_timings_type")
//	val companyTimingsType: Any? = null, // todo - remove If not getting data type

//	@field:SerializedName("banner_image")
//	val bannerImage: Any? = null, // todo - remove If not getting data type

    @field:SerializedName("brochure_second_name")
    val brochureSecondName: String? = null,

    @field:SerializedName("introduction")
    val introduction: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("slug")
    val slug: String? = null,

    @field:SerializedName("file_visibility")
    val fileVisibility: Boolean? = null,

//	@field:SerializedName("image")
//	val image: Any? = null, // todo - remove If not getting data type

//	@field:SerializedName("month_founded")
//	val monthFounded: Any? = null, // todo - remove If not getting data type

    @field:SerializedName("website")
    val website: String? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("is_visible")
    val isVisible: Boolean? = null,

    @field:SerializedName("is_deactivated")
    val isDeactivated: Boolean? = null,

//	@field:SerializedName("job_listing")
//	val jobListing: Any? = null, // todo - remove If not getting data type

    @field:SerializedName("is_verified")
    val isVerified: Boolean? = null,

    @field:SerializedName("zipcode")
    val zipcode: String? = null,

    @field:SerializedName("state_region")
    val stateRegion: String? = null,

//	@field:SerializedName("company_timings")
//	val companyTimings: Any? = null, // todo - remove If not getting data type

    @field:SerializedName("name")
    val name: String? = null,

//	@field:SerializedName("suspended_till")
//	val suspendedTill: Any? = null, // todo - remove If not getting data type

    @field:SerializedName("phone_number")
    val phoneNumber: String? = null,

//	@field:SerializedName("user")
//	val user: Any? = null, // todo - remove If not getting data type

//	@field:SerializedName("company_size")
//	val companySize: Any? = null, // todo - remove If not getting data type

//	@field:SerializedName("status")
//	val status: Any? = null // todo - remove If not getting data type
) : Parcelable