package com.databridge.mybridge.validator


class EventNameValidator(override var value: String?) : Validatable {

    override var msg: String? = null

    override fun isValid(): Boolean {
        if (value == null) {
            msg = "Event Name is required."
            return false
        } else if (value!!.isEmpty()) {
            msg = "Event Name is required."
            return false
        } else if(value!!.length < 3){
            msg = "Min. 3 character required."
            return false
        } else if(value!!.length > 40){
            msg = "Max. 40 character required."
            return false
        }
        return true
    }

}