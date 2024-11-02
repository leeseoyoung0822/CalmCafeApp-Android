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
import com.example.calmcafeapp.data.FavoriteResponse
import com.example.calmcafeapp.data.MenuDetail
import com.example.calmcafeapp.data.PointMenuDetail
import com.example.calmcafeapp.data.RankingResponse
import com.example.calmcafeapp.data.RecommendStore
import com.example.calmcafeapp.data.StoreRanking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RankViewModel(application: Application) : AndroidViewModel(application) {

    private val _storeList = MutableLiveData<List<StoreRanking>>()
    val storeList: LiveData<List<StoreRanking>> = _storeList

    private val _cafeDetail = MutableLiveData<CafeDetail>()
    val cafeDetail: LiveData<CafeDetail> = _cafeDetail

    private val _favoriteStoreId = MutableLiveData<Int?>()
    val favoriteStoreId: LiveData<Int?> = _favoriteStoreId

    private val _recommendStoreList = MutableLiveData<List<RecommendStore>>()
    val recommendStoreList: LiveData<List<RecommendStore>> get() = _recommendStoreList

    private val _storeCongestionLevel = MutableLiveData<String>()
    val storeCongestionLevel: LiveData<String> = _storeCongestionLevel

    private val _userCongestionLevel = MutableLiveData<String>()
    val userCongestionLevel: LiveData<String> = _userCongestionLevel




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

    // 즐겨찾기 랭킹 데이터를 요청하는 함수
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
                    val cafeDetail = response.body()?.result
                    _cafeDetail.value = cafeDetail
                    _recommendStoreList.value = cafeDetail?.recommendStoreResDtoList
                    _storeCongestionLevel.value = cafeDetail?.storeCongestionLevel
                    _userCongestionLevel.value = cafeDetail?.userCongestionLevel
                    Log.d("RankViewModel", "Cafe detail fetched: ${response.body()?.result}")
                } else {
                    val errorMessage = response.errorBody()?.string()
                    Log.e("RankViewModel", "Failed to fetch cafe detail. Status code: ${response.code()}, Error: $errorMessage")
                }
            }

            override fun onFailure(call: Call<CafeDetailResponse>, t: Throwable) {
                Log.e("RankViewModel", "Network error: ${t.message}")
                _recommendStoreList.value = emptyList()
                _storeCongestionLevel.value = null // 실패 시 null로 설정
                _userCongestionLevel.value = null // 실패 시 null로 설정

            }
        })
    }

    // 즐겨찾기 추가 함수
    fun addFavorite(storeId: Int) {
        apiService.addFavorite(storeId).enqueue(object : Callback<FavoriteResponse> {
            override fun onResponse(call: Call<FavoriteResponse>, response: Response<FavoriteResponse>) {
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    Log.d("RankViewModel", "즐겨찾기 성공: ${response.body()?.message}")
                    _favoriteStoreId.value = storeId  // 즐겨찾기 상태 업데이트
                } else {
                    Log.e("RankViewModel", "즐겨찾기 실패 - 코드: ${response.code()}, 오류: ${response.errorBody()?.string()}")
                }
            }
            override fun onFailure(call: Call<FavoriteResponse>, t: Throwable) {
                Log.e("RankViewModel", "네트워크 오류: ${t.message}")
            }
        })
    }
    // 즐겨찾기 취소 함수
    fun removeFavorite(storeId: Int) {
        apiService.removeFavorite(storeId).enqueue(object : Callback<FavoriteResponse> {
            override fun onResponse(call: Call<FavoriteResponse>, response: Response<FavoriteResponse>) {
                if (response.isSuccessful) {
                    _favoriteStoreId.value = storeId
                    Log.d("RankViewModel", "즐겨찾기 취소: $storeId")
                } else {
                    Log.e("RankViewModel", "즐겨찾기 취소 실패: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<FavoriteResponse>, t: Throwable) {
                Log.e("RankViewModel", "Network error: ${t.message}")
            }
        })
    }

    // 즐겨찾기 ID 초기화 함수
    fun resetFavoriteStoreId() {
        _favoriteStoreId.value = null
    }

    // SharedPreferences에서 Access Token을 가져오는 함수
    private fun getAccessToken(): String? {
        val sharedPreferences = getApplication<Application>().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("ACCESS_TOKEN", null)
        Log.d("RankViewModel", "Access Token in ViewModel: $accessToken") // 추가된 로그
        return accessToken
        //return sharedPreferences.getString("ACCESS_TOKEN", null)
    }
}