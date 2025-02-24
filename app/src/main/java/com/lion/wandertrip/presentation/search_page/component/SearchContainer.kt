package com.lion.wandertrip.presentation.search_page.component

import androidx.compose.runtime.Composable

import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.wandertrip.presentation.search_page.SearchScreen
import com.lion.wandertrip.presentation.search_page.SearchViewModel
import com.lion.wandertrip.presentation.search_result_page.SearchResultScreen

@Composable
fun SearchContainer(
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    // ViewModel의 searchQuery 상태를 가져옴
    val searchQuery = searchViewModel.searchQuery

    if (searchQuery.isBlank()) {
        // 검색어가 없으면 SearchScreen 표시
        SearchScreen(viewModel = searchViewModel)
    } else {
        // 검색어가 있으면 SearchResultScreen 표시
        SearchResultScreen(
            contentId = searchQuery,
            searchViewModel = searchViewModel
        )
    }
}