package com.databridge.mybridge.ui.login.setnewpass.viewmodel

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.databridge.mybridge.R
import com.databridge.mybridge.common.SharedPref
import com.databridge.mybridge.common.Validation
import com.databridge.mybridge.domain.model.BaseResponse
import com.databridge.mybridge.domain.repository.LoginRepository
import com.databridge.mybridge.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetNewPasswordViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val validation: Validation,
    private val sharedPref: SharedPref
) : ViewModel() {

    val passwordError = MutableLiveData<String>()
    val conpasswordError = MutableLiveData<String>()

    private val _response: MutableLiveData<Resource<BaseResponse>> = MutableLiveData()
    val response: LiveData<Resource<BaseResponse>> = _response


    fun confirmPasswordApi(token: String,password:String,password2:String) = viewModelScope.launch {
        _response.postValue(Resource.loading())
        val map = HashMap<String, String>()
        map["token"] = token
        map["password"] = password
        map["password2"] = password2

        repository.confirmPassword(map).collect { values ->
            _response.value = values
        }
    }


    /* region text watcher */

    var pass= ""
    fun passwordTextWatcher(): TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            passwordValidation(p0.toString(), p0.toString(),false)
            pass = p0.toString()
        }
    }

    fun cpasswordTextWatcher(): TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            passwordValidation(pass, p0.toString(),true)
        }
    }

    /* endregion */

    /* region validation */
    fun validation(
        password: String,
        conpassword: String,
    ): Boolean {
        return passwordValidation(password,password,false) && passwordValidation(password,conpassword,true)
    }

    fun passwordValidation(password: String,conpassword: String, cPass: Boolean): Boolean {
        val passwordValid = validation.passwordValidation(password)
        if(cPass){
            val conPasswordValid = validation.passwordValidation(conpassword)
            conpasswordError.postValue(conPasswordValid)
            if(password != conpassword){
                conpasswordError.postValue(validation.context.getString(R.string.match_password))
                return false
            }
        }else{
            passwordError.postValue(passwordValid)
        }

        return passwordValid.isEmpty()
    }

    /* endregion */


}