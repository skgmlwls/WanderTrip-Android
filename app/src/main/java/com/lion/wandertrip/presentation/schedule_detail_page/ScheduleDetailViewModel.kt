package com.lion.wandertrip.presentation.schedule_detail_page

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.model.ScheduleItem
import com.lion.wandertrip.model.TripScheduleModel
import com.lion.wandertrip.service.TripScheduleService
import com.lion.wandertrip.util.ScheduleScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ScheduleDetailViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    val tripScheduleService: TripScheduleService
) : ViewModel() {
    val application = context as TripApplication

    // 선택한 도시
    val areaName = mutableStateOf("")
    // 선택한 도시 코드
    val areaCode = mutableIntStateOf(0)
    // 일정 문서 ID
    val tripScheduleDocId = mutableStateOf("")

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


    // val tripSchedule = TripScheduleModel()
    // 일정 데이터
    val tripSchedule = mutableStateOf(TripScheduleModel())
    val tripScheduleItems = mutableStateListOf<ScheduleItem>()

    // 일정 데이터 가져 오기
    fun getTripSchedule() {
        viewModelScope.launch {
            val work1 = async(Dispatchers.IO) {
                tripScheduleService.getTripSchedule(tripScheduleDocId.value)
            }.await()
            if (work1 != null) {
                // 성공적으로 가져온 Model 처리
                tripSchedule.value = work1
            } else {
                Log.d("ScheduleViewModel", "해당 문서가 없습니다.")
            }

            val work2 = async(Dispatchers.IO) {
                tripScheduleService.getTripScheduleItems(tripScheduleDocId.value)
            }.await()
            if (work2 != null) {
                tripScheduleItems.addAll(work2)
            }

        }
    }

    // 도시 이름과 코드를 설정 하는 함수
    fun addAreaData(tripScheduleDocId: String, areaName: String, areaCode: Int) {
        this.tripScheduleDocId.value = tripScheduleDocId
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

    // 함께 하는 친구 목록으로 이동
    fun moveToScheduleDetailFriendsScreen(scheduleDocId: String) {
        application.navHostController.navigate(
            "${ScheduleScreenName.SCHEDULE_DETAIL_FRIENDS_SCREEN.name}?" +
                "scheduleDocId=${scheduleDocId}"
        )
    }

    // 이전 화면 으로 이동 (메인 일정 화면)
    fun backScreen() {
        application.navHostController.popBackStack()
    }




    //    // 일정 모델
//    val tripSchedule = TripScheduleModel(
//        scheduleStartDate = startDate,
//        scheduleEndDate = endDate,
//        scheduleDateList = scheduleDateList,
//        scheduleItems = listOf(
//            ScheduleItem(
//                itemDate = Timestamp(1738627200, 0),
//                itemIndex = 1,
//                itemTitle = "강서습지생태공원",
//                itemType = "관광지",
//                itemLongitude = 126.8171490732,
//                itemLatitude = 37.5860879769,
//                itemImagesURL = emptyList(),
//                itemReviewText = "재미없었다",
//                itemReviewImagesURL = listOf(
//                    "http://tong.visitkorea.or.kr/cms/resource/92/2671592_image2_1.jpg",
//                    "http://tong.visitkorea.or.kr/cms/resource/92/2671592_image2_1.jpg"
//                ),
//            ),
//            ScheduleItem(
//                itemDate = Timestamp(1738627200, 0),
//                itemIndex = 2,
//                itemTitle = "서울 양천고성지",
//                itemType = "관광지",
//                itemLongitude = 126.8408278075,
//                itemLatitude = 37.5740425776,
//                itemImagesURL = emptyList(),
//                itemReviewText = "너무 재밌다",
//                itemReviewImagesURL = emptyList()
//            ),
//            ScheduleItem(
//                itemDate = Timestamp(1738713600, 0),
//                itemIndex = 1,
//                itemTitle = "개화산 호국공원",
//                itemType = "관광지",
//                itemLongitude = 126.8033171574,
//                itemLatitude = 37.5805689272,
//                itemImagesURL = emptyList(),
//                itemReviewText = "",
//                itemReviewImagesURL = emptyList()
//            )
//        )
//    )

//    {
//        "addr1": "서울특별시 강서구 양천로27길 279-23",
//        "addr2": "(방화동)",
//        "areacode": "1",
//        "booktour": "0",
//        "cat1": "A01",
//        "cat2": "A0101",
//        "cat3": "A01010500",
//        "contentid": "809490",
//        "contenttypeid": "12",
//        "createdtime": "20090924090047",
//        "firstimage": "",
//        "firstimage2": "",
//        "cpyrhtDivCd": "",
//        "mapx": "126.8171490732",
//        "mapy": "37.5860879769",
//        "mlevel": "6",
//        "modifiedtime": "20240612085631",
//        "sigungucode": "4",
//        "tel": "",
//        "title": "강서습지생태공원",
//        "zipcode": "07518",
//        "showflag": "1"
//    }
//    {
//        "addr1": "서울특별시 강서구 가양동",
//        "addr2": "",
//        "areacode": "1",
//        "booktour": "0",
//        "cat1": "A02",
//        "cat2": "A0201",
//        "cat3": "A02010200",
//        "contentid": "127216",
//        "contenttypeid": "12",
//        "createdtime": "20020510090000",
//        "firstimage": "",
//        "firstimage2": "",
//        "cpyrhtDivCd": "",
//        "mapx": "126.8408278075",
//        "mapy": "37.5740425776",
//        "mlevel": "6",
//        "modifiedtime": "20240614134720",
//        "sigungucode": "4",
//        "tel": "",
//        "title": "서울 양천고성지",
//        "zipcode": "07521",
//        "showflag": "1"
//    }
//    {
//        "addr1": "서울특별시 강서구 개화동로13길 56-33 (개화동)",
//        "addr2": "",
//        "areacode": "1",
//        "booktour": "",
//        "cat1": "A02",
//        "cat2": "A0202",
//        "cat3": "A02020700",
//        "contentid": "3385911",
//        "contenttypeid": "12",
//        "createdtime": "20241011154701",
//        "firstimage": "http://tong.visitkorea.or.kr/cms/resource/69/3383069_image2_1.JPG",
//        "firstimage2": "http://tong.visitkorea.or.kr/cms/resource/69/3383069_image3_1.JPG",
//        "cpyrhtDivCd": "Type3",
//        "mapx": "126.8033171574",
//        "mapy": "37.5805689272",
//        "mlevel": "6",
//        "modifiedtime": "20241226191724",
//        "sigungucode": "4",
//        "tel": "",
//        "title": "개화산 호국공원",
//        "zipcode": "07504",
//        "showflag": "1"
//    }

}