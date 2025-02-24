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
import androidx.compose.runtime.LaunchedEffect
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
    // 초기값을 contentId로 설정
    var searchInput by remember { mutableStateOf(contentId) }
    var searchQuery by remember { mutableStateOf(contentId) }
    var selectedCategoryCode by remember { mutableStateOf<String?>(null) }

    val dummyTripList = remember { getDummyTripItems() }

    // filteredList는 searchQuery 기준으로 업데이트됨.
    var filteredList by remember { mutableStateOf<List<TripItemModel>>(emptyList()) }

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotBlank()) {
            filteredList = dummyTripList.filter {
                it.title.contains(searchQuery, ignoreCase = true)
            }
        }
        // searchQuery가 비어 있으면 이전 결과를 유지 (업데이트하지 않음)
    }

    // 카테고리별 분류
    val categorizedResults = if (selectedCategoryCode == "추천" || selectedCategoryCode == null) {
        filteredList.groupBy { it.cat2 }
    } else {
        filteredList.filter { it.cat2 == selectedCategoryCode }.groupBy { it.cat2 }
    }

    // 표시할 카테고리 리스트 (순서 유지)
    val requiredCategories = listOf("관광지", "숙소", "맛집", "여행기")

    Scaffold(containerColor = Color.White) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            // 검색 바: 화면에는 searchInput을 표시함.
            HomeSearchBar(
                query = searchInput,
                onSearchQueryChanged = { newValue ->
                    searchInput = newValue
                },
                onSearchClicked = {
                    if (searchInput.isNotBlank()) {
                        // 검색 아이콘을 눌렀을 때만 실제 필터링에 사용할 searchQuery를 업데이트
                        searchQuery = searchInput
                        val searchItem = TripItemModel(title = searchInput)
                        searchViewModel.addSearchToRecent(searchItem)
                        searchViewModel.onClickToResult(searchInput)
                    }
                },
                onClearQuery = {
                    // 검색어 입력란은 지워지지만, 이전 검색 결과는 유지됨.
                    searchInput = ""
                },
                onBackClicked = { viewModel.onClickNavIconBack() }
            )

            // 카테고리 칩
            SearchItemCategoryChips(
                selectedCategoryCode = selectedCategoryCode,
                onCategorySelected = { selectedCategoryCode = it }
            )

            // 최대 표시할 항목 수
            val maxDisplayCount = 3

            // 검색 결과 리스트
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                requiredCategories.forEach { category ->
                    item {
                        // 카테고리 제목
                        Text(
                            text = category,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    val itemsForCategory = categorizedResults[category] ?: emptyList()

                    if (itemsForCategory.isNotEmpty()) {
                        // 최대 maxDisplayCount 개의 항목만 표시
                        val visibleItems = itemsForCategory.take(maxDisplayCount)
                        items(visibleItems) { tripItem ->
                            SearchItem(
                                tripItem = tripItem,
                                onItemClick = { searchViewModel.onClickToResult(tripItem.title) }
                            )
                            CustomDividerComponent(10.dp)
                        }
                    } else {
                        // 해당 카테고리에 데이터가 없으면 "없음" 메시지 표시
                        item {
                            NoResultsMessage(category)
                        }
                    }

                    // 항목이 많을 경우 "더보기" 버튼 표시
                    if (itemsForCategory.size > maxDisplayCount) {
                        item {
                            MoreButton(category = category)
                        }
                    }

                    // 마지막 카테고리가 아니라면 구분선 추가
                    if (category != requiredCategories.last()) {
                        item {
                            CustomDividerComponent(16.dp)
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