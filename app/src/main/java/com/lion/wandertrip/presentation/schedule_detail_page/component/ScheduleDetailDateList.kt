package com.lion.wandertrip.presentation.schedule_detail_page.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.Timestamp
import com.lion.a02_boardcloneproject.component.CustomDividerComponent
import com.lion.wandertrip.model.TripScheduleModel
import com.lion.wandertrip.presentation.schedule_detail_page.ScheduleDetailViewModel
import com.lion.wandertrip.ui.theme.NanumSquareRound
import com.lion.wandertrip.ui.theme.NanumSquareRoundRegular
import com.lion.wandertrip.util.ContentTypeId

@Composable
fun ScheduleDetailDateList(
    viewModel: ScheduleDetailViewModel,
    tripSchedule: TripScheduleModel,
    formatTimestampToDate: (Timestamp) -> String
) {
    // 구글 맵 터치 감지
    var isMapTouched by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        userScrollEnabled = !isMapTouched // Google Map 터치 시 LazyColumn 스크롤 방지
    ) {
        items(tripSchedule.scheduleDateList.size) { index ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                // 날짜 표시
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .alignByBaseline(),
                        text = "DAY ${index + 1}",
                        fontSize = 25.sp,
                        fontFamily = NanumSquareRound,
                    )
                    Text(
                        modifier = Modifier.alignByBaseline(),
                        text = formatTimestampToDate(tripSchedule.scheduleDateList[index]),
                        fontSize = 14.sp,
                        fontFamily = NanumSquareRoundRegular
                    )
                }

                // 해당 날짜의 스케줄 아이템 목록 필터링 후 정렬
                // val filteredItems = tripSchedule.scheduleItems
                val filteredItems = viewModel.tripScheduleItems
                    .filter { it.itemDate.seconds == tripSchedule.scheduleDateList[index].seconds }
                    .sortedBy { it.itemIndex }

                // Google Map 표시 (해당 날짜의 스케줄 아이템 목록을 전달)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(top = 5.dp),
                ) {
                    ScheduleDetailGoogleMap(
                        scheduleItems = filteredItems,
                        onTouch = { touched -> isMapTouched = touched }
                    )
                }

                // 상세 일정 리스트 표시
                // 상세 일정 리스트 표시
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp) // 요소들 사이에 간격 0
                ) {
                    filteredItems.forEach { scheduleItem ->
                        // 각 scheduleItem마다 개별적으로 expanded 상태를 선언
                        var expanded by remember { mutableStateOf(false) }

                        Row(
                            modifier = Modifier.height(IntrinsicSize.Min) // intrinsic measurements
                        ) {
                            ScheduleDetailVerticalDividerWithCircle()

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 20.dp)
                                    .weight(1f)
                            ) {
                                Row {
                                    // 일정 항목 이름
                                    Text(
                                        text = scheduleItem.itemTitle,
                                        fontSize = 14.sp,
                                        fontFamily = NanumSquareRoundRegular,
                                        modifier = Modifier.padding(start = 10.dp)
                                    )

                                    // 일정 항목 종류
                                    Text(
                                        text = scheduleItem.itemType,
                                        fontSize = 10.sp,
                                        fontFamily = NanumSquareRoundRegular,
                                        color = Color.DarkGray,
                                        modifier = Modifier.padding(start = 10.dp)
                                    )
                                }

                                if (scheduleItem.itemReviewImagesURL.isNotEmpty()) {
                                    Row {
                                        scheduleItem.itemReviewImagesURL.forEach { imageUrl ->
                                            AsyncImage(
                                                model = imageUrl,
                                                contentDescription = "Review Image",
                                                modifier = Modifier
                                                    .size(80.dp)
                                                    .padding(start = 5.dp, end = 8.dp)
                                            )
                                        }
                                    }
                                }

                                if (scheduleItem.itemReviewText != "") {
                                    // 후기
                                    Text(
                                        text = scheduleItem.itemReviewText,
                                        fontSize = 12.sp,
                                        fontFamily = NanumSquareRoundRegular,
                                        modifier = Modifier.padding(start = 10.dp)
                                    )
                                }

                            }

                            // 메뉴 버튼
                            Box {
                                IconButton(
                                    modifier = Modifier
                                        .padding(top = 5.dp)
                                        .size(18.dp),
                                    onClick = { expanded = true }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.MoreVert,
                                        contentDescription = "더보기"
                                    )
                                }
                                ScheduleDetailDropDawn(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    onDelete = {
                                        expanded = false
                                        viewModel.removeTripScheduleItem(
                                            viewModel.tripScheduleDocId.value,
                                            scheduleItem.itemDocId,
                                            scheduleItem.itemDate
                                        )
                                    },
                                    onReview = {
                                        expanded = false
                                        viewModel.moveToScheduleItemReviewScreen(
                                            viewModel.tripScheduleDocId.value,
                                            scheduleItem.itemDocId,
                                            scheduleItem.itemTitle,
                                        )
                                    },
                                    onMove = {
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End)
                ) {
                    OutlinedButton(
                        onClick = {
                            viewModel.moveToScheduleSelectItemScreen(
                                ContentTypeId.TOURIST_ATTRACTION.contentTypeCode,
                                tripSchedule.scheduleDateList[index]
                            )
                        },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp) // 내부 여백 조정
                    ) {
                        Text(
                            text = "관광지 추가",
                            fontSize = 12.sp,
                            fontFamily = NanumSquareRound
                        )
                    }
                    OutlinedButton(
                        onClick = {
                            viewModel.moveToScheduleSelectItemScreen(
                                ContentTypeId.RESTAURANT.contentTypeCode,
                                tripSchedule.scheduleDateList[index]
                            )
                        },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp) // 내부 여백 조정
                    ) {
                        Text(
                            text = "음식점 추가",
                            fontSize = 12.sp,
                            fontFamily = NanumSquareRound
                        )
                    }
                    OutlinedButton(
                        onClick = {
                            viewModel.moveToScheduleSelectItemScreen(
                                ContentTypeId.ACCOMMODATION.contentTypeCode,
                                tripSchedule.scheduleDateList[index]
                            )
                        },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp) // 내부 여백 조정
                    ) {
                        Text(
                            text = "숙소 추가",
                            fontSize = 12.sp,
                            fontFamily = NanumSquareRound
                        )
                    }
                }

                CustomDividerComponent()
            }
        }
    }
}

