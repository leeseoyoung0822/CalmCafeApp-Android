package com.example.calmcafeapp.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.calmcafeapp.api.RankingService
import com.example.calmcafeapp.apiManager.ApiManager
import com.example.calmcafeapp.data.CafeDetailResult
import com.example.calmcafeapp.data.FavoriteResponse
import com.example.calmcafeapp.data.RankingResponse
import com.example.calmcafeapp.data.RecommendStoreResDto
import com.example.calmcafeapp.data.StoreRanking
import com.example.calmcafeapp.login.LocalDataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RankViewModel(application: Application) : AndroidViewModel(application) {

    private val _storeList = MutableLiveData<List<StoreRanking>>()
    val storeList: LiveData<List<StoreRanking>> = _storeList

    private val _cafeDetail = MutableLiveData<CafeDetailResult>()
    val cafeDetail: LiveData<CafeDetailResult> = _cafeDetail

    private val _favoriteStoreId = MutableLiveData<Int?>()
    val favoriteStoreId: LiveData<Int?> = _favoriteStoreId

    private val _recommendStoreList = MutableLiveData<List<RecommendStoreResDto>>()
    val recommendStoreList: LiveData<List<RecommendStoreResDto>> get() = _recommendStoreList

    private val _storeCongestionLevel = MutableLiveData<String>()
    val storeCongestionLevel: LiveData<String> = _storeCongestionLevel

    private val _userCongestionLevel = MutableLiveData<String>()
    val userCongestionLevel: LiveData<String> = _userCongestionLevel

    private val apiService: RankingService = ApiManager.rankingService

    // 에러 메시지를 담을 LiveData
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // 로딩 상태를 담을 LiveData
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // 실시간 방문자 수 데이터를 요청하는 함수
    fun fetchRealTimeRanking(location: String) {
        _isLoading.value = true  // 로딩 상태 시작
        val accessToken = LocalDataSource.getAccessToken()
        Log.d("RankViewModel", "Access Token: $accessToken")
        if (accessToken == null) {
            _isLoading.value = false
            _errorMessage.value = "Access token is null."
            Log.e("RankViewModel", "Access token is null.")
            return
        }

        val call: Call<RankingResponse> = apiService.getRealTimeRanking(
            "Bearer $accessToken",
            location = location
        )

        call.enqueue(object : Callback<RankingResponse> {
            override fun onResponse(call: Call<RankingResponse>, response: Response<RankingResponse>) {
                _isLoading.value = false  // 로딩 상태 종료
                if (response.isSuccessful && response.body() != null) {
                    val rankingResponse = response.body()
                    Log.d("RankViewModel", "Data fetched: $rankingResponse")

                    val storeList = rankingResponse?.result?.storeRankingResDtoList
                    _storeList.value = storeList ?: emptyList()

                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    _errorMessage.value = errorMessage
                    Log.e(
                        "RankViewModel",
                        "Failed to fetch data. Status code: ${response.code()}, Error: $errorMessage"
                    )
                }
            }

            override fun onFailure(call: Call<RankingResponse>, t: Throwable) {
                _isLoading.value = false  // 로딩 상태 종료
                _errorMessage.value = "네트워크 오류가 발생했습니다: ${t.message}"
                Log.e("RankViewModel", "Network error", t)
            }
        })
    }

    // 누적 방문자 수 데이터를 요청하는 함수
    fun fetchTotalRanking(location: String) {
        _isLoading.value = true  // 로딩 상태 시작
        val accessToken = LocalDataSource.getAccessToken()
        Log.d("RankViewModel", "Access Token: $accessToken")
        if (accessToken == null) {
            _isLoading.value = false
            _errorMessage.value = "Access token is null."
            Log.e("RankViewModel", "Access token is null.")
            return
        }

        val call: Call<RankingResponse> = apiService.getTotalRanking(
            "Bearer $accessToken",
            location = location
        )

        call.enqueue(object : Callback<RankingResponse> {
            override fun onResponse(call: Call<RankingResponse>, response: Response<RankingResponse>) {
                _isLoading.value = false  // 로딩 상태 종료
                if (response.isSuccessful && response.body() != null) {
                    val rankingResponse = response.body()
                    Log.d("RankViewModel", "Data fetched: $rankingResponse")

                    val storeList = rankingResponse?.result?.storeRankingResDtoList
                    _storeList.value = storeList ?: emptyList()

                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    _errorMessage.value = errorMessage
                    Log.e(
                        "RankViewModel",
                        "Failed to fetch data. Status code: ${response.code()}, Error: $errorMessage"
                    )
                }
            }

            override fun onFailure(call: Call<RankingResponse>, t: Throwable) {
                _isLoading.value = false  // 로딩 상태 종료
                _errorMessage.value = "네트워크 오류가 발생했습니다: ${t.message}"
                Log.e("RankViewModel", "Network error", t)
            }
        })
    }

    // 즐겨찾기 랭킹 데이터를 요청하는 함수
    fun fetchFavoriteRanking(location: String) {
        _isLoading.value = true  // 로딩 상태 시작
        val accessToken = LocalDataSource.getAccessToken()
        Log.d("RankViewModel", "Access Token: $accessToken")
        if (accessToken == null) {
            _isLoading.value = false
            _errorMessage.value = "Access token is null."
            Log.e("RankViewModel", "Access token is null.")
            return
        }

        val call: Call<RankingResponse> = apiService.getFavoriteRanking(
            "Bearer $accessToken",
            location = location
        )

        call.enqueue(object : Callback<RankingResponse> {
            override fun onResponse(call: Call<RankingResponse>, response: Response<RankingResponse>) {
                _isLoading.value = false  // 로딩 상태 종료
                if (response.isSuccessful && response.body() != null) {
                    val rankingResponse = response.body()
                    Log.d("RankViewModel", "Data fetched: $rankingResponse")

                    val storeList = rankingResponse?.result?.storeRankingResDtoList
                    _storeList.value = storeList ?: emptyList()

                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    _errorMessage.value = errorMessage
                    Log.e(
                        "RankViewModel",
                        "Failed to fetch data. Status code: ${response.code()}, Error: $errorMessage"
                    )
                }
            }

            override fun onFailure(call: Call<RankingResponse>, t: Throwable) {
                _isLoading.value = false  // 로딩 상태 종료
                _errorMessage.value = "네트워크 오류가 발생했습니다: ${t.message}"
                Log.e("RankViewModel", "Network error", t)
            }
        })
    }

    fun addFavorite(storeId: Int) {
        val accessToken = LocalDataSource.getAccessToken() ?: return
        _isLoading.value = true

        val call = apiService.addFavorite("Bearer $accessToken", storeId)
        call.enqueue(object : Callback<FavoriteResponse> {
            override fun onResponse(call: Call<FavoriteResponse>, response: Response<FavoriteResponse>) {
                _isLoading.value = false
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    Log.d("RankViewModel", "즐겨찾기 추가 성공: ${response.body()?.message}")
                    _favoriteStoreId.value = storeId
                } else {
                    Log.e("RankViewModel", "즐겨찾기 추가 실패: ${response.errorBody()?.string()}")
                    _errorMessage.value = response.body()?.message ?: "즐겨찾기 추가 실패"
                }
            }

            override fun onFailure(call: Call<FavoriteResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("RankViewModel", "네트워크 오류: ${t.message}")
                _errorMessage.value = "네트워크 오류가 발생했습니다."
            }
        })
    }

    fun removeFavorite(storeId: Int) {
        val accessToken = LocalDataSource.getAccessToken() ?: return
        _isLoading.value = true

        val call = apiService.removeFavorite("Bearer $accessToken", storeId)
        call.enqueue(object : Callback<FavoriteResponse> {
            override fun onResponse(call: Call<FavoriteResponse>, response: Response<FavoriteResponse>) {
                _isLoading.value = false
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    Log.d("RankViewModel", "즐겨찾기 삭제 성공: ${response.body()?.message}")
                    _favoriteStoreId.value = null
                } else {
                    Log.e("RankViewModel", "즐겨찾기 삭제 실패: ${response.errorBody()?.string()}")
                    _errorMessage.value = response.body()?.message ?: "즐겨찾기 삭제 실패"
                }
            }

            override fun onFailure(call: Call<FavoriteResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("RankViewModel", "네트워크 오류: ${t.message}")
                _errorMessage.value = "네트워크 오류가 발생했습니다."
            }
        })
    }

    // SharedPreferences에서 Access Token을 가져오는 함수 (사용하지 않음)
    private fun getAccessToken(): String? {
        val sharedPreferences = getApplication<Application>().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("ACCESS_TOKEN", null)
        Log.d("RankViewModel", "Access Token in ViewModel: $accessToken")
        return accessToken
    }
}
