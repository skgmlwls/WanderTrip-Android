package com.lion.wandertrip.presentation.detail_review_modify_page

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.model.ReviewModel
import com.lion.wandertrip.presentation.detail_review_modify_page.used_dummy_data.ReviewModifyModelDummyData
import com.lion.wandertrip.service.ContentsReviewService
import com.lion.wandertrip.service.UserWriteReviewService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailReviewModifyViewModel @Inject constructor(
    @ApplicationContext context: Context,
    val contentsReviewService: ContentsReviewService
) : ViewModel() {
    val tripApplication = context as TripApplication

    val reviewModel = mutableStateOf(ReviewModel())

    val ratingScoreValue = mutableStateOf(0.0f)

    val reviewContentValue = mutableStateOf("")


    fun onClickNavIconBack() {
        tripApplication.navHostController.popBackStack()
    }

    // 모델 가져오기
    fun getReviewModel(contentDocId : String , contentReviewDocId : String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                reviewModel.value = contentsReviewService.getContentsReviewByDocId(contentDocId, contentReviewDocId)
            }
        }
    }

    // 별점 스테이트 설정하기
    fun settingRatingScore(score : Float) {
        ratingScoreValue.value = score
    }

    // 내용 스테이트 설정하기
    fun settingReviewContent(reviewContent: String) {
        reviewContentValue.value = reviewContent
    }

    // 이미지 설정하기
    fun settingReviewImgList(imgPathList : MutableList<String>) {
        imgPathList.addAll(
            imgPathList
        )
    }

    // 수정 완료하기
    fun onClickIconCheckModifyReview() {
        Log.d("DRMVM","onClickIconCheckModifyReview")

        viewModelScope.launch {
            Log.d("DRMVM","viewModelScope")
            withContext(Dispatchers.IO) {

                reviewModel.value.reviewRatingScore = ratingScoreValue.value
                reviewModel.value.reviewContent = reviewContentValue.value
                contentsReviewService.modifyContentsReview(reviewModel.value.contentsDocId,reviewModel.value)
                tripApplication.navHostController.popBackStack()
            }
        }
    }

}