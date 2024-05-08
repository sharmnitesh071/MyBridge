package com.databridge.mybridge.ui.login.forgotpass.viewmodel

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
class ForgotPasswordViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val validation: Validation,
    private val sharedPref: SharedPref
) : ViewModel() {

    val emailError = MutableLiveData<String>()

    private val _response: MutableLiveData<Resource<BaseResponse>> = MutableLiveData()
    val response: LiveData<Resource<BaseResponse>> = _response

    fun sendOtpToVerify(email: String) = viewModelScope.launch {
        _response.postValue(Resource.loading())
        val map = HashMap<String, String>()
        map["email"] = email

        Debug.e("----","--btnLogin-apiCall-"+ Gson().toJson(map))
        repository.sendOtpToVerify(map).collect { values ->
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

    /* endregion */

    /* region validation */
    fun validation(email: String): Boolean {
        return emailValidation(email)
    }

    fun emailValidation(email: String): Boolean {
        val emailValid = validation.emailValidation(email.toString())
        emailError.postValue(emailValid)
        return emailValid.isEmpty()
    }

    /* endregion */

}