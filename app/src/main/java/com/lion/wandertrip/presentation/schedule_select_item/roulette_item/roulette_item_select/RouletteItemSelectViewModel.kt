package com.lion.wandertrip.presentation.schedule_select_item.roulette_item.roulette_item_select

import android.content.Context
import androidx.lifecycle.ViewModel
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.util.ScheduleScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class RouletteItemSelectViewModel @Inject constructor(
    @ApplicationContext val context: Context,
) : ViewModel() {

    val application = context as TripApplication

}