package com.example.calmcafeapp.apiManager

import com.google.gson.Gson
import com.google.gson.GsonBuilder

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
//
//object ApiManager {
//
//    private var BASE_URL = BuildConfig.AUTH_BASE_URL
//
//    val loggingInterceptor= HttpLoggingInterceptor().apply {
//        level= HttpLoggingInterceptor.Level.BODY
//    }
//
//    val gson : Gson = GsonBuilder().setLenient().create()
//
//    private val okHttpClient = OkHttpClient.Builder()
//        .addInterceptor(loggingInterceptor)
//        .connectTimeout(100, TimeUnit.SECONDS) // 연결 타임아웃 설정 (기본값은 10초)
//        .readTimeout(100, TimeUnit.SECONDS)    // 읽기 타임아웃 설정 (기본값은 10초)
//        .writeTimeout(100, TimeUnit.SECONDS)   // 쓰기 타임아웃 설정 (기본값은 10초)
//        .build()
//
//    private val retrofit: Retrofit by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            //.addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
//            .client(okHttpClient)
//            .build()
//    }
//
//}