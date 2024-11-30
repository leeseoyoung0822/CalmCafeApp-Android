package com.example.calmcafeapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class StoreDetailResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: StoreDetailResult
)

data class StoreDetailResult(
    val storeId: Int,
    val menus: List<Menu>
)
@Parcelize
data class Menu(
    val id: Int,
    val name: String,
    val price: Int,
    val image: String
): Parcelable

