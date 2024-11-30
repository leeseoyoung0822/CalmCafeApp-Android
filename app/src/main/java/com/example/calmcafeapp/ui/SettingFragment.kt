package com.example.calmcafeapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.calmcafeapp.JoinActivity
import com.example.calmcafeapp.R
import com.example.calmcafeapp.UserActivity
import com.example.calmcafeapp.apiManager.ApiManager
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.data.SurveyResponse
import com.example.calmcafeapp.data.UserProfile
import com.example.calmcafeapp.databinding.FragmentSettingBinding
import com.example.calmcafeapp.login.LocalDataSource
import com.example.calmcafeapp.viewmodel.SettingViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SettingFragment : BaseFragment<FragmentSettingBinding>(R.layout.fragment_setting) {

    private val settingViewModel: SettingViewModel by activityViewModels()
    private val settingService = ApiManager.settingService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 필요한 초기화 코드가 있다면 여기에 작성
    }

    override fun initStartView() {
        super.initStartView()
        // 시작 뷰 초기화 작업 (ex. 리사이클러뷰 초기화)
        (activity as UserActivity).binding.navigationUser.visibility = View.VISIBLE
        (activity as UserActivity).binding.btnBack.visibility = View.GONE

        // ViewModel에서 사용자 프로필 데이터를 가져옴
        settingViewModel.fetchUserProfile()

        // 프로필 데이터 관찰
        settingViewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            updateUI(profile)
        }

        // 에러 메시지 관찰
        settingViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun initDataBinding() {
        super.initDataBinding()
        // 데이터 바인딩 설정을 여기에 구현

    }

    override fun initAfterBinding() {
        super.initAfterBinding()

        // 즐겨찾기 섹션 클릭 시 FavoriteFragment로 이동
        binding.favoriteMenu.setOnClickListener {
            val favoriteFragment = FavoriteFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.container_main, favoriteFragment)
                .addToBackStack(null)
                .commit()
        }

        // 쿠폰 섹션 클릭 시 CouponFragment로 이동
        binding.couponMenu.setOnClickListener {
            val couponFragment = Setting_CouponFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.container_main, couponFragment)
                .addToBackStack(null)
                .commit()
        }

        // 쿠폰 섹션 클릭 시 CouponFragment로 이동
        binding.surveyMenu.setOnClickListener {
            val surveyFragment = SurveyFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.container_main, surveyFragment)
                .addToBackStack(null)
                .commit()

        }

        binding.logoutBtn.setOnClickListener {
            val accessToken = LocalDataSource.getAccessToken() ?: ""

            if (accessToken.isNotEmpty()) {
                // 로그아웃 API 호출
                settingService.logout("Bearer $accessToken")
                    .enqueue(object : Callback<SurveyResponse> {
                        override fun onResponse(call: Call<SurveyResponse>, response: Response<SurveyResponse>) {
                            if (response.isSuccessful && response.body()?.isSuccess == true) {
                                // 로그아웃 성공
                                Toast.makeText(context, "로그아웃되었습니다.", Toast.LENGTH_SHORT).show()

                                // 로컬 저장소 초기화
                                LocalDataSource.clear()

                                // JoinActivity로 이동
                                val intent = Intent(requireContext(), JoinActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                            } else {
                                // 로그아웃 실패 처리
                                Toast.makeText(context, "로그아웃에 실패했습니다.", Toast.LENGTH_SHORT).show()
                                Log.e("Logout", "Error: ${response.errorBody()?.string()}")
                            }
                        }

                        override fun onFailure(call: Call<SurveyResponse>, t: Throwable) {
                            // 네트워크 오류 처리
                            Toast.makeText(context, "네트워크 오류로 로그아웃에 실패했습니다.", Toast.LENGTH_SHORT).show()
                            Log.e("Logout", "Network error: ${t.message}")
                        }
                    })
            } else {
                Toast.makeText(context, "로그인 상태가 아닙니다.", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun updateUI(profile: UserProfile) {
        binding.profileName.text = "${profile.nickname}님"
        binding.profilePoint.text = "보유 포인트 : ${profile.point} P"
        // 프로필 이미지가 비어있으면 기본 이미지 사용
        if (profile.profileImage.isNullOrEmpty()) {
            binding.profileImg.setImageResource(R.drawable.profile) // 기본 이미지 설정
        } else {
            Glide.with(this)
                .load(profile.profileImage) // 프로필 이미지 로드
                .placeholder(R.drawable.profile) // 로드 중 기본 이미지
                .error(R.drawable.profile) // 로드 실패 시 기본 이미지
                .into(binding.profileImg)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // BaseFragment에서는 binding 해제를 처리하지 않아도 됨
    }
}