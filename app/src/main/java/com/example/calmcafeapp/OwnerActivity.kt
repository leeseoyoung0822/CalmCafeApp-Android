package com.example.calmcafeapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.calmcafeapp.databinding.ActivityOwnerBinding
import com.example.calmcafeapp.ui.M_HomeFragment
import com.example.calmcafeapp.ui.M_SettingFragment
import com.example.calmcafeapp.ui.M_StoreFragment


class OwnerActivity : AppCompatActivity() {
    lateinit var binding: ActivityOwnerBinding

    // 프래그먼트 변수 선언
    private lateinit var M_homeFragment: M_HomeFragment
    private lateinit var M_dataFragment: M_StoreFragment
    private lateinit var M_settingFragment: M_SettingFragment

    // 현재 활성화된 프래그먼트를 저장할 변수
    private var activeFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOwnerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 바텀 네비게이션 초기화
        initBottomNav()
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // 기본 화면 설정 (M_HomeFragment)
        if (savedInstanceState == null) {
            addFragment(M_HomeFragment())
        }

        // 바텀 네비게이션 초기화
        initBottomNav()
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.navigationOwner.selectedItemId = R.id.navigation_m_store
    }


    private fun initBottomNav() {
        binding.navigationOwner.itemIconTintList = null

        binding.navigationOwner.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.navigation_m_store -> {
                    addFragment(M_StoreFragment())
                }

                R.id.navigation_m_home -> {
                    addFragment(M_HomeFragment())
                }

                R.id.navigation_m_setting -> {
                    addFragment(M_SettingFragment())
                }
            }
            return@setOnItemSelectedListener true
        }

        binding.navigationOwner.setOnItemReselectedListener {  } // 바텀네비 재클릭시 화면 재생성 방지
    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container_owner, fragment).commit()
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
                replace(R.id.container_owner, fragment)

                addToBackStack(null)
            }
    }
}
