package com.example.calmcafeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.calmcafeapp.data.CafeData
import com.example.calmcafeapp.databinding.FragmentRankBinding
import com.example.calmcafeapp.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class RankFragment : Fragment() {

    private lateinit var binding: FragmentRankBinding
    private lateinit var adapter: CafeRecyclerViewAdapter // 어댑터 객체 선언
    private val regions = listOf(
        "전국", "서울", "경기",
        "인천", "제주", "부산",
        "대구", "광주", "대전",
        "울산", "경상", "전라",
        "강원", "충청", "세종")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRankBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCafeRecyclerView()

        setupTableLayout()

        // 디폴트로 '실시간 방문자 수' 버튼 클릭 처리
        loadData("전국","실시간 방문자 수")
        setButtonSelected(binding.btn1)
        binding.categoryTitle.text = "실시간 방문자 수 TOP 랭킹"

        binding.btn1.setOnClickListener{
            val selectedRegion = getCurrentRegion()
            loadData(selectedRegion, "실시간 방문자 수")
            setButtonSelected(binding.btn1)
            binding.categoryTitle.text = "실시간 방문자 수 TOP 랭킹" // 초기 텍스트 설정  버튼1을 선택 상태로
        }
        binding.btn2.setOnClickListener {
            val selectedRegion = getCurrentRegion()
            loadData(selectedRegion, "누적 방문자 수") // 버튼2 클릭 시 데이터 변경
            setButtonSelected(binding.btn2) // 버튼2을 선택 상태로
            binding.categoryTitle.text = "누적 방문자 수 TOP 랭킹"
        }

        binding.btn3.setOnClickListener {
            val selectedRegion = getCurrentRegion()
            loadData(selectedRegion, "즐겨찾기") // 버튼3 클릭 시 데이터 변경
            setButtonSelected(binding.btn3) // 버튼3을 선택 상태로
            binding.categoryTitle.text = "즐겨찾기 TOP 랭킹"
        }

        /*
        binding.searchBtn.setOnClickListener {
            // SearchFragment로 이동
            val searchFragment = SearchFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.container_main, searchFragment)
                .addToBackStack(null)  // 뒤로 가기 버튼으로 돌아올 수 있게 백스택에 추가
                .commit()

        }*/
    }
    // RecyclerView 초기화 함수
    private fun initCafeRecyclerView() {
        adapter = CafeRecyclerViewAdapter(mutableListOf()) // 어댑터 객체 생성
        binding.cafeRecyclerView.adapter = adapter // 리사이클러뷰에 어댑터 연결
        binding.cafeRecyclerView.layoutManager = LinearLayoutManager(requireContext()) // 레이아웃 매니저 연결
    }

    // TabLayout 설정 및 지역 선택 시 데이터를 로드하는 함수
    private fun setupTableLayout() {
        binding.tabLayout.apply {
            regions.forEach { region ->
                addTab(newTab().setText(region))
            }

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {
                        val selectedRegion = it.text.toString()
                        val selectedButton = getSelectedButton()
                        loadData(selectedRegion, selectedButton)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
    }



    // 지역과 버튼에 따라 다른 카페 데이터를 로드하는 함수
    private fun loadData(region: String, buttonType: String) {
        val data = when (region) {
            "서울" -> getSeoulData(buttonType)
            "경기" -> getGyeonggiData(buttonType)
            "인천" -> getIncheonData(buttonType)
            "제주"-> getJejuData(buttonType)
            "부산" -> getBusanData(buttonType)
            "대구"-> getDaeguData(buttonType)
            "광주" -> getGwangjuData(buttonType)
            "대전"->getDaejeonData(buttonType)
            "울산"->getUlsanData(buttonType)
            "경상"->getGyeongsangData(buttonType)
            "전라"->getJeollaData(buttonType)
            "강원"->getGangwonData(buttonType)
            "충청"->getChungcheongData(buttonType)
            "세종"->getSejongData(buttonType)
            else -> getDefaultData(buttonType) // 기본값은 전국
        }
        adapter.updateData(data)

        // 스크롤 위치를 맨 위로 리셋
        binding.cafeRecyclerView.scrollToPosition(0)
    }


    // '서울' 지역의 버튼에 맞는 데이터 제공
    private fun getSeoulData(buttonType: String): List<CafeData> {
        return when (buttonType) {
            "실시간 방문자 수" -> listOf(
                CafeData(R.drawable.starbucks, "스타벅스 서울점", "보통"),
                CafeData(R.drawable.bback, "빽다방 서울점", "여유"),
                CafeData(R.drawable.megacoffee, "메가커피 서울점", "혼잡"),
                CafeData(R.drawable.hollys, "할리스커피 서울점", "보통"),
                CafeData(R.drawable.ediya, "이디야커피 서울점", "혼잡"),
                CafeData(R.drawable.theventi, "카페베네 서울점", "여유")
            )
            "누적 방문자 수" -> listOf(
                CafeData(R.drawable.hollys, "할리스커피 서울점", "보통"),
                CafeData(R.drawable.ediya, "이디야커피 서울점", "여유"),
                CafeData(R.drawable.theventi, "카페베네 서울점", "혼잡"),
                CafeData(R.drawable.starbucks, "커피빈 서울점", "혼잡"),
                CafeData(R.drawable.bback, "백미당 서울점", "보통"),
                CafeData(R.drawable.megacoffee, "투썸플레이스 서울점", "여유")
            )
            "즐겨찾기" -> listOf(
                CafeData(R.drawable.theventi, "탐앤탐스 서울점", "보통"),
                CafeData(R.drawable.megacoffee, "더벤티 서울점", "여유"),
                CafeData(R.drawable.bback, "드롭탑 서울점", "혼잡"),
                CafeData(R.drawable.hollys, "할리스커피 서울점", "혼잡"),
                CafeData(R.drawable.ediya, "카페베네 서울점", "여유"),
                CafeData(R.drawable.starbucks, "이디야커피 서울점", "보통")
            )
            else -> emptyList()
        }
    }

    // '경기' 지역의 버튼에 맞는 데이터 제공
    private fun getGyeonggiData(buttonType: String): List<CafeData> {
        return when (buttonType) {
            "실시간 방문자 수" -> listOf(
                CafeData(R.drawable.ediya, "스타벅스 경기점", "보통"),
                CafeData(R.drawable.bback, "빽다방 경기점", "여유"),
                CafeData(R.drawable.starbucks, "메가커피 경기점", "혼잡"),
                CafeData(R.drawable.hollys, "할리스커피 경기점", "여유"),
                CafeData(R.drawable.megacoffee, "이디야커피 경기점", "혼잡"),
                CafeData(R.drawable.theventi, "탐앤탐스 경기점", "보통")
            )
            "누적 방문자 수" -> listOf(
                CafeData(R.drawable.starbucks, "할리스커피 경기점", "보통"),
                CafeData(R.drawable.bback, "이디야커피 경기점", "여유"),
                CafeData(R.drawable.hollys, "카페베네 경기점", "혼잡"),
                CafeData(R.drawable.ediya, "커피빈 경기점", "혼잡"),
                CafeData(R.drawable.theventi, "스타벅스 경기점", "보통"),
                CafeData(R.drawable.megacoffee, "할리스커피 경기점", "여유")
            )
            "즐겨찾기" -> listOf(
                CafeData(R.drawable.theventi, "탐앤탐스 경기점", "보통"),
                CafeData(R.drawable.hollys, "더벤티 경기점", "여유"),
                CafeData(R.drawable.megacoffee, "드롭탑 경기점", "혼잡"),
                CafeData(R.drawable.bback, "탐앤탐스 경기점", "보통"),
                CafeData(R.drawable.ediya, "더벤티 경기점", "혼잡"),
                CafeData(R.drawable.starbucks, "드롭탑 경기점", "여유")
            )
            else -> emptyList()
        }
    }

    // '인천' 지역의 버튼에 맞는 데이터 제공
    private fun getIncheonData(buttonType: String): List<CafeData> {
        return when (buttonType) {
            "실시간 방문자 수" -> listOf(
                CafeData(R.drawable.starbucks, "스타벅스 인천점", "보통"),
                CafeData(R.drawable.bback, "빽다방 인천점", "여유"),
                CafeData(R.drawable.megacoffee, "메가커피 인천점", "혼잡"),
                CafeData(R.drawable.hollys, "할리스커피 인천점", "보통"),
                CafeData(R.drawable.ediya, "이디야커피 인천점", "혼잡"),
                CafeData(R.drawable.theventi, "카페베네 인천점", "여유")
            )
            "누적 방문자 수" -> listOf(
                CafeData(R.drawable.hollys, "할리스커피 인천점", "보통"),
                CafeData(R.drawable.ediya, "이디야커피 인천점", "여유"),
                CafeData(R.drawable.theventi, "카페베네 인천점", "혼잡"),
                CafeData(R.drawable.starbucks, "할리스커피 인천점", "보통"),
                CafeData(R.drawable.bback, "이디야커피 인천점", "혼잡"),
                CafeData(R.drawable.megacoffee, "카페베네 인천점", "여유")
            )
            "즐겨찾기" -> listOf(
                CafeData(R.drawable.megacoffee, "탐앤탐스 인천점", "보통"),
                CafeData(R.drawable.theventi, "더벤티 인천점", "여유"),
                CafeData(R.drawable.bback, "드롭탑 인천점", "혼잡"),
                CafeData(R.drawable.hollys, "할리스커피 인천점", "혼잡"),
                CafeData(R.drawable.ediya, "카페베네 인천점", "여유"),
                CafeData(R.drawable.starbucks, "이디야커피 인천점", "보통")
            )
            else -> emptyList()
        }
    }

    // 기본 '전국' 데이터를 제공하는 함수
    private fun getDefaultData(buttonType: String): List<CafeData> {
        return when (buttonType) {
            "실시간 방문자 수" -> listOf(
                CafeData(R.drawable.starbucks, "스타벅스 홍대점", "보통"),
                CafeData(R.drawable.bback, "빽다방 제주점", "여유"),
                CafeData(R.drawable.megacoffee, "메가커피 부산점", "혼잡"),
                CafeData(R.drawable.hollys, "스타벅스 합정점", "보통"),
                CafeData(R.drawable.ediya, "빽다방 연남점", "여유"),
                CafeData(R.drawable.theventi, "메가커피 연남점", "혼잡")
            )
            "누적 방문자 수" -> listOf(
                CafeData(R.drawable.hollys, "할리스커피 연남점", "보통"),
                CafeData(R.drawable.ediya, "이디야커피 연남점", "여유"),
                CafeData(R.drawable.theventi, "카페베네 연남점", "혼잡"),
                CafeData(R.drawable.starbucks, "스타벅스 연남점", "보통"),
                CafeData(R.drawable.bback, "빽다방 연남점", "여유"),
                CafeData(R.drawable.megacoffee, "메가커피 연남점", "혼잡")
            )
            "즐겨찾기" -> listOf(
                CafeData(R.drawable.theventi, "탐앤탐스 연남점", "보통"),
                CafeData(R.drawable.hollys, "더벤티 연남점", "여유"),
                CafeData(R.drawable.bback, "드롭탑 연남점", "혼잡"),
                CafeData(R.drawable.starbucks, "스타벅스 연남점", "보통"),
                CafeData(R.drawable.ediya, "빽다방 연남점", "여유"),
                CafeData(R.drawable.megacoffee, "메가커피 연남점", "혼잡")
            )
            else -> emptyList()
        }
    }

    // '제주' 지역의 버튼에 맞는 데이터 제공
    private fun getJejuData(buttonType: String): List<CafeData> {
        return when (buttonType) {
            "실시간 방문자 수" -> listOf(
                CafeData(R.drawable.starbucks, "스타벅스 제주점", "보통"),
                CafeData(R.drawable.bback, "빽다방 제주점", "여유"),
                CafeData(R.drawable.megacoffee, "메가커피 제주점", "혼잡"),
                CafeData(R.drawable.hollys, "할리스커피 제주점", "보통"),
                CafeData(R.drawable.ediya, "이디야커피 제주점", "혼잡"),
                CafeData(R.drawable.theventi, "카페베네 제주점", "여유")
            )
            "누적 방문자 수" -> listOf(
                CafeData(R.drawable.hollys, "탐앤탐스 제주점", "보통"),
                CafeData(R.drawable.theventi, "더벤티 제주점", "혼잡"),
                CafeData(R.drawable.bback, "드롭탑 제주점", "여유"),
                CafeData(R.drawable.starbucks, "백미당 제주점", "혼잡"),
                CafeData(R.drawable.megacoffee, "커피빈 제주점", "보통"),
                CafeData(R.drawable.ediya, "투썸플레이스 제주점", "여유")
            )
            "즐겨찾기" -> listOf(
                CafeData(R.drawable.starbucks, "스타벅스 제주점", "혼잡"),
                CafeData(R.drawable.bback, "빽다방 제주점", "여유"),
                CafeData(R.drawable.megacoffee, "메가커피 제주점", "보통"),
                CafeData(R.drawable.hollys, "할리스커피 제주점", "혼잡"),
                CafeData(R.drawable.theventi, "카페베네 제주점", "여유"),
                CafeData(R.drawable.ediya, "이디야커피 제주점", "보통")
            )
            else -> emptyList()
        }
    }

    // '부산' 지역의 버튼에 맞는 데이터 제공
    private fun getBusanData(buttonType: String): List<CafeData> {
        return when (buttonType) {
            "실시간 방문자 수" -> listOf(
                CafeData(R.drawable.starbucks, "스타벅스 부산점", "보통"),
                CafeData(R.drawable.bback, "빽다방 부산점", "혼잡"),
                CafeData(R.drawable.megacoffee, "메가커피 부산점", "여유"),
                CafeData(R.drawable.hollys, "할리스커피 부산점", "보통"),
                CafeData(R.drawable.ediya, "이디야커피 부산점", "혼잡"),
                CafeData(R.drawable.theventi, "카페베네 부산점", "여유")
            )
            "누적 방문자 수" -> listOf(
                CafeData(R.drawable.hollys, "탐앤탐스 부산점", "보통"),
                CafeData(R.drawable.theventi, "더벤티 부산점", "혼잡"),
                CafeData(R.drawable.bback, "드롭탑 부산점", "여유"),
                CafeData(R.drawable.starbucks, "백미당 부산점", "혼잡"),
                CafeData(R.drawable.megacoffee, "커피빈 부산점", "보통"),
                CafeData(R.drawable.ediya, "투썸플레이스 부산점", "여유")
            )
            "즐겨찾기" -> listOf(
                CafeData(R.drawable.starbucks, "스타벅스 부산점", "혼잡"),
                CafeData(R.drawable.bback, "빽다방 부산점", "여유"),
                CafeData(R.drawable.megacoffee, "메가커피 부산점", "보통"),
                CafeData(R.drawable.hollys, "할리스커피 부산점", "혼잡"),
                CafeData(R.drawable.theventi, "카페베네 부산점", "여유"),
                CafeData(R.drawable.ediya, "이디야커피 부산점", "보통")
            )
            else -> emptyList()
        }
    }

    // '대구' 지역의 버튼에 맞는 데이터 제공
    private fun getDaeguData(buttonType: String): List<CafeData> {
        return when (buttonType) {
            "실시간 방문자 수" -> listOf(
                CafeData(R.drawable.starbucks, "스타벅스 대구점", "보통"),
                CafeData(R.drawable.bback, "빽다방 대구점", "혼잡"),
                CafeData(R.drawable.megacoffee, "메가커피 대구점", "여유"),
                CafeData(R.drawable.hollys, "할리스커피 대구점", "보통"),
                CafeData(R.drawable.ediya, "이디야커피 대구점", "혼잡"),
                CafeData(R.drawable.theventi, "카페베네 대구점", "여유")
            )
            "누적 방문자 수" -> listOf(
                CafeData(R.drawable.hollys, "탐앤탐스 대구점", "보통"),
                CafeData(R.drawable.theventi, "더벤티 대구점", "혼잡"),
                CafeData(R.drawable.bback, "드롭탑 대구점", "여유"),
                CafeData(R.drawable.starbucks, "백미당 대구점", "혼잡"),
                CafeData(R.drawable.megacoffee, "커피빈 대구점", "보통"),
                CafeData(R.drawable.ediya, "투썸플레이스 대구점", "여유")
            )
            "즐겨찾기" -> listOf(
                CafeData(R.drawable.starbucks, "스타벅스 대구점", "혼잡"),
                CafeData(R.drawable.bback, "빽다방 대구점", "여유"),
                CafeData(R.drawable.megacoffee, "메가커피 대구점", "보통"),
                CafeData(R.drawable.hollys, "할리스커피 대구점", "혼잡"),
                CafeData(R.drawable.theventi, "카페베네 대구점", "여유"),
                CafeData(R.drawable.ediya, "이디야커피 대구점", "보통")
            )
            else -> emptyList()
        }
    }

    // '광주' 지역의 버튼에 맞는 데이터 제공
    private fun getGwangjuData(buttonType: String): List<CafeData> {
        return when (buttonType) {
            "실시간 방문자 수" -> listOf(
                CafeData(R.drawable.starbucks, "스타벅스 광주점", "보통"),
                CafeData(R.drawable.bback, "빽다방 광주점", "혼잡"),
                CafeData(R.drawable.megacoffee, "메가커피 광주점", "여유"),
                CafeData(R.drawable.hollys, "할리스커피 광주점", "보통"),
                CafeData(R.drawable.ediya, "이디야커피 광주점", "혼잡"),
                CafeData(R.drawable.theventi, "카페베네 광주점", "여유")
            )
            "누적 방문자 수" -> listOf(
                CafeData(R.drawable.hollys, "탐앤탐스 광주점", "보통"),
                CafeData(R.drawable.theventi, "더벤티 광주점", "혼잡"),
                CafeData(R.drawable.bback, "드롭탑 광주점", "여유"),
                CafeData(R.drawable.starbucks, "백미당 광주점", "혼잡"),
                CafeData(R.drawable.megacoffee, "커피빈 광주점", "보통"),
                CafeData(R.drawable.ediya, "투썸플레이스 광주점", "여유")
            )
            "즐겨찾기" -> listOf(
                CafeData(R.drawable.starbucks, "스타벅스 광주점", "혼잡"),
                CafeData(R.drawable.bback, "빽다방 광주점", "여유"),
                CafeData(R.drawable.megacoffee, "메가커피 광주점", "보통"),
                CafeData(R.drawable.hollys, "할리스커피 광주점", "혼잡"),
                CafeData(R.drawable.theventi, "카페베네 광주점", "여유"),
                CafeData(R.drawable.ediya, "이디야커피 광주점", "보통")
            )
            else -> emptyList()
        }
    }

    // '대전' 지역의 버튼에 맞는 데이터 제공
    private fun getDaejeonData(buttonType: String): List<CafeData> {
        return when (buttonType) {
            "실시간 방문자 수" -> listOf(
                CafeData(R.drawable.starbucks, "스타벅스 대전점", "보통"),
                CafeData(R.drawable.bback, "빽다방 대전점", "혼잡"),
                CafeData(R.drawable.megacoffee, "메가커피 대전점", "여유"),
                CafeData(R.drawable.hollys, "할리스커피 대전점", "보통"),
                CafeData(R.drawable.ediya, "이디야커피 대전점", "혼잡"),
                CafeData(R.drawable.theventi, "카페베네 대전점", "여유")
            )
            "누적 방문자 수" -> listOf(
                CafeData(R.drawable.hollys, "탐앤탐스 대전점", "보통"),
                CafeData(R.drawable.theventi, "더벤티 대전점", "혼잡"),
                CafeData(R.drawable.bback, "드롭탑 대전점", "여유"),
                CafeData(R.drawable.starbucks, "백미당 대전점", "혼잡"),
                CafeData(R.drawable.megacoffee, "커피빈 대전점", "보통"),
                CafeData(R.drawable.ediya, "투썸플레이스 대전점", "여유")
            )
            "즐겨찾기" -> listOf(
                CafeData(R.drawable.starbucks, "스타벅스 대전점", "혼잡"),
                CafeData(R.drawable.bback, "빽다방 대전점", "여유"),
                CafeData(R.drawable.megacoffee, "메가커피 대전점", "보통"),
                CafeData(R.drawable.hollys, "할리스커피 대전점", "혼잡"),
                CafeData(R.drawable.theventi, "카페베네 대전점", "여유"),
                CafeData(R.drawable.ediya, "이디야커피 대전점", "보통")
            )
            else -> emptyList()
        }
    }

    // '울산' 지역의 버튼에 맞는 데이터 제공
    private fun getUlsanData(buttonType: String): List<CafeData> {
        return when (buttonType) {
            "실시간 방문자 수" -> listOf(
                CafeData(R.drawable.starbucks, "스타벅스 울산점", "보통"),
                CafeData(R.drawable.bback, "빽다방 울산점", "혼잡"),
                CafeData(R.drawable.megacoffee, "메가커피 울산점", "여유"),
                CafeData(R.drawable.hollys, "할리스커피 울산점", "보통"),
                CafeData(R.drawable.ediya, "이디야커피 울산점", "혼잡"),
                CafeData(R.drawable.theventi, "카페베네 울산점", "여유")
            )
            "누적 방문자 수" -> listOf(
                CafeData(R.drawable.hollys, "탐앤탐스 울산점", "보통"),
                CafeData(R.drawable.theventi, "더벤티 울산점", "혼잡"),
                CafeData(R.drawable.bback, "드롭탑 울산점", "여유"),
                CafeData(R.drawable.starbucks, "백미당 울산점", "혼잡"),
                CafeData(R.drawable.megacoffee, "커피빈 울산점", "보통"),
                CafeData(R.drawable.ediya, "투썸플레이스 울산점", "여유")
            )
            "즐겨찾기" -> listOf(
                CafeData(R.drawable.starbucks, "스타벅스 울산점", "혼잡"),
                CafeData(R.drawable.bback, "빽다방 울산점", "여유"),
                CafeData(R.drawable.megacoffee, "메가커피 울산점", "보통"),
                CafeData(R.drawable.hollys, "할리스커피 울산점", "혼잡"),
                CafeData(R.drawable.theventi, "카페베네 울산점", "여유"),
                CafeData(R.drawable.ediya, "이디야커피 울산점", "보통")
            )
            else -> emptyList()
        }
    }

    // '경상' 지역의 버튼에 맞는 데이터 제공
    private fun getGyeongsangData(buttonType: String): List<CafeData> {
        return when (buttonType) {
            "실시간 방문자 수" -> listOf(
                CafeData(R.drawable.starbucks, "스타벅스 경상점", "보통"),
                CafeData(R.drawable.bback, "빽다방 경상점", "혼잡"),
                CafeData(R.drawable.megacoffee, "메가커피 경상점", "여유"),
                CafeData(R.drawable.hollys, "할리스커피 경상점", "보통"),
                CafeData(R.drawable.ediya, "이디야커피 경상점", "혼잡"),
                CafeData(R.drawable.theventi, "카페베네 경상점", "여유")
            )
            "누적 방문자 수" -> listOf(
                CafeData(R.drawable.hollys, "탐앤탐스 경상점", "보통"),
                CafeData(R.drawable.theventi, "더벤티 경상점", "혼잡"),
                CafeData(R.drawable.bback, "드롭탑 경상점", "여유"),
                CafeData(R.drawable.starbucks, "백미당 경상점", "혼잡"),
                CafeData(R.drawable.megacoffee, "커피빈 경상점", "보통"),
                CafeData(R.drawable.ediya, "투썸플레이스 경상점", "여유")
            )
            "즐겨찾기" -> listOf(
                CafeData(R.drawable.starbucks, "스타벅스 경상점", "혼잡"),
                CafeData(R.drawable.bback, "빽다방 경상점", "여유"),
                CafeData(R.drawable.megacoffee, "메가커피 경상점", "보통"),
                CafeData(R.drawable.hollys, "할리스커피 경상점", "혼잡"),
                CafeData(R.drawable.theventi, "카페베네 경상점", "여유"),
                CafeData(R.drawable.ediya, "이디야커피 경상점", "보통")
            )
            else -> emptyList()
        }
    }

    // '전라' 지역의 버튼에 맞는 데이터 제공
    private fun getJeollaData(buttonType: String): List<CafeData> {
        return when (buttonType) {
            "실시간 방문자 수" -> listOf(
                CafeData(R.drawable.starbucks, "스타벅스 전라점", "보통"),
                CafeData(R.drawable.bback, "빽다방 전라점", "혼잡"),
                CafeData(R.drawable.megacoffee, "메가커피 전라점", "여유"),
                CafeData(R.drawable.hollys, "할리스커피 전라점", "보통"),
                CafeData(R.drawable.ediya, "이디야커피 전라점", "혼잡"),
                CafeData(R.drawable.theventi, "카페베네 전라점", "여유")
            )
            "누적 방문자 수" -> listOf(
                CafeData(R.drawable.hollys, "탐앤탐스 전라점", "보통"),
                CafeData(R.drawable.theventi, "더벤티 전라점", "혼잡"),
                CafeData(R.drawable.bback, "드롭탑 전라점", "여유"),
                CafeData(R.drawable.starbucks, "백미당 전라점", "혼잡"),
                CafeData(R.drawable.megacoffee, "커피빈 전라점", "보통"),
                CafeData(R.drawable.ediya, "투썸플레이스 전라점", "여유")
            )
            "즐겨찾기" -> listOf(
                CafeData(R.drawable.starbucks, "스타벅스 전라점", "혼잡"),
                CafeData(R.drawable.bback, "빽다방 전라점", "여유"),
                CafeData(R.drawable.megacoffee, "메가커피 전라점", "보통"),
                CafeData(R.drawable.hollys, "할리스커피 전라점", "혼잡"),
                CafeData(R.drawable.theventi, "카페베네 전라점", "여유"),
                CafeData(R.drawable.ediya, "이디야커피 전라점", "보통")
            )
            else -> emptyList()
        }
    }

    // '강원' 지역의 버튼에 맞는 데이터 제공
    private fun getGangwonData(buttonType: String): List<CafeData> {
        return when (buttonType) {
            "실시간 방문자 수" -> listOf(
                CafeData(R.drawable.starbucks, "스타벅스 강원점", "보통"),
                CafeData(R.drawable.bback, "빽다방 강원점", "혼잡"),
                CafeData(R.drawable.megacoffee, "메가커피 강원점", "여유"),
                CafeData(R.drawable.hollys, "할리스커피 강원점", "보통"),
                CafeData(R.drawable.ediya, "이디야커피 강원점", "혼잡"),
                CafeData(R.drawable.theventi, "카페베네 강원점", "여유")
            )
            "누적 방문자 수" -> listOf(
                CafeData(R.drawable.hollys, "탐앤탐스 강원점", "보통"),
                CafeData(R.drawable.theventi, "더벤티 강원점", "혼잡"),
                CafeData(R.drawable.bback, "드롭탑 강원점", "여유"),
                CafeData(R.drawable.starbucks, "백미당 강원점", "혼잡"),
                CafeData(R.drawable.megacoffee, "커피빈 강원점", "보통"),
                CafeData(R.drawable.ediya, "투썸플레이스 강원점", "여유")
            )
            "즐겨찾기" -> listOf(
                CafeData(R.drawable.starbucks, "스타벅스 강원점", "혼잡"),
                CafeData(R.drawable.bback, "빽다방 강원점", "여유"),
                CafeData(R.drawable.megacoffee, "메가커피 강원점", "보통"),
                CafeData(R.drawable.hollys, "할리스커피 강원점", "혼잡"),
                CafeData(R.drawable.theventi, "카페베네 강원점", "여유"),
                CafeData(R.drawable.ediya, "이디야커피 강원점", "보통")
            )
            else -> emptyList()
        }
    }

    // '충청' 지역의 버튼에 맞는 데이터 제공
    private fun getChungcheongData(buttonType: String): List<CafeData> {
        return when (buttonType) {
            "실시간 방문자 수" -> listOf(
                CafeData(R.drawable.starbucks, "스타벅스 충청점", "보통"),
                CafeData(R.drawable.bback, "빽다방 충청점", "혼잡"),
                CafeData(R.drawable.megacoffee, "메가커피 충청점", "여유"),
                CafeData(R.drawable.hollys, "할리스커피 충청점", "보통"),
                CafeData(R.drawable.ediya, "이디야커피 충청점", "혼잡"),
                CafeData(R.drawable.theventi, "카페베네 충청점", "여유")
            )
            "누적 방문자 수" -> listOf(
                CafeData(R.drawable.hollys, "탐앤탐스 충청점", "보통"),
                CafeData(R.drawable.theventi, "더벤티 충청점", "혼잡"),
                CafeData(R.drawable.bback, "드롭탑 충청점", "여유"),
                CafeData(R.drawable.starbucks, "백미당 충청점", "혼잡"),
                CafeData(R.drawable.megacoffee, "커피빈 충청점", "보통"),
                CafeData(R.drawable.ediya, "투썸플레이스 충청점", "여유")
            )
            "즐겨찾기" -> listOf(
                CafeData(R.drawable.starbucks, "스타벅스 충청점", "혼잡"),
                CafeData(R.drawable.bback, "빽다방 충청점", "여유"),
                CafeData(R.drawable.megacoffee, "메가커피 충청점", "보통"),
                CafeData(R.drawable.hollys, "할리스커피 충청점", "혼잡"),
                CafeData(R.drawable.theventi, "카페베네 충청점", "여유"),
                CafeData(R.drawable.ediya, "이디야커피 충청점", "보통")
            )
            else -> emptyList()
        }
    }

    // '세종' 지역의 버튼에 맞는 데이터 제공
    private fun getSejongData(buttonType: String): List<CafeData> {
        return when (buttonType) {
            "실시간 방문자 수" -> listOf(
                CafeData(R.drawable.starbucks, "스타벅스 세종점", "보통"),
                CafeData(R.drawable.bback, "빽다방 세종점", "혼잡"),
                CafeData(R.drawable.megacoffee, "메가커피 세종점", "여유"),
                CafeData(R.drawable.hollys, "할리스커피 세종점", "보통"),
                CafeData(R.drawable.ediya, "이디야커피 세종점", "혼잡"),
                CafeData(R.drawable.theventi, "카페베네 세종점", "여유")
            )
            "누적 방문자 수" -> listOf(
                CafeData(R.drawable.hollys, "탐앤탐스 세종점", "보통"),
                CafeData(R.drawable.theventi, "더벤티 세종점", "혼잡"),
                CafeData(R.drawable.bback, "드롭탑 세종점", "여유"),
                CafeData(R.drawable.starbucks, "백미당 세종점", "혼잡"),
                CafeData(R.drawable.megacoffee, "커피빈 세종점", "보통"),
                CafeData(R.drawable.ediya, "투썸플레이스 세종점", "여유")
            )
            "즐겨찾기" -> listOf(
                CafeData(R.drawable.starbucks, "스타벅스 세종점", "혼잡"),
                CafeData(R.drawable.bback, "빽다방 세종점", "여유"),
                CafeData(R.drawable.megacoffee, "메가커피 세종점", "보통"),
                CafeData(R.drawable.hollys, "할리스커피 세종점", "혼잡"),
                CafeData(R.drawable.theventi, "카페베네 세종점", "여유"),
                CafeData(R.drawable.ediya, "이디야커피 세종점", "보통")
            )
            else -> emptyList()
        }
    }




    // 선택된 버튼을 반환하는 함수
    private fun getSelectedButton(): String {
        return when {
            binding.btn1.isSelected -> "실시간 방문자 수"
            binding.btn2.isSelected -> "누적 방문자 수"
            binding.btn3.isSelected -> "즐겨찾기"
            else -> "실시간 방문자 수"
        }
    }


    // 선택된 버튼의 선택 상태를 유지하고 나머지 버튼의 선택을 해제하는 함수
    private fun setButtonSelected(selectedButton:Button) {
        // 모든 버튼의 선택 상태 해제
        binding.btn1.isSelected = false
        binding.btn2.isSelected = false
        binding.btn3.isSelected = false

        // 선택된 버튼만 선택 상태로 설정
        selectedButton.isSelected = true
    }

    // 현재 선택된 지역을 반환하는 함수
    private fun getCurrentRegion(): String {
        return binding.tabLayout.getTabAt(binding.tabLayout.selectedTabPosition)?.text.toString()
    }
}