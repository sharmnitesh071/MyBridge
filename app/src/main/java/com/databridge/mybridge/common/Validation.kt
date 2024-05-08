package com.databridge.mybridge.common

import android.content.Context
import android.os.Build
import com.databridge.mybridge.R
import com.databridge.mybridge.base.notMatches
import com.databridge.mybridge.utils.Debug
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

const val EMAIL_PATTERN =
    """[a-zA-Z0-9\+\.\_\%\-\+]{1,256}\@[a-zA-Z0-9][a-zA-Z0-9\-]{0,64}(\.[a-zA-Z0-9][a-zA-Z0-9\-]{0,25})+"""

const val PASSWORD = "^.{8,}\$"
const val OTP_PATTERN = "^.{6,}\$"

class Validation @Inject constructor(@ApplicationContext val context: Context) {

    fun fnameValidation(email: String): String {
        val error = when {
            email.isEmpty() -> context.getString(R.string.first_name_required)
            else -> ""
        }
        return error
    }

    fun lnameValidation(email: String): String {
        val error = when {
            email.isEmpty() -> context.getString(R.string.last_name_required)
            else -> ""
        }
        return error
    }

    fun emailValidation(email: String): String {
        val error = when {
            email.isEmpty() -> context.getString(R.string.email_address_required)
            email.notMatches(EMAIL_PATTERN) -> context.getString(R.string.invalid_email_address)
            else -> ""
        }
        return error
    }

    fun passwordValidation(password: String): String {
        val error = when {
            password.isEmpty() -> context.getString(R.string.password_required)
            password.notMatches(PASSWORD) -> context.getString(R.string.invalid_password)
            else -> ""
        }
        return error
    }

    fun otpValidation(otp: String): String {
        val error = when {
            otp.isEmpty() -> context.getString(R.string.enter_verification_code)
            otp.notMatches(OTP_PATTERN) -> context.getString(R.string.enter_valid_verification_code)
            else -> ""
        }
        return error
    }

    fun calculateAge(year: Int, month: Int, day: Int): Int {
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()

        dob.set(year, month, day)

        var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        Debug.e("---", "--calculateAge--$age")
        return age
    }

    fun isEmptyValidation(str: String, msg: String): String {
        val error = when {
            str.isEmpty() -> msg
            else -> ""
        }
        return error
    }

    fun isAgeValidation(str: String, msg: String): String {
        val error = when {
            str.isEmpty() -> msg
            else -> {
                val dob = convertDate(str).split("-")
                var isAgeValid = false
                try {
                    isAgeValid = calculateAge(dob[0].toInt(), dob[1].toInt(), dob[2].toInt()) > 13
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                Debug.e("----", "--isAgeValid--" + isAgeValid)
                if (isAgeValid) return "" else msg
            }
        }
        return error

    }

    private fun convertDate(input: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val outputFormat = SimpleDateFormat("yyyy-M-d", Locale.US)

        try {
            val inputDate: Date = inputFormat.parse(input)
            val outputDateStr: String = outputFormat.format(inputDate)
            println(outputDateStr) // Output will be:
            return outputDateStr
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return input
    }

}