package com.example.calmcafeapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.calmcafeapp.databinding.ActivityUserBinding
import com.example.calmcafeapp.ui.HomeFragment
import com.example.calmcafeapp.ui.RankFragment
import com.example.calmcafeapp.ui.SettingFragment

class UserActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 바텀 네비게이션 초기화
        initBottomNav()
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.navigationUser.selectedItemId = R.id.navigation_map
    }

    private fun showInit() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_main, HomeFragment()) // HomeFragment를 FragmentContainerView에 교체
            .commit()
    }

    private fun initBottomNav() {
        binding.navigationUser.itemIconTintList = null

        binding.navigationUser.setOnItemSelectedListener {
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

        binding.navigationUser.setOnItemReselectedListener {  } // 바텀네비 재클릭시 화면 재생성 방지
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
