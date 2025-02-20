package com.lion.wandertrip.presentation.start_page

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.service.UserService
import com.lion.wandertrip.util.BotNavScreenName
import com.lion.wandertrip.util.MainScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    @ApplicationContext context: Context,
    val userService: UserService,
) : ViewModel() {

    // 로그인 중입니다를 위한 상태 관리 변수
    val showLoginMessageState = mutableStateOf(false)

    val boardApplication = context as TripApplication

    // 자동 로그인 처리
    fun autoLoginProcess() {
        // Preference에 login token이 있는지 확인한다.
        val pref = boardApplication.getSharedPreferences("LoginToken", Context.MODE_PRIVATE)
        val loginToken = pref.getString("token", null)
        Log.d("test100", "$loginToken")

        CoroutineScope(Dispatchers.Main).launch {
            if (loginToken != null) {

                showLoginMessageState.value = true

                // 사용자 정보를 가져온다.
                val work1 = async(Dispatchers.IO) {
                    userService.selectUserDataByLoginToken(loginToken)
                }
                val loginUserModel = work1.await()
                // 가져온 사용자 데이터가 있다면
                if (loginUserModel != null) {

                    boardApplication.loginUserModel = loginUserModel

                    boardApplication.navHostController.popBackStack(
                        MainScreenName.MAIN_SCREEN_START.name,
                        true
                    )
                    boardApplication.navHostController.navigate(BotNavScreenName.BOT_NAV_SCREEN_HOME.name)
                } else {
                    // 로그인 화면으로 이동한다.
                    boardApplication.navHostController.navigate(MainScreenName.MAIN_SCREEN_USER_LOGIN.name)
                }
            } else if (true) {
                // 카카오 로그인 토큰도 검사한다.
                // 로그인 화면으로 이동한다.
                boardApplication.navHostController.navigate(MainScreenName.MAIN_SCREEN_USER_LOGIN.name)
            } else {
                // 로그인 화면으로 이동한다.
                boardApplication.navHostController.navigate(MainScreenName.MAIN_SCREEN_USER_LOGIN.name)
            }
        }
    }
}