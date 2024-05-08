package com.databridge.mybridge.ui.onboarding.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.databridge.mybridge.base.encodeString
import com.databridge.mybridge.common.Conts
import com.databridge.mybridge.common.SharedPref
import com.databridge.mybridge.common.Validation
import com.databridge.mybridge.domain.model.login.OnboardingDataResponse
import com.databridge.mybridge.domain.model.login.OnboardingResponse
import com.databridge.mybridge.domain.repository.LoginRepository
import com.databridge.mybridge.utils.Debug
import com.databridge.mybridge.utils.Resource
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoarding4ViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val validation: Validation,
    private val sharedPref: SharedPref
) : ViewModel() {

    private val _response: MutableLiveData<Resource<OnboardingResponse>> = MutableLiveData()
    val response: LiveData<Resource<OnboardingResponse>> = _response

    private val _getresponse: MutableLiveData<Resource<OnboardingDataResponse>> = MutableLiveData()
    val getresponse: LiveData<Resource<OnboardingDataResponse>> = _getresponse


    var countryCode: String = ""

    fun getOnboardingapiCall() =
        viewModelScope.launch {
//            _response.postValue(Resource.loading())
            val qmap = HashMap<String, String>()

            val email = sharedPref.getPrefString("prefEmail")!!
            Debug.e("----", "---getonboardingData-email-" + email)

//            qmap["email"] = encodeString(email)
            qmap["email"] = email
            qmap["step"] = Conts.O_STEP1

            Debug.e("----", "---getonboardingData-qmap-" + Gson().toJson(qmap))
            repository.getonboardingData(qmap).collect { values ->
                _getresponse.value = values
            }
        }

    fun apiCall(jobAvailabilty: String) =
        viewModelScope.launch {
            _response.postValue(Resource.loading())
            val qmap = HashMap<String, String>()

            val email = sharedPref.getPrefString("prefEmail")!!
            qmap["step"] = Conts.O_STEP4
            qmap["email"] = encodeString(email)

            val map = HashMap<String, String>()
            map["job_availabilty"] = jobAvailabilty

            Debug.e("----", "--onboarding-apiCall-qmap-" + Gson().toJson(qmap))
            Debug.e("----", "--onboarding-apiCall-" + Gson().toJson(map))
            repository.onboarding1API(qmap, map).collect { values ->
                _response.value = values
            }
        }

}