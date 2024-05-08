package com.databridge.mybridge.domain.model.onboarding

import com.google.gson.annotations.SerializedName

class Experiences(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("employment_type") var employmentType: String? = null,
    @SerializedName("company_name") var companyName: String? = null,
    @SerializedName("company_image") var companyImage: String? = null,
    @SerializedName("city") var city: String? = null,
    @SerializedName("country") var country: String? = null,
    @SerializedName("state_region") var stateRegion: String? = null,
    @SerializedName("zipcode") var zipcode: String? = null,
    @SerializedName("location") var location: String? = null,
    @SerializedName("industry") var industry: String? = null,
    @SerializedName("desc") var desc: String? = null,
    @SerializedName("profile_headline") var profileHeadline: String? = null,
    @SerializedName("start_month") var startMonth: String? = null,
    @SerializedName("start_year") var startYear: String? = null,
    @SerializedName("end_month") var endMonth: String? = null,
    @SerializedName("end_year") var endYear: String? = null,
    @SerializedName("currently_working") var currentlyWorking: Boolean? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("user") var user: Int? = null
)