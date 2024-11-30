package com.example.calmcafeapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calmcafeapp.apiManager.ApiManager
import com.example.calmcafeapp.data.Menu
import com.example.calmcafeapp.data.MenuRegisterResponse
import com.example.calmcafeapp.data.PointDiscount
import com.example.calmcafeapp.data.PointDiscountResponse
import com.example.calmcafeapp.data.Promotion
import com.example.calmcafeapp.data.PromotionDeleteResponse
import com.example.calmcafeapp.data.PromotionRegisterResponse
import com.example.calmcafeapp.data.PromotionResponse
import com.example.calmcafeapp.data.StoreDetailResponse
import com.example.calmcafeapp.login.LocalDataSource
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class M_SettingViewModel : ViewModel() {

    private val _promotionRegisterResult = MutableLiveData<Boolean>()
    val promotionRegisterResult: LiveData<Boolean> get() = _promotionRegisterResult

    private val _deleteMenuResult = MutableLiveData<Boolean>()
    val deleteMenuResult: LiveData<Boolean> get() = _deleteMenuResult

    private val _menuListLiveData = MutableLiveData<List<Menu>>()
    val menuListLiveData: LiveData<List<Menu>> get() = _menuListLiveData

    private val _promotionListLiveData = MutableLiveData<List<Promotion>>()
    val promotionListLiveData: LiveData<List<Promotion>> get() = _promotionListLiveData

    private val _pointDiscountListLiveData = MutableLiveData<List<PointDiscount>>()
    val pointDiscountListLiveData: LiveData<List<PointDiscount>> get() = _pointDiscountListLiveData

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // 메뉴 가져오기
    fun fetchMenus() {
        _isLoading.value = true

        val call: Call<StoreDetailResponse> = ApiManager.m_SettingService.getMenu(
            accessToken = "Bearer " + LocalDataSource.getAccessToken()!!
        )

        call.enqueue(object : Callback<StoreDetailResponse> {
            override fun onResponse(call: Call<StoreDetailResponse>, response: Response<StoreDetailResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.isSuccess) {
                        _menuListLiveData.postValue(body.result.menus)
                    } else {
                        _errorMessage.postValue(body?.message ?: "메뉴 데이터를 불러오지 못했습니다.")
                    }
                } else {
                    _errorMessage.postValue("메뉴 데이터를 불러오지 못했습니다. (서버 에러)")
                }
            }

            override fun onFailure(call: Call<StoreDetailResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.postValue("메뉴 데이터를 불러오지 못했습니다. (네트워크 에러)")
            }
        })
    }

    fun fetchPromotions() {
        _isLoading.value = true

        val call: Call<PromotionResponse> = ApiManager.m_SettingService.getPromotion(
            accessToken = "Bearer " + LocalDataSource.getAccessToken()!!
        )

        call.enqueue(object : Callback<PromotionResponse> {
            override fun onResponse(call: Call<PromotionResponse>, response: Response<PromotionResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        // 로그 추가: 응답 확인
                        Log.d("fetchPromotions", "Response: $body")

                        if (body.isSuccess) {
                            _promotionListLiveData.postValue(body.result)
                        } else {
                            _errorMessage.postValue(body.message ?: "프로모션 데이터를 불러오지 못했습니다.")
                        }
                    } else {
                        _errorMessage.postValue("응답이 null입니다.")
                    }
                } else {
                    _errorMessage.postValue("프로모션 데이터를 불러오지 못했습니다. (서버 에러: ${response.code()})")
                }
            }

            override fun onFailure(call: Call<PromotionResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("fetchPromotions", "Network error: ${t.message}")
                _errorMessage.postValue("프로모션 데이터를 불러오지 못했습니다. (네트워크 에러)")
            }
        })
    }


    // 포인트 할인이 적용된 메뉴 가져오기
    fun fetchPointDiscountMenus() {
        _isLoading.value = true

        val call: Call<PointDiscountResponse> = ApiManager.m_SettingService.getPointMenu(
            accessToken = "Bearer " + LocalDataSource.getAccessToken()!!
        )

        call.enqueue(object : Callback<PointDiscountResponse> {
            override fun onResponse(call: Call<PointDiscountResponse>, response: Response<PointDiscountResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.isSuccess) {
                        _pointDiscountListLiveData.postValue(body.result)
                    } else {
                        _errorMessage.postValue(body?.message ?: "할인 메뉴 데이터를 불러오지 못했습니다.")
                    }
                } else {
                    _errorMessage.postValue("할인 메뉴 데이터를 불러오지 못했습니다. (서버 에러)")
                }
            }

            override fun onFailure(call: Call<PointDiscountResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.postValue("할인 메뉴 데이터를 불러오지 못했습니다. (네트워크 에러)")
            }
        })
    }

    fun registerPromotion(discount: Int, startTime: String, endTime: String, promotionTypeValue: Int) {
        val call = ApiManager.m_SettingService.registerPromotion(
            accessToken = "Bearer " + LocalDataSource.getAccessToken()!!,
            discount = discount,
            startTime = startTime,
            endTime = endTime,
            promotionTypeValue = promotionTypeValue
        )

        call.enqueue(object : Callback<PromotionRegisterResponse> {
            override fun onResponse(
                call: Call<PromotionRegisterResponse>,
                response: Response<PromotionRegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.isSuccess) {
                        _promotionRegisterResult.postValue(true)
                    } else {
                        _promotionRegisterResult.postValue(false)
                    }
                } else {
                    _promotionRegisterResult.postValue(false)
                }
            }

            override fun onFailure(call: Call<PromotionRegisterResponse>, t: Throwable) {
                _promotionRegisterResult.postValue(false)
            }
        })
    }

    fun registerMenu(name: String, price: Int, menuImage: MultipartBody.Part?
    ) {
        val call = ApiManager.m_SettingService.registerMenu(
            accessToken = "Bearer " + LocalDataSource.getAccessToken()!!,
            name = name,
            price = price,
            menuImage = menuImage
        )

        call.enqueue(object : Callback<MenuRegisterResponse> {
            override fun onResponse(
                call: Call<MenuRegisterResponse>,
                response: Response<MenuRegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.isSuccess) {
                        // Menu registration successful
                        Log.d("MenuRegister", "Menu registered successfully: ${body.result}")
                    } else {
                        Log.e("MenuRegister", "Failed to register menu: ${body?.message}")
                    }
                } else {
                    Log.e("MenuRegister", "Server error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<MenuRegisterResponse>, t: Throwable) {
                Log.e("MenuRegister", "Network error: ${t.message}")
            }
        })
    }


    private val _updateMenuResult = MutableLiveData<Boolean>()
    val updateMenuResult: LiveData<Boolean> get() = _updateMenuResult

    fun updateMenu(menuId: Long, price: Int?, menuImage: MultipartBody.Part?) {
        val call = ApiManager.m_SettingService.updateMenu(
            accessToken = "Bearer " + LocalDataSource.getAccessToken()!!,
            menuId = menuId,
            price = price,
            menuImage = menuImage
        )

        call.enqueue(object : Callback<MenuRegisterResponse> {
            override fun onResponse(
                call: Call<MenuRegisterResponse>,
                response: Response<MenuRegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.isSuccess) {
                        Log.d("MenuUpdate", "Menu updated successfully: ${body.result}")
                        _updateMenuResult.postValue(true) // 성공 알림
                    } else {
                        Log.e("MenuUpdate", "Failed to update menu: ${body?.message}")
                        _updateMenuResult.postValue(false) // 실패 알림
                    }
                } else {
                    Log.e("MenuUpdate", "Server error: ${response.errorBody()?.string()}")
                    _updateMenuResult.postValue(false) // 실패 알림
                }
            }

            override fun onFailure(call: Call<MenuRegisterResponse>, t: Throwable) {
                Log.e("MenuUpdate", "Network error: ${t.message}")
                _updateMenuResult.postValue(false) // 실패 알림
            }
        })
    }


    fun deleteMenu(menuId: Long) {
        val call = ApiManager.m_SettingService.deleteMenu(
            accessToken = "Bearer " + LocalDataSource.getAccessToken()!!,
            menuId = menuId
        )

        call.enqueue(object : Callback<MenuRegisterResponse> {
            override fun onResponse(
                call: Call<MenuRegisterResponse>,
                response: Response<MenuRegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.isSuccess) {
                        Log.d("MenuDelete", "Menu deleted successfully: ${body.result}")
                        _deleteMenuResult.postValue(true)
                    } else {
                        Log.e("MenuDelete", "Failed to delete menu: ${body?.message}")
                        _deleteMenuResult.postValue(false)
                    }
                } else {
                    Log.e("MenuDelete", "Server error: ${response.errorBody()?.string()}")
                    _deleteMenuResult.postValue(false)
                }
            }

            override fun onFailure(call: Call<MenuRegisterResponse>, t: Throwable) {
                Log.e("MenuDelete", "Network error: ${t.message}")
                _deleteMenuResult.postValue(false)
            }
        })
    }

    fun deletePromotion(promotionId: Long) {
        val call = ApiManager.m_SettingService.deletePromotion(
            accessToken = "Bearer " + LocalDataSource.getAccessToken()!!,
            promotionId = promotionId
        )

        call.enqueue(object : Callback<PromotionDeleteResponse> {
            override fun onResponse(
                call: Call<PromotionDeleteResponse>,
                response: Response<PromotionDeleteResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.isSuccess) {
                        // Promotion deletion successful
                        Log.d("PromotionDelete", "Promotion deleted successfully: ${body.result}")
                    } else {
                        Log.e("PromotionDelete", "Failed to delete promotion: ${body?.message}")
                    }
                } else {
                    Log.e("PromotionDelete", "Server error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<PromotionDeleteResponse>, t: Throwable) {
                Log.e("PromotionDelete", "Network error: ${t.message}")
            }
        })
    }





}
