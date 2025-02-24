package com.lion.wandertrip.presentation.schedule_detail_page

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
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


    // val tripSchedule = TripScheduleModel()
    // 일정 데이터
    val tripSchedule = mutableStateOf(TripScheduleModel())
    val tripScheduleItems = mutableStateListOf<ScheduleItem>()

    val isLoading = mutableStateOf(true) // ✅ 로딩 상태 추가

    // Firestore 리스너 (변경 감지용)
    private var scheduleItemsListener: ListenerRegistration? = null

    // 서브컬렉션 데이터 변경 감지 및 UI 업데이트
    fun observeTripScheduleItems() {
        // Firestore 참조
        val firestore = FirebaseFirestore.getInstance()

        // 기존 리스너 제거 (중복 방지)
        scheduleItemsListener?.remove()

        // 새로운 리스너 추가 (데이터 변경 감지)
        scheduleItemsListener = firestore.collection("TripSchedule")
            .document(tripScheduleDocId.value)
            .collection("TripScheduleItem")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firestore Error", "❌ 서브컬렉션 리스닝 실패: ${error.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val updatedList = snapshot.documents.mapNotNull { it.toObject(ScheduleItem::class.java) }

                    // 데이터가 바뀌면 업데이트
                    tripScheduleItems.clear()
                    tripScheduleItems.addAll(updatedList)

                    Log.d("Firestore Update", "✅ 일정 항목 업데이트됨: ${tripScheduleItems.size}개")
                }
            }
    }

    // 일정 상세 정보 가져오기
    fun getTripSchedule() {
        viewModelScope.launch {
            // isLoading.value = true // ✅ 로딩 시작

            val work1 = async(Dispatchers.IO) {
                tripScheduleService.getTripSchedule(tripScheduleDocId.value)
            }.await()

            if (work1 != null) {
                tripSchedule.value = work1
            } else {
                Log.d("ScheduleViewModel", "해당 문서가 없습니다.")
            }

            // Firestore Snapshot Listener 활성화
            observeTripScheduleItems()

            isLoading.value = false // ✅ 로딩 완료
        }
    }

    // 일정 항목 삭제 후 itemIndex 재조정
    fun removeTripScheduleItem(scheduleDocId: String, itemDocId: String, itemDate: Timestamp) {
        Log.d("removeTripScheduleItem", "scheduleDocId: $scheduleDocId, itemDocId: $itemDocId")
        viewModelScope.launch {
            val work1 = async(Dispatchers.IO) {
                tripScheduleService.removeTripScheduleItem(scheduleDocId, itemDocId, itemDate)
            }.await()
        }
    }

    // Firestore 리스너 해제 (메모리 누수 방지)
    override fun onCleared() {
        super.onCleared()
        scheduleItemsListener?.remove()
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
    fun moveToScheduleSelectItemScreen(itemCode: Int, scheduleDate: Timestamp) {
        application.navHostController.navigate(
            "${ScheduleScreenName.SCHEDULE_SELECT_ITEM_SCREEN.name}?" +
                "itemCode=${itemCode}&areaName=${areaName.value}&areaCode=${areaCode.intValue}&scheduleDate=${scheduleDate.seconds}&tripScheduleDocId=${tripScheduleDocId.value}")
    }

    // 함께 하는 친구 목록으로 이동
    fun moveToScheduleDetailFriendsScreen(scheduleDocId: String) {
        application.navHostController.navigate(
            "${ScheduleScreenName.SCHEDULE_DETAIL_FRIENDS_SCREEN.name}?" +
                "scheduleDocId=${scheduleDocId}"
        )
    }

    // 일정 항목 후기 화면으로 이동
    fun moveToScheduleItemReviewScreen(
        tripScheduleDocId: String,
        scheduleItemDocId: String,
        scheduleItemTitle: String,
    ) {
        application.navHostController.navigate(
            ScheduleScreenName.SCHEDULE_ITEM_REVIEW_SCREEN.name +
                    "/$tripScheduleDocId/$scheduleItemDocId/$scheduleItemTitle"
        )
    }

    // 이전 화면 으로 이동 (메인 일정 화면)
    fun backScreen() {
        application.navHostController.popBackStack()
    }

}