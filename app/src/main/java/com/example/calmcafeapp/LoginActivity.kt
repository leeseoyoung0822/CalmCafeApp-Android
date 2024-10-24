package com.example.calmcafeapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.calmcafeapp.databinding.ActivityLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Base64
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
        getKeyHash()

        // 카카오 로그인 콜백 설정
        setKakaoCallback()

        // 로그인 버튼 클릭 시 카카오 로그인 실행
        binding.loginBtn.setOnClickListener {
            clikcKakaoLoginBtn(it)
        }
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
                // 에러 처리 추가 가능
            } else if (token != null) {
                Log.d("[카카오로그인]", "로그인에 성공하였습니다. 액세스 토큰${token.accessToken}")

                // 예시: 서버에서 role을 받아왔다고 가정하고 role에 따른 분기 처리
                val role = "CAFE" // 실제로는 API 응답에서 받아오는 값으로 대체
                handleLoginResponse(token.accessToken, role)
            } else {
                Log.d("[카카오로그인]", "토큰==null error==null")
            }
        }
    }

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
                // 카페 주인용 메인 화면으로 이동
                val intent = Intent(this, OwnerActivity::class.java)
                intent.putExtra("accessToken", accessToken)
                startActivity(intent)
            }
            "USER" -> {
                // 일반 유저용 메인 화면으로 이동
                val intent = Intent(this, UserActivity::class.java)
                intent.putExtra("accessToken", accessToken)
                startActivity(intent)
            }
            else -> {
                Log.e("Login", "알 수 없는 역할: $role")
                // 예외 처리
            }
        }
        finish() // 현재 액티비티 종료
    }

}