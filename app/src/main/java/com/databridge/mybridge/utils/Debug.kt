package com.databridge.mybridge.utils

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import android.widget.Toast

object Debug {
    var DEBUG = true
    var TESTINGDEVICE = false
    var SHOWTOAST = false
    var finalTag = ""

    @JvmStatic
    fun e(tag: String, msg: String?) {
        if (DEBUG) {
            finalTag = if (TextUtils.isEmpty(tag)) "unknown" else tag
            Log.e(finalTag, msg!!)
        }
    }

    @JvmStatic
    fun d(tag: String, msg: String?) {
        if (DEBUG) {
            finalTag = if (TextUtils.isEmpty(tag)) "unknown" else tag
            Log.d(finalTag, msg!!)
        }
    }

    fun w(tag: String, msg: String?) {
        if (DEBUG) {
            finalTag = if (TextUtils.isEmpty(tag)) "unknown" else tag
            Log.w(finalTag, msg!!)
        }
    }

    fun v(tag: String, msg: String?) {
        if (DEBUG) {
            finalTag = if (TextUtils.isEmpty(tag)) "unknown" else tag
            Log.v(finalTag, msg!!)
        }
    }

    fun i(tag: String, msg: String?) {
        if (DEBUG) {
            finalTag = if (TextUtils.isEmpty(tag)) "unknown" else tag
            Log.i(finalTag, msg!!)
        }
    }

    fun showToast(activity: Activity?, msg: String?) {
        if (SHOWTOAST) {
            Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
        }
    }
}