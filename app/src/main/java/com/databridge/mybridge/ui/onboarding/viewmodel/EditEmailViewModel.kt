package com.databridge.mybridge.ui.onboarding.viewmodel

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.databridge.mybridge.common.SharedPref
import com.databridge.mybridge.common.Validation
import com.databridge.mybridge.domain.model.BaseResponse
import com.databridge.mybridge.domain.repository.LoginRepository
import com.databridge.mybridge.utils.Debug
import com.databridge.mybridge.utils.Resource
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditEmailViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val validation: Validation,
    private val sharedPref: SharedPref
) : ViewModel() {

    val otpError = MutableLiveData<String>()

    private val _response: MutableLiveData<Resource<BaseResponse>> = MutableLiveData()
    val response: LiveData<Resource<BaseResponse>> = _response

    val emailError = MutableLiveData<String>()
    val newemailError = MutableLiveData<String>()
    val passwordError = MutableLiveData<String>()

    fun editEmail(email: String,password
    :String) = viewModelScope.launch {
        _response.postValue(Resource.loading())
        val map = HashMap<String, String>()
        map["email"] = email
        map["password"] = password
        Debug.e("----","--verifyPasswordReset-apiCall-"+ Gson().toJson(map))
        repository.editEmail(map).collect { values ->
            _response.value = values
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

    fun newEmailTextWatcher(): TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            newemailValidation(p0.toString())
        }
    }

    fun passwordTextWatcher(): TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            passwordValidation(p0.toString())
        }
    }

    fun validation(email: String,newEmail:String, password: String): Boolean {
        return emailValidation(email) && newemailValidation(newEmail) && passwordValidation(password)
    }

    fun emailValidation(email: String): Boolean {
        val emailValid = validation.emailValidation(email.toString())
        emailError.postValue(emailValid)
        return emailValid.isEmpty()
    }
    fun newemailValidation(email: String): Boolean {
        val emailValid = validation.emailValidation(email.toString())
        newemailError.postValue(emailValid)
        return emailValid.isEmpty()
    }

    fun passwordValidation(password: String): Boolean {
        val passwordValid = validation.passwordValidation(password.toString())
        passwordError.postValue(passwordValid)
        return passwordValid.isEmpty()
    }


    /* endregion */

    /* region validation */
    fun validation(otp: String): Boolean {
        return otpValidation(otp)
    }

    fun otpValidation(otp: String): Boolean {
        val otpValid = validation.otpValidation(otp.toString())
        otpError.postValue(otpValid)
        return otpValid.isEmpty()
    }

    /* endregion */

}