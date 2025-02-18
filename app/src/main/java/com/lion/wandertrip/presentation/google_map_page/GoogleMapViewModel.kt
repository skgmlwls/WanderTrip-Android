package com.lion.wandertrip.presentation.google_map_page

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lion.wandertrip.TripApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class GoogleMapViewModel @Inject constructor(@ApplicationContext context: Context) :ViewModel(){
    val tripApplication = context as TripApplication

    val hasLocationPermission = mutableStateOf(false)

    fun onClickNavIconBack() {
        tripApplication.navHostController.popBackStack()
    }

    fun makeShortDescriptionText(str: String): String {
        return if (str.length > 30) {
            str.take(30) + "..."
        } else {
            str
        }
    }

    fun makeShortTitleText(str: String): String {
        return if (str.length > 12) {
            str.take(30) + "..."
        } else {
            str
        }
    }

}