package com.example.calmcafeapp

import com.example.calmcafeapp.BuildConfig
import android.app.Application
import android.content.pm.PackageManager
import com.naver.maps.map.NaverMapSdk


class NaverMapApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val ai = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        val clientId = ai.metaData.getString("com.naver.maps.map.CLIENT_ID")
            ?: throw IllegalStateException("클라이언트 ID를 찾을 수 없습니다.")

        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(clientId)
    }
}