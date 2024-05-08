package com.databridge.mybridge.domain.model.onboarding

import com.google.gson.annotations.SerializedName

class CompanyResults (
    @SerializedName("id"         ) var id        : Int?     = null,
    @SerializedName("name"       ) var name      : String?  = null,
    @SerializedName("logo"       ) var logo      : String?  = null,
    @SerializedName("address"    ) var address   : String?  = null,
    @SerializedName("is_active"  ) var isActive  : Boolean? = null,
    @SerializedName("created_at" ) var createdAt : String?  = null,
    @SerializedName("updated_at" ) var updatedAt : String?  = null

)