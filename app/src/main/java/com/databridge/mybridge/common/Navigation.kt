package com.databridge.mybridge.common

import android.app.Activity
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.databridge.mybridge.R

fun NavController.navigate(@IdRes actionId: Int, arg: Parcelable? = null) {
    val bundle = arg?.let { Bundle().apply { putParcelable("KEY_ARG", it) } }
    navigate(actionId, bundle)
}

val Activity.mainNav: NavController
    get() = findNavController(R.id.navigationFragment)


val Fragment.mainNav: NavController
    get() = findNavController(R.id.navigationFragment)


private fun Fragment.findNavController(@IdRes viewId: Int): NavController {
    return Navigation.findNavController(requireActivity(), viewId)
}