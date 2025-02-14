package com.lion.wandertrip

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lion.wandertrip.presentation.trip_note_detail_page.TripNoteDetailScreen
import com.lion.wandertrip.presentation.trip_note_write_page.TripNoteWriteScreen
import com.lion.wandertrip.presentation.main_page.MainScreen
import com.lion.wandertrip.presentation.schedule_add.ScheduleAddScreen
import com.lion.wandertrip.presentation.start_page.StartScreen
import com.lion.wandertrip.presentation.user_login_page.UserLoginScreen
import com.lion.wandertrip.presentation.user_sign_up_page.sign_up_step1_page.UserSignUpStep1Screen
import com.lion.wandertrip.presentation.user_sign_up_page.sign_up_step2_page.UserSignUpStep2Screen
import com.lion.wandertrip.ui.theme.WanderTripTheme
import com.lion.wandertrip.util.BotNavScreenName
import com.lion.wandertrip.util.MainScreenName
import com.lion.wandertrip.util.ScheduleScreenName
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WanderTripTheme {
                MyApp()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyApp() {
    // 네비게이션 객체
    // Composable 함수가 재구성(recompose)되더라도 NavController의 인스턴스가 유지됨 (즉, 화면 회전이나 재구성이 발생해도 동일한 인스턴스를 사용).
    val rememberNavHostController = rememberNavController()
    // Appilcation 객체에 담는다.
    // Jetpack Compose에서 LocalContext.current는 현재 Composable 함수가 실행되는 컨텍스트(Context)를 가져오는 방법입니다.
    // applicationContext의 기본 타입은 Context이므로, 우리가 만든 TripApplication 클래스의 인스턴스로 사용하려면 형변환이 필요합니다.
    val tripApplication = LocalContext.current.applicationContext as TripApplication
    tripApplication.navHostController = rememberNavHostController

    NavHost(
        navController = rememberNavHostController,
        startDestination = MainScreenName.MAIN_SCREEN_USER_LOGIN.name
    ) {
        composable(MainScreenName.MAIN_SCREEN_START.name) { StartScreen() }
        // 일정 메인 화면
        composable(MainScreenName.MAIN_SCREEN_USER_LOGIN.name) { UserLoginScreen()}
        composable(BotNavScreenName.BOT_NAV_SCREEN_HOME.name) { MainScreen(navController = rememberNavHostController) }
        composable(MainScreenName.MAIN_SCREEN_USER_Sign_Up_STEP1.name) { UserSignUpStep1Screen() }
        composable(MainScreenName.MAIN_SCREEN_USER_Sign_Up_STEP2.name) { UserSignUpStep2Screen() }
        composable(MainScreenName.MAIN_SCREEN_USER_Sign_Up_STEP3.name) { UserSignUpStep1Screen() }

        composable(BotNavScreenName.BOT_NAV_SCREEN_TRIP_NOTE.name) { MainScreen(navController = rememberNavHostController) }
        composable(MainScreenName.TRIP_NOTE_DETAIL.name) { TripNoteDetailScreen() }
        composable(MainScreenName.TRIP_NOTE_WRITE.name) { TripNoteWriteScreen() }


        // 일정 추가 화면
        composable(ScheduleScreenName.SCHEDULE_ADD_SCREEN.name) { ScheduleAddScreen() }

    }
}



