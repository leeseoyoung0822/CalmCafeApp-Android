package com.example.calmcafeapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.calmcafeapp.apiManager.ApiManager
import com.example.calmcafeapp.data.KakaoLogin
import com.example.calmcafeapp.data.UserInfo
import com.example.calmcafeapp.databinding.ActivityLoginBinding
import com.example.calmcafeapp.login.LocalDataSource
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JoinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    lateinit var kakaoCallback: (OAuthToken?, Throwable?) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val keyHash = Utility.getKeyHash(this)
        Log.d("키해시", keyHash)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        KakaoSdk.init(this, BuildConfig.NATIVE_APP_KEY)

        LocalDataSource.init(this)

        // 로그인 상태 확인
        checkKakaoLoginStatus()

        binding.loginBtn.setOnClickListener {
            kakaoLogin()
        }
    }

    private fun checkKakaoLoginStatus() {
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                // 로그인 필요
                Log.d("자동 로그인", "로그인 필요: ${error.localizedMessage}")
            } else if (tokenInfo != null) {
                // 토큰이 유효한 경우 사용자 정보 요청
                Log.d("자동 로그인", "이미 로그인되어 있음, 사용자 정보 요청")
                requestUserInfoAndLogin()
            }
        }
    }

    fun requestUserInfoAndLogin() {
        UserApiClient.instance.me { user, meError ->
            if (meError != null) {
                Log.e("[카카오사용자정보]", "사용자 정보 요청 실패", meError)
            } else if (user != null) {
                Log.d(
                    "[카카오사용자정보]", """
                사용자의 이메일: ${user.kakaoAccount?.email}
                닉네임: ${user.kakaoAccount?.profile?.nickname}
                프로필 이미지: ${user.kakaoAccount?.profile?.thumbnailImageUrl}
                생일: ${user.kakaoAccount?.birthday}
                성별: ${user.kakaoAccount?.gender}
                연령대: ${user.kakaoAccount?.ageRange}
                카카오 ID: ${user.id}
            """.trimIndent()
                )

                val email = if (user.kakaoAccount?.email != null) {
                    user.kakaoAccount?.email!!
                } else {
                    "${user.id}@kakao.com"  // 이메일이 없으면 카카오 ID를 사용하여 고유 이메일 생성
                }

                val username = "${user.kakaoAccount?.profile?.nickname}_${user.id}"  // 닉네임과 카카오 ID를 조합하여 고유한 username 생성

                val userInfo = UserInfo(
                    email = email,
                    username = username,
                    provider = "kakao"
                )

                // 서버에 로그인 요청
                val call = ApiManager.loginService.login(userInfo)
                call.enqueue(object : Callback<KakaoLogin> {
                    override fun onResponse(
                        call: Call<KakaoLogin>,
                        response: Response<KakaoLogin>
                    ) {
                        if (response.isSuccessful) {
                            val data = response.body()?.result
                            LocalDataSource.setAccessToken(data!!.accessToken)
                            val kakaologinresult = response.body()
                            Log.d("tokenResponse", "$kakaologinresult")
                            if (kakaologinresult?.isSuccess == true) {
                                Log.d("[백엔드]", "토큰 생성 성공: ${kakaologinresult.result.accessToken}"
                                )
                                handleLoginResponse(
                                    kakaologinresult.result.accessToken,
                                    kakaologinresult.result.role
                                )
                            } else {
                                Log.e("[백엔드]", "토큰 생성 실패: ${kakaologinresult?.message}")
                            }
                        } else {
                            Log.e("[백엔드]", "응답 실패")
                        }
                    }

                    override fun onFailure(call: Call<KakaoLogin>, t: Throwable) {
                        Log.e("[백엔드]", "API 호출 실패", t)
                    }
                })
            }
        }
    }

    fun kakaoLogin() {
        setKakaoCallback()
        UserApiClient.instance.loginWithKakaoAccount(this, callback = kakaoCallback)
    }

    fun setKakaoCallback() {
        kakaoCallback = { token, error ->
            if (error != null) {
                // 에러 처리 코드...
            } else if (token != null) {
                requestUserInfoAndLogin()
            } else {
                Log.d("카카오로그인", "토큰==null error==null")
            }
        }
    }

    fun handleLoginResponse(accessToken: String, role: String) {
        when (role) {
            "CAFE" -> {
                val intent = Intent(this, OwnerActivity::class.java)
                intent.putExtra("accessToken", accessToken)
                startActivity(intent)
            }
            "USER" -> {
                val intent = Intent(this, UserActivity::class.java)
                intent.putExtra("accessToken", accessToken)
                startActivity(intent)
            }
            else -> {
                Log.e("Login", "알 수 없는 역할: $role")
            }
        }
        finish()
    }
}
