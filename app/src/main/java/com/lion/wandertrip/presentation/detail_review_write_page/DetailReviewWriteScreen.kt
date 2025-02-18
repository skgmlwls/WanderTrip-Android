package com.lion.wandertrip.presentation.detail_review_write_page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.a02_boardcloneproject.component.CustomIconButton
import com.lion.a02_boardcloneproject.component.CustomTopAppBar
import com.lion.wandertrip.R
import com.lion.wandertrip.component.CustomDraggableRatingBar
import com.lion.wandertrip.presentation.detail_page.DetailViewModel

@Composable
fun DetailReviewWriteScreen(contentID : String, contentTitle:String, detailReviewWriteViewModel: DetailReviewWriteViewModel = hiltViewModel()) {
    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = contentTitle,
                menuItems = {
                    // 작성 완료 아이콘
                    CustomIconButton(
                        ImageVector.vectorResource(R.drawable.ic_check_24px),
                        iconButtonOnClick = {
                            detailReviewWriteViewModel.onClickNavIconBack()
                        }
                    )
                },
                navigationIconImage = Icons.AutoMirrored.Filled.ArrowBack,
                // 뒤로가기 버튼
                navigationIconOnClick = {
                    detailReviewWriteViewModel.onClickNavIconBack()
                }
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it).fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomDraggableRatingBar(ratingState = detailReviewWriteViewModel.ratingScoreValue) {
                }

            }

        }

    }
}