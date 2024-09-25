package com.example.calmcafeapp

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class KakaoApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들

        // Kakao SDK api 호출해서 초기화
        KakaoSdk.init(this, "62ad8683c4c5d03977bde793e357956c")
    }
}