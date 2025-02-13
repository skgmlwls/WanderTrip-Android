package com.lion.wandertrip.presentation.user_login_page

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.ViewModel
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.service.UserService
import com.lion.wandertrip.util.BotNavScreenName
import com.lion.wandertrip.util.LoginResult
import com.lion.wandertrip.util.MainScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserLoginViewModel @Inject constructor(
    @ApplicationContext context: Context,
    val userService: UserService,
) : ViewModel() {

    val tripApplication = context as TripApplication

    // 아이디 입력 요소
    val textFieldUserLoginIdValue = mutableStateOf("")
    // 비밀번호 입력 요소
    val textFieldUserLoginPasswordValue = mutableStateOf("")
    // 자동 로그인 입력 요소
    val checkBoxAutoLoginValue = mutableStateOf(false)

    // 아이디 입력요소 포커스
    val textFieldUserLoginIdFocusRequester = mutableStateOf(FocusRequester())
    // 비밀번호 입력 요소 포커스
    val textFieldUserLoginPasswordFocusRequester = mutableStateOf(FocusRequester())

    // 아이디 입력 오류 다이얼로그 상태변수
    val alertDialogUserIdState = mutableStateOf(false)
    // 비밀번호 입력 오류 다이얼로그 상태변수
    val alertDialogUserPwState = mutableStateOf(false)
    // 존재하지 않는 아이디 오류 다이얼로그 상태변수
    val alertDialogLoginFail1State = mutableStateOf(false)
    // 잘못된 비밀번호 다이얼로그 상태변수
    val alertDialogLoginFail2State = mutableStateOf(false)
    // 탈퇴한 회원 다이얼로그 상태변수
    val alertDialogLoginFail3State = mutableStateOf(false)

    // 회원 가입 버튼 click
    fun buttonUserJoinClick(){
        tripApplication.navHostController.navigate(MainScreenName.MAIN_SCREEN_USER_JOIN_STEP1.name)
    }

    // 로그인 버튼 click
    fun buttonUserLoginOnClick(){
        Log.d("test100","클릭")
        tripApplication.navHostController.popBackStack(MainScreenName.MAIN_SCREEN_USER_LOGIN.name, true)
        tripApplication.navHostController.navigate(BotNavScreenName.BOT_NAV_SCREEN_HOME.name)

      /*  if(textFieldUserLoginIdValue.value.isEmpty()){
            alertDialogUserIdState.value = true
            return
        }

        if(textFieldUserLoginPasswordValue.value.isEmpty()){
            alertDialogUserPwState.value = true
            return
        }

        // 사용자가 입력한 아이디와 비밀번호
        val loginUserId = textFieldUserLoginIdValue.value
        val loginUserPw = textFieldUserLoginPasswordValue.value

        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                userService.checkLogin(loginUserId, loginUserPw)
            }
            // 로그인 결과를 가져온다.
            val loginResult = work1.await()

            // 로그인 결과로 분기한다.
            when(loginResult){
                LoginResult.LOGIN_RESULT_ID_NOT_EXIST -> {
                    alertDialogLoginFail1State.value = true
                }
                LoginResult.LOGIN_RESULT_PASSWORD_INCORRECT -> {
                    alertDialogLoginFail2State.value = true
                }
                LoginResult.LOGIN_RESULT_SIGN_OUT_MEMBER -> {
                    alertDialogLoginFail3State.value = true
                }
                LoginResult.LOGIN_RESULT_SUCCESS -> {
                    // 로그인한 사용자 정보를 가져온다.
                    val work2 = async(Dispatchers.IO){
                        userService.selectUserDataByUserIdOne(loginUserId)
                    }
                    val loginUserModel = work2.await()

                    // 만약 자동로그인이 체크되어 있다면
                    if(checkBoxAutoLoginValue.value){
                        CoroutineScope(Dispatchers.Main).launch{
                            val work1 = async(Dispatchers.IO){
                                userService.updateUserAutoLoginToken(tripApplication, loginUserModel.userDocId)
                            }
                            work1.join()
                        }
                    }

                    // Application 객체에 로그인한 사용자의 정보를 담고 게시판 메인 화면으로 이동한다.
                    tripApplication.loginUserModel = loginUserModel

                    tripApplication.navHostController.popBackStack(MainScreenName.MAIN_SCREEN_USER_LOGIN.name, true)
                    tripApplication.navHostController.navigate(MainScreenName.MAIN_SCREEN_USER_LOGIN.name)
                }
            }
        }*/
    }
}