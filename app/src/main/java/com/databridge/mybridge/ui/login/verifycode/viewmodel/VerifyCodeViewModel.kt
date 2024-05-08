package com.databridge.mybridge.ui.login.verifycode.viewmodel

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.databridge.mybridge.common.SharedPref
import com.databridge.mybridge.common.Validation
import com.databridge.mybridge.domain.model.BaseResponse
import com.databridge.mybridge.domain.model.VerifyCodeResponse
import com.databridge.mybridge.domain.repository.LoginRepository
import com.databridge.mybridge.utils.Debug
import com.databridge.mybridge.utils.Resource
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyCodeViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val validation: Validation,
    private val sharedPref: SharedPref
) : ViewModel() {

    val otpError = MutableLiveData<String>()

    private val _response: MutableLiveData<Resource<VerifyCodeResponse>> = MutableLiveData()
    val response: LiveData<Resource<VerifyCodeResponse>> = _response

    fun verifyPasswordReset(email: String,otp:String) = viewModelScope.launch {
        _response.postValue(Resource.loading())
        val map = HashMap<String, String>()
        map["email"] = email
        map["otp"] = otp

        Debug.e("----","--verifyPasswordReset-apiCall-"+ Gson().toJson(map))
        repository.verifyPasswordReset(map).collect {
            values ->
            _response.value = values
        }
    }

    private val _resendResponse: MutableLiveData<Resource<BaseResponse>> = MutableLiveData()
    val resendResponse: LiveData<Resource<BaseResponse>> = _resendResponse

    fun sendOtpToVerify(email: String) = viewModelScope.launch {
        _response.postValue(Resource.loading())
        val map = HashMap<String, String>()
        map["email"] = email

        Debug.e("----","--btnLogin-apiCall-"+ Gson().toJson(map))
        repository.sendOtpToVerify(map).collect { values ->
            _resendResponse.value = values
        }
    }


    /* region text watcher */
    fun otpTextWatcher(): TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            otpValidation(p0.toString())
        }
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