package com.example.calmcafeapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.calmcafeapp.api.SettingService
import com.example.calmcafeapp.apiManager.ApiManager
import com.example.calmcafeapp.data.FavoriteResponse
import com.example.calmcafeapp.data.FavoriteStore
import com.example.calmcafeapp.data.PointCoupon
import com.example.calmcafeapp.data.PointCouponResponse
import com.example.calmcafeapp.data.SurveyRequest
import com.example.calmcafeapp.data.SurveyResponse
import com.example.calmcafeapp.data.UserProfile
import com.example.calmcafeapp.data.UserProfileResponse
import com.example.calmcafeapp.login.LocalDataSource
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingViewModel(application: Application) : AndroidViewModel(application) {

    private val settingService: SettingService = ApiManager.settingService

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _isSurveySubmitted = MutableLiveData<Boolean>()
    val isSurveySubmitted: LiveData<Boolean> get() = _isSurveySubmitted

    private val _favoriteStores = MutableLiveData<List<FavoriteStore>>()
    val favoriteStores: LiveData<List<FavoriteStore>> get() = _favoriteStores

    private val _pointCoupons = MutableLiveData<List<PointCoupon>>()
    val pointCoupons: LiveData<List<PointCoupon>> get() = _pointCoupons

    private val _userProfile = MutableLiveData<UserProfile>()
    val userProfile: LiveData<UserProfile> get() = _userProfile

    // 설문조사 데이터 제출 함수
    fun submitSurvey(surveyRequest: SurveyRequest) {
        _isLoading.value = true
        val accessToken = LocalDataSource.getAccessToken()
        if (accessToken == null) {
            _isLoading.value = false
            _errorMessage.value = "Access token is null."
            return
        }
        // 요청 데이터 로그로 출력
        Log.d("SurveyRequest", "Request Data: ${Gson().toJson(surveyRequest)}")

        settingService.submitSurvey("Bearer $accessToken", surveyRequest)
            .enqueue(object : Callback<SurveyResponse> {
                override fun onResponse(call: Call<SurveyResponse>, response: Response<SurveyResponse>) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        _isSurveySubmitted.value = true
                        Log.d("SettingViewModel", "Survey submitted successfully.")
                    } else {
                        _isSurveySubmitted.value = false
                        val errorBody = response.errorBody()?.string()
                        val errorMessage = response.body()?.message ?: errorBody ?: "Submission failed."

                        // 서버에서 USER_4002 코드를 반환한 경우 처리
                        if (response.code() == 400 && errorBody?.contains("USER_4002") == true) {
                            _errorMessage.value = "설문 조사에 이미 참여하셨습니다."
                            Log.e("SettingViewModel", "Survey already completed: $errorMessage")
                        } else {
                            _errorMessage.value = errorMessage
                            Log.e("SettingViewModel", "Failed to submit survey: ${response.message()}")
                        }
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

    // 즐겨찾기 리스트 가져오기 함수
    fun fetchFavoriteStores() {
        _isLoading.value = true
        val accessToken = LocalDataSource.getAccessToken()
        if (accessToken == null) {
            _isLoading.value = false
            _errorMessage.value = "Access token is null."
            return
        }

        settingService.getFavoriteStores("Bearer $accessToken")
            .enqueue(object : Callback<FavoriteResponse> {
                override fun onResponse(call: Call<FavoriteResponse>, response: Response<FavoriteResponse>) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        _favoriteStores.value = response.body()?.result?.favoriteStoreDetailResDtoList
                        Log.d("SettingViewModel", "Favorite stores fetched successfully.")
                    } else {
                        _errorMessage.value = response.errorBody()?.string() ?: "Failed to fetch favorite stores."
                        Log.e("SettingViewModel", "Failed to fetch favorite stores: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<FavoriteResponse>, t: Throwable) {
                    _isLoading.value = false
                    _errorMessage.value = "Network error: ${t.message}"
                    Log.e("SettingViewModel", "Network error", t)
                }
            })
    }

    fun fetchPointCoupons() {
        _isLoading.value = true
        Log.d("SettingViewModel", "fetchPointCoupons called") // 호출 확인 로그
        val accessToken = LocalDataSource.getAccessToken()
        if (accessToken == null) {
            _isLoading.value = false
            _errorMessage.value = "Access token is null."
            return
        }

        settingService.getPointCoupons("Bearer $accessToken")
            .enqueue(object : Callback<PointCouponResponse> {
                override fun onResponse(call: Call<PointCouponResponse>, response: Response<PointCouponResponse>) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        val pointCouponList = response.body()?.result?.pointCouponResDtoList
                        _pointCoupons.value = pointCouponList

                        // 로그 추가: 성공적으로 데이터를 가져왔을 때
                        if (pointCouponList.isNullOrEmpty()) {
                            Log.d("SettingViewModel", "Coupon list is empty.")
                        } else {
                            Log.d("SettingViewModel", "Coupons fetched successfully: $pointCouponList")
                        }
                    } else {
                        _errorMessage.value = response.body()?.message ?: "Failed to fetch coupons."
                    }
                }

                override fun onFailure(call: Call<PointCouponResponse>, t: Throwable) {
                    _isLoading.value = false
                    _errorMessage.value = "Network error: ${t.message}"
                }
            })
    }
    fun fetchUserProfile() {
        _isLoading.value = true
        val accessToken = LocalDataSource.getAccessToken()
        if (accessToken == null) {
            _isLoading.value = false
            _errorMessage.value = "Access token is null."
            Log.e("SettingViewModel", "Access token is null.")
            return
        }

        settingService.getUserProfile("Bearer $accessToken")
            .enqueue(object : Callback<UserProfileResponse> {
                override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        _userProfile.value = response.body()?.result
                        Log.d("SettingViewModel", "User profile fetched: ${response.body()?.result}")
                    } else {
                        _errorMessage.value = response.body()?.message ?: "Failed to fetch user profile."
                        Log.e("SettingViewModel", "API response error: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                    _isLoading.value = false
                    _errorMessage.value = "Network error: ${t.message}"
                    Log.e("SettingViewModel", "Network error occurred", t)
                }
            })
    }
}