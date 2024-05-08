package com.databridge.mybridge.ui.login.login.datamodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class User {

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("gender")
    @Expose
    var gender: String? = null

    @SerializedName("age")
    @Expose
    var age: String? = null

    @SerializedName("where_do_you_live")
    @Expose
    var where_do_you_live: String? = null

    @SerializedName("motto")
    @Expose
    var motto: String? = null

    @SerializedName("profile_name")
    @Expose
    var profile_name: String = ""

    @SerializedName("type")
    @Expose
    var type: Int? = null

    @SerializedName("notification")
    @Expose
    var notification: Int? = null

    @SerializedName("profile_pic")
    @Expose
    var profile_pic: String? = null



}