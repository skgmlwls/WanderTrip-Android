package com.lion.wandertrip.presentation.trip_note_schedule_page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.a02_boardcloneproject.component.CustomDividerComponent
import com.lion.wandertrip.presentation.bottom.schedule_page.ScheduleViewModel
import com.lion.wandertrip.presentation.bottom.schedule_page.component.ScheduleIconButton
import com.lion.wandertrip.presentation.bottom.schedule_page.component.ScheduleItemList
import com.lion.wandertrip.ui.theme.NanumSquareRound

@Composable
fun TripNoteScheduleScreen(
    tripNoteScheduleViewModel: TripNoteScheduleViewModel = hiltViewModel(),
) {

    tripNoteScheduleViewModel.gettingTripNoteScheduleData()

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
                    text = "일정 리스트",
                    fontFamily = NanumSquareRound,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(end = 5.dp)
                        .weight(1f)
                )
            }

            CustomDividerComponent()

            // 일정 목록 표시
            // ScheduleItemList(tripNoteScheduleViewModel.tripNoteScheduleList)
        }
    }
}