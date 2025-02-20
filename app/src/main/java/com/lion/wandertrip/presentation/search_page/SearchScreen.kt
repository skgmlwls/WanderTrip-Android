package com.lion.wandertrip.presentation.search_page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun SearchScreen(searchViewModel: SearchViewModel = hiltViewModel()) {
    val selectedRecentSearch by searchViewModel.selectedRecentSearch.collectAsState()

    if (selectedRecentSearch != null) {
        // ✅ 선택된 도시가 있으면 `DetailSearchScreen` 표시
        DetailSearchScreen(contentId = selectedRecentSearch!!, searchViewModel)
    } else {
        // ✅ 선택된 도시가 없으면 `SearchView` 표시
        SearchView(searchViewModel)
    }
}

@Composable
fun SearchView(searchViewModel: SearchViewModel) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    val recentSearches by searchViewModel.recentSearches.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // 🔹 검색 바
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { searchViewModel.clearSelectedRecentSearch() } // ✅ 뒤로 가기 클릭 시 `selectedCity` 초기화
            )

            Spacer(modifier = Modifier.width(8.dp))

            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("도시, 장소, 숙소 검색", color = Color.Gray) },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF0F0F0))
            )

            Spacer(modifier = Modifier.width(8.dp))

            // 🔹 검색 버튼 (검색어 저장 & 상세 검색 화면으로 변경)
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        if (searchText.text.isNotBlank()) {
                            searchViewModel.addRecentSearch(searchText.text) // ✅ 최근 검색어 저장
                            searchViewModel.selectRecentSearch(searchText.text) // ✅ 선택된 도시 저장 (자동으로 `DetailSearchScreen`으로 변경)
                        }
                    }
            )
        }

        // 🔹 최근 검색어 목록
        RecentSearchList(recentSearches, searchViewModel)
    }
}


@Composable
fun DetailSearchScreen(
    contentId: String,
    searchViewModel: SearchViewModel = hiltViewModel(),
) {
    var searchText by remember { mutableStateOf(contentId) } // ✅ 검색 가능하도록 설정
    var selectedCategory by remember { mutableStateOf("추천") } // ✅ 선택된 카테고리 상태 추가

    val attractions = attractionData[contentId] ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 🔹 상단 검색 바 (뒤로가기 & 검색 기능 추가)
        SearchBar(searchText, onBackClick = {
            searchViewModel.backScreen() // ✅ 뒤로 가기 기능
        }) { newText ->
            searchText = newText
        }

        // 🔹 카테고리 탭 (추천, 관광지, 숙소, 맛집, 여행기)
        CategoryTabs(selectedCategory) { newCategory ->
            selectedCategory = newCategory
        }

        // 🔹 전체를 `LazyColumn`으로 감싸 스크롤 가능하도록 설정
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ✅ 선택된 카테고리에 따라 표시할 데이터 변경
            when (selectedCategory) {
                "관광지" -> {
                    item { SectionTitle("관광지") }
                    items(attractions) { attraction ->
                        AttractionItem(attraction)
                    }
                }
                "숙소" -> {
                    item { SectionTitle("숙소") }
                    items(hotels) { hotel -> // ✅ 숙소 리스트 추가
                        HotelItem(hotel)
                    }
                }
                "맛집" -> {
                    item { SectionTitle("맛집") }
                    items(restaurants) { restaurant -> // ✅ 맛집 리스트 추가
                        RestaurantItem(restaurant)
                    }
                }
                "여행기" -> {
                    item { SectionTitle("여행기") }
                    items(travelLogs) { travelLog -> // ✅ 여행기 리스트 추가
                        TravelLogItem(travelLog)
                    }
                }
                else -> { // ✅ "추천" 탭 클릭 시 모든 항목 표시
                    item { SectionTitle("관광지") }
                    items(attractions) { attraction ->
                        AttractionItem(attraction)
                    }
                    item { SectionTitle("숙소") }
                    items(hotels) { hotel ->
                        HotelItem(hotel)
                    }
                    item { SectionTitle("맛집") }
                    items(restaurants) { restaurant ->
                        RestaurantItem(restaurant)
                    }
                    item { SectionTitle("여행기") }
                    items(travelLogs) { travelLog ->
                        TravelLogItem(travelLog)
                    }
                }
            }
        }
    }
}


