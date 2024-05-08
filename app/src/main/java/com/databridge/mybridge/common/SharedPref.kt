package com.databridge.mybridge.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.databridge.mybridge.ui.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPref @Inject constructor(@ApplicationContext val context: Context) {

    private var mSettingPrefs: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)
    private lateinit var mSettingPrefEditor: SharedPreferences.Editor

    // string
    fun setPrefString(mKey: String, mItem: String) {
        mSettingPrefEditor = mSettingPrefs.edit()
        mSettingPrefEditor.putString(mKey, mItem)
        mSettingPrefEditor.commit()
    }

    fun getPrefString(mKey: String): String? {
        return mSettingPrefs.getString(mKey, "")
    }

    // Integer
    fun setPrefInt(mKey: String, mItem: Int) {
        mSettingPrefEditor = mSettingPrefs.edit()
        mSettingPrefEditor.putInt(mKey, mItem)
        mSettingPrefEditor.commit()
    }

    fun getPrefInt(mKey: String): Int {
        return mSettingPrefs.getInt(mKey, 0)
    }

    // Boolean
    fun setPrefBoolean(mKey: String, mItem: Boolean) {
        mSettingPrefEditor = mSettingPrefs.edit()
        mSettingPrefEditor.putBoolean(mKey, mItem)
        mSettingPrefEditor.commit()
    }

    fun getPrefBoolean(mKeys: String): Boolean {
        return mSettingPrefs.getBoolean(mKeys, false)
    }


    // clear all
    fun clearAllPref() {
        mSettingPrefEditor = mSettingPrefs.edit()
        mSettingPrefEditor.clear()
        mSettingPrefEditor.commit()


    }

    fun logout() {
        clearAllPref()
        setPrefBoolean("prefIsIntro", true)
        try {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // remove
    fun removePref(mKey: String) {
        mSettingPrefEditor = mSettingPrefs.edit()
        mSettingPrefEditor.remove(mKey)
        mSettingPrefEditor.commit()
    }

}