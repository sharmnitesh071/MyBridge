package com.databridge.mybridge.ui.login.previewsigninSignup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.databridge.mybridge.common.SharedPref
import com.databridge.mybridge.common.Validation
import com.databridge.mybridge.domain.model.login.LoginRegisterResponse
import com.databridge.mybridge.domain.repository.LoginRepository
import com.databridge.mybridge.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreviewSigninSignupViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val validation: Validation,
    private val sharedPref: SharedPref
) : ViewModel() {


    private val _socialresponse: MutableLiveData<Resource<LoginRegisterResponse>> =
        MutableLiveData()
    val socialresponse: LiveData<Resource<LoginRegisterResponse>> = _socialresponse

    fun saveInPref(email: String) {
        sharedPref.setPrefString("prefEmail", email)
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
        }
    }

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


}