package com.example.calmcafeapp.api

import com.example.calmcafeapp.data.MenuRegisterResponse
import com.example.calmcafeapp.data.PointDiscountResponse
import com.example.calmcafeapp.data.PointMenuRemovalResponse
import com.example.calmcafeapp.data.PromotionDeleteResponse
import com.example.calmcafeapp.data.PromotionRegisterResponse
import com.example.calmcafeapp.data.PromotionResponse
import com.example.calmcafeapp.data.RegisterMenuResponse
import com.example.calmcafeapp.data.StoreDetailResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface M_SettingService {

    @GET("/store/menus")
    fun getMenu(
        @Header("Authorization") accessToken: String,
    ): Call<StoreDetailResponse>

    @GET("/promotion/store")
    fun getPromotion(
        @Header("Authorization") accessToken: String,
    ): Call<PromotionResponse>

    @GET("/menu/non-discounted/")
    fun getPointNonMenu(
        @Header("Authorization") accessToken: String,
    ): Call<PointDiscountResponse>

    @GET("/menu/discounted/")
    fun getPointMenu(
        @Header("Authorization") accessToken: String,
    ): Call<PointDiscountResponse>

    @POST("/promotion")
    @FormUrlEncoded
    fun registerPromotion(
        @Header("Authorization") accessToken: String,
        @Field("discount") discount: Int,
        @Field("startTime") startTime: String,
        @Field("endTime") endTime: String,
        @Field("promotionTypeValue") promotionTypeValue: Int
    ): Call<PromotionRegisterResponse>

    @DELETE("/promotion/{promotionId}")
    fun deletePromotion(
        @Header("Authorization") accessToken: String,
        @Path("promotionId") promotionId: Long
    ): Call<PromotionDeleteResponse>

    @POST("/menu/menu-image/register/")
    @Multipart
    fun registerMenu(
        @Header("Authorization") accessToken: String,
        @Part("name") name: String,
        @Part("price") price: Int,
        @Part menuImage: MultipartBody.Part?
    ): Call<MenuRegisterResponse>

    @PATCH("/menu/{menu-id}/menu-image/update")
    @Multipart
    fun updateMenu(
        @Header("Authorization") accessToken: String,
        @Path("menu-id") menuId: Long,
        @Part("price") price: Int?,
        @Part menuImage: MultipartBody.Part?
    ): Call<MenuRegisterResponse>

    @PATCH("/store/remove/point-details")
    fun removePointMenu(
        @Header("Authorization") accessToken: String,
        @Query("menuId") menuId: Long,
    ): Call<PointMenuRemovalResponse>

    @DELETE("/menu/{menu-id}/delete")
    fun deleteMenu(
        @Header("Authorization") accessToken: String,
        @Path("menu-id") menuId: Long
    ): Call<MenuRegisterResponse>

    @PATCH("/store/create/point-details")
    fun registerMenuToPointStore(
        @Header("Authorization") accessToken: String,
        @Query("menuId") menuId: Long,
        @Query("pointDiscount") pointDiscount: Int,
        @Query("pointPrice") pointPrice: Int
    ): Call<RegisterMenuResponse>


}