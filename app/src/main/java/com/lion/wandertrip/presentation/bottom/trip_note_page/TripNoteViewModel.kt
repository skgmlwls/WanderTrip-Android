package com.lion.wandertrip.presentation.bottom.trip_note_page

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.model.TripNoteModel
import com.lion.wandertrip.service.TripNoteService
import com.lion.wandertrip.util.MainScreenName
import com.lion.wandertrip.util.TripNoteScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripNoteViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    val tripNoteService: TripNoteService,
) : ViewModel() {

    val tripApplication = context as TripApplication

    // TopAppBar의 타이틀
    val topAppBarTitle = mutableStateOf("")

    // 글 목록을 구성하기 위한 상태 관리 변수
    var tripNoteList = mutableStateListOf<TripNoteModel>()


    // 보여줄 이미지의 Uri
    var showImageUri = mutableStateListOf<Uri?>(null)

    // + 버튼(fab 버튼)을 눌렀을 때
    fun addButtonOnClick(){
        val scheduleTitle = "" // 기본값 설정
        // 여행기 추가하는 화면으로 이동
        tripApplication.navHostController.navigate("${TripNoteScreenName.TRIP_NOTE_WRITE.name}/$scheduleTitle")
    }

    // 각 항목을 눌렀을 때
    fun listItemOnClick(){
        // 여행기 상세보기 화면으로 이동 (각 항목의 문서 id를 전달... 추후에)
        tripApplication.navHostController.navigate("${TripNoteScreenName.TRIP_NOTE_DETAIL.name}/{documentId}")
        // tripApplication.navHostController.navigate(TripNoteScreenName.TRIP_NOTE_OTHER_SCHEDULE.name)
    }

    // 데이터 가져오기
    fun gettingTripNoteData() {
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                tripNoteService.gettingTripNoteList()
            }
            val recyclerViewList = work1.await()

//            recyclerViewList.forEach { tripNoteModel ->
//                // tripNoteImage를 Uri로 변환하여 showImageUri에 넣기
//                val imageUris = tripNoteModel.tripNoteImage?.map { imagePath ->
//                    Uri.parse(imagePath)  // String을 Uri로 변환
//                } ?: emptyList()  // 만약 tripNoteImage가 null이면 빈 리스트로 처리
//
//                showImageUri.clear()
//                showImageUri.addAll(imageUris)  // Uri 리스트를 showImageUri에 추가
//            }

            // 상태 관리 변수에 담아준다.
            tripNoteList.clear()
            tripNoteList.addAll(recyclerViewList)
        }
    }


}