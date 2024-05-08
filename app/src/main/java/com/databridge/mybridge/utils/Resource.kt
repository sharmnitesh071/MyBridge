package com.databridge.mybridge.utils

import okhttp3.ResponseBody

data class Resource<out T>(val status: Status, val data: T?, val message: String?,var errorBody: String?) {


    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null,null)
        }

//        fun <T> error(message: String, data: T? = null): Resource<T> {
//            return Resource(Status.ERROR, data, message)
//        }

        fun <T> error(errorBody : String?,message: String, data: T? = null): Resource<T> {
            return Resource(Status.ERROR, data, message,errorBody)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data, null,null)
        }
    }
}