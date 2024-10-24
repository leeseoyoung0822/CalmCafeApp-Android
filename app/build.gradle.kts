import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

val properties = Properties().apply {
    load(FileInputStream(rootProject.file("local.properties")))
}

android {
    namespace = "com.example.calmcafeapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.calmcafeapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // 네이버 오픈 API 키 설정
        val naverOpenApiClientId = properties.getProperty("NAVER_OPEN_API_CLIENT_ID")
            ?: throw IllegalArgumentException("NAVER_OPEN_API_CLIENT_ID가 local.properties에 정의되어 있지 않습니다.")
        val naverOpenApiClientSecret = properties.getProperty("NAVER_OPEN_API_CLIENT_SECRET")
            ?: throw IllegalArgumentException("NAVER_OPEN_API_CLIENT_SECRET가 local.properties에 정의되어 있지 않습니다.")
        buildConfigField("String", "NAVER_OPEN_API_CLIENT_ID", "\"$naverOpenApiClientId\"")
        buildConfigField("String", "NAVER_OPEN_API_CLIENT_SECRET", "\"$naverOpenApiClientSecret\"")

        // 네이버 클라우드 플랫폼 API 키 설정
        val ncpApiKeyId = properties.getProperty("NCP_API_KEY_ID")
            ?: throw IllegalArgumentException("NCP_API_KEY_ID가 local.properties에 정의되어 있지 않습니다.")
        val ncpApiKey = properties.getProperty("NCP_API_KEY")
            ?: throw IllegalArgumentException("NCP_API_KEY가 local.properties에 정의되어 있지 않습니다.")

        buildConfigField("String", "NCP_API_KEY_ID", "\"$ncpApiKeyId\"")
        buildConfigField("String", "NCP_API_KEY", "\"$ncpApiKey\"")


        val ODSAYApiKey = properties.getProperty("ODSAY_API_KEY")
            ?: throw IllegalArgumentException("ODSAY_API_KEY가 local.properties에 정의되어 있지 않습니다.")

        buildConfigField("String", "ODSAY_API_KEY", "\"$ODSAYApiKey\"")

        val TAMPApiKey = properties.getProperty("TMAP_API_KEY")
            ?: throw IllegalArgumentException("TMAP_API_KEY가 local.properties에 정의되어 있지 않습니다.")

        buildConfigField("String", "TMAP_API_KEY", "\"$TAMPApiKey\"")


        buildConfigField("String","AUTH_BASE_URL",properties.getProperty("base.url"))

        // 카카오 네이티브 앱 키 설정
        val nativeAppKey = properties.getProperty("NATIVE_APP_KEY")
            ?: throw IllegalArgumentException("NATIVE_APP_KEY가 local.properties에 정의되어 있지 않습니다.")
        buildConfigField("String", "NATIVE_APP_KEY", "\"$nativeAppKey\"")

        manifestPlaceholders["NCP_API_KEY_ID"] = ncpApiKeyId
        manifestPlaceholders["NATIVE_APP_KEY"] = nativeAppKey
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }





    //자바 버전 설정
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures{
        dataBinding=true
        viewBinding=true
        buildConfig=true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation ("org.locationtech.proj4j:proj4j:1.1.0")

    // Retrofit 라이브러리
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    // Gson 변환기
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // 카카오톡 모듈
    implementation ("com.kakao.sdk:v2-all:2.20.6") // 전체 모듈 설치
    implementation ("com.kakao.sdk:v2-user:2.20.6") // 카카오 로그인 API 모듈
    implementation ("com.kakao.sdk:v2-share:2.20.6") // 카카오톡 공유 API 모듈
    implementation ("com.kakao.sdk:v2-talk:2.20.6") // 카카오톡 채널, 카카오톡 소셜, 카카오톡 메시지 API 모듈
    implementation ("com.kakao.sdk:v2-friend:2.20.6") // 피커 API 모듈
    implementation ("com.kakao.sdk:v2-navi:2.20.6") // 카카오내비 API 모듈
    implementation ("com.kakao.sdk:v2-cert:2.20.6") // 카카오톡 인증 서비스 API 모듈

    //네비게이션
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    //서버통신
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("com.squareup.okhttp3:okhttp:4.8.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.8.0")
    //프로필 이미지뷰
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    //카드뷰
    implementation("com.google.android.material:material:1.12.0")
    // 네이버 지도 SDK
    implementation("com.naver.maps:map-sdk:3.19.1")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    //뷰모델
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")

    implementation ("com.google.maps.android:android-maps-utils:2.2.5")

    //이미지 출력
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
    implementation("jp.wasabeef:glide-transformations:4.3.0")

}
