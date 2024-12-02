package com.example.calmcafeapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.example.calmcafeapp.databinding.ActivityUserBinding
import com.example.calmcafeapp.ui.HomeFragment
import com.example.calmcafeapp.ui.RankFragment
import com.example.calmcafeapp.ui.SettingFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior

class UserActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private var backPressedTime: Long = 0
    private var tirr : Boolean = false

    // 프래그먼트 변수 선언
    private lateinit var homeFragment: HomeFragment
    private lateinit var rankFragment: RankFragment
    private lateinit var settingFragment: SettingFragment

    // 현재 활성화된 프래그먼트를 저장할 변수
    private var activeFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 로그 추가 - onCreate 호출 확인
        Log.d("UserActivity", "onCreate called")

        // 프래그먼트 초기화
        if (savedInstanceState == null) {
            Log.d("UserActivity", "Initializing fragments")
            homeFragment = HomeFragment()
            rankFragment = RankFragment()
            settingFragment = SettingFragment()

            // 프래그먼트 추가 및 숨기기
            supportFragmentManager.beginTransaction()
                .add(R.id.container_main, homeFragment, "home")
                .hide(homeFragment)
                .commit()

            supportFragmentManager.beginTransaction()
                .add(R.id.container_main, rankFragment, "rank")
                .hide(rankFragment)
                .commit()

            supportFragmentManager.beginTransaction()
                .add(R.id.container_main, settingFragment, "setting")
                .hide(settingFragment)
                .commit()

            // 초기 프래그먼트 설정 및 표시
            supportFragmentManager.beginTransaction()
                .show(homeFragment)
                .commit()
            activeFragment = homeFragment
        } else {
            // 화면 회전 등으로 인해 액티비티가 재생성될 때, 기존 프래그먼트들을 찾아서 할당
            homeFragment = supportFragmentManager.findFragmentByTag("home") as HomeFragment
            rankFragment = supportFragmentManager.findFragmentByTag("rank") as RankFragment
            settingFragment = supportFragmentManager.findFragmentByTag("setting") as SettingFragment

            // 현재 활성화된 프래그먼트를 찾아서 할당
            activeFragment = when {
                homeFragment.isVisible -> homeFragment
                rankFragment.isVisible -> rankFragment
                settingFragment.isVisible -> settingFragment
                else -> homeFragment
            }
        }

        // 바텀 네비게이션 초기화
        initBottomNav()
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.navigationUser.selectedItemId = R.id.navigation_map

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        // 현재 프래그먼트가 HomeFragment가 아닌 경우, HomeFragment로 전환
        if (activeFragment != homeFragment) {
            binding.navigationUser.selectedItemId = R.id.navigation_map
            showFragment(homeFragment)
        } else {
            super.onBackPressed()
        }
        val currentTime = System.currentTimeMillis()

        if (currentTime - backPressedTime < 2000) { // 2초 이내에 다시 누르면 앱 종료
            super.onBackPressed() // 앱 종료
        } else {
            backPressedTime = currentTime
            //Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initBottomNav() {
        binding.navigationUser.itemIconTintList = null

        binding.navigationUser.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_map -> {
                    showFragment(homeFragment)
                    Log.d("tirr", "${tirr}")
                    if(tirr){
                        binding.btnBack.visibility = View.VISIBLE
                        tirr = false
                    }
                    true
                }
                R.id.navigation_rank -> {
                    showFragment(rankFragment)
                    if(binding.btnBack.visibility == View.VISIBLE){
                        binding.btnBack.visibility = View.GONE
                        tirr = true
                    }

                    true
                }
                R.id.navigation_setting -> {
                    showFragment(settingFragment)
                    if(binding.btnBack.visibility == View.VISIBLE){
                        binding.btnBack.visibility = View.GONE
                        tirr = true
                    }

                    true
                }
                else -> false
            }
        }

        binding.navigationUser.setOnItemReselectedListener {
            // 바텀 네비게이션 아이템 재선택 시 아무 동작도 하지 않음
        }
    }

    private fun showFragment(fragment: Fragment) {
        if (fragment != activeFragment) {
            supportFragmentManager.beginTransaction()
                .hide(activeFragment!!)
                .show(fragment)
                .commit()
            activeFragment = fragment
        }
    }


}
