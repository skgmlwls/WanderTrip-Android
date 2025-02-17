package com.lion.wandertrip.presentation.detail_page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.a02_boardcloneproject.component.CustomIconButton
import com.lion.a02_boardcloneproject.component.CustomTopAppBar
import com.lion.wandertrip.R
import com.lion.wandertrip.presentation.detail_page.components.IndicatorButton
import com.lion.wandertrip.presentation.detail_page.components.IntroColumn


@Composable
fun DetailScreen(contentID :String , detailViewModel: DetailViewModel = hiltViewModel()) {
    val scrollState = rememberScrollState()
    detailViewModel.getContentModel()
    Scaffold(
        topBar = {
            CustomTopAppBar(
                menuItems = {
                    CustomIconButton(
                        ImageVector.vectorResource(R.drawable.ic_location_on_24px),
                        iconButtonOnClick = {
                        }
                    )
                    CustomIconButton(
                        ImageVector.vectorResource(R.drawable.ic_menu_24px),
                        iconButtonOnClick = {
                        }
                    )
                },
                navigationIconImage = Icons.AutoMirrored.Filled.ArrowBack,
                navigationIconOnClick = {
                    detailViewModel.onClickNavIconBack()
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(it)
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally // 가로 정렬 중앙
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IndicatorButton({ detailViewModel.onClickButtonIntro() }, "소개", ImageVector.vectorResource(R.drawable.ic_indicator_24px),detailViewModel.isClickIntroState)
                IndicatorButton({detailViewModel.onClickButtonBasicInfo()}, "기본정보", ImageVector.vectorResource(R.drawable.ic_indicator_24px),detailViewModel.isClickBasicInfoState)
                IndicatorButton({detailViewModel.onClickButtonReview()}, "후기", ImageVector.vectorResource(R.drawable.ic_indicator_24px),detailViewModel.isClickReviewState)
            }
            if(detailViewModel.isClickIntroState.value)
            IntroColumn(detailViewModel)
            Spacer(modifier = Modifier.height(32.dp)) // 간격

        }
    }
}