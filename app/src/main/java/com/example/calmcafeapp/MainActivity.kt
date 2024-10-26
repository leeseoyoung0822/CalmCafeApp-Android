package com.example.calmcafeapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.calmcafeapp.databinding.ActivityMainBinding
import com.example.calmcafeapp.ui.HomeFragment
import com.example.calmcafeapp.ui.RankFragment
import com.example.calmcafeapp.ui.SettingFragment
import com.kakao.sdk.user.UserApiClient


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 카카오 로그인 정보 확인
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            Log.d("login", "${tokenInfo}")
            if (error != null) {
                // 로그인 정보가 없으면 LoginActivity로 이동
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else if (tokenInfo != null) {
                // role에 따라 분기 처리
                val role = getUserRole() // role 값 가져옴
                navigateToRoleActivity(role)
            }
            else if (tokenInfo != null) {
                // 토큰 정보가 있으면 자동 로그인
            }
        }


        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* 로그아웃 버튼 클릭 시 로그아웃 처리
        binding.logoutBtn.setOnClickListener {
            logout()
        }*/

    }

    /* 로그아웃 처리 메서드
    private fun logout() {
        UserApiClient.instance.logout { error ->
            if (error != null) {
                // 로그아웃 실패 처리
                Toast.makeText(this, "로그아웃 실패: $error", Toast.LENGTH_SHORT).show()
            } else {
                // 로그아웃 성공 처리 후 로그인 화면으로 이동
                Toast.makeText(this, "로그아웃 성공", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
    }*/
    private fun navigateToRoleActivity(role: String) {
        Log.d("role", "${role}")
        val intent = when (role) {
            "CAFE" -> Intent(this, OwnerActivity::class.java) // 카페 주인용 액티비티로 이동
            "USER" -> Intent(this, UserActivity::class.java) // 일반 유저용 액티비티로 이동
            else -> {
                Toast.makeText(this, "알 수 없는 역할입니다.", Toast.LENGTH_SHORT).show()
                return
            }
        }
        startActivity(intent)
        finish()
    }

    private fun getUserRole(): String {

        return "USER"
    }
}
