package com.lion.wandertrip.presentation.trip_note_schedule_page

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.lion.wandertrip.model.TripScheduleModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class TripNoteScheduleViewModel @Inject constructor(
    @ApplicationContext val context: Context,
) : ViewModel() {

    var tripNoteScheduleList = mutableStateListOf<TripScheduleModel>()


    fun gettingTripNoteScheduleData() {
        tripNoteScheduleList = mutableStateListOf(
            TripScheduleModel(
                scheduleTitle = "제주 힐링여행",
                scheduleStartDate = Timestamp.now(),
                scheduleCity = "제주",
                scheduleEndDate = Timestamp.now(),
            ),
            TripScheduleModel(
                scheduleTitle = "서울 힐링여행",
                scheduleStartDate = Timestamp.now(),
                scheduleCity = "서울",
                scheduleEndDate = Timestamp.now(),
            )
        )
    }

}