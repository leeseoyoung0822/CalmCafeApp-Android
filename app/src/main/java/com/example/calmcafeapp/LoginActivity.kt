package com.example.calmcafeapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.calmcafeapp.apiManager.ApiManager
import com.example.calmcafeapp.data.TokenResponse
import com.example.calmcafeapp.data.UserInfo
import com.example.calmcafeapp.databinding.ActivityLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var kakaoCallback: (OAuthToken?, Throwable?) -> Unit
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // SharedPreferences 초기화
        preferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        // 저장된 토큰이 있으면 자동 로그인 처리
        val savedToken = preferences.getString("accessToken", null)
        if (savedToken != null) {
            // 토큰이 있으면 MainActivity로 이동
            navigateToMainActivity()
        }

        // 카카오 로그인 콜백 설정
        setKakaoCallback()

        // 로그인 버튼 클릭 시 카카오 로그인 실행
        binding.loginBtn.setOnClickListener {
            clikcKakaoLoginBtn(it)
        }
        // "한산\n한家"의 "家" 글자만 회색으로 변경
        val text = "한산\n한家"
        val spannable = SpannableString(text)
        spannable.setSpan(ForegroundColorSpan(Color.GRAY), 4, 5, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.logo.text = spannable
    }

    // 카카오톡 로그인 버튼 클릭 처리
    fun clikcKakaoLoginBtn(view: View) {
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this, callback = kakaoCallback)
        } else {
            // 카카오톡이 설치되어 있지 않으면 카카오계정으로 로그인
            UserApiClient.instance.loginWithKakaoAccount(this, callback = kakaoCallback)
        }
    }

    fun setKakaoCallback() {
        kakaoCallback = { token, error ->
            if (error != null) {
                Log.d("[카카오로그인]", "로그인 실패: ${error.message}")
            } else if (token != null) {
                Log.d("[카카오로그인]", "로그인에 성공하였습니다. 액세스 토큰${token.accessToken}")

                // 카카오 사용자 정보 요청
                UserApiClient.instance.me { user, meError ->
                    if (meError != null) {
                        Log.e("[카카오사용자정보]", "사용자 정보 요청 실패", meError)
                    } else if (user != null) {
                        // 백엔드로 전달할 사용자 정보 생성
                        val userInfo = UserInfo(
                            email = user.kakaoAccount?.email ?: "",
                            username = user.kakaoAccount?.profile?.nickname ?: "",
                            provider = "kakao"
                        )

                        // Retrofit을 사용하여 백엔드 API 호출
                        val call = ApiManager.instance.generateToken(userInfo)
                        call.enqueue(object : Callback<TokenResponse> {
                            override fun onResponse(
                                call: Call<TokenResponse>,
                                response: Response<TokenResponse>
                            ) {
                                if (response.isSuccessful) {
                                    val tokenResponse = response.body()
                                    if (tokenResponse?.isSuccess == true) {
                                        Log.d("[백엔드]", "토큰 생성 성공: ${tokenResponse.result.accessToken}")

                                        // 로그인 성공 후 MainActivity로 이동
                                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                        startActivity(intent)
                                        finish() // 현재 액티비티 종료
                                    } else {
                                        Log.e("[백엔드]", "토큰 생성 실패: ${tokenResponse?.message}")
                                    }
                                } else {
                                    Log.e("[백엔드]", "응답 실패")
                                }
                            }

                            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                                Log.e("[백엔드]", "API 호출 실패", t)
                            }
                        })
                    }
                }
            }
        }
    }
    // MainActivity로 이동하는 함수
    private fun navigateToMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish() // 현재 액티비티 종료
    }
}