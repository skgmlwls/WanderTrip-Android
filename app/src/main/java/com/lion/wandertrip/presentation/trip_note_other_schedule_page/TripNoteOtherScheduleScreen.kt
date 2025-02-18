package com.lion.wandertrip.presentation.trip_note_other_schedule_page

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.wandertrip.R
import com.lion.wandertrip.presentation.trip_note_other_schedule_page.component.TripNoteOtherScheduleItemList
import com.lion.wandertrip.ui.theme.NanumSquareRound


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripNoteOtherScheduleScreen(
    tripNoteOtherScheduleViewModel: TripNoteOtherScheduleViewModel = hiltViewModel(),
) {

    tripNoteOtherScheduleViewModel.gettingTripNoteScheduleData()
    tripNoteOtherScheduleViewModel.gettingTripNoteProfileData()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "일정 리스트",
                        fontFamily = NanumSquareRound,
                        fontSize = 22.sp,
                        modifier = Modifier.padding(end = 5.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { tripNoteOtherScheduleViewModel.navigationButtonClick() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },

        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally, // 가로 정렬 중앙
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                Spacer(modifier = Modifier.height(130.dp)) // 상단 여백

                // 프로필 이미지 (원형)
                Image(
                    painter = painterResource(id = R.drawable.ic_person_24px),
                    contentDescription = "프로필 이미지",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape)
                )

                Spacer(modifier = Modifier.height(14.dp)) // 이미지와 텍스트 사이의 여백

                // 텍스트 (중앙 정렬)
                Text(
                    text = tripNoteOtherScheduleViewModel.userProfileList[0].userNickName,
                    fontFamily = NanumSquareRound,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 22.dp)
                )

                // 일정 목록 표시
                TripNoteOtherScheduleItemList(
                    dataList = tripNoteOtherScheduleViewModel.tripNoteOtherScheduleList,
                    viewModel = tripNoteOtherScheduleViewModel,
                    onRowClick = {
                    },
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    )
}
