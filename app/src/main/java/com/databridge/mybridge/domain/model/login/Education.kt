package com.databridge.mybridge.domain.model.login

import com.google.gson.annotations.SerializedName

class Education (
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("start_year")
    var startYear: String? = null,
    @SerializedName("start_month")
    var startMonth: String? = null,
    @SerializedName("end_year")
    var endYear: String? = null,
    @SerializedName("end_month")
    var endMonth: String? = null,
    @SerializedName("is_graduate")
    var isGraduate: Boolean? = null,
    @SerializedName("desc")
    var desc: String? = null,
    @SerializedName("degree")
    var degree: String? = null,
    @SerializedName("courses")
    var courses: String? = null,
    @SerializedName("type")
    var type: String? = null,
    @SerializedName("created_at")
    var createdAt: String? = null,
    @SerializedName("updated_at")
    var updatedAt: String? = null,
    @SerializedName("user")
    var user: String? = null
)