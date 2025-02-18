package com.lion.wandertrip.presentation.trip_note_detail_page

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.model.TripNoteModel
import com.lion.wandertrip.model.TripNoteReplyModel
import com.lion.wandertrip.model.TripScheduleModel
import com.lion.wandertrip.model.UserModel
import com.lion.wandertrip.util.TripNoteScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@HiltViewModel
class TripNoteDetailViewModel @Inject constructor(
    @ApplicationContext val context: Context,
) : ViewModel() {

    var tripScheduleDetailList = mutableStateListOf<TripScheduleModel>()
    var tripNoteDetailList = mutableStateListOf<TripNoteModel>()
    var tripNoteReplyList = mutableStateListOf<TripNoteReplyModel>()

    val tripApplication = context as TripApplication



    // 리사이클러뷰 임시 데이터 리스트 (일정), 그 트립노트에 있는 사람걸로.....나중에
    fun gettingTripScheduleDetailData() {
        tripScheduleDetailList = mutableStateListOf(
            TripScheduleModel(
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

    // 리사이클러뷰 임시데이터 (여행기)
    fun gettingTripNoteDetailData() {
        tripNoteDetailList = mutableStateListOf(
            TripNoteModel(
                userNickname = "땡떙sla",
                tripNoteTitle = "뒤죽박죽 제주 여행기",
                tripNoteContent = "제주 첫 여행입니다.....어쩌구저쩌루~~~~~~~~~~~\n완전 추천추천",
                tripNoteImage = listOf("http://tong.visitkorea.or.kr/cms/resource/85/2504485_image2_1.jpg", "http://tong.visitkorea.or.kr/cms/resource/85/2504485_image2_1.jpg"),
            )
        )
    }

    // 리사이클러뷰 임시데이터 (댓글)
    fun gettingTripNoteReplyData() {
        tripNoteReplyList = mutableStateListOf(
            TripNoteReplyModel(
                userNickname = "땡떙",
                replyText = "추천 감사합니다!",
            ),
            TripNoteReplyModel(
                userNickname = "땡떙",
                replyText = "와~~~ 재밌게 보고 가요",
            ),
            TripNoteReplyModel(
                userNickname = "땡떙",
                replyText = "저런 장소는 처음 보네요",
            ),
            TripNoteReplyModel(
                userNickname = "땡떙",
                replyText = "맛집 추천해주세요",
            )
        )
    }


    // 뒤로 가기 버튼
    fun navigationButtonClick(){
        tripApplication.navHostController.popBackStack()
    }

    // 휴지통 아이콘
    fun deleteButtonClick(){
        // 본인 여행기면 아이콘 보이고.... 삭제 그거 ... 다이얼로그 띄워
    }

    // 일정 담기 아이콘
    fun bringTripNote(){
        tripApplication.navHostController.navigate(TripNoteScreenName.TRIP_NOTE_SELECT_DOWN.name)
    }

    // 닉네임 클릭하면 그 사람 여행기 리스트 화면으로 이동
    fun clickNickname(){
        tripApplication.navHostController.navigate(TripNoteScreenName.TRIP_NOTE_OTHER_SCHEDULE.name)
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