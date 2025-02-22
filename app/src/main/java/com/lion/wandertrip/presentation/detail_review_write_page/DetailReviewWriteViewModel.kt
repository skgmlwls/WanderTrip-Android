package com.lion.wandertrip.presentation.detail_review_write_page

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.model.ContentsModel
import com.lion.wandertrip.model.ReviewModel
import com.lion.wandertrip.service.ContentsReviewService
import com.lion.wandertrip.service.ContentsService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class DetailReviewWriteViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val contentsReviewService: ContentsReviewService,
    val contentsService: ContentsService
) : ViewModel() {
    val tripApplication = context as TripApplication

    // 별점 점수 상태 관리 변수
    val ratingScoreValue = mutableStateOf(5.0f)

    // 리뷰 내용 상태 변수
    val reviewContentValue = mutableStateOf("")

    // 사용자 이미지 추가 시 사용할 리스트
    val reviewImgList = mutableStateListOf<String>()

    fun onClickNavIconBack() {
        tripApplication.navHostController.popBackStack()
    }

    // 후기 등록 메서드
    suspend fun addContentsReview(contentId: String): String {
        var contentsDocId = ""

        val review = ReviewModel()
        review.reviewContent = reviewContentValue.value
        review.reviewImageList = listOf()
        review.reviewRatingScore = ratingScoreValue.value
        review.reviewWriterNickname = tripApplication.loginUserModel.userNickName
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

    fun addReviewAndupdateContents(contentId : String) {
        runBlocking {
            val contentDocId = addContentsReview(contentId)
            // 위에 끝날때까지 대기
            contentsService.updateContentRating(contentDocId)
            tripApplication.navHostController.popBackStack()
        }
    }

}