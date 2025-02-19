package com.lion.wandertrip.presentation.detail_review_modify

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.model.ReviewModel
import com.lion.wandertrip.presentation.detail_review_modify.used_dummy_data.ReviewModifyModelDummyData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class DetailReviewModifyViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {
    val tripApplication = context as TripApplication

    val reviewModel = mutableStateOf(ReviewModel())

    val ratingScoreValue = mutableStateOf(0.0f)

    val reviewContentValue = mutableStateOf("")

    fun onClickNavIconBack() {
        tripApplication.navHostController.popBackStack()
    }

    // 모델 가져오기
    fun getReviewModel() {
        reviewModel.value = ReviewModifyModelDummyData.reviewModifyModel
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

}