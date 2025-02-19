package com.lion.wandertrip.presentation.search_page

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SearchViewModel : ViewModel() {
    private val _selectedCity = MutableStateFlow<String?>(null)
    val selectedCity: StateFlow<String?> = _selectedCity

    fun selectCity(contentId: String) {
        _selectedCity.value = contentId // 컨텐츠 ID 선택 시 상세 화면으로 이동
    }

    fun backScreen() {
        _selectedCity.value = null // 뒤로 가기 클릭 시 검색 화면으로 돌아가기
    }
}
