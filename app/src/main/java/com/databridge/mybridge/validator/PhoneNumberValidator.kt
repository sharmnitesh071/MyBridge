package com.databridge.mybridge.validator

import java.util.regex.Pattern

class PhoneNumberValidator(override var value : String?) : Validatable {

    override var msg: String? = null
    var isOptional = false;

    constructor(value: String?,isOptional : Boolean) : this(value) {
        this.value = value
        this.isOptional = isOptional

    }

    override fun isValid(): Boolean {
        val pattern = Pattern.compile(
                "^[0-9]{3}\\-[0-9]{3}\\-[0-9]{4}$"
        )
        if (value == null) {
            msg = "Phone Number is required."
            return isOptional
        } else if (value!!.isEmpty()) {
            msg = "Phone Number is required."
            return isOptional
        } else if (!pattern.matcher(value).matches()) {
            msg = "Invalid format. Valid format is : 999-999-9999"
            return false
        }
        return true
    }

}