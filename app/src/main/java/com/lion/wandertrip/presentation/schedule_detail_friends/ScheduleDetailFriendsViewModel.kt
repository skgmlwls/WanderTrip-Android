package com.lion.wandertrip.presentation.schedule_detail_friends

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.service.TripScheduleService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleDetailFriendsViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    val tripScheduleService: TripScheduleService,
) : ViewModel() {
    val application = context as TripApplication

    // 다이얼로그 표시 여부를 기억하는 상태
    val showAddFriendDialog = mutableStateOf(false)

    // 일정 문서 ID
    val scheduleDocId = mutableStateOf("")

    // 친구 초대 에러 메시지 상태 (비어있으면 에러 없음)
    val friendAddError = mutableStateOf("")

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

    // 기존 함수 유지, 단 결과에 따라 friendAddError와 showAddFriendDialog 상태를 업데이트함
    fun addFriendByNickName(inviteNickname: String) {
        viewModelScope.launch {
            val work1 = async(Dispatchers.IO) {
                tripScheduleService.addInviteUserByInviteNickname(scheduleDocId.value, inviteNickname)
            }.await()
            if (!work1) {
                Log.d("ScheduleDetailFriendsViewModel", "유저 없음 : $work1")
                friendAddError.value = "검색된 유저가 없습니다"
                // 다이얼로그는 계속 열어둠
            } else {
                Log.d("ScheduleDetailFriendsViewModel", "유저 있음 : $work1")
                friendAddError.value = ""
                // 성공 시 다이얼로그 닫음
                showAddFriendDialog.value = false
            }
            if (work1) {
                val work2 = async(Dispatchers.IO) {
                    // 일정 항목의 초대 리스트에 유저 DocId 추가
                }
            }
        }
    }

}