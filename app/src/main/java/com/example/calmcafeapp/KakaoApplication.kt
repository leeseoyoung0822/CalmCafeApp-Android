package com.example.calmcafeapp

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class KakaoApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        // Kakao SDK api 호출해서 초기화
        KakaoSdk.init(this, BuildConfig.NATIVE_APP_KEY)
    }
}