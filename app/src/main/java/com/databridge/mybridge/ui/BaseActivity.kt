package com.databridge.mybridge.ui

import androidx.appcompat.app.AppCompatActivity
import com.databridge.mybridge.R
import com.databridge.mybridge.common.CommonUtil
import com.databridge.mybridge.common.SharedPref
import com.databridge.mybridge.domain.model.BaseResponse
import com.databridge.mybridge.domain.repository.RetrofitRepository
import com.databridge.mybridge.domain.room.AppDataBase
import com.databridge.mybridge.utils.Debug
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {
    @Inject
    lateinit var appDataBase: AppDataBase

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var retrofitRepository: RetrofitRepository

    val activity: BaseActivity
        get() = this

    public fun getErrorMessage(message: String?, errorMsg: String?): String {
        var msg = ""
        try {
//            try {
//                val gson = Gson()
//                val data: BaseResponse =
//                    gson.fromJson(response, BaseResponse::class.java)
//
//                Debug.e("----", "--getErrorMessage-response-$response")
//                Debug.e("----", "--getErrorMessage-data-" +gson.toJson(data))
//                Debug.e("----", "--getErrorMessage-message-" +data?.message)
//                data?.message?.let { msg = it }
//                if (msg.isEmpty()) {
//                    data?.detail?.let { msg = it.toString() }
//                }
//                if (msg.isEmpty()) {
//                    data?.data?.let { msg = it.toString() }
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
            errorMsg?.let { msg = it }

            if (msg.isEmpty()) {
                message?.let { msg = it }
            }

//            if (msg.isEmpty())
//                msg = message.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (msg.isEmpty())
            msg = getString(R.string.api_error)
        return msg
    }

    public fun getErrorMessage(message: String?, data: BaseResponse?): String {
        var msg = ""
        try {
            data?.message?.let { msg = it }
            if (msg.isEmpty()) {
                data?.detail?.let { msg = it.toString() }
            }
            if (msg.isEmpty()) {
                data?.data?.let { msg = it.toString() }
            }
            if (msg.isEmpty())
                msg = message.toString()
            if (msg.isEmpty())
                msg = getString(R.string.api_error)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return msg
    }
}