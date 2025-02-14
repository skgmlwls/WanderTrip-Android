package com.lion.wandertrip.presentation.my_trip_page

import android.content.Context
import androidx.lifecycle.ViewModel
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MyTripViewModel @Inject constructor(
    @ApplicationContext context: Context,
    val userService: UserService
):ViewModel(){
    val tripApplication = context as TripApplication

    fun onClickNavIconBack() {
        tripApplication.navHostController.popBackStack()
    }
}