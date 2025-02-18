package com.lion.wandertrip.presentation.detail_review_modify

import android.content.Context
import androidx.lifecycle.ViewModel
import com.lion.wandertrip.TripApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class DetailReviewModifyViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {
    val tripApplication = context as TripApplication

    fun onClickNavIconBack() {
        tripApplication.navHostController.popBackStack()
    }

}