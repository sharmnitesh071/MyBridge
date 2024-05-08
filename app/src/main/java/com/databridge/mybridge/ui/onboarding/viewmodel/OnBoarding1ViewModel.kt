package com.databridge.mybridge.ui.onboarding.viewmodel

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.databridge.mybridge.R
import com.databridge.mybridge.base.encodeString
import com.databridge.mybridge.common.Conts
import com.databridge.mybridge.common.SharedPref
import com.databridge.mybridge.common.Validation
import com.databridge.mybridge.databinding.FragmentOnboardingBinding
import com.databridge.mybridge.domain.model.login.OnboardingDataResponse
import com.databridge.mybridge.domain.model.login.OnboardingResponse
import com.databridge.mybridge.domain.model.onboarding.IpDetailResponse
import com.databridge.mybridge.domain.repository.LoginRepository
import com.databridge.mybridge.utils.Debug
import com.databridge.mybridge.utils.Resource
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoarding1ViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val validation: Validation,
    private val sharedPref: SharedPref
) : ViewModel() {

    val countryError = MutableLiveData<String>()
    val cityError = MutableLiveData<String>()
    val postalCodeError = MutableLiveData<String>()

    private val _countryresponse: MutableLiveData<IpDetailResponse> = MutableLiveData()
    val countryresponse: LiveData<IpDetailResponse> = _countryresponse

    private val _response: MutableLiveData<Resource<OnboardingResponse>> = MutableLiveData()
    val response: LiveData<Resource<OnboardingResponse>> = _response

    private val _getresponse: MutableLiveData<Resource<OnboardingDataResponse>> = MutableLiveData()
    val getresponse: LiveData<Resource<OnboardingDataResponse>> = _getresponse


    var countryCode: String = ""
    fun saveInPref(binding: FragmentOnboardingBinding) {
        sharedPref.setPrefString(
            Conts.PREF_PROFILE_LOCATION,
            binding.etCity.text.toString() + ", " + binding.etCountry.text.toString()
        )
//        sharedPref.setPrefString("prefEmail", binding.etEmail.text.toString())
//        sharedPref.setPrefBoolean("prefIsLogin", true)
    }

    fun saveOnBoardingDataInPref(rData: OnboardingResponse?) {
        rData.let { it ->
//            it?.access?.let {
//                sharedPref.setPrefString("prefAccess", it)
//            }
//            it?.refresh?.let {
//                sharedPref.setPrefString("prefRefresh", it)
//            }
//            it?.matrixAccessToken?.let {
//                sharedPref.setPrefString("prefMatrixAccessToken", it)
//
//            }
//            it?.matrixRefreshToken?.let {
//                sharedPref.setPrefString("prefMatrixRefreshToken", it)
//            }
//
//            it?.matrixUserId?.let {
//                sharedPref.setPrefString("prefMatrixUserId", it)
//            }
//
//            it?.matrixDeviceId?.let {
//                sharedPref.setPrefString("prefMatrixDeviceId", it)
//            }
        }
    }

    fun getIpAddress() = viewModelScope.launch {
        repository.getIpAddress(Conts.IP_URL).collect { values ->
//            _response.value = values
            values?.data?.ip.let {
                it?.let { it1 -> getIpDetail(it1)
                    Debug.e("-----", "-getIpAddress--$it1")
                }
            }

        }
    }

    fun getIpDetail(ipAddress: String) = viewModelScope.launch {
        val ipDetail = "https://ipapi.co/$ipAddress/json/"
        repository.getIpDetail(ipDetail).collect { values ->
            values?.data?.let {
                _countryresponse.value = it
                Debug.e("-----", "-city--${it.city}")
                Debug.e("-----", "-country--${it.country}")
            }
        }
    }


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

    fun apiCall(country: String, city: String, state: String, postalCode: String) =
        viewModelScope.launch {
            _response.postValue(Resource.loading())
            val qmap = HashMap<String, String>()

            val email = sharedPref.getPrefString("prefEmail")!!
            qmap["step"] = Conts.O_STEP1
            qmap["email"] = encodeString(email)

            val map = HashMap<String, String>()
            map["country"] = country
            if (city.isNullOrEmpty().not())
                map["city"] = city
            if (state.isNullOrEmpty().not())
                map["state"] = state
            if (postalCode.isNullOrEmpty().not())
                map["postal_code"] = postalCode

//            val token = SplashActivity.authToken
//            val authToken = "Bearer $token"
//            var hMap = HashMap<String, String>()
//            hMap["Authorization"] = authToken


            Debug.e("----", "--btnLogin-apiCall-qmap-" + Gson().toJson(qmap))
            Debug.e("----", "--btnLogin-apiCall-" + Gson().toJson(map))
            repository.onboarding1API(qmap, map).collect { values ->
                _response.value = values
            }
        }


    /* region text watcher */
    fun countryextWatcher(): TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            countryValidation(p0.toString())
        }
    }

    fun cityTextWatcher(): TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            cityValidation(p0.toString())
        }
    }

    fun postalTextWatcher(): TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            postalcodeValidation(p0.toString())
        }
    }

    /* endregion */

    /* region validation */
    fun validation(country: String, city: String): Boolean {
        return countryValidation(country) && cityValidation(city)
    }

    fun validation(country: String, city: String, postalCode: String): Boolean {
        return countryValidation(country) && cityValidation(city) && postalcodeValidation(postalCode)
    }

    fun cityValidation(city: String): Boolean {
        val cityValid = validation.isEmptyValidation(
            city,
            validation.context.getString(R.string.please_select_city)
        )
        cityError.postValue(cityValid)
        return cityValid.isEmpty()
    }

    fun countryValidation(country: String): Boolean {
        val countryValid = validation.isEmptyValidation(
            country,
            validation.context.getString(R.string.please_select_country)
        )
        countryError.postValue(countryValid)
        return countryValid.isEmpty()
    }

    fun postalcodeValidation(postalCode: String): Boolean {
        val postalCodeValid = validation.isEmptyValidation(
            postalCode,
            validation.context.getString(R.string.please_enter_postal_code)
        )
        postalCodeError.postValue(postalCodeValid)
        return postalCodeValid.isEmpty()
    }

    /* endregion */
}