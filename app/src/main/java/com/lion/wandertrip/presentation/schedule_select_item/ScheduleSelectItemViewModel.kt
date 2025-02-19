package com.lion.wandertrip.presentation.schedule_select_item

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.model.TripItemModel
import com.lion.wandertrip.service.TripScheduleService
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

            tripItemList.forEach {
                Log.d("ScheduleSelectItemViewModel", "loadTripItems: ${it.cat1}")
            }

            Log.d("ScheduleSelectItemViewModel", "loadTripItems: ${tripItemList.size}")
        }
    }




}