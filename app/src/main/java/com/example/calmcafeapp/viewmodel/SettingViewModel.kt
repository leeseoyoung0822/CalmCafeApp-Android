package com.example.calmcafeapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.calmcafeapp.api.SurveyService
import com.example.calmcafeapp.apiManager.ApiManager
import com.example.calmcafeapp.data.SurveyRequest
import com.example.calmcafeapp.data.SurveyResponse
import com.example.calmcafeapp.login.LocalDataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingViewModel(application: Application) : AndroidViewModel(application) {

    private val surveyService: SurveyService = ApiManager.surveyService

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _isSurveySubmitted = MutableLiveData<Boolean>()
    val isSurveySubmitted: LiveData<Boolean> get() = _isSurveySubmitted

    // 설문조사 데이터 제출 함수
    fun submitSurvey(surveyRequest: SurveyRequest) {
        _isLoading.value = true
        val accessToken = LocalDataSource.getAccessToken()
        if (accessToken == null) {
            _isLoading.value = false
            _errorMessage.value = "Access token is null."
            return
        }

        surveyService.submitSurvey("Bearer $accessToken", surveyRequest)
            .enqueue(object : Callback<SurveyResponse> {
                override fun onResponse(call: Call<SurveyResponse>, response: Response<SurveyResponse>) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        _isSurveySubmitted.value = true
                        Log.d("SettingViewModel", "Survey submitted successfully.")
                    } else {
                        _isSurveySubmitted.value = false
                        _errorMessage.value = response.errorBody()?.string() ?: "Submission failed."
                        Log.e("SettingViewModel", "Failed to submit survey: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<SurveyResponse>, t: Throwable) {
                    _isLoading.value = false
                    _isSurveySubmitted.value = false
                    _errorMessage.value = "Network error: ${t.message}"
                    Log.e("SettingViewModel", "Network error", t)
                }
            })
    }
}