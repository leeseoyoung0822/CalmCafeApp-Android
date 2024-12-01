package com.example.calmcafeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.calmcafeapp.R
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.databinding.FragmentMDataCongestionBinding
import com.example.calmcafeapp.databinding.FragmentMDataVistorBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [M_DataCongestionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class M_DataCongestionFragment : BaseFragment<FragmentMDataCongestionBinding>(R.layout.fragment_m__data_congestion) {
    override fun initStartView() {
        // 초기화 로직 (현재는 필요 없음)
    }

    override fun initDataBinding() {
        // 데이터 바인딩 (현재는 필요 없음)
    }

    override fun initAfterBinding() {
        // 추가 설정 (현재는 필요 없음)
    }
}