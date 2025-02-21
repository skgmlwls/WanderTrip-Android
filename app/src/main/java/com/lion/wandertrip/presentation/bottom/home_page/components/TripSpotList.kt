package com.lion.wandertrip.presentation.bottom.home_page.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.lion.a02_boardcloneproject.component.CustomDividerComponent
import com.lion.wandertrip.R
import com.lion.wandertrip.model.TripItemModel
import com.lion.wandertrip.ui.theme.NanumSquareRound
import com.lion.wandertrip.ui.theme.NanumSquareRoundRegular
import com.lion.wandertrip.util.AccommodationItemCat3.Companion.fromCodeAccommodationItemCat3
import com.lion.wandertrip.util.ContentTypeId
import com.lion.wandertrip.util.RestaurantItemCat3.Companion.fromCodeRestaurantItemCat3
import com.lion.wandertrip.util.TripItemCat2.Companion.fromCodeTripItemCat2

@Composable
fun TravelSpotList(tripItemList : List<TripItemModel>,
                   onItemClick: (TripItemModel) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(tripItemList.size) { index ->

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(tripItemList[index]) } // ✅ 클릭 이벤트 추가
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, top = 20.dp, bottom = 8.dp), // ✅ 클릭 이벤트 추가,
                    verticalAlignment = Alignment.Top
                ) {

                    Column(
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .weight(1f) // ✅ 너비 조정
                    ) {
                        // ✅ 제목 텍스트
                        Text(
                            text = tripItemList[index].title,
                            fontSize = 20.sp,
                            fontFamily = NanumSquareRound,
                            maxLines = 1, // ✅ 최대 1줄까지 표시
                            overflow = TextOverflow.Ellipsis, // ✅ 너무 길면 "..." 표시
                            modifier = Modifier.padding(bottom = 5.dp)
                        )

                        // ✅ 카테고리 텍스트
                        Text(
                            text = when(tripItemList[index].contentTypeId){
                                ContentTypeId.TOURIST_ATTRACTION.contentTypeCode.toString() ->{
                                    "관광지·" +
                                            fromCodeTripItemCat2(tripItemList[index].cat2)!!.catName
                                }
                                ContentTypeId.RESTAURANT.contentTypeCode.toString() -> {
                                    "음식점·" +
                                            fromCodeRestaurantItemCat3(tripItemList[index].cat3)!!.catName
                                }
                                ContentTypeId.ACCOMMODATION.contentTypeCode.toString() -> {
                                    "숙소·" +
                                            fromCodeAccommodationItemCat3(tripItemList[index].cat3)!!.catName
                                }
                                else -> { "" }
                            },
                            fontSize = 12.sp,
                            fontFamily = NanumSquareRound,
                            maxLines = 3, // ✅ 최대 2줄까지 표시
                            overflow = TextOverflow.Ellipsis, // ✅ 너무 길면 "..." 표시
                            lineHeight = 14.sp, // ✅ 줄 간격을 조정 (기본값보다 약간 좁게)
                            modifier = Modifier.padding(bottom = 5.dp)
                        )

                        // ✅ 주소 텍스트
                        Text(
                            text = tripItemList[index].addr1 + tripItemList[index].addr2,
                            fontSize = 12.sp,
                            fontFamily = NanumSquareRoundRegular,
                            maxLines = 3, // ✅ 최대 2줄까지 표시
                            overflow = TextOverflow.Ellipsis, // ✅ 너무 길면 "..." 표시
                            lineHeight = 14.sp // ✅ 줄 간격을 조정 (기본값보다 약간 좁게)
                        )

                    }

                    // ✅ 이미지 표시
                    Image(
                        // ✅ 이미지가 없을 경우 기본 이미지 사용
                        painter = if (tripItemList[index].firstImage.isEmpty()) {
                            painterResource(id = R.drawable.ic_hide_image_144dp) // ✅ 기본 이미지 사용
                        } else {
                            rememberAsyncImagePainter(model = tripItemList[index].firstImage)
                        },
                        contentDescription = "Trip Image",
                        modifier = Modifier
                            .size(width = 140.dp, height = 120.dp) // ✅ 이미지 크기 조정
                            .padding(end = 8.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop, // ✅ 이미지 크기 자동 맞춤
                    )
                }

                CustomDividerComponent(10.dp)
            }

        }
    }

}