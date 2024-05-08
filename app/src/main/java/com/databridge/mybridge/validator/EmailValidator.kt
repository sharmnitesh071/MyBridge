package com.databridge.mybridge.validator

import java.util.regex.Pattern

class EmailValidator(override var value : String?) : Validatable {

    override var msg: String? = null

    override fun isValid(): Boolean {

        val pattern = Pattern.compile(
                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+"
        )
        if (value == null) {
            msg = "Email is required."
            return false
        } else if (value!!.isEmpty()) {
            msg = "Email is required."
            return false
        } else if (!pattern.matcher(value).matches()) {
            msg = "Invalid format. Valid format is : a@aaa.com"
            return false
        }
        return true
    }
}