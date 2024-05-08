package com.databridge.mybridge.ui.onboarding.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.databridge.mybridge.domain.model.onboarding.SearchCityResponse
import com.databridge.mybridge.domain.repository.LoginRepository
import com.databridge.mybridge.utils.Debug
import com.databridge.mybridge.utils.Resource
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchLocationViewModel @Inject constructor(
    private val repository: LoginRepository
) : ViewModel() {


    private val _searchResponse: MutableLiveData<Resource<ArrayList<SearchCityResponse>>> =
        MutableLiveData()
    val searchResponse: LiveData<Resource<ArrayList<SearchCityResponse>>> = _searchResponse

    var countryCode: String = ""

    fun searchapiCall(address: String) = viewModelScope.launch {
        _searchResponse.postValue(Resource.loading())
        val qmap = HashMap<String, String>()

        qmap["country_code"] = countryCode
        qmap["address"] = address


        Debug.e("----", "--searchapiCall--qmap-" + Gson().toJson(qmap))
        repository.searchCityCountryAPI(qmap).collect { values ->
            Debug.e("---", "----" + Gson().toJson(values))
            _searchResponse.value = values
        }
    }
}