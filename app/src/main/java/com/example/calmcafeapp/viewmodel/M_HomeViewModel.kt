package com.example.calmcafeapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.calmcafeapp.api.M_HomeService
import com.example.calmcafeapp.apiManager.ApiManager
import com.example.calmcafeapp.data.M_CafeDetailResponse
import com.example.calmcafeapp.data.StoreResponse
import com.example.calmcafeapp.login.LocalDataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class M_HomeViewModel(application: Application) : AndroidViewModel(application){

    private val mHomeService: M_HomeService = ApiManager.m_HomeService

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _isUpdateSuccess = MutableLiveData<Boolean>()
    val isUpdateSuccess: LiveData<Boolean> get() = _isUpdateSuccess

    private val _storeResponse = MutableLiveData<StoreResponse>()
    val storeResponse: LiveData<StoreResponse> get() = _storeResponse

    private val _cafeDetail = MutableLiveData<M_CafeDetailResponse>()
    val cafeDetail: LiveData<M_CafeDetailResponse> get() = _cafeDetail


    // 운영 시간 수정
    fun modifyStoreHours(openingTime: String, closingTime: String) {
        _isLoading.value = true
        val accessToken = LocalDataSource.getAccessToken()
        if (accessToken == null) {
            _isLoading.value = false
            _errorMessage.value = "Access token is null."
            return
        }
        Log.d("API Request", "modifyStoreHours called with openingTime: $openingTime, closingTime: $closingTime")



        mHomeService.modifyHours("Bearer $accessToken",openingTime, closingTime)
            .enqueue(object : Callback<StoreResponse> {
                override fun onResponse(call: Call<StoreResponse>, response: Response<StoreResponse>) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        _isUpdateSuccess.value = true
                        Log.d("M_HomeViewModel", "Store hours updated successfully.")
                    } else {
                        _isUpdateSuccess.value = false
                        _errorMessage.value = response.body()?.message ?: "Failed to update store hours."
                        Log.e("M_HomeViewModel", "Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<StoreResponse>, t: Throwable) {
                    _isLoading.value = false
                    _isUpdateSuccess.value = false
                    _errorMessage.value = "Network error: ${t.message}"
                    Log.e("M_HomeViewModel", "Network error", t)
                }
            })
    }

    // 라스트 오더 시간 수정
    fun modifyLastOrderTime(lastOrderTime: String) {
        _isLoading.value = true
        val accessToken = LocalDataSource.getAccessToken()
        if (accessToken == null) {
            _isLoading.value = false
            _errorMessage.value = "Access token is null."
            return
        }

        mHomeService.modifyLastOrderTime("Bearer $accessToken",lastOrderTime)
            .enqueue(object : Callback<StoreResponse> {
                override fun onResponse(call: Call<StoreResponse>, response: Response<StoreResponse>) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        _isUpdateSuccess.value = true
                        Log.d("M_HomeViewModel", "Last order time updated successfully.")
                    } else {
                        _isUpdateSuccess.value = false
                        _errorMessage.value = response.body()?.message ?: "Failed to update last order time."
                        Log.e("M_HomeViewModel", "Error: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<StoreResponse>, t: Throwable) {
                    _isLoading.value = false
                    _isUpdateSuccess.value = false
                    _errorMessage.value = "Network error: ${t.message}"
                    Log.e("M_HomeViewModel", "Network error", t)
                }
            })
    }

    // 최대 수용 인원 수정
    fun modifyMaxCapacity(maxCapacity: Int) {
        _isLoading.value = true
        val accessToken = LocalDataSource.getAccessToken()
        if (accessToken == null) {
            _isLoading.value = false
            _errorMessage.value = "Access token is null."
            return
        }

        // 요청 로그 추가
        Log.d("M_HomeViewModel", "Requesting modifyMaxCapacity with maxCapacity: $maxCapacity and accessToken: $accessToken")


        mHomeService.modifyMaxCapacity("Bearer $accessToken", maxCapacity)
            .enqueue(object : Callback<StoreResponse> {
                override fun onResponse(call: Call<StoreResponse>, response: Response<StoreResponse>) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        _isUpdateSuccess.value = true
                        Log.d("M_HomeViewModel", "Max capacity updated successfully.")
                    } else {
                        _isUpdateSuccess.value = false
                        _errorMessage.value = response.errorBody()?.string() ?: "Failed to update max capacity."
                        Log.e("M_HomeViewModel", "Failed to update max capacity: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<StoreResponse>, t: Throwable) {
                    _isLoading.value = false
                    _isUpdateSuccess.value = false
                    _errorMessage.value = "Network error: ${t.message}"
                    Log.e("M_HomeViewModel", "Network error", t)
                }
            })
    }
    fun fetchCafeDetail() {
        _isLoading.value = true
        val accessToken = LocalDataSource.getAccessToken()

        if (accessToken.isNullOrEmpty()) {
            _isLoading.value = false
            _errorMessage.value = "Access token is null."
            Log.e("M_HomeViewModel", "Access token is null or empty.")
            return
        }

        Log.d("M_HomeViewModel", "AccessToken: $accessToken")
        Log.d("M_HomeViewModel", "Requesting API: /store/detail/cafe")

        mHomeService.getCafeDetail("Bearer $accessToken")
            .enqueue(object : Callback<M_CafeDetailResponse> {
                override fun onResponse(
                    call: Call<M_CafeDetailResponse>,
                    response: Response<M_CafeDetailResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body() != null) {
                        _cafeDetail.value = response.body()
                        Log.d("M_HomeViewModel", "Response Success: ${response.body()}")
                    } else {
                        val errorBody = response.errorBody()?.string()
                        _errorMessage.value = errorBody ?: "Failed to fetch cafe details."
                        Log.e("M_HomeViewModel", "Response Error: $errorBody")
                    }
                }

                override fun onFailure(call: Call<M_CafeDetailResponse>, t: Throwable) {
                    _isLoading.value = false
                    _errorMessage.value = "Network error: ${t.message}"
                    Log.e("M_HomeViewModel", "Network error occurred", t)
                }
            })
    }

}

