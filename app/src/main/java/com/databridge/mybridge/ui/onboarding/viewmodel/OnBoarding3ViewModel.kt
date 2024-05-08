package com.databridge.mybridge.ui.onboarding.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.databridge.mybridge.R
import com.databridge.mybridge.base.encodeString
import com.databridge.mybridge.common.Conts
import com.databridge.mybridge.common.SharedPref
import com.databridge.mybridge.common.Validation
import com.databridge.mybridge.databinding.FragmentOnboarding3Binding
import com.databridge.mybridge.domain.model.login.OnboardingDataResponse
import com.databridge.mybridge.domain.model.login.OnboardingResponse
import com.databridge.mybridge.domain.repository.LoginRepository
import com.databridge.mybridge.utils.Debug
import com.databridge.mybridge.utils.Resource
import com.databridge.mybridge.utils.Utils
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class OnBoarding3ViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val validation: Validation,
    private val sharedPref: SharedPref
) : ViewModel() {


    private val _response: MutableLiveData<Resource<OnboardingResponse>> = MutableLiveData()
    val response: LiveData<Resource<OnboardingResponse>> = _response

    private val _getresponse: MutableLiveData<Resource<OnboardingDataResponse>> = MutableLiveData()
    val getresponse: LiveData<Resource<OnboardingDataResponse>> = _getresponse


    var countryCode: String = ""
    fun saveInPref(binding: FragmentOnboarding3Binding) {

    }

    fun saveOnBoardingDataInPref(rData: OnboardingResponse?) {
        rData.let { it ->

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
            qmap["step"] = Conts.O_STEP2

            Debug.e("----", "---getonboardingData-qmap-" + Gson().toJson(qmap))
            repository.getonboardingData(qmap).collect { values ->
                _getresponse.value = values
            }
        }

    fun apiCall(useGooglePhoto: Boolean) =
        viewModelScope.launch {
            _response.postValue(Resource.loading())
            val qmap = HashMap<String, String>()

            val email = sharedPref.getPrefString("prefEmail")!!
            qmap["step"] = Conts.O_STEP3
            qmap["email"] = encodeString(email)

            val map = HashMap<String, String>()
            map["useGooglePhoto"] = useGooglePhoto.toString()

            Debug.e("----", "--photo-apiCall-qmap-" + Gson().toJson(qmap))
            Debug.e("----", "--photo-apiCall-" + Gson().toJson(map))
            repository.onboarding1API(qmap, map).collect { values ->
                _response.value = values
            }
        }

    fun apiCallPhoto(
        context: Context,
        fileUrl: File
    ) =
        viewModelScope.launch {
            _response.postValue(Resource.loading())
            val qmap = HashMap<String, String>()

            val email = sharedPref.getPrefString("prefEmail")!!
            qmap["step"] = Conts.O_STEP3
            qmap["email"] = encodeString(email)
            val builder = MultipartBody.Builder().setType(MultipartBody.FORM)

            val filename = context.getString(R.string.app_name).replace(
                " ",
                "_"
            ) + "_" + System.currentTimeMillis() + "." + Utils.getFileExt(fileUrl.name)
            Debug.e("-------", "---fileUrl----$fileUrl")
            Debug.e("-------", "---filename----${filename}")
            try {
                builder
                    .addFormDataPart(
                        "profile_pic", filename,
                        fileUrl.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    )

            } catch (e: IOException) {
                e.printStackTrace()
            }

            val requestBody: MultipartBody = builder.build()


            Debug.e("----", "--btnLogin-apiCall-qmap-" + Gson().toJson(qmap))
//            Debug.e("----", "--btnLogin-apiCall-" + Gson().toJson(map))
            repository.onboardingProfileImageAPI(qmap, requestBody).collect { values ->
                _response.value = values
            }
        }


}