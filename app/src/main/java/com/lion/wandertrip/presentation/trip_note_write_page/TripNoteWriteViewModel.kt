package com.lion.wandertrip.presentation.trip_note_write_page

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.util.BotNavScreenName
import com.lion.wandertrip.util.MainScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


@HiltViewModel
class TripNoteWriteViewModel @Inject constructor(
    @ApplicationContext val context: Context,
) : ViewModel() {

    val tripApplication = context as TripApplication

    // TopAppBar의 타이틀
    val topAppBarTitle = mutableStateOf("여행기 작성")

    // 여행기 제목
    val tripNoteTitle = mutableStateOf(" ")

    // 여행기 내용
    val tripNoteContent = mutableStateOf(" ")

    // Bitmap
    val tripNotePreviewBitmap = mutableStateListOf<Bitmap?>()

    // 뒤로 가기 버튼
    fun navigationButtonClick(){
        tripApplication.navHostController.popBackStack()
    }

    // 일정 추가 버튼
    fun addTripScheduleClick(){
        // 일정 선택 화면으로 이동
        tripApplication.navHostController.navigate(BotNavScreenName.BOT_NAV_SCREEN_TRIP_NOTE.name)
    }

    // 사진 삭제
    fun removeTripNoteImage(bitmap: Bitmap) {
        tripNotePreviewBitmap.remove(bitmap)
    }

    // 게시하기 버튼
    fun tripNoteDoneClick(){
        val tripNoteTitle = tripNoteTitle.value
        val tripNoteContent = tripNoteContent.value
        val tripNoteImage = "none"
        val tripNoteTimeStamp = System.nanoTime() //맞나...?

        // 서버에 저장


        // 여행기 작성 화면으로 이동
        tripApplication.navHostController.navigate(BotNavScreenName.BOT_NAV_SCREEN_TRIP_NOTE.name)
    }

}