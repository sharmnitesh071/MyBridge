package com.databridge.mybridge.ui.onboarding.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.databridge.mybridge.base.createShortName
import com.databridge.mybridge.common.Conts
import com.databridge.mybridge.common.SharedPref
import com.databridge.mybridge.common.Validation
import com.databridge.mybridge.domain.model.login.OnboardingDataResponse
import com.databridge.mybridge.domain.model.login.UserInfo
import com.databridge.mybridge.domain.repository.LoginRepository
import com.databridge.mybridge.utils.Debug
import com.databridge.mybridge.utils.Resource
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val validation: Validation,
    private val sharedPref: SharedPref
) : ViewModel() {


    private val _response: MutableLiveData<Resource<OnboardingDataResponse>> = MutableLiveData()
    val response: LiveData<Resource<OnboardingDataResponse>> = _response

   private val _userInforesponse: MutableLiveData<Resource<UserInfo>> = MutableLiveData()
    val userInforesponse: LiveData<Resource<UserInfo>> = _userInforesponse

    private fun saveProfileDataInPref(uData: UserInfo) {
        sharedPref.setPrefString(Conts.PREF_ID, uData.id.toString())
        sharedPref.setPrefString(Conts.PREF_DISPLAY_NAME, uData.displayName.toString())
        sharedPref.setPrefString(Conts.PREF_FIRST_NAME, uData.firstName.toString())
        sharedPref.setPrefString(Conts.PREF_LAST_NAME, uData.lastName.toString())
        sharedPref.setPrefString(Conts.PREF_COUNTRY, uData.country.toString())
        sharedPref.setPrefString(Conts.PREF_STATE, uData.state.toString())
        sharedPref.setPrefString(Conts.PREF_CITY, uData.city.toString())
        sharedPref.setPrefString(Conts.PREF_PROFILE_PIC, uData.profilePic.toString())

        var pPic = uData.profilePicChars
        Debug.e("---","--short name-1-"+pPic)
        Debug.e("---","--short name-2-"+uData.displayName)
        if(pPic.isNullOrEmpty()) {
            Debug.e("---","--short name-4-"+pPic)
            pPic = uData.displayName?.let { it.createShortName() } ?: ""
        }

        Debug.e("---","--short name-3-"+pPic)
        sharedPref.setPrefString(Conts.PREF_PROFILE_PIC_CHARS, pPic)
        uData.isOnboardingCompleted?.let {
            sharedPref.setPrefBoolean(Conts.PREF_IS_ONBOARDING_COMPLETED,
                it
            )
        }
        uData.isEmailVerified?.let { sharedPref.setPrefBoolean(Conts.PREF_IS_EMAIL_VERIFIED, it) }
        sharedPref.setPrefString(Conts.PREF_EXP_TITLE, uData.experience?.title.toString())
        sharedPref.setPrefString(Conts.PREF_EMPLOYMENT_TYPE, uData.experience?.employmentType.toString())
        sharedPref.setPrefString(Conts.PREF_EXP_COMPANY, uData.experience?.companyName.toString())
        sharedPref.setPrefString(Conts.PREF_EDU_START_YEAR, uData.education?.startYear.toString())
        sharedPref.setPrefString(Conts.PREF_EDU_END_YEAR, uData.education?.endYear.toString())
        sharedPref.setPrefString(Conts.PREF_EDU_CLG_NAME, uData.education?.name.toString())
    }

    fun apiCall() =
        viewModelScope.launch {
            _response.postValue(Resource.loading())
            val qmap = HashMap<String, String>()

            val email = sharedPref.getPrefString("prefEmail")!!
            Debug.e("----", "---getonboardingData-email-" + email)

//            qmap["email"] = encodeString(email)
            qmap["email"] = email

            Debug.e("----", "---getonboardingData-qmap-" + Gson().toJson(qmap))
            repository.getonboardingData(qmap).collect { values ->
                _response.value = values
            }
        }
    fun profileInfoApiCall() =
        viewModelScope.launch {
            _userInforesponse.postValue(Resource.loading())
            val qmap = HashMap<String, String>()

//            val email = sharedPref.getPrefString("prefEmail")!!
//            qmap["email"] = email

            Debug.e("----", "---getonboardingData-qmap-" + Gson().toJson(qmap))
            repository.getProfileInfo(qmap).collect { values ->
                when (values.status) {
                    Resource.Status.SUCCESS -> {
                        _userInforesponse.value = values
                        values.data?.let {
                            saveProfileDataInPref(it) }

                    }

                    Resource.Status.ERROR -> {}
                    Resource.Status.LOADING -> {}
                }
            }
        }



}