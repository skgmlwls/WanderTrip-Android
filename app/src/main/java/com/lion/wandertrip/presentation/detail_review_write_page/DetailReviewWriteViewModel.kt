package com.lion.wandertrip.presentation.detail_review_write_page

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lion.wandertrip.TripApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class DetailReviewWriteViewModel @Inject constructor(@ApplicationContext context: Context) : ViewModel(){
    val tripApplication = context as TripApplication

    // 별점 점수 상태 관리 변수
    val ratingScoreValue = mutableStateOf(0.0f)

    fun onClickNavIconBack() {
        tripApplication.navHostController.popBackStack()
    }
}