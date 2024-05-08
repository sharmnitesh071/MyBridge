package com.databridge.mybridge.ui.onboarding.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.databridge.mybridge.base.encodeString
import com.databridge.mybridge.common.Conts
import com.databridge.mybridge.common.SharedPref
import com.databridge.mybridge.common.Validation
import com.databridge.mybridge.domain.model.BaseResponse
import com.databridge.mybridge.domain.model.login.OnboardingResponse
import com.databridge.mybridge.domain.model.onboarding.UseSuggestions
import com.databridge.mybridge.domain.model.onboarding.UserSuggestionList
import com.databridge.mybridge.domain.repository.LoginRepository
import com.databridge.mybridge.utils.Debug
import com.databridge.mybridge.utils.Resource
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoarding6ViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val validation: Validation,
    private val sharedPref: SharedPref
) : ViewModel() {

    private val _response: MutableLiveData<Resource<OnboardingResponse>> = MutableLiveData()
    val response: LiveData<Resource<OnboardingResponse>> = _response

    private val _getresponse: MutableLiveData<Resource<UseSuggestions>> = MutableLiveData()
    val getresponse: LiveData<Resource<UseSuggestions>> = _getresponse

    private val _followresponse: MutableLiveData<Resource<BaseResponse>> = MutableLiveData()
    val followresponse: LiveData<Resource<BaseResponse>> = _followresponse

    private val _handshakeresponse: MutableLiveData<Resource<BaseResponse>> = MutableLiveData()
    val handshakeresponse: LiveData<Resource<BaseResponse>> = _handshakeresponse


    var countryCode: String = ""

    fun getUserSuggestions() =
        viewModelScope.launch {
//            _response.postValue(Resource.loading())
            val qmap = HashMap<String, String>()

            qmap["filter_type"] = "[university,follow,degree,role,industry,location]"

            Debug.e("----", "---getUserSuggestions-qmap-" + Gson().toJson(qmap))
            repository.getUserSuggestions(qmap).collect { values ->
                _getresponse.value = values
            }
        }

    fun apiCall() =
        viewModelScope.launch {
            _response.postValue(Resource.loading())
            val qmap = HashMap<String, String>()

            val email = sharedPref.getPrefString("prefEmail")!!
            qmap["step"] = Conts.O_STEP6
            qmap["email"] = encodeString(email)

            val map = HashMap<String, String>()

            Debug.e("----", "--onboarding-apiCall-qmap-" + Gson().toJson(qmap))
            Debug.e("----", "--onboarding-apiCall-" + Gson().toJson(map))
            repository.onboarding1API(qmap, map).collect { values ->
                _response.value = values
            }
        }

    fun sendFollowRequestApi(cData: UserSuggestionList) =
        viewModelScope.launch {
            _followresponse.postValue(Resource.loading())
            val map = HashMap<String, String>()
            val fNo = sharedPref.getPrefString(Conts.PREF_ID)!!
            map["follow_to"] = fNo

            Debug.e("----", "--sendFollowRequestApi-apiCall-" + Gson().toJson(map))
            repository.sendFollowRequest(map).collect { values ->
                _followresponse.value = values
            }
        }

    fun sendHandshakeRequestApi(cData: UserSuggestionList) =
        viewModelScope.launch {
            _followresponse.postValue(Resource.loading())
            val map = HashMap<String, Any>()

            map["to_user_id"] = cData.id.toString()
//            map["chat_room_id"] =
//            {to_user_id: 256, chat_room_id: null}

            Debug.e("----", "--sendHandshakeRequestApi-apiCall-" + Gson().toJson(map))
            repository.sendHandshakeRequest(map).collect { values ->
                _handshakeresponse.value = values
            }
        }

    fun sendUnFollowRequestApi() =
        viewModelScope.launch {
            _followresponse.postValue(Resource.loading())
            val map = HashMap<String, String>()
            val fNo = sharedPref.getPrefString(Conts.PREF_ID)!!
            map["follow_to"] = fNo

            Debug.e("----", "--sendFollowRequestApi-apiCall-" + Gson().toJson(map))
            repository.sendUnFollowRequest(map).collect { values ->
                _followresponse.value = values
            }
        }

}