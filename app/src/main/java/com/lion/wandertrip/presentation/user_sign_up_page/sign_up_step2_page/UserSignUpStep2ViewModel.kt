package com.lion.wandertrip.presentation.user_sign_up_page.sign_up_step2_page

import android.content.Context
import androidx.lifecycle.ViewModel
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.service.UserService
import com.lion.wandertrip.util.BotNavScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserSignUpStep2ViewModel @Inject constructor(
    @ApplicationContext context: Context,
    val userService: UserService,
) : ViewModel() {
    // context
    val tripApplication = context as TripApplication

    // 체크 아이콘 누를 때 리스너
    fun onClickIconCheck(){
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                // 유저 프로필 이미지 설정
            }
        }
        tripApplication.navHostController.navigate(BotNavScreenName.BOT_NAV_SCREEN_HOME.name)
    }

    // 건너뛰기 버튼
    fun onClickButtonPass(){
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                // 유저 프로필 이미지 설정
            }
        }
        tripApplication.navHostController.navigate(BotNavScreenName.BOT_NAV_SCREEN_HOME.name)
    }
}