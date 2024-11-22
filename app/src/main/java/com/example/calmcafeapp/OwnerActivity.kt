package com.example.calmcafeapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.calmcafeapp.databinding.ActivityOwnerBinding
import com.example.calmcafeapp.ui.HomeFragment
import com.example.calmcafeapp.ui.M_HomeFragment
import com.example.calmcafeapp.ui.M_SettingFragment
import com.example.calmcafeapp.ui.M_StoreFragment
import com.example.calmcafeapp.ui.RankFragment
import com.example.calmcafeapp.ui.SettingFragment


class OwnerActivity : AppCompatActivity() {
    lateinit var binding: ActivityOwnerBinding
    private var activeFragment: Fragment? = null

    private lateinit var m_homeFragment: M_HomeFragment
    private lateinit var m_storeFragment: M_StoreFragment
    private lateinit var m_settingFragment: M_SettingFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOwnerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            Log.d("OwnerActivity", "Initializing fragments")
            m_homeFragment = M_HomeFragment()
            m_storeFragment = M_StoreFragment()
            m_settingFragment = M_SettingFragment()

            // 프래그먼트 추가 및 숨기기
            supportFragmentManager.beginTransaction()
                .add(R.id.container_owner, m_homeFragment, "home")
                .hide(m_homeFragment)
                .commit()

            supportFragmentManager.beginTransaction()
                .add(R.id.container_owner, m_storeFragment, "store")
                .hide(m_storeFragment)
                .commit()

            supportFragmentManager.beginTransaction()
                .add(R.id.container_owner, m_settingFragment, "setting")
                .hide(m_settingFragment)
                .commit()

            // 초기 프래그먼트 설정 및 표시
            supportFragmentManager.beginTransaction()
                .show(m_homeFragment)
                .commit()
            activeFragment = m_homeFragment
        } else {
            // 화면 회전 등으로 인해 액티비티가 재생성될 때, 기존 프래그먼트들을 찾아서 할당
            m_homeFragment = supportFragmentManager.findFragmentByTag("home") as M_HomeFragment
            m_storeFragment = supportFragmentManager.findFragmentByTag("rank") as M_StoreFragment
            m_settingFragment = supportFragmentManager.findFragmentByTag("setting") as M_SettingFragment

            // 현재 활성화된 프래그먼트를 찾아서 할당
            activeFragment = when {
                m_homeFragment.isVisible -> m_homeFragment
                m_storeFragment.isVisible -> m_storeFragment
                m_settingFragment.isVisible -> m_settingFragment
                else -> m_homeFragment
            }
        }

        // 바텀 네비게이션 초기화
        initBottomNav()
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.navigationOwner.selectedItemId = R.id.navigation_m_store

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        // 현재 프래그먼트가 HomeFragment가 아닌 경우, HomeFragment로 전환
        if (activeFragment != m_homeFragment) {
            binding.navigationOwner.selectedItemId = R.id.navigation_map
            showFragment(m_homeFragment)
        } else {
            super.onBackPressed()
        }
    }



    private fun initBottomNav() {
        binding.navigationOwner.itemIconTintList = null

        binding.navigationOwner.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.navigation_m_store -> {
                    showFragment(m_storeFragment)
                    true
                }

                R.id.navigation_m_home -> {
                    showFragment(m_homeFragment)
                    true
                }

                R.id.navigation_m_setting -> {
                    showFragment(m_settingFragment)
                    true
                }
                else -> false
            }
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
