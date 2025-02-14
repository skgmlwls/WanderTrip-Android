package com.lion.wandertrip.presentation.bottom.schedule_page.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Timestamp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.a02_boardcloneproject.component.CustomIconButton
import com.lion.wandertrip.model.TripScheduleModel
import com.lion.wandertrip.presentation.bottom.schedule_page.ScheduleViewModel
import com.lion.wandertrip.ui.theme.NanumSquareRound
import com.lion.wandertrip.ui.theme.NanumSquareRoundLight
import com.lion.wandertrip.ui.theme.NanumSquareRoundRegular

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleItemList(
    dataList: List<TripScheduleModel>,
    viewModel: ScheduleViewModel = hiltViewModel(),
    onRowClick: (TripScheduleModel) -> Unit = {}, // 클릭 이벤트 추가
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
    ) { // RecyclerView 대체
        items(dataList.size) { index ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // 카드 그림자 효과
                shape = RoundedCornerShape(16.dp), // 라운드 모서리 설정
                colors = CardDefaults.cardColors(Color(0xFFFFFFFF))
            ) {
                Column( // 클릭 불필요 → 카드 내부에서만 클릭 감지
                    modifier = Modifier.fillMaxWidth()
                        .clickable { onRowClick(dataList[index]) },
                ) {
                    Column( // 클릭 불필요 → 카드 내부에서만 클릭 감지
                        modifier = Modifier.fillMaxWidth()
                            .padding(15.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                        ) {
                            // 일정 제목
                            Text(
                                text = dataList[index].scheduleTitle,
                                fontFamily = NanumSquareRound,
                                fontSize = 20.sp,
                                modifier = Modifier.weight(1f)
                            )

                            ScheduleDropDawnIconButton(
                                icon = Icons.Filled.MoreVert,
                                size = 15,
                                menuItems = listOf("삭제"),
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 5.dp),
                            verticalAlignment = Alignment.CenterVertically // ✅ 수직 중앙 정렬
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Place, // ✅ 머티리얼 아이콘 사용
                                contentDescription = "홈 아이콘",
                                modifier = Modifier
                                    .size(12.5.dp)  // ✅ 크기 조절
                            )

                            // 일정  지역
                            Text(
                                text = dataList[index].scheduleCity,
                                fontFamily = NanumSquareRoundRegular,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(end = 5.dp)
                            )

                            // 일정 날짜
                            Text(
                                text = "${viewModel.formatTimestampToDateString(dataList[index].scheduleStartDate)} " +
                                        "~" +
                                        " ${viewModel.formatTimestampToDateString(dataList[index].scheduleEndDate)}",
                                fontFamily = NanumSquareRoundRegular,
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 0.5.dp),
                            )
                        }
                    }

                }
            }
        }
    }
}



// 미리 보기
@Preview(showBackground = true)
@Composable
fun PreviewItemListScreen() {
    val dataList : List<TripScheduleModel> = listOf(
        TripScheduleModel(
            scheduleTitle = "제주 힐링여행",
            // scheduleStartDate = "2025.03.01",
            scheduleCity = "서울",
            // scheduleEndDate = "2025.03.05",
        ),
        TripScheduleModel(
            scheduleTitle = "서울 힐링여행",
            // scheduleStartDate = "2025.03.06",
            scheduleCity = "서울",
            // scheduleEndDate = "2025.03.11",
        )
    )

    ScheduleItemList(dataList)
}