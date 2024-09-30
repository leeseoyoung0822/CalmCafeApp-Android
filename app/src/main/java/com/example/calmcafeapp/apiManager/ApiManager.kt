package com.example.calmcafeapp.apiManager

import com.example.calmcafeapp.BuildConfig
import com.example.calmcafeapp.api.MapService
import com.example.calmcafeapp.api.NaverReverseGeocodingService
import com.example.calmcafeapp.data.ReverseGeocodingResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiManager {

    // 네이버 오픈 API Base URL
    private const val NAVER_OPEN_API_BASE_URL = "https://openapi.naver.com/"
    // 네이버 클라우드 플랫폼 API Base URL
    private const val NAVER_CLOUD_PLATFORM_API_BASE_URL = "https://naveropenapi.apigw.ntruss.com/"
    private var BASE_URL = BuildConfig.AUTH_BASE_URL

    private val gson : Gson = GsonBuilder().setLenient().create()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // 공통 OkHttpClient
    private val commonClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(100, TimeUnit.SECONDS)
        .readTimeout(100, TimeUnit.SECONDS)
        .writeTimeout(100, TimeUnit.SECONDS)
        .build()

    // 네이버 오픈 API용 OkHttpClient
    private val naverOpenApiClient = commonClient.newBuilder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-Naver-Client-Id", BuildConfig.NAVER_OPEN_API_CLIENT_ID)
                .addHeader("X-Naver-Client-Secret", BuildConfig.NAVER_OPEN_API_CLIENT_SECRET)
                .build()
            chain.proceed(request)
        }
        .build()

    // 네이버 클라우드 플랫폼 API용 OkHttpClient
    private val naverCloudPlatformClient = commonClient.newBuilder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-NCP-APIGW-API-KEY-ID", BuildConfig.NCP_API_KEY_ID)
                .addHeader("X-NCP-APIGW-API-KEY", BuildConfig.NCP_API_KEY)
                .build()
            chain.proceed(request)
        }
        .build()

    // 네이버 오픈 API용 Retrofit 인스턴스
    private val naverOpenApiRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(NAVER_OPEN_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(naverOpenApiClient)
            .build()
    }

    // 네이버 클라우드 플랫폼 API용 Retrofit 인스턴스
    private val naverCloudPlatformRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(NAVER_CLOUD_PLATFORM_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(naverCloudPlatformClient)
            .build()
    }




    private val ServerRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(commonClient)
            .build()
    }

    //val loginService: LoginService = retrofit.create(LoginService::class.java)
    val naverApiService: MapService  = naverOpenApiRetrofit.create(MapService::class.java)
    val naverReverseGeocodingService: NaverReverseGeocodingService = naverCloudPlatformRetrofit.create(NaverReverseGeocodingService::class.java)
}