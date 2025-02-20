package com.lion.wandertrip.presentation.user_login_page

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.ViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.service.UserService
import com.lion.wandertrip.util.BotNavScreenName
import com.lion.wandertrip.util.MainScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.inject.Inject
import kotlin.io.encoding.ExperimentalEncodingApi
import android.util.Base64
import androidx.lifecycle.viewModelScope
import com.lion.wandertrip.util.LoginResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


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
        tripApplication.navHostController.navigate(MainScreenName.MAIN_SCREEN_USER_SIGN_UP_STEP1.name)
    }

    // 로그인 버튼 click
    fun buttonUserLoginOnClick(){
        Log.d("test100","클릭")
        tripApplication.navHostController.popBackStack(MainScreenName.MAIN_SCREEN_USER_LOGIN.name, true)

        if(textFieldUserLoginIdValue.value.isEmpty()){
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
                // 로그인 성공시
                LoginResult.LOGIN_RESULT_SUCCESS -> {
                    // 로그인한 사용자 정보를 가져온다.
                    val work2 = async(Dispatchers.IO){
                        userService.selectUserDataByUserIdOne(loginUserId)
                    }
                    val loginUserModel = work2.await()

                    // 만약 자동로그인이 체크되어 있다면
                    if(checkBoxAutoLoginValue.value){
                        CoroutineScope(Dispatchers.Main).launch{
                            val work3 = async(Dispatchers.IO){
                                userService.updateUserAutoLoginToken(tripApplication, loginUserModel.userDocId)
                            }
                            work3.join()
                        }
                    }

                    // Application 객체에 로그인한 사용자의 정보를 담고 게시판 메인 화면으로 이동한다.
                    tripApplication.loginUserModel = loginUserModel


                    tripApplication.navHostController.popBackStack(MainScreenName.MAIN_SCREEN_USER_LOGIN.name, true)
                    tripApplication.navHostController.navigate(BotNavScreenName.BOT_NAV_SCREEN_HOME.name)
                }
            }
        }
    }

    // 카카오 로그인 버튼 눌렀을 때
    fun onClickButtonKakaoLogin() {
        // 키해시 값 불러오기
        // getHashKey()
        // 토큰값 가져오기

        //viewModelScope는 자동으로 취소됨
        //✔ viewModelScope는 ViewModel이 clear() 될 때 자동으로 취소돼!
        //✔ CoroutineScope(Dispatchers.Main).launch {}로 만든 코루틴은 Activity나 Fragment가 종료되어도 계속 실행될 수 있음 → 메모리 누수 위험 🚨
        //✔ viewModelScope는 ViewModel이 사라지면 자동으로 코루틴을 정리하므로 안정적

        viewModelScope.launch {
            val work1 = async(Dispatchers.IO) {
                createKakaoToken()
            }
            val kToken = work1.await()
            Log.d("test100", "kToken: $kToken")
            if(kToken!=""){
                tripApplication.navHostController.navigate("${MainScreenName.MAIN_SCREEN_USER_SIGN_UP_STEP3.name}/$kToken")
            }

        }
    }


    // 카카오 로그인 토큰 받아오기
    suspend fun createKakaoToken(): String? = suspendCoroutine { continuation ->
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("test100", "카카오계정으로 로그인 실패", error)
                continuation.resume(null) // 실패 시 null 반환
            } else if (token != null) {
                Log.i("test100", "카카오계정으로 로그인 성공 ${token.accessToken}")
                continuation.resume(token.accessToken) // 성공 시 토큰 반환
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(tripApplication)) {
            UserApiClient.instance.loginWithKakaoTalk(tripApplication) { token, error ->
                if (error != null) {
                    Log.e("test100", "카카오톡으로 로그인 실패", error)

                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        continuation.resume(null) // 로그인 취소 시 null 반환
                        return@loginWithKakaoTalk
                    }

                    // 카카오 계정 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(tripApplication, callback = callback)
                } else if (token != null) {
                    Log.i("test100", "카카오톡으로 로그인 성공 ${token.accessToken}")
                    continuation.resume(token.accessToken) // 성공 시 토큰 반환
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(tripApplication, callback = callback)
        }
    }

    // 키해시 받아오는 메서드
    @OptIn(ExperimentalEncodingApi::class)
    private fun getHashKey() {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = tripApplication.packageManager.getPackageInfo(tripApplication.packageName, PackageManager.GET_SIGNATURES)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        if (packageInfo == null) {
            Log.e("KeyHash", "KeyHash:null")
            return
        }

        for (signature in packageInfo.signatures!!) {
            try {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT))  // 수정된 부분
            } catch (e: NoSuchAlgorithmException) {
                Log.d("test100", "Unable to get MessageDigest. signature=$signature")
            }
        }
    }

}