package com.example.calmcafeapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calmcafeapp.R
import com.example.calmcafeapp.data.CafeData
import com.example.calmcafeapp.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding // ViewBinding 사용
    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var adapter: CafeRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView와 어댑터 초기화
        adapter = CafeRecyclerViewAdapter(listOf())
        binding.searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.searchRecyclerView.adapter = adapter

        // ViewModel에서 필터링된 리스트를 관찰하고 업데이트
        searchViewModel.filteredCafeList.observe(viewLifecycleOwner, Observer { filteredList ->
            adapter.updateData(filteredList)

            // 검색어 입력 중일 때 이미지와 텍스트 가시성 제어
            if (filteredList.isEmpty()) {
                binding.searchRecyclerView.visibility = View.GONE
                binding.searchImg.visibility = View.VISIBLE
                binding.noResultText.visibility = View.VISIBLE
            } else {
                binding.searchRecyclerView.visibility = View.VISIBLE
                binding.searchImg.visibility = View.GONE
                binding.noResultText.visibility = View.GONE
            }
        })

        // SearchView에 포커스 요청 및 소프트 키보드 자동 표시
        binding.searchView.isIconified = false  // SearchView가 자동으로 아이콘화되지 않도록 설정
        binding.searchView.clearFocus()         // 처음에는 포커스를 해제
        binding.searchView.requestFocus()       // SearchView에 포커스를 강제로 줌
        showKeyboard()                          // 소프트 키보드를 자동으로 띄움

        // SearchView에서 텍스트가 변경될 때 ViewModel로 전달
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchViewModel.setSearchQuery(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchViewModel.setSearchQuery(newText ?: "")
                // 검색어가 입력 중이면 이미지와 텍스트를 숨김
                if (newText.isNullOrBlank()) {
                    binding.searchImg.visibility = View.VISIBLE
                    binding.noResultText.visibility = View.VISIBLE
                    binding.searchRecyclerView.visibility = View.GONE
                } else {
                    binding.searchImg.visibility = View.GONE
                    binding.noResultText.visibility = View.GONE
                    binding.searchRecyclerView.visibility = View.VISIBLE
                }

                return true
            }
        })
    }

    // 소프트 키보드 표시 함수
    private fun showKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.searchView, InputMethodManager.SHOW_IMPLICIT)
    }
}
