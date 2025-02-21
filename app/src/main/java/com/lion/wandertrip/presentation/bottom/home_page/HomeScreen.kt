package com.lion.wandertrip.presentation.bottom.home_page

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.wandertrip.presentation.bottom.home_page.components.TravelSpotItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()

    viewModel.gettingTripScheduleData()
//    LaunchedEffect(Unit) {
//        // ✅ 여행지 항목 가져오기
//        viewModel.loadTripItems(
//            serviceKey = "ksezhUKKJp9M9RgOdmmu9i7lN1+AbkA1dk1xZpqMMam319sa3VIQHFtCXfADM1OxBUls7SrMrmun3AFTYRj5Qw==",
//            areaCode = "$areaCode",
//            contentTypeId = "$itemCode"
//        )
//    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                modifier = Modifier.height(56.dp),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0077C2),
                    titleContentColor  = Color.White
                ),
                title = {},
                actions = {
                    IconButton(
                        onClick = { viewModel.onClickIconSearch() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "검색",
                            tint = Color.White
                        )
                    }
                },
            )
        }
    ) { paddingValues -> // ✅ Scaffold의 contentPadding 적용
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // ✅ TopBar 높이만큼 여백 추가
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // "추천 관광지" 제목 추가
                Text(
                    text = "추천 관광지",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                        items(viewModel.tripItemList) { tripItem ->
                        TravelSpotItem(
                            tripItem = tripItem,
                            onItemClick = { viewModel.onClickTrip() }
                        )
                    }
                }

                Text(
                    text = "인기 여행기",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(viewModel.tripItemList) { tripItem ->

                    }
                }
            }
        }
    }
}

