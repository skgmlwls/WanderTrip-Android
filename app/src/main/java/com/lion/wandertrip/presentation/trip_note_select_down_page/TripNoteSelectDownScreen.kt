package com.lion.wandertrip.presentation.trip_note_select_down_page

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.a02_boardcloneproject.component.CustomAlertDialog
import com.lion.a02_boardcloneproject.component.CustomIconButton
import com.lion.a02_boardcloneproject.component.CustomTopAppBar
import com.lion.wandertrip.R
import com.lion.wandertrip.component.BlueButton
import com.lion.wandertrip.presentation.trip_note_detail_page.TripNoteDetailViewModel
import com.lion.wandertrip.presentation.trip_note_other_schedule_page.component.TripNoteOtherScheduleItemList
import com.lion.wandertrip.presentation.trip_note_select_down_page.component.TripNoteSelectDownItemList
import com.lion.wandertrip.presentation.user_sign_up_page.sign_up_step2_page.UserSignUpStep2ViewModel
import com.lion.wandertrip.ui.theme.NanumSquareRound


@Composable
fun TripNoteSelectDownScreen(tripNoteSelectDownViewModel: TripNoteSelectDownViewModel = hiltViewModel())
{
    tripNoteSelectDownViewModel.gettingTripNoteDetailData()

    // 다이얼로그를 보여주는 상태
    val showDialogState = tripNoteSelectDownViewModel.showDialogState

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "",
                navigationIconImage = Icons.AutoMirrored.Filled.ArrowBack,
                navigationIconOnClick = {
                    tripNoteSelectDownViewModel.navigationButtonClick()
                },
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 20.dp, vertical = 10.dp)

            // verticalArrangement = Arrangement.spacedBy(20.dp),
            // verticalArrangement = Arrangement.SpaceBetween, // 위, 아래로 배치 균형 조정
            // horizontalAlignment = Alignment.CenterHorizontally // 가로 정렬 중앙
        ) {
            Spacer(modifier = Modifier.height(14.dp)) // 상단 여백


            Text(
                text = "일정을 담을 여행을",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = "선택해주세요!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(17.dp)) // 상단 여백


            // 새 여행 만들어 담기 버튼
            BlueButton(text = "새 여행 만들어 담기",
                buttonWidth = 150.dp) {
                tripNoteSelectDownViewModel.newTripButtonClick()
            }

            Spacer(modifier = Modifier.height(30.dp)) // 하단 여백

            Text(
                text = "나의 다가오는 여행",
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp)) // 하단 여백

            // 스크롤 가능한 리스트 부분
            Box(modifier = Modifier.weight(1f)) {
                TripNoteSelectDownItemList(
                    dataList = tripNoteSelectDownViewModel.tripNoteMyScheduleList,
                    viewModel = tripNoteSelectDownViewModel,
                    onRowClick = {
                    },
                )
            }

            Spacer(modifier = Modifier.height(13.dp)) // 하단 여백

            // 완료 버튼
            BlueButton(text = "완료",) {
                tripNoteSelectDownViewModel.selectFinishButtonClick()
            }

            // 다이얼로그 표시
            if (showDialogState.value) {
                CustomAlertDialog(
                    showDialogState = showDialogState,
                    title = "일정 선택",
                    text = "이 일정을 그대로 담을까요?",
                    confirmButtonTitle = "확인",
                    dismissButtonTitle = "취소",
                    confirmButtonOnClick = {
                        // 확인 버튼 클릭 시 동작
                        tripNoteSelectDownViewModel.onConfirmClick()
                        // 일정 제목 입력 화면으로 이동
                        tripNoteSelectDownViewModel.goScheduleTitleButtonClick()

                    },
                    dismissButtonOnClick = {
                        // 취소 버튼 클릭 시 동작
                        tripNoteSelectDownViewModel.onDismissClick()
                    }
                )
            }

            Spacer(modifier = Modifier.height(10.dp)) // 하단 여백
        }
    }
}
