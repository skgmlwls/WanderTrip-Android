package com.lion.wandertrip.presentation.schedule_select_item

import ScheduleItemCategoryChips
import ScheduleItemSearchBar
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.Timestamp
import com.lion.a02_boardcloneproject.component.CustomTopAppBar
import com.lion.wandertrip.R
import com.lion.wandertrip.presentation.schedule_select_item.component.ScheduleItemList
import com.lion.wandertrip.ui.theme.NanumSquareRound
import com.lion.wandertrip.ui.theme.NanumSquareRoundRegular
import com.lion.wandertrip.util.ContentTypeId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleSelectItemScreen(
    itemCode: Int,
    areaName: String,
    areaCode: Int,
    scheduleDate: Long,
    tripScheduleDocId: String,
    viewModel: ScheduleSelectItemViewModel = hiltViewModel()
) {
    // 🔍 검색어 상태
    var searchQuery by remember { mutableStateOf("") }
    // 카테고리 필터 상태
    var selectedCategoryCode by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.scheduleDate.value = Timestamp(scheduleDate, 0)
        viewModel.tripScheduleDocId.value = tripScheduleDocId
        Log.d("ScheduleSelectItemScreen", "scheduleDate: ${viewModel.scheduleDate.value}")
        Log.d("ScheduleSelectItemScreen", "tripScheduleDocId: $tripScheduleDocId")

        // ✅ 여행지 항목 가져오기
        viewModel.loadTripItems(
            serviceKey = "ksezhUKKJp9M9RgOdmmu9i7lN1+AbkA1dk1xZpqMMam319sa3VIQHFtCXfADM1OxBUls7SrMrmun3AFTYRj5Qw==",
            areaCode = "$areaCode",
            contentTypeId = "$itemCode"
        )
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                ),
                title = {
                    val title = when (itemCode) {
                        ContentTypeId.TOURIST_ATTRACTION.contentTypeCode -> "관광지"
                        ContentTypeId.RESTAURANT.contentTypeCode -> "음식점"
                        ContentTypeId.ACCOMMODATION.contentTypeCode -> "숙소"
                        else -> ""
                    }
                    Text(text = "$title 추가하기", fontFamily = NanumSquareRound)
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.backScreen() }
                    ) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "뒤로 가기")
                    }
                },
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {

            // 룰렛 화면으로 이동하는 버튼
            Button(
                onClick = {

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White, // ✅ 버튼 배경색: 흰색
                    contentColor = Color(0xFF435C8F) // ✅ 버튼 텍스트 색상: 파란색 (변경 가능)
                ),
                shape = RectangleShape // ✅ 버튼을 사각형으로 변경
            ) {
                Image(
                    painter = painterResource(id = R.drawable.roulette_picture), // ✅ drawable 리소스 추가
                    contentDescription = "룰렛 이미지",
                    modifier = Modifier
                        .size(50.dp)
                        .padding(end = 16.dp) // ✅ 아이콘 크기 조정 가능
                )
                Text(
                    text = "룰렛 돌리기",
                    fontFamily = NanumSquareRoundRegular,
                    fontSize = 25.sp,
                    color = Color.Black
                )
            }

            // 🔍 검색 바 추가
            ScheduleItemSearchBar(
                query = searchQuery,
                onSearchQueryChanged = { searchQuery = it },
                onSearchClicked = { /* 검색 버튼 클릭 시 동작 */ },
                onClearQuery = { searchQuery = "" }
            )

            // 🎯 카테고리 칩 버튼 추가 (선택된 카테고리 유지)
            ScheduleItemCategoryChips(
                itemCode = itemCode,
                selectedCategoryCode = selectedCategoryCode,
                onCategorySelected = { newCategoryCode ->
                    selectedCategoryCode = newCategoryCode
                }
            )

            // ✅ 검색 및 카테고리 필터 적용된 여행지 리스트
            val filteredList = viewModel.tripItemList.filter {
                val matchesCategory = when (itemCode) {
                    12 -> selectedCategoryCode == null || it.cat2 == selectedCategoryCode
                    39, 32 -> selectedCategoryCode == null || it.cat3 == selectedCategoryCode
                    else -> true
                }
                val matchesSearchQuery = it.title.contains(searchQuery, ignoreCase = true)
                matchesCategory && matchesSearchQuery
            }

            ScheduleItemList(
                tripItemList = filteredList,
                onItemClick = {selectItem ->
                    viewModel.addTripItemToSchedule(selectItem)
                },
            )
        }
    }
}
