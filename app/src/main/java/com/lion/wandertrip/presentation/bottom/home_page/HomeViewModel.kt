package com.lion.wandertrip.presentation.bottom.home_page

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.lion.wandertrip.service.TripNoteService
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.model.SimpleTripItemModel
import com.lion.wandertrip.model.TripItemModel
import com.lion.wandertrip.model.TripNoteModel
import com.lion.wandertrip.service.TripScheduleService
import com.lion.wandertrip.util.MainScreenName
import com.lion.wandertrip.util.TripNoteScreenName
import com.lion.wandertrip.vo.TripNoteVO
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext context: Context,
    val tripNoteService: TripNoteService,
) : ViewModel(){

    val tripApplication = context as TripApplication

    
    var popularTripList = mutableStateListOf<SimpleTripItemModel>()

    var myNoteItemList = mutableStateListOf<TripNoteModel>()

    private val _tripItemList = MutableLiveData<List<TripItemModel>>()
    val tripItemList: LiveData<List<TripItemModel>> get() = _tripItemList

    private val _topScrapedTrips = MutableLiveData<List<TripNoteModel>>()
    val topScrapedTrips: LiveData<List<TripNoteModel>> get() = _topScrapedTrips

    private val _tripNoteList = MutableLiveData<List<TripNoteModel>>()
    val tripNoteList: LiveData<List<TripNoteModel>> get() = _tripNoteList

    fun getTopScrapedTrips() {
        viewModelScope.launch {
            val tripNotes = tripNoteService.gettingTripNoteListWithScrapCount()
            val top3List = tripNotes.sortedByDescending { it.tripNoteScrapCount }
                .take(3) // ✅ 스크랩 수 기준 상위 3개 추출
            _topScrapedTrips.value = top3List
        }
    }

    fun fetchTripNoteListWithScrapCount() {
        viewModelScope.launch {
            val tripNotes = tripNoteService.gettingTripNoteListWithScrapCount()
            _tripNoteList.value = tripNotes
        }
    }

    fun backScreen() {
        tripApplication.navHostController.popBackStack()
    }

    // 내 리뷰 화면 전환
    fun onClickIconSearch() {
        tripApplication.navHostController.navigate(MainScreenName.MAIN_SCREEN_SEARCH.name)
    }

    fun onClickTrip(contentId: String) {
        tripApplication.navHostController.navigate("${MainScreenName.MAIN_SCREEN_DETAIL.name}/$contentId")
    }

    fun onClickTripNote(documentId : String) {
        tripApplication.navHostController.navigate("${TripNoteScreenName.TRIP_NOTE_DETAIL.name}/${documentId}")
    }
}