package com.lion.wandertrip.presentation.detail_page.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lion.a02_boardcloneproject.component.CustomDividerComponent
import com.lion.a02_boardcloneproject.component.CustomIconButton
import com.lion.wandertrip.R
import com.lion.wandertrip.component.CustomRatingBar
import com.lion.wandertrip.model.ReviewModel
import com.lion.wandertrip.presentation.detail_page.DetailViewModel
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun VerticalReviewList(detailViewModel: DetailViewModel) {
    val sh = detailViewModel.tripApplication.screenHeight

    // 레이지 컬럼 크기를 제한해 컬럼이 무한대로 늘어나지 않도록 함
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height((sh).dp)
    ) {
        items(detailViewModel.filteredReviewList) {
            Log.d("test100", "it : $it")
            ReviewItem(it, detailViewModel, pos = detailViewModel.filteredReviewList.indexOf(it))
            Spacer(modifier = Modifier.height(8.dp))
            CustomDividerComponent(3.dp)
        }
    }
}

@Composable
fun ReviewItem(reviewModel: ReviewModel, detailViewModel: DetailViewModel, pos: Int) {
    val sh = detailViewModel.tripApplication.screenHeight
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // 등록자 이미지 넣어야 함
            GlideImage(
                imageModel = R.drawable.img_basic_profile_image,
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
                Text(text = "김지연", fontWeight = FontWeight.Bold)
                Text(text = "20개의 리뷰", color = Color.Gray)
            }
        }
        // 수정, 삭제 아이콘
        Row {
            //수정
            CustomIconButton(
                ImageVector.vectorResource(R.drawable.ic_edit_24px),
                iconButtonOnClick = {})
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
    Text(
        text = reviewModel.reviewContent,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,

    )
    // 전체 리뷰 내용
    Text(
        text = reviewModel.reviewContent,
    )

    Spacer(modifier = Modifier.height(8.dp))

    // 이미지 가로 스크롤
    LazyRow {
        items(reviewModel.reviewImageList.size) {
            GlideImage(
                imageModel = reviewModel.reviewImageList[it],
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height((sh / 10).dp)
                    .clip(RoundedCornerShape(8.dp)),  // 이미지 둥글게 만들기
                circularReveal = CircularReveal(duration = 250),
                placeHolder = ImageBitmap.imageResource(R.drawable.img_image_holder),
            )
            Spacer(modifier = Modifier.width(8.dp))
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