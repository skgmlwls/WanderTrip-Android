package com.lion.wandertrip.presentation.bottom.my_info_page.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lion.a02_boardcloneproject.component.CustomIconButton
import com.lion.wandertrip.R
import com.lion.wandertrip.presentation.bottom.my_info_page.MyInfoViewModel
import com.lion.wandertrip.util.CustomFont


@Composable
fun ProfileCard(
    userNickName: String,
    profileImage: Painter?,
    viewModel : MyInfoViewModel
) {

    val defaultImage = painterResource(id = R.drawable.img_basic_profile_image) // 기본 프로필 이미지 설정
    val profilePainter = profileImage ?: defaultImage  // NULL 체크 후 기본 이미지 사용

    // 카드뷰
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 상단 영역 (닉네임 + 프로필 편집 버튼 + 프로필 이미지)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp), // 바닥만 30dp 패딩 적용
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    // 닉네임
                    Text(
                        text = userNickName,
                        fontSize = 20.sp,
                        fontFamily = CustomFont.customFontRegular,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            // 편집화면 전환
                            viewModel.onClickTextUserInfoModify()
                        }
                    ) {
                        Text(
                            text = "프로필 편집",
                            fontSize = 14.sp,
                            fontFamily = CustomFont.customFontBold
                        )
                        // 편집 아이콘
                        Icon(
                            painter = painterResource(id = R.drawable.ic_small_arrow_forward_24px),// 뒤로가기 아이콘
                            contentDescription = "Back",
                            modifier = Modifier.size(24.dp)
                        )

                    }
                }

                // 프로필 이미지 (우측 상단)
                Image(
                    painter = profilePainter,
                    contentDescription = "프로필 이미지",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)

                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 아이콘 + 텍스트 Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileMenuItem(Icons.Default.FlightTakeoff, "내 여행", {viewModel.onClickIconMyTrip()})
                ProfileMenuItem(Icons.Default.Bookmark, "내 저장", {viewModel.onClickIconMyInteresting()})
                ProfileMenuItem(Icons.Default.Star, "내 리뷰",{viewModel.onClickIconMyReview()})
                ProfileMenuItem(Icons.Default.Edit, "내 여행기",{viewModel.onClickIconTripNote()})
            }
        }
    }
}

@Composable
fun ProfileMenuItem(icon: ImageVector, text: String, onClick: () -> Unit = {}  ) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable {
            onClick()
        }
    ) {
        Icon(imageVector = icon, contentDescription = text, modifier = Modifier.size(24.dp))
        Text(
            text, fontSize = 12.sp, fontFamily = CustomFont.customFontBold
        )
    }
}