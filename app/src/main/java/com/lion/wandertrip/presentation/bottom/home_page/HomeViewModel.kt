package com.lion.wandertrip.presentation.bottom.home_page

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.lion.wandertrip.service.TripNoteService
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.model.SimpleTripItemModel
import com.lion.wandertrip.model.TripCommonItem
import com.lion.wandertrip.model.TripItemModel
import com.lion.wandertrip.model.TripNoteModel
import com.lion.wandertrip.repository.TripAreaBaseItemRepository
import com.lion.wandertrip.repository.TripCommonItemRepository
import com.lion.wandertrip.service.TripAreaBaseItemService
import com.lion.wandertrip.service.TripScheduleService
import com.lion.wandertrip.util.MainScreenName
import com.lion.wandertrip.util.TripNoteScreenName
import com.lion.wandertrip.vo.TripNoteVO
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext context: Context,
    val tripNoteService: TripNoteService,
    val tripAreaBaseItemService: TripAreaBaseItemService
) : ViewModel(){

    val tripApplication = context as TripApplication

    private val _tripItemList = MutableLiveData<List<TripItemModel>>()
    val tripItemList: LiveData<List<TripItemModel>> get() = _tripItemList

    private val _topScrapedTrips = MutableLiveData<List<TripNoteModel>>()
    val topScrapedTrips: LiveData<List<TripNoteModel>> get() = _topScrapedTrips

    private val _tripNoteList = MutableLiveData<List<TripNoteModel>>()
    val tripNoteList: LiveData<List<TripNoteModel>> get() = _tripNoteList

    private val _imageUrlMap = mutableStateMapOf<String, String?>()
    val imageUrlMap: Map<String, String?> get() = _imageUrlMap

    private val _randomTourItems = MutableLiveData<List<TripItemModel>>() // ✅ LiveData 추가
    val randomTourItems: LiveData<List<TripItemModel>> get() = _randomTourItems

    private var hasFetched = false // ✅ 실행 여부를 ViewModel에서 관리

    // 🔥 무작위 관광지 데이터를 가져오는 함수
    fun fetchRandomTourItems() {
        if (hasFetched) return // ✅ 한 번 실행되면 다시 실행하지 않음
        viewModelScope.launch {
            val items = tripAreaBaseItemService.getTripAreaBaseItem()
            _randomTourItems.value = items ?: emptyList()
            hasFetched = true // ✅ 실행 완료 후 true 설정
        }
    }
    fun fetchTripNotes() {
        viewModelScope.launch {
            try {
                val firestore = FirebaseFirestore.getInstance()
                val collectionReference = firestore.collection("TripNoteData")

                val result = collectionReference
                    .orderBy("tripNoteTimeStamp", Query.Direction.DESCENDING)
                    .get()
                    .await()

                val tripNotes = result.documents.mapNotNull { document ->
                    val tripNoteVO = document.toObject(TripNoteVO::class.java)
                    tripNoteVO?.toTripNoteModel(document.id)
                }

                _tripNoteList.value = tripNotes // ✅ Firestore 데이터 업데이트
                fetchImageUrls() // ✅ 여행기 데이터 가져온 후 이미지 URL도 가져오기

            } catch (e: Exception) {
                Log.e("Firestore", "Error fetching trip notes", e)
            }
        }
    }

    fun fetchImageUrls() {
        val tripNotes = tripNoteList.value ?: return // 🔥 LiveData에서 최신 데이터를 가져옴

        tripNotes.forEach { tripNote ->
            val fileName = tripNote.tripNoteImage.firstOrNull() ?: return@forEach

            // 이미 로딩 중이거나 가져온 데이터가 있으면 다시 요청하지 않음
            if (_imageUrlMap.containsKey(fileName)) return@forEach

            // 🔥 초기 로딩 상태를 빈 문자열("")로 설정하여 Compose가 감지할 수 있도록 변경
            _imageUrlMap[fileName] = ""

            viewModelScope.launch {
                val imageUrl = tripNoteService.gettingImage(fileName)
                _imageUrlMap[fileName] = imageUrl?.toString() ?: ""  // 🚀 URL이 null이면 빈 문자열로 처리
            }
        }
    }


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