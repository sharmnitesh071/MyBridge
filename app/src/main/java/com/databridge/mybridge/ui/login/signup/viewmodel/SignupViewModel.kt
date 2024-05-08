package com.databridge.mybridge.ui.login.signup.viewmodel

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.databridge.mybridge.R
import com.databridge.mybridge.common.SharedPref
import com.databridge.mybridge.common.Validation
import com.databridge.mybridge.databinding.ActivitySignupBinding
import com.databridge.mybridge.domain.model.login.LoginRegisterResponse
import com.databridge.mybridge.domain.repository.LoginRepository
import com.databridge.mybridge.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val validation: Validation,
    private val sharedPref: SharedPref
) : ViewModel() {

    val fNameError = MutableLiveData<String>()
    val lNameError = MutableLiveData<String>()
    val emailError = MutableLiveData<String>()
    val passwordError = MutableLiveData<String>()
    val conpasswordError = MutableLiveData<String>()

    private val _response: MutableLiveData<Resource<LoginRegisterResponse>> = MutableLiveData()
    val response: LiveData<Resource<LoginRegisterResponse>> = _response

    private val _socialresponse: MutableLiveData<Resource<LoginRegisterResponse>> = MutableLiveData()
    val socialresponse: LiveData<Resource<LoginRegisterResponse>> = _socialresponse

    fun saveInPref(binding: ActivitySignupBinding) {
        sharedPref.setPrefString("prefEmail", binding.etEmail.text.toString())
        sharedPref.setPrefBoolean("prefIsLogin", true)
    }

//    fun saveLoginDataInPref(rData: LoginRegisterResponse?) {
//        val access = rData!!.access
//        val refresh = rData.refresh
//        val matrixAccessToken = rData.matrixAccessToken
//        val matrixUserId = rData.matrixUserId
//        val matrixRefreshToken = rData.matrixRefreshToken
//        val matrixDeviceId = rData.matrixDeviceId
//        sharedPref.setPrefString("prefAccess", access!!)
//        sharedPref.setPrefString("prefRefresh", refresh!!)
//        sharedPref.setPrefString("prefMatrixAccessToken", matrixAccessToken!!)
//        sharedPref.setPrefString("prefMatrixUserId", matrixUserId!!)
//        sharedPref.setPrefString("prefMatrixRefreshToken", matrixRefreshToken!!)
//        sharedPref.setPrefString("prefMatrixDeviceId", matrixDeviceId!!)
//    }

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
        }
    }

    fun signupApi(email: String, firstName: String,lastName:String,password:String,password2:String) = viewModelScope.launch {
        _response.postValue(Resource.loading())
        val map = HashMap<String, String>()
        map["email"] = email
        map["first_name"] = firstName
        map["last_name"] = lastName
        map["password"] = password
        map["password2"] = password2

        repository.signupApi(map).collect { values ->
            _response.value = values
        }
    }

    fun socialLoginapiCall(idToken:String,email: String, firstName: String,lastName:String,accessToken:String) = viewModelScope.launch {
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

    fun fnameTextWatcher(): TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            fNameValidation(p0.toString())
        }
    }

    fun lnameTextWatcher(): TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            lNameValidation(p0.toString())
        }
    }
    fun emailTextWatcher(): TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            emailValidation(p0.toString())
        }
    }

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
        email: String,
        password: String,
        conpassword: String,
        fName: String,
        lName: String
    ): Boolean {
        return emailValidation(email) && passwordValidation(password,password,false) && passwordValidation(password,conpassword,true)
    }

    fun fNameValidation(name: String): Boolean {
        val nameValid = validation.fnameValidation(name.toString())
        fNameError.postValue(nameValid)
        return nameValid.isEmpty()
    }

    fun lNameValidation(name: String): Boolean {
        val nameValid = validation.lnameValidation(name.toString())
        lNameError.postValue(nameValid)
        return nameValid.isEmpty()
    }

    fun emailValidation(email: String): Boolean {
        val emailValid = validation.emailValidation(email.toString())
        emailError.postValue(emailValid)
        return emailValid.isEmpty()
    }

    fun passwordValidation(password: String,conpassword: String, cPass: Boolean): Boolean {
        val passwordValid = validation.passwordValidation(password.toString())
        if(cPass){
            conpasswordError.postValue(passwordValid)
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