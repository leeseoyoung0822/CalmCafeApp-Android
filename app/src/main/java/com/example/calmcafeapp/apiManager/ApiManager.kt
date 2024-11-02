package com.example.calmcafeapp.apiManager

import android.content.Context
import android.util.Log
import com.example.calmcafeapp.BuildConfig
import com.example.calmcafeapp.api.CafeDetailService
import com.example.calmcafeapp.api.LoginService
import com.example.calmcafeapp.api.MapService
import com.example.calmcafeapp.api.NaverReverseGeocodingService
import com.example.calmcafeapp.api.ODsayService
import com.example.calmcafeapp.api.RankingService
import com.example.calmcafeapp.api.TmapService
import com.example.calmcafeapp.data.CafeDetailResponse
import com.example.calmcafeapp.data.Geometry
import com.example.calmcafeapp.data.GeometryDeserializer
import com.example.calmcafeapp.data.ReverseGeocodingResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object ApiManager {
    private var BASE_URL = BuildConfig.AUTH_BASE_URL
    //회원 토큰 반환 api

    private lateinit var appContext: Context

    val instance: LoginService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(LoginService::class.java)
    }

    fun init(context: Context) {
        appContext = context.applicationContext
    }
    // Access Token을 포함하는 Interceptor 생성
    private val authInterceptor = Interceptor { chain ->
        val sharedPreferences =
            appContext.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("ACCESS_TOKEN", null)

        val request = chain.request().newBuilder().apply {
            accessToken?.let {
                addHeader("Authorization", "Bearer $it")
            }
            addHeader("Accept", "application/json")        // JSON 형식의 응답을 원할 때 추가
            addHeader("Content-Type", "application/json")   // JSON 형식의 요청을 보낼 때 추가
        }.build()

        // Access Token 확인용 로그
        Log.d("ApiManager", "Using Access Token from SharedPreferences: $accessToken")

        chain.proceed(request)
    }

    // 공통 Retrofit 인스턴스 생성
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(commonClient)
            .build()
    }

    // 토큰 반환 api
    val loginService: LoginService by lazy {
        retrofit.create(LoginService::class.java)
    }
    // 랭킹 탑 100 api
    val rankingService: RankingService by lazy {
        retrofit.create(RankingService::class.java)
    }


    // 네이버 오픈 API Base URL
    private const val NAVER_OPEN_API_BASE_URL = "https://openapi.naver.com/"
    // 네이버 클라우드 플랫폼 API Base URL
    private const val NAVER_CLOUD_PLATFORM_API_BASE_URL = "https://naveropenapi.apigw.ntruss.com/"

    // 대중교통 API
    private const val ODSAY_BASE_URL = "https://api.odsay.com/v1/api/"

    private const val TMAP_BASE_URL = "https://apis.openapi.sk.com/tmap/"

    private val gson : Gson = GsonBuilder().setLenient().create()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }



    // 공통 OkHttpClient
    private val commonClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
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

    // ODsay API용 OkHttpClient
    private val odsayApiClient = commonClient.newBuilder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val originalUrl = originalRequest.url

            val urlWithApiKey = originalUrl.newBuilder()
                .addQueryParameter("apiKey", BuildConfig.ODSAY_API_KEY)
                .build()

            val newRequest = originalRequest.newBuilder()
                .url(urlWithApiKey)
                .build()

            chain.proceed(newRequest)
        }
        .build()


    private val tmapApiClient = commonClient.newBuilder()
        .addInterceptor { chain ->
            val originalRequest: Request = chain.request()
            val newRequest: Request = originalRequest.newBuilder()
                .header("appKey", BuildConfig.TMAP_API_KEY) // 헤더에 appKey 추가
                .header("Accept", "application/json")       // Accept 헤더 추가
                .header("Content-Type", "application/json") // Content-Type 헤더 추가
                .build()
            chain.proceed(newRequest)
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

    // ODsay API용 Retrofit 인스턴스
    private val odsayApiRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ODSAY_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(odsayApiClient)
            .build()
    }

    // TMAP API용 Retrofit 인스턴스
    private val tmapApiRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(TMAP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(tmapGson))
            .client(tmapApiClient)
            .build()
    }
    // TMAP API용 Gson 설정
    private val tmapGson = GsonBuilder()
        .registerTypeAdapter(Geometry::class.java, GeometryDeserializer())
        .create()

    private const val SERVER_API_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdHJpbmciLCJpYXQiOjE3MzA1NjIyOTksImV4cCI6MTczMDY3MDI5OSwiYXV0aG9yaXRpZXMiOiJVU0VSIn0.EhrmQ9R9RAoMpICqoaTlx9Z75bXdRMEnjYgT0p3zfz8Jcew6VIZLhNm05WXhvM-zrxE0R1Zkbo15aDJtUCueuA"
    private val serverApiClient = commonClient.newBuilder()
        .addInterceptor(AuthorizationInterceptor(SERVER_API_TOKEN))
        .build()

    // 서버 API용 Retrofit 인스턴스 (ServerRetrofit)
    private val ServerRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(serverApiClient) // 수정된 OkHttpClient 사용
            .build()
    }



    //val loginService: LoginService = retrofit.create(LoginService::class.java)
    val tmapService: TmapService = tmapApiRetrofit.create(TmapService::class.java)
    val odsayService: ODsayService = odsayApiRetrofit.create(ODsayService::class.java)
    val naverApiService: MapService  = naverOpenApiRetrofit.create(MapService::class.java)
    val naverReverseGeocodingService: NaverReverseGeocodingService = naverCloudPlatformRetrofit.create(NaverReverseGeocodingService::class.java)
    val cafeDetailService : CafeDetailService = ServerRetrofit.create(CafeDetailService::class.java)

}