package com.example.calmcafeapp

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import com.example.calmcafeapp.databinding.ActivityLoginBinding
import com.example.calmcafeapp.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // "한산\n한家"의 "家" 글자만 회색으로 변경
        val text = "한산\n한家"
        val spannable = SpannableString(text)
        spannable.setSpan(ForegroundColorSpan(Color.GRAY), 4, 5, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.logo.text = spannable


        sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)


        //스플래시 화면 표시 후 메인 액티비티로 이동 추후 JoinActivity로 변경
        Handler(Looper.getMainLooper()).postDelayed({
            //checkAutoLogin()
            moveToLoginActivity()
        }, 1000)

    }
    /*private fun checkAutoLogin() {
        val accessToken = sharedPreferences.getString("ACCESS_TOKEN", null)
        if (accessToken != null) {
            // 액세스 토큰이 존재하면 MainActivity로 이동
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            // 액세스 토큰이 없으면 LoginActivity로 이동
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        //finish() // 현재 SplashActivity 종료
    }*/

    private fun moveToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // SplashActivity 종료
    }

}