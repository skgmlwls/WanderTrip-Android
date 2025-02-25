package com.lion.wandertrip.presentation.schedule_select_item

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.model.ScheduleItem
import com.lion.wandertrip.model.TripItemModel
import com.lion.wandertrip.service.TripScheduleService
import com.lion.wandertrip.util.ContentTypeId
import com.lion.wandertrip.util.RouletteScreenName
import com.lion.wandertrip.util.SharedTripItemList
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleSelectItemViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    val tripScheduleService: TripScheduleService
) : ViewModel() {

    val application = context as TripApplication

    // 여행지 추가할 날짜
    val scheduleDate = mutableStateOf<Timestamp>(Timestamp.now())
    // 일정 Doc Id
    val tripScheduleDocId = mutableStateOf("")

    // 여행지 항목 리스트
    val tripItemList = mutableStateListOf<TripItemModel>()

    // 🔽 로딩 상태 추가
    val isLoading = mutableStateOf(false)

    // 관심 지역 목록
    val userLikeList = mutableStateListOf<String>()


    // 이전 화면 으로 이동 (일정 상세 화면)
    fun backScreen() {
        application.navHostController.popBackStack()
    }

    // 유저 일정 리스트들 옵저버
//    fun observeUserScheduleDocIdList() {
//        val firestore = FirebaseFirestore.getInstance()
//        // application.loginUserModel.userDocId 를 통해 유저 문서 ID 획득 (null 아님을 가정)
//        val userDocId = application.loginUserModel.userDocId
//        val userDocRef = firestore.collection("UserData").document(userDocId)
//
//        // 문서 변경 감지 리스너 등록
//        userDocRef.addSnapshotListener { snapshot, error ->
//            if (error != null) {
//                Log.e("observeUserData", "Error: ${error.message}")
//                return@addSnapshotListener
//            }
//            if (snapshot != null && snapshot.exists()) {
//                // userLikeList 필드를 List<String> 형태로 가져오기 (없으면 빈 리스트)
//                val likeItem = snapshot.get("userLikeList") as? List<String> ?: emptyList()
//
//                // 기존 리스트 클리어 후 업데이트
//                userLikeList.clear()
//                userLikeList.addAll(likeItem)
//            }
//        }
//    }

    // 유저 관심 지역 옵저브
    fun observeUserLikeList() {
        val firestore = FirebaseFirestore.getInstance()
        val userDocId = application.loginUserModel.userDocId
        val userLikeCollectionRef = firestore.collection("UserData")
            .document(userDocId)
            .collection("UserLikeList")

        userLikeCollectionRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("observeUserLikeList", "Error: ${error.message}")
                return@addSnapshotListener
            }
            if (snapshot != null) {
                // 각 문서의 "contentId" 값을 추출합니다.
                val likeItems = snapshot.documents.mapNotNull { doc ->
                    doc.getString("contentId")
                }
                // 기존 리스트를 클리어하고 최신 값으로 업데이트합니다.
                userLikeList.clear()
                userLikeList.addAll(likeItems)
                Log.d("observeUserLikeList", "userLikeList updated: $userLikeList")
            }
        }
    }

    // 여행지 항목 가져 오기
    fun loadTripItems(serviceKey: String, areaCode: String, contentTypeId: String) {
        viewModelScope.launch {
            isLoading.value = true // ✅ 로딩 시작

            val tripItems = async(Dispatchers.IO) {
                tripScheduleService.loadTripItems(serviceKey, areaCode, contentTypeId)
            }.await()

            if (tripItems != null) {
                SharedTripItemList.sharedTripItemList.clear()
                SharedTripItemList.sharedTripItemList.addAll(tripItems)
            }

            isLoading.value = false // ✅ 로딩 완료
        }
    }

    // 관심 지역 추가
    fun addLikeItem(likeItemContentId: String) {
        viewModelScope.launch {
            val work1 = async(Dispatchers.IO) {
                tripScheduleService.addLikeItem(application.loginUserModel.userDocId, likeItemContentId)
            }.await()
        }
    }

    // 관심 지역 삭제
    fun removeLikeItem(likeItemContentId: String) {
        viewModelScope.launch {
            val work1 = async(Dispatchers.IO) {
                tripScheduleService.removeLikeItem(application.loginUserModel.userDocId, likeItemContentId)
            }.await()
        }
    }

    // 일정에 여행지 항목 추가
    fun addTripItemToSchedule(tripItemModel: TripItemModel) {

        viewModelScope.launch {
            val work1 = async(Dispatchers.IO) {
                val scheduleItem = ScheduleItem(
                    itemTitle = tripItemModel.title,
                    itemType = when(tripItemModel.contentTypeId) {
                        ContentTypeId.TOURIST_ATTRACTION.contentTypeCode.toString() -> "관광지"
                        ContentTypeId.RESTAURANT.contentTypeCode.toString() -> "음식점"
                        ContentTypeId.ACCOMMODATION.contentTypeCode.toString() -> "숙소"
                        else -> ""
                    },
                    itemDate = scheduleDate.value,
                    itemLongitude = tripItemModel.mapLong,
                    itemLatitude = tripItemModel.mapLat,
                    itemContentId = tripItemModel.contentId,
                )

                tripScheduleService.addTripItemToSchedule(tripScheduleDocId.value, scheduleDate.value, scheduleItem)
            }.await()
            application.navHostController.popBackStack()
        }
    }

    // 룰렛 화면으로 이동
    fun moveToRouletteItemScreen(tripScheduleDocId: String, areaName: String, areaCode: Int) {
        application.navHostController.navigate(
            "${RouletteScreenName.ROULETTE_ITEM_SCREEN.name}?" +
                    "tripScheduleDocId=${tripScheduleDocId}&areaName=${areaName}&areaCode=${areaCode}&scheduleDate=${scheduleDate.value.seconds}"
        )
    }



}