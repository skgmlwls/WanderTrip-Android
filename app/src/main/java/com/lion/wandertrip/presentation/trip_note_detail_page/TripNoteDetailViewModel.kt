package com.lion.wandertrip.presentation.trip_note_detail_page

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.model.ScheduleItem
import com.lion.wandertrip.model.TripNoteModel
import com.lion.wandertrip.model.TripNoteReplyModel
import com.lion.wandertrip.model.TripScheduleModel
import com.lion.wandertrip.model.UserModel
import com.lion.wandertrip.util.ScheduleScreenName
import com.lion.wandertrip.util.TripNoteScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class TripNoteDetailViewModel @Inject constructor(
    @ApplicationContext val context: Context,
) : ViewModel() {

    // var tripScheduleDetailList = mutableStateListOf<TripScheduleModel>()
    var tripNoteDetailList = mutableStateListOf<TripNoteModel>()
    var tripNoteReplyList = mutableStateListOf<TripNoteReplyModel>()

    val tripApplication = context as TripApplication

    val application = context as TripApplication

    val areaName = mutableStateOf("")
    val areaCode = mutableIntStateOf(0)

    // 테스트 용 데이터 ///////////////////////////////////////////////////////////////////////////////
    val startDate = Timestamp(1738627200, 0)
    val endDate = Timestamp(1738886400, 0)
    val scheduleDateList = generateDateList(startDate, endDate)

    // 특정 기간 동안의 날짜 목록 생성 함수
    fun generateDateList(startDate: Timestamp, endDate: Timestamp): List<Timestamp> {
        val dateList = mutableListOf<Timestamp>()
        var currentTimestamp = startDate

        while (currentTimestamp.seconds <= endDate.seconds) {
            dateList.add(currentTimestamp)
            currentTimestamp = Timestamp(currentTimestamp.seconds + 86400, 0) // 하루(24시간 = 86400초) 추가
        }

        return dateList
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    // 일정 모델
    val tripScheduleDetailList = TripScheduleModel(
        scheduleStartDate = startDate,
        scheduleEndDate = endDate,
        scheduleDateList = scheduleDateList,
        scheduleItems = listOf(
            ScheduleItem(
                itemDate = Timestamp(1738627200, 0),
                itemIndex = 1,
                itemTitle = "강서습지생태공원",
                itemType = "관광지",
                itemLongitude = 126.8171490732,
                itemLatitude = 37.5860879769,
                itemImagesURL = emptyList(),
                itemReviewText = "재미없었다",
                itemReviewImagesURL = listOf(
                    "http://tong.visitkorea.or.kr/cms/resource/92/2671592_image2_1.jpg",
                    "http://tong.visitkorea.or.kr/cms/resource/92/2671592_image2_1.jpg"
                ),
            ),
            ScheduleItem(
                itemDate = Timestamp(1738627200, 0),
                itemIndex = 2,
                itemTitle = "서울 양천고성지",
                itemType = "관광지",
                itemLongitude = 126.8408278075,
                itemLatitude = 37.5740425776,
                itemImagesURL = emptyList(),
                itemReviewText = "너무 재밌다",
                itemReviewImagesURL = emptyList()
            ),
            ScheduleItem(
                itemDate = Timestamp(1738713600, 0),
                itemIndex = 1,
                itemTitle = "개화산 호국공원",
                itemType = "관광지",
                itemLongitude = 126.8033171574,
                itemLatitude = 37.5805689272,
                itemImagesURL = emptyList(),
                itemReviewText = "",
                itemReviewImagesURL = emptyList()
            )
        )
    )


    // 도시 이름과 코드를 설정 하는 함수
    fun addAreaData(areaName: String, areaCode: Int) {
        this.areaName.value = areaName
        this.areaCode.intValue = areaCode
    }

    // Timestamp를 "yyyy년 MM월 dd일" 형식의 문자열로 변환하는 함수
    fun formatTimestampToDate(timestamp: Timestamp): String {
        val sdf = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        return sdf.format(Date(timestamp.seconds * 1000))
    }

    // 일정 항목 선택 화면 으로 이동
    fun moveToScheduleSelectItemScreen(itemCode: Int) {
        application.navHostController.navigate(
            "${ScheduleScreenName.SCHEDULE_SELECT_ITEM_SCREEN.name}?" +
                    "itemCode=${itemCode}&areaName=${areaName.value}&areaCode=${areaCode.value}")
    }

    fun moveToScheduleDetailFriendsScreen(scheduleDocId: String) {
        application.navHostController.navigate(
            "${ScheduleScreenName.SCHEDULE_DETAIL_FRIENDS_SCREEN.name}?" +
                    "scheduleDocId=${scheduleDocId}"
        )
    }


    // 리사이클러뷰 임시데이터 (여행기)
    fun gettingTripNoteDetailData() {
        tripNoteDetailList = mutableStateListOf(
            TripNoteModel(
                userNickname = "땡떙sla",
                tripNoteTitle = "뒤죽박죽 제주 여행기",
                tripNoteContent = "제주 첫 여행입니다.....어쩌구저쩌루~~~~~~~~~~~\n완전 추천추천",
                tripNoteImage = listOf("http://tong.visitkorea.or.kr/cms/resource/92/2671592_image2_1.jpg", "http://tong.visitkorea.or.kr/cms/resource/85/2504485_image2_1.jpg"),
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