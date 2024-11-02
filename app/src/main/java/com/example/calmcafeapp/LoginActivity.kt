
package com.example.calmcafeapp

import android.content.Intent
import android.os.Bundle
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
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Base64
import androidx.core.content.ContextCompat.startActivity
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var kakaoCallback: (OAuthToken?, Throwable?) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)



        // 카카오 로그인 콜백 설정
        getKeyHash()
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


    fun clikcKakaoLoginBtn(view: View) {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this, callback = kakaoCallback)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = kakaoCallback)
        }
    }

    fun setKakaoCallback() {
        kakaoCallback = { token, error ->
            if (error != null) {
                Log.d("[카카오로그인]", "로그인 실패: ${error.message}")
            } else if (token != null) {
                Log.d("[카카오로그인]", "로그인에 성공하였습니다. 액세스 토큰: ${token.accessToken}")

                // 액세스 토큰을 SharedPreferences에 저장
                val sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
                sharedPreferences.edit().putString("ACCESS_TOKEN", token.accessToken).apply()


                UserApiClient.instance.me { user, meError ->
                    if (meError != null) {
                        Log.e("[카카오사용자정보]", "사용자 정보 요청 실패", meError)
                    } else if (user != null) {
                        Log.d("[카카오사용자정보]", """
                            사용자의 이메일: ${user.kakaoAccount?.email}
                            닉네임: ${user.kakaoAccount?.profile?.nickname}
                            프로필 이미지: ${user.kakaoAccount?.profile?.thumbnailImageUrl}
                            생일: ${user.kakaoAccount?.birthday}
                            성별: ${user.kakaoAccount?.gender}
                            연령대: ${user.kakaoAccount?.ageRange}
                        """.trimIndent())

                        val userInfo = UserInfo(
                            email = user.kakaoAccount?.email ?: "lanmecan@naver.com",
                            username = user.kakaoAccount?.profile?.nickname ?: "",
                            provider = "kakao"
                        )

                        val call = ApiManager.instance.generateToken(userInfo)
                        call.enqueue(object : Callback<TokenResponse> {
                            override fun onResponse(
                                call: Call<TokenResponse>,
                                response: Response<TokenResponse>
                            ) {
                                if (response.isSuccessful) {
                                    val tokenResponse = response.body()
                                    Log.d("tokenResponse", "$tokenResponse")
                                    if (tokenResponse?.isSuccess == true) {
                                        Log.d("[백엔드]", "토큰 생성 성공: ${tokenResponse.result.accessToken}")
                                        handleLoginResponse(tokenResponse.result.accessToken, tokenResponse.result.role)
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
    // 키해시를 확인하는 함수
    private fun getKeyHash() {
        try {
            val info: PackageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val keyHash = Base64.encodeToString(md.digest(), Base64.NO_WRAP)
                Log.d("KeyHash", "KeyHash: $keyHash")
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("KeyHash", "NameNotFoundException: ${e.message}")
        } catch (e: NoSuchAlgorithmException) {
            Log.e("KeyHash", "NoSuchAlgorithmException: ${e.message}")
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
