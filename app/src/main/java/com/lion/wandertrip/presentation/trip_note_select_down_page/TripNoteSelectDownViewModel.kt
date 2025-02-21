package com.lion.wandertrip.presentation.trip_note_select_down_page

import android.content.Context
import android.os.Build
import android.util.Log
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
import com.lion.wandertrip.service.TripNoteService
import com.lion.wandertrip.util.MainScreenName
import com.lion.wandertrip.util.ScheduleScreenName
import com.lion.wandertrip.util.TripNoteScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@HiltViewModel
class TripNoteSelectDownViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    val tripNoteService: TripNoteService,
) : ViewModel() {

    // 내 일정
    var tripNoteMyScheduleList = mutableStateListOf<TripScheduleModel?>()

    val tripApplication = context as TripApplication
    val userNickName = tripApplication.loginUserModel.userNickName

    // 클릭 일정 문서id
    var scheduleDocId = mutableStateOf("")


    // 리사이클러뷰 데이터 리스트 (다가오는 내 일정 리스트)
    fun gettingTripNoteDetailData() {

        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                tripNoteService.gettingUpcomingScheduleList(userNickName)
            }
            val recyclerViewList  = work1.await()

            // 상태 관리 변수에 담아준다.
            tripNoteMyScheduleList.clear()
            tripNoteMyScheduleList.addAll(recyclerViewList)
        }

    }

    // 뒤로 가기 버튼
    fun navigationButtonClick(){
        tripApplication.navHostController.popBackStack()
    }

    // 일정 제목 입력 화면으로 이동
    fun goScheduleTitleButtonClick(tripNoteScheduleDocId : String, documentId : String){
        // 일정 제목 + 날짜선텍 입력 화면으로 이동 - 추후에 tripNoteScheduleDocId얘도 같이 전달해야됨
        tripApplication.navHostController.navigate(ScheduleScreenName.SCHEDULE_ADD_SCREEN.name)

        // 일정 담기면 그 여행기의 tripNoteScrapCount 증가시키기
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                tripNoteService.addTripNoteScrapCount(documentId)
            }
            work1.join()
        }
    }

    // 다가오는 일정 클릭하면,, 그 일정 문서id 받기
    fun gettingSelectId(tripScheduleDocId: String){
        scheduleDocId.value = tripScheduleDocId
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

    // 다이얼로그 상태 관리
    val showDialogState = mutableStateOf(false)


    // 다이얼로그 확인 버튼 클릭 시 동작
    fun onConfirmClick() {
        showDialogState.value = false
    }

    // 완료 버튼 클릭 시 다이얼로그 상태 변경 , 담은 일정 문서id랑 내 일정 중에 담아갈 일정 id
    fun selectFinishButtonClick(tripNoteScheduleDocId : String, scheduleDocId : String, documentId : String ) {


        Log.d("TripNoteSelectDownViewModel - 담아갈 일정", "tripNoteScheduleDocId: ${tripNoteScheduleDocId}")
        Log.d("TripNoteSelectDownViewModel - 내 일정 중에 담아가려고 일정", "scheduleDocId: $scheduleDocId")
        Log.d("TripNoteSelectDownViewModel - 담아갈 여행기", "documentId: $documentId")

        // 스케쥴 디테일 페이지로 이동 - 추후에 tripNoteScheduleDocId, scheduleDocId 각각 전달..
//        tripApplication.navHostController.navigate(
//            "${ScheduleScreenName.SCHEDULE_DETAIL_SCREEN.name}?tripScheduleDocId=null&areaName=null&areaCode=null"
//        )



        // 일정 담기면 그 여행기의 tripNoteScrapCount 증가시키기
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                tripNoteService.addTripNoteScrapCount(documentId)
            }
            work1.join()
        }

    }


    // 새 일정 담기 버튼 다이얼로그 취소 버튼 클릭 시 동작
    fun onDismissClick() {
        showDialogState.value = false
    }


    // 다이얼로그 상태 관리
    val showDialogNotState = mutableStateOf(false)

    // 새 일정 담기 버튼 다이얼로그 취소 버튼 클릭 시 동작
    fun onDismissNotClick() {
        showDialogNotState.value = false
    }


    // 다이얼로그 확인 버튼 클릭 시 동작
    fun onConfirmNotClick() {
        showDialogNotState.value = false
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


}