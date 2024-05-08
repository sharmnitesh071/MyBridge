package com.databridge.mybridge.validator
import java.util.*


class DatePeriodValidator(var starDate: Date?,var endDate: Date?) {
    var msg: String? = null
    fun isValid() : Boolean{
        if(starDate!!.compareTo(endDate) == 0){
            return true
        }else if(starDate!!.compareTo(endDate) < 0){
            return true
        }else{
            msg = "Event start date should falls between selected period."
            return false
        }

    }

}