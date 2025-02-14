package com.lion.wandertrip.presentation.my_trip_page.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Timestamp
import com.lion.wandertrip.model.TripScheduleModel
import com.lion.wandertrip.util.CustomFont
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun VerticalTripItemList(tripList: List<TripScheduleModel>) {
    // remember {} ->  UI가 변할 때마다 새로운 시간이 생성되지 않도록 방지하는 역할, 크게 의미는 없는거같음
    val currentDate = remember { Timestamp.now() } // 현재 날짜 가져오기

    // 일정 분류
   // 다가오는 여행
    val upcomingTrips = tripList
        .filter { it.scheduleEndDate >= currentDate } // 현재 날짜 이후 여행 필터링
        .sortedBy { it.scheduleStartDate.toDate() } // scheduleEndDate가 가까운 순으로 정렬

    // 지난 여행
    val pastTrips = tripList
        .filter { it.scheduleEndDate < currentDate }  // 현재 날짜 이전 여행 필터링
        .sortedByDescending { it.scheduleStartDate.toDate() } // scheduleEndDate가 가까운 순으로 정렬

    Column(modifier = Modifier.fillMaxSize()
        .verticalScroll(rememberScrollState()) // 스크롤 가능하게 설정
        .padding(16.dp)) {
        // 다가오는 여행
        if(upcomingTrips.size !=0)
        Column {
            Text(text = "다가오는 여행", fontSize = 20.sp,  fontFamily = CustomFont.customFontBold)
            upcomingTrips.forEach { upComingTrip ->
                TripItem(trip = upComingTrip)
            }
        }

        Spacer(modifier = Modifier.height(16.dp)) // 두 섹션 간격
        if(pastTrips.size !=0)
        // 지난 여행
        Column {
            Text(text = "지난 여행", fontSize = 20.sp, fontFamily = CustomFont.customFontBold)
            pastTrips.forEach { pastTrip ->
                TripItem(trip = pastTrip)
            }
        }
    }
}

@Composable
fun TripItem(trip: TripScheduleModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        // 이미지 자리
        Box(
            modifier = Modifier
                .size(60.dp) // 이미지 크기
                .background(Color.Gray) // 이미지 자리만 만든다고 가정
        )

        // 여행 정보 (지역명, 시작일, 종료일)
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp)
        ) {
            Text(text = "${trip.scheduleCity} 여행", fontFamily = CustomFont.customFontRegular)
            Text(
                text = "${formatDate(trip.scheduleStartDate)} - ${formatMonthDayDate(trip.scheduleEndDate)}",
                fontFamily = CustomFont.customFontLight
            )
        }

        // 팝업 메뉴 (점 3개 버튼)
        IconButton(onClick = { /* 팝업 메뉴 동작 */ }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Menu"
            )
        }
    }
}

// 날짜 포맷 함수
fun formatDate(timestamp: Timestamp): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(timestamp.toDate())
}

// 날짜 포맷 함수
fun formatMonthDayDate(timestamp: Timestamp): String {
    val sdf = SimpleDateFormat("MM-dd", Locale.getDefault())
    return sdf.format(timestamp.toDate())
}