package com.lion.wandertrip.presentation.schedule_select_item

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.model.ScheduleItem
import com.lion.wandertrip.model.TripItemModel
import com.lion.wandertrip.service.TripScheduleService
import com.lion.wandertrip.util.ContentTypeId
import com.lion.wandertrip.util.RouletteScreenName
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

    // 이전 화면 으로 이동 (일정 상세 화면)
    fun backScreen() {
        application.navHostController.popBackStack()
    }

    // 여행지 항목 가져 오기
    fun loadTripItems(serviceKey: String, areaCode: String, contentTypeId: String) {
        viewModelScope.launch {
            val tripItems = async(Dispatchers.IO) {
                tripScheduleService.loadTripItems(serviceKey, areaCode, contentTypeId)
            }.await()

            if (tripItems != null) {
                tripItemList.addAll(tripItems)
            }
//            tripItemList.forEach {
//                Log.d("ScheduleSelectItemViewModel", "loadTripItems: ${it.cat1}")
//            }
            Log.d("ScheduleSelectItemViewModel", "loadTripItems: ${tripItemList.size}")
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

    fun moveToRouletteItemScreen() {
        application.navHostController.navigate(RouletteScreenName.ROULETTE_ITEM_SCREEN.name)
    }



}