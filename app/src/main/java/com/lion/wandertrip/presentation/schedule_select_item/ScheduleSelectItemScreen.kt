package com.lion.wandertrip.presentation.schedule_select_item

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.a02_boardcloneproject.component.CustomTopAppBar
import com.lion.wandertrip.R
import com.lion.wandertrip.ui.theme.NanumSquareRound
import com.lion.wandertrip.ui.theme.NanumSquareRoundRegular
import com.lion.wandertrip.util.AreaCode
import com.lion.wandertrip.util.ContentTypeId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleSelectItemScreen(
    itemCode: Int,
    areaName: String,
    areaCode: Int,
    viewModel: ScheduleSelectItemViewModel = hiltViewModel()
) {
    Log.d("ScheduleSelectItemScreen", itemCode.toString())
    Log.d("ScheduleSelectItemScreen", areaName)
    Log.d("ScheduleSelectItemScreen", areaCode.toString())

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White // 흰색 적용
                ),
                title = {
                    val title = when (itemCode) {
                        ContentTypeId.TOURIST_ATTRACTION.contentTypeCode -> "관광지"
                        ContentTypeId.RESTAURANT.contentTypeCode -> "음식점"
                        ContentTypeId.ACCOMMODATION.contentTypeCode -> "숙소"
                        else -> ""
                    }
                    Text(text = "$title 추가하기", fontFamily = NanumSquareRound) // ✅ 제목 설정
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.backScreen() }
                    ) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "뒤로 가기")
                    }
                },
            )
        }

    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            Button(
                onClick = {

                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White, // ✅ 버튼 배경색: 흰색
                    contentColor = Color(0xFF435C8F) // ✅ 버튼 텍스트 색상: 파란색 (변경 가능)
                ),
                shape = RectangleShape // ✅ 버튼을 사각형으로 변경
            ) {
                Image(
                    painter = painterResource(id = R.drawable.roulette_picture), // ✅ drawable 리소스 추가
                    contentDescription = "룰렛 이미지",
                    modifier = Modifier.size(70.dp).padding(end = 16.dp) // ✅ 아이콘 크기 조정 가능
                )
                Text(
                    text = "룰렛 돌리기",
                    fontFamily = NanumSquareRoundRegular,
                    fontSize = 35.sp,
                    color = Color.Black
                )
            }
        }

    }
}