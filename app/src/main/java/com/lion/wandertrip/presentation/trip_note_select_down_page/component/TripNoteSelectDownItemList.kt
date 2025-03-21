package com.lion.wandertrip.presentation.trip_note_select_down_page.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.wandertrip.model.TripScheduleModel
import com.lion.wandertrip.presentation.trip_note_schedule_page.TripNoteScheduleViewModel
import com.lion.wandertrip.presentation.trip_note_select_down_page.TripNoteSelectDownViewModel
import com.lion.wandertrip.ui.theme.NanumSquareRound
import com.lion.wandertrip.ui.theme.NanumSquareRoundRegular


@Composable
fun TripNoteSelectDownItemList(
    dataList: List<TripScheduleModel?>,
    viewModel: TripNoteSelectDownViewModel = hiltViewModel(),
    onRowClick: (TripScheduleModel) -> Unit = {}, // 클릭 이벤트 추가
    modifier: Modifier = Modifier // modifier 파라미터 추가

) {
    // 선택된 카드를 저장하는 상태
    val selectedCardIndex = remember { mutableStateOf(-1) }



    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
    ) {
        items(dataList) { tripSchedule -> // 직접 데이터 항목을 사용하는 방식으로 변경

            // tripSchedule이 null이 아닌 경우에만 처리
            tripSchedule?.let { schedule ->

            val isSelected = selectedCardIndex.value == dataList.indexOf(tripSchedule) // 현재 카드가 선택된 카드인지 확인



            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 7.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // 카드 그림자 효과
                shape = RoundedCornerShape(16.dp), // 라운드 모서리 설정
                // colors = CardDefaults.cardColors(Color(0xFFFFFFFF))
                colors = CardDefaults.cardColors(
                    if (isSelected) Color(0xFFE0E0E0) else Color(0xFFFFFFFF) // 선택된 카드 배경 색
                )
            ) {
                Column( // 클릭 불필요 → 카드 내부에서만 클릭 감지
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // onRowClick(tripSchedule)
                            // 카드를 클릭하면 해당 카드가 선택됨
                            selectedCardIndex.value = dataList.indexOf(tripSchedule)
                            onRowClick(tripSchedule) // 선택된 일정 처리
                                   }, // 클릭 이벤트 적용
                    horizontalAlignment = Alignment.Start // 내부 정렬
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp), // Row padding
                    ) {
                        // 일정 제목
                        Text(
                            text = tripSchedule.scheduleTitle,
                            fontFamily = NanumSquareRound,
                            fontSize = 20.sp,
                            modifier = Modifier.weight(1f) // 제목 부분 확장
                        )

                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 3.dp, end = 20.dp, start = 12.dp, bottom = 12.dp),
                        verticalAlignment = Alignment.CenterVertically // ✅ 수직 중앙 정렬
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Place, // ✅ 머티리얼 아이콘 사용
                            contentDescription = "홈 아이콘",
                            modifier = Modifier
                                .size(12.5.dp)

                        )

                        // 일정 지역
                        Text(
                            text = tripSchedule.scheduleCity,
                            fontFamily = NanumSquareRoundRegular,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(end = 10.dp)
                        )

                        // 일정 날짜
                        Text(
                            text = "${viewModel.formatTimestampToDateString(tripSchedule.scheduleStartDate)} " +
                                    "~" +
                                    " ${viewModel.formatTimestampToDateString(tripSchedule.scheduleEndDate)}",
                            fontFamily = NanumSquareRoundRegular,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 10.dp)
                        )
                    }
                }
                }
            }
        }
    }
}

