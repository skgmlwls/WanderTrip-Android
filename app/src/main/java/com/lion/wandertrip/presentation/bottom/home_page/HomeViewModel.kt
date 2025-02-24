package com.lion.wandertrip.presentation.bottom.home_page

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateListOf
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.model.SimpleTripItemModel
import com.lion.wandertrip.model.TripItemModel
import com.lion.wandertrip.model.TripNoteModel
import com.lion.wandertrip.model.TripScheduleModel
import com.lion.wandertrip.presentation.my_interesting_page.used_dummy_data.InterestingDummyData
import com.lion.wandertrip.presentation.my_trip_note.MyTripNoteViewModel
import com.lion.wandertrip.service.TripScheduleService
import com.lion.wandertrip.util.MainScreenName
import com.lion.wandertrip.util.TripNoteScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext context: Context,
    val tripScheduleService: TripScheduleService,
) : ViewModel(){

    val tripApplication = context as TripApplication

    // 여행지 항목 리스트
    var tripItemList = mutableStateListOf<TripItemModel>()
    
    var popularTripList = mutableStateListOf<SimpleTripItemModel>()

    var myNoteItemList = mutableStateListOf<TripNoteModel>()
    
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

    // 일정 데이터 가져 오는 메소드 호출
    fun gettingTripScheduleData() {
        tripItemList = mutableStateListOf(
            TripItemModel(
                title = "제주 힐링여행",
                firstImage = "http://tong.visitkorea.or.kr/cms/resource/50/3116450_image2_1.jpg",
            ),
            TripItemModel(
                title = "서울 힐링여행",
                firstImage = "http://tong.visitkorea.or.kr/cms/resource/50/3116450_image2_1.jpg",
            )
        )
    }


    fun backScreen() {
        tripApplication.navHostController.popBackStack()
    }

    // 내 리뷰 화면 전환
    fun onClickIconSearch() {
        tripApplication.navHostController.navigate(MainScreenName.MAIN_SCREEN_SEARCH.name)
    }

    fun onClickTrip() {
        tripApplication.navHostController.navigate(MainScreenName.MAIN_SCREEN_DETAIL.name)
    }

    fun onClickTripNote(documentId : String) {
        tripApplication.navHostController.navigate("${TripNoteScreenName.TRIP_NOTE_DETAIL.name}/${documentId}")
    }
}