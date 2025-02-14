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

    fun onClickTextUserInfoModify() {
        tripApplication.navHostController.navigate(MainScreenName.MAIN_SCREEN_USER_INFO.name)
    }

}