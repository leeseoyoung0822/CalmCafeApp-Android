package com.example.calmcafeapp.apiManager

import com.example.calmcafeapp.BuildConfig
import com.example.calmcafeapp.api.CafeDetailService
import com.example.calmcafeapp.api.LocationService
import com.example.calmcafeapp.api.LoginService
import com.example.calmcafeapp.api.M_HomeService
import com.example.calmcafeapp.api.MapService
import com.example.calmcafeapp.api.NaverReverseGeocodingService
import com.example.calmcafeapp.api.ODsayService
import com.example.calmcafeapp.api.RankingService
import com.example.calmcafeapp.api.SettingService
import com.example.calmcafeapp.api.TmapService
import com.example.calmcafeapp.data.Geometry
import com.example.calmcafeapp.data.GeometryDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request

import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
object ApiManager {
    private var BASE_URL = BuildConfig.AUTH_BASE_URL

    val loggingInterceptor= HttpLoggingInterceptor().apply {
        level= HttpLoggingInterceptor.Level.BODY
    }


    // 네이버 오픈 API Base URL
    private const val NAVER_OPEN_API_BASE_URL = "https://openapi.naver.com/"
    // 네이버 클라우드 플랫폼 API Base URL
    private const val NAVER_CLOUD_PLATFORM_API_BASE_URL = "https://naveropenapi.apigw.ntruss.com/"

    // 대중교통 API
    private const val ODSAY_BASE_URL = "https://api.odsay.com/v1/api/"

    private const val TMAP_BASE_URL = "https://apis.openapi.sk.com/tmap/"

    private val gson : Gson = GsonBuilder().setLenient().create()





    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(100, TimeUnit.SECONDS) // 연결 타임아웃 설정 (기본값은 10초)
        .readTimeout(100, TimeUnit.SECONDS)    // 읽기 타임아웃 설정 (기본값은 10초)
        .writeTimeout(100, TimeUnit.SECONDS)   // 쓰기 타임아웃 설정 (기본값은 10초)
        .build()


    // 네이버 오픈 API용 OkHttpClient
    private val naverOpenApiClient = okHttpClient.newBuilder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-Naver-Client-Id", BuildConfig.NAVER_OPEN_API_CLIENT_ID)
                .addHeader("X-Naver-Client-Secret", BuildConfig.NAVER_OPEN_API_CLIENT_SECRET)
                .build()
            chain.proceed(request)
        }
        .build()

    // 네이버 클라우드 플랫폼 API용 OkHttpClient
    private val naverCloudPlatformClient = okHttpClient.newBuilder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-NCP-APIGW-API-KEY-ID", BuildConfig.NCP_API_KEY_ID)
                .addHeader("X-NCP-APIGW-API-KEY", BuildConfig.NCP_API_KEY)
                .build()
            chain.proceed(request)
        }
        .build()

    // ODsay API용 OkHttpClient
    private val odsayApiClient = okHttpClient.newBuilder()
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


    private val tmapApiClient = okHttpClient.newBuilder()
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

    private const val SERVER_API_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdHJpbmciLCJpYXQiOjE3MzA1Njc3MjgsImV4cCI6MTczMDY3NTcyOCwiYXV0aG9yaXRpZXMiOiJVU0VSIn0.5vdPg_FkIgRcpVOt9mU86kU99OAakVNk484x4OP6sDbSw3TwbqszV4oOF_EadDbdsQtFEejtYS3AUJqAfNVTkQ"
    private val serverApiClient = okHttpClient.newBuilder()
        .addInterceptor(AuthorizationInterceptor(SERVER_API_TOKEN))
        .build()

    // 서버 API용 Retrofit 인스턴스 (ServerRetrofit)
    private val ServerRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }


    val loginService: LoginService = ServerRetrofit.create(LoginService::class.java)
    val tmapService: TmapService = tmapApiRetrofit.create(TmapService::class.java)
    val odsayService: ODsayService = odsayApiRetrofit.create(ODsayService::class.java)
    val naverApiService: MapService  = ServerRetrofit.create(MapService::class.java)
    val naverReverseGeocodingService: NaverReverseGeocodingService = naverCloudPlatformRetrofit.create(NaverReverseGeocodingService::class.java)
    val cafeDetailService : CafeDetailService = ServerRetrofit.create(CafeDetailService::class.java)
    val locationService : LocationService = ServerRetrofit.create(LocationService::class.java)
    // 랭킹 탑 100 api
    val rankingService: RankingService by lazy { ServerRetrofit.create(RankingService::class.java) }
    val settingService: SettingService by lazy { ServerRetrofit.create(SettingService::class.java) }
    val m_HomeService: M_HomeService by lazy { ServerRetrofit.create(M_HomeService::class.java) }

}