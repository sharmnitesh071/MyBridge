//package com.databridge.mybridge.ui.login.previewsigninSignup.viewmodel
//
//
//import android.app.Activity
//import android.app.Application
//import android.content.Intent
//import android.view.View
//import com.databridge.mybridge.R
//import com.databridge.mybridge.databinding.ActivityPreviewSignupBinding
//import com.databridge.mybridge.ui.base.viewmodel.BaseViewModel
//import com.databridge.mybridge.utils.Debug
//import com.databridge.mybridge.ui.login.login.datamodel.LoginDataModel
//import com.databridge.mybridge.ui.login.login.view.LoginActivity
//import com.databridge.mybridge.ui.login.signup.view.SignupActivity
//
//
//class PreviewSigninSignup1ViewModel(application: Application) : BaseViewModel(application) {
//
//    internal lateinit var binder: ActivityPreviewSignupBinding
//    private lateinit var loginDataModel: LoginDataModel
//    var isSignup = true
//
//    fun setBinder(binder: ActivityPreviewSignupBinding,isSignup:Boolean) {
//        this.binder = binder
//        this.mContext = binder.root.context
//        this.binder.viewClickHandler = ViewClickHandler()
//        this.loginDataModel = LoginDataModel()
//        this.isSignup = isSignup
//
//    }
//
//
//
//
//}