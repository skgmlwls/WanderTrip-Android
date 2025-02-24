package com.lion.wandertrip.presentation.bottom.schedule_page

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.model.TripScheduleModel
import com.lion.wandertrip.util.ScheduleScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    @ApplicationContext val context: Context,
) : ViewModel() {

    val application = context as TripApplication
    
    // 일정 데이터
    var tripScheduleList = mutableStateListOf<TripScheduleModel>()


    // 일정 데이터 가져 오는 메소드 호출
    fun gettingTripScheduleData() {
        tripScheduleList = mutableStateListOf(
            TripScheduleModel(
                scheduleTitle = "제주 힐링여행",
                // scheduleStartDate = "2025.03.01",
                scheduleCity = "제주",
                // scheduleEndDate = "2025.03.05",
            ),
            TripScheduleModel(
                scheduleTitle = "서울 힐링여행",
                // scheduleStartDate = "2025.03.06",
                scheduleCity = "서울",
                // scheduleEndDate = "2025.03.11",
            )
        )
    }

    // 일정 추가 버튼 클릭 이벤트
    fun addIconButtonEvent() {
        application.navHostController.navigate( "${ScheduleScreenName.SCHEDULE_ADD_SCREEN.name}/")
    }

    // ✅ Timestamp -> "YYYY.MM.DD" 형식 변환 함수
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatTimestampToDateString(timestamp: Timestamp): String {
        val localDate = Instant.ofEpochMilli(timestamp.seconds * 1000)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd") // ✅ 년-월-일 포맷 적용
        return localDate.format(formatter)
    }

}