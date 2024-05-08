package com.databridge.mybridge.ui.login.login.viewmodel

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.databridge.mybridge.common.SharedPref
import com.databridge.mybridge.common.Validation
import com.databridge.mybridge.databinding.ActivityLoginBinding
import com.databridge.mybridge.domain.model.login.LoginRegisterResponse
import com.databridge.mybridge.domain.repository.LoginRepository
import com.databridge.mybridge.ui.splash.view.SplashActivity
import com.databridge.mybridge.utils.Debug
import com.databridge.mybridge.utils.Resource
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val validation: Validation,
    private val sharedPref: SharedPref
) : ViewModel() {

    val emailError = MutableLiveData<String>()
    val passwordError = MutableLiveData<String>()

    private val _response: MutableLiveData<Resource<LoginRegisterResponse>> = MutableLiveData()
    val response: LiveData<Resource<LoginRegisterResponse>> = _response

    private val _socialresponse: MutableLiveData<Resource<LoginRegisterResponse>> =
        MutableLiveData()
    val socialresponse: LiveData<Resource<LoginRegisterResponse>> = _socialresponse

    fun saveInPref(binding: ActivityLoginBinding) {
        sharedPref.setPrefString("prefEmail", binding.etEmail.text.toString())
        sharedPref.setPrefBoolean("prefIsLogin", true)
    }

    fun saveLoginDataInPref(rData: LoginRegisterResponse?) {
        rData.let { it ->
            it?.access?.let {
                sharedPref.setPrefString("prefAccess", it)
            }
            it?.refresh?.let {
                sharedPref.setPrefString("prefRefresh", it)
            }
            it?.matrixAccessToken?.let {
                sharedPref.setPrefString("prefMatrixAccessToken", it)

            }
            it?.matrixRefreshToken?.let {
                sharedPref.setPrefString("prefMatrixRefreshToken", it)
            }

            it?.matrixUserId?.let {
                sharedPref.setPrefString("prefMatrixUserId", it)
            }

            it?.matrixDeviceId?.let {
                sharedPref.setPrefString("prefMatrixDeviceId", it)
            }

            SplashActivity.authToken = sharedPref.getPrefString("prefAccess")!!
        }
    }


    fun apiCall(email: String, password: String, checkRemember: Boolean) = viewModelScope.launch {
        _response.postValue(Resource.loading())
        val map = HashMap<String, String>()
        map["email"] = email
        map["password"] = password
//        map["remember_me"] = checkRemember.toString()

        Debug.e("----","--btnLogin-apiCall-"+ Gson().toJson(map))
        repository.login(map).collect { values ->
            _response.value = values
        }
    }
//    fun refreshApiToken() = viewModelScope.launch {
//        _response.postValue(Resource.loading())
//        val map = HashMap<String, String>()
//        map["refresh"] = sharedPref.getPrefString("prefRefresh").toString()
//
//        Debug.e("----","--refreshApiToken-apiCall-"+ Gson().toJson(map))
//        repository.refreshApiToken(map).collect { values ->
//            _response.value = values
//        }
//    }

    fun socialLoginapiCall(
        idToken: String,
        email: String,
        firstName: String,
        lastName: String,
        accessToken: String
    ) = viewModelScope.launch {
        _socialresponse.postValue(Resource.loading())
        val map = HashMap<String, String>()
        map["id_token"] = idToken
        map["email"] = email
        map["first_name"] = firstName
        map["last_name"] = lastName
        map["platform"] = "google-login"
        map["access_token"] = accessToken

        repository.socialLogin(map).collect { values ->
            _socialresponse.value = values
        }
    }

    /* region text watcher */
    fun emailTextWatcher(): TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            emailValidation(p0.toString())
        }
    }

    fun passwordTextWatcher(): TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            passwordValidation(p0.toString())
        }
    }

    /* endregion */

    /* region validation */
    fun validation(email: String, password: String): Boolean {
        return emailValidation(email) && passwordValidation(password)
    }

    fun emailValidation(email: String): Boolean {
        val emailValid = validation.emailValidation(email.toString())
        emailError.postValue(emailValid)
        return emailValid.isEmpty()
    }

    fun passwordValidation(password: String): Boolean {
        val passwordValid = validation.passwordValidation(password.toString())
        passwordError.postValue(passwordValid)
        return passwordValid.isEmpty()
    }
    /* endregion */

    fun endIconColorChange(mContext: Context, binding: ActivityLoginBinding) {
        binding.etEmail.setOnFocusChangeListener { _, hasFocus ->
//            val color = if (hasFocus) ContextCompat.getColor(
//                mContext,
//                R.color.purple_700
//            ) else ContextCompat.getColor(mContext, R.color.grey_text_color)
//            binding.tilEmail.setEndIconTintList(ColorStateList.valueOf(color))
        }
        binding.etPassword.setOnFocusChangeListener { _, hasFocus ->
//            val color = if (hasFocus) ContextCompat.getColor(
//                mContext,
//                R.color.purple_700
//            ) else ContextCompat.getColor(mContext, R.color.grey_text_color)
//            binding.tilPassword.setEndIconTintList(ColorStateList.valueOf(color))
        }
    }
}