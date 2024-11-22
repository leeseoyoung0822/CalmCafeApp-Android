package com.example.calmcafeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.calmcafeapp.R
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.databinding.FragmentMHomeBinding

class M_HomeFragment : BaseFragment<FragmentMHomeBinding>(R.layout.fragment_m__home) {

    override fun initStartView() {
        super.initStartView()

        // 수정 버튼 클릭 이벤트 설정
        binding.updateBtn.setOnClickListener {
            navigateToUpdateFragment()
        }
    }

    private fun navigateToUpdateFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.container_owner, M_Home_UpdateFragment())
            .addToBackStack(null) // 뒤로 가기 가능하도록 백스택에 추가
            .commit()
    }

    override fun initDataBinding() {
        super.initDataBinding()
    }

    override fun initAfterBinding() {
        super.initAfterBinding()
    }
}