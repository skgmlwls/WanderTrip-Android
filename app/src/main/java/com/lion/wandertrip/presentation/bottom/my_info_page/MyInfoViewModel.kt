package com.lion.wandertrip.presentation.bottom.my_info_page

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.service.UserService
import com.lion.wandertrip.util.MainScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MyInfoViewModel @Inject constructor(
    @ApplicationContext context: Context,
    val userService: UserService,
) : ViewModel(){

    val tripApplication = context as TripApplication

    // 프로필 편집
    fun onClickTextUserInfoModify() {
        tripApplication.navHostController.navigate(MainScreenName.MAIN_SCREEN_USER_INFO.name)
    }

    // 내 여행 화면 전환
    fun onClickIconMyTrip() {
        Log.d("test100"," 내여행")
        tripApplication.navHostController.navigate(MainScreenName.MAIN_SCREEN_MY_TRIP.name)
    }
    // 내 저장 화면 전환
    fun onClickIconMyInteresting() {
        Log.d("test100"," 내저장")

        tripApplication.navHostController.navigate(MainScreenName.MAIN_SCREEN_MY_INTERESTING.name)
    }
    // 내 리뷰 화면 전환
    fun onClickIconMyReview() {
        Log.d("test100"," 내리부")

        tripApplication.navHostController.navigate(MainScreenName.MAIN_SCREEN_MY_REVIEW.name)
    }
    // 내 여행기 화면 전환
    fun onClickIconTripNote() {
        Log.d("test100"," 내 여행기")

        tripApplication.navHostController.navigate(MainScreenName.MAIN_SCREEN_MY_TRIP_NOTE.name)
    }


}