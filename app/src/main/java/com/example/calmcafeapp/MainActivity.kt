package com.example.calmcafeapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
            if (error != null) {
                // 로그인 정보가 없으면 LoginActivity로 이동
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            else if (tokenInfo != null) {
                // 토큰 정보가 있으면 자동 로그인
                showInit()
            }
        }


        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 로그아웃 버튼 클릭 시 로그아웃 처리
        binding.logoutBtn.setOnClickListener {
            logout()
        }

        initBottomNav()

//        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.navigationMain.selectedItemId = R.id.navigation_map
    }

    // 로그아웃 처리 메서드
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
                finish() // 현재 액티비티 종료
            }
        }
    }


    private fun showInit() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_main, HomeFragment()) // HomeFragment를 FragmentContainerView에 교체
            .commit()
    }

    private fun initBottomNav() {
        binding.navigationMain.itemIconTintList = null

        binding.navigationMain.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.navigation_map -> {
                    addFragment(HomeFragment())
                }

                R.id.navigation_rank -> {
                    addFragment(RankFragment())
                }

                R.id.navigation_setting -> {
                    addFragment(SettingFragment())
                }
            }
            return@setOnItemSelectedListener true
        }

        binding.navigationMain.setOnItemReselectedListener {  } // 바텀네비 재클릭시 화면 재생성 방지
    }
    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container_main, fragment).commit()
    }
    fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .commit {
                setCustomAnimations(
                    R.anim.slide_3,
                    R.anim.fade_out,
                    R.anim.slide_1,
                    R.anim.fade_out
                )
                replace(R.id.container_main, fragment)

                addToBackStack(null)
            }
    }
}