package com.example.calmcafeapp

import android.content.Intent
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // "한산\n한家"의 "家" 글자만 회색으로 변경
        val text = "한산\n한家"
        val spannable = SpannableString(text)
        spannable.setSpan(ForegroundColorSpan(Color.GRAY), 4, 5, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.logo.text = spannable

        //스플래시 화면 표시 후 메인 액티비티로 이동 추후 JoinActivity로 변경
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)

    }

}