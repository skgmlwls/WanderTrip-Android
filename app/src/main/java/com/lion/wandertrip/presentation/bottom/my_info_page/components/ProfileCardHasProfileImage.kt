package com.lion.wandertrip.presentation.bottom.my_info_page.components

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lion.wandertrip.R
import com.lion.wandertrip.presentation.bottom.my_info_page.MyInfoViewModel
import com.lion.wandertrip.util.CustomFont
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ProfileCardHasProfileImage(
    userNickName: String,
    viewModel: MyInfoViewModel,
    imageUri : Uri
) {
    // 카드뷰
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
       // colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA)) //

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
                            text = "유저 정보 수정",
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
                GlideImage(
                    imageModel = imageUri,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(60.dp)
                        .height(60.dp)
                        .clip(RoundedCornerShape(8.dp)),  // 이미지 둥글게 만들기
                    circularReveal = CircularReveal(duration = 250),
                    placeHolder = ImageBitmap.imageResource(R.drawable.img_image_holder),
                )
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        // 아이콘 + 텍스트 Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ColumnIconAndText(
                Icons.Default.FlightTakeoff,
                "내 여행",
                { viewModel.onClickIconMyTrip() })
            ColumnIconAndText(
                Icons.Default.Bookmark,
                "내 저장",
                { viewModel.onClickIconMyInteresting() })
            ColumnIconAndText(Icons.Default.Star, "내 리뷰", { viewModel.onClickIconMyReview() })
            ColumnIconAndText(Icons.Default.Edit, "내 여행기", { viewModel.onClickIconTripNote() })
        }
    }
}
