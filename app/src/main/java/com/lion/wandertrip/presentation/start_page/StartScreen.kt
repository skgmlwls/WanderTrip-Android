package com.lion.wandertrip.presentation.start_page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.wandertrip.presentation.start_page.used_dummy_data.RecentDummyData

@Composable
fun StartScreen(startViewModel: StartViewModel = hiltViewModel()){

    startViewModel.autoLoginProcess()

    if(startViewModel.showLoginMessageState.value) {
        Scaffold {
            Row(
                modifier = Modifier.fillMaxSize().padding(it).padding(start = 10.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "로그인 중입니다",
                )
            }
        }
    }

    if(startViewModel.showKakaoLoginMessageState.value) {
        Scaffold {
            Row(
                modifier = Modifier.fillMaxSize().padding(it).padding(start = 10.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "카카오 로그인 중입니다",
                )
            }
        }
    }
}