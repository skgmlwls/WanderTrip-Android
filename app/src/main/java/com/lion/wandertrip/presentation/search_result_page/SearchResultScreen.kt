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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

@Composable
fun SearchResultScreen(
    contentId: String,
    viewModel: SearchResultViewModel = hiltViewModel(),
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    var searchQuery by remember { mutableStateOf(contentId) }
    var selectedCategoryCode by remember { mutableStateOf<String?>(null) }

    val dummyTripList = remember { getDummyTripItems() }

    // 🔹 검색어가 포함된 데이터 필터링
    val filteredList = dummyTripList.filter { it.title.contains(searchQuery, ignoreCase = true) }

    // 🔹 "추천" 선택 시 모든 카테고리를 포함
    val categorizedResults = if (selectedCategoryCode == "추천" || selectedCategoryCode == null) {
        filteredList.groupBy { it.cat2 } // ✅ 모든 카테고리를 표시
    } else {
        filteredList.filter { it.cat2 == selectedCategoryCode }.groupBy { it.cat2 }
    }

    // ✅ "맛집", "여행기" 등 특정 카테고리가 없으면 빈 메시지 표시
    val requiredCategories = listOf("관광지", "숙소", "맛집", "여행기")

    Scaffold(containerColor = Color.White) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            // 🔹 검색 바
            HomeSearchBar(
                query = searchQuery,
                onSearchQueryChanged = { searchQuery = it },
                onSearchClicked = {
                    if (searchQuery.isNotBlank()) {
                        val searchItem = TripItemModel(title = searchQuery)
                        searchViewModel.addSearchToRecent(searchItem)
                        searchViewModel.onClickToResult(searchQuery)
                    }
                },
                onClearQuery = { searchQuery = "" },
                onBackClicked = { viewModel.onClickNavIconBack() }
            )

            // 🔹 카테고리 칩 (고정된 5개 카테고리 사용)
            SearchItemCategoryChips(
                selectedCategoryCode = selectedCategoryCode,
                onCategorySelected = { selectedCategoryCode = it }
            )

            // 🔹 검색 결과 리스트
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 🔹 검색된 카테고리 표시
                categorizedResults.forEach { (category, items) ->
                    item {
                        Text(
                            text = category,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    // 🔹 개별 `items()`로 처리
                    items(items) { tripItem ->
                        SearchItem(
                            tripItem = tripItem,
                            onItemClick = { searchViewModel.onClickToResult(tripItem.title) }
                        )
                        CustomDividerComponent(10.dp)
                    }

                    // 🔹 "더보기" 버튼 추가
                    item {
                        MoreButton(category = category)
                    }
                }

                // ✅ 특정 카테고리가 없으면 "없음" 메시지 표시
                requiredCategories.forEach { category ->
                    if (!categorizedResults.containsKey(category)) {
                        item {
                            NoResultsMessage(category)
                        }
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

fun getDummyTripItems(): List<TripItemModel> {
    return listOf(
        TripItemModel(title = "서울 남산타워", cat2 = "관광지", cat3 = "랜드마크"),
        TripItemModel(title = "제주 성산일출봉", cat2 = "관광지", cat3 = "자연경관"),
        TripItemModel(title = "부산 해운대 해수욕장", cat2 = "관광지", cat3 = "해변"),
        TripItemModel(title = "인천 차이나타운", cat2 = "맛집", cat3 = "중식"),
        TripItemModel(title = "경주 불국사", cat2 = "관광지", cat3 = "사찰"),
        TripItemModel(title = "강릉 안목해변 카페거리", cat2 = "맛집", cat3 = "카페"),
        TripItemModel(title = "서울 롯데월드 호텔", cat2 = "숙소", cat3 = "호텔"),
        TripItemModel(title = "전주 한옥마을", cat2 = "관광지", cat3 = "전통문화"),
        TripItemModel(title = "속초 대포항 수산시장", cat2 = "맛집", cat3 = "해산물"),
        TripItemModel(title = "남해 독일마을", cat2 = "관광지", cat3 = "문화마을")
    )
}