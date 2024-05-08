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
import com.databridge.mybridge.databinding.FragmentOnboarding2Binding
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
class OnBoarding2ViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val validation: Validation,
    private val sharedPref: SharedPref
) : ViewModel() {

    val jobTitleError = MutableLiveData<String>()
    val empError = MutableLiveData<String>()
    val companyError = MutableLiveData<String>()

    val collageError = MutableLiveData<String>()
    val yearError = MutableLiveData<String>()
    val dobError = MutableLiveData<String>()

    private val _countryresponse: MutableLiveData<IpDetailResponse> = MutableLiveData()
    val countryresponse: LiveData<IpDetailResponse> = _countryresponse

    private val _response: MutableLiveData<Resource<OnboardingResponse>> = MutableLiveData()
    val response: LiveData<Resource<OnboardingResponse>> = _response

    private val _getresponse: MutableLiveData<Resource<OnboardingDataResponse>> = MutableLiveData()
    val getresponse: LiveData<Resource<OnboardingDataResponse>> = _getresponse

    var countryCode: String = ""
    fun saveInPref(binding: FragmentOnboarding2Binding, isStudent: Boolean) {
        if (isStudent)
            sharedPref.setPrefString(
                Conts.PREF_PROFILE_ROLE,
                binding.tvSchool.text.toString())
        else
            sharedPref.setPrefString(
                Conts.PREF_PROFILE_ROLE,
                binding.etJobTitle.text.toString() + " at " + binding.etCompany.text.toString()
            )
//        sharedPref.setPrefBoolean("prefIsLogin", true)
    }

    fun saveOnBoardingDataInPref(rData: OnboardingResponse?) {
        rData.let { it ->


//            it?.data?.let {
//                sharedPref.setPrefString(Conts.prefExpTitle, it?.title.toString())
//                sharedPref.setPrefString(Conts.prefEmploymentType, uData.experience?.employmentType.toString())
//                sharedPref.setPrefString(Conts.prefExpCompany, uData.experience?.companyName.toString())
//                sharedPref.setPrefString(Conts.prefEduStartYear, uData.education?.startYear.toString())
//                sharedPref.setPrefString(Conts.prefEduEndYear, uData.education?.endYear.toString())
//                sharedPref.setPrefString(Conts.prefEduClgName, uData.education?.name.toString())
//            }

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


    fun apiCall(title: String, employment_type: String, company_name: String, is_student: Boolean) =
        viewModelScope.launch {
            _response.postValue(Resource.loading())
            val qmap = HashMap<String, String>()

            val email = sharedPref.getPrefString("prefEmail")!!
            qmap["step"] = Conts.O_STEP2
            qmap["email"] = encodeString(email)


            val map = HashMap<String, Any>()
            if (title.isNullOrEmpty().not())
                map["title"] = title
            if (employment_type.isNullOrEmpty().not())
                map["employment_type"] = employment_type
            if (company_name.isNullOrEmpty().not())
                map["company_name"] = company_name
            map["is_student"] = is_student

            Debug.e("----", "--btnLogin-apiCall-qmap-" + Gson().toJson(qmap))
            Debug.e("----", "--btnLogin-apiCall-" + Gson().toJson(map))
            repository.onboardingAPI(qmap, map).collect { values ->
                _response.value = values
            }
        }

    fun studentapiCall(
        name: String,
        start_year: String,
        end_year: String,
        is_student: Boolean,
        eligibility: Boolean,
        dob: String
    ) =
        viewModelScope.launch {
            _response.postValue(Resource.loading())
            val qmap = HashMap<String, String>()

//                "dob": "2023-12-01"

            val email = sharedPref.getPrefString("prefEmail")!!
            qmap["step"] = Conts.O_STEP2
            qmap["email"] = encodeString(email)

            val map = HashMap<String, Any>()
            if (name.isNullOrEmpty().not())
                map["name"] = name
            if (start_year.isNullOrEmpty().not())
                map["start_year"] = start_year
            if (end_year.isNullOrEmpty().not())
                map["end_year"] = end_year
            map["is_student"] = is_student
            map["eligibility"] = eligibility
            map["dob"] = dob

            Debug.e("----", "--btnLogin-apiCall-qmap-" + Gson().toJson(qmap))
            Debug.e("----", "--btnLogin-apiCall-" + Gson().toJson(map))
            repository.onboardingAPI(qmap, map).collect { values ->
                _response.value = values
            }
        }


    /* region text watcher */
    fun jobTextWatcher(): TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            jobtitleValidation(p0.toString())
        }
    }

    fun eTypeTextWatcher(): TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            empTypeValidation(p0.toString())
        }
    }

    fun companyTextWatcher(): TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            companyValidation(p0.toString())
        }
    }

    /* endregion */

    /* region validation */
    fun isStudentValidation(
        clg: String,
        startYear: String,
        endYear: String,
        dob: String,
        eligibility: Boolean
    ): Boolean {
        return collageValidation(clg) && startYearValidation(startYear) && endYearValidation(endYear) && dobValidation(
            dob
        )
    }

    fun validation(job: String, empType: String, company: String): Boolean {
        return jobtitleValidation(job) && empTypeValidation(empType) && companyValidation(company)
    }

    fun jobtitleValidation(title: String): Boolean {
        val isValid = validation.isEmptyValidation(
            title,
            validation.context.getString(R.string.valid_job_title)
        )
        jobTitleError.postValue(isValid)
        return isValid.isEmpty()
    }

    fun collageValidation(title: String): Boolean {
        val isValid = validation.isEmptyValidation(
            title,
            validation.context.getString(R.string.valid_collage)
        )
        collageError.postValue(isValid)
        return isValid.isEmpty()
    }

    fun startYearValidation(title: String): Boolean {
        val isValid = validation.isEmptyValidation(
            title,
            validation.context.getString(R.string.valid_start_year)
        )
        yearError.postValue(isValid)
        return isValid.isEmpty()
    }

    fun endYearValidation(title: String): Boolean {
        val isValid = validation.isEmptyValidation(
            title,
            validation.context.getString(R.string.valid_end_year)
        )
        yearError.postValue(isValid)
        return isValid.isEmpty()
    }

    fun dobValidation(title: String): Boolean {
//        val dob = convertDate(title).split("-")
        val isValid = validation.isAgeValidation(
            title,
            validation.context.getString(R.string.dob_error)
        )
        dobError.postValue(isValid)

//        var isAgeValid = false
//        try {
//            isAgeValid =
//                validation.calculateAge(dob[0].toInt(), dob[1].toInt(), dob[2].toInt()) > 13
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        Debug.e("----","--isAgeValid--"+isAgeValid)
//        return isValid.isEmpty() && isAgeValid
        return isValid.isEmpty()
    }


    fun empTypeValidation(emp: String): Boolean {
        val isValid = validation.isEmptyValidation(
            emp,
            validation.context.getString(R.string.please_enter_emp_type)
        )
        empError.postValue(isValid)
        return isValid.isEmpty()
    }

    fun companyValidation(company: String): Boolean {
        val isValid = validation.isEmptyValidation(
            company,
            validation.context.getString(R.string.valid_company)
        )
        companyError.postValue(isValid)
        return isValid.isEmpty()
    }

    /* endregion */
}