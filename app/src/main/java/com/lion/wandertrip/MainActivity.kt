package com.lion.wandertrip

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.Timestamp
import com.lion.wandertrip.presentation.trip_note_detail_page.TripNoteDetailScreen
import com.lion.wandertrip.presentation.trip_note_write_page.TripNoteWriteScreen
import com.lion.wandertrip.presentation.main_page.MainScreen
import com.lion.wandertrip.presentation.schedule_add.ScheduleAddScreen
import com.lion.wandertrip.presentation.schedule_city_select.ScheduleCitySelectScreen
import com.lion.wandertrip.presentation.schedule_city_select.city_roulette.RouletteCityScreen
import com.lion.wandertrip.presentation.schedule_city_select.city_roulette.roulette_city_select.RouletteCitySelectScreen
import com.lion.wandertrip.presentation.schedule_detail_page.ScheduleDetailScreen
import com.lion.wandertrip.presentation.start_page.StartScreen
import com.lion.wandertrip.presentation.user_info_page.UserInfoScreen
import com.lion.wandertrip.presentation.user_login_page.UserLoginScreen
import com.lion.wandertrip.presentation.user_sign_up_page.sign_up_step1_page.UserSignUpStep1Screen
import com.lion.wandertrip.presentation.user_sign_up_page.sign_up_step2_page.UserSignUpStep2Screen
import com.lion.wandertrip.ui.theme.WanderTripTheme
import com.lion.wandertrip.util.BotNavScreenName
import com.lion.wandertrip.util.MainScreenName
import com.lion.wandertrip.util.RouletteScreenName
import com.lion.wandertrip.util.ScheduleScreenName
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
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
        startDestination = "${ScheduleScreenName.SCHEDULE_DETAIL_SCREEN.name}?areaName=서울&areaCode=1"
    ) {
        composable(MainScreenName.MAIN_SCREEN_START.name) { StartScreen() }
        composable(MainScreenName.MAIN_SCREEN_USER_LOGIN.name) { UserLoginScreen()}
        composable(BotNavScreenName.BOT_NAV_SCREEN_HOME.name) { MainScreen() }
        composable(MainScreenName.MAIN_SCREEN_USER_Sign_Up_STEP1.name) { UserSignUpStep1Screen() }
        composable(MainScreenName.MAIN_SCREEN_USER_Sign_Up_STEP2.name) { UserSignUpStep2Screen() }
        composable(MainScreenName.MAIN_SCREEN_USER_Sign_Up_STEP3.name) { UserSignUpStep1Screen() }
        
        composable(MainScreenName.MAIN_SCREEN_USER_INFO.name) {UserInfoScreen()}
        composable(BotNavScreenName.BOT_NAV_SCREEN_HOME.name) { MainScreen() }

        composable(MainScreenName.TRIP_NOTE_DETAIL.name) { TripNoteDetailScreen() }
        composable(MainScreenName.TRIP_NOTE_WRITE.name) { TripNoteWriteScreen() }


        // 일정 화면 ////////////////////////////////////////////////////////////////////////////
        
        // 일정 제목, 날짜 입력 화면
        composable(ScheduleScreenName.SCHEDULE_ADD_SCREEN.name) { ScheduleAddScreen() }

        // 일정 도시 선택 화면
        composable(
            route = "${ScheduleScreenName.SCHEDULE_CITY_SELECT_SCREEN.name}?" +
                    "scheduleTitle={scheduleTitle}&scheduleStartDate={scheduleStartDate}&scheduleEndDate={scheduleEndDate}",
            arguments = listOf(
                navArgument("scheduleTitle") { type = NavType.StringType },
                navArgument("scheduleStartDate") { type = NavType.LongType },
                navArgument("scheduleEndDate") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val scheduleTitle = backStackEntry.arguments?.getString("scheduleTitle") ?: ""
            val startTimestamp = backStackEntry.arguments?.getLong("scheduleStartDate") ?: 0L
            val endTimestamp = backStackEntry.arguments?.getLong("scheduleEndDate") ?: 0L

            val startDate = Timestamp(startTimestamp, 0)
            val endDate = Timestamp(endTimestamp, 0)

            ScheduleCitySelectScreen(scheduleTitle, startDate, endDate)
        }

//        composable(ScheduleScreenName.SCHEDULE_CITY_SELECT_SCREEN.name) {
//            val defaultTitle = "기본 일정"
//            val defaultStartDate = Timestamp.now()
//            val defaultEndDate = Timestamp.now()
//
//            ScheduleCitySelectScreen(defaultTitle, defaultStartDate, defaultEndDate)
//        }

        // 일정 상세 화면
        composable(
            route = "${ScheduleScreenName.SCHEDULE_DETAIL_SCREEN.name}?areaName={areaName}&areaCode={areaCode}",
            arguments = listOf(
                navArgument("areaName") { type = NavType.StringType },
                navArgument("areaCode") { type = NavType.IntType }
            )
        ) {
            val areaName = it.arguments?.getString("areaName") ?: ""
            val areaCode = it.arguments?.getInt("areaCode") ?: 0
            ScheduleDetailScreen(areaName, areaCode)
        }

        /////////////////////////////////////////////////////////////////////////////////////////////


        // 룰렛 화면 /////////////////////////////////////////////////////////////////////////////////

        // 도시 룰렛 메인 화면
        composable(
            route = "${RouletteScreenName.ROULETTE_CITY_SCREEN.name}?" +
                    "scheduleTitle={scheduleTitle}&scheduleStartDate={scheduleStartDate}&scheduleEndDate={scheduleEndDate}",
            arguments = listOf(
                navArgument("scheduleTitle") { type = NavType.StringType },
                navArgument("scheduleStartDate") { type = NavType.LongType },
                navArgument("scheduleEndDate") { type = NavType.LongType }
            )
        ) {
            val scheduleTitle = it.arguments?.getString("scheduleTitle") ?: ""
            val startTimestamp = it.arguments?.getLong("scheduleStartDate") ?: 0L
            val endTimestamp = it.arguments?.getLong("scheduleEndDate") ?: 0L

            val startDate = Timestamp(startTimestamp, 0)
            val endDate = Timestamp(endTimestamp, 0)

            RouletteCityScreen(scheduleTitle, startDate, endDate)
        }

        // 룰렛 도시 항목 추가 화면
        composable(RouletteScreenName.ROULETTE_CITY_SELECT_SCREEN.name) { RouletteCitySelectScreen(navController = rememberNavHostController) }

        ////////////////////////////////////////////////////////////////////////////////////////////

    }
}



