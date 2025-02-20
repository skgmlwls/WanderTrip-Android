package com.lion.wandertrip.presentation.search_page

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SearchViewModel : ViewModel() {
    private val _selectedRecentSearch = MutableStateFlow<String?>(null)
    val selectedRecentSearch: StateFlow<String?> = _selectedRecentSearch

    private val _recentSearches = MutableStateFlow<List<String>>(emptyList()) // ✅ 최근 검색어 리스트 추가
    val recentSearches: StateFlow<List<String>> = _recentSearches

    // ✅ 도시 선택 시 최근 검색어에 추가
    fun selectRecentSearch(city: String) {
        _selectedRecentSearch.value = city

        // ✅ 중복 제거 후 최근 검색어 업데이트
        _recentSearches.value = listOf(city) + _recentSearches.value.filter { it != city }
    }

    // ✅ 검색어 추가 (중복 제거 및 최신순 정렬)
    fun addRecentSearch(city: String) {
        _recentSearches.value = listOf(city) + _recentSearches.value.filter { it != city }
    }

    // ✅ 특정 검색어 삭제
    fun removeRecentSearch(city: String) {
        _recentSearches.value = _recentSearches.value.filter { it != city }
    }

    // ✅ 모든 검색어 삭제
    fun clearRecentSearches() {
        _recentSearches.value = emptyList()
    }

    // ✅ 선택된 도시 초기화 (뒤로 가기 시 호출)
    fun clearSelectedRecentSearch() {
        _selectedRecentSearch.value = null
    }

    fun backScreen() {
        _selectedRecentSearch.value = null // 뒤로 가기 클릭 시 검색 화면으로 돌아가기
    }
}
