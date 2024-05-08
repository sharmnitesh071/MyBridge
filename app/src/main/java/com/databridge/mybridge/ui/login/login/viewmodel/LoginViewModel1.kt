//package com.databridge.mybridge.ui.login.login.viewmodel
//
//
//import android.app.Activity
//import android.app.Application
//import android.content.Intent
//import android.view.View
//import com.databridge.mybridge.databinding.ActivityLoginBinding
//import com.databridge.mybridge.ui.base.viewmodel.BaseViewModel
//import com.databridge.mybridge.ui.login.forgotpass.view.ForgotPasswordActivity
//import com.databridge.mybridge.ui.login.login.datamodel.User
//import com.databridge.mybridge.utils.Constant
//import com.databridge.mybridge.utils.Debug
//import com.databridge.mybridge.utils.Utils
//import com.databridge.mybridge.validator.EmailValidator
//import com.databridge.mybridge.validator.PasswordValidator
//import com.google.gson.Gson
//import com.databridge.mybridge.ui.login.login.datamodel.UserData
//import com.databridge.mybridge.ui.login.login.datamodel.LoginDataModel
//import com.databridge.mybridge.ui.login.signup.view.SignupActivity
//
//
//
//
//class LoginViewModel1(application: Application) : BaseViewModel(application) {
//
//    internal lateinit var binder: ActivityLoginBinding
//    private lateinit var loginDataModel: LoginDataModel
//
//    fun setBinder(binder: ActivityLoginBinding) {
//        this.binder = binder
//        this.mContext = binder.root.context
//        this.binder.viewClickHandler = ViewClickHandler();
//        this.loginDataModel = LoginDataModel()
//    }
//
//    inner class ViewClickHandler {
//
//        fun onLogin(view: View) {
//
//            try {
//                if (isValidate()) {
//                    doLogin(view)
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//
////            doLogin(view)
////            val i = Intent(mContext, DashboardActivity::class.java)
////            (mContext as Activity).startActivity(i)
//        }
//
//
//        fun onForgotPassword() {
//            try {
//                val i = Intent(mContext, ForgotPasswordActivity::class.java)
//                (mContext as Activity).startActivity(i)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//
//        }
//
//        fun onRegister() {
//            val i = Intent(mContext, SignupActivity::class.java)
//            (mContext as Activity).startActivity(i)
//        }
//
//        fun afterEmailTextChanged(s: CharSequence) {
//            loginDataModel.email = s.toString()
//            enableBtn()
//        }
//
//        fun afterPasswordTextChanged(s: CharSequence) {
//            loginDataModel.password = s.toString()
//            enableBtn()
//        }
//
//
//    }
//
//    private fun enableBtn() {
//        val emailValidator = EmailValidator(loginDataModel.email)
//        if (binder.ecEmail.text.isNullOrEmpty().not() && binder.ecPass.text.isNullOrEmpty().not() && emailValidator.isValid()) {
//            binder.btnLogin.isSelected = true
//            binder.btnLogin.isEnabled = true
//        } else {
//            binder.btnLogin.isSelected = false
//            binder.btnLogin.isEnabled = false
//        }
//    }
//
//    private fun doLogin(view: View) {
////        isInternetAvailable(mContext, object : CallbackListener {
////            override fun onSuccess() {
////                showDialog("", (mContext as LoginActivity))
////                binder.isLoading = true
////                loginDataModel.login(mContext as Activity).observeForever { loginData ->
////                    if (binder.isLoading!!) {
////                        binder.isLoading = false
//////                        dismissDialog()
////                    }
////                    onCallResult(loginData)
////                }
////            }
////
////            override fun onCancel() {
////            }
////
////            override fun onRetry() {
////                doLogin(view)
////            }
////        })
//    }
//
//    fun isValidate(): Boolean {
//        val emailValidator = EmailValidator(loginDataModel.email)
//        val passwordValidator = PasswordValidator(loginDataModel.password)
//        if (!emailValidator.isValid()) {
//            showToast(emailValidator.msg!!)
//            return false
//        } else if (!passwordValidator.isValidPassword()) {
//            showToast(passwordValidator.msg!!)
//            return false
//        }
//        return true
//    }
//
//    private fun onCallResult(it: UserData?) {
//        try {
//            if (it != null) {
//                Debug.e("----------", "---data-----" + Gson().toJson(it));
//                if (it.status == 200) {
////                    val i = Intent(mContext, DashboardActivity::class.java)
////                    (mContext as Activity).startActivity(i)
////                    (mContext as Activity).finish()
//                    var uData = it.data!!.user
//                    Utils.setPref(mContext, Constant.UID, uData!!.id!!)
//                    signIn(loginDataModel.email.toString(), loginDataModel.password.toString(), it.data!!.user!!)
//
//
//                } else {
//                    showToast(it.data!!.errors!!.error.get(0));
////                    dismissDialog()
//                }
//
//            }
//        } catch (e: Exception) {
//            showToast(it!!.data!!.message);
//        }
//
//    }
//
//    fun signIn(email: String, password: String, uData: User) {
//
//
//    }
//
//
//
//
//}