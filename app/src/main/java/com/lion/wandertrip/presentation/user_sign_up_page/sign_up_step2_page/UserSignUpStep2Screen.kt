package com.lion.wandertrip.presentation.user_sign_up_page.sign_up_step2_page

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.a02_boardcloneproject.component.CustomIconButton
import com.lion.a02_boardcloneproject.component.CustomTopAppBar
import com.lion.wandertrip.R
import com.lion.wandertrip.component.BlueButton
@Composable
fun UserSignUpStep2Screen(userSignUpStep2ViewModel: UserSignUpStep2ViewModel = hiltViewModel()) {
    Scaffold(
        topBar = {
            CustomTopAppBar(
                menuItems = {
                    CustomIconButton(
                        icon = ImageVector.vectorResource(R.drawable.ic_check_24px),
                        iconButtonOnClick = {
                            // 다음 화면으로 넘어감
                            userSignUpStep2ViewModel.onClickIconCheck()
                        }
                    )
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 30.dp),
            verticalArrangement = Arrangement.SpaceBetween, // 위, 아래로 배치 균형 조정
            horizontalAlignment = Alignment.CenterHorizontally // 가로 정렬 중앙
        ) {
            Spacer(modifier = Modifier.height(40.dp)) // 상단 여백

            // 프로필 사진 선택 안내 텍스트
            Text(
                text = "프로필 사진 고르기",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            // 프로필 이미지 (원형)
            Image(
                painter = painterResource(id = R.drawable.ic_person_24px),
                contentDescription = "프로필 이미지",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
            )

            // 버튼 섹션
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp), // 버튼 간격 조정
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BlueButton(text = "카메라로 촬영") {
                    // 카메라 촬영 동작 추가
                }

                BlueButton(text = "갤러리에서 가져오기") {
                    // 갤러리에서 이미지 선택 동작 추가
                }

                BlueButton(text = "건너뛰기") {
                    // 건너뛰기 동작 추가
                    userSignUpStep2ViewModel.onClickButtonPass()
                }
            }

            Spacer(modifier = Modifier.height(40.dp)) // 하단 여백
        }
    }
}
