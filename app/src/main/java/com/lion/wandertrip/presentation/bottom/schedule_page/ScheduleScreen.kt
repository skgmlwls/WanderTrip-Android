package com.lion.wandertrip.presentation.bottom.schedule_page

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.a02_boardcloneproject.component.CustomDividerComponent
import com.lion.a02_boardcloneproject.component.CustomTopAppBar
import com.lion.wandertrip.R
import com.lion.wandertrip.presentation.bottom.schedule_page.component.ScheduleIconButton
import com.lion.wandertrip.presentation.bottom.schedule_page.component.ScheduleItemList
import com.lion.wandertrip.ui.theme.NanumSquareRound
import com.lion.wandertrip.ui.theme.NanumSquareRoundRegular

@Composable
fun ScheduleScreen(
    scheduleViewModel: ScheduleViewModel = hiltViewModel(),
) {

    scheduleViewModel.gettingTripScheduleData()

    Scaffold(

    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "일정화면",
                    fontFamily = NanumSquareRound,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(end = 5.dp)
                        .weight(1f)
                )
                ScheduleIconButton(
                    icon = Icons.Filled.Add,
                    size = 30
                )
            }

            CustomDividerComponent()

            // 일정 목록 표시
            ScheduleItemList(scheduleViewModel.tripScheduleList)
        }
    }
}

// 미리 보기 추가
@Preview(showBackground = true)
@Composable
fun ScheduleScreenPreview() {
    MaterialTheme { // 기본 Material3 테마 적용
        ScheduleScreen()
    }
}