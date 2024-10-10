package com.example.calmcafeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.calmcafeapp.R
import com.example.calmcafeapp.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    // ViewBinding을 위한 변수
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // ViewBinding 초기화
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 즐겨찾기 섹션 클릭 시 FavoriteFragment로 이동
        binding.favoritesSection.setOnClickListener {
            val favoriteFragment = FavoriteFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, favoriteFragment)
                .addToBackStack(null)
                .commit()
        }

        // 쿠폰 섹션 클릭 시 CouponFragment로 이동
        binding.mycoupon.setOnClickListener {
            val couponFragment = CouponFragment() // 쿠폰 프래그먼트로 전환
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, couponFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 메모리 누수를 방지하기 위해 바인딩 해제
    }

//    companion object {
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            SettingFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}