@Composable
fun SearchBar(searchText: String, onBackClick: () -> Unit, onTextChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 🔹 뒤로가기 버튼
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier
                .size(24.dp)
                .clickable { onBackClick() } // ✅ 뒤로가기 기능
        )

        Spacer(modifier = Modifier.width(8.dp))

        // 🔹 검색 입력 필드
        TextField(
            value = searchText,
            onValueChange = onTextChange,
            placeholder = { Text("도시, 장소, 숙소, 맛집 검색", color = Color.Gray) },
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF0F0F0))
        )

        Spacer(modifier = Modifier.width(8.dp))

        // 🔹 검색 버튼
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    // ✅ 검색 기능 구현 (필요하면 ViewModel과 연동)
                    println("🔍 검색 실행: $searchText")
                }
        )
    }
}

@Composable
fun CategoryTabs(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    val categories = listOf("추천", "관광지", "숙소", "맛집", "여행기")

    LazyRow( // ✅ 좌우 스크롤 가능하게 수정
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp) // ✅ 양쪽 여백 추가
    ) {
        items(categories) { category ->
            Text(
                text = category,
                fontSize = 14.sp,
                color = if (selectedCategory == category) Color.White else Color.Black, // ✅ 선택된 카테고리 강조
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (selectedCategory == category) Color.Black else Color.LightGray) // ✅ 선택 시 색상 변경
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .clickable { onCategorySelected(category) } // ✅ 카테고리 선택 이벤트 추가
            )
        }
    }
}

// 🔹 섹션 제목
@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

// 🔹 가이드 리스트 아이템
@Composable
fun GuideItem(guide: Guide) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { /* 상세 이동 */ }
    ) {
        // 임시 이미지 (API 연동 시 NetworkImage로 변경)
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color.Gray)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = guide.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = guide.description,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

// 🔹 관광지 리스트 아이템
@Composable
fun AttractionItem(attraction: Attraction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { /* 상세 이동 */ }
    ) {
        // 임시 이미지
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(Color.Gray)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = attraction.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = attraction.category,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

data class Guide(val title: String, val description: String)
data class Attraction(val name: String, val category: String)

// 🔹 지역별 관광지 데이터
val attractionData = mapOf(
    "서울" to listOf(
        Attraction("서울 경찰청", "관광명소 · 서울 종로구"),
        Attraction("키스 서울", "쇼핑 · 서울 성동구"),
        Attraction("서울 광장", "관광명소 · 서울 중구")
    ),
    "부산" to listOf(
        Attraction("해운대 해수욕장", "관광명소 · 부산 해운대구"),
        Attraction("광안리 해변", "관광명소 · 부산 수영구"),
        Attraction("부산 타워", "관광명소 · 부산 중구")
    )
)

// 숙소 리스트
val hotels = listOf(
    Hotel("서울 한옥 호텔", "서울 종로구"),
    Hotel("제주 오션뷰 호텔", "제주 서귀포시")
)

// 맛집 리스트
val restaurants = listOf(
    Restaurant("명동 교자", "서울 중구"),
    Restaurant("부산 밀면", "부산 해운대구")
)

// 여행기 리스트
val travelLogs = listOf(
    TravelLog("서울 야경 투어", "한강과 남산의 야경을 즐기기"),
    TravelLog("제주 올레길 여행", "바닷길을 따라 걷는 힐링 코스")
)

// 데이터 모델
data class Hotel(val name: String, val location: String)
data class Restaurant(val name: String, val location: String)
data class TravelLog(val title: String, val description: String)

// UI 항목
@Composable
fun HotelItem(hotel: Hotel) { /* UI 구현 */ }

@Composable
fun RestaurantItem(restaurant: Restaurant) { /* UI 구현 */ }

@Composable
fun TravelLogItem(travelLog: TravelLog) { /* UI 구현 */ }

@Composable
fun RecentSearchList(recentSearches: List<String>, searchViewModel: SearchViewModel) {
    if (recentSearches.isNotEmpty()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("최근 검색", fontWeight = FontWeight.Bold)
                Text(
                    "모두 삭제",
                    color = Color.Gray,
                    modifier = Modifier.clickable { searchViewModel.clearRecentSearches() }
                )
            }

                // ✅ 최근 검색어가 있으면 목록 표시
                LazyColumn {
                    items(recentSearches) { search ->
                        RecentSearchItem(
                            text = search,
                            onDelete = { searchViewModel.removeRecentSearch(search) },
                            onClick = { searchViewModel.selectRecentSearch(search) }
                        )
                    }
                }
        }
    } else {
        // ✅ 최근 검색어가 없을 때 메시지 표시
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "최근 검색어가 없습니다",
                color = Color.Gray
            )
        }
    }
}

@Composable
fun RecentSearchItem(text: String, onDelete: () -> Unit, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, fontSize = 16.sp, modifier = Modifier.weight(1f))

        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Delete",
            modifier = Modifier
                .size(20.dp)
                .clickable { onDelete() }
        )
    }
}