package com.lion.wandertrip.presentation.detail_review_write_page

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.model.ContentsModel
import com.lion.wandertrip.model.ReviewModel
import com.lion.wandertrip.service.ContentsReviewService
import com.lion.wandertrip.service.ContentsService
import com.lion.wandertrip.service.UserService
import com.lion.wandertrip.util.Tools
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DetailReviewWriteViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val contentsReviewService: ContentsReviewService,
    val contentsService: ContentsService,
    val userService: UserService,
) : ViewModel() {
    val tripApplication = context as TripApplication

    // 별점 점수 상태 관리 변수
    val ratingScoreValue = mutableStateOf(5.0f)

    // 리뷰 내용 상태 변수
    val reviewContentValue = mutableStateOf("")

    // 이미지 가져 왔는지 상태 여부
    val isImagePicked = mutableStateOf(false)

    // 비트맵 리스트 상태 변수
    val mutableBitMapList = mutableStateListOf<Bitmap?>()

    // 로딩 변수
    val isLoading = mutableStateOf(false)



    // 뒤로가기
    fun onClickNavIconBack() {
        tripApplication.navHostController.popBackStack()
    }

    suspend fun addContentsReview(contentId: String): String {
        val imagePathList = mutableListOf<String>()
        val serverFilePathList = mutableListOf<String>()
        var contentsDocId = ""
        var imageUrlList = listOf<String>()

        if (isImagePicked.value) {
            isLoading.value = true
            Log.d("test100", "골랐나?")

            mutableBitMapList.forEachIndexed { index, bitmap ->
                val name = "image_${index}_${System.currentTimeMillis()}.jpg"
                serverFilePathList.add(name)

                val savedFilePath = Tools.saveBitmaps(tripApplication, bitmap!!, name)
                Log.d("checkFile", "파일 저장 경로: $savedFilePath")

                imagePathList.add(savedFilePath)
            }


            // 📌 이미지 업로드 완료될 때까지 대기
             imageUrlList = withContext(Dispatchers.IO) {
                uploadImageWithTimeout(imagePathList, serverFilePathList, contentId)
            }

            // 📌 이미지 업로드 실패 시 로그
            if (imageUrlList.isEmpty()) {
                Log.e("getUri", "이미지 업로드 URL 리스트가 비어 있음! Firestore 저장 중단")
                return ""
            }

            Log.d("getUri", "이미지 URL 리스트: $imageUrlList")
        }

        // 📌 이미지 업로드가 끝난 후 리뷰 데이터 저장
        val review = ReviewModel().apply {
            reviewContent = reviewContentValue.value
            reviewImageList = imageUrlList // ✅ 업로드가 끝난 후 URL 리스트를 저장
            reviewRatingScore = ratingScoreValue.value
            reviewWriterNickname = tripApplication.loginUserModel.userNickName
            reviewWriterProfileImgURl =
                userService.gettingImage(tripApplication.loginUserModel.userProfileImageURL).toString()
        }

        // 문서 존재 여부 확인 후 저장
        contentsDocId = contentsService.isContentExists(contentId)

        if (contentsDocId.isNotEmpty()) {
            contentsReviewService.addContentsReview(contentId, review)
        } else {
            val contents = ContentsModel(contentId = contentId)
            contentsDocId = contentsService.addContents(contents)
            contentsReviewService.addContentsReview(contentId, review)
        }

        return contentsDocId
    }

    suspend fun uploadImageWithTimeout(
        sourceFilePath: List<String>,
        serverFilePath: List<String>,
        contentId: String
    ): List<String> {
        Log.d("uploadImageWithTimeout", "sourceFilePath: $sourceFilePath")
        Log.d("uploadImageWithTimeout", "serverFilePath: $serverFilePath")
        Log.d("uploadImageWithTimeout", "contentId: $contentId")

        val resultUrlList = mutableListOf<String>()

        return withTimeoutOrNull(10000) {  // 📌 타임아웃을 10초로 늘림
            var retry = true
            var tempUrlList: List<String>?

            while (retry) {
                tempUrlList = contentsReviewService.uploadReviewImageList(
                    sourceFilePath,
                    serverFilePath.toMutableStateList(),
                    contentId
                )

                if (tempUrlList.isNullOrEmpty()) {
                    Log.d("uploadImageWithTimeout", "이미지 URL이 아직 준비되지 않음. 재시도 중...")
                    delay(500)  // 0.5초 대기 후 재시도
                } else {
                    retry = false
                    resultUrlList.addAll(tempUrlList)
                }
            }
            resultUrlList
        } ?: run {
            Log.e("uploadImageWithTimeout", "이미지 URL 가져오기 실패 (타임아웃)")
            emptyList() // 타임아웃 시 빈 리스트 반환
        }
    }

    // 컨텐츠 의 별점 필드 수정
    fun addReviewAndUpdateContents(contentId: String) {

        viewModelScope.launch {
            val work1 = async(Dispatchers.IO) {

            }
            work1.await()
        }

        runBlocking {
            // 리뷰 등록 메서드 호출
            val contentDocId = addContentsReview(contentId)
            // 위에 끝날때까지 대기
            contentsService.updateContentRating(contentDocId)
            tripApplication.navHostController.popBackStack()

        }
    }

}