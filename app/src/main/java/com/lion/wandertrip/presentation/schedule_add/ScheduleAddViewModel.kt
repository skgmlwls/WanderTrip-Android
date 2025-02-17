package com.lion.wandertrip.presentation.schedule_add

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.util.ScheduleScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ScheduleAddViewModel @Inject constructor(
    @ApplicationContext val context: Context,
) : ViewModel() {

    val application = context as TripApplication

    // 일정 제목
    val scheduleTitle = mutableStateOf("")
    // 일정 시작 날짜
    val scheduleStartDate = mutableStateOf<Timestamp>(Timestamp.now())
    // 일정 종료 날짜
    val scheduleEndDate = mutableStateOf<Timestamp>(Timestamp.now())

    // ✅ Timestamp -> "YYYY.MM.DD" 형식 변환 함수
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatTimestampToDateString(timestamp: Timestamp): String {
        val localDate = Instant.ofEpochMilli(timestamp.seconds * 1000)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd") // ✅ 년-월-일 포맷 적용
        return localDate.format(formatter)
    }

    fun moveToScheduleCitySelectScreen() {
        val formattedTitle = scheduleTitle.value
        val startTimestamp = scheduleStartDate.value.seconds // 🔹 Timestamp -> Long 변환
        val endTimestamp = scheduleEndDate.value.seconds // 🔹 Timestamp -> Long 변환

        application.navHostController.navigate(
            "${ScheduleScreenName.SCHEDULE_CITY_SELECT_SCREEN.name}?" +
                    "scheduleTitle=$formattedTitle" +
                    "&scheduleStartDate=$startTimestamp" +
                    "&scheduleEndDate=$endTimestamp"
        )
    }

}