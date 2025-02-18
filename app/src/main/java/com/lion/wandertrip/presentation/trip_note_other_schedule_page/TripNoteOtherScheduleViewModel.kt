package com.lion.wandertrip.presentation.trip_note_other_schedule_page

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.model.TripScheduleModel
import com.lion.wandertrip.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@HiltViewModel
class TripNoteOtherScheduleViewModel @Inject constructor(
    @ApplicationContext val context: Context,
) : ViewModel() {

    var tripNoteOtherScheduleList = mutableStateListOf<TripScheduleModel>()

    var userProfileList = mutableStateListOf<UserModel>()

    val tripApplication = context as TripApplication

    // 리사이클러뷰 임시 데이터 리스트 (선택한 여행기 ... 사람의 일정)
    fun gettingTripNoteScheduleData() {
        tripNoteOtherScheduleList = mutableStateListOf(
            TripScheduleModel(
                scheduleTitle = "제주 힐링여행",
                scheduleStartDate = Timestamp.now(),
                scheduleCity = "제주",
                scheduleEndDate = Timestamp.now(),
            ),
            TripScheduleModel(
                scheduleTitle = "서울 힐링여행",
                scheduleStartDate = Timestamp.now(),
                scheduleCity = "서울",
                scheduleEndDate = Timestamp.now(),
            )
        )
    }

    // 프로필 사진 임시 데이터
    fun gettingTripNoteProfileData(){
        userProfileList = mutableStateListOf(
            UserModel(
            userProfileImageURL = ("http://tong.visitkorea.or.kr/cms/resource/85/2504485_image2_1.jpg"),
            userNickName = "땡땡"
            )
        )
    }


    // 뒤로 가기 버튼
    fun navigationButtonClick(){
        tripApplication.navHostController.popBackStack()
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