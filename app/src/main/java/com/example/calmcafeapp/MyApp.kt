package com.example.calmcafeapp

import android.app.Application
import com.example.calmcafeapp.apiManager.ApiManager
import com.kakao.sdk.common.KakaoSdk

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        // Kakao SDK api 호출해서 초기화
        KakaoSdk.init(this, BuildConfig.NATIVE_APP_KEY)
        ApiManager.init(this)
    }
}