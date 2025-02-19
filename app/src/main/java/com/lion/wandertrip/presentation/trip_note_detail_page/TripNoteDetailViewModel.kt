package com.lion.wandertrip.presentation.trip_note_detail_page

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.model.ScheduleItem
import com.lion.wandertrip.model.TripNoteModel
import com.lion.wandertrip.model.TripNoteReplyModel
import com.lion.wandertrip.model.TripScheduleModel
import com.lion.wandertrip.model.UserModel
import com.lion.wandertrip.service.TripNoteService
import com.lion.wandertrip.util.ScheduleScreenName
import com.lion.wandertrip.util.TripNoteScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
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
    val tripNoteService : TripNoteService
) : ViewModel() {

    // 여행기 정보를 담을 변수
    lateinit var tripNoteModel:TripNoteModel

    // 여행기 제목
    val textFieldTripNoteSubject = mutableStateOf(" ")
    // 여행기 작성자 닉네임
    val textFieldTripNoteNickName = mutableStateOf(" ")
    // 여행기 내용
    val textFieldTripNoteContent = mutableStateOf(" ")
    // 일정 문서 id
    val textFieldTripNoteScheduleDocId = mutableStateOf(" ")
    // 여행기 사진 uri
    val showImageUri = mutableStateOf(mutableListOf<Uri?>())

    // 휴지통 아이콘을 보여줄 것인지에 대한 상태 변수
    val showTopAppBarDeleteMenuState = mutableStateOf(false)
    // 이미지 요소를 띄울 것인지에 대한 상태 변수
    val showImageState = mutableStateOf(false)

    // 작성한 댓글 내용
    val textFieldTripNoteReply = mutableStateOf("")
    // 댓글 리스트
    var tripNoteReplyList = mutableStateListOf<TripNoteReplyModel>()


    val tripApplication = context as TripApplication

    val nickName = tripApplication.loginUserModel.userNickName


    var tripNoteDetailList = mutableStateListOf<TripNoteModel>()
    //var tripNoteReplyList = mutableStateListOf<TripNoteReplyModel>()


    val areaName = mutableStateOf("")
    val areaCode = mutableIntStateOf(0)

    // 테스트 용 데이터 ///////////////////////////////////////////////////////////////////////////////
    val startDate = Timestamp(1738627200, 0)
    val endDate = Timestamp(1738886400, 0)
    val scheduleDateList = generateDateList(startDate, endDate)




    // 여행기 리사이클러뷰 데이터 가져오기
    fun gettingTripNoteDetailData(documentId : String) {
        // 서버에서 데이터를 가져온다.
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                tripNoteService.selectTripNoteDataOneById(documentId)
            }
            tripNoteModel = work1.await()

            textFieldTripNoteNickName.value = tripNoteModel.userNickname
            textFieldTripNoteSubject.value = tripNoteModel.tripNoteTitle
            textFieldTripNoteContent.value = tripNoteModel.tripNoteContent
            textFieldTripNoteScheduleDocId.value = tripNoteModel.tripScheduleDocumentId

            // 만약 작성자와 로그인한 사용자가 같다면 메뉴를 보여준다.
            if(tripNoteModel.userNickname == tripApplication.loginUserModel.userNickName){
                showTopAppBarDeleteMenuState.value = true
            }

            // 첨부 이미지가 있다면
            if(tripNoteModel.tripNoteImage.isNotEmpty()){
                val work2 = async(Dispatchers.IO){
                    // tripNoteService.gettingImage(tripNoteModel.tripNoteImage)
                    // 이미지 URI 리스트를 하나씩 처리하여 MutableList<Uri?>로 변환
                    tripNoteModel.tripNoteImage.map { imagePath ->
                        tripNoteService.gettingImage(imagePath) // 개별 이미지 가져오기
                    }
                }

                showImageUri.value = work2.await().toMutableList()
                showImageState.value = showImageUri.value.isNotEmpty()

            }
        }

    }

    // 댓글 등록하기 버튼
    fun addReplyClick(documentId : String){
        // 댓글 작성한 여행기 문서 id
        val tripNoteDocumentId = documentId
        // 작성자 닉네임
        val userNickname = textFieldTripNoteNickName.value
        // 댓글 내용
        val replyText = textFieldTripNoteReply.value
        // 댓글 작성 시간
        val replyTimeStamp: Timestamp = Timestamp.now()

        // 서버에 저장할 댓글 데이터
        val tripNoteReplyModel = TripNoteReplyModel()
        tripNoteReplyModel.tripNoteDocumentId = tripNoteDocumentId
        tripNoteReplyModel.userNickname = userNickname
        tripNoteReplyModel.replyText = replyText
        tripNoteReplyModel.replyTimeStamp = replyTimeStamp

        // 저장하기
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                tripNoteService.addTripNoteReplyData(tripNoteReplyModel)
            }
        }
    }

    // 댓글 리스트 데이터 가져오기
    fun gettingTripNoteReplyData(documentId : String) {
        // 서버에서 데이터를 가져온다.
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                tripNoteService.selectReplyDataOneById(documentId)
            }
            val result = work1.await()
            tripNoteReplyList.clear()
            tripNoteReplyList.addAll(result)
        }
    }

    // 댓글 삭제하기
    fun deleteTripNoteReply(){

    }









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



    // 뒤로 가기 버튼
    fun navigationButtonClick(){
        tripApplication.navHostController.popBackStack()
    }

    // 휴지통 아이콘
    fun deleteButtonClick(){
        // 삭제 그거 ... 다이얼로그 띄워

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