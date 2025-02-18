package com.lion.wandertrip.presentation.schedule_detail_friends.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp
import com.lion.wandertrip.ui.theme.NanumSquareRound
import com.lion.wandertrip.ui.theme.NanumSquareRoundRegular

@Composable
fun ScheduleDetailFriendsList(
    friends: List<String>,      // 예: 친구 이름 목록
    profileImageUrl: String     // 예: 공통 프로필 URL 또는 각 친구별 URL을 사용하고 싶다면 데이터 클래스로 관리
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(friends) { friend ->
            Card(
                colors = CardDefaults.cardColors(Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 원형 프로필 이미지
                    AsyncImage(
                        model = profileImageUrl,
                        contentDescription = "Friend Profile Image",
                        contentScale = ContentScale.Crop,      // 이미지가 잘리지 않고 원형 영역에 꽉 차도록 설정
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .size(48.dp)                      // 이미지 크기
                            .clip(CircleShape)                 // 원형으로 만들기
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    // 친구 이름
                    Text(
                        text = friend,
                        style = MaterialTheme.typography.bodyLarge,
                        fontFamily = NanumSquareRound,
                        fontSize = 18.sp,
                        modifier = Modifier.weight(1f)
                    )

                    // 더보기(메뉴) 아이콘
                    IconButton(onClick = {  }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "더보기 메뉴"
                        )
                    }
                }
            }
        }
    }
}
