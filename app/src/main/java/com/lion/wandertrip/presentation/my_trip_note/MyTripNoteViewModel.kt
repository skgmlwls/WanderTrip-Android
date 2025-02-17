package com.lion.wandertrip.presentation.my_trip_note

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.model.TripNoteModel
import com.lion.wandertrip.presentation.my_trip_note.used_dummy_data.TripNoteListDummyData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MyTripNoteViewModel @Inject constructor(@ApplicationContext context: Context) : ViewModel() {

    // 컨텍스트
    val tripApplication = context as TripApplication

    // tripNote 리스트
    val tripNoteList = mutableStateListOf<TripNoteModel>()

    // 인덱스별 메뉴 상태를 관리할 맵
    val menuStateMap = mutableStateMapOf<Int, Boolean>()

    // 이전에 눌렀던 메뉴 인덱스
    var truedIdx = mutableStateOf(-1)

    // 메뉴 상태 관리 변수
    var isMenuOpened = mutableStateOf(false)

    // 리스트 가져오기
    fun getTripNoteList() {
        tripNoteList.addAll(
            TripNoteListDummyData.tripNoteDummyData
        )
        addMap()
    }


    // 리스트 길이로 맵을 초기화
    fun addMap() {
        tripNoteList.forEachIndexed { index, tripNoteModel ->
            menuStateMap[index] = false
        }
    }

    // 메뉴가 눌릴 때 리스너
    fun onClickIconMenu(clickPos: Int) {
        // 한번이라도 메뉴가 클릭된적이 없다면
        if (!isMenuOpened.value) {
            menuStateMap[clickPos] = true
            isMenuOpened.value = true
            truedIdx.value = clickPos

        } else {
            // 한번이상 메뉴가 클릭됐다면
            menuStateMap[truedIdx.value] = false
            menuStateMap[clickPos] = true
            truedIdx.value = clickPos
        }
    }

        // 뒤로가기
        fun onClickNavIconBack() {
            tripApplication.navHostController.popBackStack()
        }

        // 며칠전인지 계산하기
        fun calculationDate(date: Timestamp): Int {
            val now = Timestamp.now()
            val diffInMillis = now.seconds * 1000 + now.nanoseconds / 1_000_000 -
                    (date.seconds * 1000 + date.nanoseconds / 1_000_000)

            return TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt()
        }
}