package com.lion.wandertrip.presentation.user_info_page

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    @ApplicationContext context: Context,
    val userService: UserService,
) : ViewModel() {

    // 변경할 닉네임 변수
    val textFieldUserNicknameValue = mutableStateOf("")


    val tripApplication = context as TripApplication

    // 네비게이션 아이콘을 누르면 호출되는 메서드
    fun onClickNavIconBack(){
        tripApplication.navHostController.popBackStack()
    }

    // 체크 메뉴 아이콘을 누르면 호출되는 메서드
    fun onClickMenuIconCheck(){
        tripApplication.navHostController.popBackStack()
    }

}
