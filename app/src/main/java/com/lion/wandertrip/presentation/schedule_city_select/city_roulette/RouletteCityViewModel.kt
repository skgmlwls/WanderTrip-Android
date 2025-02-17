package com.lion.wandertrip.presentation.schedule_city_select.city_roulette

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.util.AreaCode
import com.lion.wandertrip.util.BotNavScreenName
import com.lion.wandertrip.util.RouletteScreenName
import com.lion.wandertrip.util.ScheduleScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class RouletteCityViewModel @Inject constructor(
    @ApplicationContext val context: Context,
) : ViewModel() {
    val application = context as TripApplication

    var cities = mutableStateListOf<AreaCode>()

    // 룰렛의 도시 항목 추가 화면 으로 이동
    fun moveToRouletteCitySelectScreen() {
        application.navHostController.navigate(RouletteScreenName.ROULETTE_CITY_SELECT_SCREEN.name)
    }

    // 일정 상세 화면 으로 이동
    fun moveToScheduleDetailScreen(areaName: String) {
        // 도시 이름 으로 도시 코드 찾기
        val areaCode = AreaCode.entries.find { it.areaName == areaName }?.areaCode

        // 일정 상세 화면 이동
        application.navHostController.navigate(
            "${ScheduleScreenName.SCHEDULE_DETAIL_SCREEN.name}?" +
                    "areaName=${areaName}" +
                    "&areaCode=${areaCode}"
        ) {
            popUpTo(BotNavScreenName.BOT_NAV_SCREEN_HOME.name) { inclusive = false }
            launchSingleTop = true
        }
    }

}