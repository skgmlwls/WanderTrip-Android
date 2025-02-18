package com.lion.wandertrip.presentation.trip_note_write_page

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.model.TripNoteModel
import com.lion.wandertrip.service.TripNoteService
import com.lion.wandertrip.util.Tools
import com.lion.wandertrip.util.TripNoteScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TripNoteWriteViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    val tripNoteService: TripNoteService
) : ViewModel() {


    val tripApplication = context as TripApplication

    // 프로그레스 상태
    val isProgressVisible = mutableStateOf(false)

    // TopAppBar의 타이틀
    val topAppBarTitle = mutableStateOf("여행기 작성")

    // 여행기 제목
    val tripNoteTitle = mutableStateOf(" ")

    // 여행기 내용
    val tripNoteContent = mutableStateOf(" ")

    // Bitmap
    val tripNotePreviewBitmap = mutableStateListOf<Bitmap?>()

    // 저장할 이미지 경로 리스트
    val tripNoteImages = mutableListOf<String>()

    // 뒤로 가기 버튼
    fun navigationButtonClick(){
        tripApplication.navHostController.popBackStack()
    }

    // 일정 추가 버튼
    fun addTripScheduleClick(){
        // 일정 선택 화면으로 이동
        tripApplication.navHostController.navigate(TripNoteScreenName.TRIP_NOTE_SCHEDULE.name)
    }

    // 사진 삭제
    fun removeTripNoteImage(bitmap: Bitmap) {
        tripNotePreviewBitmap.remove(bitmap)
    }

    // 게시하기 버튼
    fun tripNoteDoneClick(){
        isProgressVisible.value = true


        val tripNoteTitle = tripNoteTitle.value
        val tripNoteContent = tripNoteContent.value
        var tripNoteImage = "none"
        val tripNoteTimeStamp = Timestamp.now()

        // 나중에 받아와야함
        val tripScheduleDocumentId = ""

        // 나중에 로그인한거 받아서,,,?
        val userNickname = ""

        // 저장
        CoroutineScope(Dispatchers.Main).launch {
            // 이미지가 첨부되어 있다면
            if (tripNotePreviewBitmap != null) {

                for (bitmap in tripNotePreviewBitmap) {

                    // 서버상에서의 파일 이름
                    tripNoteImage = "image_${System.currentTimeMillis()}.jpg"
                    // 로컬에 ImageView에 있는 이미지 데이터를 저장한다.
                    Tools.saveBitmap(tripApplication, bitmap!!)

                    val work1 = async(Dispatchers.IO) {
                        // 외부 저장소의 경로를 가져온다.
                        val filePath = tripApplication.getExternalFilesDir(null).toString()
                        tripNoteService.uploadTripNoteImage("${filePath}/uploadTemp.jpg", tripNoteImage)
                    }
                    work1.join()

                    // 이미지 경로를 리스트에 추가
                    tripNoteImages.add(tripNoteImage)
                }
            }

            // 서버에 저장할 여행기 데이터
            val tripNoteModel = TripNoteModel()
            tripNoteModel.tripNoteTitle = tripNoteTitle
            tripNoteModel.tripNoteContent = tripNoteContent
            tripNoteModel.tripNoteImage = tripNoteImages
            tripNoteModel.tripNoteTimeStamp = tripNoteTimeStamp
            tripNoteModel.tripScheduleDocumentId = tripScheduleDocumentId
            tripNoteModel.userNickname = userNickname

            // 저장하기
            val work2 = async(Dispatchers.IO){
                tripNoteService.addTripNoteData(tripNoteModel)
            }
            val documentId = work2.await()

            // 프로그레스 바 숨기기
            isProgressVisible.value = false

            // 여행기 리스트 화면으로 이동
            tripApplication.navHostController.popBackStack()
            // 여행기 메인에서 클릭할때... documentId 전달하기
            // tripApplication.navHostController.navigate("${TripNoteScreenName.TRIP_NOTE_DETAIL.name}/${documentId}")
        }

    }

}