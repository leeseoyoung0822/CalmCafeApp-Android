package com.example.calmcafeapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.calmcafeapp.databinding.ActivityMainBinding
import com.example.calmcafeapp.ui.HomeFragment
import com.example.calmcafeapp.ui.RankFragment
import com.example.calmcafeapp.ui.SettingFragment


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showInit()
        initBottomNav()

//        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.navigationMain.selectedItemId = R.id.navigation_map


    }

    private fun showInit() {
        val transaction = supportFragmentManager.beginTransaction()
            .add(R.id.container_main, HomeFragment())
        transaction.commit()
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