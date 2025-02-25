package com.lion.wandertrip.presentation.schedule_select_item.roulette_item.roulette_item_select

import android.content.Context
import androidx.lifecycle.ViewModel
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.model.TripItemModel
import com.lion.wandertrip.util.ScheduleScreenName
import com.lion.wandertrip.util.SharedTripItemList
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class RouletteItemSelectViewModel @Inject constructor(
    @ApplicationContext val context: Context,
) : ViewModel() {

    val application = context as TripApplication

    // 선택된 여행지 리스트 업데이트
    fun updateRouletteItemList(selectedItems: List<TripItemModel>) {
        SharedTripItemList.rouletteItemList.clear()
        SharedTripItemList.rouletteItemList.addAll(selectedItems)
    }

}