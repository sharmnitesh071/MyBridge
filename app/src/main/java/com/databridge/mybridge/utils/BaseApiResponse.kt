package com.databridge.mybridge.utils

import org.json.JSONObject
import retrofit2.Response

abstract class BaseApiResponse {

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Resource<T> {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return Resource.success(body)
                }
            }
            var errorMessage = ""

            try {
                val errorBody = response.errorBody()?.string()
                val jsonObject = JSONObject(errorBody)

                try {
                    if (jsonObject.has("detail"))
                        errorMessage = JSONObject(errorBody).getString("detail")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {
                    val dataArray = if (jsonObject.has("data")) {
                        jsonObject.getJSONArray("data")
                    } else {
                        null // If "data" key is not present, set the array to null
                    }

                    errorMessage = if (dataArray != null && dataArray.length() > 0) {
                        dataArray.getString(0)
                    } else ""
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {
                    if (jsonObject.has("email")) {
                        val emailError = jsonObject.getJSONArray("email").getString(0)
                        if (errorMessage.isEmpty())
                            errorMessage = emailError
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                Debug.e(
                    "----",
                    "--safeApiCall--" + "${response.body()} ${errorBody} ${errorMessage}"
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }


            Debug.e("----", "--safeApiCall--" + "${response.code()} ${response.message()}")
            return error(errorMessage, "${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(null, e.message ?: e.toString())
        }
    }

    private fun <T> error(responseBody: String?, errorMessage: String): Resource<T> =
        Resource.error(responseBody, "Api call failed $errorMessage")


}