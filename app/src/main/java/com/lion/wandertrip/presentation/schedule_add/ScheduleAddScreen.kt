package com.lion.wandertrip.presentation.schedule_add

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.a02_boardcloneproject.component.CustomTopAppBar
import com.lion.wandertrip.component.CustomBlueButton
import com.lion.wandertrip.presentation.schedule_add.component.ScheduleAddCalendar
import com.lion.wandertrip.presentation.schedule_add.component.ScheduleAddTextInputLayout
import com.lion.wandertrip.ui.theme.NanumSquareRoundRegular

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleAddScreen(
    viewModel : ScheduleAddViewModel = hiltViewModel()
) {

    val focusManager = LocalFocusManager.current // 🔹 포커스 관리 객체 생성

    var errorMessage by remember { mutableStateOf<String?>(null) } // ✅ 에러 메시지 상태 추가

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CustomTopAppBar(
                title = "일정 추가",
                navigationIconImage = Icons.Filled.ArrowBack,
                navigationIconOnClick = { viewModel.application.navHostController.popBackStack() },
            )
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .pointerInput(Unit) { // 터치 이벤트 감지 바깥쪽 클릭 시 포커스 해제
                    detectTapGestures(
                        onTap = { focusManager.clearFocus() }
                    )
                },
        ) {
            // 일정 제목 입력 TextInputLayout
            ScheduleAddTextInputLayout(
                label = "일정 제목",
                placeholder = "일정 제목을 입력하세요",
                errorMessage = errorMessage,
                value = viewModel.scheduleTitle.value,
                onValueChange = {
                    viewModel.scheduleTitle.value = it
                    // 제목 입력 시 에러 메시지 제거
                    if (it.isNotEmpty()) errorMessage = null
                }
            )

            Text(
                text = "날짜 선택",
                fontFamily = NanumSquareRoundRegular,
                fontSize = 22.sp,
                modifier = Modifier.padding(top = 40.dp, bottom = 20.dp, end = 16.dp, start = 16.dp)
            )

            // ✅ 캘린더 컴포넌트 추가
            ScheduleAddCalendar(
                startDate = viewModel.scheduleStartDate,
                endDate = viewModel.scheduleEndDate,
                onDateRangeSelected = { start, end ->
                    viewModel.scheduleStartDate.value = start
                    viewModel.scheduleEndDate.value = end

                    // ✅ 날짜 두 개 선택 시 실행될 이벤트
                    Log.d(
                        "ScheduleAddScreen",
                        "선택한 일정: ${viewModel.scheduleStartDate.value} ~ ${viewModel.scheduleEndDate.value}"
                    )
                }
            )

            CustomBlueButton(
                text = "${viewModel.formatTimestampToDateString(viewModel.scheduleStartDate.value)} " +
                        "~" +
                        " ${viewModel.formatTimestampToDateString(viewModel.scheduleEndDate.value)}",
                paddingStart = 16.dp,
                paddingEnd = 16.dp,
                paddingTop = 20.dp,
                onClick = {
                    if (viewModel.scheduleTitle.value.isEmpty()) {
                        errorMessage = "일정 제목을 입력해주세요!" // ✅ 에러 메시지 설정
                    } else {
                        // 지역 선택 화면으로 이동
                        viewModel.moveToScheduleCitySelectScreen()
                    }
                }
            )
        }
    }
}