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

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var kakaoCallback: (OAuthToken?, Throwable?) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                // 로그인 성공 후 MainActivity로 이동하여 HomeFragment로 전환
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish() // 현재 액티비티 종료

            } else {
                Log.d("[카카오로그인]", "토큰==null error==null")
            }
        }
    }
}