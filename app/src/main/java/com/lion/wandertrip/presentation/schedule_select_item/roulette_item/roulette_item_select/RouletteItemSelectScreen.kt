package com.lion.wandertrip.presentation.schedule_select_item.roulette_item.roulette_item_select

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.lion.wandertrip.presentation.schedule_select_item.roulette_item.RouletteItemViewModel
import com.lion.wandertrip.presentation.schedule_select_item.roulette_item.roulette_item_select.component.TripItemList
import com.lion.wandertrip.ui.theme.NanumSquareRoundRegular
import com.lion.wandertrip.util.RouletteScreenName
import com.lion.wandertrip.util.SharedTripItemList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouletteItemSelectScreen(
    navController: NavHostController,
    viewModel: RouletteItemSelectViewModel = hiltViewModel()
) {
    // ✅ 기존 선택된 항목 유지 (State 사용)
    var selectedItems by remember { mutableStateOf(SharedTripItemList.rouletteItemList.toList()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("여행지 선택", fontFamily = NanumSquareRoundRegular) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로 가기")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("여행지를 선택하세요:", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(8.dp))

            // TripItemList 컴포넌트
            TripItemList(
                tripItems = SharedTripItemList.sharedTripItemList,
                selectedItems = selectedItems,
                onItemClick = { tripItem ->
                    selectedItems = if (selectedItems.contains(tripItem)) {
                        selectedItems - tripItem
                    } else {
                        selectedItems + tripItem
                    }
                },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // ✅ 선택된 항목을 ViewModel의 `rouletteItemList`에 저장
                    viewModel.updateRouletteItemList(selectedItems)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedItems.isNotEmpty()
            ) {
                Text("추가")
            }
        }
    }
}