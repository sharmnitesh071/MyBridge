package com.databridge.mybridge.ui.login.login.datamodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class UserData {

    constructor(statusCode: Int?) : super() {
        this.statusCode = statusCode
    }

    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = null


    @SerializedName("status")
    @Expose
    var status: Int = 0

    @SerializedName("data")
    @Expose
    var data: LData? = null


    inner class LData {

        @SerializedName("min_version")
        @Expose
        var min_version: String? = null
        @SerializedName("current_version")
        @Expose
        var current_version: String? = null
        @SerializedName("message")
        @Expose
        var message: String? = null
        @SerializedName("token")
        @Expose
        var token: String? = null

        @SerializedName("user")
        @Expose
        var user: User? = null

        @SerializedName("errors")
        @Expose
        var errors: Error? = null


    }

    inner class Error {
        @SerializedName("error")
        @Expose
        lateinit var error: Array<String>;

        @SerializedName("email")
        @Expose
        lateinit var email: Array<String>;
    }


//     class User {
//
//        @SerializedName("name")
//        @Expose
//        var name: String? = null
//
//        @SerializedName("id")
//        @Expose
//        var id: String? = null
//
//        @SerializedName("email")
//        @Expose
//        var email: String? = null
//
//        @SerializedName("address")
//        @Expose
//        var address: String? = null
//
//        @SerializedName("gender")
//        @Expose
//        var gender: String? = null
//
//        @SerializedName("age")
//        @Expose
//        var age: String? = null
//
//        @SerializedName("profile_pic")
//        @Expose
//        var profile_pic: String? = null
//
//
//    }

}