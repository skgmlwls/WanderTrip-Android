package com.lion.wandertrip.presentation.detail_review_write_page

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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

    // 이미지 리스트 경로명을 담을 변수
    val imagePathList = mutableStateListOf<String>()


    // 뒤로가기
    fun onClickNavIconBack() {
        tripApplication.navHostController.popBackStack()
    }

    // 후기 등록 메서드
    suspend fun addContentsReview(contentId: String): String {
        imagePathList.clear()

        var contentsDocId = ""

        if (isImagePicked.value) {
            Log.d("test100", "골랐나?")

            mutableBitMapList.forEachIndexed { index, bitmap ->
                val name = "image_${index}_${System.currentTimeMillis()}.jpg"

                // 이미지 저장 (파일명 일치)
                Tools.saveBitmaps(tripApplication, bitmap!!, name)

                // 저장된 파일이 존재하는지 확인
                val savedFile = File(tripApplication.getExternalFilesDir(null), name)
                Log.d(
                    "checkFile",
                    "파일 저장 확인: ${savedFile.absolutePath}, Exists: ${savedFile.exists()}"
                )

                imagePathList.add(name)
            }
        }

        val review = ReviewModel()
        review.reviewContent = reviewContentValue.value
        review.reviewImageList = listOf()
        review.reviewRatingScore = ratingScoreValue.value
        review.reviewWriterNickname = tripApplication.loginUserModel.userNickName
        review.reviewWriterProfileImgURl =
            userService.gettingImage(tripApplication.loginUserModel.userProfileImageURL).toString()
        review.reviewImageList = imagePathList



        // 문서가 존재하면 그 문서 DocId 를 리턴함
        contentsDocId = contentsService.isContentExists(contentId)

        // 존재하면 서브컬렉션에 리뷰 추가
        if (contentsDocId != "") {
            contentsReviewService.addContentsReview(
                contentsId = contentId,
                contentsReviewModel = review
            )
        } else {
            // 컨텐츠 문서가 없다면 문서를 만들고 넣는다
            val contents = ContentsModel(
                contentId = contentId,
            )
            // 컨텐츠 문서 만들고 docID 리턴받음
            contentsDocId = contentsService.addContents(contents)
            contentsReviewService.addContentsReview(
                contentsId = contentId,
                contentsReviewModel = review
            )
        }


        return contentsDocId
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

    fun uploadImageInFireStore(contentId : String) {
        viewModelScope.launch {
            // 병렬로 Firestore에 이미지와 사용자 데이터 업데이트
            val filePaths = imagePathList.mapIndexed { index, path ->
                tripApplication.getExternalFilesDir(null).toString() + "/${path}"
            }
            // fireStore 에 이미지 저장
            if (isImagePicked.value) {
                contentsReviewService.uploadReviewImage(filePaths, imagePathList, contentId)
            }
        }

    }

}