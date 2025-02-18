package com.lion.wandertrip.presentation.bottom.trip_note_page

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.model.TripNoteModel
import com.lion.wandertrip.util.MainScreenName
import com.lion.wandertrip.util.TripNoteScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class TripNoteViewModel @Inject constructor(
    @ApplicationContext val context: Context,
) : ViewModel() {

    val tripApplication = context as TripApplication

    // TopAppBar의 타이틀
    val topAppBarTitle = mutableStateOf("")

    // 글 목록을 구성하기 위한 상태 관리 변수
    var tripNoteList = mutableStateListOf<TripNoteModel>()

    // + 버튼(fab 버튼)을 눌렀을 때
    fun addButtonOnClick(){
        val scheduleTitle = "" // 기본값 설정
        // 여행기 추가하는 화면으로 이동
        tripApplication.navHostController.navigate("${TripNoteScreenName.TRIP_NOTE_WRITE.name}/$scheduleTitle")
    }

    // 각 항목을 눌렀을 때
    fun listItemOnClick(){
        // 여행기 상세보기 화면으로 이동 (각 항목의 문서 id를 전달... 추후에)
        tripApplication.navHostController.navigate(TripNoteScreenName.TRIP_NOTE_DETAIL.name)
        // tripApplication.navHostController.navigate(TripNoteScreenName.TRIP_NOTE_OTHER_SCHEDULE.name)
    }

    // 임시 데이터를 리사이클러뷰에 띄움
    fun gettingTripNoteData() {
        tripNoteList.clear() // 기존 리스트 초기화
        tripNoteList = mutableStateListOf(
            TripNoteModel(
                userNickname = "닉네임1",
                tripNoteTitle = "제목1",
                tripNoteContent = "내용내용",
                tripNoteImage = listOf("http://tong.visitkorea.or.kr/cms/resource/85/2504485_image2_1.jpg"),
            ),
            TripNoteModel(
                userNickname = "닉네임2",
                tripNoteTitle = "제목2",
                tripNoteContent = "내용내용2",
                tripNoteImage = listOf("http://tong.visitkorea.or.kr/cms/resource/85/2504485_image2_1.jpg", "http://tong.visitkorea.or.kr/cms/resource/85/2504485_image2_1.jpg"),
            ),
        )
    }

}