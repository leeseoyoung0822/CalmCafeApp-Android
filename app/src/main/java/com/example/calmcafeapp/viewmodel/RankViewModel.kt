package com.example.calmcafeapp.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.calmcafeapp.api.RankingService
import com.example.calmcafeapp.apiManager.ApiManager
import com.example.calmcafeapp.data.CafeDetail
import com.example.calmcafeapp.data.CafeDetailResponse
import com.example.calmcafeapp.data.RankingResponse
import com.example.calmcafeapp.data.StoreRanking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RankViewModel(application: Application) : AndroidViewModel(application) {

    private val _storeList = MutableLiveData<List<StoreRanking>>()
    val storeList: LiveData<List<StoreRanking>> = _storeList

    private val _cafeDetail = MutableLiveData<CafeDetail>()
    val cafeDetail: LiveData<CafeDetail> = _cafeDetail

    private val apiService: RankingService = ApiManager.rankingService



    // 실시간 방문자 수 데이터를 요청하는 함수
    fun fetchRealTimeRanking(location: String) {
        Log.d("RankViewModel", "Fetching real-time ranking for location: $location") // 시작 로그

        apiService.getRealTimeRanking(location).enqueue(object : Callback<RankingResponse> {
            override fun onResponse(call: Call<RankingResponse>, response: Response<RankingResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    _storeList.value = response.body()?.result?.storeRankingResDtoList
                    Log.d("RankViewModel", "Data fetched: ${response.body()}")
                } else {
                    val errorMessage = response.errorBody()?.string()
                    val accessToken = getAccessToken()
                    Log.e("RankViewModel",
                        "Failed to fetch data. Status code: ${response.code()}, Error: $errorMessage, Access Token: $accessToken")
                }
            }

            override fun onFailure(call: Call<RankingResponse>, t: Throwable) {
                Log.e("RankViewModel", "Network error: ${t.message}")
                // 실패 시 로깅 또는 에러 처리
            }
        })
    }

    // 누적 방문자 수 데이터를 요청하는 함수
    fun fetchTotalRanking(location: String) {
        apiService.getTotalRanking(location).enqueue(object : Callback<RankingResponse> {
            override fun onResponse(call: Call<RankingResponse>, response: Response<RankingResponse>) {
                if (response.isSuccessful) {
                    _storeList.value = response.body()?.result?.storeRankingResDtoList
                }
            }

            override fun onFailure(call: Call<RankingResponse>, t: Throwable) {
                // 실패 시 로깅 또는 에러 처리
            }
        })
    }

    // 즐겨찾기 데이터를 요청하는 함수
    fun fetchFavoriteRanking(location: String) {
        apiService.getFavoriteRanking(location).enqueue(object : Callback<RankingResponse>{
            override fun onResponse(call: Call<RankingResponse>, response: Response<RankingResponse>) {
                if (response.isSuccessful) {
                    _storeList.value = response.body()?.result?.storeRankingResDtoList
                }
            }

            override fun onFailure(call: Call<RankingResponse>, t: Throwable) {
                // 실패 시 로깅 또는 에러 처리
            }
        })
    }

    // 카페 상세 정보를 가져오는 함수
    fun fetchCafeDetail(storeId: Int, userLatitude: Double, userLongitude: Double) {
        apiService.getCafeDetail(storeId, userLatitude, userLongitude).enqueue(object : Callback<CafeDetailResponse> {
            override fun onResponse(call: Call<CafeDetailResponse>, response: Response<CafeDetailResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    _cafeDetail.value = response.body()?.result
                    Log.d("RankViewModel", "Cafe detail fetched: ${response.body()?.result}")
                } else {
                    val errorMessage = response.errorBody()?.string()
                    Log.e("RankViewModel", "Failed to fetch cafe detail. Status code: ${response.code()}, Error: $errorMessage")
                }
            }

            override fun onFailure(call: Call<CafeDetailResponse>, t: Throwable) {
                Log.e("RankViewModel", "Network error: ${t.message}")
            }
        })
    }

    // SharedPreferences에서 Access Token을 가져오는 함수
    private fun getAccessToken(): String? {
        val sharedPreferences = getApplication<Application>().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("ACCESS_TOKEN", null)
        Log.d("RankViewModel", "Access Token in ViewModel: $accessToken") // 추가된 로그
        return accessToken
    }
}