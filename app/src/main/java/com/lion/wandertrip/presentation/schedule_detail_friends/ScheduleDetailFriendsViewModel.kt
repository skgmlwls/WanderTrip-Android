package com.lion.wandertrip.presentation.schedule_detail_friends

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lion.wandertrip.TripApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ScheduleDetailFriendsViewModel @Inject constructor(
    @ApplicationContext val context: Context
) : ViewModel() {
    val application = context as TripApplication

    // 일정 문서 ID
    val scheduleDocId = mutableStateOf("")

    // 함께 하는 친구 목록
    val friendsList = mutableListOf<String>(
        "친구1",
        "친구2",
        "친구3",
    )

    // 일정 문서 ID 설정
    fun setScheduleDocId(id: String) {
        scheduleDocId.value = id
    }

    // 이전 화면 으로 이동 (일정 상세 화면)
    fun backScreen() {
        application.navHostController.popBackStack()
    }

}