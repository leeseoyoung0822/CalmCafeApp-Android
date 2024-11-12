package com.example.calmcafeapp.data
import com.google.gson.annotations.SerializedName

data class SurveyRequest(
    @SerializedName("age")
    val age: Int?,
    @SerializedName("sex")
    val sex: String,
    @SerializedName("job")
    val job: String,
    @SerializedName("residence")
    val residence: String,
    @SerializedName("marriage")
    val marriage: String,
    @SerializedName("hobby")
    val hobby: String,
    @SerializedName("favoriteMenu")
    val favoriteMenu: String,
    @SerializedName("cafeUsingPurpose")
    val cafeUsingPurpose: String,
    @SerializedName("cafeChooseCause")
    val cafeChooseCause: String,
    @SerializedName("cafeVisitedFrequency")
    val cafeVisitedFrequency: String,
    @SerializedName("isUsingSNS")
    val isUsingSNS: String,
    @SerializedName("convenienceFacilityPrefer")
    val convenienceFacilityPrefer: String,


)
data class SurveyResponse(
    @SerializedName("isSuccess")
    val isSuccess: Boolean,
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("result")
    val result: Any?
)
