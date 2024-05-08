package com.databridge.mybridge.exceptions

import com.databridge.mybridge.utils.Debug


class PermissionException : BaseException() {

    override fun printStackTrace() {
        super.printStackTrace()
        Debug.e("Permission","Permission denied" )
    }
}