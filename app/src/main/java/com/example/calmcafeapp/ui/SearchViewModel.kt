//package com.example.calmcafeapp.ui
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.example.calmcafeapp.data.CafeData
//
//class SearchViewModel : ViewModel() {
//
//    private val _filteredCafeList = MutableLiveData<List<CafeData>>()
//    val filteredCafeList: LiveData<List<CafeData>> get() = _filteredCafeList
//
//
//    // 더미 데이터 리스트 초기화
//    private val originalList = listOf(
//        CafeData("", "a", "혼잡"),
//        CafeData("", "b", "여유"),
//        CafeData("", "c", "보통"),
//        CafeData("", "d", "여유"),
//        CafeData("", "e", "혼잡"),
//        CafeData("", "f", "혼잡"),
//        CafeData("", "g", "여유"),
//        CafeData("", "빽다방", "보통"),
//        CafeData("", "카페베네", "여유"),
//        CafeData("", "이디야", "혼잡"),
//        CafeData("", "Starbucks", "혼잡"),
//        CafeData("", "메가커피", "여유"),
//        CafeData("", "빽다방", "보통"),
//        CafeData("", "카페베네", "여유"),
//        CafeData("", "이디야", "혼잡"),
//
//
//    private val _searchQuery = MutableLiveData<String>()
//    val searchQuery: LiveData<String> get() = _searchQuery
//
//    init {
//        // 초기에는 빈 리스트를 보여줌
//        _filteredCafeList.value = listOf()
//    }
//
//
//    fun setSearchQuery(query: String) {
//        val filteredList = if (query.isBlank()) {
//            listOf() // 검색어가 없을 때는 빈 리스트 반환
//        } else {
//            originalList.filter { it.cafe_name.contains(query, ignoreCase = true) }
//        }
//        _filteredCafeList.value = filteredList
//    }
//
//
//}