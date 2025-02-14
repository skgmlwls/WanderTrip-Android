package com.lion.wandertrip.presentation.bottom.home_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() { // ✅ ViewModel 상속 추가
    private val _travelPosts = mutableStateOf<List<Pair<String, List<String>>>>(emptyList())
    val travelPosts: State<List<Pair<String, List<String>>>> = _travelPosts

    private var currentPage = 1 // 현재 페이지
    private val pageSize = 2    // 한 번에 불러올 여행기 개수

    init {
        loadMoreTrips() // 초기 데이터 로드
    }

    fun loadMoreTrips() {
        viewModelScope.launch { // ✅ viewModelScope 정상 동작
            // val newTrips = getMoreTravelPosts(currentPage, pageSize) // 서버에서 추가 데이터 가져오기
            // _travelPosts.value = _travelPosts.value + newTrips // 기존 리스트에 추가
            // currentPage++ // 다음 페이지로 업데이트
        }
    }
}