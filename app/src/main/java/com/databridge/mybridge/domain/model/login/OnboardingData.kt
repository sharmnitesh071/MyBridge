package com.databridge.mybridge.domain.model.login

import com.google.gson.annotations.SerializedName

class OnboardingData(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("user") var user: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("employment_type") var employmentType: String? = null,
    @SerializedName("company_name") var companyName: String? = null,
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
    @SerializedName("audience") var audience: Int? = null,

    @SerializedName("state") var state: String? = null,
    @SerializedName("postal_code") var postalCode: String? = null,
    @SerializedName("profile") var profile: Boolean? = null,
    @SerializedName("employment") var employment: Boolean? = null,
    @SerializedName("photo") var photo: Boolean? = null,
    @SerializedName("job_availabilty") var jobAvailabilty: Boolean? = null,
    @SerializedName("contact") var contact: Boolean? = null,
    @SerializedName("connect") var connect: Boolean? = null,
    @SerializedName("percentage") var percentage: Int? = null,
    @SerializedName("account_active") var accountActive: Boolean? = null,

    @SerializedName("name") var name: String? = null,
    @SerializedName("is_graduate") var isGraduate: Boolean? = null,
    @SerializedName("degree") var degree: String? = null,
    @SerializedName("courses") var courses: String? = null,
    @SerializedName("type") var type: String? = null

)