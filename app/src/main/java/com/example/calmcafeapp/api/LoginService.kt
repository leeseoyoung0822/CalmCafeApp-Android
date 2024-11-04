package com.example.calmcafeapp.api


import com.example.calmcafeapp.data.KakaoLogin
import com.example.calmcafeapp.data.Reissue
import com.example.calmcafeapp.data.UserInfo
import com.example.calmcafeapp.data.getNickname
import com.example.calmcafeapp.data.getNicknameRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginService {
    @POST("/token/generate")
    fun login(@Body requestbody: UserInfo): Call<KakaoLogin>
    @POST("/users/reissue")
    fun reissue(@Header("Authorization") refreshToken:String):Call<Reissue>
    @POST("/users/nickname")
    fun getNickname(@Header("Authorization") accessToken:String, @Body requestbody: getNicknameRequest): Call<getNickname>
}