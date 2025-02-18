package com.lion.wandertrip.presentation.detail_page

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.a02_boardcloneproject.component.CustomIconButton
import com.lion.a02_boardcloneproject.component.CustomTopAppBar
import com.lion.wandertrip.R
import com.lion.wandertrip.presentation.detail_page.components.BasicInfoDescriptionColumn
import com.lion.wandertrip.presentation.detail_page.components.IndicatorButton
import com.lion.wandertrip.presentation.detail_page.components.IntroColumn
import com.lion.wandertrip.presentation.detail_page.components.ReviewLazyColumn
import com.lion.wandertrip.presentation.detail_page.components.ViewGoogleMap
import com.lion.wandertrip.util.CustomFont


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DetailScreen(contentID: String, detailViewModel: DetailViewModel = hiltViewModel()) {
    detailViewModel.getContentModel()
    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "상세페이지",
                menuItems = {
                    // 위치 아이콘
                    CustomIconButton(
                        ImageVector.vectorResource(R.drawable.ic_location_on_24px),
                        iconButtonOnClick = {
                            detailViewModel.onClickIconMap()
                        }
                    )
                    // 일정 추가 아이콘
                    CustomIconButton(
                        ImageVector.vectorResource(R.drawable.ic_calendar_add_on_24px),
                        iconButtonOnClick = {
                            detailViewModel.onClickIconAddSchedule()
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally // 가로 정렬 중앙
        ) {
            // 스크롤 시 고정되는 헤더
            stickyHeader {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White) // 배경 색상 설정
                        .zIndex(1f) // zIndex를 주어 다른 아이템 위로 고정
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        IndicatorButton(
                            { detailViewModel.onClickButtonIntro() },
                            "소개",
                            ImageVector.vectorResource(R.drawable.ic_indicator_24px),
                            detailViewModel.isClickIntroState
                        )
                        IndicatorButton(
                            { detailViewModel.onClickButtonBasicInfo() },
                            "기본정보",
                            ImageVector.vectorResource(R.drawable.ic_indicator_24px),
                            detailViewModel.isClickBasicInfoState
                        )
                        IndicatorButton(
                            { detailViewModel.onClickButtonReview() },
                            "후기",
                            ImageVector.vectorResource(R.drawable.ic_indicator_24px),
                            detailViewModel.isClickReviewState
                        )
                    }
                }
            }


            if (detailViewModel.isClickIntroState.value)
                item { IntroColumn(detailViewModel) }
            if (detailViewModel.isClickBasicInfoState.value)
                item {
                    Column {
                        Text("기본정보", fontSize = 30.sp, fontFamily = CustomFont.customFontBold)
                        Spacer(modifier = Modifier.height(32.dp)) // 간격
                        ViewGoogleMap(detailViewModel)
                        BasicInfoDescriptionColumn(detailViewModel)
                    }
                }

            if (detailViewModel.isClickReviewState.value)
                item {
                    ReviewLazyColumn(detailViewModel)
                }

            item {
                Spacer(modifier = Modifier.height(32.dp)) // 간격
            }
        }

        // 리뷰 필터 BottomSheet가 표시될 때의 설정
        if (detailViewModel.isReviewOptionSheetOpen.value) {
            ModalBottomSheet(
                onDismissRequest = { detailViewModel.isReviewOptionSheetOpen.value = false }
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                    Text(
                        "정렬", modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        fontFamily = CustomFont.customFontBold,
                        fontSize = 24.sp
                    )
                    Text(
                        "최신순", modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable {
                                detailViewModel.onClickTextRecentFilter()
                                detailViewModel.isReviewOptionSheetOpen.value = false
                            },
                        fontFamily = CustomFont.customFontRegular,
                        fontSize = 16.sp
                    )
                    Text(
                        "별점 낮은순", modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable {
                                detailViewModel.onClickTextRatingAsc()
                                detailViewModel.isReviewOptionSheetOpen.value = false
                            },
                        fontFamily = CustomFont.customFontRegular,
                        fontSize = 16.sp
                    )
                    Text(
                        "별점  높은순", modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable {
                                detailViewModel.onClickTextRatingDesc()
                                detailViewModel.isReviewOptionSheetOpen.value = false
                            },
                        fontFamily = CustomFont.customFontRegular,
                        fontSize = 16.sp
                    )
                }
            }
        }

        // 일정 추가 BottomSheet가 표시될 때의 설정
        if (detailViewModel.isAddScheduleSheetOpen.value) {
            ModalBottomSheet(
                onDismissRequest = { detailViewModel.isAddScheduleSheetOpen.value = false }
            ) {
                LazyColumn {
                    item {
                        Text("내 여행")
                    }
                }
            }
        }

    }
}