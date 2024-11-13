package com.example.calmcafeapp.ui


import android.app.Dialog
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.example.calmcafeapp.R
import com.example.calmcafeapp.UserActivity
import com.example.calmcafeapp.apiManager.ApiManager
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.data.SurveyRequest
import com.example.calmcafeapp.data.SurveyResponse
import com.example.calmcafeapp.databinding.DialogSurveyCompleteBinding
import com.example.calmcafeapp.databinding.FragmentSurveyBinding
import com.example.calmcafeapp.viewmodel.SettingViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SurveyFragment : BaseFragment<FragmentSurveyBinding>(R.layout.fragment_survey) {
    private val settingService = ApiManager.settingService
    private val settingViewModel: SettingViewModel by activityViewModels()


    override fun initStartView() {
        super.initStartView()
        // 시작 뷰 초기화 작업 (ex. 리사이클러뷰 초기화)
        applyHighlightToRequiredText()
    }

    private fun applyHighlightToRequiredText() {
        highlightRequiredText(binding.ageTv, "1. 나이(필수)", "(필수)")
        highlightRequiredText(binding.sexTv, "2. 성별(남/여)(필수)", "(필수)")
        highlightRequiredText(binding.jobTv, "3. 직업(업무 또는 분야)(필수)", "(필수)")
    }

    private fun highlightRequiredText(textView: TextView, fullText: String, highlight: String) {
        val spannable = SpannableString(fullText)
        val start = fullText.indexOf(highlight)
        val end = start + highlight.length
        if (start >= 0) {
            val green = ContextCompat.getColor(requireContext(), R.color.green)
            spannable.setSpan(
                ForegroundColorSpan(green),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            textView.text = spannable
        } else {
            textView.text = fullText
        }
    }

    private fun showSurveyCompleteDialog() {
        // Dialog 초기화 및 바인딩 설정
        val dialogBinding = DialogSurveyCompleteBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext(), R.style.CustomDialogStyle) // 스타일 적용
        dialog.setContentView(dialogBinding.root)
        dialog.setCancelable(false) // 다이얼로그 외부 클릭 방지

        // 다이얼로그의 크기를 조정
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.8).toInt(), // 화면 너비의 90% 설정
            ViewGroup.LayoutParams.WRAP_CONTENT
        )


        // 다이얼로그 메시지 설정 (필요 시 동적 설정 가능)
        dialogBinding.dialogMessage.text = "감사합니다.\n4700p가 지급되었습니다!"

        // 닫기 버튼 클릭 이벤트
        dialogBinding.btncheck.setOnClickListener {
            dialog.dismiss()
            navigateToSettingFragment() // 이전 화면으로 돌아가기
        }

        dialog.show()
    }

    private fun navigateToSettingFragment() {
        parentFragmentManager.popBackStack() // 이전 스택으로 돌아가기
    }



    override fun initDataBinding() {
        super.initDataBinding()
        // 데이터 바인딩 설정을 여기에 구현

        // ViewModel의 LiveData 관찰
        settingViewModel.isSurveySubmitted.observe(viewLifecycleOwner) { isSubmitted ->
            if (isSubmitted) {
                showSurveyCompleteDialog()
                Toast.makeText(context, "설문조사가 제출되었습니다.", Toast.LENGTH_SHORT).show()
                //parentFragmentManager.popBackStack() // 이전 화면으로 돌아가기
            }
        }

        settingViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun initAfterBinding() {
        super.initAfterBinding()

        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.submitButton.setOnClickListener {
            val age = binding.ageInput.text.toString().toIntOrNull()
            val sex = binding.sexInput.text.toString().takeIf { it.isNotBlank() }
            val job = binding.jobInput.text.toString().takeIf { it.isNotBlank() }


            val residence = binding.locationInput.text.toString().takeIf { it.isNotBlank() }?:""
            val marriage = binding.marryInput.text.toString().takeIf { it.isNotBlank() }?:""
            val hobby = binding.hobbyInput.text.toString().takeIf { it.isNotBlank() }?:""
            val favoriteMenu = binding.favoriteMenuInput.text.toString().takeIf { it.isNotBlank() }?:""
            val cafeUsingPurpose = binding.cafePurposeInput.text.toString().takeIf { it.isNotBlank() }?:""
            val cafeChooseCause = binding.cafeCauseInput.text.toString().takeIf { it.isNotBlank() }?:""
            val cafeVisitedFrequency = binding.visitFrequencyInput.text.toString().takeIf { it.isNotBlank() }?:""
            val isUsingSNS = binding.snsUsageInput.text.toString().takeIf { it.isNotBlank() }?:""
            val convenienceFacilityPrefer = binding.facilitiesPreferenceInput.text.toString().takeIf { it.isNotBlank() }?:""


            // 필수 질문 체크 및 특정 입력 오류 메시지 설정
            val missingFields = mutableListOf<String>()
            if (age == null) missingFields.add("나이를 정확히 입력해 주세요")
            if (sex.isNullOrEmpty()) missingFields.add("성별을 정확히 입력해 주세요")
            if (job.isNullOrEmpty()) missingFields.add("직업을 정확히 입력해 주세요")

            // 오류 메시지가 있을 경우 해당 메시지 표시
            if (missingFields.isNotEmpty()) {
                Toast.makeText(context, missingFields.joinToString("\n"), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val surveyRequest = SurveyRequest(
                age = age ?: 0, // 기본값 설정
                sex = sex ?: "",
                job = job ?: "",
                residence = residence,
                marriage = marriage,
                hobby = hobby,
                favoriteMenu = favoriteMenu,
                cafeUsingPurpose = cafeUsingPurpose,
                cafeChooseCause = cafeChooseCause,
                cafeVisitedFrequency = cafeVisitedFrequency,
                isUsingSNS = isUsingSNS,
                convenienceFacilityPrefer = convenienceFacilityPrefer
            )
            settingViewModel.submitSurvey(surveyRequest)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // BaseFragment에서는 binding 해제를 처리하지 않아도 됨
    }
}