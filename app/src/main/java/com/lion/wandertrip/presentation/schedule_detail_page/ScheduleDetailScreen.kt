package com.lion.wandertrip.presentation.schedule_detail_page

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.wandertrip.presentation.schedule_detail_page.component.ScheduleDetailDateList
import com.lion.wandertrip.ui.theme.NanumSquareRound

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleDetailScreen(
    tripScheduleDocId: String,
    areaName: String,
    areaCode: Int,
    viewModel: ScheduleDetailViewModel = hiltViewModel(),
) {
    // 초기 세팅
    LaunchedEffect(Unit) {
        // 초기 데이터 세팅
        viewModel.addAreaData(tripScheduleDocId, areaName, areaCode)
        // 이미 데이터가 없다면 불러오기
        if (viewModel.tripScheduleItems.isEmpty()) {
            viewModel.getTripSchedule()
        }
    }

    Log.d("ScheduleDetailScreen", "일정 문서 ID : $tripScheduleDocId")
    Log.d("ScheduleDetailScreen", "도시 이름 : $areaName")
    Log.d("ScheduleDetailScreen", "도시 코드 : $areaCode")
    Log.d("ScheduleDetailScreen", "날짜 리스트 : ${viewModel.tripSchedule.value.scheduleDateList}")

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White // ✅ 투명색 적용
                ),
                title = {
                    Text(text = "일정 상세", fontFamily = NanumSquareRound) // ✅ 제목 설정
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.backScreen() }
                    ) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "뒤로 가기")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.moveToScheduleDetailFriendsScreen("abcd") }
                    ) {
                        Icon(imageVector = Icons.Filled.People, contentDescription = "함께 하는 사람 목록")
                    }
                },
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
        ) {
//            Text(text = "도시 이름 : $areaName")
//            Text(text = "도시 코드 : $areaCode")
            ScheduleDetailDateList(
                viewModel = viewModel,
                tripSchedule = viewModel.tripSchedule.value,
                // TimeStamp 를 변환 하는 함수 타입 전달
                formatTimestampToDate = { timestamp -> viewModel.formatTimestampToDate(timestamp) }
            )
        }
    }

}