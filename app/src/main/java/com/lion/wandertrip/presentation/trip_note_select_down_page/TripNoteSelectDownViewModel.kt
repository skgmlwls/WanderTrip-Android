package com.lion.wandertrip.presentation.trip_note_select_down_page

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.lion.a02_boardcloneproject.component.CustomAlertDialog
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.model.TripNoteModel
import com.lion.wandertrip.model.TripScheduleModel
import com.lion.wandertrip.util.TripNoteScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@HiltViewModel
class TripNoteSelectDownViewModel @Inject constructor(
    @ApplicationContext val context: Context,
) : ViewModel() {

    // 내 일정
    var tripNoteMyScheduleList = mutableStateListOf<TripScheduleModel>()


    val tripApplication = context as TripApplication



    // 리사이클러뷰 임시 데이터 리스트 (다가오는 내 일정 리스트)
    fun gettingTripNoteDetailData() {
        // 나중에 내 여행 + 아직 시작 안한 일정 걸러내기
        tripNoteMyScheduleList = mutableStateListOf(
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
            ),
            TripScheduleModel(
                scheduleTitle = "서울 힐링여행",
                scheduleStartDate = Timestamp.now(),
                scheduleCity = "서울",
                scheduleEndDate = Timestamp.now(),
            ),
            TripScheduleModel(
                scheduleTitle = "서울 힐링여행",
                scheduleStartDate = Timestamp.now(),
                scheduleCity = "서울",
                scheduleEndDate = Timestamp.now(),
            ),
            TripScheduleModel(
                scheduleTitle = "서울 힐링여행",
                scheduleStartDate = Timestamp.now(),
                scheduleCity = "서울",
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

    // 뒤로 가기 버튼
    fun navigationButtonClick(){
        tripApplication.navHostController.popBackStack()
    }

    // 다이얼로그 상태 관리
    val showDialogState = mutableStateOf(false)

    // 완료 버튼 클릭 시 다이얼로그 상태 변경
    fun selectFinishButtonClick() {
        showDialogState.value = true
    }

    // 다이얼로그 확인 버튼 클릭 시 동작
    fun onConfirmClick() {
        showDialogState.value = false
        // 일정 상세로 이동
    }

    // 새 일정 담기 버튼 다이얼로그 취소 버튼 클릭 시 동작
    fun onDismissClick() {
        showDialogState.value = false
    }

    // 다이얼로그 상태 관리
    val showDialogStateNew = mutableStateOf(false)

    // 새 일정 담기 버튼 클릭 시 다이얼로그 상태 변경
    fun selectNewButtonClick() {
        showDialogStateNew.value = true
    }

    // 새 일정 담기 버튼 다이얼로그 확인 버튼 클릭 시 동작
    fun onConfirmNewClick() {
        showDialogStateNew.value = false
        // 일정 제목 그거로 이동
    }

    // 새 일정 담기 버튼 다이얼로그 취소 버튼 클릭 시 동작
    fun onDismissNewClick() {
        showDialogStateNew.value = false
    }

    // 일정 제목 입력 화면으로 이동
    fun goScheduleTitleButtonClick(){
        // tripApplication.navHostController.navigate(TripNoteScreenName.TRIP_NOTE_OTHER_SCHEDULE.name)
    }

    // 새 여행 만들어 담기 버튼
    fun newTripButtonClick(){
        // tripApplication.navHostController.navigate(TripNoteScreenName.TRIP_NOTE_OTHER_SCHEDULE.name)
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