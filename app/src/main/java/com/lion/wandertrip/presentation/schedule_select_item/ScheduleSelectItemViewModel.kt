package com.lion.wandertrip.presentation.schedule_select_item

import android.content.Context
import androidx.lifecycle.ViewModel
import com.lion.wandertrip.TripApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ScheduleSelectItemViewModel @Inject constructor(
    @ApplicationContext val context: Context
) : ViewModel() {

    val application = context as TripApplication


    // 이전 화면 으로 이동 (일정 상세 화면)
    fun backScreen() {
        application.navHostController.popBackStack()
    }

}