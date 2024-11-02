package com.example.calmcafeapp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.calmcafeapp.databinding.ActivityUserBinding
import com.example.calmcafeapp.ui.HomeFragment
import com.example.calmcafeapp.ui.RankFragment
import com.example.calmcafeapp.ui.SettingFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior

class UserActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 로그 추가 - onCreate 호출 확인
        Log.d("UserActivity", "onCreate called")

        // 초기 프래그먼트 설정
        if (savedInstanceState == null) {
            Log.d("UserActivity", "Setting initial fragment")
            switchToFragment(HomeFragment())
        }

        // 바텀 네비게이션 초기화
        initBottomNav()
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.navigationUser.selectedItemId = R.id.navigation_map

        //initBottomSheet()

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager
                .popBackStack()
        }
        else {
            super.onBackPressed()
        }
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

//    private fun initBottomSheet() {
//        val bottomSheetLayout = findViewById<View>(R.id.bottomSheetLayout)
//        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)
//
//        // 바텀시트 초기 상태를 숨김으로 설정
//        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
//    }
//
//    // 바텀시트 열기 메서드
//    fun showNavigatorBottomSheet() {
//        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
//        binding.bottomSheetLayout.visibility = View.VISIBLE
//
//    }
//
//    // 바텀시트 숨기기 메서드
//    fun hideNavigatorBottomSheet() {
//        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
//    }

    private fun switchToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_main, fragment)
            .commitNow()

        Log.d("UserActivity", "Switched to fragment: ${fragment::class.java.simpleName}")
    }


}
