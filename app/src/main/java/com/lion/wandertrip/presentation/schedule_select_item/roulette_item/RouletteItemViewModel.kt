package com.lion.wandertrip.presentation.schedule_select_item.roulette_item

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.model.TripItemModel
import com.lion.wandertrip.util.RouletteScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class RouletteItemViewModel @Inject constructor(
    @ApplicationContext val context: Context,
) : ViewModel() {

    val application = context as TripApplication

    // ✅ 전체 여행지 목록
    val tripItemList = mutableStateListOf<TripItemModel>()

    // ✅ 선택된 여행지 목록 (룰렛 항목)
    val rouletteItemList = mutableStateListOf<TripItemModel>()

    // 여행지 선택 화면으로 이동
    fun moveToRouletteItemSelectScreen() {
        application.navHostController.navigate(RouletteScreenName.ROULETTE_ITEM_SELECT_SCREEN.name)
    }

    // 선택된 여행지 리스트 업데이트
    fun updateRouletteItemList(selectedItems: List<TripItemModel>) {
        rouletteItemList.clear()
        rouletteItemList.addAll(selectedItems)
    }
}
