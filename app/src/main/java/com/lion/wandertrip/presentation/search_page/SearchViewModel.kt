package com.lion.wandertrip.presentation.search_page

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.model.ScheduleItem
import com.lion.wandertrip.model.TripItemModel
import com.lion.wandertrip.service.TripScheduleService
import com.lion.wandertrip.util.ContentTypeId
import com.lion.wandertrip.util.MainScreenName
import com.lion.wandertrip.util.TripNoteScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    val tripScheduleService: TripScheduleService
) : ViewModel() {
    val tripApplication = context as TripApplication

    // 여행지 항목 리스트
    val tripItemList = mutableStateListOf<TripItemModel>()

    val application = context as TripApplication

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

    val recentSearches = mutableStateListOf<TripItemModel>() // 최근 검색어 리스트

    fun clearRecentSearches() {
        recentSearches.clear()
    }

    fun selectRecentSearch(tripItemModel: TripItemModel) {
        // 선택한 검색어를 현재 검색 필드에 반영 (UI에서 처리 필요)
        // 예: searchText.value = tripItemModel.title
    }

    fun removeRecentSearch(tripItemModel: TripItemModel) {
        recentSearches.remove(tripItemModel)
    }

    fun addSearchToRecent(tripItemModel: TripItemModel) {
        // 중복 검색어가 있는지 확인하고 제거 (최신 검색어를 맨 앞으로 유지)
        recentSearches.removeAll { it.title == tripItemModel.title }

        // 새로운 검색어를 리스트의 맨 앞에 추가
        recentSearches.add(0, tripItemModel)

        // 최근 검색어 개수를 제한 (예: 10개까지만 저장)
        if (recentSearches.size > 5) {
            recentSearches.removeAt(recentSearches.lastIndex)
        }
    }

    fun onClickToResult(text : String) {
        tripApplication.navHostController.navigate("${MainScreenName.MAIN_SCREEN_SEARCH_RESULT.name}/${text}")
    }
}
