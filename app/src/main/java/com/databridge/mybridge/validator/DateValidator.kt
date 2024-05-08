package com.databridge.mybridge.validator


class DateValidator(override var value: String?,var prefix : String?) : Validatable {
    override var msg: String? = null

    override fun isValid(): Boolean {
        if(value == null){
            msg = "$prefix is required."
            return false
        }
        if(value!!.isEmpty()){
            msg = "$prefix is required."
            return false
        }
        return true
    }
}