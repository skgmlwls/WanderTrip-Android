package com.lion.wandertrip.presentation.detail_page.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lion.a02_boardcloneproject.component.CustomDividerComponent
import com.lion.a02_boardcloneproject.component.CustomIconButton
import com.lion.wandertrip.R
import com.lion.wandertrip.component.CustomRatingBar
import com.lion.wandertrip.component.LottieLoadingIndicator
import com.lion.wandertrip.model.ReviewModel
import com.lion.wandertrip.presentation.detail_page.DetailViewModel
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun VerticalReviewList(detailViewModel: DetailViewModel) {
    val sh = detailViewModel.tripApplication.screenHeight

    /*  LaunchedEffect(Unit) {
          Log.d("filterReview","fiterLivew : ${detailViewModel.filteredReviewList.joinToString()  }}")
          detailViewModel.setState() // 로딩 상태를 true로 변경
      }*/

    // setState() 호출 후, 로딩 상태 변경 시 UI를 다시 갱신하도록 LaunchedEffect 사용
    LaunchedEffect(detailViewModel.isLoading.value) {
        // 로딩 상태 변경 시 Lottie 로딩 화면 보여주기
        if (detailViewModel.isLoading.value) {
        } else {
            Log.d("test100", "로딩 끝")
        }
    }

    // 필터된 리뷰 리스트가 변경될 때 getUri() 호출
    LaunchedEffect(detailViewModel.filteredReviewList) {
        if (detailViewModel.filteredReviewList.isNotEmpty()) {
            detailViewModel.getUri(detailViewModel.filteredReviewList)
        }
    }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height((sh).dp)
        ) {
            items(detailViewModel.filteredReviewList) {
                ReviewItem(
                    it,
                    detailViewModel,
                    pos = detailViewModel.filteredReviewList.indexOf(it)
                )
                Spacer(modifier = Modifier.height(8.dp))
                CustomDividerComponent(3.dp)
            }
        }
    }


@Composable
fun ReviewItem(reviewModel: ReviewModel, detailViewModel: DetailViewModel, pos: Int) {
    val sh = detailViewModel.tripApplication.screenHeight
    if (detailViewModel.reviewImageUrlMap.size != 0)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // 등록자 이미지 넣어야 함
                GlideImage(
                    imageModel = reviewModel.reviewWriterProfileImgURl,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(60.dp)
                        .height(60.dp)
                        .clip(RoundedCornerShape(8.dp)),  // 이미지 둥글게 만들기
                    circularReveal = CircularReveal(duration = 250),
                    placeHolder = ImageBitmap.imageResource(R.drawable.img_image_holder),
                )
                Spacer(modifier = Modifier.width(8.dp))
                // 등록자 이름, 리뷰 개수
                Column {
                    Text(text = reviewModel.reviewWriterNickname, fontWeight = FontWeight.Bold)
                    Text(text = "20개의 리뷰", color = Color.Gray)
                }
            }
            // 수정, 삭제 아이콘
            if (reviewModel.reviewWriterNickname == detailViewModel.tripApplication.loginUserModel.userNickName)
                Row {
                    //수정
                    CustomIconButton(
                        ImageVector.vectorResource(R.drawable.ic_edit_24px),
                        iconButtonOnClick = {
                            detailViewModel.onClickIconReviewModify(
                                reviewModel.contentsDocId,
                                reviewModel.reviewDocId
                            )
                        })
                    Spacer(modifier = Modifier.width(8.dp))
                    // 삭제
                    CustomIconButton(
                        ImageVector.vectorResource(R.drawable.ic_delete_24px),
                        iconButtonOnClick = {})

                }
        }

    Spacer(modifier = Modifier.height(8.dp))

    // 별점 및 여행 날짜
    Row(verticalAlignment = Alignment.CenterVertically) {
        CustomRatingBar(reviewModel.reviewRatingScore)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            "${detailViewModel.convertToMonthDate(reviewModel.reviewTimeStamp)} 여행",
            color = Color.Gray
        )
    }

    Spacer(modifier = Modifier.height(8.dp))

    // 전체 리뷰 내용
    Text(
        text = reviewModel.reviewContent,
        modifier = Modifier.heightIn(100.dp)
    )

    Spacer(modifier = Modifier.height(8.dp))

    // 이미지 가로 스크롤
    LazyRow {
        val imageList = detailViewModel.reviewImageUrlMap[pos]

        if (!imageList.isNullOrEmpty()) {
            items(imageList.toString().length) { index ->
                GlideImage(
                    imageModel = imageList[index],
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((sh / 10).dp)
                        .clip(RoundedCornerShape(8.dp)),
                    circularReveal = CircularReveal(duration = 250),
                    placeHolder = ImageBitmap.imageResource(R.drawable.img_image_holder),
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
        } else {
            // 이미지가 없는 경우 처리
            Log.e("ReviewItem", "이미지가 없거나 리스트가 비어 있음")
        }
    }
    Spacer(modifier = Modifier.height(8.dp))

    // 좋아요 및 댓글 아이콘, 날짜 및 메뉴 아이콘
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Icon(Icons.Default.FavoriteBorder, contentDescription = "좋아요")
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "3")
            Spacer(modifier = Modifier.width(16.dp))
            Icon(Icons.Default.ChatBubbleOutline, contentDescription = "채팅")
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "5")
        }

        // 날짜
        Text(text = detailViewModel.convertToDate(reviewModel.reviewTimeStamp), color = Color.Gray)

    }
}