package com.lion.wandertrip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bottomnav.BottomNavItem
import com.example.bottomnav.BottomNavigation
import com.lion.wandertrip.screen.test.TestScreen
import com.lion.wandertrip.screen.test2.Test2Screen
import com.lion.wandertrip.ui.theme.WanderTripTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WanderTripTheme {
                TripMain()
            }
        }
    }
}




@Composable
fun TripMain() {

    // 충돌해라
    // 네비게이션 객체
    // Composable 함수가 재구성(recompose)되더라도 NavController의 인스턴스가 유지됨 (즉, 화면 회전이나 재구성이 발생해도 동일한 인스턴스를 사용).
    val rememberNavHostController = rememberNavController()
    // Appilcation 객체에 담는다.
    // Jetpack Compose에서 LocalContext.current는 현재 Composable 함수가 실행되는 컨텍스트(Context)를 가져오는 방법입니다.
    // applicationContext의 기본 타입은 Context이므로, 우리가 만든 TripApplication 클래스의 인스턴스로 사용하려면 형변환이 필요합니다.
    val tripApplication = LocalContext.current.applicationContext as TripApplication
    tripApplication.navHostController = rememberNavHostController

    val items: List<BottomNavItem> = listOf(
        BottomNavItem("홈", Icons.Filled.Home, "TESTSCREEN"),
        BottomNavItem("일정", Icons.Filled.CalendarMonth, "TEST2SCREEN"),
        BottomNavItem("여행기", Icons.Filled.EditCalendar, "travels"),
        BottomNavItem("내정보", Icons.Filled.PersonOutline, "TEST2SCREEN") // ✅ "내정보" 클릭 시 Test2Screen으로 이동
    )

    // 현재 화면의 route 확인
    val currentRoute = rememberNavHostController.currentBackStackEntryAsState().value?.destination?.route

    // 네비게이션 처리
    Scaffold(
        bottomBar = {  // ✅ 모든 화면에서 공통으로 유지
            BottomNavigation(navController = rememberNavHostController, items = items)
        }
    ) { innerPadding ->
        NavHost(
            navController = rememberNavHostController,
            startDestination = "TESTSCREEN",
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                fadeIn(tween(300)) +
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Start,
                            tween(300)
                        )
            },
            popExitTransition = {
                fadeOut(tween(300)) +
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.End,
                            tween(300)
                        )
            },
            exitTransition = { fadeOut(tween(300)) },
            popEnterTransition = { fadeIn(tween(300)) },
        ) {
            composable(route = "TESTSCREEN") {
                TestScreen(navController = rememberNavHostController)
            }
            composable(route = "TEST2SCREEN") {  // ✅ "내정보" 클릭 시 이동할 경로 추가
                Test2Screen(navController = rememberNavHostController)
            }
        }
    }

}



