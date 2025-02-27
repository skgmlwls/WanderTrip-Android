package com.lion.wandertrip.presentation.search_result_page

import SearchItemCategoryChips
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.a02_boardcloneproject.component.CustomDividerComponent
import com.lion.wandertrip.model.TripItemModel
import com.lion.wandertrip.presentation.search_page.SearchViewModel
import com.lion.wandertrip.presentation.search_page.component.HomeSearchBar
import com.lion.wandertrip.presentation.search_result_page.component.MoreButton
import com.lion.wandertrip.presentation.search_result_page.component.SearchItem
import com.lion.wandertrip.util.ContentTypeId
import com.lion.wandertrip.util.MainScreenName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultScreen(
        contentId: String,
        viewModel: SearchResultViewModel = hiltViewModel(),
        searchViewModel: SearchViewModel = hiltViewModel()
    ) {
    var searchInput by remember { mutableStateOf(contentId) }
    var searchQuery by remember { mutableStateOf(contentId) }
    var selectedCategoryCode by remember { mutableStateOf<String?>(null) }

    // ✅ ViewModel에서 검색 결과 가져오기
    val filteredList by viewModel.searchResults.observeAsState(emptyList())

    // ✅ "더보기"를 눌렀을 때 표시할 개수를 저장하는 Map
    val visibleItemCounts = remember { mutableStateMapOf<String, Int>() }

    // ✅ 검색어 변경 시 검색 실행
    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotBlank()) {
            viewModel.searchTrip(searchQuery)
        }
    }

    // ✅ 카테고리 매핑 함수 (Int → Enum → String)
    fun getCategoryName(contentTypeId: String?): String {
        return ContentTypeId.values()
            .find { it.contentTypeCode.toString() == contentTypeId }?.contentTypeName ?: "기타"
    }

    // ✅ 카테고리별 분류 (Enum 값의 `contentTypeName` 기준)
    val categorizedResults = if (selectedCategoryCode == "추천" || selectedCategoryCode == null) {
        filteredList.groupBy { getCategoryName(it.contentTypeId) }
    } else {
        filteredList
            .filter { getCategoryName(it.contentTypeId) == selectedCategoryCode }
            .groupBy { getCategoryName(it.contentTypeId) }
    }

    Scaffold(containerColor = Color.White) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            // ✅ 검색 바
            HomeSearchBar(
                query = searchInput,
                onSearchQueryChanged = { newValue -> searchInput = newValue },
                onSearchClicked = {
                    if (searchInput.isNotBlank()) {
                        searchQuery = searchInput
                        val searchItem = TripItemModel(title = searchInput)
                        searchViewModel.addSearchToRecent(searchItem)
                        searchViewModel.onClickToResult(searchInput)
                    }
                },
                onClearQuery = { searchInput = "" },
                onBackClicked = { viewModel.onNavigateBackToSearchScreen() }
            )


            // ✅ 카테고리 필터
            SearchItemCategoryChips(
                selectedCategoryCode = selectedCategoryCode,
                onCategorySelected = { selectedCategoryCode = it }
            )

            // ✅ 검색 결과 리스트
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val requiredCategories = listOf("관광지", "숙박", "음식점")

                requiredCategories.forEach { category ->
                    val itemsForCategory = categorizedResults[category] ?: emptyList()

                    // ✅ 표시할 개수 (초기값: 3, "더보기" 클릭 시 증가)
                    val visibleCount = visibleItemCounts[category] ?: 3

                    item {
                        Text(
                            text = category,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    if (itemsForCategory.isNotEmpty()) {
                        items(itemsForCategory.take(visibleCount)) { tripItem ->
                            SearchItem(
                                tripItem = tripItem,
                                onItemClick = { searchViewModel.onClickToResult(tripItem.title) }
                            )
                            CustomDividerComponent(10.dp)
                        }
                    } else {
                        item { NoResultsMessage(category) }
                    }

                    // ✅ "더보기" 버튼 추가 (현재 표시 개수 < 전체 개수일 때)
                    if (visibleCount < itemsForCategory.size) {
                        item {
                            MoreButton(category = category) {
                                visibleItemCounts[category] = visibleCount + 3 // ✅ 3개씩 추가
                            }
                        }
                    }

                    // ✅ 마지막 카테고리가 아니라면 구분선 추가
                    if (category != requiredCategories.last()) {
                        item { CustomDividerComponent(16.dp) }
                    }
                }
            }
        }
    }
}
@Composable
fun NoResultsMessage(category: String) {
    val message = when (category) {
        "맛집" -> "맛집이 없습니다."
        "여행기" -> "여행기가 없습니다."
        "관광지" -> "관광지가 없습니다."
        "숙소" -> "숙소가 없습니다."
        else -> "결과가 없습니다."
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}