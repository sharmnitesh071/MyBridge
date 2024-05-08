package com.databridge.mybridge.validator


class DateTimeValidator(var date: String?,var prefix : String?) {
    var msg: String? = null

    fun isValid(): Boolean {
        if(date == null){
            msg = "$prefix is required."
            return false
        }
        return true
    }
}