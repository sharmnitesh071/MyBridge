package com.databridge.mybridge.exceptions.networks

import com.databridge.mybridge.utils.Debug

class NoInternetException : NetworkException() {

    override var errMessage: String = "No Internet Available"

    override var title: String = "Alert"

    override fun printStackTrace() {
        super.printStackTrace()
        Debug.e("NoInternet","No Internet Connection")
    }

    override fun getLocalizedMessage(): String {
        return super.getLocalizedMessage()
    }
}