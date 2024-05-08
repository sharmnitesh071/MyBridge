package com.databridge.mybridge.exceptions

open class BaseException() : Exception() {

    open lateinit var errMessage : String
    open lateinit var title : String


}