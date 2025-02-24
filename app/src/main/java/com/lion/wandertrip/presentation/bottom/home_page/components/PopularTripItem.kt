package com.lion.wandertrip.presentation.bottom.home_page.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.lion.wandertrip.model.SimpleTripItemModel
import com.lion.wandertrip.util.ContentTypeId

@Composable
fun PopularTripItem(
    tripItem: SimpleTripItemModel,
    onItemClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            // 대표 이미지
            AsyncImage(
                model = tripItem.contentSmallImageUri,
                contentDescription = tripItem.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                // 타이틀
                Text(
                    text = tripItem.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // contentTypeID -> 텍스트 변환
                val contentTypeText = when (tripItem.contentTypeID) {
                    ContentTypeId.TOURIST_ATTRACTION -> "관광지"
                    ContentTypeId.RESTAURANT -> "맛집"
                    ContentTypeId.ACCOMMODATION -> "숙소"
                }
                Text(
                    text = "카테고리: $contentTypeText",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 지역 코드 / 시군구 코드
                Text(
                    text = "지역코드: ${tripItem.areaCode}, 시군구: ${tripItem.siGunGuCode}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 세부 카테고리 (cat2, cat3)
                if (tripItem.cat2.isNotBlank() || tripItem.cat3.isNotBlank()) {
                    Text(
                        text = "세부정보: ${tripItem.cat2} / ${tripItem.cat3}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}