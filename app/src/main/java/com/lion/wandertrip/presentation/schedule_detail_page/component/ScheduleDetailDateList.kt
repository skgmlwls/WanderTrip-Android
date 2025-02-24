package com.lion.wandertrip.presentation.schedule_detail_page.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.firebase.Timestamp
import com.lion.a02_boardcloneproject.component.CustomDividerComponent
import com.lion.wandertrip.R
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
        userScrollEnabled = !isMapTouched,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        tripSchedule.scheduleDateList.forEachIndexed { index, date ->

            // 해당 날짜의 스케줄 아이템 목록 필터링 후 정렬
            val filteredItems = viewModel.tripScheduleItems
                .filter { it.itemDate.seconds == date.seconds }
                .sortedBy { it.itemIndex }
            // 날짜 헤더 추가
            item {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "DAY ${index + 1}",
                            fontSize = 25.sp,
                            fontFamily = NanumSquareRound,
                        )
                        Text(
                            text = formatTimestampToDate(date),
                            fontSize = 14.sp,
                            fontFamily = NanumSquareRoundRegular
                        )
                    }

                    // Google Map 표시
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
                }
            }

            // 일정 리스트 추가
            items(filteredItems) { scheduleItem ->
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
                            Log.d("ScheduleDetailDateList", "scheduleItem: ${scheduleItem.itemTitle}")
                            Text(
                                text = scheduleItem.itemTitle,
                                fontSize = 14.sp,
                                fontFamily = NanumSquareRoundRegular,
                                modifier = Modifier.padding(start = 10.dp)
                            )

                            Text(
                                text = scheduleItem.itemType,
                                fontSize = 10.sp,
                                fontFamily = NanumSquareRoundRegular,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(start = 10.dp)
                            )
                        }

                        Row {
                            scheduleItem.itemReviewImagesURL.forEach { imageUrl ->
                                val painter = rememberAsyncImagePainter(model = imageUrl)

                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .padding(horizontal = 5.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                ) {
                                    Image(
                                        painter = painter,
                                        contentDescription = "Review Image",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }

                        if (scheduleItem.itemReviewText.isNotEmpty()) {
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
                            onMove = {}
                        )
                    }
                }
            }

            // 버튼 추가
            item {
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
                                date
                            )
                        },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
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
                                date
                            )
                        },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
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
                                date
                            )
                        },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
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
