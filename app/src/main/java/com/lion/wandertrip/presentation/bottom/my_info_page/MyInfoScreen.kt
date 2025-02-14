package com.lion.wandertrip.presentation.bottom.my_info_page

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.wandertrip.presentation.bottom.my_info_page.components.HorizontalRecentPostsList
import com.lion.wandertrip.presentation.bottom.my_info_page.components.HorizontalScheduleList
import com.lion.wandertrip.presentation.bottom.my_info_page.components.ProfileCard
import com.lion.wandertrip.presentation.bottom.my_info_page.used_dummy_data.RecentPostsDummyData
import com.lion.wandertrip.presentation.bottom.my_info_page.used_dummy_data.ScheduleDummyData
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun MyInfoScreen(myInfoViewModel: MyInfoViewModel = hiltViewModel()) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,  // 콘텐츠를 상단에 배치
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 프로필 카드뷰 추가
            ProfileCard(
                userNickName = "홍길동",
                profileImage = null, // 프로필 이미지 URL 또는 "NONE"
                viewModel = myInfoViewModel
            )

            Spacer(modifier = Modifier.height(16.dp))  // 프로필 카드와 일정 리스트 사이 간격

            // 일정 리스트
            HorizontalScheduleList(ScheduleDummyData.scheduleDummyDataList)

            // 최근 게시글 리스트
            HorizontalRecentPostsList(RecentPostsDummyData.RecentPostsDummyDataList)


        }
    }
}